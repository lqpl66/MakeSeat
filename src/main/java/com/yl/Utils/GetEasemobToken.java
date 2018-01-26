package com.yl.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import com.yl.bean.Token;

import net.sf.json.JSONObject;

public class GetEasemobToken {
	private static String org_name = "1153170224115621";
	//正式环信
	private static String app_name = "makeseat";
	private static String appKey = "YXA6VQDFsNDtEeehybciblVz0A";
	private static String appSecret = "YXA6_ACMqaKa6gz0FbksV8HCe5Zkv3s";
//	
	//测试环信
//	private static String app_name = "makeseattest";
//	private static String appKey = "YXA6hYLEMPzWEeeuK13nPtWPqg";
//	private static String appSecret = "YXA6hobRmKMaJi_me-uagFgNALW0gkg";
//	
	
//	private static String org_name = "1153170224115621";
//	private static String app_name = "youleapp";
//	private static String appKey = "YXA6YE_d4AOqEeeAQPcLyIJCCA";
//	private static String appSecret = "YXA663ccNpCGo9_V4-a67atE5Tl3x0U";
//	
	
	private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	// https://a1.easemob.com
	private static String url = "https://a1.easemob.com";

	/*
	 * type 1 获取
	 */
	public static JSONObject httpPost() {
		JSONObject jsondata = new JSONObject();
		JSONObject result = new JSONObject();
		String data = "";
		String token = "";

		String path = url + "/" + org_name + "/" + app_name + "/token";
		try {

			// {
			// "grant_type":"client_credentials",
			// "client_id":"YXA6YE_d4AOqEeeAQPcLyIJCCA",
			// "client_secret":"YXA663ccNpCGo9_V4-a67atE5Tl3x0U"
			// }
			jsondata.put("grant_type", "client_credentials");
			jsondata.put("client_id", appKey);
			jsondata.put("client_secret", appSecret);
			URL url = new URL(path);
			HttpURLConnection http = (HttpURLConnection) url.openConnection();
			http.setDoOutput(true);
			http.setDoInput(true);
			http.setRequestMethod("POST");
			http.setRequestProperty("Content-Type", "application/json;charset=UTF-8;");
			// String author = "Bearer" + token;
			// http.setRequestProperty("Authorization", author);
			http.connect();
			OutputStream os = http.getOutputStream();
			data = jsondata.toString();
			os.write(data.getBytes("UTF-8"));
			os.close();

			InputStream is = http.getInputStream();
			int size = is.available();
			byte[] bt = new byte[size];
			is.read(bt);
			String message = new String(bt, "UTF-8");
			result = JSONObject.fromObject(message);
			// status = jsonMsg.getString("error");
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	public static void addToken(Token form) {
		/*
		 * 1. 得到Document 2. 得到root元素 3. 要把User对象转换成Element元素 4.
		 * 把user元素插入到root元素中 5. 回写document
		 */
		try {
			/*
			 * 1. 得到Docuembnt
			 */
			// 创建解析器
			SAXReader reader = new SAXReader();
			String path = GetProperties.getPath() + "EasemobToken.xml";
			// 调用读方法，得到Document
			Document doc = reader.read(path);
         
			/*
			 * 2. 得到根元素
			 */
			Element root = doc.getRootElement();
			/*
			 * 3. 完成添加元素，并返回添加的元素！ 向root中添加一个名为user的元素！并返回这个元素
			 */
			String xpath = "//token";
			Element userEle = (Element) doc.selectSingleNode(xpath);
			root.remove(userEle);
			Element userElement = root.addElement("token");
			// 设置userElement的属性！
			userElement.addAttribute("access_token", form.getAccess_token());
			userElement.addAttribute("application", form.getApplication());
			userElement.addAttribute("endTime", form.getEndTime());

			/*
			 * 回写 注意：创建的users.xml需要使用工具修改成UTF-8编码！ Editplus：标记列--> 重新载入为 -->
			 * UTF-8
			 */

			// 创建目标输出流，它需要与xml文件绑定
			Writer out = new PrintWriter(path, "UTF-8");
			// 创建格式化器
			OutputFormat format = new OutputFormat("\t", true);
			format.setTrimText(true);// 先干掉原来的空白(\t和换行和空格)！

			// 创建XMLWrtier
			XMLWriter writer = new XMLWriter(out, format);

			// 调用它的写方法，把document对象写到out流中。
			writer.write(doc);

			// 关闭流
			out.close();
			writer.close();

		} catch (Exception e) {
			// 把编译异常转换成运行异常！
			throw new RuntimeException(e);
		}
	}

	/**
	 * 按用户名进行查询
	 * 
	 * @param username
	 * @return
	 */
	public static String getToken() {
		String token_str = "";
		try {
			// 创建解析器
			SAXReader reader = new SAXReader();
			String path = GetProperties.getPath() + "EasemobToken.xml";
			// 调用读方法，得到Document
			Document doc = reader.read(path);
			String xpath = "//token";
			Element userEle = (Element) doc.selectSingleNode(xpath);
			if (userEle == null) {
				return null;
			}
			Token token = new Token();
			token.setAccess_token(userEle.attributeValue("access_token"));
			token.setEndTime(userEle.attributeValue("endTime"));
			token.setApplication(userEle.attributeValue("application"));
			// 判断是否超时
			Date d1 = df.parse(token.getEndTime());
			Date d2 = new Date();
			if (d1.getTime() < d2.getTime()) {// 超时重新请求token并保存
				JSONObject result = httpPost();
				String access_token = result.optString("access_token");
				String endTime = result.optString("expires_in");
				String application = result.optString("application");
				Integer time = Integer.valueOf(endTime);// 秒
				System.out.println(time);
				Calendar c =Calendar.getInstance();
				c.add(Calendar.SECOND, time);
				token.setAccess_token(access_token);
				token.setApplication(application);
				token.setEndTime(df.format(c.getTime()));
				addToken(token);
			}
			return token.getAccess_token();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String args[]) {
		Token user = new Token();
		user.setAccess_token("ss");
		user.setApplication("ddd");
		user.setEndTime("1990-12-24 12:50:58");
		addToken(user);

//		System.out.println(getToken());
	}

}
