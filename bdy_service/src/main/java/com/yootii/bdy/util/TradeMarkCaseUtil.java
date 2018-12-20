package com.yootii.bdy.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TradeMarkCaseUtil {
	public static String generateAgentNum(Integer caseId){
		//自动生成文号，TMYD+年+6位编码
		String random ="";
		Integer caseIdLength = String.valueOf(caseId).length();
		if(caseIdLength<=6){
			for(int i=0;i<6-caseIdLength;i++){
				random +="0";
			}
		}else{
			caseId = Integer.parseInt(String.valueOf(caseId).substring(caseIdLength-6));
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		String year = sdf.format(new Date());
		
		//Modification start, 2018-11-22
//		String agentNum ="TM"+year+random+caseId;
		String agentNum ="TMYD"+year+random+caseId;
		//Modification end
		
		return agentNum;
	}
	
	public static void main(String[] args) {
		String generateAgentNum = generateAgentNum(1);
		System.out.println(generateAgentNum);
	}
	
}
