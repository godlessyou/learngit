package com.yootii.bdy.remind.service.Impl;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.customer.service.CustomerService;
import com.yootii.bdy.mail.MailSenderInfo;
import com.yootii.bdy.mail.SendMailUtil;
import com.yootii.bdy.remind.model.TrademarkDongtai;
import com.yootii.bdy.remind.model.TrademarkGonggao;
import com.yootii.bdy.remind.model.TrademarkXuzhan;
import com.yootii.bdy.remind.service.RemindService;
import com.yootii.bdy.remind.service.TrademarkRemindService;
import com.yootii.bdy.user.service.UserService;


@Service
public class TrademarkRemindServiceImpl implements TrademarkRemindService {

	@Override
	public ReturnInfo selectTrademarkXuzhan(Integer agencyId, Integer custId, Boolean justcount,
			GeneralCondition gcon) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public ReturnInfo SendTrademarkXuzhan(Integer custId, Integer userId) throws Exception {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public ReturnInfo selectTrademarkChushenGG(Integer agencyId, Integer custId, Boolean justcount,
			GeneralCondition gcon) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public ReturnInfo SendTrademarkChushenGG(Integer custId) throws Exception {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public ReturnInfo selectTrademarkSongdaGG(Integer agencyId, Integer custId, Boolean justcount,
			GeneralCondition gcon) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public ReturnInfo SendTrademarkSongdaGG(Integer custId) throws Exception {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public ReturnInfo selectTrademarkDongtai(Integer agencyId, Integer custId, Boolean justcount,
			GeneralCondition gcon) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public ReturnInfo SendTrademarkDongtai(Integer custId) throws Exception {
		// TODO 自动生成的方法存根
		return null;
	}


	

	
}