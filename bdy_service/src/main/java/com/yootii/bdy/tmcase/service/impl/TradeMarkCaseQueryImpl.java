package com.yootii.bdy.tmcase.service.impl;

import java.util.ArrayList;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.Globals;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.ipinfo.service.ForbidContentService;
import com.yootii.bdy.material.model.Material;
import com.yootii.bdy.security.model.Token;
import com.yootii.bdy.solr.SolrInfo;
import com.yootii.bdy.solr.SolrSendTrademarkCase;
import com.yootii.bdy.tmcase.dao.TradeMarkCaseMapper;
import com.yootii.bdy.tmcase.dao.TradeMarkCaseTypeMapper;
import com.yootii.bdy.tmcase.model.DissentPerson;
import com.yootii.bdy.tmcase.model.TmCaseAppOnline;
import com.yootii.bdy.tmcase.model.TradeMarkCase;
import com.yootii.bdy.tmcase.model.TradeMarkCaseJoinApp;
import com.yootii.bdy.tmcase.model.TradeMarkCaseSolr;
import com.yootii.bdy.tmcase.model.TradeMarkCaseType;
import com.yootii.bdy.trademark.model.Trademark;

@Component
public class TradeMarkCaseQueryImpl {

	private final Logger logger = Logger.getLogger(this.getClass());

	@Resource
	private SolrInfo solrInfo;

	@Resource
	private TradeMarkCaseMapper tradeMarkCaseMapper;

	@Resource
	private TradeMarkCaseTypeMapper tradeMarkCaseTypeMapper;

	@Resource
	private ForbidContentService forbidContentService;
	
	
	

	public ReturnInfo queryTradeMarkCaseList(TradeMarkCase tradeMarkCase,
			GeneralCondition gcon, Token token, int isFinished) {
		ReturnInfo info = new ReturnInfo();
		if (token == null) {
			info.setSuccess(false);
			info.setMessage("查询失败");
			return info;
		}
		Integer userId = null;
		/* tradeMarkCase.setUsername(gcon.getKeyword()); */
		if (token.isUser()) {// 是user登录，为代理机构人员
			userId = token.getUserID();
			// 根据userId获取agencyId
			// 是否是代理机构管理员
			List<String> roles = tradeMarkCaseMapper.checkRole(userId);
			if (roles != null && roles.size() > 0) {
				for (String s : roles) {
					if ("代理机构管理员".equals(s) || "公司领导".equals(s)) {// 查询当前机构所有案件
						List<TradeMarkCase> tmcases = tradeMarkCaseMapper
								.selectAllByTmCase(tradeMarkCase, gcon, userId,
										isFinished);
						// if(tmcases != null) {
						// for(TradeMarkCase t : tmcases) {
						// List<Material> materials = t.getMaterials();
						// if(materials != null) {
						// for(Material m : materials) {
						// String fileName = processFileName(m.getSubject());
						// m.setFileName(fileName);
						// }
						// }
						// }
						// }

						List<Map<String, Long>> counts = tradeMarkCaseMapper
								.selectAllByTmCaseCount(tradeMarkCase, gcon,
										userId, isFinished);
						Long total = 0L;
						for (Map<String, Long> onecount : counts) {
							total += onecount.get("count");
						}
						info.setTotal(total);
						info.setCurrPage(gcon.getPageNo());
						info.setData(tmcases);
						info.setSuccess(true);
						info.setMessage("查询案件列表成功");
						return info;
					} else if ("一级部门负责人".equals(s)) {
						// 该部门下包括二级部门的所有用户相关的案件
						Integer level = 0;
						List<TradeMarkCase> tmcases = tradeMarkCaseMapper
								.selectTmCaseByDept(tradeMarkCase, gcon,
										userId, level, isFinished);
						// if(tmcases != null) {
						// for(TradeMarkCase t : tmcases) {
						// List<Material> materials = t.getMaterials();
						// if(materials != null) {
						// for(Material m : materials) {
						// String fileName = processFileName(m.getSubject());
						// m.setFileName(fileName);
						// }
						// }
						// }
						// }
						List<Map<String, Long>> counts = tradeMarkCaseMapper
								.selectTmCaseByDeptCount(tradeMarkCase, gcon,
										userId, level, isFinished);
						Long total = 0L;
						for (Map<String, Long> onecount : counts) {
							total += onecount.get("count");
						}
						info.setTotal(total);
						info.setCurrPage(gcon.getPageNo());
						info.setData(tmcases);
						info.setSuccess(true);
						info.setMessage("查询案件列表成功");
						return info;
					} else if ("二级部门负责人".equals(s)) {
						// 二级部门下的所有用户相关的案件
						Integer level = 1;
						List<TradeMarkCase> tmcases = tradeMarkCaseMapper
								.selectTmCaseByDept(tradeMarkCase, gcon,
										userId, level, isFinished);
						// if(tmcases != null) {
						// for(TradeMarkCase t : tmcases) {
						// List<Material> materials = t.getMaterials();
						// if(materials != null) {
						// for(Material m : materials) {
						// String fileName = processFileName(m.getSubject());
						// m.setFileName(fileName);
						// }
						// }
						// }
						// }
						List<Map<String, Long>> counts = tradeMarkCaseMapper
								.selectTmCaseByDeptCount(tradeMarkCase, gcon,
										userId, level, isFinished);
						Long total = 0L;
						for (Map<String, Long> onecount : counts) {
							total += onecount.get("count");
						}
						info.setTotal(total);
						info.setCurrPage(gcon.getPageNo());
						info.setData(tmcases);
						info.setSuccess(true);
						info.setMessage("查询案件列表成功");
						return info;
					}
				}
			}
		} else {// 是客户
			Integer custId = token.getCustomerID();
			tradeMarkCase.setCustId(custId);
		}
		// 客户和普通代理人查询
		List<TradeMarkCase> tmcases = tradeMarkCaseMapper.selectByTmCase(
				tradeMarkCase, gcon, userId, isFinished);
		// if(tmcases != null) {
		// for(TradeMarkCase t : tmcases) {
		// List<Material> materials = t.getMaterials();
		// if(materials != null) {
		// for(Material m : materials) {
		// String fileName = processFileName(m.getSubject());
		// m.setFileName(fileName);
		// }
		// }
		// }
		// }
		List<Map<String, Long>> counts = tradeMarkCaseMapper
				.selectByTmCaseCount(tradeMarkCase, gcon, userId, isFinished);
		Long total = 0L;
		for (Map<String, Long> onecount : counts) {
			total += onecount.get("count");
		}
		info.setTotal(total);
		info.setCurrPage(gcon.getPageNo());
		info.setData(tmcases);
		info.setSuccess(true);
		info.setMessage("查询案件列表成功");
		return info;
	}

	public ReturnInfo queryTradeMarkCaseDetail(Integer id) {
		ReturnInfo info = new ReturnInfo();
		TradeMarkCase tmcase = tradeMarkCaseMapper.selectByPrimaryKey(id);
		info.setSuccess(true);
		info.setData(tmcase);
		info.setMessage("查询案件信息成功");
		return info;
	}

	public ReturnInfo queryTradeMarkCaseByProcessId(Integer processId) {
		ReturnInfo info = new ReturnInfo();
		TradeMarkCase tmcase = new TradeMarkCase();
		tmcase.setProcessId(processId.toString());
		tmcase = tradeMarkCaseMapper.selectTmCaseByProcessId(tmcase);
		info.setSuccess(true);
		info.setData(tmcase);
		info.setMessage("查询案件信息成功");
		return info;
	}

	public ReturnInfo queryAppOnlineCaseList(TradeMarkCase tradeMarkCase,
			GeneralCondition gcon, Token token) {
		ReturnInfo info = new ReturnInfo();
		if (token == null) {
			info.setSuccess(false);
			info.setMessage("查询失败");
			return info;
		}
		Integer userId = null;
		if (token.isUser()) {// 是user登录，为代理机构人员
			userId = token.getUserID();
			// 根据userId获取agencyId
			// 是否是代理机构管理员
			List<String> roles = tradeMarkCaseMapper.checkRole(userId);
			if (roles != null && roles.size() > 0) {
				for (String s : roles) {
					if ("代理机构管理员".equals(s) || "公司领导".equals(s)) {// 查询当前机构所有案件
						List<TradeMarkCase> tmcases = tradeMarkCaseMapper
								.selectAllAppOnlineCaseList(tradeMarkCase,
										gcon, userId);
						List<Map<String, Long>> counts = tradeMarkCaseMapper
								.selectAllAppOnlineCaseCount(tradeMarkCase,
										gcon, userId);
						Long total = 0L;
						for (Map<String, Long> onecount : counts) {
							total += onecount.get("count");
						}
						info.setTotal(total);
						info.setCurrPage(gcon.getPageNo());
						info.setData(tmcases);
						info.setSuccess(true);
						info.setMessage("查询案件列表成功");
						return info;
					} else if ("一级部门负责人".equals(s)) {
						// 该部门下包括二级部门的所有用户相关的案件
						Integer level = 0;
						List<TradeMarkCase> tmcases = tradeMarkCaseMapper
								.selectAppOnlineCaseListByDept(tradeMarkCase,
										gcon, userId, level);
						List<Map<String, Long>> counts = tradeMarkCaseMapper
								.selectAppOnlineCaseListByDeptCount(
										tradeMarkCase, gcon, userId, level);
						Long total = 0L;
						for (Map<String, Long> onecount : counts) {
							total += onecount.get("count");
						}
						info.setTotal(total);
						info.setCurrPage(gcon.getPageNo());
						info.setData(tmcases);
						info.setSuccess(true);
						info.setMessage("查询案件列表成功");
						return info;
					} else if ("二级部门负责人".equals(s)) {
						// 二级部门下的所有用户相关的案件
						Integer level = 1;
						List<TradeMarkCase> tmcases = tradeMarkCaseMapper
								.selectAppOnlineCaseListByDept(tradeMarkCase,
										gcon, userId, level);
						List<Map<String, Long>> counts = tradeMarkCaseMapper
								.selectAppOnlineCaseListByDeptCount(
										tradeMarkCase, gcon, userId, level);
						Long total = 0L;
						for (Map<String, Long> onecount : counts) {
							total += onecount.get("count");
						}
						info.setTotal(total);
						info.setCurrPage(gcon.getPageNo());
						info.setData(tmcases);
						info.setSuccess(true);
						info.setMessage("查询案件列表成功");
						return info;
					}
				}
			}
		} else {// 是客户
			Integer custId = token.getCustomerID();
			tradeMarkCase.setCustId(custId);
		}
		// 客户和普通代理人查询
		List<TmCaseAppOnline> tmcases = tradeMarkCaseMapper
				.getAppOnlineCaseList(tradeMarkCase, gcon, userId);
		Long count = tradeMarkCaseMapper.getAppOnlineCaseCount(tradeMarkCase,
				gcon, userId);

		Long total = (long) 0;
		if (count > 0) {
			total = count;
		}
		info.setTotal(total);
		info.setCurrPage(gcon.getPageNo());
		info.setData(tmcases);
		info.setSuccess(true);
		info.setMessage("查询案件列表成功");
		return info;
	}

	/**
	 * 查询网申所需的数据
	 */

	public ReturnInfo queryTradeMarkCaseForWs(Integer id) {
		ReturnInfo info = new ReturnInfo();
		TradeMarkCase tmcase = tradeMarkCaseMapper.selectByPrimaryKey(id);
		if (tmcase == null) {
			info.setSuccess(false);
			info.setMessageType(Globals.MESSAGE_TYPE_GETDATA_FAILED);
			info.setMessage("不存在案件编号为" + id + "的案件");
			return info;
		}
		processTradeMarkCaseInfo(tmcase);
		info.setSuccess(true);
		info.setData(tmcase);
		info.setMessage("查询案件信息成功");
		return info;
	}

	private void processTradeMarkCaseInfo(TradeMarkCase tmcase) {
		List<Material> files = tmcase.getMaterials();
		
		Integer caseTypeId=tmcase.getCaseTypeId();
		
		String attorneyFile=null;
		
		String transferorAttorneyFile=null;
		
		String assigneeAttorneyFile=null;
		
		for (Material file : files) {
			// Modification start, by yang guang, 2018-07-13
			/*
			 * String subject = file.getSubject(); int index =
			 * subject.indexOf("fileName :"); int index2 =
			 * subject.indexOf("joinAppId :"); Pattern pattern =
			 * Pattern.compile("^[-\\+]?[\\d]*$");
			 * 
			 * String fileNameStr = subject.substring(index+10,
			 * index2-1).trim(); Integer type = null;
			 * if(pattern.matcher(fileNameStr).matches()){ type =
			 * Integer.parseInt(fileNameStr); }
			 * 
			 * String joinAppStr =
			 * subject.substring(index2+11,subject.lastIndexOf(",")).trim();
			 * Integer joinAppId = null;
			 * if(pattern.matcher(joinAppStr).matches()){ joinAppId =
			 * Integer.parseInt(joinAppStr); }
			 */

			Integer type = file.getFileName();
			Integer joinAppId = file.getJoinAppId();

			// Modification end

			if (type == null) {
				continue;
			}
			switch (type) {
			case 1:
				
				//Modification start, by yang guang, 2018-12-19
				if (caseTypeId==5){
					if(attorneyFile==null){
						attorneyFile=file.getAddress();
					}else{
						attorneyFile=attorneyFile+";"+file.getAddress();
					}
				}
				//Modification end	
				
				break;
			case 2:
				tmcase.setAppCertFileCn(file.getAddress());
				break;
			case 3:
				tmcase.setAppCertFileEn(file.getAddress());
				break;
			case 4:
				tmcase.setCompanyCertFileCn(file.getAddress());
				break;
			case 5:
				tmcase.setCompanyCertFileEn(file.getAddress());
				break;
			case 6:
				tmcase.setMemberRuleFile(file.getAddress());
				break;
			case 7:
				tmcase.setMemberNamelistFile(file.getAddress());
				break;
			case 8:
				tmcase.setAppInspectionCertFile(file.getAddress());
				break;
			case 9:
				tmcase.setAppZyjcsbFile(file.getAddress());
				break;
			case 10:
				tmcase.setAppZyjsFile(file.getAddress());
				break;
			case 11:
				tmcase.setAppJsryzsFile(file.getAddress());
				break;
			case 12:
				tmcase.setAppWtjchtFile(file.getAddress());
				break;
			case 13:
				tmcase.setWtfrFile(file.getAddress());
				break;
			case 14:
				tmcase.setWtzzFile(file.getAddress());
				break;
			case 15:
				tmcase.setZyjcsbFile(file.getAddress());
				break;
			case 16:
				tmcase.setZyjsryFile(file.getAddress());
				break;
			case 17:
				tmcase.setRegionSignFile1(file.getAddress());
				break;
			case 18:
				tmcase.setRegionSignFile2(file.getAddress());
				break;
			case 19:
				tmcase.setRegionSignFile3(file.getAddress());
				break;
			case 20:
				tmcase.setRegionSignFile4(file.getAddress());
				break;
			case 21:
				tmcase.setRegionSignFile5(file.getAddress());
				break;
			case 22:
				tmcase.setVoiceFile(file.getAddress());
				break;
			case 23:
				tmcase.setPriorityFile(file.getAddress());
				break;
			case 24:
				tmcase.setImageFile(file.getAddress());
				break;
			case 25:
				tmcase.setPortraitFile(file.getAddress());
				break;
			case 26:
				tmcase.setRelatedFile(file.getAddress());
				break;
			case 27:
				tmcase.setBlackWhiteFile(file.getAddress());
				break;
			case 401:
				
				//Modification start, by yang guang, 2018-12-19
				if (transferorAttorneyFile==null){
					transferorAttorneyFile=file.getAddress();
				}else{
					transferorAttorneyFile=transferorAttorneyFile+";"+file.getAddress();
				}				
				break;
			case 402:				
				if (assigneeAttorneyFile==null){
					assigneeAttorneyFile=file.getAddress();
				}else{
					assigneeAttorneyFile=assigneeAttorneyFile+";"+file.getAddress();
				}				
				//Modification end
				
				break;
			case 403:
				tmcase.setGqcxNameFile(file.getAddress());
				break;
			case 404:
				tmcase.setTransferorCertFileCn(file.getAddress());
				break;
			case 405:
				tmcase.setAssigneeCertFileCn(file.getAddress());
				break;
			case 406:
				tmcase.setTransferorCertFileEn(file.getAddress());
				break;
			case 407:
				tmcase.setAssigneeCertFileEn(file.getAddress());
				break;
			case 408:
				tmcase.setTransferorCompanyCertFileCn(file.getAddress());
				break;
			case 409:
				tmcase.setAssigneeCompanyCertFileCn(file.getAddress());
				break;
			case 410:
				tmcase.setTransferorCompanyCertFileEn(file.getAddress());
				break;
			case 411:
				tmcase.setAssigneeCompanyCertFileEn(file.getAddress());
				break;
			case 412:
				tmcase.setFlwsNameFile(file.getAddress());
				break;
			case 413:
				tmcase.setSfzsFile(file.getAddress());
				break;
			case 414:
				tmcase.setZrhtFile(file.getAddress());
				break;
			case 415:
				tmcase.setGtzqNameFile(file.getAddress());
				break;
			case 501:
				tmcase.setBgCertFileCn(file.getAddress());
				break;
			case 502:
				tmcase.setBgCertFileEn(file.getAddress());
				break;
			case 101:
				if (joinAppId == null) {
					continue;
				}
				List<TradeMarkCaseJoinApp> joinApps1 = tmcase.getJoinApps();
				if (joinApps1.size() > 0) {
					for (TradeMarkCaseJoinApp joinApp : joinApps1) {
						if (joinApp.getId() == joinAppId) {
							joinApp.setGetSfFile(file.getAddress());
						}
					}
				}

				break;
			case 102:
				if (joinAppId == null) {
					continue;
				}
				List<TradeMarkCaseJoinApp> joinApps2 = tmcase.getJoinApps();
				if (joinApps2.size() > 0) {
					for (TradeMarkCaseJoinApp joinApp : joinApps2) {
						if (joinApp.getId() == joinAppId) {
							joinApp.setGetSfFileEn(file.getAddress());
						}
					}
				}
				break;
			case 103:
				if (joinAppId == null) {
					continue;
				}
				List<TradeMarkCaseJoinApp> joinApps3 = tmcase.getJoinApps();
				if (joinApps3.size() > 0) {
					for (TradeMarkCaseJoinApp joinApp : joinApps3) {
						if (joinApp.getId() == joinAppId) {
							joinApp.setGetZtFile(file.getAddress());
						}
					}
				}
				break;
			case 104:
				if (joinAppId == null) {
					continue;
				}
				List<TradeMarkCaseJoinApp> joinApps4 = tmcase.getJoinApps();
				if (joinApps4.size() > 0) {
					for (TradeMarkCaseJoinApp joinApp : joinApps4) {
						if (joinApp.getId() == joinAppId) {
							joinApp.setGetZtFileEn(file.getAddress());
						}
					}
				}
				break;
			}			
			
		}
		
		
		
		tmcase.setAttorneyFile(attorneyFile);
		tmcase.setTransferorAttorneyFile(transferorAttorneyFile);
		tmcase.setAssigneeAttorneyFile(assigneeAttorneyFile);
		
		
	}

	public ReturnInfo queryTradeMarkCaseBySolr(Integer departId,
			Integer custId, Integer agencyId, TradeMarkCaseSolr tradeMarkCase,
			String startYear, String endYear, GeneralCondition gcon) {
		ReturnInfo returnInfo = new ReturnInfo();
		try {
			// if(custId == null) throw new Exception("客户信息有误");

			SolrSendTrademarkCase solr = new SolrSendTrademarkCase();
			returnInfo.setSuccess(true);
			returnInfo.setMessage("查询成功");
			returnInfo.setCurrPage(gcon.getPageNo());
			return solr.selectTradeMarkCase(gcon, departId, custId, agencyId,
					tradeMarkCase, startYear, endYear, "createDate", solrInfo,
					returnInfo);

		} catch (Exception e) {
			returnInfo.setSuccess(false);
			returnInfo.setMessage("查询有误" + ":" + e.getMessage());
			e.printStackTrace();
			return returnInfo;
		}
	}

	/**
	 * 系统首页工作台 案件统计
	 */

	public ReturnInfo statsTmcaseProInfo(Integer custId, Integer agencyId,
			Integer departId) {
		ReturnInfo returnInfo = new ReturnInfo();
		try {
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			Integer Year = c.get(Calendar.YEAR);

			// if(departId==null)departId=0;

			SolrSendTrademarkCase solr = new SolrSendTrademarkCase();
			Map<String, Object> ret = new HashMap<String, Object>();
			ret.put("总数", solr.SelectTradeMarkCaseCount(solrInfo, custId,
					agencyId, departId, null, null, "createDate", null));
			ret.put("今年", solr.SelectTradeMarkCaseCount(solrInfo, custId,
					agencyId, departId, Year, Year, "createDate", null));
			ret.put("处理中", solr.SelectTradeMarkCaseCount(solrInfo, custId,
					agencyId, departId, null, null, "createDate",
					"NOT status:注册完成"));

			returnInfo.setData(ret);
			returnInfo.setSuccess(true);
			returnInfo.setMessage("查询成功");

			return returnInfo;

		} catch (Exception e) {
			returnInfo.setSuccess(false);
			returnInfo.setMessage("查询有误" + ":" + e.getMessage());
			e.printStackTrace();
			return returnInfo;
		}
	}

	public ReturnInfo statsTmagencyNameList(Integer custId, Integer startYear,
			Integer endYear) {
		ReturnInfo returnInfo = new ReturnInfo();
		try {

			SolrSendTrademarkCase solr = new SolrSendTrademarkCase();
			List<String> fieldList = new ArrayList<String>();
			fieldList.add("agencyName");
			returnInfo.setData(solr.SelectTradeMarkCaseByList(solrInfo, custId,
					null, null, startYear, endYear, fieldList, "createDate",
					true));
			returnInfo.setSuccess(true);
			returnInfo.setMessage("查询成功");
			return returnInfo;

		} catch (Exception e) {
			returnInfo.setSuccess(false);
			returnInfo.setMessage("查询有误" + ":" + e.getMessage());
			e.printStackTrace();
			return returnInfo;
		}
	}

	public ReturnInfo statsTmstatusList(Integer custId, Integer agencyId,
			Integer departId, Integer startYear, Integer endYear) {
		ReturnInfo returnInfo = new ReturnInfo();
		try {

			SolrSendTrademarkCase solr = new SolrSendTrademarkCase();
			List<String> fieldList = new ArrayList<String>();
			fieldList.add("createDateYear");
			fieldList.add("status");
			returnInfo.setData(fullTwoDeep(solr.SelectTradeMarkCaseByList(
					solrInfo, custId, agencyId, departId, startYear, endYear,
					fieldList, "createDate", true)));
			returnInfo.setSuccess(true);
			returnInfo.setMessage("查询成功");
			return returnInfo;

		} catch (Exception e) {
			returnInfo.setSuccess(false);
			returnInfo.setMessage("查询有误" + ":" + e.getMessage());
			e.printStackTrace();
			return returnInfo;
		}
	}

	public ReturnInfo statsTmcaseTypeList(Integer custId, Integer agencyId,
			Integer departId, Integer startYear, Integer endYear) {
		ReturnInfo returnInfo = new ReturnInfo();
		try {

			SolrSendTrademarkCase solr = new SolrSendTrademarkCase();
			List<String> fieldList = new ArrayList<String>();
			fieldList.add("createDateYear");
			fieldList.add("caseType");
			returnInfo.setData(fullTwoDeep(solr.SelectTradeMarkCaseByList(
					solrInfo, custId, agencyId, departId, startYear, endYear,
					fieldList, "createDate", true)));
			returnInfo.setSuccess(true);
			returnInfo.setMessage("查询成功");
			return returnInfo;

		} catch (Exception e) {
			returnInfo.setSuccess(false);
			returnInfo.setMessage("查询有误" + ":" + e.getMessage());
			e.printStackTrace();
			return returnInfo;
		}
	}

	public ReturnInfo statsTmappCnNameList(Integer custId, Integer agencyId,
			Integer departId, Integer startYear, Integer endYear) {
		ReturnInfo returnInfo = new ReturnInfo();
		try {

			SolrSendTrademarkCase solr = new SolrSendTrademarkCase();
			List<String> fieldList = new ArrayList<String>();
			fieldList.add("createDateYear");
			fieldList.add("appCnName");
			returnInfo.setData(fullTwoDeep(solr.SelectTradeMarkCaseByList(
					solrInfo, custId, agencyId, departId, startYear, endYear,
					fieldList, "createDate", true)));
			returnInfo.setSuccess(true);
			returnInfo.setMessage("查询成功");
			return returnInfo;

		} catch (Exception e) {
			returnInfo.setSuccess(false);
			returnInfo.setMessage("查询有误" + ":" + e.getMessage());
			e.printStackTrace();
			return returnInfo;
		}
	}

	public ReturnInfo statsTmDateList(Integer custId, Integer agencyId,
			Integer departId, Integer startYear, Integer endYear) {
		ReturnInfo returnInfo = new ReturnInfo();
		try {

			SolrSendTrademarkCase solr = new SolrSendTrademarkCase();
			List<String> fieldList = new ArrayList<String>();
			fieldList.add("createDateYear");
			returnInfo.setData(solr.SelectTradeMarkCaseByList(solrInfo, custId,
					agencyId, departId, startYear, endYear, fieldList,
					"createDate", true));
			returnInfo.setSuccess(true);
			returnInfo.setMessage("查询成功");
			return returnInfo;

		} catch (Exception e) {
			returnInfo.setSuccess(false);
			returnInfo.setMessage("查询有误" + ":" + e.getMessage());
			e.printStackTrace();
			return returnInfo;
		}
	}

	public ReturnInfo statsTmCustTop5List(Integer agencyId, Integer departId,
			Integer startYear, Integer endYear) {
		ReturnInfo returnInfo = new ReturnInfo();
		try {

			SolrSendTrademarkCase solr = new SolrSendTrademarkCase();
			List<String> fieldList = new ArrayList<String>();
			fieldList.add("custName");
			returnInfo.setData(Top((Map<String, Object>) solr
					.SelectTradeMarkCaseByList(solrInfo, null, agencyId,
							departId, startYear, endYear, fieldList,
							"createDate", true), 5, "custName"));
			returnInfo.setSuccess(true);
			returnInfo.setMessage("查询成功");
			return returnInfo;

		} catch (Exception e) {
			returnInfo.setSuccess(false);
			returnInfo.setMessage("查询有误" + ":" + e.getMessage());
			e.printStackTrace();
			return returnInfo;
		}
	}

	private Object fullTwoDeep(Object list) {
		List<String> titlelist = new ArrayList<String>();
		List<Map<String, Object>> retlist = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> a : (List<Map<String, Object>>) list) {
			if (a.get("value") instanceof List) {
				for (Map<String, Object> b : (List<Map<String, Object>>) a
						.get("value")) {
					if (!titlelist.contains(b.get("name"))) {
						titlelist.add(b.get("name").toString());
					}
				}
			}
		}
		for (Map<String, Object> a : (List<Map<String, Object>>) list) {
			if (a.get("value") instanceof List) {
				for (String title : titlelist) {
					if (!mapexitvalue(
							(List<Map<String, Object>>) a.get("value"), title)) {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("value", 0);
						map.put("name", title);
						((List<Map<String, Object>>) a.get("value")).add(map);
					}
				}

			} else {
				List<Map<String, Object>> maplist = new ArrayList<Map<String, Object>>();
				for (String title : titlelist) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("value", 0);
					map.put("name", title);
					maplist.add(map);
				}
				a.put("value", maplist);
			}
			retlist.add(a);
		}
		return retlist;
	}

	private boolean mapexitvalue(List<Map<String, Object>> list, String title) {
		for (Map<String, Object> a : list) {
			if (a.get("name").toString().equals(title))
				return true;
		}
		return false;
	}

	private Object Top(Map<String, Object> map, int i, String name) {
		try {
			Map<String, Object> ret = new HashMap<String, Object>();
			List<Map<String, Object>> list = (List<Map<String, Object>>) map
					.get(name);
			Collections.sort(list, new orderByCount());
			ret.put(name, list.subList(0, i));

			return ret;
		} catch (Exception e) {

			return map;
		}
	}

	
	
	class orderByCount implements Comparator<Map<String, Object>> {
		public int compare(Map<String, Object> s1, Map<String, Object> s2) {
			return (Integer.valueOf((s2.get("count").toString()))
					.compareTo(Integer.valueOf((s1.get("count").toString()))));
		}
	}

	
	
	public ReturnInfo queryCaseTypeList(Integer allType) {
		ReturnInfo info = new ReturnInfo();
		List<TradeMarkCaseType> list = null;
		if (allType == 1) {
			list = tradeMarkCaseTypeMapper.selectCaseTypeList();
		} else {
			list = tradeMarkCaseTypeMapper.selectAvalibleCaseType();
		}
		info.setSuccess(true);
		info.setData(list);
		info.setMessage("查询案件信息成功");
		return info;
	}

	
	
	//
	public ReturnInfo queryAboutByRegnumber(GeneralCondition gCondition,
			String regNumber, String goodClass) {

		ReturnInfo returnInfo = new ReturnInfo();

		returnInfo = forbidContentService.queryTm(regNumber, goodClass,
				gCondition);
		if (returnInfo.getSuccess()) {
			Trademark trademark = (Trademark) returnInfo.getData();
			DissentPerson dissentPerson = new DissentPerson();
			String tmName = trademark.getTmName();
			String applicantName = trademark.getApplicantName();
			String address = trademark.getApplicantAddress();
			String agent = trademark.getAgent();
			String imgFile = trademark.getImgFilePath();
			String approval = trademark.getApprovalNumber();
			Date date = trademark.getApprovalDate();
			dissentPerson.setAgentName(agent);
			dissentPerson.setApprovalDate(date);
			dissentPerson.setPersonAddress(address);
			dissentPerson.setPersonName(applicantName);
			dissentPerson.setImgFilePath(imgFile);
			dissentPerson.setApprovalNumber(approval);
			dissentPerson.setTmName(tmName);
			returnInfo.setData(dissentPerson);
		}
		return returnInfo;
	}
	
	


}
