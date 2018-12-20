package com.yootii.bdy.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yootii.bdy.bill.model.Bill;
import com.yootii.bdy.bill.model.ChargeRecord;
import com.yootii.bdy.task.model.TmCaseTaskToDoList;
import com.yootii.bdy.tmcase.model.TmCaseAppOnline;
import com.yootii.bdy.tmcase.model.TradeMarkCase;
import com.yootii.bdy.trademark.model.TrademarkProcess;


public class TranslationNameList {
	 public static final Map<Class, List<String> > nameList = new HashMap<Class, List<String> >();  
	 
	 static {  
		 
		 List<String> TmCaseTaskToDoListlist = new ArrayList<String>();		 
		 TmCaseTaskToDoListlist.add("caseStatus");		  
		 TmCaseTaskToDoListlist.add("caseType");		   
		 nameList.put(TmCaseTaskToDoList.class, TmCaseTaskToDoListlist);
		 
		 List<String> ChargeRecordlist = new ArrayList<String>();		 
		 ChargeRecordlist.add("status");		  
		 ChargeRecordlist.add("ecurrency");		   
		 nameList.put(ChargeRecord.class, ChargeRecordlist);
		 
		 List<String> TradeMarkCaselist = new ArrayList<String>();		 
		 TradeMarkCaselist.add("caseType");		  
		 TradeMarkCaselist.add("status");	  
		 TradeMarkCaselist.add("caseStatus");	
		 TradeMarkCaselist.add("submitType");	
		 TradeMarkCaselist.add("chargeRecords");	
		 nameList.put(TradeMarkCase.class, TradeMarkCaselist);
	
		 List<String> Billlist = new ArrayList<String>();		 
		 Billlist.add("billType");		  
		 Billlist.add("status");		  
		 Billlist.add("payAcount");		  
		 Billlist.add("chargeRecords");	
		 nameList.put(Bill.class, Billlist);
		 
		 List<String> TmCaseAppOnlinelist = new ArrayList<String>();		 
		 TmCaseAppOnlinelist.add("caseType");		  
		 TmCaseAppOnlinelist.add("caseStatus");	
		 nameList.put(TmCaseAppOnline.class, TmCaseAppOnlinelist);
		 
		 List<String> TrademarkProcesslist = new ArrayList<String>();		 	  
		 TrademarkProcesslist.add("status");	
		 nameList.put(TrademarkProcess.class, TrademarkProcesslist);
		 
		 List<String> ReturnInfolist = new ArrayList<String>();		 
		 ReturnInfolist.add("data");
		 ReturnInfolist.add("message");
		 nameList.put(ReturnInfo.class,ReturnInfolist);
		 
		 
		 
	 }
}
