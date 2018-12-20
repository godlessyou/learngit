package com.yootii.bdy.log.model;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 操作日志类型
 * 
 *2018/4/8
 */
public enum LogTypeEnum {
	COMMON_TASKREMIND_ADDTASKREMIND("taskremind/addtaskremind","任务提醒","添加任务提醒"),
    COMMON_TASKREMIND_DELETETASKREMIND("taskremind/deletetaskremind","任务提醒","删除任务提醒"),
    COMMON_MONITORM_ADDTM("monitortm/addtm","被监测商标","增加被监测的商标"),
    COMMON_MONITORM_DELETETM("monitortm/deletetm","被监测商标","删除被监测的商标"),
    COMMON_MONITORM_MDIFYTM("monitortm/modifytm","被监测商标","修改被监测的商标"),
    COMMON_PALTFORMSERVICE_CREATESERVICE("platformservice/createservice","平台业务服务","创建平台业务服务"),
    COMMON_PALTFORMSERVICE_DELETESERVICE("platformservice/deleteservice","平台业务服务","删除平台业务服务"),
    COMMON_PALTFORMSERVICE_MODIFYSERVICE("platformservice/modifyservice","平台业务服务","修改平台业务服务"),
    COMMON_AGENCYSERVICE_CREATEAGENCYSERVICE("agencyservice/createagencyservice","代理机构对外业务服务","代理所开放对外的业务"),
    COMMON_AGENCYSERVICE_DELETEAGENCYSERVICE("agencyservice/deleteagencyservice","代理机构对外业务服务","代理所关闭对外的业务"),
    COMMON_AGENCYSERVICE_MODIFYAGENCYSERVICE("agencyservice/modifyagencyservice","代理机构对外业务服务","修改对外业务服务"),    
    COMMON_TASK_CREATECASE("task/createcase","案件","创建案件"),
    COMMON_TASK_MODIFYCASE("task/modifycase","案件","修改案件"),
    COMMON_TASK_REFUSE("task/refuse","案件","拒绝"),
    COMMON_TASK_ASSGINCASE("task/assginCase","案件","分配案件"),
    COMMON_TASK_AUDITED("task/audited","案件","审核通过"),
    COMMON_TASK_NOTAUDITED("task/notaudited","案件","审核不通过"),
    COMMON_TASK_SUBMITAPP("task/submitapp","案件","递交申请"),
    COMMON_TASK_APPOLINE("task/appoffline","案件","直接递交申请"),
    COMMON_TASK_OFFICELDOC("task/officaldoc","案件","官文录入"),
    COMMON_TASK_AUDITDOC("task/auditdoc","案件","审核关文"),
    COMMON_TASK_PROCESSDOC("task/processdoc","案件","处理官文"),
    COMMON_TASK_FEEDBACK("task/feedback","案件","向客户反馈"),
    COMMON_TASK_PROCESSDESCISION("task/processdecision","案件","处理客户决定"),
    COMMON_TASK_SUBMITCASE("stask/ubmitcase","案件","提交案件"),
    COMMON_TASK_AGREE("task/agree","案件","同意"),
    COMMON_TASK_CLOSECASE("task/closecase","案件","关闭案件"),
    COMMON_TASK_APPONLINERESULT("task/apponlineresult","案件","网上递交申请结果接口"),
    
    COMMON_TMCASE_CREATECASE("tmcase/createcase","案件","创建正式的案件信息"),
    COMMON_TMCASE_MODIFYCASE("tmcase/modifycase","案件","修改案件信息"),
    COMMON_TMCASE_CREATECASEASSOCIATE("tmcase/createcaseassociate","案件","创建一条新案件，采用指定的原始案件的数据"),
    COMMON_TMCASEFILE_UPLOADCASEFILE("tmcasefile/uploadcasefile","案件","上传案件的文件"),
    
    COMMON_TMCASEPRE_CREATECASE("tmcasepre/createcase","预立案","创建预立案"),
    COMMON_TMCASEPRE_MODIFYCASE("tmcasepre/modifycase","预立案","修改预立案"),
    COMMON_TMCASEFILEPRE_UPLOADCASEFILE("tmcasefilepre/uploadcasefile","预立案","上传预立案的文件"),
    
    COMMON_TMCASEJOINAPP_CREATEJOINAPP("tmcasejoinapp/createjoinapp","共同申请人","创建共同申请人"),
    COMMON_TMCASEJOINAPP_MODIFYJOINAPP("tmcasejoinapp/modifyjoinapp","共同申请人","修改共同申请人"),
    COMMON_TMCASEJOINAPP_DELETEJOINAPP("tmcasejoinapp/deletejoinapp","共同申请人","删除共同申请人"),
    
    COMMON_TMCASEPROCESS_CREATECASEPROCESS("tmcaseprocess/createcaseprocess","案件流程","创建案件流程"),
    
    COMMON_TASK_CREATEBILL("task/creatbill","账单流程","创建账单"),
    COMMON_TASK_AUDITEDBILL("task/auditedbill","账单流程","审核账单"),
    COMMON_TASK_MODIFYBILL("task/modifybill","账单流程","审核失败修改账单"),
    
    COMMON_BILL_CREATECHARGERECORD("bill/createchargerecord","账单记录","创建账单记录"),
    COMMON_BILL_DELETECHARGERECORD("bill/deletechargerecord","账单记录","删除账单记录"),
    COMMON_BILL_MODIFYCHARGERECORD("bill/modifychargerecord","账单记录","修改账单记录"),
    
    COMMON_BILL_MODIFYBILL("bill/modifybill","账单","修改账单"),
    COMMON_BILL_CREATEBILL("bill/createbill","账单","增加账单"),
    COMMON_BILL_DELETEBILL("bill/deletebill","账单","删除账单"),
    COMMON_BILL_GENERATEBILLNO("bill/generatebillno","账单","生成账单号"),
    COMMON_BILL_CASEGENERATEBILL("bill/casegeneratebill","账单","将案件对应的未核销的账单记录生成账单"),
    COMMON_BILL_RECORDGENERATEBILL("bill/recordgeneratebill","账单","选择未核销的账单记录生成账单"),
	
	
    COMMON_MATERAL_CREATEMATERAL("materal/createmateral","资料","创建资料"),
    COMMON_MATERAL_MODIFYMATERAL("materal/modifymateral","资料","修改资料属性"),
    COMMON_MATERAL_UPLOADFULE("materal/uploadFile","资料","上传资料文件"),
	COMMON_MATERAL_DELETEMATERIAL("materal/deleteMaterial","资料","删除资料");
    
	
    
    //其他

    private String methodName;//方法名称与controller一致 字母全小写
    private String key;//事件类型
    private String description;//事件描述
    private LogTypeEnum(String methodName,String key,String description){
        this.methodName = methodName;
        this.key = key;
        this.description = description;
    }
    public String getMethodName() {
        return methodName;
    }
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 根据方法名返回
     * @param methodName
     * @return
     */
    public static LogTypeEnum getDesByMethodName(String methodName){
    	methodName = methodName.toLowerCase();
        return innerMap.map.get(methodName);
    }

    /**
     * 内部类 用户保存所有的enum 无须通过Enum.values()每次遍历
     * @author Mingchenchen
     *
     */
    private static class innerMap{
        private static Map<String, LogTypeEnum> map = new ConcurrentHashMap<>(128);

        static{
            //初始化整个枚举类到Map
            for (LogTypeEnum logTypeEnum : LogTypeEnum.values()) {
                map.put(logTypeEnum.getMethodName(), logTypeEnum);
            }
        }
    }
}