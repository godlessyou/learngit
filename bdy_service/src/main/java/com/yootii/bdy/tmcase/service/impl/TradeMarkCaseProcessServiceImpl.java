package com.yootii.bdy.tmcase.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.tmcase.dao.TradeMarkCaseProcessMapper;
import com.yootii.bdy.tmcase.model.TradeMarkCaseProcess;
import com.yootii.bdy.tmcase.service.TradeMarkCaseProcessService;

@Service
public class TradeMarkCaseProcessServiceImpl implements TradeMarkCaseProcessService{
	
	@Resource
	private TradeMarkCaseProcessMapper tradeMarkCaseProcessMapper;
	
	@Override
	public ReturnInfo createTradeMarkCaseProcess(
			TradeMarkCaseProcess tradeMarkCaseProcess) {
		ReturnInfo info = new ReturnInfo();
		tradeMarkCaseProcessMapper.insertSelective(tradeMarkCaseProcess);
		info.setSuccess(true);
		info.setMessage("创建成功");
		return info;
	}

	@Override
	public ReturnInfo modifyTradeMarkCaseProcess(
			TradeMarkCaseProcess tradeMarkCaseProcess) {
		ReturnInfo info = new ReturnInfo();
		Date statusTime=new Date();
		tradeMarkCaseProcess.setStatusTime(statusTime);
		tradeMarkCaseProcessMapper.updateByPrimaryKeySelective(tradeMarkCaseProcess);
		info.setSuccess(true);
		info.setMessage("修改成功");
		return info;
	}

	@Override
	public ReturnInfo queryTradeMarkCaseProcess(
			TradeMarkCaseProcess tradeMarkCaseProcess) {
		ReturnInfo info = new ReturnInfo();
		Integer caseId = tradeMarkCaseProcess.getCaseId();
		if(caseId==null){
			info.setSuccess(true);
			info.setMessage("案件ID不能为空");
			return info;
		}
		List<TradeMarkCaseProcess> tmCaseProcesses = tradeMarkCaseProcessMapper.selectByTmCaseProcess(tradeMarkCaseProcess);
		info.setData(tmCaseProcesses);
		info.setSuccess(true);
		info.setMessage("查询成功");
		return info;
	}

}
