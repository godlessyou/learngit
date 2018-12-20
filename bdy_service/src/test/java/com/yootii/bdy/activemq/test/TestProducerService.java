package com.yootii.bdy.activemq.test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yootii.bdy.activemq.ProducerService;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.tmcase.model.TradeMarkCase;
import com.yootii.bdy.tmcase.service.TradeMarkCaseService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mybatis.xml" })
public class TestProducerService {
	@Resource
	private ProducerService producerService;
	@Resource
	private TradeMarkCaseService tradeMarkCaseService;
	private static final Logger logger = Logger.getLogger(TestProducerService.class);
	@Test
	public void createTmcaseTest() {
		TradeMarkCase tradeMarkCase = new TradeMarkCase();
		
		ReturnInfo info =tradeMarkCaseService.queryTradeMarkCaseForWs(16128);
		if (info!=null){
			logger.info(info);
		}
//		tradeMarkCase =(TradeMarkCase) info.getData();
		
//		Integer caseProId=17;
//		
//		Integer agencyId=2;
//		
//		Boolean result=producerService.sendMapMessage(tradeMarkCase, caseProId, agencyId);
		logger.info(info);
			
	}

}
