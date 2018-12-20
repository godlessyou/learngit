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
@Order(1)
public class AopLog {
	
	private static final Logger logger = Logger.getLogger(AopLog.class);
	
	@Resource
	protected TranslationService translationService;	
	
	
	AopLog(){
		
	}

	

	@Around("execution(* com..*.*Controller.*(..))")
	public Object process(ProceedingJoinPoint point) throws Throwable {
		logger.debug("------------------bdy_service-----------------------------");
		logger.debug("---------------------请求开始--------------------------------");
		Long begin = System.nanoTime();
		logger.debug("线程："+String.valueOf(Thread.currentThread().getId())+"开始时间："+String.valueOf(begin));
		
		
		//System.out.println("开始时间："+new SimpleDateFormat("HH:mm:ss:SSS").format(new Date()));	
		
		//判断是否是内部接口
		Boolean internal = false;
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        if(request!=null) {
			if(request.getParameter("internal")!=null)
				internal = request.getParameter("internal").equals("yes");
			String url = request.getRequestURL().toString();
	        String method = request.getMethod();
	        String uri = request.getRequestURI();
	        String queryString = request.getQueryString();
	        logger.debug("请求开始, 各个参数, url: "+url+", method: "+method+", uri: "+uri+", params: "+queryString);
		}
        
        Object returnValue = point.proceed();
        logger.debug("内部接口："+internal.toString());
        logger.debug("输出语音为："+com.yootii.bdy.common.Globals.getLanguage());

        if(returnValue != null) 
        	logger.debug("文件类型为："+returnValue.getClass().getName());
        logger.debug("线程："+String.valueOf(Thread.currentThread().getId())+" 方法名称："+point.getSignature().getName() + " 耗时："+String.valueOf(System.nanoTime() - begin));
        logger.debug("---------------------请求结束--------------------------------");
        return returnValue;
        
    }
	
	@Around("execution(* com..*.*Mapper.*(..))")
	public Object process2(ProceedingJoinPoint point) throws Throwable {
		logger.debug("------------------数据库操作开始-----------------------------");
		Long begin = System.nanoTime();
		logger.debug("线程："+String.valueOf(Thread.currentThread().getId())+"开始时间："+String.valueOf(begin));
        Object returnValue = point.proceed();
        logger.debug("线程："+String.valueOf(Thread.currentThread().getId())+" 方法名称："+point.getSignature().getName() + " 耗时："+String.valueOf(System.nanoTime() - begin));
        logger.debug("---------------------数据库操作结束--------------------------------");
        return returnValue;
        
    }
	
	
	
	@Around("execution(* com.yootii.bdy.solr.SolrSend*.*(..))")
	public Object process3(ProceedingJoinPoint point) throws Throwable {
		logger.debug("---------------------SOLR操作开始-----------------------------");
		Long begin = System.nanoTime();
		logger.debug("线程："+String.valueOf(Thread.currentThread().getId())+"开始时间："+String.valueOf(begin));
        Object returnValue = point.proceed();
        logger.debug("线程："+String.valueOf(Thread.currentThread().getId())+" 方法名称："+point.getSignature().getName() + " 耗时："+String.valueOf(System.nanoTime() - begin));
        logger.debug("---------------------SOLR操作结束-----------------------------");
        return returnValue;
        
    }
	
	@Around("execution(* com..*.*Service.*(..))")
	public Object process4(ProceedingJoinPoint point) throws Throwable {
		logger.debug("---------------------Service操作开始-----------------------------");
		Long begin = System.nanoTime();
		logger.debug("线程："+String.valueOf(Thread.currentThread().getId())+"开始时间："+String.valueOf(begin));
        Object returnValue = point.proceed();
        logger.debug("线程："+String.valueOf(Thread.currentThread().getId())+" 方法名称："+point.getSignature().getName() + " 耗时："+String.valueOf(System.nanoTime() - begin));
        logger.debug("---------------------Service操作结束-----------------------------");
        return returnValue;
        
    }
	
	
}
