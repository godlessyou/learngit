package com.yootii.bdy.trademark.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yootii.bdy.common.CommonController;
import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.Globals;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.security.model.Token;
import com.yootii.bdy.security.service.AuthenticationService;
import com.yootii.bdy.trademark.model.Trademark;
import com.yootii.bdy.trademark.service.TradeMarkService;

@Controller
@RequestMapping("/interface/trademark")
public class TradeMarkController extends CommonController {
	
	private final Logger logger = Logger.getLogger(this.getClass());
	
	
	@Resource
	private TradeMarkService tradeMarkService;
	@Resource
	private AuthenticationService authenticationService;
	// /interface/trademark/querytmlist
	/*@RequestMapping(value = "/querytm", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo queryTmList(Integer govId,Integer entId,Date appStartDate,
    		Date appEndDate,GeneralCondition gcon, Trademark trademark) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		makeOffsetAndRows(gcon);
		if(rtnInfo != null && rtnInfo.getSuccess()){//通过身份验证
			rtnInfo = tradeMarkService.queryTmList(govId,entId,appStartDate,appEndDate,gcon,trademark);
			
		}
		return rtnInfo;
	}*/
	/**
	 * 
	 * @param govId
	 * @param entId
	 * @param appStartDate
	 * @param appEndDate
	 * @param gcon
	 * @param trademark
	 * @param regStartDate
	 * @param regEndDate
	 * @param trademarkProcess
	 * @param statusStartDate
	 * @param statusEndDate
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/selecttrademark", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo queryTmList(Date appStartDate,
    		Date appEndDate,GeneralCondition gcon, Trademark trademark,Date regStartDate,
    		Date regEndDate,String appName,Integer custId,Integer appId,Integer national, String addressList) throws Exception {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		makeOffsetAndRows(gcon);
		if(rtnInfo != null && rtnInfo.getSuccess()){//通过身份验证
			trademark.setApplicantName(appName);
			rtnInfo = tradeMarkService.queryTmList(appStartDate, appEndDate, gcon, trademark, regStartDate, regEndDate, custId,appId,national, addressList);
		}
		return rtnInfo;
	}
	
	
	
	@RequestMapping(value = "/queryCanProcessTm", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo queryCanProcessTm(Date appStartDate,
    		Date appEndDate,GeneralCondition gcon, Trademark trademark,Date regStartDate,
    		Date regEndDate,String appName,Integer custId,Integer appId,Integer national, Integer caseTypeId, String addressList) throws Exception {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		makeOffsetAndRows(gcon);
		if(rtnInfo != null && rtnInfo.getSuccess()){//通过身份验证
			if (caseTypeId==null){
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage("缺少必要的参数caseTypeId");
				rtnInfo.setMessageType(Globals.MESSAGE_TYPE_PARAM_INVALID);
				return rtnInfo;
			}
			trademark.setApplicantName(appName);
			rtnInfo = tradeMarkService.queryCanProcessTm(appStartDate, appEndDate, gcon, trademark, regStartDate, regEndDate, custId, appId, national, caseTypeId, addressList);
		}
		return rtnInfo;
	}

	

	
	@RequestMapping(value = "/tmdetail", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo queryTmDetail(GeneralCondition gcon,Trademark trademark) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if(rtnInfo != null && rtnInfo.getSuccess()){//通过身份验证
			rtnInfo = tradeMarkService.queryTmDetail(trademark);
		}
		return rtnInfo;
	}
	
	@RequestMapping(value = "/update", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo updateTmDetail(GeneralCondition gcon,String regNumber,HttpServletRequest request) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if(rtnInfo != null && rtnInfo.getSuccess()){//通过身份验证
			Token token = (Token)rtnInfo.getData();
			this.addToken(token);//审计日志需要用到token信息
			this.addURL(request.getRequestURI());
			rtnInfo = tradeMarkService.updateSolrDate();
		}
		return rtnInfo;
	}
	
	@RequestMapping(value = "/querytmproinfo", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo querytmproinfo(GeneralCondition gcon,Integer custId) {

		ReturnInfo returnInfo =new ReturnInfo();
		if(custId == null ) {
			returnInfo.setSuccess(false);
			returnInfo.setMessage("调用/querytmproinfo接口的参数custId为空");				
			return returnInfo;
		}
		
		long startGetImag = System.currentTimeMillis();	
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if(rtnInfo != null && rtnInfo.getSuccess()){//通过身份验证
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			Integer startYear =null;			
			Integer endYear =null;
//			endYear = c.get(Calendar.YEAR);

			
			Integer appId=null;
			rtnInfo=tradeMarkService.statsTmStautsList(custId, appId, startYear, endYear, gcon);
			List<Map<String,Object>> data = (List<Map<String, Object>>) ((Map<String, Object>) rtnInfo.getData()).get("tmStatus");
			Map<String,Object> ret = new HashMap<String,Object>();
			int yzc=0;
			int sqz=0;
			int ywx=0;
			if(data!=null) {
				for(Map<String, Object> a:data) {
					switch (a.get("tmStatus").toString()) {
					case "已注册":
						yzc = Integer.valueOf(a.get("count").toString()).intValue()+yzc;						
						break;
					case "申请中":
						sqz = Integer.valueOf(a.get("count").toString()).intValue()+sqz;						
						break;
					
					case "已无效":
						ywx = Integer.valueOf(a.get("count").toString()).intValue()+ywx;						
						break;
					}
				}
			}
			ret.put("已注册", yzc);
			ret.put("申请中", sqz);
			ret.put("已无效", ywx);

			rtnInfo.setData(ret);
		}
		long endGetImag = System.currentTimeMillis();			
		long time=endGetImag-startGetImag;
		logger.debug("querytmproinfo time: "+ time +" ms");
		return rtnInfo;
	}
	
	/**
	 * 统计商标各种状态的数量(Ok)
	 * by  H.x
	 * @return
	 */
	@RequestMapping(value="/statisticTmStatus",produces="application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo  statisticTmStatus(GeneralCondition gcon,Integer custId,Integer appId,Integer startYear,Integer endYear,Integer flag){
		
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		//非空判断
		if(custId==null){
			rtnInfo.setMessage("参数为空");
			rtnInfo.setSuccess(false);
			return rtnInfo;
		}
		if(flag == null){
			flag = 2;
		}
		//默认时间是4年之内的数据	
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		int currentYear=c.get(Calendar.YEAR);			
		if(startYear == null) {
			startYear = currentYear - 4;
		} 
		if(endYear == null) {
			endYear = currentYear;
		} 
		if(rtnInfo != null && rtnInfo.getSuccess()){
			try {
				rtnInfo = tradeMarkService.statisticTmStatus(gcon,custId,appId,startYear,endYear,flag);
				rtnInfo.setSuccess(true);
				rtnInfo.setMessage("查询统计成功");
			} catch (Exception e) {
				e.printStackTrace();
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage("查询统计失败");
			}
		}
		return rtnInfo;
	}
	
	/**
	 * 根据商标状态查询数据
	 * @param request
	 * @param custId
	 * @param tmStatus
	 * @return
	 */
	
	@RequestMapping(value="/statisticTmDate",produces="application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo statisticTmDate(HttpServletRequest request,Integer startYear,Integer endYear,Integer custId,Integer appId,String tmStatus,Integer flag,GeneralCondition  gcon){
		
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		//非空判断
		if(custId==null){
			rtnInfo.setMessage("参数不能为空");
			rtnInfo.setSuccess(false);
			return rtnInfo;
		}
		if(flag == null){
			flag = 2;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		int currentYear=c.get(Calendar.YEAR);			
		if(startYear == null) {
			startYear = currentYear - 4;
		} 
		if(endYear == null) {
			endYear = currentYear;
		} 
		makeOffsetAndRows(gcon);
		try{
			rtnInfo  = tradeMarkService.statisticTmDate(gcon,custId, appId, tmStatus,flag, startYear, endYear);
			rtnInfo.setSuccess(true);
			rtnInfo.setCurrPage(gcon.getPageNo());
			
		}catch (Exception e) {
			e.printStackTrace();
			rtnInfo.setSuccess(false);
		}
		return rtnInfo;
	}
	
	/**
	 * 统计商标状态  （以前的实现）
	 * @param gcon
	 * @param appId
	 * @param custId
	 * @param startYear
	 * @param endYear
	 * @return
	 */
	@RequestMapping(value = "/statstmstauts", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo statstmstauts(GeneralCondition gcon,Integer appId,Integer custId,Integer startYear,Integer endYear) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if(rtnInfo != null && rtnInfo.getSuccess()){//通过身份验证
			/*
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			int currentYear=c.get(Calendar.YEAR);			
			if(startYear == null) {
				startYear = currentYear - 4;
			} 
			if(endYear == null) {
				endYear = currentYear;
			} 
			*/
			
			rtnInfo = tradeMarkService.statsTmStautsList(custId,appId, startYear, endYear, gcon);

		}
		return rtnInfo;
	}
	/**
	 * 代理机构申请量(OK)
	 * @param gcon
	 * @param appId
	 * @param custId
	 * @param startYear
	 * @param endYear
	 * @return
	 */
	@RequestMapping(value = "/statstmagent", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo statstmagent(GeneralCondition gcon,Integer appId,Integer custId,Integer startYear,Integer endYear,Integer flag) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if(rtnInfo != null && rtnInfo.getSuccess()){//通过身份验证

			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			int currentYear=c.get(Calendar.YEAR);			
			if(startYear == null) {
				startYear = currentYear - 4;
			} 
			if(endYear == null) {
				endYear = currentYear;
			} 
			if(flag == null){
				flag = 2;
			}
			rtnInfo = tradeMarkService.statsTmAgentList(custId,appId, startYear, endYear, gcon,flag);

		}
		return rtnInfo;
	}
	
	/**
	 * 查询具体的代理机构申请量的数据
	 * @param request
	 * @param custId
	 * @param flag
	 * @param year
	 * @return
	 */
	@RequestMapping(value="/statsTmAgentData",produces="application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo statsTmAgentData(HttpServletRequest request,Integer custId,Integer flag,Integer year,GeneralCondition gcon,Integer appId){
		
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		
		if(rtnInfo!=null && rtnInfo.getSuccess()){
			if(custId == null || year==null){
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage("参数不能为空");
			}
			if(flag ==null){
				flag = 2;
			}
			makeOffsetAndRows(gcon);
			try{
				rtnInfo = tradeMarkService.statsTmAgentData(gcon, custId, flag, year, appId);
				rtnInfo.setCurrPage(gcon.getPageNo());
				rtnInfo.setSuccess(true);
				rtnInfo.setMessage("查询成功");
			}catch (Exception e) {
				e.printStackTrace();
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage("查询失败");
			}
		}
		return rtnInfo;
	}
	
	
	/**
	 * 统计每年商标申请数量 ； 统计每年注册商标的数量(Ok)
	 * @param gcon
	 * @param custId
	 * @param appId
	 * @param startYear
	 * @param endYear
	 * @return
	 */
	@RequestMapping(value = "/statstmappdateregdate", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo statstmappdateregdate(GeneralCondition gcon,Integer custId,Integer appId,Integer startYear,Integer endYear,Integer flag) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if(rtnInfo != null && rtnInfo.getSuccess()){//通过身份验证

			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			int currentYear=c.get(Calendar.YEAR);			
			if(startYear == null) {
				startYear = currentYear - 4;
			} 
			if(endYear == null) {
				endYear = currentYear;
			} 
			if(flag == null){
				flag = 2;
			}
			rtnInfo = tradeMarkService.statsTmAppdateRegDateList(custId,appId, startYear, endYear, gcon,flag);

		}
		return rtnInfo;
	}
	/**
	 * 按申请人名称统计商标注册量（OK）
	 * @param gcon
	 * @param custId
	 * @param startYear
	 * @param endYear
	 * @param regFlag
	 * @return
	 */
	@RequestMapping(value = "/statstmappname", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo statstmappname(GeneralCondition gcon,Integer custId,Integer startYear,Integer endYear,Integer regFlag,Integer flag) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if(rtnInfo != null && rtnInfo.getSuccess()){//通过身份验证

			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
						
			/*
			int currentYear=c.get(Calendar.YEAR);	
			if(startYear == null) {
				//如果regFlag=1，是执行统计，否则，是获取该申请人的商标数量
				//因此，起始年份不同
				if (regFlag==null || regFlag==1){
					startYear = currentYear - 4;
				}else{
					startYear = 1970;
				}
			} 
			if(endYear == null) {
				endYear = currentYear;
			} 
			*/
			
			
			if(flag==null){
				flag = 2;
			}
			rtnInfo = tradeMarkService.statsTmAppnameList(custId, startYear, endYear, gcon, regFlag,flag);

		}
		return rtnInfo;
	}
	
	
	/**
	 * 按申请人名称统计商标注册量（OK）
	 * @param gcon
	 * @param custId
	 * @param startYear
	 * @param endYear
	 * @param regFlag
	 * @return
	 */
	@RequestMapping(value = "/queryTmCountByAppId", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo queryTmCountByAppId(GeneralCondition gcon, String appId) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		Integer custId=null;
		Integer startYear=null;
		Integer endYear=null;
		Integer regFlag=2;
		Integer flag=0;
		if(rtnInfo != null && rtnInfo.getSuccess()){//通过身份验证

			Calendar c = Calendar.getInstance();			
			c.setTime(new Date());
			
			/*
			int currentYear=c.get(Calendar.YEAR);			
			if(startYear == null) {
				//如果regFlag=1，是执行统计，否则，是获取该申请人的商标数量
				//因此，起始年份不同
				if (regFlag==null || regFlag==1){
					startYear = currentYear - 4;
				}else{
					startYear = 1970;
				}
			} 
			if(endYear == null) {
				endYear = currentYear;
			} 
			*/
			
			rtnInfo = tradeMarkService.statsTmAppnameList(custId, startYear, endYear, gcon, regFlag,flag);

		}
		return rtnInfo;
	}
	
	/**
	 * 商标续展
	 * @param gcon
	 * @param custId
	 * @param appId
	 * @param startYear
	 * @param endYear
	 * @return
	 */
	@RequestMapping(value = "/statstmxuzhan", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo statstmxuzhan(GeneralCondition gcon,Integer custId,Integer appId,Integer startYear,Integer endYear,Integer flag) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if(rtnInfo != null && rtnInfo.getSuccess()){//通过身份验证

			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			if(startYear == null) {
				startYear = c.get(Calendar.YEAR);
			} 
			if(endYear == null) {
				endYear = c.get(Calendar.YEAR) + 4;
			} 
			if(flag == null){
				flag = 2;
			}
			rtnInfo = tradeMarkService.statstmxuzhan(custId,appId, startYear, endYear, gcon,flag);

		}
		return rtnInfo;
	}
	/**
	 * 商标名称分类统计
	 * @param gcon
	 * @param custId
	 * @param appId
	 * @param startYear
	 * @param endYear
	 * @return
	 */
	@RequestMapping(value = "/statstmname", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo statstmname(GeneralCondition gcon,Integer custId,Integer appId,Integer startYear,Integer endYear,Integer flag) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if(rtnInfo != null && rtnInfo.getSuccess()){//通过身份验证
			/*
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			if(startYear == null) {
				startYear = c.get(Calendar.YEAR) - 4;
			} 
			if(endYear == null) {
				endYear = c.get(Calendar.YEAR);
			} 
			if(flag == null){
				flag =2 ;
			}
			*/
			rtnInfo = tradeMarkService.statsTmNameList(custId,appId, startYear, endYear, gcon,flag);

		}
		return rtnInfo;
	}
	
	@RequestMapping(value = "/querytmxuzhan", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo queryTmXuzhanList(GeneralCondition gcon,Integer custId,Integer appId,Integer national) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if(rtnInfo != null && rtnInfo.getSuccess()){//通过身份验证		
			makeOffsetAndRows(gcon);
			rtnInfo = tradeMarkService.queryTmXuzhanList(gcon, custId,appId,national);		

		}
		return rtnInfo;
	}
	
	@RequestMapping(value = "/querytmchange", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo queryTmChangeList(GeneralCondition gcon,Trademark trademark, Integer appId,Integer national, String addressList) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if(rtnInfo != null && rtnInfo.getSuccess()){//通过身份验证		
			makeOffsetAndRows(gcon);
			rtnInfo = tradeMarkService.queryTmChangeList(gcon,trademark, appId,national, addressList);

		}
		return rtnInfo;
	}
	
	@RequestMapping(value = "/querytmkuanzhan", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo queryTmKuanzhanList(GeneralCondition gcon,Integer custId,Integer appId,Integer national) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if(rtnInfo != null && rtnInfo.getSuccess()){//通过身份验证		
			makeOffsetAndRows(gcon);
			rtnInfo = tradeMarkService.queryTmKuanzhanList(gcon, custId,appId,national);

		}
		return rtnInfo;
	}
	
	/**
	 * 图表统计后显示具体数据
	 * @param gcon
	 * @param custId
	 * @param appId
	 * @param dateType
	 * @param trademark
	 * @param startYear
	 * @param endYear
	 * @param timetype
	 * @param bodytype
	 * @return
	 */
	@RequestMapping(value = "/queryTrademarkBySolr", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo queryTrademarkBySolr(GeneralCondition gcon,Integer custId,Integer appId,Integer dateType,Trademark trademark,
			Integer startYear,Integer endYear,Integer timetype,Integer bodytype,Integer flag) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if(rtnInfo != null && rtnInfo.getSuccess()){//通过身份验证

			makeOffsetAndRows(gcon);
			
			/*
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			if(startYear == null) {
				startYear = c.get(Calendar.YEAR) - 4;
			} 
			if(endYear == null) {
				endYear = c.get(Calendar.YEAR);
			} 
			if(flag ==null ){
				flag = 2;
			}
			*/
			
			rtnInfo = tradeMarkService.queryTrademarkBySolr(custId,appId, dateType, trademark, startYear.toString(), endYear.toString(), gcon,false,flag);

		}
		return rtnInfo;
	}
	/**
	 * 商标类别统计
	 * @param gcon
	 * @param custId
	 * @param appId
	 * @param startYear
	 * @param endYear
	 * @return
	 */
	@RequestMapping(value = "/statstmtype", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo statstmtype(GeneralCondition gcon,Integer custId,Integer appId,Integer startYear,Integer endYear,Integer flag) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if(rtnInfo != null && rtnInfo.getSuccess()){//通过身份验证

			/*
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			int currentYear=c.get(Calendar.YEAR);			
			if(startYear == null) {
				startYear = currentYear - 4;
			} 
			if(endYear == null) {
				endYear = currentYear;
			} 
			if(flag == null){
				flag = 2;
			}
			*/
			
			rtnInfo = tradeMarkService.statsTmTypeList(custId,appId, startYear, endYear, gcon,flag);

		}
		return rtnInfo;
	}
	/**
	 * 商标动态查询
	 * @param gcon
	 * @param custId
	 * @param appId
	 * @param startYear
	 * @param endYear
	 * @return
	 */
	@RequestMapping(value = "/statstmdongtai", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo statstmdongtai(GeneralCondition gcon,Integer custId,Integer appId,Integer startYear,Integer endYear) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if(rtnInfo != null && rtnInfo.getSuccess()){//通过身份验证
			/*
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			int currentYear=c.get(Calendar.YEAR);			
			if(startYear == null) {
				startYear = currentYear - 4;
			} 
			if(endYear == null) {
				endYear = currentYear;
			} 
			*/
			
			rtnInfo = tradeMarkService.statsTmDongtai(custId,appId, startYear, endYear, gcon);

		}
		return rtnInfo;
	}
	
	/**
	 * 
	 * @param request
	 * @param cusId
	 * @param appId
	 * @param flag         国内、外标识
	 * @param abnormalType  商标异常类型
	 * @param gcon
	 * @param year
	 * @return
	 */
	@RequestMapping(value="tmDynamicData",produces = "application/json;charset=UTF-8")
	@ResponseBody
	public  ReturnInfo tmDynamicData(HttpServletRequest request,Integer custId,Integer appId,Integer flag,Integer abnormalType,GeneralCondition gcon,Integer year){
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if(rtnInfo.getSuccess() && rtnInfo != null){
			//非空判断
			if(custId == null || abnormalType==null || year == null){
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage("参数不能为空");
			}
			if(flag == null){
				flag =2;
			}
			//分页
			makeOffsetAndRows(gcon);
			try{
				rtnInfo = tradeMarkService.tmDynamicData(gcon, custId, flag, appId, year, abnormalType);
				rtnInfo.setSuccess(true);
				rtnInfo.setMessage("查询成功");
			}catch (Exception e) {
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage("查询失败");
				e.printStackTrace();
			}
		}
		return rtnInfo;
	}
	
	
	
	/**
	 * 商标被驳回动态查询
	 * @param gcon
	 * @param custId
	 * @param appId
	 * @param startYear
	 * @param endYear
	 * @return
	 */
	@RequestMapping(value = "/statstmbeibohuidongtai", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo statsTmBeibohuiDongtai(GeneralCondition gcon,Integer custId,Integer appId,Integer startYear,Integer endYear) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if(rtnInfo != null && rtnInfo.getSuccess()){//通过身份验证
			/*
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			int currentYear=c.get(Calendar.YEAR);			
			if(startYear == null) {
				startYear = currentYear - 4;
			} 
			if(endYear == null) {
				endYear = currentYear;
			} 
			*/
			rtnInfo = tradeMarkService.statsTmBeibohuiDongtai(custId, appId, startYear, endYear, gcon);

		}
		return rtnInfo;
	}
	/**
	 * 统计商标异常通过solr的tmProcess核心
	 * @param request
	 * @param custId
	 * @param appId
	 * @param startYear
	 * @param endYear
	 * @return
	 */
	@RequestMapping(value = "/statisByTmProcessCore", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo statisByTmProcessCore(GeneralCondition gcon,Integer custId,Integer appId,Integer startYear,Integer endYear,Integer status,Integer flag){
		
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		try{
			if(rtnInfo != null && rtnInfo.getSuccess()){//通过身份验证

				Calendar c = Calendar.getInstance();
				c.setTime(new Date());
				int currentYear=c.get(Calendar.YEAR);
				if(custId == null || status == null){
					rtnInfo.setSuccess(false);
					rtnInfo.setMessage("参数不能为空");
					return rtnInfo;
				}
				if(startYear == null) {
					startYear = currentYear - 4;
				} 
				if(endYear == null) {
					endYear = currentYear;
				} 
				if(flag == null){
					flag = 2;
				}
				rtnInfo = tradeMarkService.statisByTmProcess(custId, appId, startYear, endYear, gcon,status,flag);
				rtnInfo.setSuccess(true);
				rtnInfo.setMessage("查询成功");
			}
		}catch (Exception e) {
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("查询失败");
			e.printStackTrace();
		}
		return rtnInfo;
		
	}
	
	
	/**
	 *被撤三 
	 * @return
	 */
	@RequestMapping(value = "/statstmbeichesandongtai", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo statsTmBeichesanDongtai(GeneralCondition gcon,Integer custId,Integer appId,Integer startYear,Integer endYear) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if(rtnInfo != null && rtnInfo.getSuccess()){//通过身份验证

			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			int currentYear=c.get(Calendar.YEAR);			
			if(startYear == null) {
				startYear = currentYear - 4;
			} 
			if(endYear == null) {
				endYear = currentYear;
			}  
			
			rtnInfo = tradeMarkService.statsTmBeichesanDongtai(custId, appId, startYear, endYear, gcon);

		}
		return rtnInfo;
	}
	
	/**
	 *  商标被无效动态查询
	 * @param gcon
	 * @param custId
	 * @param appId
	 * @param startYear
	 * @param endYear
	 * @return
	 */
	@RequestMapping(value = "/statstmbeiwuxiaodongtai", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo statsTmBeiwuxiaoDongtai(GeneralCondition gcon,Integer custId,Integer appId,Integer startYear,Integer endYear) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if(rtnInfo != null && rtnInfo.getSuccess()){//通过身份验证

			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			int currentYear=c.get(Calendar.YEAR);			
			if(startYear == null) {
				startYear = currentYear - 4;
			} 
			if(endYear == null) {
				endYear = currentYear;
			}  
			
			rtnInfo = tradeMarkService.statsTmBeiwuxiaoDongtai(custId, appId, startYear, endYear, gcon);

		}
		return rtnInfo;
	}
	
	/**
	 *  商标异议动态查询
	 * @param gcon
	 * @param custId
	 * @param appId
	 * @param startYear
	 * @param endYear
	 * @return
	 */
	@RequestMapping(value = "/statstmbeiyiyidongtai", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo statsTmBeiyiyiDongtai(GeneralCondition gcon,Integer custId,Integer appId,Integer startYear,Integer endYear) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if(rtnInfo != null && rtnInfo.getSuccess()){//通过身份验证

			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			int currentYear=c.get(Calendar.YEAR);			
			if(startYear == null) {
				startYear = currentYear - 4;
			} 
			if(endYear == null) {
				endYear = currentYear;
			} 
			
			rtnInfo = tradeMarkService.statsTmBeiyiyiDongtai(custId, appId, startYear, endYear, gcon);

		}
		return rtnInfo;
	}
	
	
	/**
	 *  商标变更动态查询
	 * @param gcon
	 * @param custId
	 * @param appId
	 * @param startYear
	 * @param endYear
	 * @return
	 */
	@RequestMapping(value = "/statstmbiangengdongtai", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo statsTmBiangengDongtai(GeneralCondition gcon,Integer custId,Integer appId,Integer startYear,Integer endYear) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if(rtnInfo != null && rtnInfo.getSuccess()){//通过身份验证

			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			int currentYear=c.get(Calendar.YEAR);			
			if(startYear == null) {
				startYear = currentYear - 4;
			} 
			if(endYear == null) {
				endYear = currentYear;
			}  
			
			rtnInfo = tradeMarkService.statsTmBiangengDongtai(custId, appId, startYear, endYear, gcon);

		}
		return rtnInfo;
	}
	
	/**
	 *  商标通用名称动态查询
	 * @param gcon
	 * @param custId
	 * @param appId
	 * @param startYear
	 * @param endYear
	 * @return
	 */
	@RequestMapping(value = "/statstmtongyongmingchengdongtai", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo statsTmTongyongmingchengDongtai(GeneralCondition gcon,Integer custId,Integer appId,Integer startYear,Integer endYear) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if(rtnInfo != null && rtnInfo.getSuccess()){//通过身份验证

			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			int currentYear=c.get(Calendar.YEAR);			
			if(startYear == null) {
				startYear = currentYear - 4;
			} 
			if(endYear == null) {
				endYear = currentYear;
			} 
			
			rtnInfo = tradeMarkService.statsTmTongyongmingchengDongtai(custId, appId, startYear, endYear, gcon);

		}
		return rtnInfo;
	}
	

	/**
	 * 查看申请人的商标的代理所
	 * @param gcon
	 * @param appName
	 * @return
	 */
	@RequestMapping(value = "/queryAgentByAppName", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo queryAgentByAppName(GeneralCondition gcon, String appName) {
		ReturnInfo rtnInfo = this.checkUser(gcon);
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证
			rtnInfo = tradeMarkService.queryAgentByAppName(appName);
		}	
		return rtnInfo;
	}
	

	@RequestMapping(value = "/queryTrademarkbyRenumber", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo queryTrademarkbyRenumber(GeneralCondition gcon,Trademark trademark) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证
			rtnInfo = tradeMarkService.queryTrademarkbyRenumber(trademark, gcon);
		}

		
		return rtnInfo;
	}
	
	
	@RequestMapping(value = "/updateGoods", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo updateGoods(GeneralCondition gcon,String goodsPath,String gfPath,HttpServletRequest request) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if(rtnInfo != null && rtnInfo.getSuccess()){//通过身份验证
			Token token = (Token)rtnInfo.getData();
			this.addToken(token);//审计日志需要用到token信息
			this.addURL(request.getRequestURI());
			rtnInfo = tradeMarkService.updateGoods(goodsPath, gfPath);
		}
		return rtnInfo;
	}
	
	
	
	@RequestMapping(value = "/queryAddressList", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo queryAddressList(GeneralCondition gcon,Trademark trademark) throws Exception {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		makeOffsetAndRows(gcon);
		if(rtnInfo != null && rtnInfo.getSuccess()){//通过身份验证		
			String applicantName=trademark.getApplicantName();	
			String applicantEnName=trademark.getApplicantEnName();
			if ((applicantName==null || applicantName.equals("")) && (applicantEnName==null || applicantEnName.equals(""))){
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage("缺少参数applicantName/applicantEnName");
				rtnInfo.setMessageType(Globals.MESSAGE_TYPE_PARAM_INVALID);
				return rtnInfo;
			}
			
			rtnInfo = tradeMarkService.queryAddressList(trademark);
		}
		return rtnInfo;
	}
	
	
//	/**
//	 * 分代理机构,客户商标top10;
//	 * @param gcon
//	 * @param custId
//	 * @param startYear
//	 * @param endYear
//	 * @return
//	 */
//	@RequestMapping(value = "/statstmtop10", produces = "application/json;charset=UTF-8")
//	@ResponseBody
//	public ReturnInfo statsTmTop10(GeneralCondition gcon,Integer startYear,Integer endYear) {
//		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
//		if(rtnInfo != null && rtnInfo.getSuccess()){//通过身份验证
//
//			try {
//				rtnInfo = tradeMarkService.statsTmTop10(gcon);
//			} catch (Exception e) {
//				
//				rtnInfo.setSuccess(false);
//				rtnInfo.setMessageType(-2);
//				rtnInfo.setMessage(e.getMessage());
//				return rtnInfo;
//			}
//
//		}
//		return rtnInfo;
//	}
//	
//	@RequestMapping(value = "/checktm", produces = "application/json;charset=UTF-8")
//	@ResponseBody
//	public ReturnInfo checkTm(GeneralCondition gcon,String regNumber){
//		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
//		if(rtnInfo != null && rtnInfo.getSuccess()){//通过身份验证
//			rtnInfo = tradeMarkService.checkTm(regNumber);
//		}
//		return rtnInfo;
//	}
//	
//	@RequestMapping(value = "/exporttm", produces = "application/json;charset=UTF-8")
//	@ResponseBody
//	public Object exportTm(HttpServletRequest request, HttpServletResponse response,Integer govId,Integer entId,Date appStartDate,
//    		Date appEndDate,GeneralCondition gcon, Trademark trademark,Date regStartDate,
//    		Date regEndDate, Date statusStartDate, Date statusEndDate,Integer regionId,String appName,String excludeBranch,String entName,String column) {
//		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
//		if(rtnInfo != null && rtnInfo.getSuccess()){//通过身份验证
//			return tradeMarkService.exportTm(request,response,govId,entId,appStartDate,appEndDate,gcon,trademark,
//					regStartDate,regEndDate,statusStartDate,statusEndDate,excludeBranch,entName,column);
//		}
//		return rtnInfo;
//	}
//	//检查禁止注册商标
//	@RequestMapping(value = "/checkForbidContent", produces = "application/json;charset=UTF-8")
//	@ResponseBody
//	public Object checkForbidContent(String content,GeneralCondition gcon) {
//		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
//		if(rtnInfo != null && rtnInfo.getSuccess()){//通过身份验证
//			ForbidContent fc=forbidContentService.checkForbidContent(content);
//			if (fc==null) {				
//				fc=new ForbidContent();
//				fc.setContent(content);
//				fc.setCause("noforbid");				
//			}
//			rtnInfo.setSuccess(true);			
//			rtnInfo.setData(fc);			
//		}
//		return rtnInfo;
//	}	
//	//查询商标通过tmId
//	@RequestMapping(value = "/querytmbytmid", produces = "application/json;charset=UTF-8")
//	@ResponseBody
//	public ReturnInfo queryTmListByTmId(HttpServletRequest request,Integer tmId,GeneralCondition gcon) throws Exception {
//		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
//		makeOffsetAndRows(gcon);
//		rtnInfo = tradeMarkService.queryTmListByTmId(tmId,gcon);
//		return rtnInfo;
//		
//	}


}