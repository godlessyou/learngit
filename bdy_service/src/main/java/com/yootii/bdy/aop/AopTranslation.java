package com.yootii.bdy.aop;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.translation.service.TranslationService;

@Aspect
@Order(2)
public class AopTranslation {
	
	private static final Logger logger = Logger.getLogger(AopTranslation.class);
	
	@Resource
	protected TranslationService translationService;	
	
	
	AopTranslation(){
		
	}

	

	@Around("execution(* com..*.*Controller.*(..))")
	public Object process(ProceedingJoinPoint point) throws Throwable {
//		logger.debug("------------------bdy_service-----------------------------");
//		logger.debug("---------------------请求开始--------------------------------");
//		logger.debug(""+String.valueOf(Thread.currentThread().getId())+"开始时间："+String.valueOf(System.nanoTime()));
//		
		
		//System.out.println("开始时间："+new SimpleDateFormat("HH:mm:ss:SSS").format(new Date()));	
		
		//判断是否是内部接口
		Boolean internal = false;
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        if(request!=null) {
			if(request.getParameter("internal")!=null)
				internal = request.getParameter("internal").equals("yes");
//			String url = request.getRequestURL().toString();
//	        String method = request.getMethod();
//	        String uri = request.getRequestURI();
//	        String queryString = request.getQueryString();
//	        logger.debug("请求开始, 各个参数, url: "+url+", method: "+method+", uri: "+uri+", params: "+queryString);
		}
        
        Object returnValue = point.proceed();
        if(returnValue == null) return returnValue;
//        logger.debug("内部接口："+internal.toString());
//        logger.debug("输出语音为："+com.yootii.bdy.common.Globals.getLanguage());
//        logger.debug("文件类型为："+returnValue.getClass().getName());
//        logger.debug("结束时间："+String.valueOf(System.nanoTime()));
//        logger.debug("---------------------请求结束--------------------------------");
        if(internal) return returnValue;
        return translationService.translationObject((ReturnInfo)returnValue, "cn", com.yootii.bdy.common.Globals.getLanguage());
    }
}
