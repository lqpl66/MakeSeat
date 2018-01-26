package com.yl.Http;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.sf.json.JSONObject;

/*
 * @author  lqpl66
 * @date 创建时间：2017年11月15日 上午11:01:34 
 * @function     
 */
public class HttpGetCode {
	// https://kyfw.12306.cn/otn/passcodeNew/getPassCodeNew?module=other&rand=sjrand
	private static String contentType = "application/x-www-form-urlencoded; charset=UTF-8";
	private static String userAgent = "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36";
	private static String referer = "https://kyfw.12306.cn/otn/queryTrainInfo/init";

	public static String doGet(String url, String charset) {
		HttpClient httpClient = null;
		HttpGet httpGet = null;
		String result = null;
		try {
			httpClient = new SSLClient();
			httpGet = new HttpGet(url);
			httpGet.addHeader("Content-Type", contentType);
			httpGet.addHeader("User-Agent", userAgent);
			httpGet.addHeader("Refere", referer);
			httpGet.addHeader("Content-Type", contentType);
			httpGet.addHeader("Cookie",
					"JSESSIONID=374C6FDBF7FA9F8F564CA552FEBCCAB8; route=495c805987d0f5c8c84b14f60212447d; BIGipServerotn=1389953290.24610.0000; RAIL_EXPIRATION=1511002598383; RAIL_DEVICEID=I1-CpaqyrCHQJuBi57Xk8iH_951x4l_7Dt8i_19ScIymsy6E4zDgHzud7ZEBUBjQDuxo1pDHnB5gxZutreOE_LNxWark3XF63Fk7YVzlkWC9o0OeG_ijgi0ETHZj0ywsiegdyGmKwU2SPilMWHDS1HQd4R5ek5UX; current_captcha_type=C; _jc_save_fromDate=2017-11-15");
			HttpResponse response = httpClient.execute(httpGet);
			if (response != null) {
				HttpEntity resEntity = response.getEntity();
				System.out.println(response.getHeaders("Cookie"));
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
		HttpClient httpClient = null;
		HttpGet httpGet = null;
		String result = null;
		try {
			httpClient = new SSLClient();
			httpGet = new HttpGet(url);
			httpGet.addHeader("Content-Type", "application/json");
			StringEntity se = new StringEntity(jsonstr);
			se.setContentType("text/json");
			se.setContentEncoding(new BasicHeader("Content-Type", "application/json"));
			// httpGet.setEntity(se);
			HttpResponse response = httpClient.execute(httpGet);
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

	public static void main(String args[]) throws SQLException, Exception {
		// String url =
		// "https://kyfw.12306.cn/otn/passcodeNew/getPassCodeNew?module=other&rand=sjrand";
		String url = "https://kyfw.12306.cn/otn/queryTrainInfo/query?leftTicketDTO.train_no=24000000D10W&leftTicketDTO.train_date=2017-11-15&rand_code=xt8u";
		String httpOrgCreateTestRtn = doGet(url, "utf-8");
		System.out.println(httpOrgCreateTestRtn);
	}
}
