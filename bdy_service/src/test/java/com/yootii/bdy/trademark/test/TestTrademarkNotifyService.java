package com.yootii.bdy.trademark.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.solr.SolrInfo;
import com.yootii.bdy.trademark.dao.TrademarkNotifyMapper;
import com.yootii.bdy.trademark.service.TradeMarkNotifyService;
import com.yootii.bdy.util.JsonUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mybatis.xml" })
public class TestTrademarkNotifyService {

	private static final Logger logger = Logger.getLogger(TestTrademarkNotifyService.class);
	
	@Autowired
	private TradeMarkNotifyService tradeMarkNotifyService;
	@Autowired
	private TrademarkNotifyMapper trademarkNotifyMapper;
	
//	@Test
	public void test1(){
		GeneralCondition gcon = new GeneralCondition();
		Object info ;
		try{
			info = tradeMarkNotifyService.statisticTmNotifyStatus(gcon,"2018",3);
			if(info!=null){
				logger.info(JsonUtil.toJson(info));
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void statisticTmNotifyDataTest(){
		
		GeneralCondition gCondition = new GeneralCondition();
		gCondition.setOffset(0);
		gCondition.setRows(10);
		String year = "2018";
		int custId = 3;
		int quarter = 3;
		int type = 2;
		Object info;
		try{
			info = tradeMarkNotifyService.statisticTmNotifyData(gCondition, year, custId, quarter, type);
			if(info!= null){
				logger.info(JsonUtil.toJson(info));
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	//@Test
	public void test2(){
		Integer custId=5;
		String createTime="2018-09-12";
		createTime+=" 00:00:00";
		
		GeneralCondition gcon = new GeneralCondition();
//		gcon.setPageNo(1);
//		gcon.setPageSize(10);
		gcon.setKeyword("L");
		gcon.setOffset(0);
		gcon.setRows(10);
		try{
		
		  Object info=	tradeMarkNotifyService.queryTmNotifyByCustId(custId, createTime, gcon);
		  
		  if (info != null) {
				logger.info(JsonUtil.toJson(info));
				logger.info("queryTmListTest测试通过");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//@Test
	public void testQueryTmNotifyList(){
		GeneralCondition gcon = new GeneralCondition();
		
		tradeMarkNotifyService.queryTmNotifyList(5, "1536320942797", gcon, 1);	
	}
	
	@Test
	public void testQueryTmAbnormalList(){
		
		GeneralCondition gCondition = new GeneralCondition();
		Integer custId = 3;
		Integer userId = null;
		gCondition.setOffset(0);
		gCondition.setRows(10);
		Object info;
		try{
			info = tradeMarkNotifyService.queryTmAbnormalList(custId, gCondition,userId);
			if(info !=null){
				logger.info(JsonUtil.toJson(info));
				logger.info("查询完毕");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	//@Test
	public void testqueryAbnormalTimes(){
		
		Integer custId = 3;
		Object info;
		try{
			info = tradeMarkNotifyService.queryAbnormalTimes(custId);
			if(info !=null){
				logger.info(JsonUtil.toJson(info));
				logger.info("查询完毕");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testQueryAnnouncementRemind(){
		GeneralCondition generalCondition = new GeneralCondition();
		Integer custId = 3;
		Integer userId =null;
		Integer appId = null;
		Integer type = 1;
		generalCondition.setKeyword("sdfsd");
		generalCondition.setGgQihao(44444);
		generalCondition.setOffset(0);
		generalCondition.setRows(10);
		Object info;
		try{
			info = tradeMarkNotifyService.queryAnnouncementRemind(generalCondition, custId, appId,userId,type);
			if(info != null){
				logger.info(JsonUtil.toJson(info));
				logger.info("测试完毕");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testQueryAllGGQiao(){
		Integer custId = 5;
		Object object;
		try{
			object = tradeMarkNotifyService.queryAllGGQiao(custId);
			if(object!=null){
				logger.info(JsonUtil.toJson(object));
				logger.info("测试完毕");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//
//	@Test
	public void testUpdateAnnouncementRemind(){
		List<String> ids = new ArrayList<>();
		ids.add("1405100");
		ids.add("1405101");
		try{
			tradeMarkNotifyService.updateAnnouncementRemind(ids);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	//
	//@Test
	public void testList(){
		String idString="1";
	    String[] arr =idString.split(",");
	    System.err.println(arr.length);
		logger.info(arr.length);
	}
	
	@Test
	public void queryAnnoucementForMailTest(){
		GeneralCondition gCondition = new GeneralCondition();
		Object info;
		Integer custId = 3;
		Integer type = 1;
		try{
			info = tradeMarkNotifyService.queryAnnoucementForMail(custId,type);
			if(info!=null){
				logger.info(JsonUtil.toJson(info));
				logger.info("查询完毕");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testqueryRenewalRemindTimes(){
		
		Integer custId = 2;
		GeneralCondition gcon = new GeneralCondition();
		gcon.setType(3);
		try{
			tradeMarkNotifyService.queryRenewalRemindTimes(gcon, custId);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	//@Test
	public void testModifyTmAbnormal(){
		
		List<String> list = new ArrayList<>();
		list.add("53191");
		list.add("53192");
		try{
			tradeMarkNotifyService.modifyTmAbnormal(list);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	//@Test
	public void updateLocalSolrData(){
		//
		SolrInfo solrInfo = new SolrInfo();
		solrInfo.setSolrHome("http://localhost:8983/solr/");
		/*String beginQuarter = "";
		String endQuarter = "";
		Integer custId = 4;
		GeneralCondition gcon = new GeneralCondition();*/
		HttpSolrClient client = new HttpSolrClient(solrInfo.getSolrHome()+"bdyNotification");
		try{
			client.deleteByQuery("*:*");
			UpdateResponse response = client.commit();
			logger.info("本地删除成功");
			//
		//	trademarkNotifyMapper.statisticsTmByQuarter(beginQuarter, endQuarter, custId, 0, gcon);
			//插入测试数据
			Collection<SolrInputDocument> documents = new ArrayList<>();
			SolrInputDocument document = new SolrInputDocument();
			document.addField("id", 1);
			document.addField("createTime","2017-03-12");
			documents.add(document);
			UpdateResponse response2 = client.add(documents);
			client.commit();
			logger.info("更新成功");
		}catch (Exception e) {
			e.printStackTrace();
		}
		//
		
		
	}
	
	
	
	
	
	
}
