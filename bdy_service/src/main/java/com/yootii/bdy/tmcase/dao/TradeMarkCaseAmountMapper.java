package com.yootii.bdy.tmcase.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.tmcase.model.RetuenTmCaseAmount;
import com.yootii.bdy.tmcase.model.ReturnTmCaseAmountYear;
import com.yootii.bdy.tmcase.model.TradeMarkCase;

public interface TradeMarkCaseAmountMapper {

	List<RetuenTmCaseAmount> selectTmCaseByCustomer(@Param("tradeMarkCase")TradeMarkCase tradeMarkCase, @Param("gcon")GeneralCondition gcon,@Param("interfacetype")String interfacetype);

	List<RetuenTmCaseAmount> selectTmCaseByAgencyUser(@Param("tradeMarkCase")TradeMarkCase tradeMarkCase, @Param("gcon")GeneralCondition gcon,
			@Param("interfacetype")String interfacetype, @Param("userId")Integer userId);

	List<RetuenTmCaseAmount> queryTmCaseAmountByAgency(@Param("tradeMarkCase")TradeMarkCase tradeMarkCase, @Param("gcon")GeneralCondition gcon,
			@Param("interfacetype")String interfacetype);

	List<RetuenTmCaseAmount> queryTmCaseAmountByPalteform(@Param("tradeMarkCase")TradeMarkCase tradeMarkCase, @Param("gcon")GeneralCondition gcon,
			@Param("interfacetype")String interfacetype);

	List<ReturnTmCaseAmountYear> selectTmCaseByCustomerYear(@Param("tradeMarkCase")TradeMarkCase tradeMarkCase, @Param("gcon")GeneralCondition gcon,
			@Param("interfacetype")String interfacetype);

	List<Map<String, String>> statstmCasetop10(@Param("gcon")GeneralCondition gcon);
	

	
	
}
