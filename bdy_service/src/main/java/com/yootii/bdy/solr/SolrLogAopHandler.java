package com.yootii.bdy.solr;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.ibatis.javassist.ClassClassPath;
import org.apache.ibatis.javassist.ClassPool;
import org.apache.ibatis.javassist.CtClass;
import org.apache.ibatis.javassist.CtMethod;
import org.apache.ibatis.javassist.bytecode.CodeAttribute;
import org.apache.ibatis.javassist.bytecode.LocalVariableAttribute;
import org.apache.ibatis.javassist.bytecode.MethodInfo;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import com.yootii.bdy.agency.dao.AgencyMapper;
import com.yootii.bdy.agency.model.Agency;
import com.yootii.bdy.common.CommonController;
import com.yootii.bdy.customer.service.CustomerService;
import com.yootii.bdy.log.model.LogTypeEnum;
import com.yootii.bdy.security.model.Token;
import com.yootii.bdy.tmcase.dao.TradeMarkCaseMapper;
import com.yootii.bdy.tmcase.model.TradeMarkCase;
import com.yootii.bdy.user.service.UserService;
import com.yootii.bdy.util.JsonUtil;
import com.yootii.bdy.util.SpringContextUtil;

//@Aspect
public class SolrLogAopHandler {
	@Resource
	private SolrInfo solrInfo ;
	@Resource
	protected UserService userService ;
	
	@Resource
	protected CustomerService customerService;
	@Resource
	protected AgencyMapper agencyMapper;
	
	@Resource
	protected TradeMarkCaseMapper tradeMarkCaseMapper;
		
	SolrLogAopHandler(){
		
	}

	@Around( "execution(* com..*.*Mapper.update*(..))"
			+ " or execution(* com..*.*Mapper.insert*(..))"
			
			)
    public int around1(ProceedingJoinPoint joinPoint) throws Throwable{
		try {
			Object proceed = joinPoint.proceed(); //执行拦截的方法
		 
		CommonController co = new CommonController();
		Token token = (Token) co.getToken();
		Object url = co.getURL();
		String processURL = "";
	    String operate ="";
        if(url != null) {
        	processURL= processURL(url.toString());
        	if(LogTypeEnum.getDesByMethodName(processURL) !=null) {
        		operate = LogTypeEnum.getDesByMethodName(processURL).getDescription();
        	}
        }
		System.out.println(operate);
        System.out.println("bdy_service 的AOP");
        String declaringTypeName = joinPoint.getSignature().getDeclaringTypeName();  //方法所在类名
        String tableName = processClass(declaringTypeName); //数据库表名        
        Class<?> forName = Class.forName(declaringTypeName);  
        Object bean = SpringContextUtil.getBean(forName);  //获取对象        
        //Method[] methods = forName.getMethods(); //获取类下面的方法
        String methodName = joinPoint.getSignature().getName(); //获取拦截到的方法名称  
        String operateType = "";
        String substring1 = methodName.substring(0, 6);
        if("update".equals(substring1)) {
        	operateType="update";
        }
        if("insert".equals(substring1)) {
        	operateType="insert";
        }
        
        Date currentTime = new Date();  
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        String dateString = formatter.format(currentTime);  
        String logId = UUID.randomUUID().toString().replace("-", "");		
		SolrData log = new SolrData();
		log.setIdName("logId");
		
		List<Map<String,Object>> logtable = new ArrayList<Map<String,Object>>();
		Object[] args = joinPoint.getArgs();
	 	Properties properties =  new  Properties();
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream( "table_id.properties" );  
		properties.load(inputStream);
		String id = properties.getProperty(tableName);  //table_id.properties 里的key
		if(args.length>0) {
			for(int j = 0; j<args.length; j++) {				
				Object object2 = args[j];
				Map<String, Object> packParams =new HashMap<>();
				Method m = forName.getDeclaredMethod("selectByPrimaryKey", Integer.class);
				Object id2 = null;
				if(id != null) {
					id2 = packParamsById(object2, id);
				}
				
				if(id2 != null && object2!=null) {
					Integer parseInt = Integer.valueOf(id2.toString());
					Object invoke = m.invoke(bean, parseInt);
					//System.out.println(JsonUtil.toJson(invoke));
					String json = JsonUtil.toJson(invoke);
					/*int indexOf = json.indexOf("[");
					if(indexOf >0) {
						json = json.substring(0, indexOf);
						int lastIndexOf = json.lastIndexOf(",");
						if(lastIndexOf > 0) {
							json = json.substring(0, lastIndexOf);
						}
						json= json +"}";
					}*/
					packParams.put("primaryKey", parseInt);
					packParams.put("data", json);
				}	
				packParams.put("tableName", tableName);
				packParams.put("operate", operate);
				packParams.put("operateURL", url);
				packParams.put("methodName", methodName);
				packParams.put("operateType", operateType);
				packParams.put("operateDate", dateString);
				packParams.put("logId", logId);
				if(token != null) {
					if(token.getUserID() != null ) {
						Agency agency = agencyMapper.selectAgencyByUserId(token.getUserID());
						if(agency != null) {
							packParams.put("agencyId", agency.getId());
							packParams.put("agencyName", agency.getName());
						}
						packParams.put("operateUserId", token.getUserID());
						packParams.put("operateUserName", token.getUsername());
					}
					if(token.getCustomerID() != null) {
						if("tradeMarkCase".equals(tableName) && id2 != null) {
							TradeMarkCase tradeMarkCase = tradeMarkCaseMapper.selectByPrimaryKey(Integer.parseInt(id2.toString()));
							Agency agency = agencyMapper.selectByPrimaryKey(tradeMarkCase.getAgencyId());
							if(agency != null) {
								packParams.put("agencyId", agency.getId());
								packParams.put("agencyName", agency.getName());
							}
						}
						packParams.put("operateCustId", token.getCustomerID());
						packParams.put("operateCustName", token.getUsername());
					}
				}
				
				logtable.add(packParams);
			}
		}
		//System.out.println(logtable.toString());
			log.setDataTable(logtable);			
			SolrLog solr = new SolrLog();
			solr.createDocs(solrInfo, log);			
		} catch(Exception e) {
			
			e.printStackTrace();
			
		} finally {
		}
		return 1;
        
        
    }
	@Around("execution(* com..*.*Mapper.deleteByPrimaryKey(..))"
			)
    public int around2(ProceedingJoinPoint joinPoint) throws Throwable{
		try {
			
			
		CommonController co = new CommonController();
		Token token = (Token) co.getToken();
		Object url = co.getURL();
		String processURL = "";
	    String operate ="";
        if(url != null) {
        	processURL= processURL(url.toString());
        	if(LogTypeEnum.getDesByMethodName(processURL) !=null) {
        		operate = LogTypeEnum.getDesByMethodName(processURL).getDescription();
        	}
        }
        System.out.println("bdy_service 的AOP");
       
        String declaringTypeName = joinPoint.getSignature().getDeclaringTypeName();  //方法所在类名
        String tableName = processClass(declaringTypeName); //数据库表名        
        Class<?> forName = Class.forName(declaringTypeName);  
        Object bean = SpringContextUtil.getBean(forName);  //获取对象        
        //Method[] methods = forName.getMethods(); //获取类下面的方法
        String methodName = joinPoint.getSignature().getName(); //获取拦截到的方法名称   
        String operateType = "";
        String substring1 = methodName.substring(0, 6);
        if("delete".equals(substring1)) {
        	operateType="delete";
        }
        Date currentTime = new Date();  
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        String dateString = formatter.format(currentTime);  
        String logId = UUID.randomUUID().toString().replace("-", "");		
		SolrData log = new SolrData();
		log.setIdName("logId");
		
		List<Map<String,Object>> logtable = new ArrayList<Map<String,Object>>();
		Object[] args = joinPoint.getArgs();
	 	Properties properties =  new  Properties();
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream( "table_id.properties" );  
		properties.load(inputStream);
		String id = properties.getProperty(tableName);
		if(args.length>0) {
			for(int j = 0; j<args.length; j++) {				
				Object object2 = args[j];
				Map<String, Object> packParams =new HashMap<>();
				Method m = forName.getDeclaredMethod("selectByPrimaryKey", Integer.class);
				Object id2 = null;
				if(id != null) {
					id2 = packParamsById(object2, id);
				}
				if(id2 != null && object2!=null) {
					Integer parseInt = Integer.valueOf(object2.toString());
					Object invoke = m.invoke(bean, parseInt);
				//	System.out.println(JsonUtil.toJson(invoke));
					String json = JsonUtil.toJson(invoke);
				/*	int indexOf = json.indexOf("[");
					if(indexOf >0) {
						json = json.substring(0, indexOf);
						int lastIndexOf = json.lastIndexOf(",");
						if(lastIndexOf > 0) {
							json = json.substring(0, lastIndexOf);
						}
						json= json +"}";
					}*/
					packParams.put("primaryKey", parseInt);
					packParams.put("data", json);
				}	
				packParams.put("tableName", tableName);
				packParams.put("operate", operate);
				packParams.put("operateURL", url);
				packParams.put("methodName", methodName);
				packParams.put("operateType", operateType);
				packParams.put("operateDate", dateString);
				packParams.put("logId", logId);
				if(token != null) {
					if(token.getUserID() != null ) {
						Agency agency = agencyMapper.selectAgencyByUserId(token.getUserID());
						if(agency != null) {
							packParams.put("agencyId", agency.getId());
							packParams.put("agencyName", agency.getName());
						}
						packParams.put("operateUserId", token.getUserID());
						packParams.put("operateUserName", token.getUsername());
					}
					if(token.getCustomerID() != null) {
						if("tradeMarkCase".equals(tableName) && id2 != null) {
							TradeMarkCase tradeMarkCase = tradeMarkCaseMapper.selectByPrimaryKey(Integer.parseInt(id2.toString()));
							Agency agency = agencyMapper.selectByPrimaryKey(tradeMarkCase.getAgencyId());
							if(agency != null) {
								packParams.put("agencyId", agency.getId());
								packParams.put("agencyName", agency.getName());
							}
						}
						packParams.put("operateCustId", token.getCustomerID());
						packParams.put("operateCustName", token.getUsername());
					}
				}
				
				logtable.add(packParams);
			}
		}

			log.setDataTable(logtable);		
			Object proceed = joinPoint.proceed();  //执行拦截的方法
		
			SolrLog solr = new SolrLog();
			solr.createDocs(solrInfo, log);			
		} catch(Exception e) {
			
			e.printStackTrace();
			
		} finally {
		}
		return 1;
        
        
    }
	@Around("execution(* com..*.*Mapper.unbind*(..))"
			+ " or execution(* com..*.*Mapper.bind*(..))"
			+ " or execution(* com..*.*Mapper.delete*(..))"
			+ "&& !execution(* com..*.*Mapper.deleteByPrimaryKey(..))")
    public int around3(ProceedingJoinPoint joinPoint) throws Throwable{
		try {

		CommonController co = new CommonController();
		Token token = (Token) co.getToken();
		Object url = co.getURL();
		String processURL = "";
	    String operate ="";
        if(url != null) {
        	processURL= processURL(url.toString());
        	if(LogTypeEnum.getDesByMethodName(processURL) !=null) {
        		operate = LogTypeEnum.getDesByMethodName(processURL).getDescription();
        	}
        }
        System.out.println("bdy_service 的AOP");
        
   
        String declaringTypeName = joinPoint.getSignature().getDeclaringTypeName();  //方法所在类名
        String tableName = processClass(declaringTypeName); //数据库表名        
        Class<?> forName = Class.forName(declaringTypeName);  
        Object bean = SpringContextUtil.getBean(forName);  //获取对象        
        //Method[] methods = forName.getMethods(); //获取类下面的方法
        String methodName = joinPoint.getSignature().getName(); //获取拦截到的方法名称   
        String operateType = "";
        String substring1 = methodName.substring(0, 6);
        if("unbind".equals(substring1)) {
        	operateType="unbind";
        }
        String substring2 = methodName.substring(0, 4);
        if("bind".equals(substring2)) {
        	operateType="bind";
        }
        Date currentTime = new Date();  
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        String dateString = formatter.format(currentTime);  
        String logId = UUID.randomUUID().toString().replace("-", "");		
		SolrData log = new SolrData();
		log.setIdName("logId");
		
		List<Map<String,Object>> logtable = new ArrayList<Map<String,Object>>();
		Object[] args = joinPoint.getArgs();
	 	Properties properties =  new  Properties();
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream( "table_id.properties" );  
		properties.load(inputStream);
		String id = properties.getProperty(tableName);
		if(args.length>0) {
			Map<String, Object> packParams =new HashMap<>();
			Map<String, Object> map =new HashMap<>();
			String date="";
			for(int j = 0; j<args.length; j++) {				
				Object object2 = args[j];
				
				
				 map = packParams(object2);
				 date +=map.toString();
				
			}
			packParams.put("data", map.toString());
			
			packParams.put("tableName", tableName);
			packParams.put("operate", operate);
			packParams.put("operateURL", url);
			packParams.put("methodName", methodName);
			packParams.put("operateType", operateType);
			packParams.put("operateDate", dateString);
			packParams.put("logId", logId);
			if(token != null) {
				if(token.getUserID() != null ) {
					Agency agency = agencyMapper.selectAgencyByUserId(token.getUserID());
					if(agency != null) {
						packParams.put("agencyId", agency.getId());
						packParams.put("agencyName", agency.getName());
					}
					packParams.put("operateUserId", token.getUserID());
					packParams.put("operateUserName", token.getUsername());
				}
				if(token.getCustomerID() != null) {
					packParams.put("operateCustId", token.getCustomerID());
					packParams.put("operateCustName", token.getUsername());
				}
			}
			
			logtable.add(packParams);
		}

			log.setDataTable(logtable);		
			Object proceed = joinPoint.proceed();  //执行拦截的方法
		
			SolrLog solr = new SolrLog();
			solr.createDocs(solrInfo, log);			
		} catch(Exception e) {
			
			e.printStackTrace();
			
		} finally {
		}
		return 1;
        
        
    }
  /*  @After("execution(* com..*.*Mapper.delete*(..))")
    public void after(){
        System.out.println("饭后擦嘴。");
    }*/
    
  /*  @Before("execution(* * *(..))")
    public void before1(){
        System.out.println("匹配所有方法。");
    }*/
    
    /**
    * @Description 获取字段名和字段值
    * @Author 
    * @date 
    * @return Map<String,Object>
    */
    private Map<String,Object> getFieldsName(Class cls, String clazzName, String methodName, Object[] args) throws Exception {   
            Map<String,Object > map=new HashMap<String,Object>(); 


            ClassPool pool = ClassPool.getDefault();    
            ClassClassPath classPath = new ClassClassPath(cls);    
            pool.insertClassPath(classPath);    
                
            CtClass cc = pool.get(clazzName);    
            CtMethod cm = cc.getDeclaredMethod(methodName);    
            MethodInfo methodInfo = cm.getMethodInfo();  
            CodeAttribute codeAttribute = methodInfo.getCodeAttribute();    
            LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);    
            if (attr == null) {    
                // exception    
            }    
            int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;    
            for (int i = 0; i < cm.getParameterTypes().length; i++){    
                map.put( attr.variableName(i + pos),args[i]);//paramNames即参数名    
            }    
            return map;    
        }    
    
    private Map<String,Object> getFieldsNameValueMap(ProceedingJoinPoint joinPoint) throws Exception {   
        Object[] args = joinPoint.getArgs();
        String classType = joinPoint.getTarget().getClass().getName();    
        Class<?> clazz = Class.forName(classType);    
        String clazzName = clazz.getName();    
        String methodName = joinPoint.getSignature().getName(); //获取方法名称   
        Map<String,Object > map=new HashMap<String,Object>();  
        ClassPool pool = ClassPool.getDefault();    
        ClassClassPath classPath = new ClassClassPath(this.getClass());    
        pool.insertClassPath(classPath);    
        CtClass cc = pool.get(clazzName);    
        CtMethod cm = cc.getDeclaredMethod(methodName);    
        MethodInfo methodInfo = cm.getMethodInfo();  
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();    
        LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);    
        if (attr == null) {    
            throw new RuntimeException();
        }    
        int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;    
        for (int i = 0; i < cm.getParameterTypes().length; i++){    
            map.put( attr.variableName(i + pos),args[i]);//paramNames即参数名    
        }    
        return map;    
    }    
    
    /**
     * 将指定的对象转为Map
     *
     * @param obj
     * @return Map<String,String>
     * @Title: packParams
     * @author 
     */
    public static Map<String, Object> packParams(Object obj) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true); // 设置属性可以访问
            String key = field.getName();// 获取属性名
            Object value = "";// 获取属性的值
            try {
                value =  field.get(obj);
            } catch (IllegalArgumentException e) {
                continue;
            } catch (IllegalAccessException e) {
                continue;
            }
            resultMap.put(key, value);
        }
        return resultMap;
    }
    /*
     * 返回id(主键)  对应的值
     */
    public static Object packParamsById(Object obj,String id) {
        //Map<String, Object> resultMap = new HashMap<String, Object>();
        Field[] fields = obj.getClass().getDeclaredFields();
        Object value = "";// 获取属性的值
        for (Field field : fields) {
            field.setAccessible(true); // 设置属性可以访问
            String key = field.getName();// 获取属性名
            if (id.equals(key)) { 
	            try {
	                value =  field.get(obj);
	            } catch (IllegalArgumentException e) {
	                continue;
	            } catch (IllegalAccessException e) {
	                continue;
	            }
	          
            }
        }
        return value;
    }
    /**
     * 将包名转成数据库表名 
     * 例:com.yootii.bdy.user.dao.UserMapper   user
     */
    public static String processClass(String str) {
    	
    	String[] split = str.split("[.]");
    	String string = split[split.length-1];
    	String substring = string.substring(0, (string.length()-6));
    	if(Character.isLowerCase(substring.charAt(0))) {
    	    return substring;
    	    }
    	  else {
    	    return (new StringBuilder()).append(Character.toLowerCase(substring.charAt(0))).append(substring.substring(1)).toString();
    	  }
    }
    /**
     * 获取接口ID
     */
    public static String processURL(String str) {
    	String[] split = str.split("/");
    	String string1 = split[split.length-1];
    	String string2 = split[split.length-2];
		return string2+"/"+string1;
    	
    }
    
    public static void main(String[] args) {
    	String processClass = processURL("/bdy_service/interface/bill/modifybill");
    	System.out.println(processClass);
	}
}
