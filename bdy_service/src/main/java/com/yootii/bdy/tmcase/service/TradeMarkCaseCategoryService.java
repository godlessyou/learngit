package com.yootii.bdy.tmcase.service;

import java.util.List;

import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.tmcase.model.TradeMarkCaseCategory;

public interface TradeMarkCaseCategoryService {
	public ReturnInfo createTradeMarkCaseCategory(TradeMarkCaseCategory good); 
	public ReturnInfo modifyTradeMarkCaseCategory(TradeMarkCaseCategory good);
	public ReturnInfo queryTradeMarkCaseCategoryById(Integer id);
	public ReturnInfo deleteTradeMarkCaseCategoryById(Integer id);
	public ReturnInfo queryTradeMarkCaseCategoryList(TradeMarkCaseCategory good);
	public ReturnInfo checkTradeMarkCaseCategoryList(List<TradeMarkCaseCategory> goods);
}
