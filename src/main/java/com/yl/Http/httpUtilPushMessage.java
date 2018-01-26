package com.yl.Http;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.yl.Push.PushConfig;
import com.yl.Push.UmengNotification;

@SuppressWarnings("deprecation")
public class httpUtilPushMessage {

	protected static HttpClient client = new DefaultHttpClient();

	@SuppressWarnings({ "finally" })
	public static boolean send(UmengNotification msg) throws Exception {
		BufferedReader rd = null;
		StringBuffer result = new StringBuffer();
		try {
			String timestamp = Integer.toString((int) (System.currentTimeMillis() / 1000));
			msg.setPredefinedKeyValue("timestamp", timestamp);
			String url = PushConfig.postUrl;
			String postBody = msg.getPostBody();
			String sign = DigestUtils.md5Hex(("POST" + url + postBody + msg.getAppMasterSecret()).getBytes("utf8"));
			url = url + "?sign=" + sign;
			HttpPost post = new HttpPost(url);
			post.setHeader("User-Agent", "Mozilla/5.0");
			post.setHeader("Connection", "close");
			StringEntity se = new StringEntity(postBody, "UTF-8");
			post.setEntity(se);
			// Send the post request and get the response
			HttpResponse response = client.execute(post);
			int status = response.getStatusLine().getStatusCode();
			System.out.println("Response Code : " + status);
			rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			System.out.println(result.toString());
			if (status == 200) {
				System.out.println("Notification sent successfully.");
			} else {
				System.out.println("Failed to send the notification!");
			}
			post.abort();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
//			client.getConnectionManager().shutdown();
			return true;
		}
	}

	public static String uploadContents(String appkey, String appMasterSecret, String string) {
		// TODO Auto-generated method stub
		return null;
	}

}
