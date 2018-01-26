package com.yl.Utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ConstantUtil {
	
	
    public static void main(String[] args) throws ParseException
    {
//       Calendar cal = Calendar.getInstance();
//       int day = cal.get(Calendar.DATE);       //日
//       int month = cal.get(Calendar.MONTH) + 1;//月
//       int year = cal.get(Calendar.YEAR);      //年
//
//       System.out.println("Date: " + cal.getTime());
//       System.out.println("Day: " + day);
//       System.out.println("Month: " + month);
//       System.out.println("Year: " + year);
    	
//    	  Calendar c = Calendar.getInstance();
//
//          c.set(2016,5,4);
//          Date before =c.getTime();
//
//          c.set(2016,5,5);
//          Date now=c.getTime();
//
//          c.set(2016,5,6);
//          Date after=c.getTime();
//
//          //before早于now，返回负数，可用于判断活动开始时间是否到了
//          int compareToBefore=before.compareTo(now);
//          System.out.println("compareToBefore = "+compareToBefore);
//
//          int compareToIntNow=now.compareTo(now);
//          System.out.println("compareToIntNow = "+compareToIntNow);
//
//          //after晚于now，返回正数，可用于判断活动结束时间是否到了
//          int compareToIntAfter=after.compareTo(now);
//          System.out.println("compareToIntAfter = "+compareToIntAfter);
    	
    	String DateStr1 = "2014-08-27";
    	String DateStr2 = "2014-08-26";
    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    	Date dateTime1 = dateFormat.parse(DateStr1);
    	Date dateTime2 = dateFormat.parse(DateStr2);
    	int i = dateTime1.compareTo(dateTime2); 
    	System.out.println(i ); 
    	
   }
}
