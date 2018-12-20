package com.yootii.bdy.task.service.Impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import javax.annotation.Resource;

import com.yootii.bdy.bill.dao.ChargeRecordMapper;
import com.yootii.bdy.bill.model.Bill;
import com.yootii.bdy.bill.model.ChargeItem;
import com.yootii.bdy.bill.model.ChargeRecord;
import com.yootii.bdy.bill.service.BillService;
import com.yootii.bdy.bill.service.ChargeItemService;
import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.ipservice.model.AgencyService;
import com.yootii.bdy.ipservice.service.AgencyServiceService;
import com.yootii.bdy.model.User;
import com.yootii.bdy.tmcase.dao.TradeMarkAssociationMapper;
import com.yootii.bdy.tmcase.dao.TradeMarkCaseMapper;
import com.yootii.bdy.tmcase.dao.TradeMarkCaseProcessMapper;
import com.yootii.bdy.tmcase.model.TradeMarkAssociation;
import com.yootii.bdy.tmcase.model.TradeMarkCase;
import com.yootii.bdy.tmcase.model.TradeMarkCaseCategory;
import com.yootii.bdy.tmcase.model.TradeMarkCaseProcess;
import com.yootii.bdy.user.service.UserService;
import com.yootii.bdy.util.GonUtil;
import com.yootii.bdy.util.ServiceUrlConfig;

import org.springframework.stereotype.Component;


@Component
public class CaseChargeRecordServiceImpl  {

	private final Logger logger = Logger.getLogger(this.getClass());

	@Resource
	private ServiceUrlConfig serviceUrlConfig;
	
	@Resource
	private UserService userService;

	@Resource
	private	ChargeItemService chargeItemService;
	
	@Resource
	private ChargeRecordMapper chargeRecordMapper;
	
	@Resource
	private TradeMarkCaseMapper tradeMarkCaseMapper;
	
	@Resource
	private TradeMarkAssociationMapper tradeMarkAssociationMapper;
	
	@Resource
	private AgencyServiceService agencyServiceService;
	
	@Resource
	private BillService billService;	
	
	@Resource
	private TaskCommonImpl taskCommonImpl;
	
	@Resource
	private TradeMarkCaseProcessMapper tradeMarkCaseProcessMapper;
	
	
	
	public List<ChargeRecord>  queryChargeRecord(GeneralCondition gcon,ChargeRecord chargeRecord, String userId) throws Exception {
		
		Integer uId=null;
		
		if (userId!=null && !userId.equals("")){
		 uId=new Integer(userId);
		}
		
		List<ChargeRecord> list=chargeRecordMapper.selectByChargeRecord(gcon, chargeRecord, uId, null);
		return list;	
	}
	
	
	//创建或者修改账单记录
	//如果该案件没有账单记录，那么创建账单记录
	//如果该案件已经有账单记录，那么修改账单记录
	public String createChargeRecord(GeneralCondition gcon, User user,TradeMarkCase tradeMarkCase, String agencyServiceId,  String userId ) throws Exception {
		
		String chargeRecordIds=null;
		
		String username=null;
		String fullName=null;
		if (user!=null){
			username=user.getUsername();
			fullName=user.getFullname();		
		}
		
		Integer caseTypeId=tradeMarkCase.getCaseTypeId();
		boolean register=false;
		//判断案件类型是否为商标注册
		if (caseTypeId!=null && caseTypeId.intValue()==1){
			register=true;
		}
		
		
		String goodClasses=tradeMarkCase.getGoodClasses();		
		if (goodClasses==null || goodClasses.equals("")){
			logger.info("can not create charge record, because of no good class.");
			return null;
		}
		
		int goodClassNumber=0;
		int toTal=0;
		
		List<TradeMarkCaseCategory> goods = null;
		
		if(register){		
			goods = tradeMarkCase.getGoods();		
			if(goods==null || goods.size()==0){
				logger.info("can not create charge record, because of no good code.");
				return null;
			}			
		}
			
		
		StringTokenizer idtok = new StringTokenizer(goodClasses, ";");
				
		while (idtok.hasMoreTokens()) {
			String gClass = idtok.nextToken();
			goodClassNumber++;
			//如果是商标注册类型的案件，那么需要计算商品服务的数量，如果超过10项，记录超过的总数。
			if (register){
				int goodCodeNumber=0;	
				if (goods!=null){
					for(TradeMarkCaseCategory good : goods){
						String goodClass=good.getGoodClass();
						String tGoodClass=goodClass;
						if (goodClass.startsWith("0")){
							tGoodClass=goodClass.substring(1);
						}
						if(gClass.equals(tGoodClass)){
							goodCodeNumber++;
						}					
					}
					if (goodCodeNumber>10){
						toTal=toTal+goodCodeNumber-10;
					}	
				}
			}
		}
		
		
		Integer agencyId=tradeMarkCase.getAgencyId();
		Integer caseId=tradeMarkCase.getId();
		
		ChargeRecord cr = new ChargeRecord();
		cr.setAgencyId(agencyId);
		cr.setCaseId(caseId);
		//查询是否已经有该案件的账单记录
		gcon.setOffset(0);
		gcon.setRows(10);
		List<ChargeRecord> chargeRecordList=queryChargeRecord(gcon, cr, userId);
		
		
		//如果agencyServiceId为null
		//需要从之前的账单记录中获取agencyServiceId
		if(agencyServiceId==null || agencyServiceId.equals("")){
			//ChargeRecord chargeRecord = chargeRecordMapper.selectByCaseId(caseId);	
			if (chargeRecordList!=null && chargeRecordList.size()>0){				
				for (ChargeRecord chargeRecord:chargeRecordList){	
					Integer aServiceId=chargeRecord.getAgencyServiceId();
					if (aServiceId!=null){
						agencyServiceId=aServiceId.toString();
						break;
					}			
				}
			}
			if (agencyServiceId==null || agencyServiceId.equals("")){
				logger.info("can not get agencyServiceId from charge record");
				return null;
			}
		}
				
		
		//获取该案件类型的价目表
		List<ChargeItem>  list=chargeItemService.queryChargeItemListById(gcon, agencyServiceId);
		
		if(list==null){
			logger.info("can not get ChargeItem, because agencyServiceId is wrong");
			return null;
		}
		
		
		
		double fee=0;
		boolean guanfei=false;
		boolean fuwufei=false;		
		Integer agencyServiceIdInt=new Integer(agencyServiceId);
		
		for(ChargeItem item: list){
			if (fuwufei && guanfei){
				break;
			}
			
			Integer chargeId=item.getChargeItemId();
//			String caseType=item.getCaseType();
			String chargeType=item.getChargeType();
			BigDecimal price=item.getPrice();
			
			String descChn=item.getChnName();
			String descEng=item.getEngName();
			
			double pirceD=price.doubleValue();
			
			if (chargeType!=null && chargeType.equals("官费") && !guanfei){//记录官费
				fee=goodClassNumber*pirceD+toTal*(pirceD/10);	
				ChargeRecord chargeRecord = new ChargeRecord();
				chargeRecord.setAgencyId(agencyId);
				chargeRecord.setCaseId(caseId);
				
				chargeRecord.setChargeItemId(chargeId);				
				chargeRecord.setChargeType(chargeType);
				chargeRecord.setDescChn(descChn);
				chargeRecord.setDescEng(descEng);	
				chargeRecord.setPrice(price);
				chargeRecord.setNumber(goodClassNumber);
				chargeRecord.setCreater(username);
				chargeRecord.setVerifyPerson(username);
				chargeRecord.setCreaterFullname(fullName);
				chargeRecord.setStatus(0);				
				chargeRecord.setAgencyServiceId(agencyServiceIdInt);
				
				BigDecimal amount=new BigDecimal(fee);
				chargeRecord.setAmount(amount);
				
				//更新账单记录
				if (chargeRecordList!=null && chargeRecordList.size()>0){
					for(ChargeRecord record:chargeRecordList){
						Integer chargeItemId=record.getChargeItemId();
						if (chargeId.intValue()==chargeItemId.intValue()){
							Integer chargeRecordId=record.getChargeRecordId();
							chargeRecord.setChargeRecordId(chargeRecordId);
							chargeRecordMapper.updateByPrimaryKeySelective(chargeRecord);
							break;
						}
					}
				}else{				
					chargeRecordMapper.insertSelective(chargeRecord);
					Integer chargeRecordId=chargeRecord.getChargeRecordId();
					if (chargeRecordId!=null){
						if (chargeRecordIds==null){
							chargeRecordIds=chargeRecordId.toString();
						}else{
							chargeRecordIds=chargeRecordIds+","+chargeRecordId.toString();
						}
					}
				}
				guanfei=true;
				
			}else if (chargeType!=null && chargeType.equals("服务费") && !fuwufei){//记录服务费	
							
				ChargeRecord chargeRecord = new ChargeRecord();
				chargeRecord.setAgencyId(agencyId);
				chargeRecord.setCaseId(caseId);
				chargeRecord.setChargeItemId(chargeId);	
				chargeRecord.setChargeType(chargeType);
				chargeRecord.setDescChn(descChn);
				chargeRecord.setDescEng(descEng);				
				chargeRecord.setPrice(price);
				chargeRecord.setNumber(1);
				chargeRecord.setAmount(price);
				chargeRecord.setCreater(username);
				chargeRecord.setVerifyPerson(username);
				chargeRecord.setCreaterFullname(fullName);
				chargeRecord.setStatus(0);
				chargeRecord.setAgencyServiceId(agencyServiceIdInt);
				
				if (chargeRecordList!=null && chargeRecordList.size()>0){
					for(ChargeRecord record:chargeRecordList){
						Integer chargeItemId=record.getChargeItemId();
						if (chargeId.intValue()==chargeItemId.intValue()){
							Integer chargeRecordId=record.getChargeRecordId();
							chargeRecord.setChargeRecordId(chargeRecordId);
							chargeRecordMapper.updateByPrimaryKeySelective(chargeRecord);
						}
					}
				}else{				
					chargeRecordMapper.insertSelective(chargeRecord);
					Integer chargeRecordId=chargeRecord.getChargeRecordId();
					if (chargeRecordId!=null){
						if (chargeRecordIds==null){
							chargeRecordIds=chargeRecordId.toString();
						}else{
							chargeRecordIds=chargeRecordIds+","+chargeRecordId.toString();
						}
					}
				}				
				
				fuwufei=true;
			}			
		
		}
		
		return chargeRecordIds;
		
	}
	
		
	
	
	
	//创建账单记录	
	public void createChargeRecords(GeneralCondition gcon, String userId, String caseId, String agencyId, String agencyServiceId) throws Exception{
		String tokenID=gcon.getTokenID();
		User user=new User();	
		if (userId!=null){
			user=userService.getUserById(userId, tokenID);
		}else{
			user=userService.queryAdminByAgencyId(agencyId, tokenID);
			String username=user.getUsername();
			if (username==null || username.equals("")){				
				user.setUsername("admin");
				user.setFullname("管理员");
			}
		}
				
		//创建当前案件的账单记录		
		//----------应该根据agencyServiceId获取到服务条目和价格		
		
		Integer cId=new Integer(caseId);
		TradeMarkCase tmCase=tradeMarkCaseMapper.selectByPrimaryKey(cId);		
		String chargeRecordIds=createChargeRecord(gcon, user, tmCase, agencyServiceId, userId);

				
		Integer newCaseId=new Integer(caseId);
		String newAgencyId=null;
		String newAgencyServiceId=agencyServiceId;
		//创建相关案件的账单记录
		while(true){			
			TradeMarkAssociation tradeMarkAssociation=tradeMarkAssociationMapper.selectByCaseId(newCaseId);			
			if (tradeMarkAssociation==null){
				//创建账单
//				Integer receiverType=1; //默认是账单开给客户
//				createBill(agencyId,receiverType,chargeRecordIds);
				break;
			}else{
				newCaseId=tradeMarkAssociation.getRelatedCaseId();
				TradeMarkCase tradeMarkCase=tradeMarkCaseMapper.selectByPrimaryKey(newCaseId);					
				Integer id=tradeMarkCase.getAgencyId();
				if (id!=null){
					newAgencyId=id.toString();
					AgencyService agencyService=agencyServiceService.getAgencyService(gcon, newAgencyId, newAgencyServiceId);
					
					if (agencyService==null){
						logger.info("can not get service of cooperative agency, current agency Id is "+ newAgencyId);
					}else{
						Integer asId=agencyService.getAgencyServiceId();
						if (asId==null){
							continue;
						}
						newAgencyServiceId=asId.toString();
						User user2=null;
						Integer uId=tradeMarkCaseProcessMapper.selectUserIdByCaseId(tradeMarkCase);						
						if (uId!=null){
							//从案件处理过程记录中获取案件的处理人
							userId=uId.toString();
							user2=userService.getUserById(userId, tokenID);
						}
														
						String chargeRecordIdList=createChargeRecord(gcon, user2, tradeMarkCase, newAgencyServiceId, userId);
						//创建账单
//						Integer receiverType=2; //账单开给合作代理机构
//						createBill(newAgencyId, receiverType,chargeRecordIdList);												
						
					}

				}			
				
			}
		}
	}
	
	
	
	
	
	
	private void createBill(String agencyId, Integer receiverType, String chargeRecordIds) throws Exception{
				
		//创建账单
		Bill bill =new Bill();
		Integer aId=new Integer(agencyId);
		bill.setAgencyId(aId);
	
		bill.setReceiverType(receiverType);
//		billService.createBill(bill, chargeRecordIds);		
		
	}
			
	
	
}
