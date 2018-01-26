package yl.test;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/*
 * @author  lqpl66
 * @date 创建时间：2017年5月8日 上午11:04:35 
 * @function     
 */
public class ReadBinaryTest {
	public void testDataInputStream(String filepath) throws IOException {
		File file = new File(filepath);
		DataInputStream din = new DataInputStream(new FileInputStream(file));
		StringBuilder hexData = new StringBuilder();
		StringBuilder asciiData = new StringBuilder();
		byte temp = 0;
		for (int i = 0; i < file.length(); i++) {
			temp = din.readByte(); // 以十六进制的无符号整数形式返回一个字符串表示形式。
			String str = Integer.toHexString(temp); //
			System.out.println(++i + ":" + Integer.toHexString(temp).length() + ":" + Integer.toHexString(temp)); //
			System.out.flush();
			if (str.length() == 8) {// 去掉补位的f
				str = str.substring(6);
			}
			if (str.length() == 1) {
				str = "0" + str;
			}
			hexData.append(str.toUpperCase()); // 转换成ascii码
			String ascii = "";
			if (temp > 34 && temp < 127) {
				char c = (char) temp;
				ascii = c + "";
			} else {
				ascii = ".";
			}
			asciiData.append(ascii);
			// System.out.println(str.toUpperCase()+"|"+temp+"|"+ascii); }
			// //计算行数
			int line = hexData.toString().length() / 32;
			din.close();
			StringBuilder lines = new StringBuilder();
			for (int j = 0; j <= line; j++) {
				String li = j + "0";
				if (li.length() == 2) {
					li = "00" + li;
				}
				if (li.length() == 3) {
					li = "0" + li;
				}
				lines.append(li);
			}
			System.out.println(lines.toString());
		}
	}

	public static void main(String[] args) throws IOException {
		String filepath = "D:/dev_serial_v2017.bin";
		ReadBinaryTest test = new ReadBinaryTest();
		test.testDataInputStream(filepath);
	}
}
