package com.yootii.bdy.user.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.customer.model.Customer;
import com.yootii.bdy.mail.MailSenderInfo;
import com.yootii.bdy.mail.SendMailUtil;
import com.yootii.bdy.security.service.AuthenticationService;
import com.yootii.bdy.model.User;
import com.yootii.bdy.user.service.UserService;
import com.yootii.bdy.util.GraspUtil;
import com.yootii.bdy.util.JsonUtil;
import com.yootii.bdy.util.ServiceUrlConfig;




@Service("userService")
public class UserServiceImpl implements UserService {
	
	private static final Logger logger = Logger.getLogger(UserServiceImpl.class);
	
	@Autowired	
	private ServiceUrlConfig serviceUrlConfig;
	
	public boolean checkUserPermission(String permission, String userId, String tokenID) {
		boolean result=false;
		ReturnInfo rtnInfo = new ReturnInfo();
		try {
			
		
			String url=serviceUrlConfig.getBdysysmUrl()+"/user/haspermission?userId="+ userId +"&permission="+permission+"&tokenID="+ tokenID;
			logger.info(url);
			String jsonString = GraspUtil.getText(url);
			rtnInfo=JsonUtil.toObject(jsonString, ReturnInfo.class);
			if (rtnInfo!=null && rtnInfo.getSuccess()){
				Map<String, String> data=(Map<String, String>)rtnInfo.getData();
				String hasPermission=data.get("hasPermission");
				if (hasPermission!=null && hasPermission.equals("true")){
					result=true;
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage(e.getMessage());
			rtnInfo.setMessageType(-1);
		} 		
		
		return result;
	}
	
	
	public List<String> findUsersByPermission(String permission, String agencyId, String tokenID) {
		
		List<String> userIds=new ArrayList<String>();		
		ReturnInfo rtnInfo = new ReturnInfo();
		try {			
					
			String url=serviceUrlConfig.getBdysysmUrl()+"/user/queryuserlistbypermission?agencyId="+ agencyId +"&permission="+permission+"&tokenID="+ tokenID;
//			System.out.println(url);
			String jsonString = GraspUtil.getText(url);
			rtnInfo=JsonUtil.toObject(jsonString, ReturnInfo.class);
			if (rtnInfo!=null && rtnInfo.getSuccess()){
				List<Map<String, Object>> userList=(List<Map<String, Object>> )rtnInfo.getData();
			
				if (userList!=null){					
					for(Map<String, Object> user2: userList){
						Integer userId=(Integer)user2.get("userId");		
						System.out.println(userId);
						userIds.add(userId.toString());
					}
				}
				
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage(e.getMessage());
			rtnInfo.setMessageType(-1);
		} 		
		
		return userIds;
	}
	
	

	/**
	 * @param userId
	 * @param tokenID
	 * @return
	 */
	public User getUserById(String userId, String tokenID) throws Exception{
		ReturnInfo rtnInfo = new ReturnInfo();
		User user=new User();
				
		String url=serviceUrlConfig.getBdysysmUrl()+"/user/queryUserByUserId?userId="+ userId +"&tokenID="+ tokenID;
		
		String jsonString = GraspUtil.getText(url);
		
		rtnInfo=JsonUtil.toObject(jsonString, ReturnInfo.class);
		
		if (rtnInfo!=null && rtnInfo.getSuccess()){
			Map<String, String> data=(Map<String, String>)rtnInfo.getData();		
			if(data!=null){
				String username=data.get("username");
				String fullname=data.get("fullname");
				String email=data.get("email");	
				String emailPass=data.get("emailPass");	
				user.setUsername(username);
				user.setFullname(fullname);
				user.setEmail(email);
				user.setEmailPass(emailPass);
			}
		}
	
		
		return user;
	}
	
	
	/**
	 * @param username
	 * @param tokenID
	 * @return
	 */
	public User getUserByUserName(String username, String tokenID) throws Exception{
		ReturnInfo rtnInfo = new ReturnInfo();
		User user=new User();
				
		String url=serviceUrlConfig.getBdysysmUrl()+"/user/queryUserByUserName?username="+ username +"&tokenID="+ tokenID;
		
		String jsonString = GraspUtil.getText(url);
		
		rtnInfo=JsonUtil.toObject(jsonString, ReturnInfo.class);
		
		if (rtnInfo!=null && rtnInfo.getSuccess()){
			Map<String, String> data=(Map<String, String>)rtnInfo.getData();		
			if(data!=null){
				
				String fullname=data.get("fullname");
				String email=data.get("email");	
				String emailPass=data.get("emailPass");	
				user.setUsername(username);
				user.setFullname(fullname);
				user.setEmail(email);
				user.setEmailPass(emailPass);
			}
		}
	
		
		return user;
	}
	
	
	/**
	 * @param agencyId
	 * @param tokenID
	 * @return
	 */
	public User queryAdminByAgencyId(String agencyId, String tokenID) throws Exception{
		ReturnInfo rtnInfo = new ReturnInfo();
		User user=new User();
				
		String url=serviceUrlConfig.getBdysysmUrl()+"/user/queryAdminByAgencyId?agencyId="+ agencyId +"&tokenID="+ tokenID;
		
		String jsonString = GraspUtil.getText(url);
		
		rtnInfo=JsonUtil.toObject(jsonString, ReturnInfo.class);
		
		if (rtnInfo!=null && rtnInfo.getSuccess()){
			Map<String, String> data=(Map<String, String>)rtnInfo.getData();		
			if(data!=null){
				String username=data.get("username");
				String fullname=data.get("fullname");
				String email=data.get("email");	
				
				user.setUsername(username);
				user.setFullname(fullname);
				user.setEmail(email);
			
			}
		}
	
		
		return user;
	}
	
	
	
}
