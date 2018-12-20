package com.yootii.bdy.activemq;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.yootii.bdy.agency.dao.AgencyMapper;
import com.yootii.bdy.agency.model.Agency;
import com.yootii.bdy.tmcase.model.TradeMarkCase;
import com.yootii.bdy.tmcase.model.Trademarkws;
import com.yootii.bdy.util.JsonUtil;
import com.yootii.bdy.util.ObjectUtil;

import org.springframework.jms.connection.CachingConnectionFactory;
import org.apache.activemq.ActiveMQConnectionFactory;

@Service
public class ProducerServiceImpl implements ProducerService {
	@Resource(name = "jmsTemplate")
	private JmsTemplate jmsTemplate;

	@Resource(name = "jmsTemplate2")
	private JmsTemplate jmsTemplate2;

	@Resource(name = "sendDestination")
	private Destination destination;
	
	@Resource
	private AgencyMapper agencyMapper;
	
	private static Logger logger = LoggerFactory
			.getLogger(ProducerServiceImpl.class);

	@Override
	public Boolean sendMapMessage(final TradeMarkCase tradeMarkCase,
			final Integer caseProId, Integer agencyId) {
		Boolean ret = true;
		try {
			
			Agency agency=agencyMapper.selectByPrimaryKey(agencyId);
			Integer appChannel=agency.getAppChannel();
			
			//网申通道
			if (appChannel == null || (appChannel != 1 && appChannel != 2)) {
				appChannel = 1;
			}

			if (appChannel == 1) {//万慧达

				jmsTemplate.send(destination, new MessageCreator() {

					@Override
					public Message createMessage(Session session)
							throws JMSException {
						Trademarkws tradeMarkws = changews(tradeMarkCase);
						
						String changeType=tradeMarkws.getChangeType();
//						logger.info("22222222222222222 changeType"+ changeType);
						String data = JsonUtil.toJson(tradeMarkws);
						
						

						Map<String, Object> ret = new HashMap<String, Object>();
						ret.put("id", caseProId);
						ret.put("type", tradeMarkCase.getCaseTypeId());
						ret.put("length", data.length());
						ret.put("data", tradeMarkws);
						
						String msg=JsonUtil.toJson(ret);
						logger.info("wang shen send msg: "+ msg);
						TextMessage message = session
								.createTextMessage(msg);
						return message;
					}
				});

			} else if (appChannel == 2) {//曜斗
				/*
				 * ActiveMQConnectionFactory targetConnectionFactory=new
				 * ActiveMQConnectionFactory();
				 * targetConnectionFactory.setBrokerURL
				 * ("tcp://192.168.0.4:61616");
				 * targetConnectionFactory.setUserName("admin");
				 * targetConnectionFactory.setPassword("admin");
				 * 
				 * CachingConnectionFactory cachingConnectionFactory=new
				 * CachingConnectionFactory();
				 * cachingConnectionFactory.setSessionCacheSize(100);
				 * cachingConnectionFactory
				 * .setTargetConnectionFactory(targetConnectionFactory);
				 * jmsTemplate.setConnectionFactory(cachingConnectionFactory);
				 * jmsTemplate.send(destination, new MessageCreator() {
				 */

				jmsTemplate2.send(destination, new MessageCreator() {

					@Override
					public Message createMessage(Session session)
							throws JMSException {
						Trademarkws tradeMarkws = changews(tradeMarkCase);
						String data = JsonUtil.toJson(tradeMarkws);

						Map<String, Object> ret = new HashMap<String, Object>();
						ret.put("id", caseProId);
						ret.put("type", tradeMarkCase.getCaseTypeId());
						ret.put("length", data.length());
						ret.put("data", tradeMarkws);
						TextMessage message = session
								.createTextMessage(JsonUtil.toJson(ret));
						return message;
					}
				});

			}
		} catch (Exception e) {
			ret = false;
		}
		return ret;
	}

	Trademarkws changews(TradeMarkCase tradeMarkCase) {
		Trademarkws tradeMarkws = new Trademarkws();

		String data = JsonUtil.toJson(tradeMarkCase);
		Map<String, Object> map = JsonUtil.toObject(data, Map.class);
		ObjectUtil.setPropertys(map, tradeMarkws);
		return tradeMarkws;
	}

}
