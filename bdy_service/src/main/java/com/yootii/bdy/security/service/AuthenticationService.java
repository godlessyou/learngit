package com.yootii.bdy.security.service;

import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.LoginReturnInfo;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.customer.model.Customer;
import com.yootii.bdy.model.User;

public interface AuthenticationService {

	public ReturnInfo authorize(GeneralCondition gcon);

	public LoginReturnInfo login(User user);
	
	public LoginReturnInfo customerin(Customer customer);

	public String getUsername(GeneralCondition gcon);

}