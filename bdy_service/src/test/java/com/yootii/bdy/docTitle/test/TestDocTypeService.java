package com.yootii.bdy.docTitle.test;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.downloadapplicant.model.DocTitleWithBLOBs;
import com.yootii.bdy.downloadapplicant.model.DocType;
import com.yootii.bdy.downloadapplicant.service.DocTitleService;
import com.yootii.bdy.downloadapplicant.service.DocTypeService;
import com.yootii.bdy.security.model.Token;
import com.yootii.bdy.security.service.AuthenticationService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mybatis.xml" })
public class TestDocTypeService {
	
	@Resource
	private AuthenticationService authenticationService;
	@Resource
	private DocTypeService docTypeService;
	
	private static final Logger logger = Logger.getLogger(TestDocTypeService.class);
//	@Test
	public void createTest() {
		Token token = new Token();
		token.setUser(true);
		token.setUserID(2);
		try {
			ReturnInfo result = docTypeService.createDocType(null, token);
			logger.info("result="+result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	public void queryTest() {
		DocType docType = new DocType();
		GeneralCondition gcon = new GeneralCondition();
		gcon.setPageNo(1);
		gcon.setPageSize(10);
		Token token = new Token();
//		token.setUser(true);
		token.setUserID(2);
		try {
			ReturnInfo result = docTypeService.queryDocType(docType, gcon, token);
			logger.info("result="+result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
