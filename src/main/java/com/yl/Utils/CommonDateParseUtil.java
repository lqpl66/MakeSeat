package com.yl.Utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonDateParseUtil {
	public static final String ENG_DATE_FROMAT = "EEE, d MMM yyyy HH:mm:ss z";
	public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
	public static final String YYYY_MM_DD = "yyyy-MM-dd";
	public static final String YYYY = "yyyy";
	public static final String MM = "MM";
	public static final String DD = "dd";
	public static final String HHMM = "HH:mm";

	public static Date date2date(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
		String str = sdf.format(date);
		try {
			date = sdf.parse(str);
		} catch (Exception e) {
			return null;
		}
		return date;
	}

	/**
	 * @param date
	 * @return
	 * @描述 —— 时间对象转换成字符串
	 */
	public static String date2string(Date date, String formatStr) {
		String strDate = "";
		SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
		strDate = sdf.format(date);
		return strDate;
	}

	/**
	 * @param date
	 * @return
	 * @描述 —— 时间对象转换成字符串
	 */
	public static String date2string(Date date) {
		String strDate = "";
		SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
		strDate = sdf.format(date);
		return strDate;
	}

	public static String birthTostring(Date date) {
		String strDate = "";
		SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD);
		strDate = sdf.format(date);
		return strDate;
	}

	/**
	 * @param date
	 * @return
	 * @描述 —— sql时间对象转换成字符串
	 */
	public static String timestamp2string(Timestamp timestamp, String formatStr) {
		String strDate = "";
		SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
		strDate = sdf.format(timestamp);
		return strDate;
	}

	/**
	 * @param dateString
	 * @param formatStr
	 * @return
	 * @描述 —— 字符串转换成时间对象
	 */
	public static Date string2date(String dateString) {
		Date formateDate = null;
		DateFormat format = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
		try {
			formateDate = format.parse(dateString);
		} catch (ParseException e) {
			return null;
		}
		return formateDate;
	}

	/**
	 * @param date
	 * @return
	 * @描述 —— Date类型转换为Timestamp类型
	 */
	public static Timestamp date2timestamp(Date date) {
		if (date == null)
			return null;
		return new Timestamp(date.getTime());
	}

	/**
	 * @param dateString
	 * @param formatStr
	 * @return
	 * @描述 —— 字符串转换成时间对象
	 */
	public static Date string2day(String dateString) {
		Date formateDate = null;
		DateFormat format = new SimpleDateFormat(YYYY_MM_DD);
		try {
			formateDate = format.parse(dateString);
		} catch (ParseException e) {
			return null;
		}
		return formateDate;
	}

	public static String hhmmTostring(Date date) {
		String strDate = "";
		SimpleDateFormat sdf = new SimpleDateFormat(HHMM);
		strDate = sdf.format(date);
		return strDate;
	}

	public static Date string2hhmm(String dateString) {
		Date formateDate = null;
		DateFormat format = new SimpleDateFormat(HHMM);
		try {
			formateDate = format.parse(dateString);
		} catch (ParseException e) {
			return null;
		}
		return formateDate;
	}
}
