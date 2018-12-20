package com.yootii.bdy.ipinfo.test;



import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.LoginReturnInfo;
import com.yootii.bdy.ipinfo.service.ForbidContentService;
import com.yootii.bdy.model.User;
import com.yootii.bdy.security.service.AuthenticationService;
import com.yootii.bdy.util.GonUtil;
import com.yootii.bdy.util.JsonUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mybatis.xml" })
public class TestIpInfoService {
	@Resource
	private ForbidContentService forbidContentService;
	
	@Resource
	private AuthenticationService authenticationService;
	
	private static final Logger logger = Logger.getLogger(TestIpInfoService.class);

//	@Test
	public void checkForbidContentTest() {
		
		GeneralCondition gcon = new GeneralCondition();
		
		String content="小乘胜";
		
		
		Object info;
		try {
			
			info = forbidContentService.checkForbidContent(content);
			if (info != null) {
				logger.info(JsonUtil.toJson(info));
				logger.info("queryUserList测试通过");
			}
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void checkSameTmTest() {
		
		GeneralCondition gcon=getGcon("whd_wangfang");
		
		GonUtil.makeOffsetAndRows(gcon);

		
		String tmName="无限极萃雅";
		String tmType="29";
		String localSearch="2";
		
		
		Object info;
		try {
			
//			info = forbidContentService.checkSameTm(tmName, tmType, localSearch, gcon);
//			if (info != null) {
//				logger.info(JsonUtil.toJson(info));
//				logger.info("checkSameTmTest测试通过");
//			}
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

	

	private  GeneralCondition getGcon(String username){
		GeneralCondition gcon=new GeneralCondition();
		gcon.setPageNo(1);
		gcon.setOffset(0);
		gcon.setRows(10);
		
		User user=new User();
		
		user.setUsername(username);
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
