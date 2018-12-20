package com.yootii.bdy.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ExcelTool {
	
	
	// 获取excel表格的每一列与属性名的对应关系
	public static Map<Integer, String> getProperNameCol(
			List<ExcelRow> excelContent, Map<String, String> colMaps) {

		int rowNumber = 0;
		if (excelContent != null) {
			rowNumber = excelContent.size();
		}

		if (rowNumber < 2) {
			System.out.println("no data in excel");
			return null;
		}

		Map<Integer, String> propertyNames = new HashMap<Integer, String>();
		try {
			ExcelRow cxcelRow=excelContent.get(0);
			List<Object> titles = cxcelRow.getColList();
			int colSize = titles.size();

			for (int j = 0; j < colSize; j++) {
				Object titleValue = titles.get(j);
				if (titleValue != null) {
					String title = titleValue.toString();

					Iterator<Entry<String, String>> iter = colMaps.entrySet()
							.iterator();
					while (iter.hasNext()) {
						Map.Entry<String, String> entry = (Map.Entry<String, String>) iter
								.next();
						String titleName = entry.getKey();
						String propertyName = entry.getValue();
						// 通过预先设置好的每列的名称与excel表格中的每列名称进行匹配
						// 形成列的序号与对象属性名的对应关系，并保存到propertyNames中。
						if (title.equals(titleName)) {
							propertyNames.put(j, propertyName);
							break;
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();

		}
		return propertyNames;

	}
		
	

}
