package com.yootii.bdy.tmcase.model;


import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yootii.bdy.bill.model.ChargeRecord;
import com.yootii.bdy.material.model.Material;

public class TradeMarkCaseSolr extends TradeMarkCase {
	
	private String agencyName;

	public String getAgencyName() {
		return agencyName;
	}

	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
	}
}