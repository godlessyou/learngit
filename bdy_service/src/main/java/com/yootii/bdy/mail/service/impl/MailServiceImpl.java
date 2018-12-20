package com.yootii.bdy.mail.service.impl;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Map;
import java.net.URLEncoder;

import javax.annotation.Resource;



import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;



import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.mail.service.MailService;
import com.yootii.bdy.util.GraspUtil;
import com.yootii.bdy.util.JsonUtil;
import com.yootii.bdy.util.ServiceUrlConfig;


@Service
public class MailServiceImpl implements MailService {

	
	private final Logger logger = Logger.getLogger(this.getClass());
	
	@Resource	
	private ServiceUrlConfig serviceUrlConfig;

	public ReturnInfo sendMail(String mailType,Map<String,Object> mailMap) {
		// 返回结果对象
		ReturnInfo rtnInfo = new ReturnInfo();
		try {
			String runMapUrl = "";
			for(Map.Entry<String, Object> run :mailMap.entrySet()) {
				Object value=run.getValue();
				String key=run.getKey();
				if (value!=null){
	//				runMapUrl = runMapUrl +"&"+key+"="+URLEncoder.encode(value.toString());
					runMapUrl = runMapUrl +"&"+key+"="+value.toString();
				}
			}
			
			runMapUrl = runMapUrl.replaceAll(" ","%20");  
			
//			runMapUrl = runMapUrl.replaceAll("\\+","%20");  

			String url=serviceUrlConfig.getMailServiceUrl()+"/mail/sendmail?mailType="+ mailType+runMapUrl;
			logger.info(url);
			String jsonString;

			jsonString = GraspUtil.getText(url);
			rtnInfo=JsonUtil.toObject(jsonString, ReturnInfo.class);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage(e.getMessage());
			rtnInfo.setMessageType(-1);
		} 
		return rtnInfo;
	}
	
	public static void main(String[] args) {
		String runMapUrl = "http://localhost:8080/bdy_automail/interface/mail/sendmail?mailType=sbaj_create_en&toPerson=联系人1&tokenID=15253157562881&subject=Notice of filing a case&caseId=284&bdyUrl=http://localhost:8000/bdy/bdy/case_xq.html?caseId=284&userId=3&toAddress=taikang@ipshine.com&customerName=泰康保险集团股份有限公司&caseType=商标注册";
	
		String runMapUrl1 = runMapUrl.replaceAll(" ","%20"); 
		

		System.out.println(runMapUrl1);
		String runMapUrl2 = runMapUrl.replaceAll("\\+","%20");  
		
		System.out.println(runMapUrl2);
	}
	
}
