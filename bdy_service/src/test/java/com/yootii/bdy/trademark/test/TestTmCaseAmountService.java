package com.yootii.bdy.trademark.test;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.tmcase.model.TradeMarkCase;
import com.yootii.bdy.tmcase.service.TradeMarkCaseAmountService;
import com.yootii.bdy.trademark.model.Trademark;
import com.yootii.bdy.trademark.service.TradeMarkService;
import com.yootii.bdy.util.JsonUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mybatis.xml" })
public class TestTmCaseAmountService {
	
	@Resource
	private TradeMarkCaseAmountService tradeMarkCaseAmountService;
	private static final Logger logger = Logger.getLogger(TestTmCaseAmountService.class);
	
	@Test
	public void queryTmCaseByCustomerTest() {
		TradeMarkCase tradeMarkCase =new TradeMarkCase();
		tradeMarkCase.setCustId(1);
		GeneralCondition gcon = new GeneralCondition();
		//gcon.setStartYear(2017);
		//gcon.setEndYear(2018);
		Trademark trademark = new Trademark();
		Object info;
		String interfacetype="near5year";
		try {
			
			info = tradeMarkCaseAmountService.queryTmCaseByCustomer(tradeMarkCase, gcon, interfacetype);
			if (info != null) {
				logger.info(JsonUtil.toJson(info));
				logger.info("queryTmCaseByCustomerTest测试通过");
			}
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	@Test
	public void queryTmCaseAmountByAgencyUserTest() {
		TradeMarkCase tradeMarkCase =new TradeMarkCase();
		tradeMarkCase.setCustId(1);
		GeneralCondition gcon = new GeneralCondition();
		//gcon.setStartYear(2017);
		//gcon.setEndYear(2018);
		Trademark trademark = new Trademark();
		Object info;
		String interfacetype="near5year";
		try {
			
			info = tradeMarkCaseAmountService.queryTmCaseAmountByAgencyUser(tradeMarkCase, gcon, interfacetype, 1);
			if (info != null) {
				logger.info(JsonUtil.toJson(info));
				logger.info("queryTmCaseByCustomerTest测试通过");
			}
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	
	@Test
	public void queryTmCaseAmountByAgencyTest() {
		TradeMarkCase tradeMarkCase =new TradeMarkCase();
		tradeMarkCase.setAgencyId(1);
		tradeMarkCase.setCustId(1);
		GeneralCondition gcon = new GeneralCondition();
		gcon.setStartYear(2017);
		gcon.setEndYear(2018);
		Trademark trademark = new Trademark();
		Object info;
		String interfacetype="near5year";
		try {
			
			info = tradeMarkCaseAmountService.queryTmCaseAmountByAgency(tradeMarkCase, gcon, interfacetype);
			if (info != null) {
				logger.info(JsonUtil.toJson(info));
				logger.info("queryTmCaseAmountByAgencyTest测试通过");
			}
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	
	@Test
	public void queryTmCaseAmountByPalteformTest() {
		TradeMarkCase tradeMarkCase =new TradeMarkCase();
		tradeMarkCase.setAgencyId(1);
		tradeMarkCase.setCustId(1);
		GeneralCondition gcon = new GeneralCondition();
		//gcon.setStartYear(2017);
		//gcon.setEndYear(2018);
		Trademark trademark = new Trademark();
		Object info;
		String interfacetype="near5year";
		try {
			
			//info = tradeMarkCaseAmountService.queryTmCaseAmountByPalteform(tradeMarkCase, gcon, interfacetype);
			info=tradeMarkCaseAmountService.statstmCasetop10(gcon);
			if (info != null) {
				logger.info(JsonUtil.toJson(info));
				logger.info("queryTmCaseAmountByPalteformTest测试通过");
			}
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
}
