package com.yootii.bdy.util;
 
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Random;

import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;


public class GraspUtil {
	private static CookieStore cookie;
	/**
	 * 采集核心函数：根据url，获得该url的html值
	 * @param redirectLocation
	 * @return
	 * @throws Exception
	 */
	public static  String getText(String redirectLocation ) throws Exception {
		redirectLocation = redirectLocation.trim();
		redirectLocation = redirectLocation.replace(" ", "%20");
		redirectLocation = redirectLocation+"&internal=yes";
		BasicHttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 600000);
		HttpConnectionParams.setSoTimeout(httpParams, 600000); 
		HttpGet httpget = new HttpGet(redirectLocation);
		DefaultHttpClient httpclient = new DefaultHttpClient(httpParams); 
		String[] agent=new String[]{"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.17 (KHTML, like Gecko) Chrome/24.0.1312.57 Safari/537.17",
		 "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)"};
		Random rad=new Random();
		httpclient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, agent[rad.nextInt(2)] );
		httpclient.getParams().setParameter(  CoreProtocolPNames.HTTP_CONTENT_CHARSET, "UTF-8"); 
		if (cookie!=null){
			httpclient.setCookieStore(cookie);
		} 
		HttpContext httpContext = new BasicHttpContext(); 
		String responseBody = "";
		try { 
			
			HttpResponse response = httpclient.execute(httpget, httpContext);
			if(cookie == null){
				cookie=httpclient.getCookieStore();
				}
			int status = response.getStatusLine().getStatusCode();
			if(status == 503){
				throw new Exception("服务器503错误，连接不上，"+redirectLocation); 
			}
			responseBody = org.apache.http.util.EntityUtils.toString(response.getEntity());
		} catch (Exception e) {
			e.printStackTrace();
			responseBody = null;
			throw e;
		} finally {
			httpget.abort();
		}
		return responseBody;
	}
    /**将指定元素转换为SOlR查询语句
     * 将beanClass类型转换为SOlR查询语句的方法，因为有时候会有重名问题，ignore为忽略的转换名称
     * @param bean beanClass类型
     * @param ignore 忽略字段
     * @return
     */
	 public static final <T> String GetQueryByClass(T bean) {
		 
		 String query = "";
	        //获取类的方法
	        Method[] methods = bean.getClass().getMethods();
	        int len = methods.length;
	        for(int i = 0; i < len; ++i) {
	            Method method = methods[i];
	            String methodName = method.getName();
	            //如果方法名是set开头的且名字长度大于3的
	            if(methodName.startsWith("get") && methodName.length() > 3) {
	                //获取方法的参数类型
	                Class[] types = method.getParameterTypes();
	                //只有一个参数的方法才继续执行
	                if(types.length == 0) {
	                    //取字段名且让其首字母小写
	                    String attrName = firstCharToLowerCase(methodName.substring(3));
	                    //map中是否有属性名
	                    if((!method.getReturnType().isArray())&&(!attrName.equals("class"))) {
	                    	Object value;
	                        try {
	                            //通过反射的方式执行bean的mothod方法，在这里相当于执行set方法赋值
	                        	value = method.invoke(bean, new Object[]{});
	                        	if(value!=null) query = query+"&"+attrName+"="+value.toString();
	                        } catch (IllegalAccessException e) {
	                            e.printStackTrace();
	                        } catch (InvocationTargetException e) {
	                            e.printStackTrace();
	                        }
	                    }
	                }
	            }
	        }

	        return query;
	    }
	 
	    //取字段名且让其首字母小写
	    public static String firstCharToLowerCase(String substring) {
	        if (substring!=null&& substring.charAt(0)>='A' && substring.charAt(0)<='Z'){
	            char[] arr = substring.toCharArray();
	            arr[0] = (char)(arr[0] + 32);
	            return new String(arr);
	        }else {
	            return substring;
	        }
	    }
}

