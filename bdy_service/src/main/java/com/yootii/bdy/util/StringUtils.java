package com.yootii.bdy.util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;



public class StringUtils {
	
	public static String replaceChar(String name) {
		name = name.replaceAll("[/\\\\:*\"?<>|]","");
	    return name;
	}
	
	
	
	 // 字符串合并
	public static String addString(String dest, String source) {

		String data = "";
		if (source != null && !source.equals("")) {
			data = dest + source;
		}else{
			data=dest;
		}
		return data;
	}
	
	
	
	// 计算字符串的hash值
	public static int getStringHash(String data) {					
	
		int hashValue = HashAlgorithms.FNVHash1(data); 
		
		return hashValue;

	}
	
	public static String getNumber(String str) {
		String number = "";
	
		List<Character> list = new ArrayList<Character>();
		for (int i = 0; i < str.length(); i++) {
			char ca = str.charAt(i);
			if (Character.isDigit(ca)) {
				list.add(ca);
			}
		}
		if (list != null) {
			int size = list.size();
			char[] tt = new char[size];
			
			for (Character cat : list) {
				char ca = cat.charValue();
				number = number + ca;
			}			
		}
		
		return number;
	}
	
	// 整型数组排序
	public static void bubbleSort(int[] numbers) {
		int temp; // 记录临时中间值
		int size = numbers.length; // 数组大小
		for (int i = 0; i < size - 1; i++) {
			for (int j = i + 1; j < size; j++) {
				if (numbers[i] > numbers[j]) { // 交换两数的位置
					temp = numbers[i];
					numbers[i] = numbers[j];
					numbers[j] = temp;
				}
			}
		}
	}
	
	// 字符串排序
	public static void listSort(List<?> list) {		
		Collections.sort(list, new Comparator<Object>() {
	      @Override
	      public int compare(Object o1, Object o2) {
	        return new Integer((String) o1).compareTo(new Integer((String) o2));
	      }
	    });		
	}
	
	
	
	
	public static boolean isNum(String str) {
		return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
	}
	
	
	
	//判断字符串是否是数字， 只要字符串中有一个字符不是数字，就返回false
	public static boolean isNumeric(String str) {
		for (int i = 0; i < str.length(); i++) {
//				System.out.println(str.charAt(i));
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}
	
	
	public static String getPercent(long y, long z, boolean baifenhao){
		String baifenbi = "";// 接受百分比的值
		double baiy = y * 1.0;
		double baiz = z * 1.0;
		double fen = baiy / baiz;
		// NumberFormat nf = NumberFormat.getPercentInstance();注释掉的也是一种方法
		// nf.setMinimumFractionDigits( 2 ); 保留到小数点后几位
//		DecimalFormat df1 = new DecimalFormat("##.00%");
		DecimalFormat df2 = new DecimalFormat("0.00%");
		
		// ##.00%
		// 百分比格式，后面不足2位的用0补齐
		// baifenbi=nf.format(fen);
//		baifenbi = df1.format(fen);		
		
		baifenbi = df2.format(fen);	
		if(!baifenhao){	
			if (baifenbi!=null){
				int len=baifenbi.length();
				baifenbi = baifenbi.substring(0,len-1);
			}			
		}	
		
		return baifenbi;
	} 
	

	
	public static String processTmGroup(String tmType, String tmGroup, String tmGroup2){
		
		int tmTypeLen=tmType.length();
		
		if (tmGroup==null || tmGroup.equals("")){			
			return tmGroup2;
		}
		
		List<String> tmGroupList2 = Arrays.asList(tmGroup2.split(";"));
		List<String> tmGroupList = Arrays.asList(tmGroup.split(";"));
		
		List<String> arrayList2 = new ArrayList<String>(tmGroupList2);
		
		Iterator<String> it = arrayList2.iterator();
		while (it.hasNext()) {
			String group=it.next();	
			if (group==null){
				continue;
			}
			int len=group.length();
			if (len>tmTypeLen){
				String s=group.substring(0,tmTypeLen);
				if (s.equals(tmType)){
					it.remove();
				}
			}			
		}
		
		
		for (String group: tmGroupList){
			arrayList2.add(group);
		}
		
		//排序
		StringUtils.listSort(arrayList2);
		
		String[] b = arrayList2.toArray(new String[arrayList2.size()]);
		String tmGroupResult= String.join(";", b);
		if(tmGroupResult!=null && !tmGroupResult.endsWith(";")){
			tmGroupResult=tmGroupResult+";";
		}
		
		return tmGroupResult;
	}
	
	
	public static void main(String[] args) {
		String tmType="25";
		String tmGroup="1605;1606;";
		String tmGroup2="0301;0301,0306;0305;0306;0501;1605;1606;";
		
		String result=processTmGroup(tmType, tmGroup, tmGroup2);
		
		System.out.println(result);
		
//		String status="LIVE/REGISTRATION/Issued and Active 注册 ";
//		
//		status=status.trim();
//		
//		int pos=status.lastIndexOf(" ");
//		
//		if (pos>-1){
//		
//			int len=status.length();
//			
//			String s=status.substring(pos+1, len);
//			
//			System.out.println(s);
//		}
//		
		
		
//		String tasklists="12,13,14,15,16,0,18";
//		
//		List<String> taskList = Arrays.asList(tasklists.split(","));
//		
//		String[] b = taskList.toArray(new String[taskList.size()]);
//		
//		String taskIds= String.join(",", b);			
//	
//		System.out.println("taskIds="+taskIds);
//		
//		
//		String caseId="2,3,4,5,6,7,8";
//		
//		String taskId="12,13,14,15,16,0,18";
//				
//		
//		int count=0;
//		StringTokenizer idtok = new StringTokenizer(caseId, ",");
//		
//		StringTokenizer idtok2 = new StringTokenizer(taskId, ",");
//		while (idtok.hasMoreTokens()) {
//			String value = idtok.nextToken();
////			String result= idtok.nextToken(",");
//			System.out.println(value);
//			int taskCount=0;
//			String taskIdValue = null;
//			while (idtok2.hasMoreTokens()) {
//				taskIdValue = idtok2.nextToken();				
//				break;				
//			}
//
//			System.out.println("tasIdValue="+taskIdValue);
//			count++;
//		}
		
		
//		 ArrayList list = new ArrayList();
//	    list.add("1");
//	    list.add("10");
//	    list.add("22");
//	    list.add("33");
//	    list.add("41");
//	    list.add("45");	    
//	    listSort(list);
	    
//	    System.out.println(list.toString());
		
//		String baifenbi=getPercent(29,59, false);
//		System.out.println(baifenbi);
//		
//		baifenbi=getPercent(29,59, true);
//		System.out.println(baifenbi);
		
//		String currentTask="录入形式审查结果官文";
//		
//		currentTask=currentTask.replaceAll("形式审查结果", "形审");
//		
//		System.out.println(currentTask);
		 
		 
	}


}
