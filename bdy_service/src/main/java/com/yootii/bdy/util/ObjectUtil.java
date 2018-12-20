package com.yootii.bdy.util;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jsoup.helper.StringUtil;

import com.yootii.bdy.tmcase.model.TradeMarkCase;
import com.yootii.bdy.tmcase.model.TradeMarkCaseCategory;




public class ObjectUtil {
	public static Object getPropertyValue(Object obj, String filedName) {
		try {
			Class clazz = obj.getClass();
			PropertyDescriptor pd = new PropertyDescriptor(filedName, clazz);
			Method getMethod = pd.getReadMethod();// 获得get方法
			if (pd != null) {
				Object result = getMethod.invoke(obj);// 执行get方法返回一个Object
				// System.out.println(result);
				return result;

			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IntrospectionException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static Map<String,Object>  getPropertyNamesAndValuesByList(Object obj, List<String> list) {
		
		Map<String,Object> propertyMap= new HashMap<String,Object>();
		try {
			Class<? extends Object> clazz = obj.getClass();
			Field[] fields = obj.getClass().getDeclaredFields();// 获得属性
			for (Field field : fields) {
				String propertyName=field.getName();
				if(list.contains(propertyName)) {
					Object value=getPropertyValue(obj, propertyName);
					if (value!=null){
						propertyMap.put(propertyName,value);
					}
				}
			}
			Field[] fields2 = obj.getClass().getSuperclass().getDeclaredFields();// 获得属性
			for (Field field : fields2) {
				String propertyName=field.getName();
				if(list.contains(propertyName)) {
					Object value=getPropertyValue(obj, propertyName);
					if (value!=null){
						propertyMap.put(propertyName,value);
					}
				}				
			}
			 
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} 
		return propertyMap;
	}	 
	
	public static String Method(Object obj) {
		String result = "";
		try {
			Class clazz = obj.getClass();
			Field[] fields = obj.getClass().getDeclaredFields();// 获得属性
			for (Field field : fields) {
				PropertyDescriptor pd = new PropertyDescriptor(field.getName(),
						clazz);
				Method setMethod = pd.getReadMethod();// 获得get方法
				Object miniObj = setMethod.invoke(obj);// 执行get方法返回一个Object
				System.out.println(miniObj);
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IntrospectionException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	public static List<String>  getPropertyNames(Object obj) {
		
		List<String> propertyNames= new ArrayList<String>();
		try {
			Class<? extends Object> clazz = obj.getClass();
			Field[] fields = obj.getClass().getDeclaredFields();// 获得属性
			for (Field field : fields) {
				String propertyName=field.getName();
				propertyNames.add(propertyName);
			}
			Field[] fields2 = obj.getClass().getSuperclass().getDeclaredFields();// 获得属性
			for (Field field : fields2) {
				String propertyName=field.getName();
				propertyNames.add(propertyName);
			}
			 
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} 
		return propertyNames;
	}
	
	
	
	//获取有值的那些属性
	public static List<String>  getPropertyNamesWithValue(Object obj) {
		
		List<String> propertyNames= new ArrayList<String>();
		try {
			Class<? extends Object> clazz = obj.getClass();
			Field[] fields = obj.getClass().getDeclaredFields();// 获得属性
			for (Field field : fields) {
				String propertyName=field.getName();
				Object value=getPropertyValue(obj, propertyName);
				if (value!=null){
					propertyNames.add(propertyName);
				}
			}
			Field[] fields2 = obj.getClass().getSuperclass().getDeclaredFields();// 获得属性
			for (Field field : fields2) {
				String propertyName=field.getName();
				Object value=getPropertyValue(obj, propertyName);
				if (value!=null){
					propertyNames.add(propertyName);
				}				
			}
			 
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} 
		return propertyNames;
	}
	
	
	public static List<Map<String, Class<?> >>  getPropertys(Object obj) {
		
		List<Map<String, Class<?> >> properties= new ArrayList<Map<String, Class<?> >>();
		Class<?> type = null;
		try {
			Class<? extends Object> clazz = obj.getClass();
			Field[] fields = obj.getClass().getDeclaredFields();// 获得属性
			for (Field field : fields) {
				Map<String, Class<?> > property = new HashMap<String, Class<?> >();				
				String propertyName=field.getName();
				type = field.getType();				
				property.put(propertyName, type);	
				properties.add(property);
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} 
		return properties;
	}
	

	// 获取与对象属性相同的参数，并保存到Map中。
	public static void setProperty(Map<String, Object> paramMap,Object bean, String name, String value){  
		List<Map<String, Class<?>>> properties= getPropertys(bean);		
		for (Map<String, Class<?>> property: properties){
			boolean find = false;
			Iterator<Entry<String, Class<?>>> iter =  property.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry<String, Class<?>> entry = (Map.Entry<String, Class<?>>) iter
						.next();
				String key = entry.getKey();
				Class<?> type = entry.getValue();
				if (key.equalsIgnoreCase(name)){					
				    String className=type.getName();
				    if (className.equals("java.lang.String")){
				    	paramMap.put(key, value);
				    }else if (className.equals("java.util.Date")){		
				    	if (value!=null && !value.equals("")){
							Date date = DateTool.StringToDate(value);
							paramMap.put(key, date);
				    	}
					}else if(className.equals("java.lang.Integer")){
						if (StringUtils.isNum(value)){
							Integer id = Integer.parseInt(value);
							paramMap.put(key, id);
						}
					}
				    
				    find = true;
				    break;	
				}
			}
			
			if (find){
				  break;
			}						
		}
	}
	
	
	public static void setBeanProperty(Object obj,String propertyName,Object value){  
        Class<? extends Object> clazz = obj.getClass();//获取对象的类型  
        //获取 clazz 类型中的 propertyName 的属性描述器  
        PropertyDescriptor pd;
		try {
			pd = new PropertyDescriptor(propertyName, clazz);	
			Method setMethod = pd.getWriteMethod();//从属性描述器中获取 set 方法  
			setMethod.invoke(obj, new Object[]{value});//调用 set 方法将传入的value值保存属性中去  			
		} catch (IntrospectionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	 
    }
	
	
	public static void setProperty(Object obj,String propertyName,String value){  
        Class<? extends Object> clazz = obj.getClass();//获取对象的类型  
        //获取 clazz 类型中的 propertyName 的属性描述器  
        PropertyDescriptor pd;
		try {
			pd = new PropertyDescriptor(propertyName, clazz);
			
			String className=pd.getPropertyType().getName();
			Method setMethod = pd.getWriteMethod();//从属性描述器中获取 set 方法  
			if (className.equals("java.lang.String")){
				setMethod.invoke(obj, new Object[]{value});//调用 set 方法将传入的value值保存属性中去  
		    }else if (className.equals("java.util.Date")){	
		    	if (value!=null && !value.equals("") && value.length()>7){
					Date date = DateTool.StringToDate(value);
					setMethod.invoke(obj, new Object[]{date});//调用 set 方法将传入的value值保存属性中去  
		    	}
			}else if(className.equals("java.lang.Integer")){				
				if (StringUtils.isNum(value)){
					Integer id = Integer.parseInt(value);
					setMethod.invoke(obj, new Object[]{id});//调用 set 方法将传入的value值保存属性中去  
				}
			}
			
		} catch (IntrospectionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	 
    }
	
	// 从Map中获取与对象属性相同的参数，设置对象的属性值为参数值。
	public static void setPropertys(Map<String, Object> paramMap, Object bean){
		
		List<String> propertyNames = getPropertyNames(bean);
		
		for (String propertyName:propertyNames){
			Iterator<Entry<String, Object>> iter =  paramMap.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry<String, Object> entry = (Map.Entry<String, Object>) iter
						.next();
				String name = entry.getKey();
				Object value=entry.getValue();
				if (propertyName.equalsIgnoreCase(name)){					
					if (value!=null){
						setBeanProperty(bean, propertyName, value);
					}
					break;
				}
			}			
		}
	}

	
	
	
	// 从源对象中获取与目标对象属性相同的参数，设置目标对象的属性值为源对象的参数值。
	public static void setProperty(Object sourceBean, Object targetBean){
			 	 
		 List<String> targetNames =  getPropertyNames(targetBean);				 
		 List<String> sourceNames =  getPropertyNames(sourceBean);
		 for (String propertyName: targetNames){
			 for (String name: sourceNames){
				
				 if(propertyName.equalsIgnoreCase(name)){
					 Object value=getPropertyValue(sourceBean, name);
					 if (value!=null){
						 setBeanProperty(targetBean, name, value);
					 }
					 break;
				 }
			 }
		 }	
		
	}
	
	
	// 从源对象中获取与目标对象属性相同的参数，设置目标对象的属性值为源对象的参数值。
	public static void setObjectProperty(Object sourceBean, Object targetBean, List<String> propertyNames ){
			
		 for (String name: propertyNames){
			 Object value=getPropertyValue(sourceBean, name);
			 if (value!=null){				 
				 //设置属性值不为空的属性
				 setBeanProperty(targetBean, name, value);
			 }		
		 }
		
	}
	
	
	
	
	// 从两个对象中，获取具有不同属性值的属性。只能比较String, Integer等基本类型的对象
	public static boolean compareObject(Object bean1, Object bean2){
		boolean same=true;
		List<String> propertyNames= getPropertyNames(bean1);
		
		 for (String name: propertyNames){
			 if (name.equals("id")){ //不比较两条记录的编号
				 continue;
			 }
			 Object value1=getPropertyValue(bean1, name);
			 Object value2=getPropertyValue(bean2, name);
			 				 
			 if (value1!=null && value2!=null){
				 if (value1  instanceof java.lang.String || value1  instanceof java.lang.Integer
						 || value1 instanceof java.util.Date){
					 String s1=value1.toString();
					 String s2=value2.toString();
					 if (!s1.equals(s2)){				 
						 same=false;
						 return same;
					 }
				 }else  if (value1  instanceof java.util.List){
					 int size1 = ((java.util.List) value1).size();
					 int size2 = ((java.util.List) value2).size();					 
					 if (size1!=size2){
						 same=false;
						 return same;
					 }else{
						 boolean hasDifference=compareListObject((java.util.List)value1, (java.util.List)value2);
						 if (hasDifference){
							return hasDifference;
						 }
					 }
				 }
				 else{
					 same=false;
					 return same;
				 }
			 }else if ((value1==null && value2!=null) || (value1!=null && value2==null)){
				 same=false;
				 return same;
			 }
			
		 }
		 
		 return same;
		
	}
	
	
	
	
	// 从两个对象中，获取具有不同属性值的属性。
	public static List<String> getDifferenceProperty(Object bean1, Object bean2, List<String> propertyNames ){
			
		List<String> result=new ArrayList<String>();
		 for (String name: propertyNames){
			 Object value1=getPropertyValue(bean1, name);
			 Object value2=getPropertyValue(bean2, name);
			 if (value1!=null && value2!=null){				 
				 if (value1  instanceof java.lang.String || value1  instanceof java.lang.Integer
						 || value1 instanceof java.util.Date){
					 String s1=value1.toString();
					 String s2=value2.toString();
					 if (!s1.equals(s2)){				 
						 result.add(name);					 
					 } 
				 }
				 else  if (value1  instanceof java.util.List){
					 int size1 = ((java.util.List) value1).size();
					 int size2 = ((java.util.List) value2).size();
					 if (size1!=size2){
						 result.add(name);
					 }else{
						 boolean hasDifference=compareListObject((java.util.List)value1, (java.util.List)value2);
						 if (hasDifference){
							 result.add(name);
						 }						
					 }
				 }
				 else {	
					 result.add(name);	
				 }
				
			 }else if ((value1==null && value2!=null) || (value1!=null && value2==null)){
				 result.add(name);
			 }
		 }
		 
		 return result;
		
	}
	
	

	public static String specialMethod(Object obj) {
		String result = "";
		try {
			Class clazz = obj.getClass();
			Field[] fields = obj.getClass().getDeclaredFields();// 获得属性
			for (Field field : fields) {
				PropertyDescriptor pd = new PropertyDescriptor(field.getName(),
						clazz);
				Method getMethod = pd.getReadMethod();// 获得get方法
				Object miniObj = getMethod.invoke(obj);// 执行get方法返回一个Object
				String data = "";
				boolean isDate = false;
				if (miniObj==null){
					continue;
				}
				if (miniObj instanceof Date) {
					isDate = true;
					Date date = (Date) miniObj;
					if (date != null) {
						data = DateTool.getDate(date);
					}
				} else {
					data = miniObj.toString();
				}
				// 如果字符串是数字并且长度小于2，意味着是商品和服务中的id，去掉这个id。
				boolean isNumber = StringUtil.isNumeric(data);
				boolean addFlag = true;
				if (isNumber) {
					if (data.length() <= 2) {
						addFlag = false;
					}
				}
				if (addFlag) {
					if (result.equals("")) {
						result = result + data;
					} else {
						if (isDate) {
							result = result + " " + data;
						} else {
							result = result + "-" + data;
						}
						isDate = false;
					}
				}

				// System.out.println(miniObj);
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IntrospectionException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static void processExportData(List<Object> tmList,
			List<String> columns, ArrayList<ArrayList<Object>> result)
			throws Exception {

		for (Object tm : tmList) {
			ArrayList<Object> rows = new ArrayList<Object>();
			for (String filed : columns) {
				Object obj = getPropertyValue(tm, filed);
				/*
				 * if (filed.equals("liucheng")){ String liuchengData = "";
				 * List<Object> list=(List<Object>)obj; List<ReturnLiucheng>
				 * liuchengs=(List<ReturnLiucheng>)obj; for (ReturnLiucheng
				 * lc:liuchengs){ String status=lc.getStatus(); Date
				 * date=lc.getDate(); if(status!=null && !status.equals("")){
				 * String statusDate=null; if (date!=null){
				 * statusDate=DateTool.getDate(date); } if(statusDate!=null &&
				 * !statusDate.equals("")){ if(liuchengData.equals("")){
				 * liuchengData=liuchengData+statusDate +" "+status; }else{
				 * liuchengData=liuchengData+";"+statusDate +" "+status; } } } }
				 * rows.add(liuchengData); }else if (filed.equals("goods")){
				 * String goodsData = ""; List<ReturnCategory>
				 * goods=(List<ReturnCategory>)obj; for (ReturnCategory
				 * rc:goods){ String tmGroup=rc.getTmgroup(); String
				 * service=rc.getService(); if(tmGroup!=null &&
				 * !tmGroup.equals("")){ if(service!=null &&
				 * !service.equals("")){ if(goodsData.equals("")){
				 * goodsData=goodsData+tmGroup +"-"+service; }else{
				 * goodsData=goodsData+";"+tmGroup +"-"+service; } } } }
				 * rows.add(goodsData); }
				 */
				// 重构上述代码，采用通用的方法处理对象中的List类型的属性，代码更简洁
				if (obj == null){
					String value = "";
					rows.add(value);
					continue;
				}
				if (obj instanceof List) {
					String dataCollection = "";
					List<Object> list = (List<Object>) obj;
					for (Object lobj : list) {
						String data = specialMethod(lobj);
						if (dataCollection.equals("")) {
							dataCollection = dataCollection + data;
						} else {
							dataCollection = dataCollection + ";" + data;
						}
					}
					rows.add(dataCollection);
				} else if (obj instanceof Date) {
					Date date = (Date) obj;
					String statusDate = "";
					if (date != null) {
						statusDate = DateTool.getDate(date);
					}
					rows.add(statusDate);
				} else {
					rows.add(obj);
				}					
				
			}

			result.add(rows);
		}

	}
	
	
	public static boolean compareListObject(List<Object>oldList,List<Object>newList){
		boolean hasDifference=false;
		for(Object newObject:newList){				
			boolean sameOne=false;			
			for(Object oldObject:oldList){
				sameOne=compareObject(oldObject, newObject);
				if (sameOne){
					break;
				}
			}					
			if (!sameOne){
				hasDifference=true;
				break;
			}				
		}	
		
		return hasDifference;
	}
	
	

	public static void main(String[] args) {
		TradeMarkCase tradeMarkCase1 = new TradeMarkCase();
		TradeMarkCase tradeMarkCase2 = new TradeMarkCase();
		
		tradeMarkCase1.setGoodClasses("32");
		tradeMarkCase1.setAgencyId(2);
		Date date=new Date();
		tradeMarkCase1.setAppDate(date);
		
		List<TradeMarkCaseCategory> goods=new ArrayList<TradeMarkCaseCategory>();
		
		TradeMarkCaseCategory tradeMarkCaseCategory=new TradeMarkCaseCategory();
		tradeMarkCaseCategory.setGoodName("aaaa");
		goods.add(tradeMarkCaseCategory);
		tradeMarkCase1.setGoods(goods);
		
		
		tradeMarkCase2.setGoodClasses("32");
		tradeMarkCase2.setAgencyId(2);
		tradeMarkCase2.setAppDate(date);
		
		
		List<TradeMarkCaseCategory> goods2=new ArrayList<TradeMarkCaseCategory>();
		TradeMarkCaseCategory tradeMarkCaseCategory2=new TradeMarkCaseCategory();
		tradeMarkCaseCategory2.setGoodName("bbbb");
		goods2.add(tradeMarkCaseCategory2);
		tradeMarkCase2.setGoods(goods2);
		
		boolean same=ObjectUtil.compareObject(tradeMarkCase1, tradeMarkCase2);
		System.out.println("is same: " + same);
		
	}
	

}
