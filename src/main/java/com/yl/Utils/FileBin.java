package com.yl.Utils;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/*
 * @author  lqpl66
 * @date 创建时间：2017年4月26日 下午6:54:40 
 * @function     
 */
public class FileBin {
	private final static int OFFSET = 538309;
	private static DateFormat df = new SimpleDateFormat("yyMMddHHmm");
	private static byte[] m_datapadding = { 0x00 };

	public static void main(String args[]) throws IOException {
		// dev_lease_v2017
		// dev_serial_v2016
		 InputStream is = new FileInputStream("D:/doc_file.bin");
		// InputStream is = new FileInputStream("D:/dev_lease_v2017-1.bin");
//		InputStream is = new FileInputStream("D:/dev_serial_v2017.bin");
		byte[] b = new byte[is.available()];
		is.read(b);
		is.close();
		String s = byte2hex(b);
		getDoc_fileList(s);
//		String ss = getDev_serialStr(s);
//		String sss = getAuthorizationStr(s);
//		System.out.println("ss:" + s);
//	    byte[] b = new byte[4];  
//	    is.read(b);  
		String fileName = "D:/dev_lease_v2017.bin";
		FileOutputStream out = new FileOutputStream(fileName);
//		 long key0 = 0x0298;//序列号
//		 long key1 = 0x2030;//可以随机生成
//		 long key2 = 0x2083;//可以随机生成
//		 long time0=0x01E0; //授权时间长
//		 long time1=0x300; //授权上位机日期时间
		
//		 long key0 =  bytesToInt(b,0);//序列号
		 long key0 =  5025;
		 long key1 = 8733;//可以随机生成
		 long key2 = 4212;//可以随机生成
		 long time0=262800; //授权时间长
		 long time1=20170519; //授权上位机日期时间
		
		
		long invalue = key0 ^ key1;
		byte[] b1 = new byte[4];
		b1[0] = (byte) (invalue & 0xff);
		b1[1] = (byte) ((invalue >> 8) & 0xff);
		b1[2] = (byte) ((invalue >> 16) & 0xff);
		b1[3] = (byte) ((invalue >> 24) & 0xff);
		out.write(b1);
		System.out.println("________________");
		System.out.println(invalue);
		invalue = time0 ^ key0;
		b1[0] = (byte) (invalue & 0xff);
		b1[1] = (byte) ((invalue >> 8) & 0xff);
		b1[2] = (byte) ((invalue >> 16) & 0xff);
		b1[3] = (byte) ((invalue >> 24) & 0xff);
		out.write(b1);
		System.out.println(invalue);
		invalue = time1 ^ key2;
		b1[0] = (byte) (invalue & 0xff);
		b1[1] = (byte) ((invalue >> 8) & 0xff);
		b1[2] = (byte) ((invalue >> 16) & 0xff);
		b1[3] = (byte) ((invalue >> 24) & 0xff);
		out.write(b1);
		System.out.println(invalue);
		invalue = key1;
		b1[0] = (byte) (invalue & 0xff);
		b1[1] = (byte) ((invalue >> 8) & 0xff);
		b1[2] = (byte) ((invalue >> 16) & 0xff);
		b1[3] = (byte) ((invalue >> 24) & 0xff);
		out.write(b1);
		System.out.println(invalue);
		invalue = key2;
		b1[0] = (byte) (invalue & 0xff);
		b1[1] = (byte) ((invalue >> 8) & 0xff);
		b1[2] = (byte) ((invalue >> 16) & 0xff);
		b1[3] = (byte) ((invalue >> 24) & 0xff);
		out.write(b1);
		System.out.println(invalue);
//
//		System.out.println("最大：" + Integer.MAX_VALUE);
	}
    public static int bytesToInt(byte[] src, int offset) {  
        int value;    
        value = (int) ((src[offset] & 0xFF)   
                | ((src[offset+1] & 0xFF)<<8)   
                | ((src[offset+2] & 0xFF)<<16)   
                | ((src[offset+3] & 0xFF)<<24));  
        return value;  
    } 
	private static byte[] getBytes(char[] chars) {
		Charset cs = Charset.forName("ASCII");
		CharBuffer cb = CharBuffer.allocate(chars.length);
		cb.put(chars);
		cb.flip();
		ByteBuffer bb = cs.encode(cb);

		return bb.array();

	}

	/*
	 * 点击次数解析
	 */
	public static JSONArray getDoc_fileList(String s) {
		JSONArray list = new JSONArray();
		String a = "";
		long pre = 0;
		long aft = 0;
		String prev;
		String afte;
		String first, second, third, fourth;
		JSONObject js = new JSONObject();
		for (int i = 0; i < s.length() / 16; i++) {
			a = s.substring(i * 16, (i + 1) * 16);
			first = (a.substring(0, 2));
			second = (a.substring(2, 4));
			third = (a.substring(4, 6));
			fourth = (a.substring(6, 8));
			prev = fourth + third + second + first;
			pre = Long.parseLong(prev, 16);
			first = (a.substring(8, 10));
			second = (a.substring(10, 12));
			third = (a.substring(12, 14));
			fourth = (a.substring(14, 16));
			afte = fourth + third + second + first;
			aft = Long.parseLong(afte, 16);
			if (pre != 0 && aft != 0) {
				js.put("aId", pre);
				js.put("clickNum", aft);
				list.add(js);
			}
		}
		System.out.println(list);
		System.out.println(list.size());
		return list;
	}

	public static JSONArray getDoc_fileList1(String s) {
		JSONArray list = new JSONArray();
		String a = "";
		long pre = 0;
		long aft = 0;
		String prev;
		String afte;
		String first, second, third, fourth;
		JSONObject js = new JSONObject();
		for (int i = 0; i < s.length() / 16; i++) {
			a = s.substring(i * 16, (i + 1) * 16);
			first = (a.substring(0, 2));
			second = (a.substring(2, 4));
			third = (a.substring(4, 6));
			fourth = (a.substring(6, 8));
			prev = fourth + third + second + first;
			pre = Long.parseLong(prev, 16);
			first = (a.substring(8, 10));
			second = (a.substring(10, 12));
			third = (a.substring(12, 14));
			fourth = (a.substring(14, 16));
			afte = fourth + third + second + first;
			aft = Long.parseLong(afte, 16);
			if (pre != 0 && aft != 0) {
				js.put("aId", pre);
				js.put("clickNum", aft);
				list.add(js);
			}
		}
		System.out.println(list);
		return list;
	}

	/*
	 * 获取导览笔编号
	 */
	public static String getDev_serialStr(String s) {
		String str = new String();
		String a = "";
		long pre = 0;
		String prev;
		String first, second, third, fourth;
		a = s.substring(0, 8);
		first = (a.substring(0, 2));
		second = (a.substring(2, 4));
		third = (a.substring(4, 6));
		fourth = (a.substring(6, 8));
		prev = fourth + third + second + first;
		pre = Long.parseLong(prev, 16);
		// System.out.println(pre);
		str = String.valueOf(pre);
		return str;
	}

	public static String reverseString(String str) {
		StringBuffer pre = new StringBuffer(str);
		StringBuffer aft = new StringBuffer();
		aft = pre.reverse();
		return aft.toString();
	}

	public static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1) {
				hs = hs + "0" + stmp;
			} else {
				hs = hs + stmp;
			}
		}
		// System.out.println(hs.length());
		// System.out.println(hs.toUpperCase().length());
		return hs.toUpperCase();
	}

	public static int byte2Int(byte b) {
		int r = (int) b;
		return r;
	}

	/*
	 * // 返回一个8位随机动态码
	 */
	public static String getCode(int num) {
		long seed = System.currentTimeMillis() + OFFSET;
		SecureRandom secureRandom = null; // 安全随机类
		try {
			secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(seed);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		String codeList = "1234567890"; // 动态码取值范围
		String sRand = ""; // 定义一个验证码字符串变量
		for (int i = 0; i < num; i++) {
			int code = ThreadLocalRandom.current().nextInt(codeList.length() - 1);
			String rand = codeList.substring(code, code + 1);
			sRand += rand;
		}
		return sRand;
	}

	/*
	 * 字符翻转
	 */
	public static String getFlip_Str(String s) {
		// String a = "";
		// String first, second, third, fourth;
		// a = s.substring(0, 8);
		// first = (a.substring(0, 2));
		// second = (a.substring(2, 4));
		// third = (a.substring(4, 6));
		// fourth = (a.substring(6, 8));
		// flip_Str = fourth + third + second + first;
		String hex = "";

		if (s.length() % 2 == 1) {
			s = "0" + s;
		}
		for (int i = 0; i < s.length() / 2; i++) {
			String h = s.substring(i * 2, i * 2 + 2);
			hex = h + hex;
		}
		// long dec = Long.parseLong(hex, 16);
		// hex = Long.toString(dec);
		return hex;
	}

	/*
	 * 补全8位
	 */
	public static String getCompleteStr(String s) {
		String completeStr = "";
		String c_Str = "";
		if (s.length() < 8) {
			for (int i = 0; i < 8 - s.length(); i++) {
				c_Str += "0";
			}
		}
		completeStr = s + c_Str;
		return completeStr;
	}

	/*
	 * 获取授权文件
	 */
	public static String getAuthorizationStr(String s) {
		String flip_Str = "";
		String a = "";
		String prev;
		String first, second, third, fourth;
		a = s.substring(0, 8);
		System.out.println(a);
		first = (a.substring(0, 2));
		second = (a.substring(2, 4));
		third = (a.substring(4, 6));
		fourth = (a.substring(6, 8));
		prev = fourth + third + second + first;
		long pre = Long.parseLong(prev, 16);// 导览笔编号
		long dynamicCode1 = Long.parseLong(getCode(4));// 12-15动态码
		long dynamicCode2 = Long.parseLong(getCode(4));// 16-19动态码
		long pre1 = pre ^ dynamicCode1;
		long pre2 = Long.parseLong("480") ^ pre;
		long pre3 = Long.parseLong(df.format(new Date())) ^ dynamicCode2;
		System.out.println(Long.toString(pre1) + ":" + Long.toString(pre2) + ":" + Long.toString(pre3) + ":"
				+ Long.toString(dynamicCode1) + ":" + Long.toString(dynamicCode2));
		String flip_Str1 = getCompleteStr(getFlip_Str(Long.toHexString(pre1)))
				+ getCompleteStr(getFlip_Str(Long.toHexString(pre2)))
				+ getCompleteStr(getFlip_Str(Long.toHexString(pre3)))
				+ getCompleteStr(getFlip_Str(Long.toHexString(dynamicCode1)))
				+ getCompleteStr(getFlip_Str(Long.toHexString(dynamicCode2)));
		// String flip_Str1 = getCompleteStr((Long.toHexString(pre1)))
		// + getCompleteStr((Long.toHexString(pre2)))
		// + getCompleteStr((Long.toHexString(pre3)))
		// + getCompleteStr((Long.toHexString(dynamicCode1)))
		// + getCompleteStr((Long.toHexString(dynamicCode2)));
		// String flip_Str1 = Long.toString(pre1) + Long.toString(pre2) +
		// Long.toString(pre3) + Long.toString(dynamicCode1)
		// + Long.toString(dynamicCode2);
		System.out.println(pre);
		System.out.println(pre2);
		System.out.println(getCompleteStr(getFlip_Str(Long.toHexString(pre2))));
		System.out.println(pre1 ^ dynamicCode1);
		// String flip_Str1 = Long.toBinaryString(663);
		// String flip_Str1 = "663";
		return flip_Str1.toUpperCase();
	}

	public static String decToHex(long dec) {
		String hex = "";
		while (dec != 0) {
			String h = Long.toString(dec & 0xff, 16);
			if ((h.length() & 0x01) == 1)
				h = '0' + h;
			hex = hex + h;
			dec = dec >> 8;
		}
		return hex;
	}

	public static String decToHex1(String s) {
		String hex = "";
		for (int i = 0; i < s.length() / 2; i++) {
			String h = s.substring(i * 2, i * 2 + 2);
			hex = h + hex;
		}
		long dec = Long.parseLong(hex, 16);
		hex = Long.toString(dec);
		return hex;
	}

	/**
	 * bytes字符串转换为Byte值
	 * 
	 * @param String
	 *            src Byte字符串，每个Byte之间没有分隔符
	 * @return byte[]
	 */
	public static byte[] hexStr2Bytes(String src) {
		int m = 0, n = 0;
		int l = src.length() / 2;
		System.out.println(l);
		byte[] ret = new byte[l];
		for (int i = 0; i < l; i++) {
			m = i * 2 + 1;
			n = m + 1;
			ret[i] = Byte.decode("0x" + src.substring(i * 2, m) + src.substring(m, n));
		}
		return ret;
	}

	/**
	 * String的字符串转换成unicode的String
	 * 
	 * @param String
	 *            strText 全角字符串
	 * @return String 每个unicode之间无分隔符
	 * @throws Exception
	 */
	public static String strToUnicode(String strText) throws Exception {
		char c;
		StringBuilder str = new StringBuilder();
		int intAsc;
		String strHex;
		for (int i = 0; i < strText.length(); i++) {
			c = strText.charAt(i);
			intAsc = (int) c;
			strHex = Integer.toHexString(intAsc);
			if (intAsc > 128)
				str.append("\\u" + strHex);
			else
				// 低位在前面补00
				str.append("\\u00" + strHex);
		}
		return str.toString();
	}
}
