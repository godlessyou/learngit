package com.yootii.bdy.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Constants {
	
	
	public static boolean DataSyn=false;
	

	public static final Map<String, String> serviceList = new LinkedHashMap<String, String>();
	static {
		serviceList.put("1", "商标注册");
		serviceList.put("8", "商标自助注册");
		serviceList.put("9", "商标高级注册");
		
		serviceList.put("22", "商标续展");
		serviceList.put("23", "商标续展自助服务");
		serviceList.put("24", "商标续展高级服务");
		
		serviceList.put("16", "商标转让");
		serviceList.put("17", "商标转让自助服务");
		serviceList.put("18", "商标转让高级服务");
		
		serviceList.put("19", "商标变更");
		serviceList.put("20", "商标变更自助服务");
		serviceList.put("21", "商标变更高级服务");
		
		serviceList.put("6", "商标异议申请");
		serviceList.put("4", "商标异议答辩");
		
		serviceList.put("3", "商标撤三申请");
		serviceList.put("13", "商标撤三申请");
		
		serviceList.put("5", "商标无效宣告申请");
		
		serviceList.put("2", "商标驳回复审");
		serviceList.put("11", "商标驳回复审");
	}
	

	public static final Map<String, String> fileNameList = new LinkedHashMap<String, String>();
	static {
		fileNameList.put("332", "受理通知");
		fileNameList.put("305", "补正");
		fileNameList.put("306", "不予受理");
		fileNameList.put("316", "初审公告");
		fileNameList.put("303", "部分驳回");
		fileNameList.put("302", "驳回");
		fileNameList.put("320", "注册证");
		fileNameList.put("325", "评审意见");		
		
		
		fileNameList.put("340", "异议答辩");
		fileNameList.put("348", "部分不予注册复审通知");
		fileNameList.put("311", "不予注册复审通知");
		fileNameList.put("312", "撤销复审答辩通知");
		fileNameList.put("339", "无效宣告答辩通知");		
		fileNameList.put("342", "质证通知");
		fileNameList.put("343", "证据交换通知");
		fileNameList.put("344", "证据再交换通知");		
	
		
		fileNameList.put("353", "不予注册复审决定-赢");
		fileNameList.put("354", "不予注册复审决定-输");
		fileNameList.put("355", "不予注册复审决定-部分赢");
		
		fileNameList.put("356", "撤销复审决定-部分赢");
		fileNameList.put("357", "撤销复审决定定-输");
		fileNameList.put("358", "撤销复审决定-赢");
		
		fileNameList.put("349", "驳回复审裁定-赢");
		fileNameList.put("350", "驳回复审裁定-输");
		fileNameList.put("359", "驳回复审裁定-部分赢");
		
		fileNameList.put("361", "无效裁定_输");
		fileNameList.put("362", "无效裁定_部分赢");
		fileNameList.put("363", "无效裁定_赢");
		
		fileNameList.put("313", "撤销决定-输");
		fileNameList.put("314", "撤销决定-赢");
		fileNameList.put("364", "撤销决定-部分赢");		
	
		fileNameList.put("504", "异议决定_输");
		fileNameList.put("505", "异议决定_赢");
		fileNameList.put("503", "异议决定_部分赢");
		
		
		
	}
	
	
	public static final Map<String, String> agreeOperation = new LinkedHashMap<String, String>();
	static {	
		fileNameList.put("303", "同意");	
		fileNameList.put("305", "回复");	
		fileNameList.put("325", "回复");		
		
	}
	
	public static final Map<String, String> templateFileNameList = new LinkedHashMap<String, String>();
	static {
		templateFileNameList.put("sbzc", "商标注册申请书");
		templateFileNameList.put("sbxz", "续展注册申请书");
		templateFileNameList.put("zrzy", "转让移转申请注册商标申请书");
		templateFileNameList.put("sbgz", "更正商标申请注册事项申请书");
		templateFileNameList.put("sbbgdlr", "变更商标代理人文件接收人申请书");
		templateFileNameList.put("sbbgsqrmydz", "变更商标申请人注册人名义地址变更集体商标证明商标管理规则集体成员名单申请书");
		templateFileNameList.put("bhfs", "驳回复审申请书");
		templateFileNameList.put("sbdlwt", "商标代理委托书");
		templateFileNameList.put("sbpsdlwt", "商标评审代理委托书");
		templateFileNameList.put("sbdlwt_EN", "商标代理委托书(英文)");
		templateFileNameList.put("sbpsdlwt_EN", "商标评审代理委托书(英文)");
		templateFileNameList.put("yysq", "异议申请书");
		templateFileNameList.put("yyly", "异议理由书");
		templateFileNameList.put("yydbwt_CN", "异议答辩委托书-中文版");
		templateFileNameList.put("yydbwt_EN", "异议答辩委托书-英文版");
		templateFileNameList.put("cs", "测试用例");
	}
	public static final Map<String, String> templateList = new LinkedHashMap<String, String>();
	static {
		templateList.put("202","sbzc");
		templateList.put("203","sbxz");
		templateList.put("204","zrzy");
		templateList.put("205","sbgz");
		templateList.put("206","sbbgdlr");
		templateList.put("207","sbbgsqrmydz");
		
		
		templateList.put("209", "bhfs");    
		templateList.put("215", "sbdlwt");
		templateList.put("216", "sbpsdlwt");
		
		templateList.put("211", "sbdlwt_EN");
		templateList.put("212", "sbpsdlwt_EN");
		templateList.put("213", "yysq");
		templateList.put("214", "yyly");
		
		templateList.put("217", "yydbwt_CN");
		templateList.put("218", "yydbwt_EN");
		templateList.put("200", "cs");
	}
	
	public static final String customer_prefix="cust_";

	
	public static final String solrUrl = "http://localhost:8984/solr/"; //8983端口被solr6.0占用，solr6.6.0使用8984端口
	
	public static final String imageWebPath="http://localhost/monimage/";
	
	public static final String ggHttpLink="http://www.ip-manager.top/hgj_pages/hint_arrivals.html";
	
	public static final String [] dataFileNames={"trademarkcase","tmtrademark","trademarkfile","timelimit","hgjupdate"};
	
	public static final String bat_location=System.getProperty("batlocation", "C:/support/hgjupdate");
	
	public static final String data_export_bat_path=bat_location+"/export-for-hgj.bat";

	public static final String data_import_bat_path=bat_location+"/import-for-hgj.bat";
	
	public static final String data_dir=bat_location+"/casedata";
	
	public static final String gonggao_dir=System.getProperty("gglocation", "C:/support/gonggao");
	
	public static final String gonggao_pages=System.getProperty("ggpages", "C:/support/gonggao/pages");
	
	public static final String[] gongGaoTypes = {"送达公告", "商标初步审定公告"};
	
	public static final String[] gongGaoNames = { "关于提供注册商标使用证据的通知 ",
		"商标驳回通知书", "商标部分驳回通知书", "商标评审案件证据再交换通知书", "补正通知书", "商标注册同日申请抽签通知书",
		"商标注册同日申请协商通知书", "不予受理通知书", "异议决定书" };
	
	private static String isDevelopemode = System.getProperty("developemode", "false");

	public static boolean developemodeFlag = (isDevelopemode.equals("true")) ? true : false;
	
	public static final int user_type_admin =1;
	public static final int user_type_guest =2;
	public static final int user_type_customer =10;
	public static final int user_type_agent =20;
	
	public static final String customerLogo = "/logo/";
	
//	public static final String StartDay = "2017-03-24";
	
	public static final int caseProgressDay = 5;
	
	public static final int bohuiProgressDay = 15;
	
	public static final int qitaProgressMonth = 6;
	
	public static final String autocreater = "system";
	
	public static final String guanfangtongzhi = "官方通知";
	
	public static final String daichuliStatus = "待处理";
	
	public static final String chulizhongStatus = "处理中";
	
	public static final String completeStatus = "完成";
	
	public static final String closeStatus = "关闭";
	
	// 代理人的角色，分为组长、组员
	public static final String agentType ="leader";
	
	// 提醒周期的默认值是7天
	public static final String alarmDefaut="7";
	
	public static final Map<String, String> zhuceliucheng = new LinkedHashMap<String, String>();
	static {
		zhuceliucheng.put("申请书", "申请");
		zhuceliucheng.put("受理通知", "受理通知");
		zhuceliucheng.put("初步审定公告", "初步审定公告");
		zhuceliucheng.put("注册证", "注册完成");
	}
	
	
	public static final List<Map<String, String>> caseliucheng = new ArrayList<Map<String, String>>();
	static {
		caseliucheng.add(zhuceliucheng);
		
	}
	
	
	public static final Map<String, String> guanwang = new LinkedHashMap<String, String>();
	static {
		guanwang.put("tmimage", "商标图样");
		guanwang.put("tmname", "商标名称");
		guanwang.put("tmtype", "国际分类号");
		guanwang.put("regnumber", "注册号");
		guanwang.put("appdate", "申请日期");		
		guanwang.put("applicantname", "申请人");
		guanwang.put("applicantaddress", "申请人地址");
		guanwang.put("applicantenname", "申请人英文名称");
		guanwang.put("applicantenaddress", "申请人英文地址");	
		guanwang.put("gtapplicantname", "共同申请人");
		guanwang.put("gtapplicantaddress", "共同申请人地址");
		guanwang.put("status", "商标状态");
		guanwang.put("approvalnumber", "初审公告期号");
		guanwang.put("approvaldate", "初审公告日期");
		guanwang.put("regnoticenumber", "注册公告期号");
		guanwang.put("regnoticedate", "注册公告日期");
		guanwang.put("validstartdate", "有效期起始日");
		guanwang.put("validenddate", "有效期截止日");
		guanwang.put("classify", "商标分类");
		guanwang.put("liucheng", "流程");
		guanwang.put("agent", "代理机构名称");		
		guanwang.put("goods", "商品或服务列表");		
	}
	
	public static final Map<String, String> tmJingWai = new LinkedHashMap<String, String>();
	static {	
			
		tmJingWai.put("tmimage", "商标图样");
		tmJingWai.put("country", "申请国家/地区");		
		tmJingWai.put("tujing", "申请途径");	
		tmJingWai.put("agent", "代理机构名称");	
		tmJingWai.put("tmname", "商标名称");
		tmJingWai.put("tmtype", "国际分类号");
		tmJingWai.put("status", "商标状态");	
		tmJingWai.put("appdate", "申请日期");	
		tmJingWai.put("appnumber", "商标申请号");
		tmJingWai.put("regnoticedate", "注册日期");	
		tmJingWai.put("regnumber", "商标注册号");			
		tmJingWai.put("validstartdate", "有效期起始日");
		tmJingWai.put("validenddate", "有效期截止日");
		tmJingWai.put("applicantname", "申请人");
		tmJingWai.put("applicantaddress", "申请人地址");	
		tmJingWai.put("goods", "商品/服务");	
		
	}
	
	
	public static final Map<String, String> tmcase_trademark = new LinkedHashMap<String, String>();
	static {
		tmcase_trademark.put("tmimage", "商标图样");		
		tmcase_trademark.put("tmtype", "国际分类号");
		tmcase_trademark.put("tmnumber", "商标号");
		tmcase_trademark.put("tmname", "商标名称");		
		tmcase_trademark.put("custname", "客户名称");
		tmcase_trademark.put("appname", "申请人");
		tmcase_trademark.put("address", "申请人地址");		
		tmcase_trademark.put("status", "商标状态");		
		tmcase_trademark.put("overdate", "递交日期");
		tmcase_trademark.put("appdate", "申请日期");
		tmcase_trademark.put("approvaldate", "初审日期");
		tmcase_trademark.put("regdate", "注册日期");		
		tmcase_trademark.put("validstartdate", "有效期起始日");
		tmcase_trademark.put("validenddate", "有效期截止日");		
		tmcase_trademark.put("goods", "商品或服务列表");
	}
	
	public static final Map<String, String> tmcase_case = new LinkedHashMap<String, String>();
	static {
		tmcase_case.put("tmimage", "商标图样");		
		tmcase_case.put("tmtype", "国际分类号");
		tmcase_case.put("tmnumber", "商标号");
		tmcase_case.put("tmname", "商标名称");		
		tmcase_case.put("custname", "客户名称");
		tmcase_case.put("appname", "申请人");
		tmcase_case.put("address", "申请人地址");		
		tmcase_case.put("tmstatus", "商标状态");		
		tmcase_case.put("overdate", "递交日期");
		tmcase_case.put("appdate", "申请日期");
		tmcase_case.put("approvaldate", "初审日期");
		tmcase_case.put("regdate", "注册日期");		
		tmcase_case.put("validstartdate", "有效期起始日");
		tmcase_case.put("validenddate", "有效期截止日");
		tmcase_case.put("caseno", "案件号");
		tmcase_case.put("casetype", "案件类型");
		tmcase_case.put("casestatus", "案件状态");
		tmcase_case.put("statusdate", "状态日期");
		tmcase_case.put("goods", "商品或服务列表");	
		tmcase_case.put("inveota", "被申请人");
		tmcase_case.put("rejectdate", "驳回日期");	
//		tmcase_case.put("objota", "异议人");
//		tmcase_case.put("invaota", "无效宣告申请人");
//		tmcase_case.put("revota", "撤销申请人");				
//		tmcase_case.put("reason", "无效原因");	
	}
	
	
	public static final Map<String, String> tmcase_jingwai= new LinkedHashMap<String, String>();
	static {
		tmcase_jingwai.put("tmimage", "商标图样");	
		tmcase_jingwai.put("country", "申请国家/地区");		
		tmcase_jingwai.put("tujing", "申请途径");	
		tmcase_jingwai.put("casetype", "案件类型");
		tmcase_jingwai.put("casestatus", "案件状态");
		tmcase_jingwai.put("overdate", "递交日期");
		tmcase_jingwai.put("tmname", "商标名称");	
		tmcase_jingwai.put("tmtype", "国际分类号");		
		tmcase_jingwai.put("appdate", "申请日期");	
		tmcase_jingwai.put("regdate", "注册日期");	
		tmcase_jingwai.put("tmnumber", "商标号");			
		tmcase_jingwai.put("validstartdate", "有效期起始日");
		tmcase_jingwai.put("validenddate", "有效期截止日");
		tmcase_jingwai.put("appname", "申请人");
		tmcase_jingwai.put("address", "申请人地址");		
		tmcase_jingwai.put("inveota", "被申请人");		
		tmcase_jingwai.put("caseno", "案件号");
		tmcase_jingwai.put("agent", "代理机构名称");	
		tmcase_jingwai.put("goods", "商品/服务");	
	}
	
	
	//下列流程的时间相同，所以取其中一个作为查询条件
	//商标异议申请中
	//收到异议申请或补充材料,待审
	
	
	//撤销三年不使用待审中
	//撤销连续三年停止使用注册商标中
	
	//无效宣告中
	//无效宣告收到争议申请或补充材料,待审
	
	public static final Map<String, String> dongtaiMap = new HashMap<String, String>();
	static {
		dongtaiMap.put("驳回", "商标注册申请驳回通知发文,打印驳回或部分驳回通知书");
		dongtaiMap.put("被异议", "商标异议申请中");
		dongtaiMap.put("被撤三", "撤销三年不使用待审中");
		dongtaiMap.put("被无效宣告", "无效宣告中");
		dongtaiMap.put("被撤销通用名称", "通用名称");
		dongtaiMap.put("变更", "商标变更完成");
		dongtaiMap.put("转让", "商标转让完成");
		dongtaiMap.put("续展", "商标续展完成,商标续展中");
		dongtaiMap.put("许可备案", "商标使用许可备案完成");		
	}
	

	
//  撤回商标注册申请中
//	撤销连续三年停止使用注册商标申请完成
//	商标注销申请完成
//	商标注册申请等待驳回复审
//	商标注册申请等待驳回通知发文
//	打印驳回或部分驳回通知书
//	驳回复审排版送达公告(驳回复审决定书)
	
	public static final Map<String, String> wuxiaoMap = new HashMap<String, String>();
	static {
		wuxiaoMap.put("被撤三", "撤销三年不使用审理完成,撤销连续三年停止使用注册商标申请完成");
		wuxiaoMap.put("被注销", "商标注销完成,商标注销申请完成");
		wuxiaoMap.put("被驳回", "商标注册申请驳回通知发文,商标注册申请等待驳回通知发文,打印驳回或部分驳回通知书,驳回复审完成");
		wuxiaoMap.put("撤回商标注册", "撤回商标注册申请中");
		wuxiaoMap.put("专用权已过期", "专用权已过期");		
	}
	
	public static final Map<String, String> tmstatus = new HashMap<String, String>();
	static {			
		tmstatus.put("已注册", "已注册");
		tmstatus.put("申请中", "待审中,已初审");
		tmstatus.put("已无效", "已销亡,已驳回");		
	}
	
	public static final Map<String, String> tmstatus_new = new HashMap<String, String>();
	static {
		//其他情形;
		//撤销/无效宣告申请审查中
		//无效(仅供参考)
		//此商标正等待受理，暂无法查询详细信息。
		tmstatus_new.put("已注册", "注册,注册公告");
		tmstatus_new.put("已无效", "无效,其他情形");
		tmstatus_new.put("申请中", "驳回复审中,等待实质审查,异议中,初审公告,申请审查中,此商标正等待受理");		
	}
	
	public static final Map<String, String> tmgwstatus = new HashMap<String, String>();
	static {			
		tmgwstatus.put("已注册", "已注册");
		tmgwstatus.put("申请中", "待审中");
		tmgwstatus.put("已无效", "已驳回");
		
	}
	
	
	public static final Map<String, String> tmdongtai = new HashMap<String, String>();
	static {			
		tmdongtai.put("被驳回", "商标注册申请驳回通知发文,商标注册申请等待驳回通知发文,打印驳回或部分驳回通知书");
		tmdongtai.put("被提起异议申请", "商标异议申请中");
		tmdongtai.put("被提起撤三申请", "撤销三年不使用待审中");
		tmdongtai.put("被提起无效宣告申请", "无效宣告中");
		tmdongtai.put("被提起撤销通用名称申请", "通用名称");	
	}
	
	public static final Map<String, String> casedongtai = new HashMap<String, String>();
	static {			
		casedongtai.put("受理通知", "受理通知书");
		casedongtai.put("初审公告", "初步审定公告");
		casedongtai.put("提交报告", "报官清单回执");
		casedongtai.put("驳回通知", "驳回通知,部分驳回通知");
		casedongtai.put("核准报告", "核准证明,注册证");	
		casedongtai.put("裁定", "裁定-赢,裁定-输,裁定-部分");
		casedongtai.put("决定", "决定");	
	}
	
	
	public static final Map<String, String> wpmdongtai = new HashMap<String, String>();
	static {			
		wpmdongtai.put("被驳回", "驳回通知");
		wpmdongtai.put("被提起异议申请", "异议答辩通知");
		wpmdongtai.put("被提起撤三申请", "撤销复审答辩通知");
		wpmdongtai.put("被提起无效宣告申请", "无效宣告答辩通知");
		wpmdongtai.put("被提起撤销通用名称申请", "通用名称撤销答辩通知");	
	}
	

	public static final Map<String, String> daibantype = new LinkedHashMap<String, String>();
	static {
		daibantype.put("确认合同信息", "qrht_alarm_interval");
		daibantype.put("签署委托手续", "qswtd_alarm_interval");
		daibantype.put("提供证据", "tgzj_alarm_interval");
		daibantype.put("续展", "xuzhan_alarm_interval");
		daibantype.put("被驳回", "beibohui_alarm_interval");
		daibantype.put("被提起撤三申请", "beichesan_alarm_interval");
		daibantype.put("被提起异议申请", "beiyiyi_alarm_interval");
		daibantype.put("被提起无效宣告申请", "beiwuxiaoxg_alarm_interval");
		daibantype.put("被提起撤销通用名称申请", "beicxtymc_alarm_interval");		
		daibantype.put("对他人商标提起撤三申请", "chesan_alarm_interval");
		daibantype.put("对他人商标提起异议申请", "yiyi_alarm_interval");
		daibantype.put("对他人商标提起无效宣告申请", "wuxiaoxg_alarm_interval");
	}

	

	
//	异议
//	无效宣告申请
//	撤销三年停止使用申请
//	撤回撤销三年不使用注册商标
//	撤销商标复审
//	不予注册复审
//	参加不予注册复审	
	public static String [] nonquanliren_status={
		"异议",
		"撤回商标异议",
		"无效宣告申请",
		"撤销",
		"撤销三年停止使用申请",
		"撤回撤销三年不使用注册商标",
		"撤销商标复审",
		"撤销成为通用名称注册商标",
		"撤销成为通用名称注册商标",
		"撤回撤销成为通用名称",
		"撤销注册不当",
		"不予注册复审",
		"商标监控协议",
		"咨询/其他",
		"许可备案"};
	
	public static String [] nonquanliren_caseType={
		"签署代理合同协议"};
		
	public static String [] officialnames={"驳回通知","异议答辩通知","撤销复审答辩通知","无效宣告答辩通知","通用名称撤销答辩通知"};
	
	public static String [] wuxiao={"已销亡","已驳回"};
	
	
	public static String [] status_condition={"已注册","已无效","效力待定"};
	
	public static String [] yizhuce={"已注册","完成","裁定-赢","决定-赢","已核准", "注册，已转达"};
	
	public static String [] yiwuxiao={"商标无效","不予","裁定-输","决定-输","视为放弃","客户放弃","客户撤回","放弃"};
	
	public static final String base64ImageStr ="data:image/png;base64,";
	
	public static final String imageDir = "downloadimage";
	
	public static final String uploadDir = "upload";
	
	public static final String exportDir = "export";
	
	public static final String casefileDir = "mydoc";
	
	public static final String app_prefix = "biaodeyi";

	public static final String catalina_home = System.getProperty("catalina.home","C:/tmimage");
		
	public static final String ftpConfigFile = "ftp.properties";

	public static String app_dir = catalina_home + "/"+ app_prefix;
	
	public static final String download_image_dir = "C:/tmimage"+ "/"+ app_prefix;

	public static String image_dir = app_dir + "/"+ imageDir;
	
	public static String upload_dir = app_dir + "/" + uploadDir;	
	
	public static final String upload_TmImage_dir = upload_dir + "/tmimage/";
	
	public static final String export_dir = app_dir + "/" + exportDir;
	
	public static final String casefile_dir = app_dir + "/"	+ casefileDir;
	
	
	public static final String tmcaseFileDir = "/tmcasefile/" ;
	
	public static final String tmcaseImageDir = "/tmcaseimage/" ;
	
	public static final String velocityDir = "/velocity/" ;
	
	public static final String templateDir = "/template/";
	
	public static String[] illegalCharacter={"\\", "/", ":", "*", "?", "\"", "<", ">", "|", "\r\n"};


	public static final String BUNDLE_KEY = "ApplicationResources";
	
	//public static String [] tmStatus={"已驳回","待审中","已初审","已注册","已销亡"};
	
	//分页
	public static final Integer LIMIT_NUM = 10;

	public static final String FILE_SEP = System.getProperty("file.separator");

	public static final String USER_HOME = System.getProperty("user.home")
			+ FILE_SEP;

	public static final String CONFIG = "appConfig";
 

	public static final String USER_KEY = "userForm";

	public static final String USER_LIST = "userList";

	public static final String REGISTERED = "registered";

	public static final String ADMIN_ROLE = "管理员组";

	public static final String USER_ROLE = "ROLE_USER";

	public static final String USER_ROLES = "userRoles";

	public static final String AVAILABLE_ROLES = "availableRoles";

	/**
	 * The name of the CSS Theme setting.
	 */
	public static final String CSS_THEME = "csstheme";

	public static final String USER_ADAPTER = "userAdapter";

	public static final String PRODUCT_ADAPTER = "productAdapter";

	public static final String SPECIFICATION_ADAPTER = "specificationAdapter";

	public static final String CURRENT_LOGIN_USER = "currentLoginUser";

	/**
	 * 一级标签, 用户缓存一级目录
	 */
	public static final String MENU_LEVEL_ONE = "menuLevelOne";

	/**
	 * 进入系统的第一个主标签, 将进行自动跳转
	 */
	public static final String DEFAULT_ACTION = "defaultAction";

	/**
	 * 二级标签, 系统管理
	 */
	public static final String MENU_SYSTEM_MANAGER = "menuSystemManager";

	/**
	 * 标签类型
	 */
	public static final String TAG_TYPE_MAP = "tagTypeMap";

	/**
	 * 资源类型
	 */
	public static final String RESOURCE_TYPE_MAP = "resourceTypeMap";

	/**
	 * 标签类型 系统管理
	 */
	public static final String TAG_TYPE_SYSTEM_MANAGER = "systemManager";

	/**
	 * 数据缓存类型
	 */
	public static final String DATA_CACHE_MAP = "dataCacheMap";

	/**
	 * 安全级别
	 */
	public static final String SECURITY_LEVEL_MAP = "securityLevelMap";

	/**
	 * 一级标签, 用于标签信息 维护其父标签id
	 */
	public static final String MENU_LEVEL_ONE_MAP = "menuLevelOneMap";

	public static final String TAG_CODE_SYSTEM = "SystemMenu";

	public static final String TAG_CODE_EXIT = "Logout";
	
	/**
	 * 图片映射路径
	 */
	public static final String SERVER_HTTP_URL = "http://portal.yootii.cn:8080";
 
	/**
	 * 档案资料上传路径
	 */
	public static final String FILE_UPLOAD_DOC = "/WHDDOC/ipms_upload/docs/";
	
	/**
	 * 图片资料上传路径
	 */
	public static final String FILE_UPLOAD_IMAGES = "/WHDDOC/ipms_upload/images/";
	
	/**
	 * FTP用户
	 */
	public static final String FTP_SERVER_USER = "ustrftp";
	/**
	 * FTP密码
	 */
	public static final String FTP_SERVER_PASSWORD = "ts_WJ.680602.wHdIt";
	
	/**
	 * FTP IP地址
	 */
	public static final String FTP_SERVER_IP = "192.168.0.28";
	
	/**
	 * FTP访问路径
	 */
	public static final String FTP_SERVER_URL = "ftp://ustrftp:ts_WJ.680602.wHdIt@192.168.0.28";
	
	/**
	 * 商标局官网 商标列表  cnNameFlag=2&paiType=0&type=cn&intcls=&appCnName=
	 */
	//public static final String TRADEMARK_OFFICE_List = "http://sbcx.saic.gov.cn:9080/tmois/wszhcx_getLikeCondition.xhtml";
	
	public static final String TRADEMARK_OFFICE_List = "http://sbcx.saic.gov.cn:9080/tmois/wszhcx_pageZhcxMain.xhtml";
	
	/**
	 * 商标局官网 商标状态地址  regNum=9560706&intcls=34
	 */
	public static final String TRADEMARK_OFFICE_STATES = "http://sbcx.saic.gov.cn:9080/tmois/wsztcx_getStatesByRegInt.xhtml";
	
	/**
	 * 商标局官网 商品服务列表 regNum=9560706&intcls=34
	 */
	public static final String TRADEMARK_OFFICE_GOODS = "http://sbcx.saic.gov.cn:9080/tmois/wszhcx_getGoodsDetail.xhtml";
	
	/**
	 * 商标局官网 共同申请人列表 regNum=14592871
	 */
	public static final String TRADEMARK_OFFICE_APPLICANT = "http://sbcx.saic.gov.cn:9080/tmois/wsjscx_getCoowners.xhtml";
	
	/**
	 * 商标局官网 商品信息图片 regNum=14592871&intcls=32
	 */
	public static final String TRADEMARK_OFFICE_INFOS = "http://sbcx.saic.gov.cn:9080/tmois/wszhcx_showPdfToImageInfo.xhtml";
	
	/**
	 * 商标局官网 商品信息  regNum=15560135&intcls=9 带验证码不能破解 geetest.com
	 */
	public static final String TRADEMARK_OFFICE_DETAIL = "http://sbcx.saic.gov.cn:9080/tmois/wszhcx_getDetailByRegInt.xhtml";
	
	/**
	 * 商标局官网 商品图样 regNum=12173264&intcls=45
	 */
	public static final String TRADEMARK_OFFICE_IMAGE = "http://sbcx.saic.gov.cn:9080/tmois/wszhcx_getImageInputSterem.xhtml";
	
	/**
	 * 官网截取的商标图样文件夾地址
	 */
	public static final String TRADEMARK_OFFICE_PICFOLDER = "images/trademark/";
	/**
	 * 资料存放地址
	 */
	public static final String material_dir="/material";
	
	public static final String guanwen_dir="/guanwen";
	
	
	public static final String guanwen_download_dir=app_dir + "/gwdownload";
	
	
	private static boolean initFlag = false;
	
	public static boolean isInitFlag() {
		return initFlag;
	}
	
	public static void init(String fileUrl) {		
		
		if (!initFlag){	
			if (fileUrl!=null && !fileUrl.equals("")){
				app_dir=fileUrl;
				image_dir = app_dir + "/"+ imageDir;
				upload_dir = app_dir + "/" + uploadDir;	
			}			
			initFlag=true;
		}
	}
	
	
	public static void initDataSyn(boolean dataSyn) {		
		
		DataSyn=dataSyn;
	}
	
}
