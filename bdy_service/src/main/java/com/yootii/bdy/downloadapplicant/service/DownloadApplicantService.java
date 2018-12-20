package com.yootii.bdy.downloadapplicant.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.material.model.Material;
import com.yootii.bdy.security.model.Token;

public interface DownloadApplicantService {
	public ReturnInfo downloadApplicant(HttpServletRequest request, HttpServletResponse response,
			Integer caseId,String fileName,Token token,GeneralCondition gCondition,Integer isDownload);

	public ReturnInfo downLoadRejectRechickDoc(HttpServletRequest request,HttpServletResponse response,
			Integer caseId,String fileName,Token token,GeneralCondition gCondition,Integer userId,Integer docTypeId,Integer flag);
	
	public ReturnInfo upload(HttpServletRequest request, String fileName,
			Integer custId,Integer applicantId,String creater,Integer caseId);
	
	
	public ReturnInfo createProxyPicture(HttpServletRequest request, HttpServletResponse response,
			Integer caseId,String fileName,Token token,GeneralCondition gCondition,String appCnName,String appEnName,
			String country,String agenName,String tmName,String goodClass);

	
}
