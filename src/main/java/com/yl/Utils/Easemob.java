package com.yl.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Easemob {
//	1153170224115621
	private static String org_name = "1153170224115621";
	// private static String app_name = "youleapp";
	// private static String appKey = "YXA6YE_d4AOqEeeAQPcLyIJCCA";
	// private static String appSecret = "YXA663ccNpCGo9_V4-a67atE5Tl3x0U";
//	正式
	private static String app_name = "makeseat";
	private static String appKey = "YXA6VQDFsNDtEeehybciblVz0A";
	private static String appSecret = "YXA6_ACMqaKa6gz0FbksV8HCe5Zkv3s";
	//测试
//	private static String app_name = "makeseattest";
//	private static String appKey = "YXA6hYLEMPzWEeeuK13nPtWPqg";
//	private static String appSecret = "YXA6hobRmKMaJi_me-uagFgNALW0gkg";
	// https://a1.easemob.com
	private static String url = "https://a1.easemob.com";

	/*
	 * type 1 注册环信账号
	 */
	public static String registerEasemon(String data, String type) throws Exception {
		// String jsondata = JSONObject.fromObject(data).toString();
		String status = "";
		String token = GetEasemobToken.getToken();
		System.out.println("token:" + token);
		String path = url + "/" + org_name + "/" + app_name + "/users";
		// try {
		URL url = new URL(path);
		HttpURLConnection http = (HttpURLConnection) url.openConnection();
		http.setDoOutput(true);
		http.setDoInput(true);
		http.setRequestMethod("POST");
		http.setRequestProperty("Content-Type", "application/json;charset=UTF-8;");
		String author = "Bearer " + token;
		http.setRequestProperty("Authorization", author);
		http.connect();
		OutputStream os = http.getOutputStream();
		os.write(data.getBytes("UTF-8"));
		os.close();
		InputStream is = http.getInputStream();
		int size = is.available();
		byte[] bt = new byte[size];
		is.read(bt);
		String message = new String(bt, "UTF-8");
		JSONObject jsonMsg = JSONObject.fromObject(message);
		System.out.println(jsonMsg.toString());
		// } catch (IOException e) {
		// e.printStackTrace();
		//
		// }
		return status;
	}

	public static void main(String args[]) throws Exception {
		// String ss= '[{"username":"u1", "password":"p1"}, {"username":"u2",
		// "password":"p2"}]';
		JSONArray ja = new JSONArray();
		JSONObject js = new JSONObject();
		js.put("username", "test34");
		js.put("password", "124781");
		ja.add(js);

		System.out.println(registerEasemon(ja.toString(), null));
	}
}
