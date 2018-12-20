package com.yootii.bdy.customer.service;


import java.util.List;

import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.customer.model.Applicant;


public interface ApplicantService {
	
	Applicant queryApplicantByAppName(String appCnName, String tokenID);
	
	Applicant getApplicantByName(Applicant applicant, String tokenID);
	
	List<Applicant> queryApplicantByRegNumbers(List<String>regNumbers);
	
	ReturnInfo checkAndSaveApplicant(GeneralCondition gcon, Applicant  applicant,Integer customerId);	
	
	ReturnInfo queryApplicant(Applicant applicant, GeneralCondition gcon, Integer customerId);

	ReturnInfo queryApplicantDetail(Applicant applicant, GeneralCondition gcon);	

	ReturnInfo queryApplicantByCustId(GeneralCondition gcon, Integer customerId);
	
	ReturnInfo queryApplicantNameByCustId(GeneralCondition gcon, Integer customerId);

	ReturnInfo queryAndCreateAapplicant(GeneralCondition gcon, Applicant applicant, Integer custId);

	ReturnInfo queryApplicantbyappcnname(String applicantCnName, GeneralCondition gcon);

	ReturnInfo queryApplicantbyappname(String appCnName, String appEnName, GeneralCondition gcon);

	ReturnInfo queryAllApplicantByCustId(GeneralCondition gcon, Integer customerId);

	ReturnInfo queryAllApplicantByAppId(GeneralCondition gcon, Integer AppId);

	
}
