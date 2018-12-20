package com.yootii.bdy.trademarkcase.test;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.tmcase.model.TradeMarkCase;
import com.yootii.bdy.tmcase.model.TradeMarkCaseProcess;
import com.yootii.bdy.tmcase.service.TradeMarkCaseProcessService;
import com.yootii.bdy.tmcase.service.TradeMarkCaseService;
import com.yootii.bdy.util.JsonUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mybatis.xml" })
public class TestTrademarkCaseProcessService {
	@Resource
	private TradeMarkCaseProcessService tradeMarkCaseProcessService;
	private static final Logger logger = Logger.getLogger(TestTrademarkCaseProcessService.class);
//	@Test
	public void createTmcaseTest() {
		TradeMarkCaseProcess tradeMarkCaseProcess = new TradeMarkCaseProcess();
		tradeMarkCaseProcess.setCaseId(1);
		tradeMarkCaseProcess.setStatus("注册申请");
		tradeMarkCaseProcess.setUsername("张三");
		Object info =tradeMarkCaseProcessService.createTradeMarkCaseProcess(tradeMarkCaseProcess);
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("queryUserList测试通过");
		}
	}
//	@Test
	public void modifyTmcaseTest() {
		TradeMarkCaseProcess tradeMarkCaseProcess = new TradeMarkCaseProcess();
		tradeMarkCaseProcess.setId(1);
		tradeMarkCaseProcess.setCaseId(1);
		tradeMarkCaseProcess.setStatus("注册申请递交官方");
		tradeMarkCaseProcess.setUsername("张三");
		Object info =tradeMarkCaseProcessService.modifyTradeMarkCaseProcess(tradeMarkCaseProcess);
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("queryUserList测试通过");
		}
	}
	@Test
	public void queryTmcaseTest() {
		TradeMarkCaseProcess tradeMarkCaseProcess = new TradeMarkCaseProcess();
		tradeMarkCaseProcess.setCaseId(1);
		tradeMarkCaseProcess.setStatus("注册申请递交官方");
		Object info =tradeMarkCaseProcessService.queryTradeMarkCaseProcess(tradeMarkCaseProcess);
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("queryUserList测试通过");
		}
	}
}
