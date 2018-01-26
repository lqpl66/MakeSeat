package com.yl.Utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.json.JSONObject;

public class MapToXml {
	public static String getMapToXml(Map<String, String> map, Map<String, String> Articlesmap, String type) {
		String xmlResult = "";
		StringBuffer sb = new StringBuffer();
		String[] keys = map.keySet().toArray(new String[0]);
		Arrays.sort(keys);
		sb.append("<xml>");
		for (String key : keys) {
			if (type != null) {
				if (type.equals("Image")) {
					if (key.contains("MediaId")) {
						String value = "<![CDATA[" + map.get(key) + "]]>";
						sb.append("<" + type + ">" + "<" + key + ">" + value + "</" + key + ">" + "</" + type + ">");
					} else {
						String value = "<![CDATA[" + map.get(key) + "]]>";
						sb.append("<" + key + ">" + value + "</" + key + ">");
					}
				}
				if (type.equals("Articles")) {
					if (key.contains("Articles")) {
						String[] keys1 = Articlesmap.keySet().toArray(new String[0]);
						Arrays.sort(keys1);
						sb.append("<Articles><item>");
						for (String key1 : keys1) {
							String value1 = "<![CDATA[" + Articlesmap.get(key1) + "]]>";
							sb.append("<" + key1 + ">" + value1 + "</" + key1 + ">");
						}
						sb.append("</item></Articles>");
					} else {
						String value = "<![CDATA[" + map.get(key) + "]]>";
						sb.append("<" + key + ">" + value + "</" + key + ">");
					}
				}
			} else {
				String value = "<![CDATA[" + map.get(key) + "]]>";
				sb.append("<" + key + ">" + value + "</" + key + ">");
			}
		}
		sb.append("</xml>");
		xmlResult = sb.toString();
		 System.out.println("返回值："+xmlResult);
		return xmlResult;
	}

	public static void main(String args[]) {
		Map<String, String> messageInfomap = new HashMap<String, String>();
		messageInfomap.put("ToUserName", "dd");
		messageInfomap.put("FromUserName", "ddd");
		long longTime1 = System.currentTimeMillis();
		messageInfomap.put("CreateTime", String.valueOf(longTime1 / 1000));
		// messageInfomap.put("MsgType", "image");
		// messageInfomap.put("MediaId",
		// "vbyH7Rd0N5XEY20vWYQGgYMqVthATa7Ym9Wl8kzRf8OWcVlM-d__cNZYSB-kHGct");
		// String messageInfo = MapToXml.getMapToXml(messageInfomap,null,
		// "Image");
		messageInfomap.put("MsgType", "news");
		messageInfomap.put("ArticleCount", "1");
		Map<String, String> UrlInfomap = new HashMap<String, String>();
		UrlInfomap.put("Title", "ddddqeqweq");
		UrlInfomap.put("Description", "qeqeqwe");
		UrlInfomap.put("PicUrl", "dddddw");
		UrlInfomap.put("Url", "fffwe");
		messageInfomap.put("Articles", "");
		String messageInfo = MapToXml.getMapToXml(messageInfomap, UrlInfomap, "Articles");
		System.out.println(messageInfo);
	}
}
