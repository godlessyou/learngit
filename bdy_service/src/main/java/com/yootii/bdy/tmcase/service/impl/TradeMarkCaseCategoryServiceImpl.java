package com.yootii.bdy.tmcase.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.tmcase.dao.TradeMarkCaseCategoryMapper;
import com.yootii.bdy.tmcase.dao.TradeMarkCasePreMapper;
import com.yootii.bdy.tmcase.model.TradeMarkCaseCategory;
import com.yootii.bdy.tmcase.model.TradeMarkCasePre;
import com.yootii.bdy.tmcase.service.TradeMarkCaseCategoryService;

@Service
public class TradeMarkCaseCategoryServiceImpl implements TradeMarkCaseCategoryService{
	
	@Resource
	private TradeMarkCaseCategoryMapper tradeMarkCaseCategoryMapper;
	@Resource
	private TradeMarkCasePreMapper tradeMarkCasePreMapper;
	
	@Override
	public ReturnInfo createTradeMarkCaseCategory(TradeMarkCaseCategory good) {
		ReturnInfo info = new ReturnInfo();
		/*Integer custId = good.getCustId();
		Integer agencyId = good.getAgencyId();
		Integer casePreId = good.getCasePreId();//创建商品时，只是绑定预立案的ID,正式案件的caseId在创建正式案件时绑定
		Integer caseId = good.getCaseId();
		//如果前端传入caseId,则不需要插入
		if(custId==null||agencyId==null){
			info.setSuccess(false);
			info.setMessage("客户ID、代理所ID不能为空");
			return info ;
		}
		if(casePreId==null&&caseId==null){
			//往tmcase数据库插入一条
			try{
				tradeMarkCasePreMapper.deleteByCustIdAndAgencyId(custId, agencyId);//如果有相同custId和agencyId的数据，则删除
				TradeMarkCasePre tradeMarkCasePre = new TradeMarkCasePre();
				tradeMarkCasePre.setCustId(custId);
				tradeMarkCasePre.setAgencyId(agencyId);
				tradeMarkCasePre.setCaseType("商标注册");
				tradeMarkCasePreMapper.insertSelective(tradeMarkCasePre);
				casePreId = tradeMarkCasePre.getId();
				good.setCasePreId(casePreId);
			}catch(Exception e){
				e.printStackTrace();
				info.setSuccess(false);
				return info;
			}
		}
		if(casePreId!=null){
			good.setCaseId(null);//预立案时不设置商品的caseId
		}
		tradeMarkCaseCategoryMapper.deleteByCustIdAndAgencyId(custId,agencyId,casePreId);//删除预立案ID小于tradeMarkCasePre.getId()，且caseId为空的数据删除
*/		
		tradeMarkCaseCategoryMapper.insertSelective(good);
		info.setSuccess(true);
		Map<String, Integer> data = new HashMap<String, Integer>();
		data.put("goodId", good.getId());
//		data.put("casePreId", casePreId);
		info.setData(data);//刚插入的记录，自增长的id已存在
		info.setMessage("操作成功");
		return info;
	}

	@Override
	public ReturnInfo modifyTradeMarkCaseCategory(TradeMarkCaseCategory good) {
		ReturnInfo info = new ReturnInfo();
		if(good.getId()==null){
			info.setSuccess(false);
			info.setMessage("商品或服务ID不能为空");
			return info ;
		}
		tradeMarkCaseCategoryMapper.updateByPrimaryKeySelective(good);
		info.setSuccess(true);
		info.setMessage("操作成功");
		return info;
	}

	@Override
	public ReturnInfo queryTradeMarkCaseCategoryById(Integer id) {
		ReturnInfo info = new ReturnInfo();
		if(id==null){
			info.setSuccess(false);
			info.setMessage("商品或服务Id不能为空");
			return info;
		}
		TradeMarkCaseCategory good = tradeMarkCaseCategoryMapper.selectByPrimaryKey(id);
		info.setSuccess(true);
		info.setData(good);
		info.setMessage("查询成功");
		return info;
	}

	@Override
	public ReturnInfo deleteTradeMarkCaseCategoryById(Integer id) {
		ReturnInfo info = new ReturnInfo();
		tradeMarkCaseCategoryMapper.deleteByPrimaryKey(id);
		info.setSuccess(true);
		info.setMessage("删除成功");
		return info;
	}

	@Override
	public ReturnInfo queryTradeMarkCaseCategoryList(TradeMarkCaseCategory good) {
		ReturnInfo info = new ReturnInfo();
		List<TradeMarkCaseCategory> goods = tradeMarkCaseCategoryMapper.selectByTradeMarkCaseCategory(good);
		info.setSuccess(true);
		info.setData(goods);
		info.setMessage("查询成功");
		return info;
	}

	@Override
	public ReturnInfo checkTradeMarkCaseCategoryList(List<TradeMarkCaseCategory> goods) {
		ReturnInfo info = new ReturnInfo();
		List<TradeMarkCaseCategory> standardGoods = new ArrayList<TradeMarkCaseCategory>();//标准商品
		List<TradeMarkCaseCategory> nonStandardGoods = new ArrayList<TradeMarkCaseCategory>();//不标准商品
		String message = "";
		if(goods!=null&&goods.size()>0){
			for(int i=0;i<goods.size();i++){
				TradeMarkCaseCategory good = goods.get(i);
				if(good.getGoodCode()==null&&good.getGoodName()==null){
					continue;
				}
				TradeMarkCaseCategory tmcaseCategory = tradeMarkCaseCategoryMapper.selectCaseCategoryDetailByGood(good);
				if(tmcaseCategory!=null){
					standardGoods.add(tmcaseCategory);
				}else {
					if(i==(goods.size()-1)){
						if(good.getGoodNameEn()!=null){
							message = message+good.getGoodNameEn();
						}else{
							message = message+good.getGoodName();
						}
					}else{
						if(good.getGoodNameEn()!=null){
							message = message+good.getGoodNameEn()+"; ";
						}else{
							message = message+good.getGoodName()+"; ";
						}
					}
					good.setGoodKey(null);
					nonStandardGoods.add(good);
				}
			}
		}
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("standard", standardGoods);
		data.put("nonStandard", nonStandardGoods);
		info.setSuccess(true);
		info.setData(data);
		if("".equals(message)){
			message=null;
		}
		info.setMessage(message);
		return info;
	}

}
