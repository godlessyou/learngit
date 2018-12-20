package com.yootii.bdy.downloadapplicant.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yootii.bdy.agency.service.AgencyService;
import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.downloadapplicant.dao.DocTypeMapper;
import com.yootii.bdy.downloadapplicant.model.DocType;
import com.yootii.bdy.downloadapplicant.service.DocTypeService;
import com.yootii.bdy.security.model.Token;
@Service
public class DocTypeServiceImpl implements DocTypeService{
	@Resource
	private DocTypeMapper docTypeMapper;
	@Resource
	private AgencyService agencyService;
	@Override
	public ReturnInfo createDocType(DocType docType,Token token) {
		ReturnInfo info = new ReturnInfo();
		String type = docType.getDocTypeName();
		if(type==null||"".equals(type)){
			info.setSuccess(false);
			info.setMessage("文档名称不能为空");
			return info;
		}
		info = checkRole(token);
		if(!info.getSuccess()){
			return info;
		}
		Integer userId = token.getUserID();
		Integer agencyId = agencyService.getAgentIdByUserId(userId);
		docType.setAgencyId(agencyId);
		
		docTypeMapper.insertSelective(docType);
		info.setSuccess(true);
		info.setMessage("添加成功");
		return info;
	}

	@Override
	public ReturnInfo deleteDocType(Integer docTypeId,Token token) {
		ReturnInfo info = new ReturnInfo();
		if(docTypeId==null){
			info.setSuccess(false);
			info.setMessage("文档类型ID不能为空");
			return info;
		}
		info = checkRole(token);
		if(!info.getSuccess()){
			return info;
		}
		docTypeMapper.deleteByPrimaryKey(docTypeId);
		info.setSuccess(true);
		info.setMessage("删除成功");
		return info;
	}

	@Override
	public ReturnInfo queryDocType(DocType docType,GeneralCondition gcon,Token token) {
		ReturnInfo info = new ReturnInfo();
		Integer userId = token.getUserID();
		Integer agencyId = agencyService.getAgentIdByUserId(userId);
		docType.setAgencyId(agencyId);
		List<DocType> docTypes = docTypeMapper.selectByDocType(docType, gcon);
		int count = docTypeMapper.selectByDocTypeCount(docType, gcon);
		info.setSuccess(true);
		info.setData(docTypes);
		info.setTotal((long)count);
		info.setCurrPage(gcon.getPageNo());
		info.setMessage("查询成功");
		return info;
	}
	private ReturnInfo checkRole(Token token){
		ReturnInfo info = new ReturnInfo();
		boolean hasPermission = false;
		if(token.isUser()){
			Integer userId = token.getUserID();
			List<String> roles = docTypeMapper.checkRole(userId);
			if(roles!=null&&roles.size()>0){
				for(String r: roles){
					if("代理机构管理员".equals(r)){
						hasPermission = true;
					}
				}
			}
		}
		if(!hasPermission){
			info.setSuccess(false);
			info.setMessage("权限不足");
			return info;	
		}
		info.setSuccess(true);
		return info;
	}
}
