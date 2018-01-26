package yl.test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

/*
 * @author  lqpl66
 * @date 创建时间：2017年5月4日 上午10:10:46 
 * @function     
 */
public class T1 {
	public static void main(String args[]) throws IOException {
		String strSource = "4218319674429496681516360477457664762169519355";
		try {
			byte[] ba = strSource.getBytes("UTF-16");
			String str = "";//
			String s ="";
			for (byte b : ba) {
				if (b == 0) {
					str = "0x00";
				} else if (b == -1) {
					str = "0xFF";
				} else {
					// 将int类型数字转换成‘16进制’格式的字符串
					str = Long.toHexString(b).toUpperCase();
					if (str.length() > 2) {// 此时为‘负数’
						// 截取‘16进制格式字符串’的最后两位字符
						str = str.substring(str.length() - 2);
					}
					str = "0x" + str;
				}
//				System.out.println(str);
				s+=str;
			}
			System.out.println(s);
			 OutputStream  outputStream = new FileOutputStream("D:/dev_lease_v2017-1.bin");
			 byte[] b1 = s.toUpperCase().getBytes();
			 outputStream.write(b1);
			 outputStream.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
