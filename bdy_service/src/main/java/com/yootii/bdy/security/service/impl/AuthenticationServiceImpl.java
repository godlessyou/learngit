package com.yootii.bdy.security.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.Globals;
import com.yootii.bdy.common.LoginReturnInfo;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.customer.model.Customer;
import com.yootii.bdy.model.User;
import com.yootii.bdy.security.model.Token;
import com.yootii.bdy.security.service.AuthenticationService;
import com.yootii.bdy.util.GraspUtil;
import com.yootii.bdy.util.JsonUtil;
import com.yootii.bdy.util.ServiceUrlConfig;

@Service("authenticationService")
public class AuthenticationServiceImpl implements AuthenticationService {
	
	private final Logger logger = Logger.getLogger(this.getClass());

	@Resource
	private ServiceUrlConfig serviceUrlConfig;

	// 登录认证
	public LoginReturnInfo login(User user) {
		// 返回结果对象
		LoginReturnInfo rtnInfo = new LoginReturnInfo();
	
		String username=null;
		String password=null;
		if (user != null) {
			username = user.getUsername();
			password = user.getPassword();
		} 
			
		String url=serviceUrlConfig.getBdysysmUrl()+"/auth/login?"+"username="+username+"&password="+ password;
		String jsonString = null;
		try {
			jsonString = GraspUtil.getText(url);
			rtnInfo=JsonUtil.toObject(jsonString, LoginReturnInfo.class);
			
		} catch (Exception e) {
			logger.info("login return object: "+ jsonString);
			e.printStackTrace();
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage(e.getMessage());
			rtnInfo.setMessageType(Globals.MESSAGE_TYPE_OPERATION_EXCEPTION);
		} 
		return rtnInfo;
	}
	
	
	// 登录认证
	public LoginReturnInfo customerin(Customer customer) {
		// 返回结果对象
		LoginReturnInfo rtnInfo = new LoginReturnInfo();
	
		String username=null;
		String password=null;
		if (customer != null) {
			username = customer.getUsername();
			password = customer.getPassword();
		} 
			
		String url=serviceUrlConfig.getBdysysmUrl()+"/auth/customerin?"+"username="+username+"&password="+ password;
		String jsonString = null;
		try {
			jsonString = GraspUtil.getText(url);
			rtnInfo=JsonUtil.toObject(jsonString, LoginReturnInfo.class);
			
		} catch (Exception e) {
			logger.info("login return object: "+ jsonString);
			e.printStackTrace();
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage(e.getMessage());
			rtnInfo.setMessageType(Globals.MESSAGE_TYPE_OPERATION_EXCEPTION);
		} 
		return rtnInfo;
	}
	
	

	public ReturnInfo authorize(GeneralCondition gcon) {
		// 返回结果对象
		ReturnInfo rtnInfo = new ReturnInfo();
		String tokenID=null;
		if (gcon != null) {
			tokenID = gcon.getTokenID();		
		}
		try {			
			String url=serviceUrlConfig.getBdysysmUrl()+"/auth/authenticate?tokenID="+ tokenID;
			String jsonString;
			jsonString = GraspUtil.getText(url);
//			rtnInfo=JsonUtil.toObject(jsonString, ReturnInfo.class);
			JSONObject objData = new JSONObject().fromObject(jsonString);
			Map<String, Class> classMap = new HashMap<String, Class>();  
			//注意key 应与bean中的元素名一致
			classMap.put("data", Token.class);  
			rtnInfo=(ReturnInfo)JSONObject.toBean(objData,ReturnInfo.class,classMap);
			if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证
				Token token =(Token)rtnInfo.getData();		
				Globals.setToken(token);
				String language=token.getLanguage();	
				if (language!=null && !language.equals("")){
					//设置用户的语言环境
					Globals.setLanguage(language);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("验证用户身份失败：" + e.getMessage());
			rtnInfo.setMessageType(Globals.MESSAGE_TYPE_GETDATA_FAILED);
			return rtnInfo;
		}
	
		return rtnInfo;
	}

	// 返回用户名
	public String getUsername(GeneralCondition gcon) {

		String username = null;
		ReturnInfo rtnInfo = new ReturnInfo();
		
		String tokenID=null;
		if (gcon != null) {
			tokenID = gcon.getTokenID();		
		}
		
		String url=serviceUrlConfig.getBdysysmUrl()+"/auth/queryusername?tokenID="+ tokenID;
		
		String jsonString;

		try {
			jsonString = GraspUtil.getText(url);
			rtnInfo=JsonUtil.toObject(jsonString, ReturnInfo.class);		
			Object json=rtnInfo.getData();		
			Map<?, ?> map=(Map<?, ?>)json;
			if (map!=null){
				username=(String)map.get("username");
			}
	
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} 		
		
		return username;
	}
	
	

	public static void main(String[] args) {

	}



}
