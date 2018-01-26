package com.yl.Utils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.log4j.Logger;

public class LineUtils {

	private static Logger log = Logger.getLogger(LineUtils.class);
	private static Integer num = 10;

	// 算法计算排队的开始时间
	public static List<String> getarriveStartTime(Integer totalNum, Integer avgTime) {
		List<String>   list = new  ArrayList<String>();
		DateFormat df = new SimpleDateFormat("HH:mm:ss");
		Calendar time = Calendar.getInstance();
		Integer s = totalNum * avgTime +  totalNum * avgTime/num;
		time.add(Calendar.SECOND, s);
		String arriveStartTime = df.format(time.getTime());
		time.add(Calendar.MINUTE, 10);
		String arriveEndTime = df.format(time.getTime());
		list.add(arriveStartTime);
		list.add(arriveEndTime);
		return list;
	}


	public static void main(String[] args) {
		
		List<String> s = LineUtils.getarriveStartTime(30,100);
		System.out.println(s.get(0)+"ddddddddddddd"+s.get(1));
	}

}
