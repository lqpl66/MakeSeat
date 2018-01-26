package com.yl.Push;

import com.yl.Http.httpUtilPushMessage;
import com.yl.Push.Android.AndroidBroadcast;
import com.yl.Push.Android.AndroidCustomizedcast;
import com.yl.Push.Android.AndroidFilecast;
import com.yl.Push.Android.AndroidGroupcast;
import com.yl.Push.Android.AndroidUnicast;
import com.yl.Push.IOS.IOSBroadcast;
import com.yl.Push.IOS.IOSCustomizedcast;
import com.yl.Push.IOS.IOSFilecast;
import com.yl.Push.IOS.IOSGroupcast;
import com.yl.Push.IOS.IOSUnicast;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Demo {
	private String appkey = PushConfig.IOS_appkey;
	private String appMasterSecret = PushConfig.IOS_app_master_secret;
	private String timestamp = null;
	// private PushClient client = new PushClient();

//	public Demo(String key, String secret) {
//		try {
//			appkey = key;
//			appMasterSecret = secret;
//		} catch (Exception e) {
//			e.printStackTrace();
//			System.exit(1);
//		}
//	}

	public void sendAndroidBroadcast() throws Exception {
		AndroidBroadcast broadcast = new AndroidBroadcast(appkey, appMasterSecret);
		broadcast.setTicker("Android broadcast ticker");
		broadcast.setTitle("中文的title");
		broadcast.setText("Android broadcast text");
		broadcast.goAppAfterOpen();
		broadcast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
		// TODO Set 'production_mode' to 'false' if it's a test device.
		// For how to register a test device, please see the developer doc.
		broadcast.setProductionMode();
		// Set customized fields
		broadcast.setExtraField("test", "helloworld");
		httpUtilPushMessage.send(broadcast);
	}

	public void sendAndroidUnicast() throws Exception {
		AndroidUnicast unicast = new AndroidUnicast(PushConfig.Android_appkey, PushConfig.Android_app_master_secret);
		// TODO Set your device token  ApHKESOligD8daBfGUAlbr1WZZFOsnUxF6NjiQvTnk1Z
		//AtW64y4mNTP-aawzxaHfZa2htIexf4cAHHySMbRbbP8Z
		//An8NxPkTa-dwMNBerYNMyM7_MzcS5muA0UxGLCYc9Isl
		unicast.setDeviceToken("An8NxPkTa-dwMNBerYNMyM7_MzcS5muA0UxGLCYc9Isl");
		unicast.setTicker("Android unicast ticker");
		unicast.setTitle("中文的title");
		unicast.setText("Android unicast text");
		JSONObject aa = new JSONObject();
		aa.put("type", "1");
		aa.put("type1", "11");
//		unicast.setPredefinedKeyValue("extra", aa);
		unicast.goActivityAfterOpen("com.uto168.yl.activity.my.MessageListActivity");
		unicast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
		// TODO Set 'production_mode' to 'false' if it's a test device.
		// For how to register a test device, please see the developer doc.
		unicast.setProductionMode();
		// Set customized fields
		unicast.setExtraField("type", "1");
		unicast.setExtraField("type1", "12");
		unicast.setExtraField("type12", "123");
		unicast.setExtraField("type123", "1234");
		System.out.println(unicast.getPostBody());
		httpUtilPushMessage.send(unicast);
	}

	public void sendAndroidGroupcast() throws Exception {
		AndroidGroupcast groupcast = new AndroidGroupcast(appkey, appMasterSecret);
		/*
		 * TODO Construct the filter condition: "where": { "and": [
		 * {"tag":"test"}, {"tag":"Test"} ] }
		 */
		JSONObject filterJson = new JSONObject();
		JSONObject whereJson = new JSONObject();
		JSONArray tagArray = new JSONArray();
		JSONObject testTag = new JSONObject();
		JSONObject TestTag = new JSONObject();
		testTag.put("tag", "test");
		TestTag.put("tag", "Test");
		tagArray.add(testTag);
		tagArray.add(TestTag);
		whereJson.put("and", tagArray);
		filterJson.put("where", whereJson);
		System.out.println(filterJson.toString());

		groupcast.setFilter(filterJson);
		groupcast.setTicker("Android groupcast ticker");
		groupcast.setTitle("中文的title");
		groupcast.setText("Android groupcast text");
		groupcast.goAppAfterOpen();
		groupcast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
		// TODO Set 'production_mode' to 'false' if it's a test device.
		// For how to register a test device, please see the developer doc.
		groupcast.setProductionMode();
		httpUtilPushMessage.send(groupcast);
	}

	public void sendAndroidCustomizedcast() throws Exception {
		AndroidCustomizedcast customizedcast = new AndroidCustomizedcast(appkey, appMasterSecret);
		// TODO Set your alias here, and use comma to split them if there are
		// multiple alias.
		// And if you have many alias, you can also upload a file containing
		// these alias, then
		// use file_id to send customized notification.
		customizedcast.setAlias("alias", "alias_type");
		customizedcast.setTicker("Android customizedcast ticker");
		customizedcast.setTitle("中文的title");
		customizedcast.setText("Android customizedcast text");
		customizedcast.goAppAfterOpen();
		customizedcast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
		// TODO Set 'production_mode' to 'false' if it's a test device.
		// For how to register a test device, please see the developer doc.
		customizedcast.setProductionMode();
		httpUtilPushMessage.send(customizedcast);
	}

	public void sendAndroidCustomizedcastFile() throws Exception {
		AndroidCustomizedcast customizedcast = new AndroidCustomizedcast(appkey, appMasterSecret);
		// TODO Set your alias here, and use comma to split them if there are
		// multiple alias.
		// And if you have many alias, you can also upload a file containing
		// these alias, then
		// use file_id to send customized notification.
		String fileId = httpUtilPushMessage.uploadContents(appkey, appMasterSecret,
				"aa" + "\n" + "bb" + "\n" + "alias");
		customizedcast.setFileId(fileId, "alias_type");
		customizedcast.setTicker("Android customizedcast ticker");
		customizedcast.setTitle("中文的title");
		customizedcast.setText("Android customizedcast text");
		customizedcast.goAppAfterOpen();
		customizedcast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
		// TODO Set 'production_mode' to 'false' if it's a test device.
		// For how to register a test device, please see the developer doc.
		customizedcast.setProductionMode();
		httpUtilPushMessage.send(customizedcast);
	}

	public void sendAndroidFilecast() throws Exception {
		AndroidFilecast filecast = new AndroidFilecast(appkey, appMasterSecret);
		// TODO upload your device tokens, and use '\n' to split them if there
		// are multiple tokens
		String fileId = httpUtilPushMessage.uploadContents(appkey, appMasterSecret, "aa" + "\n" + "bb");
		filecast.setFileId(fileId);
		filecast.setTicker("Android filecast ticker");
		filecast.setTitle("中文的title");
		filecast.setText("Android filecast text");
		filecast.goAppAfterOpen();
		filecast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
		httpUtilPushMessage.send(filecast);
	}

	public void sendIOSBroadcast() throws Exception {
		IOSBroadcast broadcast = new IOSBroadcast(appkey, appMasterSecret);
		broadcast.setAlert("IOS 广播测试");
		broadcast.setBadge(0);
		broadcast.setSound("default");
		broadcast.setTestMode();
		broadcast.setCustomizedField("test", "helloworld");
		httpUtilPushMessage.send(broadcast);
	}

	public void sendIOSUnicast()  {
		try {
//			listcast
			IOSUnicast unicast = new IOSUnicast(appkey, appMasterSecret,"unicast");
			unicast.setDeviceToken("9ad9d777708123965a8593ffc5f74d21550f38de54e259a1c36c3f48e940e74a");
			unicast.setAlert("dasdad");
			unicast.setBadge(2);
			unicast.setSound("default");
			unicast.setTestMode();
//			unicast.setProductionMode(true);
			System.out.println(unicast.toString());
			httpUtilPushMessage.send(unicast);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void sendIOSGroupcast() throws Exception {
		IOSGroupcast groupcast = new IOSGroupcast(appkey, appMasterSecret);
		JSONObject filterJson = new JSONObject();
		JSONObject whereJson = new JSONObject();
		JSONArray tagArray = new JSONArray();
		JSONObject testTag = new JSONObject();
		testTag.put("tag", "iostest");
		tagArray.add(testTag);
		whereJson.put("and", tagArray);
		filterJson.put("where", whereJson);
		System.out.println(filterJson.toString());

		// Set filter condition into rootJson
		groupcast.setFilter(filterJson);
		groupcast.setAlert("IOS 组播测试");
		groupcast.setBadge(0);
		groupcast.setSound("default");
		// TODO set 'production_mode' to 'true' if your app is under production
		// mode
		groupcast.setTestMode();
		httpUtilPushMessage.send(groupcast);
	}

	public void sendIOSCustomizedcast() throws Exception {
		IOSCustomizedcast customizedcast = new IOSCustomizedcast(appkey, appMasterSecret);
		// TODO Set your alias and alias_type here, and use comma to split them
		// if there are multiple alias.
		// And if you have many alias, you can also upload a file containing
		// these alias, then
		// use file_id to send customized notification.
		customizedcast.setAlias("alias", "alias_type");
		customizedcast.setAlert("IOS 个性化测试");
		customizedcast.setBadge(0);
		customizedcast.setSound("default");
		// TODO set 'production_mode' to 'true' if your app is under production
		// mode
		customizedcast.setTestMode();
		httpUtilPushMessage.send(customizedcast);
	}

	public void sendIOSFilecast() throws Exception {
		IOSFilecast filecast = new IOSFilecast(appkey, appMasterSecret);
		// TODO upload your device tokens, and use '\n' to split them if there
		// are multiple tokens
		String fileId = httpUtilPushMessage.uploadContents(appkey, appMasterSecret, "aa" + "\n" + "bb");
		filecast.setFileId(fileId);
		filecast.setAlert("IOS 文件播测试");
		filecast.setBadge(0);
		filecast.setSound("default");
		// TODO set 'production_mode' to 'true' if your app is under production
		// mode
		filecast.setTestMode();
		httpUtilPushMessage.send(filecast);
	}

	public static void main(String[] args) {
		// TODO set your appkey and master secret here
//		Demo demo = new Demo("your appkey", "the app master secret");
		try {
//			demo.sendAndroidUnicast();
			Demo demo = new Demo();
			demo.sendIOSUnicast();
//			demo.sendAndroidUnicast();
//			demo.sendAndroidBroadcast();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
