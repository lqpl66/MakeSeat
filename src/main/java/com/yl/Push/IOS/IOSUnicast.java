package com.yl.Push.IOS;

import com.yl.Push.IOSNotification;

public class IOSUnicast extends IOSNotification {
	public IOSUnicast(String appkey, String appMasterSecret,String type) throws Exception {
		setAppMasterSecret(appMasterSecret);
		setPredefinedKeyValue("appkey", appkey);
		this.setPredefinedKeyValue("type", type);
	}

	public void setDeviceToken(String token) throws Exception {
		setPredefinedKeyValue("device_tokens", token);
	}
}
