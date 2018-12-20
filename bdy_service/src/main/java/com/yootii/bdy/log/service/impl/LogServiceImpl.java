package com.yootii.bdy.log.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.log.model.AuditLogEntity;
import com.yootii.bdy.log.service.LogService;
import com.yootii.bdy.solr.SolrInfo;
import com.yootii.bdy.solr.SolrLog;

@Service
public class LogServiceImpl implements LogService{
	
	@Override
	public ReturnInfo queryLog(GeneralCondition gcon, AuditLogEntity log) {
		ReturnInfo rtnInfo = new ReturnInfo();
		SolrLog  solLog =new SolrLog();
		SolrInfo solrInfo =new SolrInfo();
		try {
			rtnInfo = solLog.selectLog(gcon, log, solrInfo, rtnInfo);
		} catch (Exception e) {
			
			e.printStackTrace();
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage(e.getMessage());
			rtnInfo.setMessageType(-1);
		}
			
				
		
		return rtnInfo;
	}

	@Override
	public ReturnInfo queryLogByTable(GeneralCondition gcon, AuditLogEntity log) {
		ReturnInfo rtnInfo = new ReturnInfo();
		SolrLog  solLog =new SolrLog();
		SolrInfo solrInfo =new SolrInfo();
		try {
			rtnInfo = solLog.selectLogByTable(gcon, log, solrInfo, rtnInfo);
		} catch (Exception e) {
			
			e.printStackTrace();
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage(e.getMessage());
			rtnInfo.setMessageType(-1);
		}
			
				
		
		return rtnInfo;
	}

}
