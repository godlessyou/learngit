package com.yootii.bdy.common;

import com.yootii.bdy.security.model.Token;

public class Globals {
	//返回的消息类型
	public final static Integer MESSAGE_TYPE_SESSION_INVALID = -1;// session失效

	public final static Integer MESSAGE_TYPE_GETDATA_FAILED = -2;// 操作数据库失败
	
	public final static Integer MESSAGE_TYPE_AUTHORTY_INVALID = -3;// 没有访问权限
	
	public final static Integer MESSAGE_TYPE_PARAM_INVALID = -4;// 参数不正确
	
	public final static Integer MESSAGE_TYPE_LINK_INVALID = -5;// 无效的链接

	public final static Integer MESSAGE_TYPE_OPERATION_INVALID = -6;// 无效操作
	
	public final static Integer MESSAGE_TYPE_OPERATION_EXCEPTION = -7;// 发生异常
	
	
	private final static ThreadLocal<String> languageLocal = new ThreadLocal<String>();

	private final static ThreadLocal<Token> TokenLocal = new ThreadLocal<Token>();
	

	
	//设置用户的语言环境
	public static void setLanguage(String language) {
		languageLocal.set(language);
		
	}
	
	public static String getLanguage() {			
		String language = (String)languageLocal.get();
		if (language==null){
			language="en";
			languageLocal.set(language);
		}			
		return language;			
	}  
	//设置用户的Token
	public static void setToken(Token token) {
		TokenLocal.set(token);
		
	}
	
	public static Token getToken() {			
		return TokenLocal.get();			
	} 
		
}
