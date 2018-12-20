package com.yootii.bdy.task.service.Impl;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.constraints.Null;

import com.sun.corba.se.spi.ior.ObjectKey;
import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.LoginReturnInfo;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.common.TrademarkProcessStatus;
import com.yootii.bdy.model.User;
import com.yootii.bdy.security.service.AuthenticationService;
import com.yootii.bdy.task.dao.TracdemarkTaskMapper;
import com.yootii.bdy.task.model.MailInfor;
import com.yootii.bdy.task.model.Notification;
import com.yootii.bdy.task.service.TracdemarkTaskService;
import com.yootii.bdy.trademark.model.Trademark;
import com.yootii.bdy.util.DateTool;
import com.yootii.bdy.util.GraspUtil;
import com.yootii.bdy.util.JsonUtil;
import com.yootii.bdy.util.ServiceUrlConfig;

@Service("tracdemarkTaskService")
public class TracdemarkTaskServiceImpl implements TracdemarkTaskService {
	@Autowired
	private TracdemarkTaskMapper tracdemarkTaskMapper;
	@Resource	
	private ServiceUrlConfig serviceUrlConfig;
	@Resource AuthenticationService authenticationService;
	/**
	 * 提醒的商标：包括在宽展期内（有效期截止日<=提醒日期—0.5年）；
	 * 						在续展期内（有效期截止日<=提醒日期+1年）。
	 */
	@Override
	public void scheduling() {
		//模拟登陆
		User user=new User();
		user.setUsername("admin");
		user.setPassword("123456");		
	    Object obj = authenticationService.login(user);				
		LoginReturnInfo rtnInfo = (LoginReturnInfo)obj;		
		String tokenID = rtnInfo.getTokenID();		
		System.err.println("登陆成功！");
		//获取当前时间
		Date date = new Date();
		//提醒日期-半年
		Date beforeDate= DateTool.getDateBeforeMonth(date, 6);
		//提醒日期+1年
		Date afterDate = DateTool.getDateAfterYear(date, 1);
		//符合期限内的商标
		List<Trademark> trademarks = tracdemarkTaskMapper.selectTMByDate(beforeDate,afterDate);
		ArrayList<Notification> notifications= new ArrayList<Notification>();
		Long timestamp = System.currentTimeMillis();
		for(Trademark tm:trademarks){
			int tmId = tm.getTmId();
			String tmName  = tm.getTmName();
			Date validatEndDate = tm.getValidEndDate();
			HashMap<String, Object> map = new HashMap<>();
			map.put("tmId",tmId);
			map.put("tmName",tmName);
			map.put("validatEndDate",validatEndDate);
			int repeat = tracdemarkTaskMapper.isRepeat(map);
			//判断是否重复，重复就不再插入
			if(repeat==0){
				Notification notification = creat(tm,timestamp);
				notifications.add(notification);
			}
		}
		//插入通知   防止插入重复数据
		if(notifications.size()!=0){
			tracdemarkTaskMapper.insert(notifications);
		}
		//调用接口发送邮件；http://ip:port/bdy_automail/interface/sendrenewalremind
		Long begin = System.currentTimeMillis();
		//serviceUrlConfig.getMailServiceUrl()
		 String url=serviceUrlConfig.getMailServiceUrl()+"/mail/sendrenewalremind?"+"tokenID="+tokenID;
		 Long end = System.currentTimeMillis();
		 System.out.println("耗时"+(end-begin)/1000/60+"分钟");
		 try{
			 String jsonString = GraspUtil.getText(url);
			/* rutnInfo=JsonUtil.toObject(jsonString, ReturnInfo.class);
			 System.err.println(rutnInfo.getSuccess());*/
		 }catch (Exception e) {
			 e.printStackTrace();
		 }
		
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public ReturnInfo list(int time) {
		List<MailInfor> list =tracdemarkTaskMapper.selectNotificationList(time);
		List<Notification> infor =new ArrayList<>();
		List<Map<String, Object>> obj = new ArrayList<>();
		Map<String, Object> data = new HashMap<>(); 
		Map<String, Object> map = new HashMap<>();
		for(MailInfor mail:list){
			for(Notification notification:mail.getList()){
				//判断map中key是否已经有相同的值  如果相同邮箱，则添加到列表中，否则 存放进 key ,value同时存放,
				//按照邮箱地址进行分类
				if(map.containsKey(notification.getEmail())){
					List<Notification> noti =(List<Notification>)map.get(notification.getEmail());
					noti.add(notification);
				}else{
					infor.add(notification) ;
					map.put(notification.getEmail(), infor);
				};
			}
			//组织数据
			if(data.containsKey(mail.getUserId())){
				 List<Map> content=(List<Map>)data.get(mail.getUserId());
				 content.add(map);
			}else{
				obj.add(map);
				data.put(mail.getUserId(), obj);
			}
		}
		ReturnInfo returnInfo = new ReturnInfo();
		returnInfo.setData(data);
		returnInfo.setSuccess(true);
		returnInfo.setMessage("查询成功");
		return returnInfo;
	}

	@Override
	public ReturnInfo findTmList(Integer custId) {
		ReturnInfo returnInfo = new ReturnInfo();
		List<Map<String,Object>> data = new ArrayList<>();
		/*List<MailDTO> mailDTOs = tracdemarkTaskMapper.selectTmForMails();
		for(MailDTO mailDTO : mailDTOs){
			mailDTO.setSubject("续展通知");
			int custId = mailDTO.getCustId();
			Integer userId = tracdemarkTaskMapper.queryUserByCustId(custId);
			if(userId==null) {};
			Map<String, Object> map = new HashMap<>();
			map.put("userId", userId);
			map.put("mails",mailDTO);
			data.add(map);
		}*/
		
		data= tracdemarkTaskMapper.queryNotification(custId);
		String batchNo = (String)data.get(0).get("batchNo");
		Map<String, Object> map = new  HashMap<>();
		map.put("batchNo", batchNo);
		map.put("trademarks", data);
		returnInfo.setData(map);
		return returnInfo;
	}

	
	
	

	private Notification creat(Trademark trademark,Long timestamp){
		Notification notification = new Notification();
		
		notification.setTmId(trademark.getTmId());
		notification.setTmName(trademark.getTmName());
		notification.setTmDeadTime(trademark.getValidEndDate());
		notification.setBatchNo(timestamp+"");
		return notification;
	}


	@Override
	public ReturnInfo queryRenewalRemindList(GeneralCondition gcon,Integer custId) {
		ReturnInfo returnInfo = new  ReturnInfo();
		
		List<Map<String, Object>> list  = tracdemarkTaskMapper.queryRenewalRemindList(gcon,custId);
		Long count = tracdemarkTaskMapper.queryRenewalRemindListCount(gcon,custId);
		
		if(list!=null&&list.size()!=0){
			//遍历转格式
			for(Map<String, Object> map :list){
				Date ccDate = (Date)map.get("createTime");
				if(ccDate!=null){
					String createTime= DateTool.getDate(ccDate);
					map.put("createTime", createTime);
				}
				//转换
				Integer status = (Integer)map.get("mailStatus");
				if(status !=null){
					String mailStatus = "";
					switch (status) {
						case 1 :
							mailStatus = "成功";
							break;
						case 0 :
							mailStatus = "失败";
							break;
						default :
							break;
					}
					map.put("mailStatus", mailStatus);
				}
			}
		}
		returnInfo.setData(list);
		returnInfo.setTotal(count);
		
		return returnInfo;
	}


	/**
	 * 自动 定时创建商标异常 数据  每天定时执行
	 * 
	 * 被驳回的异常动态的符合要求的时间：小于当前时间，并且距今15天之内。
		其它的状态的时间：小于当前时间，并且距今6个月之内。
		1被驳回
		2被提起异议申请
		3被提起撤三申请
		4被提起无效宣告申请
		5被提起撤销通用名称申请
		1 .被驳回 2.被提起异议申请 3.被提起撤三申请  4.被提起无效宣告申请   5.被提起撤销通用名称申请 
	 */
	public void autoTimingCreateTmAbnormal(){
		
		Date now = new Date();
		//6个月前
		Date date = DateTool.getDateBeforeMonth(now, 6);
		Date date2 = DateTool.getDateBefore(now,15);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		//起始时间
		String beganDate = sdf.format(date);
		String endDate =sdf.format(now);
		//被驳回的起始时间
		String rejectBeganDate  =sdf.format(date2);
		//驳回状态
		String statusBeiBohui1 = TrademarkProcessStatus.beibohui_1.substring(1, 9);
		String statusBeiBohui2 = TrademarkProcessStatus.beibohui_2.substring(1, 13);
		String statusBeiBohui3 = TrademarkProcessStatus.beibohui_3.substring(1, 15);
		String statusBeiBohui4 = TrademarkProcessStatus.beibohui_4.substring(1, 13);
		String statusBeiBohui5 = TrademarkProcessStatus.beibohui_5.substring(1, 7);
		// 异议，撤三....
		String statusBeiYiyi = TrademarkProcessStatus.beiyiyi_1.substring(1, 8);
		String statusBeiChesan = TrademarkProcessStatus.beichesan.substring(1,10);
		String statusBeiWuxiao = TrademarkProcessStatus.beiwuxiao.substring(1, 6);
		String statusTongYongMingcheng = TrademarkProcessStatus.tongyongmingcheng.substring(1, 4);
		//封装参数
		Map<String, Object> map = new HashMap<>();
		map.put("status1", statusBeiBohui1);
		map.put("status2", statusBeiBohui2);
		map.put("status3", statusBeiBohui3);
		map.put("status4", statusBeiBohui4);
		map.put("status5", statusBeiBohui5);
		map.put("beganDate", rejectBeganDate);
		map.put("endDate", endDate);
		//被驳回的流程
		List<Map<String, Object>> tmProcessReject =tracdemarkTaskMapper.queryRejectTmProcess(map);
		// 分类查询6个月以来的异常列表 ，除了被驳回 类型  ，其它 的异常类型
		List<Map<String, Object>> tmProcessBeiYiyi = tracdemarkTaskMapper.queryTmProcess(beganDate, endDate,statusBeiYiyi);
		List<Map<String, Object>> tmProcessBeiChesan = tracdemarkTaskMapper.queryTmProcess(beganDate, endDate, statusBeiChesan);
		List<Map<String, Object>> tmProcessBeiWuxiao = tracdemarkTaskMapper.queryTmProcess(beganDate, endDate, statusBeiWuxiao);
		List<Map<String, Object>> tmProcessBeiTongYong  = tracdemarkTaskMapper.queryTmProcess(beganDate, endDate, statusTongYongMingcheng);
		
		//分类查询6个月以来 异常表中 的数据
		List<Map<String, Object>> tmAbnormalReject = tracdemarkTaskMapper.queryTmAbnormal(rejectBeganDate, endDate,1);
		List<Map<String, Object>> tmAbnormalBeiYiyi = tracdemarkTaskMapper.queryTmAbnormal(beganDate,endDate,2);
		List<Map<String, Object>> tmAbnormalBeiChesan = tracdemarkTaskMapper.queryTmAbnormal(beganDate, endDate,3);
		List<Map<String, Object>> tmAbnormalBeiWuxiao = tracdemarkTaskMapper.queryTmAbnormal(beganDate, endDate,4);
		List<Map<String, Object>> tmAbnormalBeiTongYong = tracdemarkTaskMapper.queryTmAbnormal(beganDate, endDate,5);
		//数据库中的针对同一个商标，同一个时间的，相同异常动态的记录应该只有一条，不能重复；
		//调用比较算法
		HashMap<String, Object> rejectMap = new HashMap<>();
		rejectMap.put("beganDate",rejectBeganDate);
		rejectMap.put("endDate",endDate);
		compareArithmetic(tmProcessReject,tmAbnormalReject,rejectMap, 1);
		HashMap<String, Object> dateMap = new HashMap<>();
		dateMap.put("beganDate",beganDate);
		dateMap.put("endDate",endDate);
		compareArithmetic(tmProcessBeiYiyi, tmAbnormalBeiYiyi,dateMap,2);
		compareArithmetic(tmProcessBeiChesan, tmAbnormalBeiChesan,dateMap,3);
		compareArithmetic(tmProcessBeiWuxiao, tmAbnormalBeiWuxiao,dateMap,4);
		compareArithmetic(tmProcessBeiTongYong, tmAbnormalBeiTongYong,dateMap,5);
		
	}
	
	private void compareArithmetic(List<Map<String, Object>> tmProcesss,List<Map<String,Object>> tmAbnormals,HashMap<String, Object> map,int abnormalType){
		map.put("abnormalType", abnormalType);
		for(Map<String, Object> tmProcess: tmProcesss){
			String regNum =(String)tmProcess.get("regNumber");
			int tmId = (int)tmProcess.get("tmId");
			map.put("regNum",regNum);
			map.put("tmId",tmId);
			if(tmAbnormals.size()==0){
				insertTmAbnormal(tmProcess, abnormalType);
			}else{
				int count = tracdemarkTaskMapper.isExist(map);
				if(count == 0){
					insertTmAbnormal(tmProcess, abnormalType);
				}
			}
		}
	}
		//添加数据
		private void insertTmAbnormal(Map<String, Object> map,int abnormalType){
			map.put("abnormalType", abnormalType);
			tracdemarkTaskMapper.insertTmAbnormal(map);
		}


		//自动更新公告提醒表 
		@Override
		public void autoTimingCreateAnnouncementRemind() {
			//模拟登陆
			String tokenID = null;
			//获取tokenID
			User user = new User();
			user.setUsername("admin");
			user.setPassword("123456");
			LoginReturnInfo info = authenticationService.login(user);
			tokenID = info.getTokenID();
			
			String url = serviceUrlConfig.getMailServiceUrl()+"/mail/sendAnnoRemind?tokenID="+tokenID;
			List<Map<String, Object>>list = tracdemarkTaskMapper.queryGongGao();
			List<Map<String, Object>> anns = tracdemarkTaskMapper.queryAnnouncementRemind();
			//如果公告表中最初没有数据 则全部插入
			if(anns.size() == 0){
				if(list.size()!=0){
					this.insert(list);
					//调用邮件服务，发送邮件
					try{
						String jsonString = GraspUtil.getText(url);
						System.err.println("邮件发送成功....");
					}catch (Exception e) {
						e.printStackTrace();
					}
				}
			}else{
				//如果两者的内容数据一样多则不用再更新
				List<Map<String, Object>> newAdds = new ArrayList<>();
				if(list.size()==anns.size()){
				}else{
					for(int i=0;i<list.size();i++){
						Map<String, Object> gongGao = list.get(i);
						int gongGaoId = (int)gongGao.get("id");
						int isExist = tracdemarkTaskMapper.isExists(gongGaoId);
						if(isExist == 0){
							newAdds.add(gongGao);
						}
					}
					//
					if(newAdds.size()!=0){
						this.insert(newAdds);
						//调用发送邮件服务，每次新检索出的信息要发送邮件
						try{
							GraspUtil.getText(url);
							System.err.println("邮件发送成功...");
						}catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				
			}
		
		}

		private void insert(List<Map<String, Object>> list){
			tracdemarkTaskMapper.insertAnnouncementRemind(list);
		}
	
	
}
