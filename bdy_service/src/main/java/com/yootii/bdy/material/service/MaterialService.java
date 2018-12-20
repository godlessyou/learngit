package com.yootii.bdy.material.service;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.material.model.Material;
import com.yootii.bdy.security.model.Token;
import com.yootii.bdy.tmcase.model.TradeMarkCase;
public interface MaterialService {

	/**
	 * 
	 * author:dengwq
	 * time: 2018年4月3日 下午3:51:43 
	 * @param material
	 * @param gcon
	 * @return ReturnInfo
	 */
	ReturnInfo createMaterial(HttpServletRequest request,Material material, GeneralCondition gcon,Token token);

	/**
	 * 
	 * author:dengwq
	 * time: 2018年4月3日 下午3:54:30 
	 * @param material,根据里面的materialId,做删除操作
	 * @return ReturnInfo
	 */
	ReturnInfo deleteMaterial(Material material,GeneralCondition gcon,Token token);
	/**
	 * 
	 * author:dengwq
	 * time: 2018年4月3日 下午4:05:21 
	 * @param material
	 * @param gcon
	 * @return
	 */

	ReturnInfo findMaterial(Material material,GeneralCondition gcon,Token token);
    /**
     * 
     * author:dengwq
     * time: 2018年4月3日 下午4:15:21 
     * @param material
     * @param gcon
     * @return
     */
	Object updateMaterial(Material material,GeneralCondition gcon,Token token);
	/**
	 * 下载文件
	 * author:dengwq
	 * time: 2018年4月3日 下午4:22:16 
	 * @param request
	 * @param response
	 * @param material
	 * @param gcon
	 * @return
	 */
	
	ReturnInfo modifyMaterial(HttpServletRequest request,Material material, GeneralCondition gcon,Token token);
	/**
	 * 下载文件
	 * author:dengwq
	 * time: 2018年4月3日 下午4:22:16 
	 * @param request
	 * @param response
	 * @param material
	 * @param gcon
	 * @return
	 */

	Object downloadFile(HttpServletRequest request, 
			HttpServletResponse response,Material material,GeneralCondition gcon,Token token);
	/**
	 * 
	 * author:
	 * time: 2018年4月3日 下午4:26:42 
	 * @param material
	 * @param request
	 * @param gcon
	 * @return
	 */

	ReturnInfo uploadFile( HttpServletRequest request,Material material,GeneralCondition gcon,Token token);
	/**
	 * 
	 * author:
	 * time: 2018年4月3日 下午4:28:38 
	 * @param request
	 * @param material
	 * @return
	 */
	ReturnInfo deleteFile(HttpServletRequest request, Material material,GeneralCondition gcon,Token token);

	ReturnInfo queryMaterialByApp(Material material, GeneralCondition gcon, Token token);
	/**
	 * @param precase 
	 * @param token 
	 * @throws Exception 
	 * 
	 */
	ReturnInfo createMaterialSimple(Material material, Integer agencyId, GeneralCondition gcon, boolean precase, Token token) throws Exception;
	/**
	 * @param tmId 
	 * 
	 */
	ReturnInfo queryMaterialByTmId(Material material, GeneralCondition gcon, Integer tmId);

	ReturnInfo queryMaterialByRegNumber(Material material, GeneralCondition gcon, String regNumber);

	ReturnInfo queryMaterialByAppId(Material material, GeneralCondition gcon);

	ReturnInfo queryMaterialByAppName(String appCnName, String appEnName, GeneralCondition gcon, String tokenID) throws Exception;

	ReturnInfo queryMaterialByCaseIdLast(Integer caseId);
	
	

	public ReturnInfo checkFile(Material material,int fileName);
	
	
	public void createMaterialByCase(TradeMarkCase tmCase, GeneralCondition gcon) throws Exception;
	
}
