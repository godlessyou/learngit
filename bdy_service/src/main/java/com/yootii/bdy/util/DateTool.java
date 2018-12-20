package com.yootii.bdy.util;


import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;


public class DateTool {
	
	public static Date getDateAfterYear(Date d, int year) {
		Calendar cd = Calendar.getInstance();
		cd.setTime(d);
		cd.add(Calendar.YEAR, year);// 增加一年
		return cd.getTime();
	}

	public static Date getDateBeforeMonth(Date d, int month) {
		Calendar cd = Calendar.getInstance();
		cd.setTime(d);
		cd.add(Calendar.MONTH, -month);// 减少一月
		return cd.getTime();
	}

	public static Date getDateAfterMonth(Date d, int month) {
		Calendar cd = Calendar.getInstance();
		cd.setTime(d);
		cd.add(Calendar.MONTH, month);// 增加一月
		return cd.getTime();
	}

	/*public static Date getDateAfterYear(Date d,int year){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(d);
		calendar.add(Calendar.YEAR, year);
		return calendar.getTime();
	}*/
	
	public static Date getDateBefore(Date d, int day) {
		Calendar cd = Calendar.getInstance();
		cd.setTime(d);
		cd.set(Calendar.DATE, cd.get(Calendar.DATE) - day);
		return cd.getTime();
	}

	public static Date getDateAfter(Date d, int day) {
		Calendar cd = Calendar.getInstance();
		cd.setTime(d);
		cd.set(Calendar.DATE, cd.get(Calendar.DATE) + day);
		return cd.getTime();
	}

	public static Date StringToDate(String dateString) {
		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			date = sdf.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	public static Date StringToDateTime(String dateString) {
		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			date = sdf.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static String getEnglishDate(Date date) {

		DateFormat df = new SimpleDateFormat("MMMMM dd, yyyy", Locale.ENGLISH);
		String time = df.format(date);
		return time;
	}
	
	public static String getDate(Date date) {

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String time = df.format(date);
		return time;
	}
	
	public static String getDateTime(Date date) {

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = df.format(date);
		return time;
	}
	

	public static int getYear(String dateString) {		
		Date date=StringToDate(dateString);
		Calendar calender = Calendar.getInstance();  
		calender.setTime(date);  	
	    int year = calender.get(Calendar.YEAR);		  
		return year;
	}
	
	
	public static int getCurrentYear() {	
		Date today = new Date();// 取时间	
		String dateString = DateTool.getDate(today);
		int yearDate = DateTool.getYear(dateString);		
		return yearDate;
	}
	
	public static int getMonth(String dateString) {		
		Date date=StringToDate(dateString);
		Calendar calender = Calendar.getInstance();  
		calender.setTime(date);  	
	    int month = calender.get(Calendar.MONTH);	
	    month++;
		return month;
	}
	
	public static int getCurrentMonth(){
		Date today = new Date();// 取时间	
		String dateString = DateTool.getDate(today);
		int monthDate = DateTool.getMonth(dateString);		
		return monthDate;
	}
	
	public static int getDay(String dateString) {		
		Date date=StringToDate(dateString);
		Calendar calender = Calendar.getInstance();  
		calender.setTime(date);  	
	    int day = calender.get(Calendar.DAY_OF_MONTH);	
		return day;
	}
	
	public static int getCurrentDay(){
		Date today = new Date();// 取时间	
		String dateString = DateTool.getDate(today);
		int day = DateTool.getDay(dateString);		
		return day;
	}
	
	public static final int daysBetween(Date early, Date late) { 
	     
        java.util.Calendar calst = java.util.Calendar.getInstance();   
        java.util.Calendar caled = java.util.Calendar.getInstance();   
        calst.setTime(early);   
         caled.setTime(late);   
         //设置时间为0时   
         calst.set(java.util.Calendar.HOUR_OF_DAY, 0);   
         calst.set(java.util.Calendar.MINUTE, 0);   
         calst.set(java.util.Calendar.SECOND, 0);   
         caled.set(java.util.Calendar.HOUR_OF_DAY, 0);   
         caled.set(java.util.Calendar.MINUTE, 0);   
         caled.set(java.util.Calendar.SECOND, 0);   
        //得到两个日期相差的天数   
         int days = ((int) (caled.getTime().getTime() / 1000) - (int) (calst   
                .getTime().getTime() / 1000)) / 3600 / 24;   
         
        return days;   
   } 
	
	
	public static void test()
    {
         Date earlydate = new Date();   
         Date latedate = new Date();   
         DateFormat df = DateFormat.getDateInstance();   
         try {   
             earlydate = df.parse("2009-09-21");   
             latedate = df.parse("2009-10-16");   
         } catch (ParseException e) {   
               e.printStackTrace();   
           }   
          int days = daysBetween(earlydate,latedate);   
          System.out.println(days);   
    }

	
	public static int processDate(String startDate, String endDate,
			List<Integer> times) {
		int startTime = 0;
		int endTime = 0;
		int currentYear = DateTool.getCurrentYear();

		if (startDate == null || startDate.equals("")) {
			startTime = currentYear - 4;
		} else {
			if (StringUtils.isNum(startDate)) {
				startTime = Integer.parseInt(startDate);
			}
		}
		if (endDate == null || endDate.equals("")) {
			endTime = currentYear;
		} else {
			if (StringUtils.isNum(startDate)) {
				endTime = Integer.parseInt(endDate);
			}
		}
		if (endTime < startTime) {
			endTime = startTime;
		}

		times.add(startTime);
		times.add(endTime);

		return currentYear;
	}

	public static void main(String args[]) {
		
		test();

//		String dateString = "2016-01-01";
//		Date date = StringToDate(dateString);
//		String result = getDate(date);
//		System.out.println(result + "\r\n");
//		
//		int year = getYear(dateString);
//		System.out.println(year);
//
//		// 减12个月
//		Date dateBefore = getDateBeforeMonth(date, 12);
//		// 加一天
//		dateBefore = getDateAfter(dateBefore, 1);
//		result = getEnglishDate(dateBefore);
//		System.out.println(result + "\r\n");
//
//		// 加6个月
//		Date dateAfter = getDateAfterMonth(date, 6);
//		result = getEnglishDate(dateAfter);
//		System.out.println(result + "\r\n");

	}

}

