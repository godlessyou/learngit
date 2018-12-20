package com.yootii.bdy.common;

import java.util.LinkedHashMap;
import java.util.Map;

public class ExcelCells {
	
	public static final Map<String, String> ApplicantData = new LinkedHashMap<String, String>();
	static {
		ApplicantData.put("申请人中文名称","applicantName");
		ApplicantData.put("英文名称", "applicantEnName");				
	}
	
	
	public static final Map<String, String> CategoryData = new LinkedHashMap<String, String>();
	static {
		CategoryData.put("Classes","classes");
		CategoryData.put("Goods", "goods");		
	
		
	}
	
	
	
	public static final Map<String, String> WaiBuCase = new LinkedHashMap<String, String>();
	static {
		WaiBuCase.put("案件文号","caseNo");
		WaiBuCase.put("商标注册号", "regNumber");		
		WaiBuCase.put("客户名称","custName");
		WaiBuCase.put("案件类型","caseType");	
		WaiBuCase.put("时间", "caseDate");
		WaiBuCase.put("案由", "caseReason");
		WaiBuCase.put("案件结果", "caseResult");	
		WaiBuCase.put("案件结果文件名称", "caseFile");		
		WaiBuCase.put("案件主管部门", "caseDept");
		
	}
	
	
	public static final Map<String, String> GuoNeiCase = new LinkedHashMap<String, String>();
	static {
			
		GuoNeiCase.put("案件类型", "casetype");		
		GuoNeiCase.put("案件状态", "status");		
		GuoNeiCase.put("案件提交日", "overdate");			
		GuoNeiCase.put("商标图片", "tmimage");
		GuoNeiCase.put("商标名称", "tmname");		
		GuoNeiCase.put("商标类别", "tmtype");
		GuoNeiCase.put("商标申请日", "appdate");
		GuoNeiCase.put("商标申请号", "appnumber");
		GuoNeiCase.put("商标注册日", "regdate");
		GuoNeiCase.put("商标注册号", "regnumber");	
		GuoNeiCase.put("有效期起","validstartdate");
		GuoNeiCase.put("有效期止", "validenddate");	
		GuoNeiCase.put("申请人名称","appname");	
		GuoNeiCase.put("申请人地址","address");		
		GuoNeiCase.put("被申请人名称","inveota");
//		GuoNeiCase.put("代理所","agent");	
		GuoNeiCase.put("案件卷号","caseno");				
		GuoNeiCase.put("商品/服务","tmcategory");		
		
	}
	
	
	public static final Map<String, String> GuoWaiCase = new LinkedHashMap<String, String>();
	static {
		
		GuoWaiCase.put("申请国家/地区","country");
		GuoWaiCase.put("案件类型", "casetype");		
		GuoWaiCase.put("案件状态", "status");		
		GuoWaiCase.put("案件提交日", "overdate");		
		GuoNeiCase.put("商标图片", "tmimage");
		GuoWaiCase.put("商标名称", "tmname");		
		GuoWaiCase.put("商标类别", "tmtype");
		GuoWaiCase.put("商标申请日", "appdate");
		GuoWaiCase.put("商标申请号", "appnumber");
		GuoWaiCase.put("商标注册日", "regdate");
		GuoWaiCase.put("商标注册号", "regnumber");	
		GuoWaiCase.put("有效期起","validstartdate");
		GuoWaiCase.put("有效期止", "validenddate");	
		GuoWaiCase.put("申请人名称","appname");	
		GuoWaiCase.put("申请人地址","address");		
		GuoWaiCase.put("被申请人名称","inveota");
		GuoWaiCase.put("代理所","agent");	
		GuoWaiCase.put("案件卷号","caseno");				
		GuoWaiCase.put("商品/服务","tmcategory");		
		
	}
	
	
	
	public static final Map<String, String> JingWaiTm = new LinkedHashMap<String, String>();
	static {
		JingWaiTm.put("申请国家/地区","country");
		JingWaiTm.put("申请途径","tujing");
		JingWaiTm.put("代理所","agent");	
		JingWaiTm.put("商标图片", "tmimage");
		JingWaiTm.put("商标名称", "tmname");
		JingWaiTm.put("商标类别", "tmtype");		
		JingWaiTm.put("状态", "status");
		JingWaiTm.put("申请日", "appdate");
		JingWaiTm.put("申请号", "appnumber");	
		JingWaiTm.put("注册日", "regnoticedate");
		JingWaiTm.put("注册号", "regnumber");		
		JingWaiTm.put("有效期起","validstartdate");
		JingWaiTm.put("有效期止", "validenddate");			
		JingWaiTm.put("申请人名称（中英文）","applicantname");	
		JingWaiTm.put("申请人地址（中英文）","applicantaddress");
		JingWaiTm.put("商品/服务（中英文）","tmcategory");		
//		JingWaiTm.put("代理文号", "caseno");	
	}
	
	
	
	public static final Map<String, String> JingWaiCase = new LinkedHashMap<String, String>();
	static {
		
		JingWaiCase.put("申请国家/地区","country");
		JingWaiCase.put("案件类型", "casetype");		
		JingWaiCase.put("案件状态", "status");		
		JingWaiCase.put("案件提交日", "overdate");	
		
		JingWaiCase.put("商标标识", "tmimage");		
		JingWaiCase.put("商标名称", "tmname");		
		JingWaiCase.put("商标类别", "tmtype");
		JingWaiCase.put("商标申请日", "appdate");
		JingWaiCase.put("商标申请号", "appnumber");
		JingWaiCase.put("商标注册日", "regdate");
		JingWaiCase.put("商标注册号", "regnumber");	
		JingWaiCase.put("有效期起","validstartdate");
		JingWaiCase.put("有效期止", "validenddate");	
		JingWaiCase.put("申请人名称","appname");	
		JingWaiCase.put("申请人地址","address");		
		JingWaiCase.put("被申请人名称","inveota");
		JingWaiCase.put("代理所","agent");	
		JingWaiCase.put("案件卷号","caseno");				
		JingWaiCase.put("商品/服务","tmcategory");		
		
	}
	
	
	
	public static final Map<String, String> GuoNeiTm = new LinkedHashMap<String, String>();
	static {
		GuoNeiTm.put("申请号", "regNumber");	
		GuoNeiTm.put("国际分类", "tmType");
		GuoNeiTm.put("申请日期", "appDate");
		GuoNeiTm.put("商标名称", "tmName");	
		GuoNeiTm.put("商标图片地址", "imgUrl");
		GuoNeiTm.put("商品/服务","tmCategory");	
		GuoNeiTm.put("类似群","tmGroup");			
		GuoNeiTm.put("申请人名称（中文）", "applicantName");	
		GuoNeiTm.put("申请人名称（英文）", "applicantEnName");	
		GuoNeiTm.put("申请人地址（中文）", "applicantAddress");	
		GuoNeiTm.put("申请人地址（英文）", "applicantEnAddress");	
		GuoNeiTm.put("初审公告期号", "approvalNumber");	
		GuoNeiTm.put("初审公告日期", "approvalDate");	
		GuoNeiTm.put("注册公告期号", "regnoticeNumber");	
		GuoNeiTm.put("注册公告日期", "regNoticeDate");	
		GuoNeiTm.put("专用权期限开始", "validStartDate");
		GuoNeiTm.put("专用权期限结束", "validEndDate");
		GuoNeiTm.put("是否共有商标", "ifShareTm");
		GuoNeiTm.put("商标类型", "classify");
		GuoNeiTm.put("商标形式", "tmForm");
		GuoNeiTm.put("国际注册日期", "gjRegDate");
		GuoNeiTm.put("后期指定日期", "hqzdDate");
		GuoNeiTm.put("优先权日期", "priorDate");
		GuoNeiTm.put("代理/办理机构", "agent");
		GuoNeiTm.put("商标状态图标", "status");
		GuoNeiTm.put("共有商标人列表", "gtApplicantName");
//		GuoNeiTm.put("商标图片", "tmimage");		
		
	}
	
	public static final Map<String, String> GuoNeiTmProcess = new LinkedHashMap<String, String>();
	static {
		GuoNeiTmProcess.put("申请/注册号", "regNumber");	
		GuoNeiTmProcess.put("商评委文号", "spwNumber");
		GuoNeiTmProcess.put("业务名称", "bussName");	
		GuoNeiTmProcess.put("环节名称", "status");
		GuoNeiTmProcess.put("日期","statusDate");
	}
	
	
}
