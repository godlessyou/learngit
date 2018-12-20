package com.yootii.bdy.translation.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.yootii.bdy.agency.model.Agency;
import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.customer.model.Customer;

public interface TranslationService {  
	//
	//
	public String translation(String value,String bflanguage,String aflanguage);

	public Object translationObject(Object oj,String bflanguage,String aflanguage);

	public Map<String, Object> translationMap(Map<String, Object> value,String bflanguage,String aflanguage);


	
}