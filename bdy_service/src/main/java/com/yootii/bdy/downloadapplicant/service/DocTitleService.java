package com.yootii.bdy.downloadapplicant.service;

import java.util.Map;

import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.downloadapplicant.model.DocTitle;
import com.yootii.bdy.downloadapplicant.model.DocTitleWithBLOBs;
import com.yootii.bdy.security.model.Token;

public interface DocTitleService {
	public ReturnInfo createDocTitle(DocTitleWithBLOBs docTitle,Token token);
	public ReturnInfo modifyDocTitle(DocTitleWithBLOBs docTitle,Token token);
	public ReturnInfo queryDocTitle(DocTitleWithBLOBs docTitle,Token token);
	public ReturnInfo deleteDocTitle(Integer docTitleId,Token token);
	public ReturnInfo modifyDocTitleChecked(DocTitle docTitle);
}
