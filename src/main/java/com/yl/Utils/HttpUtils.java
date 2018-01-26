package com.yl.Utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import net.sf.json.JSONObject;

@SuppressWarnings("deprecation")
public class HttpUtils {
	private static Logger log = Logger.getLogger(HttpUtils.class);

	public static JSONObject httpGet(Map<String, String> paramsMap) {
		// get请求返回结果
		JSONObject jsonResult = null;
		String url = "http://api.map.baidu.com/place/v2/search?";
		try {
			String paramStr = toQueryString(paramsMap);
			url = url + paramStr;
			DefaultHttpClient client = new DefaultHttpClient();
			// 发送get请求
			HttpGet request = new HttpGet(url);
			HttpResponse response = client.execute(request);

			/** 请求发送成功，并得到响应 **/
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				/** 读取服务器返回过来的json字符串数据 **/
				String strResult = EntityUtils.toString(response.getEntity());
				/** 把json字符串转换成json对象 **/
				jsonResult = JSONObject.fromObject(strResult);
				url = URLDecoder.decode(url, "UTF-8");
			} else {
				 log.error("get请求提交失败:" + url);
			}
		} catch (IOException e) {
			 log.error("get请求提交失败:" + url, e);
		}
		return jsonResult;
	}

	public static String toQueryString(Map<?, ?> data) throws UnsupportedEncodingException {
		StringBuffer queryString = new StringBuffer();
		for (Entry<?, ?> pair : data.entrySet()) {
			if ("timestamp".equals(pair.getKey())) {

			}
			queryString.append(pair.getKey() + "=");
			queryString.append(URLEncoder.encode((String) pair.getValue(), "UTF-8") + "&");
		}
		if (queryString.length() > 0) {
			queryString.deleteCharAt(queryString.length() - 1);
		}
		return queryString.toString();
	}
	
	public static void main(String args[]){
		String type = "2";
		Map<String, String> paramsMap = new LinkedHashMap<String, String>();
		if (type.equals("1")) {
			paramsMap.put("location", "31.196814,121.427876");
			paramsMap.put("radius", "3000");
//			美食$酒店$购物$旅游景点$休闲娱乐$文化传媒$交通设施
			paramsMap.put("query", "美食$酒店$购物$旅游景点$休闲娱乐$文化传媒$交通设施");
		} else {
			paramsMap.put("region", "上海市");
			paramsMap.put("query", "豫园老街");
//			paramsMap.put("tag", "美食,酒店,购物,旅游景点,休闲娱乐,文化传媒,交通设施");
		}
		paramsMap.put("page_size", String.valueOf(2));
		paramsMap.put("page_num", String.valueOf(23));
		paramsMap.put("ak", "3r6o0LH8SRfWiQ0VyglnPExlpMWa5bUr");
		paramsMap.put("scope", "1");
		paramsMap.put("output", "json");
		JSONObject js = httpGet(paramsMap);
		System.out.println(js.toString());
	}
	

}
