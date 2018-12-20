package com.yootii.bdy.customer.test;


import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.customer.model.Applicant;
import com.yootii.bdy.customer.model.Customer;
import com.yootii.bdy.customer.service.ApplicantService;
import com.yootii.bdy.customer.service.CustomerService;
import com.yootii.bdy.model.User;
import com.yootii.bdy.security.service.impl.AuthenticationServiceImpl;
import com.yootii.bdy.tmcase.model.TradeMarkCase;
import com.yootii.bdy.tmcase.service.TradeMarkCaseService;
import com.yootii.bdy.user.service.UserService;
import com.yootii.bdy.util.JsonUtil;


@RunWith(SpringJUnit4ClassRunner.class)
//表示继承了SpringJUnit4ClassRunner类
@ContextConfiguration(locations = { "classpath:spring-mybatis.xml" })
public class TestApplicantService {
	@Resource
	private ApplicantService applicantService;

	@Resource
	private TradeMarkCaseService tradeMarkCaseService;

	@Resource
	private AuthenticationServiceImpl authenticationServiceImpl;
	private static final Logger logger = Logger.getLogger(TestApplicantService.class);

	private MockHttpServletRequest request= new MockHttpServletRequest();
	

	//@Test
	public void testSaveApplicantByTm() {
		
		String tokenID=getTokenID();
		GeneralCondition gcon = new GeneralCondition();		
		gcon.setTokenID(tokenID);
		authenticationServiceImpl.authorize(gcon);
		
		
		TradeMarkCase tradeMarkCase = (TradeMarkCase) tradeMarkCaseService.queryTradeMarkCaseDetail(90).getData();
		
		if (tradeMarkCase!=null){
			Applicant applicant = new Applicant();
			
			applicant.setApplicantAddress(tradeMarkCase.getAppCnAddr());
			applicant.setApplicantEnAddress(tradeMarkCase.getAppEnAddr());
			applicant.setApplicantEnName(tradeMarkCase.getAppEnName());
			applicant.setApplicantName(tradeMarkCase.getAppCnName());
			applicant.setCountry(tradeMarkCase.getAppCountryOrRegion());
			applicant.setAppType(tradeMarkCase.getApplicantType());
			applicant.setCardName(tradeMarkCase.getAppCertificate());
			applicant.setCardNumber(tradeMarkCase.getAppCertificateNum());
			applicant.setSendType(tradeMarkCase.getAppGJdq());
			applicant.setUnifiedNumber(tradeMarkCase.getCertCode());
			
			applicantService.checkAndSaveApplicant(gcon, applicant, tradeMarkCase.getCustId());
		}
	}
	
	
	@Test
	public void testQueryApplicantByRegNumbers() {
		
		List<String>regNumbers=new ArrayList<String>();
		regNumbers.add("17188084");
		regNumbers.add("17188085");
		
//		List<Applicant> applicantList =applicantService.queryApplicantByRegNumbers(regNumbers);	
//		if (applicantList != null) {
//			logger.info(JsonUtil.toJson(applicantList));
//			logger.info("queryApplicantByRegNumbers测试通过");
//		}
	}
	
	
	@Test
	public void testQueryApplicantByName() {
		
      
	    String tokenID=getTokenID();
		
		String applicantName="微软公司";
		Applicant applicant=new Applicant();
		applicant.setApplicantName(applicantName);
		
		Applicant app=applicantService.getApplicantByName(applicant, tokenID);
		if (app != null) {
			logger.info(JsonUtil.toJson(app));
			logger.info("queryApplicantByRegNumbers测试通过");
		}
	}
	
	
	private String getTokenID(){
        User user = new User();
		
		user.setUsername("whd_wangfang");
		user.setPassword("123456");

		
	    String tokenID=authenticationServiceImpl.login(user).getTokenID();
	    
	    return tokenID;
	}

}
