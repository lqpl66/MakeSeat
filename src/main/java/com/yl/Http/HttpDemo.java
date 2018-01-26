package com.yl.Http;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yl.Utils.CommonDateParseUtil;
import com.yl.Utils.DBHelperUtils;
import com.yl.bean.StationName;

import net.sf.json.JSONObject;

/*
 * @author  lqpl66
 * @date 创建时间：2017年11月13日 下午4:07:31 
 * @function     
 */
public class HttpDemo {
	private static Logger log = Logger.getLogger(HttpDemo.class);

	/**
	 * 
	 * @description
	 * @param
	 * @return
	 */
	public static JSONObject httpGet(String url) {
		// get请求返回结果
		JSONObject jsonResult = null;
		CloseableHttpClient client = HttpClients.createDefault();
		// 发送get请求
		HttpGet request = new HttpGet(url);
		// 设置请求和传输超时时间
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(2000).setConnectTimeout(2000).build();
		request.setConfig(requestConfig);
		try {
			CloseableHttpResponse response = client.execute(request);

			// 请求发送成功，并得到响应
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// 读取服务器返回过来的json字符串数据
				HttpEntity entity = response.getEntity();
				String strResult = EntityUtils.toString(entity, "utf-8");
				// 把json字符串转换成json对象
				jsonResult = JSONObject.fromObject(strResult);
			} else {
				log.error("get请求提交失败:" + url);
			}
		} catch (IOException e) {
			log.error("get请求提交失败:" + url, e);
		} finally {
			request.releaseConnection();
		}
		return jsonResult;
	}

	public static String doPost(String url, String jsonstr, String charset) {
		HttpClient httpClient = null;
		HttpPost httpPost = null;
		String result = null;
		try {
			httpClient = new SSLClient();
			httpPost = new HttpPost(url);
			httpPost.addHeader("Content-Type", "application/json");
			StringEntity se = new StringEntity(jsonstr);
			se.setContentType("text/json");
			se.setContentEncoding(new BasicHeader("Content-Type", "application/json"));
			httpPost.setEntity(se);
			HttpResponse response = httpClient.execute(httpPost);
			if (response != null) {
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					result = EntityUtils.toString(resEntity, charset);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	public static String doGet(String url, String jsonstr, String charset) {
		// HttpClient httpClient = null;
		// HttpGet httpGet = null;
		// String result = null;
		// try {
		// httpClient = new SSLClient();
		// httpGet = new HttpGet(url);
		//// httpGet.addHeader("Content-Type", "application/json");
		// httpGet.addHeader("Accept",
		// "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		// httpGet.addHeader("User-Agent",
		// "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML,
		// like Gecko) Chrome/61.0.3163.100 Safari/537.36");
		//
		// StringEntity se = new StringEntity(jsonstr);
		//// se.setContentType("text/json");
		//// se.setContentEncoding(new BasicHeader("Content-Type",
		// "application/json"));
		// // httpGet.setEntity(se);
		// HttpResponse response = httpClient.execute(httpGet);
		// if (response != null) {
		// HttpEntity resEntity = response.getEntity();
		// if (resEntity != null) {
		// result = EntityUtils.toString(resEntity, charset);
		// }
		// }
		// } catch (Exception ex) {
		// ex.printStackTrace();
		// }
		// return result;
		HttpClient httpClient = null;
		HttpGet httpGet = null;
		String result = null;
		try {
			httpClient = new SSLClient();
			httpGet = new HttpGet(url);
			// httpGet.addHeader("Content-Type", "text/plain;charset=utf-8");
			httpGet.addHeader("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			// httpGet.addHeader("Cache-Control", "no-cache");
			httpGet.addHeader("Cache-Control", "max-age=0");
			 httpGet.addHeader("Connection", "close");
			httpGet.addHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
			HttpResponse response = httpClient.execute(httpGet);
			if (response != null) {
				HttpEntity resEntity = response.getEntity();
				System.out.println(resEntity.getContentLength());
				if (resEntity != null) {
					result = EntityUtils.toString(resEntity, charset);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return result;
	}

	static String sql = null;
	static DBHelperUtils db1 = null;
	static ResultSet ret = null;

	public static void main(String args[]) throws SQLException, Exception {
		// String url =
		// "https://kyfw.12306.cn/otn/resources/js/framework/station_name.js";
		String jsonStr = "{xxx}";
		// String httpOrgCreateTestRtn = doGet(url, jsonStr, "utf-8");
		// httpOrgCreateTestRtn = httpOrgCreateTestRtn.substring(21,
		// httpOrgCreateTestRtn.length() - 2);
		// String[] list = httpOrgCreateTestRtn.split("@");
		// for (String str : list) {
		// String[] stationName = str.split("\\|");
		// StationName sn = new StationName();
		// sn.setStationCode(stationName[2]);
		// sn.setStationName(stationName[1]);
		// sn.setStationNameAttr(stationName[0]);
		// sn.setStationNameAttr1(stationName[4]);
		// sn.setStationNameFull(stationName[3]);
		// sn.setStationNum(Integer.valueOf(stationName[5]));
		// DBHelperUtils.addStationName(sn);
		// }
		// System.out.println(list.length);
		// ------------------------------------------------------------------------------
		// sql = "select * from test";//SQL语句
		// db1 = new DBHelperUtils(sql);// 创建DBHelper对象
		// try {
		// ret = db1.pst.executeQuery();// 执行语句，得到结果集
		// while (ret.next()) {
		// String uid = ret.getString(1);
		// System.out.println(uid);
		// } // 显示数据
		// ret.close();
		// db1.close();// 关闭连接
		// } catch (SQLException e) {
		// e.printStackTrace();
		// }
		// --------------------------------------------------------------------------------------------------
		// https://kyfw.12306.cn/otn/resources/js/query/train_list.js?scriptVersion=1.0
		String url = "https://kyfw.12306.cn/otn/resources/js/query/train_list.js?scriptVersion=1.0";
//		for (int i = 0; i < 20; i++) {

			String httpOrgCreateTestRtn = HttpGetStationTrain.doGet1(url, "utf-8");
			JsonObject obj = new JsonParser().parse(httpOrgCreateTestRtn.substring(16)).getAsJsonObject();
			// System.out.println(httpOrgCreateTestRtn);
			for (Entry<String, JsonElement> pair : obj.entrySet()) {
				// sql = "select * from trainlist where date = '" +
				// pair.getKey() + "'";// SQL语句
				// db1 = new DBHelperUtils(sql);// 创建DBHelper对象
				// ret = db1.pst.executeQuery();// 执行语句，得到结果集
				// String uid = null;
				// while (ret.next()) {
				// uid = ret.getString(2);
				// } // 显示数据
				// ret.close();
				// db1.close();// 关闭连接
				// if (uid == null || uid.isEmpty()) {
				JsonObject obj1 = new JsonParser().parse(pair.getValue().toString()).getAsJsonObject();
				System.out.println("key1:" + pair.getKey());
				for (Entry<String, JsonElement> pair1 : obj1.entrySet()) {
					// System.out.println("key1:" + pair1.getKey());
					JsonArray js = null;
					if (pair1.getValue().isJsonArray()) {
						js = pair1.getValue().getAsJsonArray();
						// for (int i = 0; i < js.size(); i++) {
						// System.out.println(js.get(i).getAsJsonObject().get("station_train_code").getAsString());
						// DBHelperUtils.addtrain(js.get(i).getAsJsonObject().get("station_train_code").getAsString(),
						// js.get(i).getAsJsonObject().get("train_no").getAsString(),
						// pair1.getKey(),
						// pair.getKey());
						// }
					}
				}
				// }
			}
		}
//	}
}
