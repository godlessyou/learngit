package com.yootii.bdy.common;

import java.util.Date;




import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.security.model.Token;
import com.yootii.bdy.security.service.AuthenticationService;

public class CommonController {

	@Resource
	protected AuthenticationService authenticationService;
	
	private final static ThreadLocal local = new ThreadLocal();
	  
	private final static ThreadLocal local1 = new ThreadLocal();  

	public void addToken(Token str) {
		local.set(str);
		
	}
	public Object getToken() {
		
		return local.get();
		
	}   
	public void addURL(String str) {
		local1.set(str);
		
	}
	public Object getURL() {
		
		return local1.get();
		
	}  

	protected ReturnInfo checkUser(GeneralCondition gcon) {
		return authenticationService.authorize(gcon);
	}

	protected void makeOffsetAndRows(GeneralCondition gcon) {
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

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new DateEditor());
	}

}