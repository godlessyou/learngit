package com.yootii.bdy.task.test;
import java.text.SimpleDateFormat;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.LoginReturnInfo;
import com.yootii.bdy.customer.model.Customer;
import com.yootii.bdy.model.User;
import com.yootii.bdy.security.service.AuthenticationService;

import com.yootii.bdy.tmcase.model.TradeMarkCase;
import com.yootii.bdy.tmcase.model.TradeMarkCasePre;
import com.yootii.bdy.tmcase.service.TradeMarkCasePreService;
import com.yootii.bdy.trademark.model.Trademark;
import com.yootii.bdy.trademark.service.TradeMarkService;
import com.yootii.bdy.util.JsonUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mybatis.xml" })
public class TestTrademarkRegister {
	
	@Resource
	private AuthenticationService authenticationService;
	
	private static final Logger logger = Logger.getLogger(TestTrademarkRegister.class);

	@Test
	public void testStartTmRegisterSubProcess() {
//		
//		GeneralCondition gcon = new GeneralCondition();
//		
//		Customer customer=new Customer();
//		Integer customerId=1;
//		customer.setId(1);		
//		customer.setUserType(20);
//		customer.setUsername("guanxiao");
//		customer.setPassword("123456");		
//		TradeMarkCase tmCase = new TradeMarkCase();
//		
//		Integer caseId=1;
//		Integer agencyId=1;
//		tmCase.setId(caseId);
//		tmCase.setAgencyId(agencyId);
//					
//		String tokenID=customerLogin(customer);
//		gcon.setTokenID(tokenID);
//		
//		
//		Object info =tradeMarkRegisterTaskService.startTmRegisterSubProcess(gcon, null, customerId.toString(), tmCase);
//		if (info != null) {
//			logger.info(JsonUtil.toJson(info));
//			logger.info("startTmRegisterSubProcess测试通过");
//		}	
//		

	}
	
	
	private String customerLogin(Customer customer) {
		
		
		Object obj = authenticationService.customerin(customer);				
		LoginReturnInfo rtnInfo = (LoginReturnInfo)obj;
		
		String tokenID = rtnInfo.getTokenID();
		
		return tokenID;
		
	}
}
