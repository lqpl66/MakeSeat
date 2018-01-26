package com.yl.Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

public class InitUtils {
	private static long num = 0;
	private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	private static DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
	private static Calendar time = Calendar.getInstance();

	/**
	 * 注册用户的数据初始化，默认数据
	 * @param userName
	 * @return
	 */
//	public static Userinfo getinitUser(String userName) {
//		Userinfo userinfo = new Userinfo();
//		// df.parse(df.format((Date) time.getTime()));
//		userinfo.setCreateTime(df.format((Date) time.getTime()));
//		userinfo.setLeavel(1);
//		userinfo.setNickName(userName);
//		userinfo.setUserName(userName);
//		return userinfo;
//	}

	/**
	 * 随机生成UUID
	 * 
	 * @return
	 */
	public static synchronized String getUUID() {
		UUID uuid = UUID.randomUUID();
		String str = uuid.toString();
		String uuidStr = str.replace("-", "");
		return uuidStr;
	}

	/**
	 * 根据字符串生成固定UUID
	 * 
	 * @param name
	 * @return
	 */
	public static synchronized String getUUID(String name) {
		UUID uuid = UUID.nameUUIDFromBytes(name.getBytes());
		String str = uuid.toString();
		String uuidStr = str.replace("-", "");
		return uuidStr;
	}

	/**
	 * 根据日期生成长整型id
	 * 
	 * @param args
	 */
	// public static synchronized long getLongId() {
	// String date = DateUtil.getDate2FormatString(new Date(),
	// "yyyyMMddHHmmssS");
	// System.out.println("原始id=" + date);
	// if (num >= 99)
	// num = 0l;
	// ++num;
	// if (num < 10) {
	// date = date + 00 + num;
	// } else {
	// date += num;
	// }
	// return Long.valueOf(date);
	// }
}
