package yl.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.yl.Utils.CodeUtils;

/*
 * @author  lqpl66
 * @date 创建时间：2017年5月8日 上午9:50:17 
 * @function     
 */
public class T2 {
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
		if (s.length() % 2 == 1) {
			s = "0" + s;
		}
		for (int i = 0; i < s.length() / 2; i++) {
			String h = s.substring(i * 2, i * 2 + 2);
			// System.out.println(h);
			hex = h + hex;
		}
		// long dec = Long.parseLong(hex, 16);
		// hex = Long.toString(dec);
		System.out.println(hex);
		return hex;
	}

	// 0f8701
	public static int bytesToInt(byte[] src, int offset) {
		int value;
		value = (int) ((src[offset] & 0xFF) | ((src[offset + 1] & 0xFF) << 8) | ((src[offset + 2] & 0xFF) << 16)
				| ((src[offset + 3] & 0xFF) << 24));
		return value;
	}

	public static byte[] intToBytes2(int value) {
		byte[] src = new byte[4];
		src[0] = (byte) ((value >> 24) & 0xFF);
		src[1] = (byte) ((value >> 16) & 0xFF);
		src[2] = (byte) ((value >> 8) & 0xFF);
		src[3] = (byte) (value & 0xFF);
		return src;
	}

	public static byte[] intToBytes(int value) {
		byte[] src = new byte[4];
		src[3] = (byte) ((value >> 24) & 0xFF);
		src[2] = (byte) ((value >> 16) & 0xFF);
		src[1] = (byte) ((value >> 8) & 0xFF);
		src[0] = (byte) (value & 0xFF);
		return src;
	}

	public static void main(String args[]) throws IOException {
		int key0 = 0x0297;// 序列号
		key0 = 0x2;
		System.out.println(Integer.toHexString(663));
		int i = 663;
		byte[] b = intToBytes(663);
		int j = bytesToInt(b, 0);
		int k = j ^ 8240;
		System.out.println(k);
		System.out.println(i);
		System.out.println(key0);
		System.out.println(j);
		CodeUtils.getCode(4);
		String fileName = "D:/dev_lease_v2017-61.bin";
		FileOutputStream out = new FileOutputStream(fileName);
		byte[] b1 = new byte[4];
		// "guideIds":54759386,"time":887,"date":89501891,"key1":12345,"key2":54321   
//		"guideIds":54759386,"time":887,"date":89501891,"key1":54758733,"key2":73824212
		System.out.println("ll");
		System.out.println(664^12345);
		System.out.println(480^664);
		System.out.println(519^54321);
		long invalue = 12961;
		b1[0] = (byte) (invalue & 0xff);
		b1[1] = (byte) ((invalue >> 8) & 0xff);
		b1[2] = (byte) ((invalue >> 16) & 0xff);
		b1[3] = (byte) ((invalue >> 24) & 0xff);
		out.write(b1);
		byte[] b2 = new byte[4];
		invalue = 888;
		b2[0] = (byte) (invalue & 0xff);
		b2[1] = (byte) ((invalue >> 8) & 0xff);
		b2[2] = (byte) ((invalue >> 16) & 0xff);
		b2[3] = (byte) ((invalue >> 24) & 0xff);
		out.write(b2);
		invalue = 54838;
		byte[] b3 = new byte[4];
		b3[0] = (byte) (invalue & 0xff);
		b3[1] = (byte) ((invalue >> 8) & 0xff);
		b3[2] = (byte) ((invalue >> 16) & 0xff);
		b3[3] = (byte) ((invalue >> 24) & 0xff);
		out.write(b3);
		invalue = 12345;
		byte[] b4 = new byte[4];
		b4[0] = (byte) (invalue & 0xff);
		b4[1] = (byte) ((invalue >> 8) & 0xff);
		b4[2] = (byte) ((invalue >> 16) & 0xff);
		b4[3] = (byte) ((invalue >> 24) & 0xff);
		out.write(b4);
		invalue = 54321;
		byte[] b5 = new byte[4];
		b5[0] = (byte) (invalue & 0xff);
		b5[1] = (byte) ((invalue >> 8) & 0xff);
		b5[2] = (byte) ((invalue >> 16) & 0xff);
		b5[3] = (byte) ((invalue >> 24) & 0xff);
		out.write(b5);
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
		System.out.println(hs.toUpperCase());
		return hs.toUpperCase();
	}
}
