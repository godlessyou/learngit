package com.yootii.bdy.util;

import com.yootii.bdy.common.GeneralCondition;

public class GonUtil {
	public static void makeOffsetAndRows(GeneralCondition gcon) {
		// 当前页号（默认0）
		Integer defaultPageNo = 1;
		// 每页大小（默认10）
		Integer defaultPageSize = 10;
		// 截止页（默认1）
		Integer defaultPageEnd = 1;

		Integer pageNo = (Integer) gcon.getPageNo();
		Integer pageSize = (Integer) gcon.getPageSize();
		Integer pageEnd = defaultPageEnd;

		int startPage = defaultPageNo;
		if (pageNo != null) {
			startPage = pageNo;
		}
		if (startPage > 0) {
			startPage--;
		}

		gcon.setPageNo(pageNo);

		Integer offset = startPage * defaultPageSize;

		Integer rows = defaultPageSize * defaultPageEnd;

		if (pageSize != null && pageSize > 0) {
			offset = startPage * pageSize;
			rows = pageSize * defaultPageEnd;
		}

		gcon.setOffset(offset);
		gcon.setRows(rows);
	}
}
