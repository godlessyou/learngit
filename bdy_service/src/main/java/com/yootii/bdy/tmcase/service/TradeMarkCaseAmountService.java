package com.yootii.bdy.tmcase.service;

import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.tmcase.model.TradeMarkCase;

public interface TradeMarkCaseAmountService {

	ReturnInfo queryTmCaseByCustomer(TradeMarkCase tradeMarkCase, GeneralCondition gcon, String interfacetype);

	ReturnInfo queryTmCaseAmountByAgencyUser(TradeMarkCase tradeMarkCase, GeneralCondition gcon, String interfacetype,Integer userId);

	ReturnInfo queryTmCaseAmountByAgency(TradeMarkCase tradeMarkCase, GeneralCondition gcon, String interfacetype);

	ReturnInfo queryTmCaseAmountByPalteform(TradeMarkCase tradeMarkCase, GeneralCondition gcon, String interfacetype);

	ReturnInfo statstmCasetop10(GeneralCondition gcon);

}
