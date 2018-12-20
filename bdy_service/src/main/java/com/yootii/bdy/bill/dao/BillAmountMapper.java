package com.yootii.bdy.bill.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yootii.bdy.bill.model.ReturnBillAmount;
import com.yootii.bdy.common.GeneralCondition;

public interface BillAmountMapper {


	List<ReturnBillAmount> selectStatsByCustomer(@Param("custId")Integer custId, @Param("gcon")GeneralCondition gcon, @Param("interfacetype")String interfacetype);

	List<ReturnBillAmount> selectStatsByAgencyUser(@Param("userId")Integer userId, @Param("agencyId")Integer agencyId, @Param("gcon")GeneralCondition gcon,
			@Param("interfacetype")String interfacetype);

	List<ReturnBillAmount> selectStatsByAgency(@Param("agencyId")Integer agencyId, @Param("gcon")GeneralCondition gcon, @Param("interfacetype")String interfacetype);

	List<ReturnBillAmount> statsBillTop10(@Param("gcon")GeneralCondition gcon);

}
