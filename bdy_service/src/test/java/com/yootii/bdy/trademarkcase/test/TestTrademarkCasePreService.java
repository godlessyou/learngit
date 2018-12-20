package com.yootii.bdy.trademarkcase.test;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yootii.bdy.tmcase.dao.TradeMarkCasePreMapper;
import com.yootii.bdy.tmcase.model.TradeMarkCaseCategory;
import com.yootii.bdy.tmcase.model.TradeMarkCasePre;
import com.yootii.bdy.tmcase.service.TradeMarkCasePreService;
import com.yootii.bdy.util.JsonUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mybatis.xml" })
public class TestTrademarkCasePreService {
	@Resource
	private TradeMarkCasePreService tradeMarkCasePreService;
	@Resource
	private TradeMarkCasePreMapper tradeMarkCasePreMapper;
	private static final Logger logger = Logger.getLogger(TestTrademarkCasePreService.class);
//	@Test
	public void createTmcaseTest() {
		TradeMarkCasePre tradeMarkCase = new TradeMarkCasePre();
		tradeMarkCase.setCustId(1);
		tradeMarkCase.setAgencyId(1);
		tradeMarkCase.setTmName("腾讯");
		tradeMarkCase.setApplicantType("中国大陆");
		TradeMarkCaseCategory good = new TradeMarkCaseCategory();
		good.setGoodClass("2");
		good.setGoodName("哈哈哈");
		TradeMarkCaseCategory good2 = new TradeMarkCaseCategory();
		good2.setGoodClass("3");
		good2.setGoodName("哈哈哈ha");
		TradeMarkCaseCategory good3 = new TradeMarkCaseCategory();
		good3.setGoodClass("4");
		good3.setGoodName("哈哈哈hahaha");
		List<TradeMarkCaseCategory> goods = new ArrayList<TradeMarkCaseCategory>();
		goods.add(good);
		goods.add(good2);
		goods.add(good3);
		tradeMarkCase.setGoods(goods);
		Object info =tradeMarkCasePreService.createTradeMarkCasePre(tradeMarkCase);
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("queryUserList测试通过");
		}
	}
//	@Test
	public void modifyTmcaseTest() {
		TradeMarkCasePre tradeMarkCasePre = new TradeMarkCasePre();
		tradeMarkCasePre.setId(312);
		tradeMarkCasePre.setCustId(1);
		tradeMarkCasePre.setAgencyId(1);
		tradeMarkCasePre.setTmName("腾讯游戏");
		tradeMarkCasePre.setApplicantType("法人或其它组织");
		tradeMarkCasePre.setGoodClasses("2;3");
		TradeMarkCaseCategory good = new TradeMarkCaseCategory();
		good.setGoodClass("2");
		good.setGoodName("哈哈哈");
		TradeMarkCaseCategory good2 = new TradeMarkCaseCategory();
		good2.setGoodClass("3");
		good2.setGoodName("哈哈哈哈");
		List<TradeMarkCaseCategory> goods = new ArrayList<TradeMarkCaseCategory>();
		goods.add(good);
		goods.add(good2);
		tradeMarkCasePre.setGoods(goods);
		Object info =tradeMarkCasePreService.modifyTradeMarkCasePre(tradeMarkCasePre);
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("queryUserList测试通过");
		}
	}
	@Test
	public void queryDetailTest() {
//		Object info =tradeMarkCasePreService.queryDetailByCustIdAndAgencyId(1, 1);
//		if (info != null) {
//			logger.info(JsonUtil.toJson(info));
//			logger.info("queryUserList测试通过");
//		}
	}
	
	
	
}
