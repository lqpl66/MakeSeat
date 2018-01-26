package com.yl.Http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import net.sf.json.JSONObject;

public class HttpsUtils {
	// private static Logger log = Logger.getLogger(HttpsUtils.class);

	public static final String GET_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?";// 获取access
	// url
	public static final String APP_ID = "wxc9e385e97966ab61";
	public static final String grant_type = "client_credential";
	public static final String SECRET = "f5e754310bfbd50819172c7afeff25ab";
//	wxc9e385e97966ab61  服务号1
//	f5e754310bfbd50819172c7afeff25ab  服务号1
	
//	wxa095cf3d62e9ea06   服务号2
//	f77ac9f341c8c7178dbcf27864e70ede   服务号2  
	// 获取token
	public static String getToken(String apiurl, Map<String, String> params) {
		OutputStreamWriter out = null;
		BufferedReader in = null;
		StringBuilder result = new StringBuilder();
		try {
			URL realUrl = new URL(apiurl);
			HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// POST方法
			conn.setRequestMethod("GET");
			// 设置通用的请求属性
			// conn.setRequestProperty("accept", "*/*");
			// conn.setRequestProperty("connection", "Keep-Alive");
			// conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible;
			// MSIE 6.0; Windows NT 5.1;SV1)");
			// conn.setRequestProperty("Content-Type",
			// "application/x-www-form-urlencoded");
			conn.connect();
			// 获取URLConnection对象对应的输出流
			out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
			// 发送请求参数
			if (params != null) {
				StringBuilder param = new StringBuilder();
				for (Map.Entry<String, String> entry : params.entrySet()) {
					if (param.length() > 0) {
						param.append("&");
					}
					param.append(entry.getKey());
					param.append("=");
					param.append(entry.getValue());
				}
				out.write(param.toString());
			}
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) { // ff
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result.toString();
	}

	public static String getCode(String apiurl, Map<String, String> params) {
		OutputStreamWriter out = null;
		BufferedReader in = null;
		StringBuilder result = new StringBuilder();
		try {
			URL realUrl = new URL(apiurl);
			HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("GET");
			conn.connect();
			// 获取URLConnection对象对应的输出流
			out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
			// 发送请求参数
			if (params != null) {
				StringBuilder param = new StringBuilder();
				for (Map.Entry<String, String> entry : params.entrySet()) {
					if (param.length() > 0) {
						param.append("&");
					}
					param.append(entry.getKey());
					param.append("=");
					param.append(entry.getValue());
				}
				out.write(param.toString());
			}
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result.toString();
	}

	/**
	 * 
	 * @description 通过code换取网页授权openid
	 * @param
	 * @return
	 */
	public static String getOpenId(String code) {
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + APP_ID + "&secret=" + SECRET
				+ "&code=" + code + "&grant_type=authorization_code";
		String openId = "";
		try {
			URL getUrl = new URL(url);
			HttpURLConnection http = (HttpURLConnection) getUrl.openConnection();
			http.setRequestMethod("GET");
			http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			http.setDoOutput(true);
			http.setDoInput(true);

			http.connect();
			InputStream is = http.getInputStream();
			int size = is.available();
			byte[] b = new byte[size];
			is.read(b);

			String message = new String(b, "UTF-8");

			JSONObject json = JSONObject.fromObject(message);
			System.out.println(json.toString());
			openId = json.getString("openid");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return openId;
	}

	/**
	 * 
	 * @description 通过code换取网页授权access_token
	 * @param
	 * @return
	 */

	public static JSONObject getJsonOpenId(String code) {
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + APP_ID + "&secret=" + SECRET
				+ "&code=" + code + "&grant_type=authorization_code";
		JSONObject json = new JSONObject();
		try {
			URL getUrl = new URL(url);
			HttpURLConnection http = (HttpURLConnection) getUrl.openConnection();
			http.setRequestMethod("GET");
			http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			http.setDoOutput(true);
			http.setDoInput(true);

			http.connect();
			InputStream is = http.getInputStream();
			int size = is.available();
			byte[] b = new byte[size];
			is.read(b);
			String message = new String(b, "UTF-8");
			json = JSONObject.fromObject(message);
			System.out.println("getJsonOpenId:" + json.toString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return json;
	}

	/**
	 * 
	 * @description 检验授权凭证（access_token）是否有效
	 * @param
	 * @return
	 */
	public static Boolean getCheck(String ACCESS_TOKEN, String OPENID) {
		String url = "https://api.weixin.qq.com/sns/auth?access_token=" + ACCESS_TOKEN + "&openid=" + OPENID;
		Boolean flag = false;
		// url.replace("ACCESS_TOKEN", ACCESS_TOKEN).replace("OPENID", OPENID);
		try {
			URL getUrl = new URL(url);
			HttpURLConnection http = (HttpURLConnection) getUrl.openConnection();
			http.setRequestMethod("GET");
			http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			http.setDoOutput(true);
			http.setDoInput(true);

			http.connect();
			InputStream is = http.getInputStream();
			int size = is.available();
			byte[] b = new byte[size];
			is.read(b);
			String message = new String(b, "UTF-8");
			JSONObject json = JSONObject.fromObject(message);
			System.out.println("getCheck:" + json.toString());
			if (json.optInt("errcode") == 0) {
				flag = true;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 
	 * @description 刷新 access_token
	 * @param
	 * @return
	 */
	public static JSONObject getRefresh_token(String refresh_token, String OPENID) {
		String url = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=" + APP_ID
				+ "&grant_type=refresh_token&refresh_token=" + refresh_token;
		JSONObject json = new JSONObject();
		try {
			URL getUrl = new URL(url);
			HttpURLConnection http = (HttpURLConnection) getUrl.openConnection();
			http.setRequestMethod("GET");
			http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			http.setDoOutput(true);
			http.setDoInput(true);

			http.connect();
			InputStream is = http.getInputStream();
			int size = is.available();
			byte[] b = new byte[size];
			is.read(b);
			String message = new String(b, "UTF-8");
			json = JSONObject.fromObject(message);
			System.out.println("getRefresh_token:" + json.toString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return json;
	}

	public static void main(String[] args) throws Exception {
		// System.out.println("=========1获取token=========");
		// Map<String, String> params = new HashMap<String, String>();
		// // GET_TOKEN_URL, APP_ID, SECRET,grant_type
		// params.put("appid", APP_ID);
		// params.put("secret", SECRET);
		// params.put("grant_type", grant_type);
		// String accessToken = getToken(GET_TOKEN_URL, params);// 获取token
		// if (accessToken != null)
		// System.out.println(accessToken);
		getCheck(
				"Pzq8bSOB-3wQYLd0-yrUbF1_N4in-oQMP07dEXmgR_FVcjR7-zES8DpGrLMsNy0L-ZLKa9Abwosfbtl29K8n-UeRlY0sjIKZIN5G9PciVIM",
				"oNEJ_v_BTqEuG1vXw4G7N_TkwCkc");

	}
}
