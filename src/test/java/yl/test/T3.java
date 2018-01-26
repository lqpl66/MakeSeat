package yl.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class T3 {
	public static String PATH_USBOTG = "./";
	public static final String NAME_LEASE_FILE = "dev_lease_v2017.bin";// 租赁文件
	public static final String NAME_SERIAL_FILE = "dev_serial_v2017.bin";// 序列号文件
	public static final String NAME_DOC_FILE = "doc_file.bin";// 文档文件
	public static String LEASE_FILE = PATH_USBOTG + NAME_LEASE_FILE;
	public static String SERIAL_FILE = PATH_USBOTG + NAME_SERIAL_FILE;
	public static String DOC_FILE = PATH_USBOTG + NAME_DOC_FILE;
	static String TAG = "AuthFileOpreator";

	// int key0 = 0;// 序列号
	// int key1 = 0x2030;// 可以随机生成
	// int key2 = 0x2083;// 可以随机生成
	//
	// int time0 = 0x300; // 授权时间长
	// int time1 = 0x300; // 授权上位机日期时间

	public static void main(String[] args) {
		writeLeaseFile(new int[] {54759386,887,89501891,54758733,73824212});
		// writeLeaseFile(new int[] { 663, 0x2030, 0x2083 },new
		// int[]{0x300,0x300});
	}

	/**
	 * 写入租赁文件
	 *
	 * @param values
	 * @return
	 */
	public static boolean writeLeaseFile(int[] values) {
		if (values.length != 5) {
			return false;
		}
		// values = toHex(values);

		File file = new File("D:/dev_lease_v2017-9.bin");
		if (!file.exists()) {
			try {
				// 文件不存在，就创建一个新文件
				file.createNewFile();
				FileOutputStream fos = new FileOutputStream(file);

				long invalue = 54759386;
				byte[] b = intToBytes(invalue);
				fos.write(b);
				System.out.println(invalue);
				invalue = 887;
				b = intToBytes(invalue);
				fos.write(b);
				System.out.println(invalue);
				invalue =89501891;
				b = intToBytes(invalue);
				fos.write(b);
				System.out.println(invalue);
				invalue = 54758733;
				b = intToBytes(invalue);
				fos.write(b);
				System.out.println(invalue);
				invalue = 73824212;
				b = intToBytes(invalue);
				System.out.println(invalue);
				fos.write(b);
				fos.flush();
				fos.getFD().sync();
				fos.close();
				System.out.println("授权成功");
				return deleteSerialFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 写入租赁文件
	 *
	 * @param keys
	 * @param times
	 * @return
	 */
	public static boolean writeLeaseFile(int[] keys, int[] times) {
		if (keys.length != 3 || times.length != 2) {
			return false;
		}

		File file = new File(LEASE_FILE);
		if (!file.exists()) {
			try {
				// 文件不存在，就创建一个新文件
				file.createNewFile();
				FileOutputStream fos = new FileOutputStream(file);

				int invalue = keys[0] ^ keys[1];
				byte[] b = intToBytes(invalue);
				fos.write(b);

				invalue = times[0] ^ keys[0];
				b = intToBytes(invalue);
				fos.write(b);

				invalue = times[1] ^ keys[2];
				b = intToBytes(invalue);
				fos.write(b);

				invalue = keys[1];
				b = intToBytes(invalue);
				fos.write(b);

				invalue = keys[2];
				b = intToBytes(invalue);
				fos.write(b);
				fos.flush();
				fos.getFD().sync();
				fos.close();
				System.out.println("授权成功");
				return deleteSerialFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 删除序号文件
	 *
	 * @return
	 */
	private static boolean deleteSerialFile() {
		File file = new File(SERIAL_FILE);
		if (file != null && file.exists()) {
			return file.delete();
		}
		return false;
	}

	// /**
	// * 读取导览笔点读文档
	// *
	// * @return
	// */
	// public static List<UploadGuideClickRequest.ClickInfo> readDocData() {
	// List<UploadGuideClickRequest.ClickInfo> infos = new ArrayList<>();
	// UploadGuideClickRequest.ClickInfo info = null;
	// try {
	// File file = new File(DOC_FILE);
	// FileInputStream is = new FileInputStream(file);
	// byte[] b1 = new byte[8];
	// while (is.read(b1) == 8) {
	// byte[] b2 = new byte[4];
	// System.arraycopy(b1, 4, b2, 0, 4);// 点击次数
	// int num = bytesToInt(b2);
	// if (num <= 0) {
	// continue;
	// }
	// info = new UploadGuideClickRequest.ClickInfo();
	// info.setClickNum(num);
	//
	// System.arraycopy(b1, 0, b2, 0, 4);// 文件编号
	// num = bytesToInt(b2);
	// info.setBaseCode(Integer.toString(num));
	// infos.add(info);
	// }
	// is.close();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// return infos;
	// }

	private static int bytesToInt(byte[] bytes) {
		int value;
		value = ((bytes[0] & 0xFF) | ((bytes[1] & 0xFF) << 8) | ((bytes[2] & 0xFF) << 16) | ((bytes[3] & 0xFF) << 24));
		return value;
	}

	private static byte[] intToBytes(long value) {
		byte[] src = new byte[4];
//		src[0] = (byte) (value & 0xFF);
//		src[1] = (byte) ((value >> 8) & 0xFF);
//		src[2] = (byte) ((value >> 16) & 0xFF);
//		src[3] = (byte) ((value >> 24) & 0xFF);
		src[0] = (byte) (value & 0xff);
		src[1] = (byte) ((value >> 8) & 0xff);
		src[2] = (byte) ((value >> 16) & 0xff);
		src[3] = (byte) ((value >> 24) & 0xff);
		return src;
	}

	/**
	 * 读取序号文件的导览笔序号
	 *
	 * @return
	 */
	public static int readSerialNum() {
		int key = -111;// 序列号
		try {
			File file = new File(SERIAL_FILE);
			FileInputStream is = new FileInputStream(file);
			byte[] b = new byte[4];
			is.read(b);
			is.close();
			key = bytesToInt(b);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return key;
	}

	/**
	 * 检查已挂在设备是否事合法设备
	 *
	 * @return
	 */
	public static boolean checkSdDevice() {
		File file = new File(SERIAL_FILE);
		return file != null && file.exists();
	}

	public static int[] toHex(int[] values) {
		int length = values.length;
		int[] v = new int[length];
		for (int i = 0; i < length; i++) {
			v[i] = OxStringtoInt(Integer.toHexString(i));
		}
		return v;
	}

	public static int OxStringtoInt(String ox) {
		ox = ox.toLowerCase();
		if (ox.startsWith("0x")) {
			ox = ox.substring(2, ox.length());
		}
		int ri = 0;
		int oxlen = ox.length();

		for (int i = 0; i < oxlen; i++) {
			char c = ox.charAt(i);
			int h = 0;
			if (('0' <= c && c <= '9')) {
				h = c - 48;
			} else if (('a' <= c && c <= 'f')) {
				h = c - 87;

			} else if ('A' <= c && c <= 'F') {
				h = c - 55;
			}
			byte left = (byte) ((oxlen - i - 1) * 4);
			ri |= (h << left);
		}
		return ri;

	}

}

