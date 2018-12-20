package com.yootii.bdy.trademark.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonFormat.Value;
import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion.User;
import com.yootii.bdy.common.CommonController;
import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.trademark.service.TradeMarkNotifyService;
import com.yootii.bdy.util.DateTool;

@Controller
@RequestMapping("/interface/trademarkNotify")
public class TradeMarkNotifyController extends CommonController{
	@Autowired
	private TradeMarkNotifyService trademarkNotifyService;
	
	/**
	 * 根据batchNo 查询 商标续展 通知(也即  记录的详情)  
	 * @param request
	 * @param custId
	 * @param batchNo
	 * @param gcon
	 * @return
	 */
	@RequestMapping(value="queryTmNotifyList",produces="application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo queryTmNotifyList(HttpServletRequest request,Integer custId,String batchNo,Integer no,GeneralCondition gcon){
		ReturnInfo returnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if(no == null){
			returnInfo.setSuccess(false);
			returnInfo.setMessage("no参数不能为空");
			return returnInfo;
		}
		try{
			if(!StringUtils.isNotBlank(batchNo))throw new Exception("参数为空");
			returnInfo = trademarkNotifyService.queryTmNotifyList(custId, batchNo,gcon,no);
			returnInfo.setSuccess(true);
			returnInfo.setMessage("查询成功");
		}catch (Exception e) {
			e.printStackTrace();
			returnInfo.setSuccess(false);
			returnInfo.setMessage("查询失败");
		}
		return returnInfo;
	}
	/**
	 * 根据custId 查询商标续展通知
	 * @param request
	 * @param custId
	 * @param gcon
	 * @return
	 */
	@RequestMapping(value="queryTmNotifyByCustId",produces="application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo queryTmNotifyByCustId(HttpServletRequest request,Integer custId,String createTime,GeneralCondition gcon){
		ReturnInfo returnInfo = authenticationService.authorize(gcon);
		makeOffsetAndRows(gcon);
		try{
			if(custId==null) throw new Exception();
			if(createTime ==null || createTime ==""){
				List<String>list =(List<String>)trademarkNotifyService.queryAllTimes(custId).getData();
			    if(list!=null && list.size()!=0){
			    	createTime = list.get(0);
			    }else{
			    	createTime = DateTool.getDate(new Date());
			    }
			}else{
				createTime+=" 00:00:00";
			}
			/*returnInfo = trademarkNotifyService.queryTmNotifyList(custId, null, gcon,6);*/
			returnInfo = trademarkNotifyService.queryTmNotifyByCustId(custId,createTime,gcon);
			returnInfo.setSuccess(true);
			returnInfo.setMessage("查询成功");
			returnInfo.setCurrPage(gcon.getPageNo());
		}catch (Exception e) {
			returnInfo.setSuccess(false);
			returnInfo.setMessage("查询失败");
		}
		return returnInfo; 
	}
	
	/**
	 * 修改notification表（商标续展通知）数据的状态
	 * @param request
	 * @param id
	 */
	@RequestMapping(value="modifyNotification",produces="application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo modifyNotification(HttpServletRequest request,GeneralCondition gcon,String ids){
		
		ReturnInfo returnInfo = authenticationService.authorize(gcon);
		if(ids==null || ids  ==""){
			returnInfo.setSuccess(false);
			returnInfo.setMessage("参数不能为空");
		}
		String arr[] = ids.split(",");
		List<String> list = new ArrayList<>();
		if(arr.length == 1){
			String id = ids;
			list.add(id);
		}else{
			 list  = Arrays.asList(arr);
		}
		try{
			trademarkNotifyService.modifyNotification(list);
			returnInfo.setSuccess(true);
			returnInfo.setMessage("更新成功");
		}catch (Exception e) {
			returnInfo.setSuccess(false);
			returnInfo.setMessage("更新失败");
			e.printStackTrace();
		}
		return returnInfo ;
	}
	
	
	
	/**
	 * 查询 （notification表）通知生成的时间点
	 * @param request
	 * @param gcon
	 * @param custId
	 * @return
	 */
	@RequestMapping(value="queryAllTimes",produces="application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo queryAllTimes(HttpServletRequest request,GeneralCondition gcon,Integer custId,Integer agencyId){
		ReturnInfo returnInfo = authenticationService.authorize(gcon);
		if(custId==null){
			returnInfo.setMessage("参数为空");
			returnInfo.setSuccess(false);
			return returnInfo;
		}
		try{
			if(agencyId ==null){
				returnInfo = trademarkNotifyService.queryAllTimes(custId);
			}else {
				returnInfo = trademarkNotifyService.queryRenewalRemindTimes(gcon,custId);
			}
			returnInfo.setMessage("查询成功");
			returnInfo.setSuccess(true);
		}catch (Exception e) {
			e.printStackTrace();
			returnInfo.setMessage("查询失败");
			returnInfo.setSuccess(false);
		}
		return returnInfo;
	}
	
	
	
	
	/**
	 * 、支持对客户续展的3个方面的数量的统计
	 * 客户的可续展的商标数量
		2）已经提醒续展的商标数量
		3）已经进行续展的商标数量
		2、支持按照年来统计，当选择了某个年之后，后端提供4个季度的统计值
	 * @return
	 */
	@RequestMapping(value="statisticTmNotifyStatus",produces="application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo statisticTmNotifyStatus(HttpServletRequest request,GeneralCondition gcon,String year,Integer custId){
		
		ReturnInfo returnInfo = authenticationService.authorize(gcon);
		if(year==null || year ==""){
			Calendar calendar= Calendar.getInstance();
			calendar.setTime(new Date());
			int years = calendar.get(Calendar.YEAR);
			year = years+"";
		}
		try {
			 returnInfo = trademarkNotifyService.statisticTmNotifyStatus(gcon, year,custId);
			 returnInfo.setSuccess(true);
			 returnInfo.setMessage("统计成功");
		} catch (Exception e) {
			e.printStackTrace();
			returnInfo.setSuccess(false);
			returnInfo.setMessage("统计失败");
		}
		return returnInfo;
	}
	/**
	 * 数据接口
	 * @param request
	 * @param gcon
	 * @param quarter
	 * @param year
	 * @param custId
	 * @param type
	 * @return
	 */
	@RequestMapping(value = "statisticTmNotifyData" ,produces= "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo statisticTmNotifyData(HttpServletRequest request,GeneralCondition gcon,Integer quarter,String year,Integer custId,Integer type){
		ReturnInfo returnInfo = authenticationService.authorize(gcon);
		if(year==null || year ==""){
			Calendar calendar= Calendar.getInstance();
			calendar.setTime(new Date());
			int years = calendar.get(Calendar.YEAR);
			year = years+"";
		}
		if(custId == null){
			returnInfo.setSuccess(false);
			returnInfo.setMessage("参数不能为空");
		}
		if(quarter==null){
			//默认为一季度
			quarter =1;
		}
		if(type == null){
			type= 0;
		}
		try{
			makeOffsetAndRows(gcon);
			returnInfo = trademarkNotifyService.statisticTmNotifyData(gcon, year, custId, quarter, type);
			 returnInfo.setSuccess(true);
			 returnInfo.setMessage("统计成功");
		}catch (Exception e) {
			e.printStackTrace();
			returnInfo.setSuccess(false);
			returnInfo.setMessage("统计失败");
		}
		return returnInfo;
	}
	
	
	
	/**
	 * 查询查表异常记录（abnormal 表）
	 * @param request
	 * @param custId
	 * @param gCondition
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value ="queryAbnormalList",produces="application/json;charset=UTF-8")
	public ReturnInfo queryTmAbnormalList(HttpServletRequest request,Integer custId,GeneralCondition gCondition){
		
		ReturnInfo returnInfo = authenticationService.authorize(gCondition);
		if(custId==null){
			returnInfo.setMessage("参数不完整");
			returnInfo.setSuccess(false);
			return returnInfo;
		}
		makeOffsetAndRows(gCondition);
		Integer userId = null;
		try{
			returnInfo = trademarkNotifyService.queryTmAbnormalList(custId,gCondition,userId);
			returnInfo.setSuccess(true);
			returnInfo.setMessage("查询成功");
			returnInfo.setCurrPage(gCondition.getPageNo());
		}catch (Exception e) {
			e.printStackTrace();
			returnInfo.setMessage("查询失败");
			returnInfo.setSuccess(false);
		}
		return returnInfo;
	}
	/**
	 * 异常表的时间分段
	 * @param gCondition
	 * @param custId
	 * @return
	 */
	@RequestMapping(value="queryAbnormalTimes",produces="application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo queryAbnormalTimes(GeneralCondition gCondition,Integer custId){
		ReturnInfo returnInfo =  authenticationService.authorize(gCondition);
		if(custId !=null){
			returnInfo.setSuccess(false);
			returnInfo.setMessage("参数不能为空");
			return returnInfo;
		}
		try{
			returnInfo = trademarkNotifyService.queryAbnormalTimes(custId);
			returnInfo.setSuccess(true);
			returnInfo.setMessage("查询成功");
		}catch (Exception e) {
			e.printStackTrace();
			returnInfo.setSuccess(false);
			returnInfo.setMessage("查询 失败");
		}
		return returnInfo;
	}
	
	/**
	 * 修改商标异常信息的状态（逻辑删除）
	 * @param request
	 * @param id
	 */
	@RequestMapping(value="modifyTmAbnormal",produces="application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo modifyTmAbnormal(HttpServletRequest request,GeneralCondition gcon,String ids){
		
		ReturnInfo returnInfo = authenticationService.authorize(gcon);
		if(ids==null || ids  ==""){
			returnInfo.setSuccess(false);
			returnInfo.setMessage("参数不能为空");
		}
		String arr[] = ids.split(",");
		List<String> list = new ArrayList<>();
		if(arr.length == 1){
			String id = ids;
			list.add(id);
		}else{
			 list  = Arrays.asList(arr);
		}
		try{
			trademarkNotifyService.modifyTmAbnormal(list);
			returnInfo.setSuccess(true);
			returnInfo.setMessage("更新成功");
		}catch (Exception e) {
			returnInfo.setSuccess(false);
			returnInfo.setMessage("更新失败");
			e.printStackTrace();
		}
		return returnInfo ;
	}
	
	/**
	 * 查询公告提醒表数据信息
	 * @param gcon
	 * @param custId
	 * @param appId
	 * @param type
	 * @return
	 */
	@RequestMapping(value ="queryAnnouncementRemind" ,produces="application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo queryAnnouncementRemind(GeneralCondition gcon,Integer custId,Integer userId,Integer appId,Integer type){
		
		ReturnInfo returnInfo = authenticationService.authorize(gcon);
		if(custId == null && userId ==null){
			returnInfo.setSuccess(false);
			returnInfo.setMessage("参数不能为空");
			return returnInfo;
		}
		if(type == null){
			returnInfo.setSuccess(false);
			returnInfo.setMessage("参数不能为空");
			return returnInfo;
		}
		makeOffsetAndRows(gcon);
		try{
			returnInfo = trademarkNotifyService.queryAnnouncementRemind(gcon, custId, appId,userId,type);
			returnInfo.setCurrPage(gcon.getPageNo());
			returnInfo.setMessage("查询成功");
			returnInfo.setSuccess(true);
		}catch (Exception e) {
			e.printStackTrace();
			returnInfo.setSuccess(false);
			returnInfo.setMessage("查询失败");
		}
		return returnInfo;
	}
	/**
	 * 提供公告期号列表
	 * @param gcon
	 * @param custId
	 * @param userId
	 * @return
	 */
	@RequestMapping(value="queryAllGGQiao",produces="application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo queryAllGGQiao(GeneralCondition gcon,Integer custId,Integer userId){
		ReturnInfo returnInfo = authenticationService.authorize(gcon);
		if(custId == null){
			returnInfo.setSuccess(false);
			returnInfo.setMessage("参数不能为空");
			return returnInfo;
		}
		if(returnInfo.getSuccess() && returnInfo !=null){
			try{
				returnInfo = trademarkNotifyService.queryAllGGQiao(custId);
				returnInfo.setSuccess(true);
				returnInfo.setMessage("查询成功");
			}catch (Exception e) {
				e.printStackTrace();
				returnInfo.setSuccess(false);
				returnInfo.setMessage(e.getMessage());
			}
		}
		return returnInfo;
	}
	
	/**
	 * 为邮件服务提供 公告数据
	 * @param request
	 * @param gcon
	 * @return
	 */
	@RequestMapping(value="queryAnnoucementForMail",produces="application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo queryAnnoucementForMail(HttpServletRequest request,GeneralCondition gcon,Integer custId,Integer type){
		ReturnInfo returnInfo = authenticationService.authorize(gcon);
		try{
			if(custId ==null){
				returnInfo.setSuccess(false);
				returnInfo.setMessage("参数不能为空");
				return returnInfo;
			}
			returnInfo = trademarkNotifyService.queryAnnoucementForMail(custId,type);
			returnInfo.setSuccess(true);
			returnInfo.setMessage("查询成功");
		}catch (Exception e) {
			e.printStackTrace();
			returnInfo.setSuccess(false);
			returnInfo.setMessage("查询失败");
		}
		return returnInfo;
	}
	
	/**
	 * 更改公告通知的状态
	 * @param gcon
	 * @param ids
	 * @return
	 */
	@RequestMapping(value="updateAnnouncementRemind",produces="application/json;charset=UTF-8")
	@ResponseBody
	public  ReturnInfo updateAnnouncementRemind(GeneralCondition gcon,String ids){
		ReturnInfo returnInfo = authenticationService.authorize(gcon);
		if(ids==null || ids.equals("")){
			returnInfo.setSuccess(false);
			returnInfo.setMessage("参数不能为空");
		}
		String[] arr = ids.split(",");
		List<String> list = new ArrayList<>();
		if(arr.length == 1){
			String id = ids;
			list.add(id);
		}else{
			 list  = Arrays.asList(arr);
		}
		try{
			//批量更新
			trademarkNotifyService.updateAnnouncementRemind(list);
			returnInfo.setSuccess(true);
			returnInfo.setMessage("查询成功");
		}catch (Exception e) {
			returnInfo.setSuccess(false);
			returnInfo.setMessage("查询失败");
			e.printStackTrace();
		}
		return returnInfo;
	}
	
	

}
