package com.yootii.bdy.customer.service;


import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.customer.model.Customer;
import com.yootii.bdy.customer.model.CustomerContact;

public interface CustomerService {
	
	
	public Customer getCustById(String custId, String tokenID) throws Exception;
	
	public CustomerContact getCustomerContact(String custId, String tokenID) throws Exception;
		
//	public ReturnInfo bindApplicant(Integer custId, Integer appId);
//
//	public ReturnInfo unBindApplicant(Integer custId, Integer appId);
	
}
