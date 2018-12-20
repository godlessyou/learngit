package com.yootii.bdy.remind.service;

import java.util.List;

import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.ReturnInfo;

public interface TrademarkRemindService {

	public ReturnInfo selectTrademarkXuzhan(Integer agencyId,Integer custId,Boolean justcount,GeneralCondition gcon);

	public ReturnInfo SendTrademarkXuzhan(Integer custId,Integer userId) throws Exception;

	public ReturnInfo selectTrademarkChushenGG(Integer agencyId,Integer custId,Boolean justcount,GeneralCondition gcon);

	public ReturnInfo SendTrademarkChushenGG(Integer custId) throws Exception;
	
	public ReturnInfo selectTrademarkSongdaGG(Integer agencyId,Integer custId,Boolean justcount,GeneralCondition gcon);

	public ReturnInfo SendTrademarkSongdaGG(Integer custId) throws Exception;
	
	public ReturnInfo selectTrademarkDongtai(Integer agencyId,Integer custId,Boolean justcount,GeneralCondition gcon);

	public ReturnInfo SendTrademarkDongtai(Integer custId) throws Exception;
	
	
}