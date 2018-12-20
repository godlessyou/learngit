package com.yootii.bdy.remind.service.Impl;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.Globals;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.remind.model.Remind;
import com.yootii.bdy.remind.service.RemindService;
import com.yootii.bdy.security.model.Token;
import com.yootii.bdy.util.GraspUtil;
import com.yootii.bdy.util.JsonUtil;
import com.yootii.bdy.util.ServiceUrlConfig;


@Service
public class RemindServiceImpl implements RemindService {

	@Resource	
	private ServiceUrlConfig serviceUrlConfig;
	
	@Override
	public ReturnInfo deleteRemind(Integer remindId) {
		ReturnInfo rtnInfo = new ReturnInfo();
		try {
			Token token = Globals.getToken();
			
			String url=serviceUrlConfig.getBdysysmUrl()+"/remind/deletetaskremind?tokenID="+ 
					token.getTokenID()+"&remindId="+remindId.toString();
			String jsonString = GraspUtil.getText(url);
			rtnInfo=JsonUtil.toObject(jsonString, ReturnInfo.class);
		} catch (Exception e) {
			e.printStackTrace();
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage(e.getMessage());
			rtnInfo.setMessageType(-1);
		} 
		return rtnInfo;
	}	

	@Override
	public ReturnInfo insertRemindByType(Integer type,Date date,Integer agencyId,Integer custId,Integer caseId,GeneralCondition gcon) {
		ReturnInfo rtnInfo = new ReturnInfo();
		try {
			Token token = Globals.getToken();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			String url=serviceUrlConfig.getBdysysmUrl()+"/remind/addremind?tokenID="+ token.getTokenID()+
					"&date="+formatter.format(date)+"&type="+type.toString()+"&agencyId="+((agencyId==null)?"":agencyId.toString())+
					"&custId="+((custId==null)?"":custId.toString())+"&caseId="+((caseId==null)?"":caseId.toString());
			String jsonString;

			jsonString = GraspUtil.getText(url);
			rtnInfo=JsonUtil.toObject(jsonString, ReturnInfo.class);

		} catch (Exception e) {
			e.printStackTrace();
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage(e.getMessage());
			rtnInfo.setMessageType(-1);
		} 
		return rtnInfo;
	}

	@Override
	public ReturnInfo selectRemindList(Remind remind, GeneralCondition gcon) {
		ReturnInfo returnInfo = new ReturnInfo();
		try{
			//Modification start, 2018-10-31
			//to resolve BDY-694， modify  parameter tokenId to tokenID		
			
//			Token token = Globals.getToken();
//			String tokenID=token.getTokenID();
			String tokenID= gcon.getTokenID();
			Integer custId = remind.getCustid();
			Integer caseId = remind.getCaseId();
			String url = serviceUrlConfig.getBdysysmUrl()+"/remind/queryremindlist?tokenID="+ tokenID +
					"&custid="+((custId==null)?"":custId)
					+"&caseId="+((caseId==null)?"":caseId);;
//			System.out.println(url);		
			//Modification end		

			String jsonString = GraspUtil.getText(url);
			if(jsonString!=null){
				returnInfo = JsonUtil.toObject(jsonString,ReturnInfo.class);
			}
		}catch (Exception e) {
			e.printStackTrace();
			returnInfo.setSuccess(false);
			returnInfo.setMessage(e.getMessage());
			returnInfo.setMessageType(-1);
		}
		return returnInfo;
	}

	
	
}