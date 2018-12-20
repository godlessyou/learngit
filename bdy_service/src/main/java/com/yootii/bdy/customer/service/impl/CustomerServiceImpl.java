package com.yootii.bdy.customer.service.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.ReturnInfo;
//import com.yootii.bdy.customer.dao.CustomerApplicantMapper;
import com.yootii.bdy.customer.model.Customer;
//import com.yootii.bdy.customer.model.CustomerApplicant;
import com.yootii.bdy.customer.model.CustomerContact;
import com.yootii.bdy.customer.service.CustomerService;
import com.yootii.bdy.security.service.AuthenticationService;
import com.yootii.bdy.util.GraspUtil;
import com.yootii.bdy.util.JsonUtil;
import com.yootii.bdy.util.ServiceUrlConfig;


@Service
public class CustomerServiceImpl implements CustomerService{
	
	@Autowired	
	private ServiceUrlConfig serviceUrlConfig;
	
//	@Resource
//	public CustomerApplicantMapper customerApplicantMapper;
	
	/**
	 * @param custId
	 * @param tokenID
	 * @return
	 */
	public Customer getCustById(String custId, String tokenID) throws Exception{
		ReturnInfo rtnInfo = new ReturnInfo();
		Customer customer=new Customer();
				
		String url=serviceUrlConfig.getBdysysmUrl()+"/customer/customerdetail?custId="+ custId +"&tokenID="+ tokenID;
//			System.out.println(url);
		String jsonString = GraspUtil.getText(url);
		rtnInfo=JsonUtil.toObject(jsonString, ReturnInfo.class);
		
		if (rtnInfo!=null && rtnInfo.getSuccess()){
			Map<String, String> data=(Map<String, String>)rtnInfo.getData();
			if (data!=null){
				String email=data.get("email");	
				String username=data.get("username");
				String fullname=data.get("fullname");
				String emailPass=data.get("emailPass");	
				customer.setUsername(username);
				customer.setFullname(fullname);
				customer.setEmail(email);
				customer.setEmailPass(emailPass);
			}
		}
	
		
		return customer;
	}
	
	
	/**
	 * @param custId
	 * @param tokenID
	 * @return
	 */
	public CustomerContact getCustomerContact(String custId, String tokenID) throws Exception{
		ReturnInfo rtnInfo = new ReturnInfo();
		CustomerContact customer=new CustomerContact();
				
		String url=serviceUrlConfig.getBdysysmUrl()+"/customer/querycustomercontact?custId="+ custId +"&tokenID="+ tokenID;
//			System.out.println(url);
		String jsonString = GraspUtil.getText(url);
		rtnInfo=JsonUtil.toObject(jsonString, ReturnInfo.class);
		
		if (rtnInfo!=null && rtnInfo.getSuccess()){
			List<Map<String, String>> data=(List<Map<String, String>>)rtnInfo.getData();
			if (data!=null && data.size()>0){
				Map<String, String> map=data.get(0);
				String email=map.get("email");	
				String name=map.get("name");
				
				customer.setName(name);			
				customer.setEmail(email);
				
			}
		}
	
		
		return customer;
	}
	
	
	/*
	@Override
	public ReturnInfo bindApplicant(Integer custId, Integer appId) {
		ReturnInfo info = new ReturnInfo();
		if(custId==null||appId==null){
			info.setMessageType(-4);
			info.setMessage("参数为空");
			info.setSuccess(false);
			return info;
		}
		CustomerApplicant c =new CustomerApplicant();
		c.setAppId(appId);
		c.setCustId(custId);
		customerApplicantMapper.unbindApplicant(c);
		customerApplicantMapper.bindApplicant(c);
		info.setMessage("绑定成功");
		info.setSuccess(true);
		return info;
	}

	@Override
	public ReturnInfo unBindApplicant(Integer custId, Integer appId) {
		ReturnInfo info = new ReturnInfo();
		if(custId==null||appId==null){
			info.setMessageType(-4);
			info.setMessage("参数为空");
			info.setSuccess(false);
			return info;
		}
		CustomerApplicant c =new CustomerApplicant();
		c.setAppId(appId);
		c.setCustId(custId);
		customerApplicantMapper.unbindApplicant(c);
		info.setMessage("解绑成功");
		info.setSuccess(true);
		return info;
	}
	*/
	
}
