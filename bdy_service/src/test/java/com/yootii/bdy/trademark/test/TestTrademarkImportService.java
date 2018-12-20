package com.yootii.bdy.trademark.test;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.tmcase.model.TradeMarkCasePre;
import com.yootii.bdy.tmcase.service.TradeMarkCasePreService;
import com.yootii.bdy.trademark.dao.TrademarkCategoryMapper;
import com.yootii.bdy.trademark.dao.TrademarkMapper;
import com.yootii.bdy.trademark.dao.TrademarkProcessMapper;
import com.yootii.bdy.trademark.model.Trademark;
import com.yootii.bdy.trademark.model.TrademarkCategory;
import com.yootii.bdy.trademark.model.TrademarkProcess;
import com.yootii.bdy.trademark.service.TradeMarkService;
import com.yootii.bdy.util.JsonUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mybatis.xml" })
public class TestTrademarkImportService {
	@Resource
	private TradeMarkService tradeMarkService;
	@Resource
	private TrademarkMapper trademarkMapper;
	@Resource
	private TrademarkCategoryMapper trademarkCategoryMapper;
	@Resource
	private TrademarkProcessMapper trademarkProcessMapper;

	
	private static final Logger logger = Logger.getLogger(TestTrademarkImportService.class);

	@Test
	public void Test1() {
		System.out.println("test");
	}
	// TrademarkCategory  为tmId赋值
	//@Test
	/*public void Test2() {
		Trademark t =new Trademark();
		TrademarkCategory tc =new TrademarkCategory();
		GeneralCondition gcon = new GeneralCondition();
		//gcon.setOffset(186448);
		gcon.setOffset(0);
		gcon.setRows(2500000);
		List<Trademark>	listT= trademarkMapper.selectByTrademark(t,gcon);
		
		gcon.setOffset(0);
		gcon.setRows(2000000);
		List<TrademarkCategory>	listTc= trademarkCategoryMapper.selectByTrademarkCategory(tc,gcon);
		System.out.println(listTc.size());
		int a=0;
		for(int i =496000;i<listTc.size();i++) {
			for(int j=0; j<listT.size();j++) {
				if(listTc.get(i).getTmId() ==null ) {
					if(listT.get(j).getRegNumber().equals(listTc.get(i).getRegNumber())) {
						TrademarkCategory category = listTc.get(i);
						category.setTmId(listT.get(j).getTmId().toString());
						trademarkCategoryMapper.updateByPrimaryKeySelective(category);
						System.out.println("tmId :" + listT.get(j).getTmId() + "   "+a++);
					}
				}
				
			}
		}
		
		System.out.println("test");
	}
	// TrademarkProcess  为tmId赋值
		//@Test
		public void Test3() {
			Trademark t =new Trademark();
			
			TrademarkProcess tp = new TrademarkProcess();
			GeneralCondition gcon = new GeneralCondition();
			//gcon.setOffset(186448);
			gcon.setOffset(0);
			gcon.setRows(1000000);
			List<Trademark>	listT= trademarkMapper.selectByTrademark(t,gcon);
			//gcon.setOffset(171610);
			gcon.setOffset(0);
			gcon.setRows(2000000);
			List<TrademarkProcess>	listTp= trademarkProcessMapper.selectByTrademarkProcess(tp,gcon);
			for(int i =0;i<listTp.size();i++) {
				for(int j=0; j<listT.size();j++) {
					if(listT.get(j).getRegNumber().equals(listTp.get(i).getRegNumber())) {
						TrademarkProcess process = listTp.get(i);
						process.setTmId(listT.get(j).getTmId());
						trademarkProcessMapper.updateByPrimaryKeySelective(process);
					}
				}
			}
			
			System.out.println("test");
		}
		//@Test
		public void Test4() {
			// 导入访问本地商标图片的url
			Trademark t =new Trademark();
			GeneralCondition gcon = new GeneralCondition();
			//gcon.setOffset(186448);
			gcon.setOffset(0);
			gcon.setRows(1000000);
			
			List<Map<String,String>> map= trademarkMapper.selectCustIdByApplicantName();
			int a = 0; 
			for(Map m : map) {
				
				t.setTmId((Integer) m.get("tmId"));
				t.setImgFilePath("/"+m.get("custId")+"/"+m.get("regNumber")+".jpg");
				trademarkMapper.updateByPrimaryKeySelective(t);
				System.out.println(a++);
			}
			
		}*/
}
