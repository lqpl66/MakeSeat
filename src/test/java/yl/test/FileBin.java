package yl.test;

import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

/*
 * @author  lqpl66
 * @date 创建时间：2017年4月26日 下午6:54:40 
 * @function     
 */
public class FileBin {
	public static void main(String args[]) throws IOException {
		// dev_lease_v2017
		// dev_serial_v2016
//		 InputStream is = new FileInputStream("D:/doc_file.bin");
		// InputStream is = new FileInputStream("D:/dev_lease_v2017.bin");
		InputStream is = new FileInputStream("D:/dev_serial_v2017.bin");
		byte[] b = new byte[is.available()];
		is.read(b);
		is.close();
//		System.out.println(byte2hex(b));
		String s = byte2hex(b);
		System.out.println(s);
		String a = "";
		long pre = 0;
		 long aft = 0;
//		String pre = "";
//		String aft = "";
		String prev;
		String afte;
		String first, second, third, fourth;
		List<String> l = new ArrayList<String>();
		for (int i = 0; i < s.length() / 16; i++) {
			a = s.substring(i * 16, (i + 1) * 16);
			first = (a.substring(0, 2));
			second = (a.substring(2, 4));
			third = (a.substring(4, 6));
			fourth = (a.substring(6, 8));
			prev = fourth + third + second + first;
			pre = Long.parseLong(prev, 16);
//			pre = Long.parseLong(a.substring(0,8), 16);
			first = (a.substring(8, 10));
			second = (a.substring(10, 12));
			third = (a.substring(12, 14));
			fourth = (a.substring(14, 16));
			afte = fourth + third + second + first;
			aft =Long.parseLong(afte, 16);
//			pre = Long.parseLong(a.substring(8,16), 16);
			System.out.println(i + ":" + a);
			System.out.println(a.substring(0,8) + ":" + a.substring(8,16));
			System.out.println(prev + ":" + afte);
			System.out.println("景点编号：" + pre + ";点击次数：" + aft);
			l.add(a);
//			if(i==0){
//				break;
//			}
		}
		System.out.println(l.toString());
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
//		for (int n = b.length-1; n >-1 ; n--) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1) {
				hs = hs + "0" + stmp;
			} else {
				hs = hs + stmp;
			}
		}
		System.out.println(hs.length());
		System.out.println(hs.toUpperCase().length());
		return hs.toUpperCase();
	}

	public static int byte2Int(byte b) {
		int r = (int) b;
		return r;
	}
	 /** 
     * 16进制字符串转换为字符串 
     *  
     * @param s 
     * @return 
     */  
	public static String hexStringToString(String s) {
		if (s == null || s.equals("")) {
			return null;
		}
		byte[] baKeyword = new byte[s.length() / 2];
		for (int i = 0; i < baKeyword.length; i++) {
			try {
				baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			System.out.println(baKeyword);
			s = new String(baKeyword, "UTF-8");
			System.out.println(s);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return s;
	}

}
