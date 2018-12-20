package com.yootii.bdy.tmcase.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.material.dao.MaterialMapper;
import com.yootii.bdy.material.model.Material;
import com.yootii.bdy.tmcase.dao.TradeMarkCaseJoinAppMapper;
import com.yootii.bdy.tmcase.dao.TradeMarkCaseMapper;
import com.yootii.bdy.tmcase.model.TradeMarkCase;
import com.yootii.bdy.tmcase.model.TradeMarkCaseJoinApp;
import com.yootii.bdy.tmcase.service.TradeMarkCaseJoinAppService;

@Service
public class TradeMarkCaseJoinAppServiceImpl implements TradeMarkCaseJoinAppService{
	
	@Resource
	private TradeMarkCaseJoinAppMapper tradeMarkCaseJoinAppMapper;
	@Resource
	private MaterialMapper materialMapper;
	@Resource
	private TradeMarkCaseMapper tradeMarkCaseMapper;
	
	@Override
	public ReturnInfo createTradeMarkCaseJoinApp(
			TradeMarkCaseJoinApp joinApp) {
		ReturnInfo info = new ReturnInfo();
		Integer custId = joinApp.getCustId();
		Integer agencyId = joinApp.getAgencyId();
		Integer casePreId = joinApp.getCasePreId();//创建共同申请人时，只是绑定预立案的ID,正式案件的caseId在创建正式案件时绑定
		Integer caseId = joinApp.getCaseId();
		if(custId==null||agencyId==null||(casePreId==null&&caseId==null)){
			info.setSuccess(false);
			info.setMessage("客户ID、代理所ID和预立案ID不能为空");
			return info ;
		}
		if(casePreId!=null){
			joinApp.setCaseId(null);//预立案时不设置共同申请人的caseId
		}
		tradeMarkCaseJoinAppMapper.deleteByCustIdAndAgencyId(custId,agencyId,casePreId);//删除预立案ID小于tradeMarkCasePre.getId()，且caseId为空的数据删除
		tradeMarkCaseJoinAppMapper.insertSelective(joinApp);
		//设置案件中是否共同申请人为“是”
		if(joinApp.getCaseId()!=null){
			TradeMarkCase record = new TradeMarkCase();
			record.setId(joinApp.getCaseId());
			record.setIfShareTm("是");
			tradeMarkCaseMapper.updateByPrimaryKeySelective(record);
		}
		info.setSuccess(true);
		Map<String, Integer> data = new HashMap<String, Integer>();
		data.put("joinAppId", joinApp.getId());
		info.setData(data);//刚插入的记录，自增长的id已存在
		info.setMessage("操作成功");
		return info;
	}

	@Override
	public ReturnInfo modifyTradeMarkCaseJoinApp(TradeMarkCaseJoinApp joinApp) {
		ReturnInfo info = new ReturnInfo();
		if(joinApp.getId()==null){
			info.setSuccess(false);
			info.setMessage("共同申请人ID不能为空");
			return info ;
		}
		tradeMarkCaseJoinAppMapper.updateByPrimaryKeySelective(joinApp);
		info.setSuccess(true);
		info.setMessage("操作成功");
		return info;
	}

	@Override
	public ReturnInfo queryTradeMarkCaseJoinAppById(Integer id) {
		ReturnInfo info = new ReturnInfo();
		TradeMarkCaseJoinApp tradeMarkCaseJoinApp = tradeMarkCaseJoinAppMapper.selectByPrimaryKey(id);
		if (tradeMarkCaseJoinApp!=null){
//			String subject = "joinAppId : "+tradeMarkCaseJoinApp.getId();		
			List<Material>  m =materialMapper.selectMaterialByJoinApp(tradeMarkCaseJoinApp);
			tradeMarkCaseJoinApp.setMaterials(m);
			
		}
		info.setSuccess(true);
		info.setData(tradeMarkCaseJoinApp);
		info.setMessage("查询成功");
		
		return info;
	}

	@Override
	public ReturnInfo deleteTradeMarkCaseJoinAppById(Integer id) {
		ReturnInfo info = new ReturnInfo();
		//检查该案件是否有共同申请人
		TradeMarkCaseJoinApp joinApp = tradeMarkCaseJoinAppMapper.selectByPrimaryKey(id);
		
		tradeMarkCaseJoinAppMapper.deleteByPrimaryKey(id);
		
		if(joinApp!=null){
			Integer caseId = joinApp.getCaseId();
			if(caseId!=null){
				TradeMarkCaseJoinApp joinAppTmp = new TradeMarkCaseJoinApp();
				joinAppTmp.setCaseId(caseId);
				List<TradeMarkCaseJoinApp> joinApps = tradeMarkCaseJoinAppMapper.selectByTradeMarkCaseJoinApp(joinAppTmp);
				if(joinApps.size()==0){
					TradeMarkCase record = new TradeMarkCase();
					record.setId(caseId);
					record.setIfShareTm("否");
					tradeMarkCaseMapper.updateByPrimaryKeySelective(record);
				}
			}
			
		}
		
		info.setSuccess(true);
		info.setMessage("删除成功");
		return info;
	}

	@Override
	public ReturnInfo queryTradeMarkCaseJoinAppList(TradeMarkCaseJoinApp joinApp) {
		ReturnInfo info = new ReturnInfo();
		List<TradeMarkCaseJoinApp> joinApps = tradeMarkCaseJoinAppMapper.selectByTradeMarkCaseJoinApp(joinApp);
		for(TradeMarkCaseJoinApp t : joinApps) {
//			String subject = "joinAppId : "+t.getId();
			
			List<Material>  m =materialMapper.selectMaterialByJoinApp(t);
			t.setMaterials(m);
		}
		info.setSuccess(true);
		info.setData(joinApps);
		info.setMessage("查询成功");
		return info;
	}
}
