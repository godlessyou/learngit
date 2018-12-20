package com.yootii.bdy.activemq;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yootii.bdy.material.dao.MaterialMapper;
import com.yootii.bdy.material.dao.MaterialSortMapper;
import com.yootii.bdy.material.model.Material;
import com.yootii.bdy.material.model.MaterialSort;
import com.yootii.bdy.task.service.TradeMarkCaseTaskService;
import com.yootii.bdy.tmcase.dao.TradeMarkCaseMapper;
import com.yootii.bdy.tmcase.model.TradeMarkCase;
import com.yootii.bdy.tmcase.model.TradeMarkCaseFile;
import com.yootii.bdy.tmcase.model.TradeMarkCaseProcess;
import com.yootii.bdy.tmcase.service.TradeMarkCaseFileService;
import com.yootii.bdy.tmcase.service.TradeMarkCaseProcessService;
import com.yootii.bdy.tmcase.service.TradeMarkCaseService;
import com.yootii.bdy.util.TaskTool;

public class ConsumerMessageListener implements MessageListener {
	@Resource
	private TradeMarkCaseProcessService tradeMarkCaseProcessService;
	@Resource
	private TradeMarkCaseFileService tradeMarkCaseFileService;
	@Resource
	private TradeMarkCaseService tradeMarkCaseService;
	@Resource
	private TradeMarkCaseTaskService tradeMarkCaseTaskService;
	
	@Resource
	private TradeMarkCaseMapper tradeMarkCaseMapper;
	
	@Resource
	private MaterialMapper materialMapper;
	
	@Resource
	private MaterialSortMapper materialSortMapper;
//	@Resource
//	private MessageProcessor tmInfo;
//	@Resource
//	private ProducerService producerService;
//	@Resource
//	private FileService download;
	private static Logger logger = LoggerFactory.getLogger(ConsumerMessageListener.class);
	@Override
	public void onMessage(Message msg) {
		
		logger.info("onMessage start ");
		
		MapMessage textMsg = (MapMessage)msg;
		try {
			
			Integer MQKind = textMsg.getInt("MQKind");
			Integer Processid = textMsg.getInt("Id");

			String Success = "";
			String Message = "";
			String File = "";
			String ApplNO = "";
			String ApplDate = "";
			String Type = "";
			String id ="";
			
			Success = textMsg.getString("Success");
			Message = textMsg.getString("Message");
			
			logger.info("Success: "+Success);
			
			if (Success!=null && Success.equals("false")){	
				logger.info("Message: "+ Message);
			}
			
			
			TradeMarkCase tmcase =(TradeMarkCase)tradeMarkCaseService.queryTradeMarkCaseByProcessId(Processid).getData();
			if(tmcase  == null) throw new Exception("Process instance id is wrong, id: "+ Processid);
			TradeMarkCaseProcess tradeMarkCaseProcess = new TradeMarkCaseProcess();
			tradeMarkCaseProcess.setCaseId(tmcase.getId());
			tradeMarkCaseProcess.setUsername("系统");
			tradeMarkCaseProcess.setUserId(0);
			tradeMarkCaseProcess.setCustId(0);
			tradeMarkCaseProcess.setLevel(1);
			
			logger.info("MQKind: "+MQKind);
			
			switch(MQKind) {
			case 5:
				
			
								
				if(Success.equals("true")) {
					tradeMarkCaseProcess.setStatus("网申待支付");
					tradeMarkCaseProcess.setSubmitStatus("待支付");
					tradeMarkCaseProcessService.createTradeMarkCaseProcess(tradeMarkCaseProcess);
				} else {

				
					
					tradeMarkCaseProcess.setStatus("网申提交失败");
					tradeMarkCaseProcess.setSubmitStatus("提交失败");
					tradeMarkCaseProcess.setFailReason(Message); 
					
					tradeMarkCaseProcessService.createTradeMarkCaseProcess(tradeMarkCaseProcess);
										
					logger.info("notifyAppResult start");					
					tradeMarkCaseTaskService.notifyAppResult(null, "2", Processid.toString());
					
					logger.info("notifyAppResult end");
				}
				break;
			case 6:							
				
				Type = textMsg.getString("Type");
//				Message = textMsg.getString("Message");
				Integer submitStatus = 2;	
				
				logger.info(" Type=" + Type);
								
				if(Type.equals("1")) {
					tradeMarkCaseProcess.setStatus("网申支付成功");
					tradeMarkCaseProcess.setSubmitStatus("支付成功");
					
					logger.info("createTradeMarkCaseProcess start ");
					tradeMarkCaseProcessService.createTradeMarkCaseProcess(tradeMarkCaseProcess);
					
					
					submitStatus = 1;
				} else if(Type.equals("2")){
					tradeMarkCaseProcess.setStatus("网申申请作废");
					tradeMarkCaseProcess.setSubmitStatus("申请作废");				
					tradeMarkCaseProcessService.createTradeMarkCaseProcess(tradeMarkCaseProcess);				
					submitStatus = 2;
				}
				
				logger.info("notifyAppResult start");
				tradeMarkCaseTaskService.notifyAppResult(null, submitStatus.toString(), Processid.toString());
				
				logger.info("notifyAppResult end");
				break;
			case 7:
							
				File = textMsg.getString("File");
				ApplNO = textMsg.getString("ApplNO");
				ApplDate = textMsg.getString("ApplDate");
				
				logger.info(" File=" + File +" ApplNO=" + ApplNO + " ApplDate="+ApplDate);

				Integer caseId = tmcase.getId();	
				
				Integer caseTypeId=tmcase.getCaseTypeId();
				
				Integer custId=tmcase.getCustId();
				
				Integer fileName=null;
				
				if (caseTypeId!=null){
					fileName=TaskTool.getFileNameByCaseType(caseTypeId.intValue());
				}else{
					logger.info("caseTypeId is null, caseId is " + caseId );

				}
				
				TradeMarkCase tradeMarkCase = new TradeMarkCase();
				tradeMarkCase.setId(caseId);
			    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");  
			    Date date = sdf.parse(ApplDate);  
				tradeMarkCase.setAppDate(date);
				tradeMarkCase.setAppNumber(ApplNO);
				
//				tradeMarkCaseService.modifyTradeMarkCase(tradeMarkCase );		
				
				tradeMarkCaseMapper.updateByPrimaryKeySelective(tradeMarkCase);
				

							
				/*TradeMarkCaseFile tmcaseFile = new TradeMarkCaseFile();
				
				tmcaseFile.setCaseId(Caseid);
				tmcaseFile.setFileUrl(File);
				tmcaseFile.setFileName(201);
				tmcaseFile.setFileType("递交官方");
				
				tradeMarkCaseFileService.insertCaseFileData(Caseid, tmcaseFile);*/
				
				Material  material =new Material();
				material.setCaseId(caseId);
				material.setCustId(custId);
				material.setAddress(File);
				material.setFileName(fileName);
				material.setSubject("递交官方");
				material.setTitle("申请书");
				material.setCreater("系统");
				material.setModifier("系统");
				material.setVersionNo(0);
				
				if(fileName != null) {
					MaterialSort materialSort =new MaterialSort();		
					materialSort.setFileName(fileName);
					List<MaterialSort> sort = materialSortMapper.selectByMaterialSort(materialSort, null);
					material.setSortId(sort.get(0).getId());					
				}
				
//				m.setSubject("fileName : "+201+","+"递交官方");
				materialMapper.insertSelective(material); 
				break;
			}
			
			
			
//			ResultMsg resultMsg = new ResultMsg();
//			System.out.println("内容:"+textMsg.getText());
//			String json=textMsg.getText();
//			//检查json格式是否正确
//			try{
//				JSONObject obj = new JSONObject().fromObject(json);//将json字符串转换为json对象
//			}catch (Exception e) {
//				//出现异常，则json格式有误
//				resultMsg.setMQKind(5);
//				System.out.println(json.substring(json.indexOf("id")+6,json.indexOf("type")-5));
//				resultMsg.setWPMid(Integer.parseInt(json.substring(json.indexOf("id")+6,json.indexOf("type")-5)));
//				resultMsg.setSuccess(false);
//				resultMsg.setErroCode(1);
//				resultMsg.setMessage("提交的数据格式有误，请联系开发人员！");
//				producerService.sendMapMessage(resultMsg);
//				logger.debug("提交的数据格式有误，请联系开发人员！");
//				return;
//			}
//			//判断json数据是否丢失
//			if(!tmInfo.isLost(textMsg.getText())){
//				Trademark tm = tmInfo.parseJson(textMsg.getText());
//				OnlineRecord record = new OnlineRecord();
//				record.setRefno(tm.getAgentNum());
//				record.setElemid(tmInfo.getId(textMsg.getText()));
//				record.setAgent(tm.getAgentPerson());
//				record.setJson(textMsg.getText());
//				record.setMsgRecvDate(new Date());
//				record.setSubSuccess(-1);//表示未提交
//				record.setIsPay(-1);//表示未支付
//				record.setIsDownload(-1);//表示未下载
//				//如果数据库中存在该文号则更新
//				if(recordDao.getRecordByRefno(tm.getAgentNum())!=null){
//					System.out.println("文号已存在，更新数据库。。。");
//					recordDao.updateRecordByRefno(record);
//				}else{
//					//如果不存在则插入
//					System.out.println("插入数据库。。。");
//					recordDao.addRecord(record);
//				}
////				download.download(tm);
//			}else{
//				resultMsg.setMQKind(5);
//				resultMsg.setWPMid(tmInfo.getId(textMsg.getText()));
//				resultMsg.setSuccess(false);
//				resultMsg.setErroCode(1);
//				resultMsg.setMessage("数据部分丢失，请重新提交");
//				producerService.sendMapMessage(resultMsg);
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
}
