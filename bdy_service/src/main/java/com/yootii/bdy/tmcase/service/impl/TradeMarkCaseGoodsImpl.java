package com.yootii.bdy.tmcase.service.impl;

import java.util.List;


import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.yootii.bdy.common.ReturnInfo;

import com.yootii.bdy.tmcase.dao.GoodsMapper;
import com.yootii.bdy.tmcase.dao.GoodsPlanMapper;
import com.yootii.bdy.tmcase.dao.TradeMarkCaseCategoryMapper;
import com.yootii.bdy.tmcase.model.Goods;
import com.yootii.bdy.tmcase.model.GoodsPlan;
import com.yootii.bdy.tmcase.model.TradeMarkCase;
import com.yootii.bdy.tmcase.model.TradeMarkCaseCategory;



@Component
public class TradeMarkCaseGoodsImpl {
	
	@Resource
	private GoodsMapper goodsMapper;

	@Resource
	private GoodsPlanMapper goodsPlanMapper;	
	
	@Resource
	private TradeMarkCaseCategoryMapper tradeMarkCaseCategoryMapper;

	
	
	public ReturnInfo addGoodsPlan(GoodsPlan goodsPlan) {
		ReturnInfo info = new ReturnInfo();		
		String appName = goodsPlan.getAppName();		
		if(appName==null||appName.equals("")){
			info.setSuccess(false);
			info.setMessage("申请人名称不能为空");
			return info;
		}
		
//		String planName = goodsPlan.getPlanName();	
//		if(planName==null||planName.equals("")){
//			info.setSuccess(false);
//			info.setMessage("方案名称不能为空");
//			return info;
//		}		
		
		try{
			
			goodsPlanMapper.insertSelective(goodsPlan);
			
			int planId=goodsPlan.getPlanId();
			
			String planName = goodsPlan.getPlanName();	
			if(planName==null||planName.equals("")){
				
				planName="plan_"+planId;
				goodsPlan.setPlanName(planName);
				goodsPlanMapper.updateByPrimaryKeySelective(goodsPlan);
			}
			
			
			List<Goods> goodsList = goodsPlan.getGoods();
			if(goodsList!=null&&goodsList.size()>0){
				for(Goods goods : goodsList){
					goods.setPlanId(planId);
					if(goods.getGoodKey()==null){
						TradeMarkCaseCategory goodTmp = selectGoodKey(goods.getGoodName(), goods.getGoodClass());
						if(goodTmp!=null){
							goods.setGoodKey(goodTmp.getGoodKey());
							goods.setSimilarGroup(goodTmp.getSimilarGroup());
							goods.setGoodCode(goodTmp.getGoodCode());
						}
					}
					String goodClass = goods.getGoodClass();
					if(goodClass !=null && goodClass!=""){
						goodsMapper.insertSelective(goods);
					}
				}
			}			
			
		}catch(Exception e){
			info.setSuccess(false);
			info.setMessage(e.getMessage());
			info.setMessageType(-1);
			return info;
		}
		
		info.setSuccess(true);
		return info;
	}
	
	
	
	public ReturnInfo modifyGoodsPlan(GoodsPlan goodsPlan) {
		ReturnInfo info = new ReturnInfo();
		Integer planId=goodsPlan.getPlanId();
		
		if(planId==null||planId.equals("")){
			info.setSuccess(false);
			info.setMessage("planId不能为空");
			return info;
		}
		
		
		try{
			
			
			List<Goods> goodsList = goodsPlan.getGoods();
			if(goodsList!=null && goodsList.size()>0){				
				goodsMapper.deleteByPrimaryKey(planId);
				for(Goods good : goodsList){				
					good.setPlanId(planId);
					goodsMapper.insertSelective(good);			
				}
			}
			
			goodsPlanMapper.updateByPrimaryKeySelective(goodsPlan);
			
			
		}catch(Exception e){
			info.setSuccess(false);
			info.setMessage(e.getMessage());
			info.setMessageType(-1);
			return info;
		}
		
		info.setSuccess(true);
		return info;
	}
	
	
	
	
	public ReturnInfo deleteGoodsPlan(GoodsPlan goodsPlan) {
		ReturnInfo info = new ReturnInfo();
		
		Integer planId=goodsPlan.getPlanId();
		if(planId==null||planId.equals("")){
			info.setSuccess(false);
			info.setMessage("planId不能为空");
			return info;
		}
		try{			
			
			goodsMapper.deleteByPrimaryKey(planId);
			
			goodsPlanMapper.deleteByPrimaryKey(planId);
			
		}catch(Exception e){
			info.setSuccess(false);
			info.setMessage(e.getMessage());
			info.setMessageType(-1);
			return info;
		}
		
		info.setSuccess(true);
		return info;
	}


		

	
	
	public ReturnInfo queryGoodsPlan(GoodsPlan goodsPlan) {
		ReturnInfo info = new ReturnInfo();
		Integer planId=goodsPlan.getPlanId();
		String planName=goodsPlan.getPlanName();
		String appName=goodsPlan.getAppName();
		
		if(planId==null && planName==null && appName==null){
			info.setSuccess(false);
			info.setMessage("planId/planName/appName不能都为空");
			return info;
		}
		
		try{
			
			List<GoodsPlan> list=goodsPlanMapper.selectByGoodsPlan(goodsPlan);
			long total=(long)0;
			if (list!=null){
				int size=list.size();
				total=(long)size;
			}
			info.setData(list);
			info.setTotal(total);
			info.setSuccess(true);
			
		}catch(Exception e){
			info.setSuccess(false);
			info.setMessage(e.getMessage());
			info.setMessageType(-1);
			return info;
		}
		
		info.setSuccess(true);
		return info;
	}
	
	
	
	public ReturnInfo queryGoods(GoodsPlan goodsPlan) {
		ReturnInfo info = new ReturnInfo();
		Integer planId=goodsPlan.getPlanId();
	
		if(planId==null ){
			info.setSuccess(false);
			info.setMessage("planId不能都为空");
			return info;
		}
		
		try{
			
			List<Goods> list=goodsMapper.selectByPlanId(planId);
			long total=(long)0;
			if (list!=null){
				int size=list.size();
				total=(long)size;
			}
			info.setData(list);
			info.setTotal(total);
			info.setSuccess(true);
			
		}catch(Exception e){
			info.setSuccess(false);
			info.setMessage(e.getMessage());
			info.setMessageType(-1);
			return info;
		}
		
		info.setSuccess(true);
		return info;
	}

	
	
	
	public void addGoods(TradeMarkCase tradeMarkCase){
		List<TradeMarkCaseCategory> goods = tradeMarkCase.getGoods();
		if(goods!=null&&goods.size()>0){
			for(TradeMarkCaseCategory good : goods){
				good.setCaseId(tradeMarkCase.getId());
				if(good.getGoodKey()==null){
					TradeMarkCaseCategory goodTmp = selectGoodKey(good.getGoodName(), good.getGoodClass());
					if(goodTmp!=null){
						good.setGoodKey(goodTmp.getGoodKey());
						good.setSimilarGroup(goodTmp.getSimilarGroup());
						good.setGoodCode(goodTmp.getGoodCode());
					}
				}
				String goodClass = good.getGoodClass();
				if(goodClass !=null && goodClass !=""){
					tradeMarkCaseCategoryMapper.insertSelective(good);
				}
			}
		}
	}
	
	
	public TradeMarkCaseCategory selectGoodKey(String goodName,String goodClass){
		TradeMarkCaseCategory good = tradeMarkCaseCategoryMapper.selectGoodKeyByGoodNameAndClass(goodName,goodClass);
		return good;
	}
	
	
	public void updateGoods(TradeMarkCase tradeMarkCase){
		List<TradeMarkCaseCategory> goods = tradeMarkCase.getGoods();
		if(goods!=null && goods.size()>0){
			Integer caseId=tradeMarkCase.getId();
			tradeMarkCaseCategoryMapper.deleteByCaseId(caseId);
			for(TradeMarkCaseCategory good : goods){				
				good.setCaseId(caseId);
				String goodClass = good.getGoodClass();
				if(goodClass !=null && goodClass!=""){
					tradeMarkCaseCategoryMapper.insertSelective(good);			
				}
			}
		}
	}
	
	
	
}
