package com.yl.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Pattern;
import java.text.DecimalFormat;
import java.util.regex.Matcher;



/**
 * �ַ�����
 * 
 * @author YanXue
 * @version 1.0 Create at 2012-07-16
 * 
 */
public class StringUtil {
	public static final String isMobilePhoneFormat = "^1[3|4|5|8][0-9]{9}$";//�ֻ��������
	public static final String isTelePhoneFormat = "^((0[0-9]{2,3})-)([0-9]{7,8})(-([0-9]{3,}))?$";//�绰��������
	
	
	public static String buildSubCode(String no,String colorCode,String sizeCode,String braCode){
		return StringUtil.trimString(no)+StringUtil.trimString(colorCode)
				+StringUtil.trimString(sizeCode)+StringUtil.trimString(braCode);
	}
	
	public static String trimString(Object obj){
		if (obj==null){
			return "";
		}
		String result =String.valueOf(obj);
		return result ;
	}
	public static String trimStrZero(Object obj){
		if (obj==null||obj==""){
			return "0";
		}
		String result =String.valueOf(obj);
		return result ;
	}
	public static String trimStrNum(Object obj,String n){
		if (obj==null||obj==""){
			return n;
		}
		String result =String.valueOf(obj);
		return result ;
	}
	/**
	 * ��ֹ���ֿ�ָ���쳣
	 * @param name �ַ�
	 * @return	������ַ�
	 * @throws Exception
	 */
	public static String trimString(String name){
		if(name != null){
			name = name.trim();
		}else{
			name = "";
		}
		return name;
	}
	
	/**
	 * ��ֹ���ֿ�ָ���쳣
	 * @param name �ַ�
	 * @return	������ַ�
	 * @throws Exception
	 */
	public static String trimString(int value){
		return String.valueOf(value);
	}
	
	/**
	 * ��ֹ���ֿ�ָ���쳣
	 * @param value ����
	 * @return	������ַ�
	 * @throws Exception
	 */
	public static String trimString(Date value){
		String result="";
		if(value != null){
			result = value.toString();
		}else{
			result = "";
		}
		return result;
	}
	
	/**
	* ��jquery ajax �����ַ� ����
	* @param str ��Ҫ������ַ�
	* @return
	*/
	public static String decodeJqueryAjaxStr(String str){
		if(str==null || str.equals("")){
			return "";
		}
		str = str.trim();
		try {
			str = java.net.URLDecoder.decode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}
	
	/**
	* ��jquery ajax �����ַ� ����
	* @param str ��Ҫ������ַ�
	* @return
	*/
	public static String decodeJqueryAjaxString(String str){
		if(str==null || str.equals("")){
			return "";
		}
		str = str.trim();
		try {
			str = java.net.URLDecoder.decode(java.net.URLDecoder.decode(str, "UTF-8"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}

/*	public static void main(String[] args)
			throws BadHanyuPinyinOutputFormatCombination {
		
	}*/
	
	
	/**
	 * float����ȡ��С������λ
	 * @param fo
	 * @return
	 */
	public static String floatRtTwo(float money){
		try {
			if(Float.isNaN(money)){
				return "0.0";
			}
			//��ѧ����ת������ͨ�㷨
			BigDecimal db = new BigDecimal(money);
			String Profit = String.valueOf(db.toPlainString());
			if(Profit.indexOf(".") != -1){
				 String point = Profit.substring(Profit.indexOf(".")+1,Profit.length());
				 if(point.length()>2){
					 Profit = Profit.substring(0,Profit.indexOf(".")+3);
				 }
			 }
			return Profit;
		} catch (RuntimeException e) {
			e.printStackTrace();
			return "0.0";
		}
	}
	/**
	 * float����ȡ��С������λ
	 * @param fo
	 * @return
	 */
	public static String floatRtTwoProduct(float fo){
		try {
			if(Float.isNaN(fo)){
				return "0.0";
			}
			String Profit = String.valueOf(fo);
			if(Profit.indexOf(".") != -1){
				 String point = Profit.substring(Profit.indexOf(".")+1,Profit.length());
				 if(point.length()>2){
					 Profit = Profit.substring(0,Profit.indexOf(".")+3);
				 }
			 }
			return Profit;
		} catch (RuntimeException e) {
			e.printStackTrace();
			return "0.0";
		}
	}
	
	/**
	 * float����ȡ��С������λ
	 * @param fo
	 * @return
	 */
	public static String integerRtTwo(int fo){
		try {
			String Profit = String.valueOf(fo);
			if(Profit.indexOf(".") != -1){
				 String point = Profit.substring(Profit.indexOf(".")+1,Profit.length());
				 if(point.length()>2){
					 Profit = Profit.substring(0,Profit.indexOf(".")+3);
				 }
			 }
			return Profit;
		} catch (RuntimeException e) {
			e.printStackTrace();
			return "0.0";
		}
	}
	
	/**
	 * double����ȡ��С������λ
	 * @param fo
	 * @return
	 */
	public static String doubleRtTwo(double doubleValue){
		try {
			if(Double.isNaN(doubleValue)){
				return "0.0";
			}
			//��ѧ����ת������ͨ�㷨
			return String.format("%.2f", doubleValue);
		} catch (RuntimeException e) {
			e.printStackTrace();
			return "0.0";
		}
	}
	/**
	 * double����ȡ��С������λ
	 * @param fo
	 * @return
	 */
	public static double double2Two(double doubleValue){
		try {
			if(Double.isNaN(doubleValue)){
				return 0.0;
			}
			//��ѧ����ת������ͨ�㷨
			return Double.parseDouble(String.format("%.2f", doubleValue));
		} catch (RuntimeException e) {
			return 0.0;
		}
	}
	/**
	 * Double��������������ת��2λС��
	 * ͬʱȥ���ѧ�����Ӱ��
	 * @param val ��ֵ, type ����  0(89123456.79)  1(89,123,456.79)
	 * @add YanXue
	 * @date 2014-05-29
	 * @return ��ʽ������ֵ
	 * */
	public static String doubleFixed(double val , int type){
		String returnValue = "";
		if(type == 1){
			NumberFormat nf = NumberFormat.getNumberInstance();
			nf.setMaximumFractionDigits(2);
			returnValue =  nf.format(val);
		}else{
			returnValue = String.format("%.2f", val);
		}
		return returnValue;
	}

	public static String stringToNull(String amount){
		if(amount != null && "0".equals(amount)){
			return "";
		}else{
			return amount;
		}
	}
	
	/**
	 * ��ȡһ�����ַ�
	 * @param fo
	 * @return
	 */
	public static String subLengthString(String value,int len){
		if(value != null && !value.equals("")){
			value = value.trim();
			if(value.length() > len){
				value = value.substring(0,len) + "...";
			}
		}else{
			value = "";
		}
		return value;
	} 
	
	public static String translateBigNumber(int key){
		String BigNumber ="";
		switch (key) {
		case 1:
			BigNumber ="һ";
			break;
		case 2:
			BigNumber ="��";
			break;
		case 3:
			BigNumber ="��";
			break;
		case 4:
			BigNumber ="��";
			break;
		case 5:
			BigNumber ="��";
			break;
		case 6:
			BigNumber ="��";
			break;
		case 7:
			BigNumber ="��";
			break;
		case 8:
			BigNumber ="��";
			break;
		case 9:
			BigNumber ="��";
			break;
		case 10:
			BigNumber ="ʮ";
			break;
		case 11:
			BigNumber ="ʮһ";
			break;
		case 12:
			BigNumber ="ʮ��";
			break;
		default:
			break;
		}
		return BigNumber;
		
	}
	public static String translateNumberToEnglish(int key){
		String EnglishNumber ="";
		switch (key) {
		case 1:
			EnglishNumber ="One";
			break;
		case 2:
			EnglishNumber ="Two";
			break;
		case 3:
			EnglishNumber ="Three";
			break;
		case 4:
			EnglishNumber ="Four";
			break;
		case 5:
			EnglishNumber ="Five";
			break;
		case 6:
			EnglishNumber ="Six";
			break;
		case 7:
			EnglishNumber ="Seven";
			break;
		case 8:
			EnglishNumber ="Eight";
			break;
		case 9:
			EnglishNumber ="Nine";
			break;
		case 10:
			EnglishNumber ="Ten";
			break;
		case 11:
			EnglishNumber ="Eleven";
			break;
		case 12:
			EnglishNumber ="Twelve";
			break;
		default:
			break;
		}
		return EnglishNumber;
	}
	
	public static String parseBlobToString(Blob blob){
		InputStream read = null; 
		try {
			read = blob.getBinaryStream();
			int flength = (int) blob.length();
			byte[] b = new byte[flength];
			byte[] nb = new byte[1024];
			int len = 0;
			int tlen = 0;
			while(flength>0){
				len = read.read(nb);
				System.arraycopy(nb, 0, b, tlen, len);
				tlen += len;
				flength -= len;
			}
			return new String(b,"UTF-8");
		} catch (Exception e) {
			return "";
		}finally{
			if(read != null)
			try {
				read.close();
			} catch (IOException e) {
			}
		}
	}
	
	/***********************2014-09-01 Add By zhanglei Start*************************/
	public static String object2String(Object o)
	{
		if (o == null){
			return null;
		}
		return o.toString();
	}

	/**
	 * �ַ��Ƿ�Ϊ��
	 * @param str
	 */
	public static boolean isEmpty(String str)
	{
		return str == null || str.trim().length() == 0;
	}

	/**
	 * �ַ��Ƿ�ǿ�
	 */
	public static boolean isNotEmpty(String str)
	{
		return !isEmpty(str);
	}

	public static boolean equalsIgnoreCase(String str1, String str2)
	{
		boolean result = false;

		if (str1 == null && str2 == null)
		{
			result = true;
		}
		if (str1 != null && str2 != null && str1.trim().equalsIgnoreCase(str2.trim()))
		{
			result = true;
		}
		return result;

	}
	
	public static boolean equals(String str1, String str2)
	{
		boolean result = false;

		if (str1 == null && str2 == null)
		{
			result = true;
		}
		if (str1 != null && str2 != null && str1.trim().equals(str2.trim()))
		{
			result = true;
		}
		return result;

	}
	
	public static boolean equals(Integer str1, Integer str2)
	{
		boolean result = false;

		if (str1 == null && str2 == null)
		{
			result = true;
		}
		if (str1 != null && str2 != null && str1.intValue()==(str2.intValue()))
		{
			result = true;
		}
		return result;

	}

	public static boolean equalsIgnoreCase(Object str1, Object str2)
	{
		return equalsIgnoreCase((str1 == null ? null : str1.toString()), (str2 == null ? null : str2.toString()));
	}

	/**
	 * @param content
	 * @param max ��30
	 */
	public static String getNumbersForShort(String content, int max)
	{
		if (StringUtil.isEmpty(content)){
			return "";
		}
		content = content.replaceAll("[\t\n\r\f]", "");
		return content.length() > max ? content.subSequence(0, max) + "..." : content; // description
	}

	/**
	 * @param value 
	 * @param pattern ��ʽ���磺"##,###.0"
	 * @return �����ʾ��ʽ��
	 */
	public static String getDecimalFormat(Object value, String pattern)
	{
		String result = null;
		DecimalFormat myformat = new DecimalFormat();
		myformat.applyPattern(pattern);//����:"##,###.0"
		try
		{
			value = value.toString().replaceAll(",", "");
			result = myformat.format(Double.parseDouble(value.toString()));
		}
		catch (Exception e)
		{
			return value == null ? "-" : value.toString();
		}
		if (result != null && result.toString().startsWith("."))
		{
			result = "0" + result;
		}
		if (result != null && result.toString().startsWith("-."))
		{
			result = result.replaceFirst("-.", "-0.");
		}
		return result;
	}

	/**
	 * String��һ���ַ��д
	 * @param str
	 */
	public static String firstLetterUpperCase(String str)
	{
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	/**
	 * �ж��ַ��Ƿ�Ϊ����
	 * @param strName
	 */
	public static final boolean isChinese(String strName)
	{
		if (StringUtil.isEmpty(strName))
			return false;
		char[] ch = strName.toCharArray();
		for (int i = 0; i < ch.length; i++)
		{
			char c = ch[i];
			if (isChinese(c))
			{
				return true;
			}
		}
		return false;
	}
	
	public static final boolean isChinese(char c)
	{
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS)
		{
			return true;
		}
		return false;
	}

	/**
	 * ��֤���ʼ���ַ�Ƿ���ȷ
	 * @param email
	 */
	public static boolean validateEmailAddress(String email)
	{
		if (StringUtil.isNotEmpty(email))
		{
			if (email.indexOf("@") > 0)
			{
				String realemail = email.trim();
				final int start = email.lastIndexOf("<");
				final int end = email.lastIndexOf(">");
				if(end>0&&!realemail.endsWith(">"))
				{
					return false;
				}
				if (start >= 0 && end >= 0 && start < end)
				{
					realemail = email.substring(start + 1, end);
				}
				realemail = realemail.trim();
				String subStringByPattern = StringUtil.getSubStringByPattern(realemail, "[\\w\\-\\.]{1,}[@][\\w\\-]{1,}([\\.]([\\w\\-]{1,})){1,3}");
				return StringUtil.isNotEmpty(subStringByPattern) && realemail.equals(subStringByPattern);
			}
		}
		return false;
	}
	
	public static String getSubStringByPattern(String str, String pattern)
	{
		String result = null;
		try
		{
			Pattern p = Pattern.compile(pattern);
			Matcher m = p.matcher(str);
			while (m.find())
			{
				result = m.group();
				break;
			}
		}
		catch (Exception e)
		{
		}
		return result;
	}
	
	/**
	 * ���˷Ƿ��ַ�,XSS�ű�ע��
	 * */
	public static String filterContent(String content){
		content=content.replaceAll("<","");
		content=content.replaceAll(">","");
		content=content.replaceAll("javascript:","");
		content=content.replaceAll("jscript:","");
		content=content.replaceAll("vbscript:","");
		content=content.replaceAll("&","");
		content=content.replaceAll("\"","");
		return content;
	}
	
	/**
	 * �ж��ǲ����ֻ����
	 * @param  mobilePhone
	 * */
	public static boolean isMobile(String mobilePhone) {
        return Pattern.matches(isMobilePhoneFormat, mobilePhone);
    }
	
	/**
	 * �ж��ǲ��ǵ绰����
	 * @param  telePhone
	 * */
	public static boolean isTelePhone(String telePhone) {
        return Pattern.matches(isTelePhoneFormat, telePhone);
    }

	/***********************2014-09-01 Add By zhanglei End***************************/
	
	/**
	 * �滻�Ƿ��ַ�
	 * add miaosai
	 * @date 2014-09-30
	 */
	public static String filterIllegalChar(String content) {
		if (content == null || content.length() == 0) {
			return "";
		}
		content = content.replace("\\", "\\\\");
		content = content.replace("'", "\\'");
		content = content.replace("\"", "\\\"");
		return content;
	}
	
	/**
	 * �ַ��ʽ��
	 * @param pattern  �ַ�ģ��,ռλ��{0}{1}{2}...
	 * @param args ����
	 * @add miaosai 
	 * @date 2015-08-02
	 */
	public static String StringFormat(String pattern,Object[] args){
		MessageFormat format = new MessageFormat(pattern);
		return format.format(args);
	}
	/**
	 * ������λ��
	 * @param total
	 * @return
	 * @author zgt
	 * @date 2015-9-10
	 */
	public static String  randomNumber(int total) {
   	    Random rd=new Random();
   	    StringBuffer sixNumber=new StringBuffer();
        for(int i=0;i<total;i++){
        	int j=rd.nextInt(10);
        	sixNumber.append(j);
        }
        return sixNumber.toString();
	}
	/**
	 * ����n������ظ��������
	 * @param number��ǰ����������
	 * @param numberMap������
	 * @param randomCount���ٸ�������
	 * @return
	 * @author zgt 
	 * @date 2015-10-14
	 */
	public static String volidNumber(String number,Map<String, String> numberMap,int randomCount){
		if( numberMap.get(number) !=null){
			number=randomNumber(randomCount);
			volidNumber(number,numberMap,randomCount);

		}else{
			numberMap.put(number, "");
		}

		return number;
	}
 
	public static void main(String[] args) {
//		String divertName="TSDZQ"+DateUtil.getCurrentTime("YYYYMMddHHmmss")+StringUtil.randomNumber(4);
//	    System.out.println(divertName);

//		Map<String, String> numberMap=new HashMap<String, String>();
//		int randomCount=2;
//		for(int i=0;i<10;i++){
//			String randomNumber=randomNumber(randomCount);
//			volidNumber(randomNumber,numberMap,randomCount);
//		}
//		System.out.println("O");
		 /*for (Map.Entry<String, String> map: numberMap.entrySet()) {
            System.out.println(map.getKey());
		  }*/
//		StringBuffer sb=new StringBuffer();
//		sb.append("JCK").append("1394").append("002").append("002").append(new SimpleDateFormat("yyMMddHHmmss").format(new Date()));
//		System.out.println(sb.toString());
	}
	
	 /**
	  * �س�����һ���հױ�ʾ
	  * @param str
	  * @return
	  */
	 public static String replaceEnter(String str) {
	        String dest = "";
	        if (str!=null && str!="" ) {
	            //Pattern p = Pattern.compile("\\s*|\t|\r|\n");
	        	Pattern p = Pattern.compile("\r|\n|\r\n");
	            Matcher m = p.matcher(str);
	            dest = m.replaceAll(" ");
	        }else{
	        	dest=str;
	        }
	        return dest;
	 }
	 public static int subCounter(String str1, String str2) {
		    int counter = 0;
		    for (int i = 0; i <= str1.length() - str2.length(); i++) {
		      if (str1.substring(i, i + str2.length()).equalsIgnoreCase(str2)) {
		        counter++;
		      }
		    }
//		    System.out.println("子字符串的个数为： " + counter);
		    return counter;
	 }
		//****************add by zgt 2015-8-19  end***************************/
}
