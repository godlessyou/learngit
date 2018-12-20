package com.yootii.bdy.mail.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.yootii.bdy.common.ReturnInfo;


public interface MailService {
	public ReturnInfo sendMail(String mailType,Map<String,Object> mailMap);
}
