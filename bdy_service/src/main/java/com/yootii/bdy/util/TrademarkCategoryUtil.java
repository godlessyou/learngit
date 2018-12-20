package com.yootii.bdy.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;

import com.yootii.bdy.tmcase.dao.TradeMarkCaseCategoryDetilMapper;
import com.yootii.bdy.tmcase.dao.TradeMarkCaseCategoryMapper;
import com.yootii.bdy.tmcase.model.TradeMarkCaseCategory;
import com.yootii.bdy.tmcase.model.TradeMarkCaseCategoryDetil;

@Component
public class TrademarkCategoryUtil {

	@Resource
	private TradeMarkCaseCategoryDetilMapper tradeMarkCaseCategoryDetilMapper;

	public static class ReadTxtFile {

		public static List readTxt(String filePath) {
			List<TradeMarkCaseCategory> list = new ArrayList<>();
			try {
				File file = new File(filePath);
				if (file.isFile() && file.exists()) {
					InputStreamReader isr = new InputStreamReader(
							new FileInputStream(file), "utf-8");
					BufferedReader br = new BufferedReader(isr);
					String lineTxt = null;
					while ((lineTxt = br.readLine()) != null) {
						if (lineTxt.contains("%hpb%")) {
							String[] split = lineTxt.split("%hpb%");
							TradeMarkCaseCategory t = new TradeMarkCaseCategory();
							t.setGoodClass(split[0]);
							t.setSimilarGroup(split[1]);
							t.setGoodCode(split[2]);
							t.setGoodName(split[3]);
							t.setGoodKey(split[4]);
							list.add(t);
						}
					}
					br.close();
				} else {
					System.out.println("文件不存在!");
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("文件读取错误!");
			}
			return list;

		}
	}

	public static List<TradeMarkCaseCategoryDetil> readExcel(String filePath) {
		Map<Integer, List<ExcelRow>> map = ExcelUtil.readExcel2003(new File(
				filePath), true);
		List<ExcelRow> object = map.get(0);
		List<TradeMarkCaseCategoryDetil> list = new ArrayList<>();
		System.out.println(object.get(22377));
		for (int i = 1; i < object.size(); i++) {
			// Map<Object,Object> map1 = new HashMap<>();
			// List<Object> colList = object.get(0).getColList();
			List<Object> colList1 = object.get(i).getColList();
			TradeMarkCaseCategoryDetil t = new TradeMarkCaseCategoryDetil();
			t.setGoodName((String) colList1.get(0));
			t.setGoodEnName((String) colList1.get(1));
			t.setSimilarGroup((String) colList1.get(3));
			// t.setDetailID((String) colList1.get(2));
			// t.setDetailTypeNumber((String) colList1.get(3));
			// t.setUnionType((String) colList1.get(4));
			// t.setDescriptionGF((String) colList1.get(5));
			// t.setPageNo((String) colList1.get(6));
			// t.setPartition((String) colList1.get(7));
			// t.setParagraph((String) colList1.get(8));
			// t.setOrderId((String) colList1.get(9));
			// t.setId((Integer) (i));
			t.setGoodCode((String) colList1.get(2));
			t.setGoodClass((String) colList1.get(4));
			t.setGoodNotes((String) colList1.get(5));
			// map.put(colList.g, value)
			// System.out.println(t);
			list.add(t);

		}
		return list;
	}

	public void setGoodsName(String filePath) {
		List readTxt = ReadTxtFile.readTxt(filePath);
		if(readTxt!=null &&  readTxt.size()>0){
			
			//清理原有数据
			tradeMarkCaseCategoryDetilMapper.deleteData();
			
			for (int i = 0; i < readTxt.size(); i++) {
				TradeMarkCaseCategory tradeMarkCaseCategory = (TradeMarkCaseCategory) readTxt
						.get(i);
				TradeMarkCaseCategoryDetil t = new TradeMarkCaseCategoryDetil();
				t.setGoodClass(tradeMarkCaseCategory.getGoodClass());
				t.setSimilarGroup(tradeMarkCaseCategory.getSimilarGroup());
				t.setGoodCode(tradeMarkCaseCategory.getGoodCode());
				t.setGoodName(tradeMarkCaseCategory.getGoodName());
				t.setGoodKey(tradeMarkCaseCategory.getGoodKey());
				tradeMarkCaseCategoryDetilMapper.insertSelective(t);
			}
		}
	}

	public void setGoodsEnName(String filePath) {// 先执行test2将官网数据插入数据库，再执行setNameEn，将国芳数据的英文入库
													// --张紫维20180704，test1是乔康平所写，setNameEn是优化后的方法
		List<TradeMarkCaseCategoryDetil> detils = tradeMarkCaseCategoryDetilMapper
				.selectAll();
		List readExcel = readExcel(filePath);
		for (int i = 0; i < detils.size(); i++) {
			TradeMarkCaseCategoryDetil detil = detils.get(i);

			for (int j = 0; j < readExcel.size(); j++) {
				TradeMarkCaseCategoryDetil tradeMarkCaseCategoryDetil = (TradeMarkCaseCategoryDetil) readExcel
						.get(j);
				if (detil.getGoodName().equals(
						tradeMarkCaseCategoryDetil.getGoodName())
						&& detil.getSimilarGroup().equals(
								tradeMarkCaseCategoryDetil.getSimilarGroup())) {
					TradeMarkCaseCategoryDetil t = tradeMarkCaseCategoryDetilMapper
							.selectByGoodNameAndGroup(tradeMarkCaseCategoryDetil);
					t.setGoodEnName(tradeMarkCaseCategoryDetil.getGoodEnName());
					t.setGoodNotes(tradeMarkCaseCategoryDetil.getGoodNotes());
					tradeMarkCaseCategoryDetilMapper
							.updateByPrimaryKeySelective(t);
				}
			}
		}
	}

	public static void main(String[] args) {
		// "C:\\Users\\Administrator\\Desktop\\catagory.xls"

		// List readTxt =
		// ReadTxtFile.readTxt("C:\\Users\\Administrator\\Desktop\\2.txt");

		List readExcel = readExcel("D:\\gf.xls");

		System.out.println(readExcel);
	}
}