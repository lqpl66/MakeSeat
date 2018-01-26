package com.yl.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.yl.Http.HttpsUtils;

import net.sf.json.JSONObject;

public class GetExistAccessToken {
	public static final String GET_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?";// 获取access
	// url
	public static final String APP_ID = "wxc9e385e97966ab61";
	public static final String grant_type = "client_credential";
	public static final String SECRET = "f5e754310bfbd50819172c7afeff25ab";
	//wxc9e385e97966ab61  服务号1
//	wxa095cf3d62e9ea06  服务号2
	// f5e754310bfbd50819172c7afeff25ab  服务号1
//f77ac9f341c8c7178dbcf27864e70ede   服务号2
	DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static Logger log = Logger.getLogger(GetExistAccessToken.class);

	// 读取XML文件中的数据
	public static String getToken() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String token = "";
		String token1 = "";
		String overTime = "";
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			// Document doc = db.parse(new FileInputStream(new
			// File("usr/local/files/XMLToken.xml")));
			String url = GetProperties.getXmlurl();
			Document doc = db.parse(new File(url));
			Element root = doc.getDocumentElement();
			NodeList list = root.getChildNodes();
			for (int i = 0; i < list.getLength(); i++) {
				Node Node = list.item(i);
				NodeList list1 = Node.getChildNodes();
				if (Node.getNodeName().equals("AccessToken")) {
					for (int j = 0; j < list1.getLength(); j++) {
						Node Node1 = list1.item(j);
						token = Node1.getNodeValue();
						token1 = Node1.getNodeValue();
					}
				}
				if (Node.getNodeName().equals("AccessExpires")) {
					for (int j = 0; j < list1.getLength(); j++) {
						Node Node1 = list1.item(j);
						overTime = Node1.getNodeValue();
						Date d = df.parse(overTime);
						Date d2 = new Date();
						// 超时两小时失效，重新请求access_token
						if (d.getTime() < d2.getTime()) {
							Map<String, String> params = new HashMap<String, String>();
							params.put("appid", APP_ID);
							params.put("secret", SECRET);
							params.put("grant_type", grant_type);
							String accessToken = HttpsUtils.getToken(GET_TOKEN_URL, params);// 获取token
							System.out.println("accessToken:"+accessToken);
							JSONObject js = JSONObject.fromObject(accessToken);
							if (js != null) {
								token1 = js.optString("access_token");
								Node pres = Node.getPreviousSibling().getPreviousSibling();
								NodeList list2 = pres.getChildNodes();
								if (pres.getNodeName().equals("AccessToken")) {
									for (int k = 0; k < list2.getLength(); k++) {
										Node Node3 = list2.item(k);
										Node3.setNodeValue(token1);
									}
								}
							}
							Calendar ca = Calendar.getInstance();
							ca.add(Calendar.HOUR_OF_DAY, 2);
							Node1.setNodeValue(df.format(ca.getTime()));
							log.info("access_token请求:" + token);
							log.info("失效时间请求:" + ca.getTime());
						}
					}
				}
			}
			if (!token.equals(token1)) {
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource domSource = new DOMSource(doc);
				// 设置编码类型
				transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
				StreamResult result = new StreamResult(new FileOutputStream(url));
				transformer.transform(domSource, result);
				token = token1;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return token;
	}

	public static void main(String args[]) {
		String d = getToken();
		System.out.println(d);
	}
}
