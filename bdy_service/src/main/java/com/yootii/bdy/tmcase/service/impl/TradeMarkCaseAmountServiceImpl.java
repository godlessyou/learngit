package com.yootii.bdy.tmcase.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.tmcase.dao.TradeMarkCaseAmountMapper;
import com.yootii.bdy.tmcase.model.RetuenTmCaseAmount;
import com.yootii.bdy.tmcase.model.ReturnTmCaseAmountYear;
import com.yootii.bdy.tmcase.model.TradeMarkCase;
import com.yootii.bdy.tmcase.service.TradeMarkCaseAmountService;
@Service
public class TradeMarkCaseAmountServiceImpl implements TradeMarkCaseAmountService{
	
	@Resource
	private TradeMarkCaseAmountMapper tradeMarkCaseAmountMapper;
	@Override
	public ReturnInfo queryTmCaseByCustomer(TradeMarkCase tradeMarkCase, GeneralCondition gcon, String interfacetype) {
		ReturnInfo returnInfo = new ReturnInfo();
		 if(interfacetype.equals("yearAndAgency")) {
			List<ReturnTmCaseAmountYear> r=tradeMarkCaseAmountMapper.selectTmCaseByCustomerYear(tradeMarkCase,gcon,interfacetype);
			returnInfo.setData(r);
		}else {
			List<RetuenTmCaseAmount> r=tradeMarkCaseAmountMapper.selectTmCaseByCustomer(tradeMarkCase,gcon,interfacetype);
			returnInfo.setData(r);
		}
		
		returnInfo.setSuccess(true);
		
		returnInfo.setMessage("查询成功");
		return returnInfo;
	}
	@Override
	public ReturnInfo queryTmCaseAmountByAgencyUser(TradeMarkCase tradeMarkCase, GeneralCondition gcon,
			String interfacetype,Integer userId) {
		ReturnInfo returnInfo = new ReturnInfo();
		
		List<RetuenTmCaseAmount> r=tradeMarkCaseAmountMapper.selectTmCaseByAgencyUser(tradeMarkCase,gcon,interfacetype,userId);
		returnInfo.setData(r);
		
		
		returnInfo.setSuccess(true);
		
		returnInfo.setMessage("查询成功");
		return returnInfo;
	}
	@Override
	public ReturnInfo queryTmCaseAmountByAgency(TradeMarkCase tradeMarkCase, GeneralCondition gcon,
			String interfacetype) {
		ReturnInfo returnInfo = new ReturnInfo();
		
		List<RetuenTmCaseAmount> r=tradeMarkCaseAmountMapper.queryTmCaseAmountByAgency(tradeMarkCase,gcon,interfacetype);
		returnInfo.setData(r);
		
		
		returnInfo.setSuccess(true);
		
		returnInfo.setMessage("查询成功");
		return returnInfo;
	}
	@Override
	public ReturnInfo queryTmCaseAmountByPalteform(TradeMarkCase tradeMarkCase, GeneralCondition gcon,
			String interfacetype) {
		ReturnInfo returnInfo = new ReturnInfo();
		
		List<RetuenTmCaseAmount> r=tradeMarkCaseAmountMapper.queryTmCaseAmountByPalteform(tradeMarkCase,gcon,interfacetype);
		returnInfo.setData(r);
		
		
		returnInfo.setSuccess(true);
		
		returnInfo.setMessage("查询成功");
		return returnInfo;
	}
	@Override
	public ReturnInfo statstmCasetop10(GeneralCondition gcon) {
		ReturnInfo returnInfo = new ReturnInfo();
		
		List<Map<String,String>> r=tradeMarkCaseAmountMapper.statstmCasetop10(gcon);
		returnInfo.setData(r);
		
		
		returnInfo.setSuccess(true);
		
		returnInfo.setMessage("查询成功");
		return returnInfo;
	}

}
