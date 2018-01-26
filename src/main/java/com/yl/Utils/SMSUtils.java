package com.yl.Utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.apache.log4j.Logger;
import com.yl.Utils.ParseXml;;
public class SMSUtils {
	private static final String addr = "http://120.55.197.77:1210/Services/MsgSend.asmx/SendMsg";
	private static final String userPass = "jrycf319";
	private static final String userCode = "jrycf";
	private static Logger log = Logger.getLogger(SMSUtils.class);

	// userCode=string&userPass=string&DesNo=string&Msg=string&Channel=string
	public static boolean send(String smsCode, String mobile) throws Exception {
		// 【佑途】您的验证码是123456。如非本人操作，请忽略本条短信。
		// 组建请求
		String straddr = addr + "?userCode=" + userCode + "&userPass=" + userPass + "&DesNo=" + mobile + "&Msg=您的验证码是"
				+ smsCode + "。如非本人操作，请忽略本条短信。【巨如意】" + "&Channel=0";
		StringBuffer sb = new StringBuffer(straddr);
		boolean flag = false;
		System.out.println("URL:" + sb);
		HttpURLConnection connection = null;
		try {
			// 发送请求
			URL url = new URL(sb.toString());
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			// 请求返回内容
			InputStreamReader isr = new InputStreamReader(connection.getInputStream(), "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			StringBuilder xmlstring = new StringBuilder();
			String str = null;
			while ((str = br.readLine()) != null) {
				xmlstring.append(str + "\n");
			}
			br.close();
			isr.close();
			String getString = ParseXml.xmlElements(xmlstring.toString());
			if (getString.length() > 3) {
				flag = true;
			} else {
				flag = false;
			}
		} catch (Exception e) {
			log.error("短信接口："+e);

		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		return flag;
	}
	private static final String addr1 = "http://localhost:8080/YlServer/us/getcheckcode";
	public static boolean send1(String userName, String codetype) throws Exception {
		String straddr = addr1+ "?userName=" + userName + "&codetype=" + codetype;
		StringBuffer sb = new StringBuffer(straddr);
		boolean flag = false;
		System.out.println("URL:" + sb);
		HttpURLConnection connection = null;
		try {
			// 发送请求
			URL url = new URL(sb.toString());
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			// 请求返回内容
			InputStreamReader isr = new InputStreamReader(connection.getInputStream(), "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			StringBuilder xmlstring = new StringBuilder();
			String str = null;
			while ((str = br.readLine()) != null) {
				xmlstring.append(str + "\n");
			}
			br.close();
			isr.close();
			System.out.println("fanghui:::"+xmlstring);
			String getString = ParseXml.xmlElements(xmlstring.toString());
//			if (getString.length() > 3) {
//				flag = true;
//			} else {
//				flag = false;
//			}
		} catch (Exception e) {

		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		return flag;
	}
	
	
	
	
	public static void main(String[] args) {
		try {
			boolean flag = send1("1865164125", "1");
			System.out.println("flag" + flag);
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
	}

}
