package com.yootii.bdy.trademarkcase.test;

import java.text.SimpleDateFormat;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yootii.bdy.tmcase.model.TradeMarkCaseJoinApp;
import com.yootii.bdy.tmcase.model.TradeMarkCasePre;
import com.yootii.bdy.tmcase.service.TradeMarkCaseJoinAppService;
import com.yootii.bdy.tmcase.service.TradeMarkCasePreService;
import com.yootii.bdy.util.JsonUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mybatis.xml" })
public class TestTrademarkCaseJoinAppService {
	@Resource
	private TradeMarkCaseJoinAppService tradeMarkCaseJoinAppService;
	private static final Logger logger = Logger.getLogger(TestTrademarkCaseJoinAppService.class);
//	@Test
	public void createTmJoinAppTest() {
		TradeMarkCaseJoinApp joinApp = new TradeMarkCaseJoinApp();
		joinApp.setCustId(22);
		joinApp.setAgencyId(22);
		joinApp.setNameCn("哈哈哈");
		Object info =tradeMarkCaseJoinAppService.createTradeMarkCaseJoinApp(joinApp);
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("queryUserList测试通过");
		}
	}
	@Test
	public void modifyTmJoinAppTest() {
		TradeMarkCaseJoinApp joinApp = new TradeMarkCaseJoinApp();
		joinApp.setId(5);
		joinApp.setCustId(22);
		joinApp.setAgencyId(22);
		joinApp.setAddrCn("北京海淀区");
		Object info =tradeMarkCaseJoinAppService.modifyTradeMarkCaseJoinApp(joinApp);
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("queryUserList测试通过");
		}
	}
	@Test
	public void queryTmJoinAppTest() {
		Object info =tradeMarkCaseJoinAppService.queryTradeMarkCaseJoinAppById(1);
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("queryUserList测试通过");
		}
	}
	@Test
	public void deleteTmJoinAppTest() {
		Object info =tradeMarkCaseJoinAppService.deleteTradeMarkCaseJoinAppById(4);
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("queryUserList测试通过");
		}
	}
//	@Test
	public void queryTmJoinAppListTest() {
		TradeMarkCaseJoinApp joinApp = new TradeMarkCaseJoinApp();
		joinApp.setCustId(22);
		joinApp.setAgencyId(22);
		Object info =tradeMarkCaseJoinAppService.queryTradeMarkCaseJoinAppList(joinApp);
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("queryUserList测试通过");
		}
	}
}
