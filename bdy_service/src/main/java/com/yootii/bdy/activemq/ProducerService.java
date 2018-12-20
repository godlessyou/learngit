package com.yootii.bdy.activemq;


import org.springframework.stereotype.Service;

import com.yootii.bdy.tmcase.model.TradeMarkCase;



@Service
public interface ProducerService  {
	public Boolean sendMapMessage(TradeMarkCase tradeMarkCase,Integer caseProId, Integer agencyId) ;
}
