package com.yootii.bdy.trademarkcase.test;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yootii.bdy.tmcase.model.TradeMarkCaseCategory;
import com.yootii.bdy.tmcase.service.TradeMarkCaseCategoryService;
import com.yootii.bdy.util.JsonUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mybatis.xml" })
public class TestTrademarkCaseCategoryService {
	@Resource
	private TradeMarkCaseCategoryService tradeMarkCaseCategoryService;
	private static final Logger logger = Logger.getLogger(TestTrademarkCaseCategoryService.class);
//	@Test
	public void createTmCategoryTest() {
		TradeMarkCaseCategory Category = new TradeMarkCaseCategory();
		Category.setGoodName("hahaha");
//		Category.setCasePreId(304);
		Category.setCaseId(365);
		Object info =tradeMarkCaseCategoryService.createTradeMarkCaseCategory(Category);
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("queryUserList测试通过");
		}
	}
	@Test
	public void modifyTmCategoryTest() {
		TradeMarkCaseCategory Category = new TradeMarkCaseCategory();
		Category.setId(1898);
		Category.setGoodName("xixixi");
		Object info =tradeMarkCaseCategoryService.modifyTradeMarkCaseCategory(Category);
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("queryUserList测试通过");
		}
	}
	@Test
	public void queryTmCategoryTest() {
		Object info =tradeMarkCaseCategoryService.queryTradeMarkCaseCategoryById(1898);
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("queryUserList测试通过");
		}
	}
//	@Test
	public void deleteTmCategoryTest() {
		Object info =tradeMarkCaseCategoryService.deleteTradeMarkCaseCategoryById(1882);
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("queryUserList测试通过");
		}
	}
//	@Test
	public void queryTmCategoryListTest() {
		TradeMarkCaseCategory Category = new TradeMarkCaseCategory();
		Category.setCaseId(365);
		Object info =tradeMarkCaseCategoryService.queryTradeMarkCaseCategoryList(Category);
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("queryUserList测试通过");
		}
	}
	@Test
	public void checkTmCategoryListTest() {
		List<TradeMarkCaseCategory> goods = new ArrayList<TradeMarkCaseCategory>();
		TradeMarkCaseCategory Category = new TradeMarkCaseCategory();
		Category.setGoodName("锑");
		Category.setGoodCode("010074");
		TradeMarkCaseCategory Category2 = new TradeMarkCaseCategory();
		Category2.setGoodNameEn("argon");
		Category2.setGoodCode("010082");
		goods.add(Category);
		goods.add(Category2);
		Object info =tradeMarkCaseCategoryService.checkTradeMarkCaseCategoryList(goods);
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("queryUserList测试通过");
		}
	}
}
