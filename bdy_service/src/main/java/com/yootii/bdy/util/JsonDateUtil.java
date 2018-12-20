
package com.yootii.bdy.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;



public class JsonDateUtil {

	 /**
     * 日期转换成json以后会变成一串数字，如何把数字转换为日期
     */
    public static String dateFormat(Date date) {
//        long unixstamp=1437646938000l;
//        Date dt=new Date(unixstamp);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        String result=sdf.format(date);
        System.out.println(result);
        
        return result;
    }
    
    
    public static void main(String[] args) {
    	Date date=new Date();
    	dateFormat(date);
    }
}
