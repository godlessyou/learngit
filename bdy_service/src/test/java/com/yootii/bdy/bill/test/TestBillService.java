package com.yootii.bdy.bill.test;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yootii.bdy.bill.dao.BillMapper;
import com.yootii.bdy.bill.dao.BillSolrMapper;
import com.yootii.bdy.bill.model.Bill;
import com.yootii.bdy.bill.service.BillService;
import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.security.model.Token;
import com.yootii.bdy.util.JsonUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mybatis.xml" })
public class TestBillService {
	@Resource
	private BillService billService;
	@Resource 
	private BillSolrMapper billSolrMapper;
	private static final Logger logger = Logger.getLogger(TestBillService.class);
	//@Test
	public void deleteBillTest() {
		Bill bill = new Bill();
		bill.setBillType("商标注册");
		bill.setStatus("待审核");
		bill.setCustId(1);
		bill.setBillId(11);
		Object info =billService.deleteBill(bill);
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("queryUserList测试通过");
		}
	}
	//@Test
	public void modifyBillTest() {
		GeneralCondition gcon =new GeneralCondition();
		Bill bill = new Bill();
		bill.setBillId(1);
		bill.setBillType("商标注册");
		bill.setStatus("待审核");
		bill.setCustId(1);
		bill.setGroupName("商标组");
		bill.setDescription("商标注册账单");
		Object info =billService.modifyBill(gcon, bill);
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("queryUserList测试通过");
		}
	}
//	@Test
	public void modifyBillStatusTest() {
		Bill bill = new Bill();
//		bill.setBillId(24);
//		bill.setStatus("待审核");
		Object info =billService.modifyBillStatus(24, "未付款");
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("queryUserList测试通过");
		}
	}
//	@Test
	public void queryBillDetailTest() {
		Bill bill = new Bill();
		bill.setBillId(33);
		Object info =billService.queryBillDetail(bill);
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("queryBillDetail测试通过");
		}
	}
//	@Test
	public void generateBillByCasesTest() {
		Object info =billService.caseGenerateBill("1,3");
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("queryUserList测试通过");
		}
	}
//	@Test
	public void generateBillByChargeRecordsTest() {
		Object info =billService.recordGenerateBill("1,3");
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("queryUserList测试通过");
		}
	}
	
	
//	@Test
	public void createBillTest() {
		GeneralCondition gcon =new GeneralCondition();
		String chargeRecordIds = "3148,3149,3150,3151";
		Bill bill = new Bill();
		bill.setCustId(3);
		bill.setBillType("商标变更");
		bill.setContactId(1);
		bill.setCreater("agentA");
		bill.setDescription("记录商标变更账单");
		bill.setPayAcountId(1);
		bill.setAgencyId(1);
		bill.setReceiverType(1);
		String caseIds="16088,16089";
		bill.setCaseIds(caseIds);
		
		long sumValue=(long)8400;
		BigDecimal sum=BigDecimal.valueOf(sumValue);
		long foreignSumValue=(long)200;
		BigDecimal foreignSum=BigDecimal.valueOf(foreignSumValue);
		
		bill.setSum(sum);
		bill.setForeignSum(foreignSum);
		Object info =billService.createBill(gcon, bill, chargeRecordIds);
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("createBillTest测试通过");
		}
	}
//	@Test
	public void queryBillListTest() throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Bill bill = new Bill();
//		bill.setBillType("商标注册");
//		bill.setContactId(1);
//		bill.setCreater("zhangsan");
//		bill.setDescription("账单");
//		bill.setCustId(5);
//		bill.setPayAcountId(1);
//		bill.setCreateDateStart(sdf.parse("2018-4-10"));
//		bill.setCreateDateEnd(sdf.parse("2018-4-20"));
//		bill.setAgencyId(1);
		GeneralCondition gcon = new GeneralCondition();
		gcon.setOffset(0);
		gcon.setRows(10);
		Token token = new Token();
		token.setUser(true);
//		token.setCustomerID(5);
		token.setUserID(3);
		Object info =billService.queryBillList(gcon, bill,token,null);
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("queryUserList测试通过");
		}
	}
	
	
	@Test
	public void queryUnPayedBillTest() throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Bill bill = new Bill();
//		bill.setBillType("商标注册");
//		bill.setContactId(1);
//		bill.setCreater("zhangsan");
//		bill.setDescription("账单");
//		bill.setCustId(5);
//		bill.setPayAcountId(1);
//		bill.setCreateDateStart(sdf.parse("2018-4-10"));
//		bill.setCreateDateEnd(sdf.parse("2018-4-20"));
//		bill.setAgencyId(1);
		GeneralCondition gcon = new GeneralCondition();
		gcon.setOffset(0);
		gcon.setRows(10);
		Token token = new Token();
		token.setUser(true);
		token.setUserID(3);
//		token.setCustomerID(55);

		Object info =billService.queryUnPayedBill(gcon, bill,token);
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("queryUnPayedBillTest测试通过");
		}
	}
	
	
//	@Test
	public void generateBillNoTest() {
		Object info =billService.generateBillNo(1);
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("queryUserList测试通过");
		}
	}	
	
	
//	@Test
	public void updateSolrBillTest() {
		Object info =billService.updateSolrBill();
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("updateSolrBill测试通过");
		}
	}	
	
	
//	@Test
	public void statsBillProInfoTest() {
		Object info =billService.statsBillProInfo(null, 1, null);
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("statsBillProInfo测试通过");
		}
	}
	
//	@Test
	public void statsBillagencyTest() {
		Object info =billService.statsBillagency(1,  "2017", "2018");
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("queryUserList测试通过");
		}
	}
	
	@Test
	public void statsBillmonthTest() {
		Object info =billService.statsBillbillType(1,null,null ,"2017-1", "2018-3", 1);
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("queryUserList测试通过");
		}
	}
	
	@Test
	public void solrSendBillTest(){
		Object ret = billService.updateSolrBill();
		if (ret!=null) {
		  
		  logger.info(JsonUtil.toJson(ret));
		}
	}
	
//	@Test
	public void statsBillmonthcustTest() {
		Object info =billService.statsBillmonthcust(1,7,null, 2, "2017-01", "2018-04");
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("queryUserList测试通过");
		}
	}
	
	@Test
	public void selectAllBillTest(){
		List<Map<String, Object>> lists= billSolrMapper.selectAllBill(1);
		System.err.println(lists.size());
	}
	
	
	//@Test
	public void statsByCustomerTest() {
		GeneralCondition gcon =new GeneralCondition();
//		Object info =billService.statsByCustomer(1, gcon, "total");
//		Object info1 =billService.statsByCustomer(1, gcon, "pay");
//		Object info2 =billService.statsByCustomer(1, gcon, "nopay");
//		Object info3 =billService.statsByCustomer(1, gcon, "status");
//		Object info4 =billService.statsByCustomer(1, gcon, "type");
//		Object info5=billService.statsByCustomer(1, gcon, "agency");
//		if (info != null) {
//			logger.info(JsonUtil.toJson(info));
//			logger.info("statsByCustomerTest测试通过");
//		}
//		if (info1 != null) {
//			logger.info(JsonUtil.toJson(info1));
//			logger.info("statsByCustomerTest测试通过");
//		}
//		if (info2 != null) {
//			logger.info(JsonUtil.toJson(info2));
//			logger.info("statsByCustomerTest测试通过");
//		}
//		if (info3 != null) {
//			logger.info(JsonUtil.toJson(info3));
//			logger.info("statsByCustomerTest测试通过");
//		}
//		if (info4 != null) {
//			logger.info(JsonUtil.toJson(info4));
//			logger.info("statsByCustomerTest测试通过");
//		}
//		if (info5 != null) {
//			logger.info(JsonUtil.toJson(info5));
//			logger.info("statsByCustomerTest测试通过");
//		}
	}
//	@Test
	public void statsByAgencyUserTest() {
		GeneralCondition gcon =new GeneralCondition();
//		Object info =billService.statsByAgencyUser(29, 1, gcon, "total");
//		Object info1 =billService.statsByAgencyUser(29, 1, gcon, "customer");
//		Object info2 =billService.statsByAgencyUser(14, 1, gcon, "byyear");
//		Object info3 =billService.statsByAgencyUser(29, 1, gcon, "status");
//		Object info4 =billService.statsByAgencyUser(29, 1,gcon, "type");
//		//Object info5=billService.statsByAgencyUser(1, 1,gcon, "agency");
//		if (info != null) {
//			logger.info(JsonUtil.toJson(info));
//			logger.info("statsByCustomerTest测试通过");
//		}
//		if (info1 != null) {
//			logger.info(JsonUtil.toJson(info1));
//			logger.info("statsByCustomerTest测试通过");
//		}
//		if (info2 != null) {
//			logger.info(JsonUtil.toJson(info2));
//			logger.info("statsByCustomerTest测试通过");
//		}
//		if (info3 != null) {
//			logger.info(JsonUtil.toJson(info3));
//			logger.info("statsByCustomerTest测试通过");
//		}
//		if (info4 != null) {
//			logger.info(JsonUtil.toJson(info4));
//			logger.info("statsByCustomerTest测试通过");
//		}
		
	}
	
//	@Test
	public void statsByAgencyTest() {
		GeneralCondition gcon =new GeneralCondition();
		/*Object info =billService.statsByAgency(1, gcon, "total");
		Object info1 =billService.statsByAgency(1,  gcon, "pay");
		Object info2 =billService.statsByAgency(1,  gcon, "byyear");
		Object info3 =billService.statsByAgency(1,  gcon, "status");
		Object info4 =billService.statsByAgency(1, gcon, "type");
		Object info5 =billService.statsByAgency(1, gcon, "customer");*/
		//Object info5=billService.statsByAgencyUser(1, 1,gcon, "agency");
		/*if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("statsByCustomerTest测试通过");
		}
		if (info1 != null) {
			logger.info(JsonUtil.toJson(info1));
			logger.info("statsByCustomerTest测试通过");
		}
		if (info2 != null) {
			logger.info(JsonUtil.toJson(info2));
			logger.info("statsByCustomerTest测试通过");
		}
		if (info3 != null) {
			logger.info(JsonUtil.toJson(info3));
			logger.info("statsByCustomerTest测试通过");
		}
		if (info4 != null) {
			logger.info(JsonUtil.toJson(info4));
			logger.info("statsByCustomerTest测试通过");
		}
		if (info5 != null) {
			logger.info(JsonUtil.toJson(info5)); 
			logger.info("statsByCustomerTest测试通过");
		}*/
//		Object info6=billService.statsBillTop10(gcon);
//		if (info6 != null) {
//			logger.info(JsonUtil.toJson(info6));
//			logger.info("statsByCustomerTest测试通过");
//		}
	}
}
