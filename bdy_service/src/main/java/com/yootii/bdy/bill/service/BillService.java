package com.yootii.bdy.bill.service;

import com.yootii.bdy.bill.model.Bill;
import com.yootii.bdy.bill.model.BillSolr;
import com.yootii.bdy.bill.model.PayAcount;
import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.security.model.Token;

public interface BillService {
	public ReturnInfo createBill(GeneralCondition gcon, Bill bill,String chargeRecordIds);
	public ReturnInfo deleteBill(Bill bill);
	public ReturnInfo modifyBill(GeneralCondition gcon, Bill bill);
	public boolean modifyBillStatus(Integer billId,String status);
	public ReturnInfo queryBillList(GeneralCondition gcon,Bill bill,Token token,Integer selectType);
	public ReturnInfo queryBillDetail(Bill bill);
	public ReturnInfo exportBill(Bill bill);
	public ReturnInfo caseGenerateBill(String caseIds);
	public ReturnInfo recordGenerateBill(String chargeRecordIds);
//	public ReturnInfo statsByCustomer(Integer custId, GeneralCondition gcon, String interfacetype);
//	public ReturnInfo statsByAgencyUser(Integer userId, Integer agencyId, GeneralCondition gcon, String interfacetype);
//	public ReturnInfo statsByAgency(Integer agencyId, GeneralCondition gcon, String interfacetype);
//	public ReturnInfo statsBillTop10(GeneralCondition gcon);
	public ReturnInfo generateBillNo(Integer billId);
	public ReturnInfo updateSolrBill();
	public ReturnInfo statsBillagency(Integer custId, String startYear, String endYear);
	public ReturnInfo statsBillmonth(Integer agencyId,Integer departId, Integer timetype, String startYear, String endYear);
	public ReturnInfo statsBillmonthcoagency(Integer agencyId,Integer departId, Integer timetype, String startYear, String endYear);
	public ReturnInfo statsBillmonthagency(Integer agencyId,Integer departId, Integer timetype, String startYear, String endYear);
	public ReturnInfo statsBillbillType(Integer custId, Integer agencyId,Integer departId, String startYears, String endYears,
			Integer timetype);
	public ReturnInfo statsBillmonthcust(Integer agencyId,Integer departId, Integer custId, Integer timetype, String startYear,
			String endYear);
	public ReturnInfo statsBillcreaterName(Integer custId,Integer agencyId,Integer departId, String startYears, String endYears, Integer timetype);
	public ReturnInfo statsBillcustomer(Integer agencyId,Integer departId, String startYears, String endYears, Integer timetype);
	public ReturnInfo statsBillProInfo(Integer custId, Integer agencyId, Integer departId);
	public ReturnInfo queryBillBySolr(Integer departId, Integer custId, Integer agencyId, Integer bodytype, BillSolr bill,
			Integer timetype, String startYears, String endYears, GeneralCondition gcon);
	
	public ReturnInfo queryUnPayedBill(GeneralCondition gcon, Bill bill,Token token);
}
