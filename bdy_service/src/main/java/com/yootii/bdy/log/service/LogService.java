package com.yootii.bdy.log.service;

import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.log.model.AuditLogEntity;

public interface LogService {

	ReturnInfo queryLog(GeneralCondition gcon, AuditLogEntity log);

	ReturnInfo queryLogByTable(GeneralCondition gcon, AuditLogEntity log);

	

}
