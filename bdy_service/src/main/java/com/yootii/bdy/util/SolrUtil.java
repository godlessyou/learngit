package com.yootii.bdy.util;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest.METHOD;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;

/**SOLR处理工具类
 * @author 陈圣东
 *
 */
public class SolrUtil{
	
	
	 /**将List<Map<String,Object>> 转换成SolrDocumentList
	 * @param list
	 * @param ignore
	 * @return
	 */
	public static final Collection<SolrInputDocument> GetSolrDocumentByClass(List<Map<String,Object>> list,List<String> ignore) {
		Collection<SolrInputDocument> doclist= new ArrayList<SolrInputDocument>();
		 
		 for(Map<String,Object> map: list) {
			 SolrInputDocument doc = new SolrInputDocument();
			 for(Map.Entry<String, Object> val :map.entrySet()) {
				 if(!ignore.contains(val.getKey()))
				 doc.addField(val.getKey(), val.getValue());
			 }
			 doclist.add(doc);
		 }
		 
		 return doclist;
	 }
		 

	
	

    /**将指定元素转换为SOlRDOC
     * 将beanClass类型转换为SolrDocument对象指定的方法，因为有时候会有重名问题，ignore为忽略的转换名称
     * @param bean beanClass类型
     * @param ignore 忽略字段
     * @return
     */
	 public static final <T> SolrDocument GetSolrDocumentByClass(T bean,List<String> ignore) {
		 
		 SolrDocument doc= new SolrDocument();
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
	                    if((!method.getReturnType().isArray())&&(!ignore.contains(attrName))) {
	                    	Object value;
	                        try {
	                            //通过反射的方式执行bean的mothod方法，在这里相当于执行set方法赋值
	                        	value = method.invoke(bean, new Object[]{});
	                        	doc.setField(attrName, value);
	                        } catch (IllegalAccessException e) {
	                            e.printStackTrace();
	                        } catch (InvocationTargetException e) {
	                            e.printStackTrace();
	                        }
	                    }
	                }
	            }
	        }

	        return doc;
	    }
	 
	    /**将指定元素转换为SOlR查询语句
	     * 将beanClass类型转换为SOlR查询语句的方法，因为有时候会有重名问题，ignore为忽略的转换名称
	     * @param bean beanClass类型
	     * @param ignore 忽略字段
	     * @return
	     */
		 public static final <T> String GetSolrQueryByClass(T bean,List<String> ignore) {
			 
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
		                    if((!method.getReturnType().isArray())&&(!ignore.contains(attrName))&&(!attrName.equals("class"))) {
		                    	Object value;
		                        try {
		                            //通过反射的方式执行bean的mothod方法，在这里相当于执行set方法赋值
		                        	value = method.invoke(bean, new Object[]{});
		                        	if(value!=null) query = query+" AND "+attrName+":\""+value.toString()+"\"";
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
	
	
    /**将SOlRDOC转换为指定元素
     * 将对象SolrDocument转换为beanClass类型指定的方法，因为有时候会有重名问题，ignore为忽略的转换名称
     * @param beanClass 需要转换的类
     * @param doc SOLRDOC元素
     * @param ignore 忽略字段
     * @return
     */
    public static final <T> T SolrDocumentToClass(Class<T> beanClass,SolrDocument doc,List<String> ignore) {
        T bean = null;
        try {
            //通过反射生成对象
            bean = beanClass.newInstance();
            //还可以用Class.forName生成对象
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        //获取类的方法
        Method[] methods = beanClass.getMethods();
        int len = methods.length;
        for(int i = 0; i < len; ++i) {
            Method method = methods[i];
            String methodName = method.getName();
            //如果方法名是set开头的且名字长度大于3的
            if(methodName.startsWith("set") && methodName.length() > 3) {
                //获取方法的参数类型
                Class[] types = method.getParameterTypes();
                //只有一个参数的方法才继续执行
                if(types.length == 1) {
                    //取字段名且让其首字母小写
                    String attrName = firstCharToLowerCase(methodName.substring(3));
                    //map中是否有属性名
                    if(doc.containsKey(attrName)&&(!ignore.contains(attrName))) {
                            Object value = doc.getFirstValue(attrName);
                            if(value == null) continue;
                            if(doc.getFieldValues(attrName).size()>1) {
                            	value = "";
                            	for(Object one:doc.getFieldValues(attrName)) {
                                	value =value+ ","+one.toString();
                            	}
                            	value = value.toString().substring(1);
                            } 
                            
                            if (types[0].getName().equals("java.lang.Integer")) {
                            	value=Integer.valueOf((String)value).intValue();
                            }
                            if (types[0].getName().equals("java.lang.Double")) {
                            	value=Double.valueOf(value.toString()).doubleValue();
                            }
                            if (types[0].getName().equals("java.math.BigDecimal")) {
                            	value=BigDecimal.valueOf(Double.valueOf(value.toString()).doubleValue());
                            }
                            
                        try {
                            //通过反射的方式执行bean的mothod方法，在这里相当于执行set方法赋值
                            method.invoke(bean, new Object[]{value});
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        return bean;
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
    
    /**将SOlRDOC转换为指定元素
     * 将对象SolrDocument转换为beanClass类型指定的方法，
     * @param beanClass 需要转换的类
     * @param doc SOLRDOC元素
     * @return 
     */    
    public static final <T> T SolrDocumentToClass(Class<T> beanClass,SolrDocument doc) {
    	List<String> ignore = new ArrayList<>();
    	return SolrDocumentToClass(beanClass,doc,ignore);
    }
	 /** 通过SOLR源进行商标查询
	 * @param value 查询参数集合
	 * @param client SOLR源
	 * @return 查询结果
	 * @throws Exception SOLR执行错误
	 */
	public static  QueryResponse solrQuery(Map<String, String> value,SolrClient client) throws Exception {
    	SolrQuery params = new SolrQuery();
        try {
	    	for(Map.Entry<String, String> entry : value.entrySet()) {
	    		params.set(entry.getKey(),entry.getValue());
	    	}
	//        params.set("q", "title:" + value + " issuetime:" + value + " author:" + value);
	//        params.set("start", begin);
	//        params.set("rows", max);
	        params.set("wt", "json");

	       // System.out.println(params.toQueryString());
			QueryResponse rsp = client.query(params,METHOD.POST);
	        return rsp;
		} catch (SolrServerException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			throw e;
		}
	}
        public static  QueryResponse solrQuery(Map<String, String> value,Map<String, List<String>> otherList,SolrClient client) throws Exception {
	    	SolrQuery params = new SolrQuery();
	        try {
		    	for(Map.Entry<String, String> entry : value.entrySet()) {
		    		params.set(entry.getKey(),entry.getValue());
		    	}
		    	for(Map.Entry<String, List<String>> entry : otherList.entrySet()) {
		    		if(entry.getKey().equals("facet.query")) {
		    			for(String facetQuery:entry.getValue()) {
		    				params.addFacetQuery(facetQuery);
		    			}
		    		}else {
		    			for(String val:entry.getValue()) {
		    				params.add(entry.getKey(), val);
		    			}
		    		}
		    		
		    	}
		//        params.set("q", "title:" + value + " issuetime:" + value + " author:" + value);
		//        params.set("start", begin);
		//        params.set("rows", max);
		        params.set("wt", "json");


				System.out.println(params);
				QueryResponse rsp = client.query(params,METHOD.POST);
		        return rsp;
			} catch (SolrServerException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
				throw e;
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
				throw e;
			}
	        

	    }
	/**SOLR日期转换
	 * 将Date类型的数据转换为SOLR可以识别的String类型
	 * @param date 需要转换的日期
	 * @return SOLR可以识别的String型日期
	 */
	public static String getSolrDate(Date date) {
		String solrdate = "";
		SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-ddHH:mm:ss" );
		solrdate =  sdf.format(date);
		solrdate = solrdate.substring(0, 10) +"T"+solrdate.substring(10, 18) +"Z";
		return solrdate;
	}
}