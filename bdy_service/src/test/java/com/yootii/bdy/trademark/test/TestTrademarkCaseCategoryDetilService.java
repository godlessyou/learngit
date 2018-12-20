package com.yootii.bdy.trademark.test;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yootii.bdy.tmcase.dao.TradeMarkCaseCategoryDetilMapper;
import com.yootii.bdy.tmcase.dao.TradeMarkCaseCategoryMapper;
import com.yootii.bdy.tmcase.model.TradeMarkCaseCategory;
import com.yootii.bdy.tmcase.model.TradeMarkCaseCategoryDetil;
import com.yootii.bdy.tmcase.model.TradeMarkCasePre;
import com.yootii.bdy.tmcase.service.TradeMarkCasePreService;
import com.yootii.bdy.trademark.service.TradeMarkService;
import com.yootii.bdy.util.ExcelUtil;
import com.yootii.bdy.util.JsonUtil;
import com.yootii.bdy.util.TrademarkCategoryUtil;
import com.yootii.bdy.util.TrademarkCategoryUtil.ReadTxtFile;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mybatis.xml" })
public class TestTrademarkCaseCategoryDetilService {
	@Resource
	private TradeMarkService tradeMarkService;
	private static final Logger logger = Logger.getLogger(TestTrademarkCaseCategoryDetilService.class);
	@Resource
	private TradeMarkCaseCategoryDetilMapper tradeMarkCaseCategoryDetilMapper;
	@Resource
	private TradeMarkCaseCategoryMapper tradeMarkCaseCategoryMapper;
	
//	@Test
	public void TestUpdate() {
//		tradeMarkService.updateGoods("/goods.txt", "/gf.xls");
	}
	
	
	
	
	/**
	 *  合并两个文件中的内容,先运行test2,在运行test1 
	 */
	//@Test
	public void Test1() {
		List readTxt = ReadTxtFile.readTxt("C:\\Users\\Administrator\\Desktop\\商品.txt");
		 
		List readExcel = TrademarkCategoryUtil.readExcel("C:\\Users\\Administrator\\Desktop\\catagory.xls");
		
		//tradeMarkCaseCategoryDetilMapper.insertSelectiveList(readExcel);
		for(int i=0;i<readTxt.size();i++) {
			for(int j=0;j<readExcel.size();j++) {
				TradeMarkCaseCategory tradeMarkCaseCategory = (TradeMarkCaseCategory) readTxt.get(i);
				TradeMarkCaseCategoryDetil tradeMarkCaseCategoryDetil = (TradeMarkCaseCategoryDetil) readExcel.get(j);
				
				
				if(tradeMarkCaseCategory.getGoodName().equals(tradeMarkCaseCategoryDetil.getGoodName()) && tradeMarkCaseCategory.getSimilarGroup().equals(tradeMarkCaseCategoryDetil.getSimilarGroup()) ) {
					TradeMarkCaseCategoryDetil t=tradeMarkCaseCategoryDetilMapper.selectByGoodNameAndGroup(tradeMarkCaseCategoryDetil);
					//TradeMarkCaseCategoryDetil t = new TradeMarkCaseCategoryDetil();
					
					//TradeMarkCaseCategory tradeMarkCaseCategory = (TradeMarkCaseCategory) readTxt.get(i);
					//TradeMarkCaseCategoryDetil tradeMarkCaseCategoryDetil = (TradeMarkCaseCategoryDetil) readExcel.get(i);
					
					t.setCaseId(tradeMarkCaseCategory.getCaseId());
					t.setGoodClass(tradeMarkCaseCategory.getGoodClass());
					t.setSimilarGroup(tradeMarkCaseCategory.getSimilarGroup());
					t.setGoodCode(tradeMarkCaseCategory.getGoodCode());
					t.setGoodName(tradeMarkCaseCategory.getGoodName());
					t.setGoodKey(tradeMarkCaseCategory.getGoodKey());
					t.setGoodEnName(tradeMarkCaseCategoryDetil.getGoodEnName());
					t.setGoodNotes(tradeMarkCaseCategoryDetil.getGoodNotes());
					
					t.setId(t.getId());
					System.out.println(t);
					tradeMarkCaseCategoryDetilMapper.updateByPrimaryKeySelective(t);
					
				}
			}
		TradeMarkCaseCategoryDetil t = new TradeMarkCaseCategoryDetil();
		t.setSimilarGroup("4506");
		t.setGoodName("离婚调解服务");
		t= tradeMarkCaseCategoryDetilMapper.selectByGoodNameAndGroup(t);

		tradeMarkCaseCategoryDetilMapper.updateByPrimaryKeySelective(t);
		
		}

	}
	
	
//	@Test
	// 现存txt文件的内容
	public void Test2() {
		List readTxt = ReadTxtFile.readTxt("C:/support/guofang/goods.txt");
		for(int i=0;i<readTxt.size();i++) {
			TradeMarkCaseCategory tradeMarkCaseCategory = (TradeMarkCaseCategory) readTxt.get(i);
			TradeMarkCaseCategoryDetil t = new TradeMarkCaseCategoryDetil();
			t.setGoodClass(tradeMarkCaseCategory.getGoodClass());
			t.setSimilarGroup(tradeMarkCaseCategory.getSimilarGroup());
			t.setGoodCode(tradeMarkCaseCategory.getGoodCode());
			t.setGoodName(tradeMarkCaseCategory.getGoodName());
			t.setGoodKey(tradeMarkCaseCategory.getGoodKey());
			tradeMarkCaseCategoryDetilMapper.insertSelective(t);
		}
	}
	
	
	
	
//	@Test
	public void setNameEn() {//先执行test2将官网数据插入数据库，再执行setNameEn，将国芳数据的英文入库  --张紫维20180704，test1是乔康平所写，setNameEn是优化后的方法
		List<TradeMarkCaseCategoryDetil> detils = tradeMarkCaseCategoryDetilMapper.selectAll();
		List readExcel = TrademarkCategoryUtil.readExcel("C:/support/guofang/gf.xls");
		for(int i=0;i<detils.size();i++) {
			for(int j=0;j<readExcel.size();j++) {
				TradeMarkCaseCategoryDetil detil = detils.get(i);
				TradeMarkCaseCategoryDetil tradeMarkCaseCategoryDetil = (TradeMarkCaseCategoryDetil) readExcel.get(j);
				if(detil.getGoodName().equals(tradeMarkCaseCategoryDetil.getGoodName()) && detil.getSimilarGroup().equals(tradeMarkCaseCategoryDetil.getSimilarGroup()) ) {
					TradeMarkCaseCategoryDetil t=tradeMarkCaseCategoryDetilMapper.selectByGoodNameAndGroup(tradeMarkCaseCategoryDetil);
					t.setGoodEnName(tradeMarkCaseCategoryDetil.getGoodEnName());
					t.setGoodNotes(tradeMarkCaseCategoryDetil.getGoodNotes());
					tradeMarkCaseCategoryDetilMapper.updateByPrimaryKeySelective(t);
				}
			}
		}
	}

//	@Test
	public void Test3() {
		//List readTxt = ReadTxtFile.readTxt("C:\\Users\\Administrator\\Desktop\\商品.txt");
		 
		//List readExcel = TrademarkCategoryUtil.readExcel("C:\\Users\\Administrator\\Desktop\\catagory.xls");
		List readExcel = TrademarkCategoryUtil.readExcel("C:\\gf.xls");
		//tradeMarkCaseCategoryDetilMapper.insertSelectiveList(readExcel);
		int i = 0;
			for(int j=0;j<readExcel.size();j++) {
				TradeMarkCaseCategoryDetil tradeMarkCaseCategoryDetil = (TradeMarkCaseCategoryDetil)readExcel.get(j);
				TradeMarkCaseCategoryDetil selectByGoodName = tradeMarkCaseCategoryDetilMapper.selectByGoodNameAndGroup(tradeMarkCaseCategoryDetil);
				if(selectByGoodName != null) {
					
				}else {
					i++;
					System.out.println(tradeMarkCaseCategoryDetil.getGoodName()+"++++"+i);
				}
				
			}
			System.out.println("结束"+"++++"+i);
	}
	@Test
		public void Test4() {
			System.out.println("test");
		}
}
