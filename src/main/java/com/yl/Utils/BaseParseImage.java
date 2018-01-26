package com.yl.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder; 

public class BaseParseImage {
	public static String generateImage(String imgStr, String path, String type, String prefix) {
		String filename = null;
		if (imgStr == null) { // 图像数据为空
			return filename;
		}
	
		BASE64Decoder b4 = new BASE64Decoder();
		try {
			if (prefix != null) {
				filename = prefix + "_" + CodeUtils.getfileName() + type;
			} else {
				filename = CodeUtils.getfileName() + type;
			}
			// Base64解码
			byte[] bytes = b4.decodeBuffer(imgStr);
			for (int i = 0; i < bytes.length; ++i) {
				if (bytes[i] < 0) {// 调整异常数据
					bytes[i] += 256;
				}
			}
			File file = new File(path);
			File f = new File(path + filename);
			if (!file.exists()) {
				file.mkdirs();
			}
			// 生成jpg图片
			OutputStream out = new FileOutputStream(f); 
			
			
			out.write(bytes);
			out.flush();
			out.close();
			// TODO 如果是linux赋值权限
			Resource resource = null;
			Properties props = null;
			resource = new ClassPathResource("/resources.properties");
			props = PropertiesLoaderUtils.loadProperties(resource);
		    try {
				props = PropertiesLoaderUtils.loadProperties(resource);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			String SystemMarking=(String) props.get("System")+"";
			if(SystemMarking.equals("Linux")){
				Runtime.getRuntime().exec("chmod 777 " + f);
				System.out.println("去掉空格赋值权限成功");
			}
			return filename;
		} catch (Exception e) {
			System.out.println(e);
			return filename;
		}
	}

	public static String GetImageStr(String imgFile) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
		// String imgFile =
		// "E:/usr/local/files/Evaluate/Scenic/Maxfile/24.jpg";// 待处理的图片
		InputStream in = null;
		byte[] data = null;
		// 读取图片字节数组
		try {
			in = new FileInputStream(imgFile);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			System.out.println(e);
			e.printStackTrace();
		}
		// 对字节数组Base64编码
		BASE64Encoder encoder = new BASE64Encoder();
//		System.out.println(encoder.encode(data));
		return encoder.encode(data);// 返回Base64编码过的字节数组字符串
	}

	public static void main(String[] args) {
		// String path = GetProperties.getEvaImgScenicMaxUrl();
		// String fileName = generateImage(imgStr, path, ".jpg");
		// System.out.println(fileName);
		JSONObject js = new JSONObject();
		js.put("uuID", "7e2da08b741b43198c066cc36b283161");
		js.put("fkId", 1);
		js.put("score", 2.9);
		js.put("content", "dsada2313");
		js.put("model", "scenic");
		JSONArray jss = new JSONArray();
		String imgStr1 = GetImageStr("C:/Users/Administrator/Desktop/IMG_20170824_155945.jpg");
		generateImage(imgStr1, "D:/TestImg", ".jpg",null);
//		System.out.println(imgStr1.toString());
//		String imgStr2 = GetImageStr("E:/usr/local/files/Evaluate/Scenic/Maxfile/22.jpg");
//		String imgStr3 = GetImageStr("E:/usr/local/files/Evaluate/Scenic/Maxfile/23.jpg");
//		String imgStr4 = GetImageStr("E:/usr/local/files/Evaluate/Scenic/Maxfile/24.jpg");
		JSONObject eva1 = new JSONObject();
//		JSONObject eva2 = new JSONObject();
//		JSONObject eva3 = new JSONObject();
//		JSONObject eva4 = new JSONObject();
		js.clear();
		js.put("d", imgStr1);
//		eva1.put("imgUrl", imgStr1);
//		eva2.put("imgUrl", imgStr2);
//		eva3.put("imgUrl", imgStr3);
//		eva4.put("imgUrl", imgStr4);
//		jss.add(eva1);
//		jss.add(eva2);
//		jss.add(eva3);
//		jss.add(eva4);
//		js.put("evaluateImgList", jss);
		System.out.println(js);

	}

}