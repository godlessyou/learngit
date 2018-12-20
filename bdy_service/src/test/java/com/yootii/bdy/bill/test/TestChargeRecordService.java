package com.yootii.bdy.bill.test;


import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.tools.ant.taskdefs.compilers.Gcj;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yootii.bdy.bill.model.ChargeItem;
import com.yootii.bdy.bill.model.ChargeRecord;
import com.yootii.bdy.bill.service.ChargeItemService;
import com.yootii.bdy.bill.service.ChargeRecordService;
import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.LoginReturnInfo;
import com.yootii.bdy.model.User;
import com.yootii.bdy.security.model.Token;
import com.yootii.bdy.security.service.AuthenticationService;
import com.yootii.bdy.task.service.Impl.CaseChargeRecordServiceImpl;

import com.yootii.bdy.tmcase.dao.TradeMarkCaseMapper;
import com.yootii.bdy.tmcase.model.TradeMarkCase;
import com.yootii.bdy.user.service.UserService;
import com.yootii.bdy.util.JsonUtil;
import com.yootii.bdy.util.GonUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mybatis.xml" })
public class TestChargeRecordService {
	@Resource
	private ChargeRecordService chargeRecordService;
	private static final Logger logger = Logger.getLogger(TestChargeRecordService.class);
	
	@Resource
	private CaseChargeRecordServiceImpl caseChargeRecordServiceImpl;
	
	@Resource
	private AuthenticationService authenticationService;
	@Resource
	private TradeMarkCaseMapper tradeMarkCaseMapper;
	
	@Resource
	private	ChargeItemService chargeItemService;
	
	
	
//	@Test
	public void newCreateChargeRecordTest() {
		
		User user=new User();
	
		user.setUsername("whd_wangfang");
		user.setPassword("123456");		
		
		String userId="3";
		String tokenID=login(user);
		String caseType="商标注册";
		
		GeneralCondition gcon = new GeneralCondition();		
		gcon.setTokenID(tokenID);
		
		Integer caseId=3244;
		
		TradeMarkCase tradeMarkCase=tradeMarkCaseMapper.selectByPrimaryKey(caseId);
		
		String agencyServiceId="2";
		
		String agencyId="1";
		
		try {
			caseChargeRecordServiceImpl.createChargeRecords(gcon, userId, caseId.toString(), agencyId, agencyServiceId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	
//	@Test
	public void newQuereyChargeRecordTest() {
		
		User user=new User();
	
		user.setUsername("yd_dingli");
		user.setPassword("123456");		
		
		String userId="16";
		String tokenID=login(user);
		String caseType="商标注册";
		
		GeneralCondition gcon = new GeneralCondition();		
		gcon.setTokenID(tokenID);
		
		Integer caseId=4;
		Integer agencyId=2;
		
		String agencyServiceId="1";
		
		List<ChargeItem>  list=chargeItemService.queryChargeItemListById(gcon, agencyServiceId);
			
		
		
		try {
			
			GonUtil.makeOffsetAndRows(gcon);
			
			for(ChargeItem item:list){	
				String chargeType=item.getChargeType();
				Integer chargeItemId=item.getChargeItemId();
				ChargeRecord chargeRecord = new ChargeRecord();
//				chargeRecord.setAgencyId(agencyId);
				chargeRecord.setCaseId(caseId);
				chargeRecord.setChargeItemId(chargeItemId);				
				chargeRecord.setChargeType(chargeType);
				
				if (chargeType.equals("官费") || chargeType.equals("服务费")){
					List<ChargeRecord> chargeRecordList=caseChargeRecordServiceImpl.queryChargeRecord(gcon, chargeRecord, userId);
					if (chargeRecordList!=null){
						for(ChargeRecord record:chargeRecordList){
							System.out.println(record);
						}
					}else{
						System.out.println("no charge record");
					}
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
//	@Test
	public void createChargeRecordTest() {
		ChargeRecord chargeRecord = new ChargeRecord();
		chargeRecord.setAgencyId(1);
		chargeRecord.setCaseId(1);
		chargeRecord.setChargeItemId(1);
		chargeRecord.setCreater("zhangsan");
		Token token = new Token();
		token.setUser(true);
		Object info =chargeRecordService.createChargeRecord(chargeRecord,token);
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("queryUserList测试通过");
		}
	}
//	@Test
	public void deleteChargeRecordTest() {
		ChargeRecord chargeRecord = new ChargeRecord();
		chargeRecord.setChargeRecordId(2);
		Object info =chargeRecordService.deleteChargeRecord(chargeRecord);
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("queryUserList测试通过");
		}
	}
//	@Test
	public void modifyChargeRecordTest() {
		ChargeRecord chargeRecord = new ChargeRecord();
		chargeRecord.setChargeRecordId(1);
		chargeRecord.setAgencyId(1);
		chargeRecord.setCaseId(1);
		chargeRecord.setChargeItemId(1);
		chargeRecord.setAmount(new BigDecimal(152.2));
		chargeRecord.setNumber(2);
		Object info =chargeRecordService.modifyChargeRecord(chargeRecord);
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("queryUserList测试通过");
		}
	}
//	@Test
	public void queryChargeRecordDetailTest() {
		ChargeRecord chargeRecord = new ChargeRecord();
		chargeRecord.setChargeRecordId(1);
		Object info =chargeRecordService.queryChargeRecordDetail(chargeRecord);
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("queryUserList测试通过");
		}
	}
	@Test
	public void queryChargeRecordListTest() {
		ChargeRecord chargeRecord = new ChargeRecord();
//		chargeRecord.setChargeRecordId(1);
//		chargeRecord.setCaseId(1);
//		chargeRecord.setChargeItemId(1);
//		chargeRecord.setStatus(1);
		chargeRecord.setChargeType("服务费");
		GeneralCondition gcon = new GeneralCondition();
		gcon.setKeyword("");
		gcon.setOffset(0);
		gcon.setRows(10);
		Token token = new Token();
//		token.setUser(true);
//		token.setUserID(3);
		token.setCustomerID(5);
//		Object info =chargeRecordService.queryChargeRecordList(gcon, chargeRecord,token);
//		if (info != null) {
//			logger.info(JsonUtil.toJson(info));
//			logger.info("queryUserList测试通过");
//		}
	}
	
	
//	@Test
	public void queryChargeRecordByCasesTest() {
		Object info =chargeRecordService.queryChargeRecordByCases("3204");
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("queryUserList测试通过");
		}
	}
	
	
	private String login(User user) {
		
		
		Object obj = authenticationService.login(user);				
		LoginReturnInfo rtnInfo = (LoginReturnInfo)obj;
		
		String tokenID = rtnInfo.getTokenID();
		
		return tokenID;
		
	}
}
