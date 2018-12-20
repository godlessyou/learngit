package com.yootii.bdy.material.test;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.LoginReturnInfo;
import com.yootii.bdy.material.model.Material;
import com.yootii.bdy.material.model.MaterialSort;
import com.yootii.bdy.material.service.MaterialService;
import com.yootii.bdy.material.service.MaterialSortService;
import com.yootii.bdy.model.User;
import com.yootii.bdy.security.model.Token;
import com.yootii.bdy.security.service.AuthenticationService;
import com.yootii.bdy.util.GonUtil;
import com.yootii.bdy.util.JsonUtil;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mybatis.xml" })
public class TestMaterialService {
	
	@Resource
	private AuthenticationService authenticationService;
	
	@Resource
	private MaterialSortService materialSortService;
	
	@Resource
	private MaterialService materialService;
	
	private static final Logger logger = Logger.getLogger(TestMaterialService.class);
	

//	@Test
	public void queryMaterialSortTest() {
		
		GeneralCondition gcon=new GeneralCondition();
		MaterialSort materialSort=new MaterialSort();
//		Object info =materialSortService.queryMaterialSort(materialSort, gcon);
//		if (info != null) {
//			logger.info(JsonUtil.toJson(info));
//			logger.info("queryMaterialSort测试通过");
//		}
	}
	
//	@Test
	public void queryMaterialByAppTest() {
		
		GeneralCondition gcon=new GeneralCondition();
		GonUtil.makeOffsetAndRows(gcon);
		
		
		Material material=new Material();
	    Integer fileName=1;
	    material.setFileName(fileName);
		material.setCustId(3);
//		material.setApplicantId(16);
		Token token=null;
		Object info =materialService.queryMaterialByApp(material, gcon, token);
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("queryMaterialByAppTest测试通过");
		}
	}
	
	
	@Test
	public void createMaterialSimpleTest() {
		
		GeneralCondition gcon=getGcon();
	
		boolean precase=false;
		Material material=new Material();
		material.setMaterialId(1);
//		material.setCaseId(3597);
//		material.setApplicantId(16);
		
		Integer agencyId=1;
		Integer custId=5;
		material.setCustId(custId);
		
		
		String tokenID=gcon.getTokenID();
		Token token=new Token();
		token.setTokenID(tokenID);
		Object info;
		try {
//			info = materialService.createMaterialSimple(material, agencyId, gcon, precase, token);
//			if (info != null) {
//				logger.info(JsonUtil.toJson(info));
//				logger.info("queryMaterialSort测试通过");
//			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Test
	public void checkFileTest() {
		Material material=new Material();
		Integer caseId=4329;
		material.setCaseId(caseId);
		Integer fileName=24;
		Object info = materialService.checkFile(material, fileName);
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("checkFileTest测试通过");
		}
	}
	
	private  GeneralCondition getGcon(){
		GeneralCondition gcon=new GeneralCondition();
		gcon.setPageNo(1);
		gcon.setOffset(0);
		gcon.setRows(10);
		
		User user=new User();
		
		user.setUsername("whd_wangfang");
		user.setPassword("123456");		
		
	    String tokenID=login(user);	   
	    gcon.setTokenID(tokenID);			
	    return gcon;	
	}
	
	private String login(User user) {
		Object obj = authenticationService.login(user);				
		LoginReturnInfo rtnInfo = (LoginReturnInfo)obj;		
		String tokenID = rtnInfo.getTokenID();		
		return tokenID;
		
	}

}
