package com.yootii.bdy.downloadapplicant.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.downloadapplicant.dao.DocTitleMapper;
import com.yootii.bdy.downloadapplicant.model.DocTitle;
import com.yootii.bdy.downloadapplicant.model.DocTitleWithBLOBs;
import com.yootii.bdy.downloadapplicant.service.DocTitleService;
import com.yootii.bdy.model.User;
import com.yootii.bdy.security.model.Token;
@Service
public class DocTitleServiceImpl implements DocTitleService {
	@Resource
	private DocTitleMapper docTitleMapper;
	@Override
	public ReturnInfo createDocTitle(DocTitleWithBLOBs docTitle,Token token) {
		ReturnInfo info = new ReturnInfo();
		if(docTitle.getDocTypeId()==null){
			info.setSuccess(false);
			info.setMessage("请选择文书类型");
			return info;
		}
		if(docTitle.getOrderNum()==null){
			info.setSuccess(false);
			info.setMessage("标题序号不能为空");
			return info;
		}
		if(docTitle.getLevel()==null){
			info.setSuccess(false);
			info.setMessage("标题级别不能为空");
			return info;
		}
		if(isAdminRole(token)){//管理员
			docTitle.setIsBase(1);//是基础模板
		}else {
			docTitle.setIsBase(3);//是代理人模板
			docTitle.setUserId(token.getUserID());//代理人的模板
		}
		//序号的改变
		docTitleMapper.updateOrderNumByIns(docTitle);
				
		docTitleMapper.insertSelective(docTitle);
		info.setSuccess(true);
		info.setMessage("创建成功");
		return info;
	}
	@Override
	public ReturnInfo modifyDocTitle(DocTitleWithBLOBs docTitle,Token token) {
		ReturnInfo info = new ReturnInfo();
		Integer titleId = docTitle.getTitleId();
		if(titleId==null){
			info.setSuccess(false);
			info.setMessage("文书标题ID不能为空");
			return info;
		}
		DocTitleWithBLOBs docTitleDb = docTitleMapper.selectByPrimaryKey(titleId);
		if(docTitleDb!=null){
			Integer isBase = docTitleDb.getIsBase();
			if(isBase==1&&isAdminRole(token)){//基础模板只有管理员可以修改
				docTitleMapper.updateByPrimaryKeySelective(docTitle);
			}else if(isBase==0) {
				docTitleMapper.updateByPrimaryKeySelective(docTitle);
			}
		}
		info.setSuccess(true);
		info.setMessage("修改成功");
		return info;
	}
	@Override
	public ReturnInfo queryDocTitle(DocTitleWithBLOBs docTitle,
			Token token) {
		ReturnInfo info = new ReturnInfo();
		Integer docTypeId = docTitle.getDocTypeId();
		if(docTypeId==null){
			info.setSuccess(false);
			info.setMessage("文书类型ID不能为空");
			return info;
		}
		List<DocTitleWithBLOBs> data = null;
		if(isAdminRole(token)){//管理员
			List<DocTitleWithBLOBs> level1 = docTitleMapper.selectByDocType(docTypeId,null,1);
			data = adminGetTitle(level1,docTypeId, null);
		}else{
			Integer userId = token.getUserID();
			List<DocTitleWithBLOBs> level1 = docTitleMapper.selectByUserIdAndDocType(userId,docTypeId,null,1);
			if(level1==null||level1.size()==0){
				//向该用户增加基础的模板
				insertBaseToUser(null, null, docTypeId, userId);
				level1 = docTitleMapper.selectByUserIdAndDocType(userId,docTypeId,null,1);
			}
			data = agentGetTitle(level1,userId,docTypeId, null);
		}
		info.setSuccess(true);
		info.setMessage("查询成功");
		info.setData(data);
		return info;
	}
	private void insertBaseToUser(Integer baseParentId,Integer parentId,Integer docTypeId,Integer userId){
		List<DocTitleWithBLOBs> title1;
		if(baseParentId==null){
			title1 = docTitleMapper.selectByDocType(docTypeId,null,1);
		}else {
			title1 = docTitleMapper.selectByParentId(baseParentId);
		}
		if(title1!=null&&title1.size()>0){
			for(DocTitleWithBLOBs tit1:title1){
				baseParentId = tit1.getTitleId();
				tit1.setIsBase(2);
				tit1.setUserId(userId);
				tit1.setTitleId(null);
				tit1.setParentId(parentId);
				System.out.println(baseParentId);
				docTitleMapper.insertSelective(tit1);
				Integer parentId2 = tit1.getTitleId();
				insertBaseToUser(baseParentId,parentId2, docTypeId, userId);
			}
		}
	}
	
	/**
	 * 利用递归的方式查询出数据
	 * @param level1 第1级标题
	 * @param docTypeId 文书类型
	 * @param titleId 标题id
	 * @return
	 */
	private List<DocTitleWithBLOBs> adminGetTitle(List<DocTitleWithBLOBs> level1,Integer docTypeId,Integer titleId){
		if(level1!=null&&level1.size()>0){
			for(DocTitleWithBLOBs docTitle:level1){
				titleId = docTitle.getTitleId();
				Integer level = docTitle.getLevel()+1;
				List<DocTitleWithBLOBs> children = docTitleMapper.selectByDocType(docTypeId,titleId,level);
				docTitle.setChildren(children);
				adminGetTitle(children, docTypeId ,titleId);
			}
		}
		return level1;
	}
	private List<DocTitleWithBLOBs> agentGetTitle(List<DocTitleWithBLOBs> level1,Integer userId,Integer docTypeId,Integer titleId){
		if(level1!=null&&level1.size()>0){
			for(DocTitleWithBLOBs docTitle:level1){
				titleId = docTitle.getTitleId();
				Integer level = docTitle.getLevel()+1;
				List<DocTitleWithBLOBs> children = docTitleMapper.selectByUserIdAndDocType(userId,docTypeId,titleId,level);
				docTitle.setChildren(children);
				agentGetTitle(children,userId,docTypeId ,titleId);
			}
		}
		return level1;
	}
	@Override
	public ReturnInfo deleteDocTitle(Integer titleId,Token token) {
		ReturnInfo info = new ReturnInfo();
		if(titleId==null){
			info.setSuccess(false);
			info.setMessage("文书标题ID不能为空");
			return info;
		}
		DocTitleWithBLOBs docTitleDb = docTitleMapper.selectByPrimaryKey(titleId);
		if(docTitleDb!=null){
			Integer isBase = docTitleDb.getIsBase();
			if(isBase==1&&isAdminRole(token)){//基础模板只有管理员可以删除
				//级联删除
				delDocTitle(titleId);
				//序号的改变
				docTitleMapper.updateOrderNumByDel(docTitleDb);
			}else if(isBase==0) {
				delDocTitle(titleId);
				//序号的改变
				docTitleMapper.updateOrderNumByDel(docTitleDb);
			}
		}
		info.setSuccess(true);
		info.setMessage("删除成功");
		return info;
	}
	private void delDocTitle(Integer titleId){
		docTitleMapper.deleteByPrimaryKey(titleId);
		List<DocTitleWithBLOBs> titles = docTitleMapper.selectByParentId(titleId);
		if(titles!=null&&titles.size()>0){
			for(DocTitleWithBLOBs title: titles){
				titleId = title.getTitleId();
				delDocTitle(titleId);
			}
		}
	}
	private boolean isAdminRole(Token token){
		boolean hasPermission = false;
		if(token.isUser()){
			Integer userId = token.getUserID();
			List<String> roles = docTitleMapper.checkRole(userId);
			if(roles!=null&&roles.size()>0){
				for(String r: roles){
					if("代理机构管理员".equals(r)){
						hasPermission = true;
					}
				}
			}
		}
		return hasPermission;
	}
	@Override
	public ReturnInfo modifyDocTitleChecked(DocTitle docTitle) {
		ReturnInfo info = new ReturnInfo();
		List<DocTitleWithBLOBs> docTitleWithBLOBs = docTitle.getChildren();
		if(docTitleWithBLOBs==null||docTitleWithBLOBs.size()==0){
			info.setSuccess(false);
			info.setMessage("文书标题是否选中不能为空");
			return info;
		}
		for(DocTitleWithBLOBs dt:docTitleWithBLOBs){
			docTitleMapper.modifyCheckedByTitleIds(dt.getChecked(),dt.getTitleId());
		}
		info.setSuccess(true);
		info.setMessage("更新成功");
		return info;
	}
}
