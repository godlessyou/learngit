package com.yootii.bdy.user.test;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;





import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.LoginReturnInfo;
import com.yootii.bdy.model.User;
import com.yootii.bdy.security.service.AuthenticationService;
import com.yootii.bdy.tmcase.model.TradeMarkCasePre;
import com.yootii.bdy.tmcase.service.TradeMarkCasePreService;
import com.yootii.bdy.trademark.model.Trademark;
import com.yootii.bdy.trademark.service.TradeMarkService;
import com.yootii.bdy.user.service.UserService;
import com.yootii.bdy.util.JsonUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mybatis.xml" })
public class TestUserService {
	
	@Resource
	private AuthenticationService authenticationService;
	@Resource
	private UserService userService;
	
	private static final Logger logger = Logger.getLogger(TestUserService.class);
	
	
//	@Test
	public void getUserByIdTest() {
		
		User user=new User();
		user.setUserId(1);
		user.setUsername("yd_lina");
		user.setPassword("123456");		
		
		String userId=1+"";
		String tokenID=login(user);
		

		User result;
		try {
			result = userService.getUserById(userId, tokenID);
			logger.info("result="+result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		

	}
	

//	@Test
	public void testCheckUserPermission() {
		
		User user=new User();
		user.setUserId(1);
		user.setUsername("test");
		user.setPassword("123456");		
		
		String userId=1+"";
		String tokenID=login(user);
		
		String permission="代理机构开通";
		boolean result=userService.checkUserPermission(permission, userId, tokenID);
	
		logger.info("result="+result);

	}
	
	
	
	@Test
	public void testFindUsersByPermission() {
		
		User user=new User();
		user.setUserId(1);
		user.setUsername("whd_wangfang");
		user.setPassword("123456");	
//		
//		String tokenID=login(user);
		String agencyId="1";
		String custId="166";
		
		String permission="案件分配";
		List<String> userList=userService.findUsersByPermission(permission, agencyId, custId);
	
		logger.info("userList: "+userList);

	}
	
	
//	@Test
	public void testAuthenticate() {
		
		User user=new User();
		user.setUserId(1);
		user.setUsername("whd_wangfang");
		user.setPassword("123456");	
//		
		String tokenID=login(user);
		GeneralCondition gcon=new GeneralCondition();
		gcon.setTokenID(tokenID);
		
		Object info = authenticationService.authorize(gcon);
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("testAuthenticate测试通过");
		}

	}
	
	
	private String login(User user) {
		
		
		Object obj = authenticationService.login(user);				
		LoginReturnInfo rtnInfo = (LoginReturnInfo)obj;
		
		String tokenID = rtnInfo.getTokenID();
		
		return tokenID;
		
	}
}
