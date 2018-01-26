package com.yl.pay.Alipay.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConstants;
import com.alipay.api.DefaultAlipayClient;

/**
 * 创建时间：2016年11月10日 下午7:09:08
 * 
 * alipay支付
 * 
 * @author andy
 * @version 2.2
 */

public class AlipayUtil {

	public static final String ALIPAY_APPID = ConfigUtil.getProperty("alipay.appid"); // appid

	public static String APP_PRIVATE_KEY = null; // app支付私钥

	public static String ALIPAY_PUBLIC_KEY = null; // 支付宝公钥

	static {
		try {
			Resource resource = new ClassPathResource("alipay_private_key_pkcs8.pem");
			APP_PRIVATE_KEY = FileUtil.readInputStream2String(resource.getInputStream());
			resource = new ClassPathResource("alipay_public_key.pem");
			ALIPAY_PUBLIC_KEY = FileUtil.readInputStream2String(resource.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 统一收单交易创建接口
	private static AlipayClient alipayClient = null;

	public static AlipayClient getAlipayClient() {
		if (alipayClient == null) {
			synchronized (AlipayUtil.class) {
				if (null == alipayClient) {
					alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", ALIPAY_APPID,
							APP_PRIVATE_KEY, AlipayConstants.FORMAT_JSON, AlipayConstants.CHARSET_UTF8,
							ALIPAY_PUBLIC_KEY);
				}
			}
		}
		return alipayClient;
	}
}
