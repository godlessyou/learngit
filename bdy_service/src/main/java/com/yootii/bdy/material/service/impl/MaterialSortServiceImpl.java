package com.yootii.bdy.material.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.material.dao.MaterialSortMapper;
import com.yootii.bdy.material.model.MaterialSort;
import com.yootii.bdy.material.service.MaterialSortService;
@Service
public class MaterialSortServiceImpl implements MaterialSortService{
	
	@Resource
	private MaterialSortMapper materialSortMapper;
	/**
	 * 暂时按level分为 0 1 2 3 ,4个等级
	 */
	@Override
	public ReturnInfo queryMaterialSort(MaterialSort materialSort, GeneralCondition gcon) {
		ReturnInfo rinfo = new ReturnInfo();
		
		/*if(materialSort.getLevel() ==null) {
			materialSort.setLevel(0);
		}*/
		Integer level=materialSort.getLevel();
		if (level==null){
			materialSort.setLevel(0);
		}
		List<MaterialSort>  materialSorts=	materialSortMapper.selectByMaterialSort(materialSort,gcon);
		if(materialSorts != null && materialSorts.size()>0) {
			for(MaterialSort  ms1 : materialSorts) {
				List<MaterialSort>  m1= materialSortMapper.selectChildMaterialSort(ms1,gcon);
				if(m1 != null && m1.size()>0) {
					ms1.setNextMaterialSort(m1);					
					for(MaterialSort  ms2 : m1) {
						List<MaterialSort>  m2= materialSortMapper.selectChildMaterialSort(ms2,gcon);						
						if(m2 != null && m2.size()>0) {						
							ms2.setNextMaterialSort(m2);
							for(MaterialSort  ms3 : m2) {
								List<MaterialSort>  m3= materialSortMapper.selectChildMaterialSort(ms3,gcon);
								if (m3!=null && m3.size()>0){
									ms3.setNextMaterialSort(m3);
								}
							}
							
							
						}
						
					}
				}
				
			}
		}
		
		rinfo.setData(materialSorts);
		rinfo.setTotal(Long.parseLong(String.valueOf(materialSorts.size())));
		rinfo.setSuccess(true);
		rinfo.setMessage("查询资料类型成功");
		return rinfo;
	}
	@Override
	public ReturnInfo createMaterialSort(MaterialSort materialSort, GeneralCondition gcon) {
		ReturnInfo rinfo = new ReturnInfo();
		if(materialSort.getLevel() ==null) {
			materialSort.setLevel(0);
		}
		
		materialSortMapper.insertSelective(materialSort);
		
		rinfo.setSuccess(true);
		rinfo.setMessage("增加资料类型成功");
		return rinfo;
	}
	@Override
	public ReturnInfo deleteMaterialSort(MaterialSort materialSort, GeneralCondition gcon) {
		ReturnInfo rinfo = new ReturnInfo();
		System.out.println(materialSort);
		materialSortMapper.deleteByPrimaryKey(materialSort.getId());
		
		rinfo.setSuccess(true);
		rinfo.setMessage("删除资料类型成功");
		return rinfo;
	}
	@Override
	public ReturnInfo modifyMaterialSort(MaterialSort materialSort, GeneralCondition gcon) {
		ReturnInfo rinfo = new ReturnInfo();
		if(materialSort.getLevel() ==null) {
			materialSort.setLevel(0);
		}
		
		materialSortMapper.updateByPrimaryKeySelective(materialSort);
		
		rinfo.setSuccess(true);
		rinfo.setMessage("修改资料类型成功");
		return rinfo;
	}
}
