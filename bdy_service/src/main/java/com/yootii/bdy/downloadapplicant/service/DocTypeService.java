package com.yootii.bdy.downloadapplicant.service;

import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.downloadapplicant.model.DocType;
import com.yootii.bdy.security.model.Token;

public interface DocTypeService {
	public ReturnInfo createDocType(DocType docType,Token token);
	public ReturnInfo deleteDocType(Integer docTypeId,Token token);
	public ReturnInfo queryDocType(DocType docType,GeneralCondition gcon,Token token);
}
