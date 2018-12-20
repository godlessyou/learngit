package com.yootii.bdy.translation.service.Impl;



import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.common.TranslationNameList;
import com.yootii.bdy.solr.SolrInfo;
import com.yootii.bdy.solr.SolrTranslationSend;
import com.yootii.bdy.translation.service.TranslationService;
import com.yootii.bdy.util.ObjectUtil;


@Service
public class TranslationServiceImpl implements TranslationService {

	@Resource
	private SolrInfo solrInfo ;	
	
	private static Map<String,Map<String,String>> cache = new HashMap<String,Map<String,String>>();

	@Override
	public String translation(String value,String bflanguage,String aflanguage) {
		if(bflanguage.equals(aflanguage)) return value;
		SolrTranslationSend solr = new SolrTranslationSend();
		try {
			Map<String,String> map = new HashMap<String,String>();
			if(cache.get(value)!=null) {
				if(cache.get(value).get(aflanguage)!=null) {
					return cache.get(value).get(aflanguage);
				}
				map = cache.get(value);
			}
			String ret= solr.translation(solrInfo, value, bflanguage,aflanguage);
			map.put(aflanguage, ret);
			cache.put(value, map);
			
			return ret;
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return "";
	}
	
	@Override
	public Map<String,Object> translationMap(Map<String,Object> value,String bflanguage,String aflanguage) {
		if(bflanguage.equals(aflanguage)) return value;
		Map<String,Object> ret = new HashMap<String,Object>();
		for(Map.Entry<String, Object> a:value.entrySet()) {
			if(a.getValue().getClass().equals(String.class)) {
				ret.put(a.getKey(), translation(a.getValue().toString(), bflanguage,aflanguage));
			} else {
				ret.put(a.getKey(), translationObject(a.getValue(), bflanguage,aflanguage));
			}
		}
		return ret;

	}
	
	@Override	
	public Object translationObject(Object oj,String bflanguage,String aflanguage) {
		ReturnInfo res =new ReturnInfo();
		try {
			if(oj instanceof List) {
				List<Object> ret = (List)oj.getClass().newInstance();
				for(Object a:(List)oj) {
					ret.add(translationObject(a, bflanguage,aflanguage));
				}
				return ret;
				
			}else {
			
			
				if(bflanguage.equals(aflanguage)) return oj;
				
				List<String>  names = TranslationNameList.nameList.get(oj.getClass());
				
				if(names == null) return oj;
	
				Map<String,Object> agmap = ObjectUtil.getPropertyNamesAndValuesByList(oj, names);
				
				Object ao =translationMap(agmap, bflanguage,aflanguage);
				
				ObjectUtil.setPropertys((Map<String, Object>)ao, oj);
				
				return oj;
			}
		}
		catch(Exception e) {
			//关闭流程
			e.printStackTrace();
			res.setSuccess(false);
			res.setMessage(e.getMessage());
			return null;
		}
	}



	
}