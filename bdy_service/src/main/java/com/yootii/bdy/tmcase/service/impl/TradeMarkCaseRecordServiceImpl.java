package com.yootii.bdy.tmcase.service.impl;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;








import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.tmcase.dao.TradeMarkCaseColRecordMapper;
import com.yootii.bdy.tmcase.dao.TradeMarkCaseDescMapper;
import com.yootii.bdy.tmcase.dao.TradeMarkCaseRecordMapper;
import com.yootii.bdy.tmcase.model.TradeMarkCaseCategory;
import com.yootii.bdy.tmcase.model.TradeMarkCaseColRecord;
import com.yootii.bdy.tmcase.model.TradeMarkCaseDesc;
import com.yootii.bdy.tmcase.model.TradeMarkCaseJoinApp;
import com.yootii.bdy.tmcase.model.TradeMarkCaseRecord;
import com.yootii.bdy.tmcase.service.TradeMarkCaseRecordService;
import com.yootii.bdy.util.JsonDateUtil;
import com.yootii.bdy.util.ObjectUtil;


@Service
public class TradeMarkCaseRecordServiceImpl implements TradeMarkCaseRecordService{

	@Resource
	private TradeMarkCaseDescMapper tradeMarkCaseDescMapper;
	
	@Resource
	private TradeMarkCaseRecordMapper tradeMarkCaseRecordMapper;
		
	
	@Resource
	private TradeMarkCaseColRecordMapper tradeMarkCaseColRecordMapper;

	@Override
	public ReturnInfo queryTradeMarkCaseRecordList(TradeMarkCaseRecord tradeMarkCaseRecord) {
		ReturnInfo info = new ReturnInfo();
				
		
		List<TradeMarkCaseRecord> tmcases = tradeMarkCaseRecordMapper.selecTmCaseRecordList(tradeMarkCaseRecord);
		
		TradeMarkCaseColRecord tradeMarkCaseColRecord=new TradeMarkCaseColRecord();
		
		tradeMarkCaseColRecord.setId(tradeMarkCaseRecord.getId());
		tradeMarkCaseColRecord.setRecordId(tradeMarkCaseRecord.getRecordId());
			
		
		//获取修改的字段
		List<TradeMarkCaseColRecord> modifiedColList=tradeMarkCaseColRecordMapper.selecTmCaseColRecordList(tradeMarkCaseColRecord);
		
		
		List<TradeMarkCaseDesc> descList=tradeMarkCaseDescMapper.selectTradeMarkCaseDescList();
		
		List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
		for (TradeMarkCaseColRecord colRecord: modifiedColList){
			Integer recordId=colRecord.getRecordId();
			String modifiedCol=colRecord.getModifiedCol();
			if(modifiedCol.length()==0){
				continue;
			}
			ArrayList<String> propertyNames = new ArrayList<String>(Arrays.asList(modifiedCol.split(",")));			
			
			for (TradeMarkCaseRecord oldTMCR: tmcases){	
				Integer recordId2=oldTMCR.getRecordId();
				Date modifyTime=oldTMCR.getModifyTime();
				String modifyUserName=oldTMCR.getModifyUserName();
								
				//找到具备同一个recordId的记录
				if (recordId.intValue()==recordId2.intValue()){
					
					TradeMarkCaseRecord oldDataRecord=new TradeMarkCaseRecord();
					//返回的对象，应该只包含被修改的字段的属性
					ObjectUtil.setObjectProperty(oldTMCR, oldDataRecord, propertyNames);
					oldDataRecord.setGoods(oldTMCR.getGoods());
					oldDataRecord.setJoinApps(oldTMCR.getJoinApps());	
					
					TradeMarkCaseRecord newDataRecord=new TradeMarkCaseRecord();
					for (TradeMarkCaseRecord newTMCR: tmcases){	
						Date modifyTime2=newTMCR.getModifyTime();
						Integer recordId3=newTMCR.getRecordId();
						
						//对于修改时间相同但recordId不同的记录，指的是修改后的记录。						
						if (modifyTime.getTime()==modifyTime2.getTime() && recordId2.intValue()!=recordId3.intValue()){
							//返回结果应该也是只包含被修改的字段
							ObjectUtil.setObjectProperty(newTMCR, newDataRecord, propertyNames);
							newDataRecord.setGoods(newTMCR.getGoods());
							newDataRecord.setJoinApps(newTMCR.getJoinApps());
							break;
						}
					}
						
					
					//构造返回结果
					Map<String, Object> propertyMap=new HashMap<String,  Object>();
					for(String name:propertyNames){	
						Map<String, Object> map=new HashMap<String,  Object>();
						Object oldValue=null;						
						Object newValue=null;
						if (oldDataRecord!=null){
							oldValue=ObjectUtil.getPropertyValue(oldDataRecord, name);
						}
						if (newDataRecord!=null){
							newValue=ObjectUtil.getPropertyValue(newDataRecord, name);
						}
						if (oldValue!=null || newValue!=null){							
							String rtnName=name;
							
							if (name.equals("goods")){
								List<TradeMarkCaseCategory> oldList=(List<TradeMarkCaseCategory> )oldValue;
								List<Map<String, Object>> tccList=new ArrayList<Map<String,  Object>>();
								for(TradeMarkCaseCategory tcc:oldList){
									Map<String, Object> tccMap=new HashMap<String,  Object>();	
									setProperty(tcc, "goodName", descList, tccMap);
									setProperty(tcc, "goodNameEn", descList, tccMap);
									setProperty(tcc, "goodCode", descList, tccMap);
									tccList.add(tccMap);
								}
								
								List<TradeMarkCaseCategory> newList=(List<TradeMarkCaseCategory> )newValue;
								List<Map<String, Object>> newTccList=new ArrayList<Map<String,  Object>>();
								for(TradeMarkCaseCategory tcc:newList){
									Map<String, Object> tccMap=new HashMap<String,  Object>();	
									setProperty(tcc, "goodName", descList, tccMap);
									setProperty(tcc, "goodNameEn", descList, tccMap);
									setProperty(tcc, "goodCode", descList, tccMap);
									newTccList.add(tccMap);
								}
								for(TradeMarkCaseDesc desc:descList){
									String colName=desc.getCol();
									if (colName.equals(name)){
										rtnName=desc.getCnName();
										break;
									}
								}
								map.put("old", tccList);
								map.put("new", newTccList);
								propertyMap.put(rtnName, map);
							}
							else if (name.equals("joinApps")){
								List<TradeMarkCaseJoinApp> oldList=(List<TradeMarkCaseJoinApp> )oldValue;
								List<Map<String, Object>> tcjaList=new ArrayList<Map<String,  Object>>();
								for(TradeMarkCaseJoinApp tcc:oldList){
									Map<String, Object> tccMap=new HashMap<String,  Object>();										
									setProperty(tcc, "nameCn", descList, tccMap);
									setProperty(tcc, "nameEn", descList, tccMap);
									tcjaList.add(tccMap);
								}
								List<TradeMarkCaseJoinApp> newList=(List<TradeMarkCaseJoinApp> )newValue;
								List<Map<String, Object>> newTcjaList=new ArrayList<Map<String,  Object>>();
								for(TradeMarkCaseJoinApp tcc:newList){
									Map<String, Object> tccMap=new HashMap<String,  Object>();										
									setProperty(tcc, "nameCn", descList, tccMap);
									setProperty(tcc, "nameEn", descList, tccMap);
									newTcjaList.add(tccMap);
								}
								for(TradeMarkCaseDesc desc:descList){
									String colName=desc.getCol();
									if (colName.equals(name)){
										rtnName=desc.getCnName();
										break;
									}
								}
								map.put("old", tcjaList);
								map.put("new", newTcjaList);
								propertyMap.put(rtnName, map);
							}
							else{
								map.put("old", oldValue);
								map.put("new", newValue);								
								for(TradeMarkCaseDesc desc:descList){
									String colName=desc.getCol();
									if (colName.equals(name)){
										rtnName=desc.getCnName();
										break;
									}
								}
								propertyMap.put(rtnName, map);
							}							
								
						}
					}		
					
					String jsonTime=JsonDateUtil.dateFormat(modifyTime);
					propertyMap.put("modifyTime", jsonTime);
					propertyMap.put("modifyUserName", modifyUserName);
					list.add(propertyMap);
					break;
					
				}
				
			}				
			
		}
		
		
		Long total = 0L;
		
		if(list!=null){
			total= (long)list.size();
		}
		
		info.setTotal(total);		
		info.setData(list);
		info.setSuccess(true);		
		return info;
		
	}

	
	private void setProperty(Object bean, String filedName, List<TradeMarkCaseDesc>descList, Map<String, Object> map){
		Object obj=ObjectUtil.getPropertyValue(bean, filedName);
		String key=filedName;
		for(TradeMarkCaseDesc desc:descList){
			String colName=desc.getCol();
			if (colName.equals(filedName)){
				key=desc.getCnName();
				break;
			}
		}
		map.put(key, obj);
	}
	
	
	
	@Override
	public ReturnInfo queryTradeMarkCaseRecordDetail(Integer id) {
		ReturnInfo info = new ReturnInfo();
		TradeMarkCaseRecord tmcase = tradeMarkCaseRecordMapper.selectByPrimaryKey(id);
		info.setSuccess(true);
		info.setData(tmcase);
		return info;
	}

	
	
}
