package com.yl.Utils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class GetProperties {
	/**
	 * 
	 * @Title: getFileUrl
	 * @Description:返回文件地址
	 * @param fileType
	 * @return String
	 */
	public static String getFileUrl(String fileType) {
		Resource resource = null;
		Properties props = null;
		String fileUrl = null;
		try {
			resource = new ClassPathResource("/resources.properties");
			props = PropertiesLoaderUtils.loadProperties(resource);
			fileUrl = (String) props.get(fileType);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileUrl;
	}

	public static String getXmlurl() {
		Resource resource = null;
		Properties props = null;
		String xmlurl = null;
		try {
			resource = new ClassPathResource("/resources.properties");
			props = PropertiesLoaderUtils.loadProperties(resource);
			xmlurl = (String) props.get("xmlurl");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return xmlurl;
	}

	// 用于返回数据时景区图片回显路径拼接
	public static String getscenicImgUrl() {
		Resource resource = null;
		Properties props = null;
		String fileUrl = null;
		try {
			resource = new ClassPathResource("/resources.properties");
			props = PropertiesLoaderUtils.loadProperties(resource);
			fileUrl = (String) props.get("scenicImgUrl");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileUrl;
	}

	public static String getServerUrl() {
		Resource resource = null;
		Properties props = null;
		String fileUrl = null;
		try {
			resource = new ClassPathResource("/resources.properties");
			props = PropertiesLoaderUtils.loadProperties(resource);
			fileUrl = (String) props.get("serverUrl");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileUrl;
	}

	public static String getNotify_url_Wechat_guide() {
		Resource resource = null;
		Properties props = null;
		String fileUrl = null;
		try {
			resource = new ClassPathResource("/resources.properties");
			props = PropertiesLoaderUtils.loadProperties(resource);
			fileUrl = (String) props.get("notify_url_Wechat_guide");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileUrl;
	}

	// refund_notify_url_guide
	public static String refund_notify_url_guide() {
		Resource resource = null;
		Properties props = null;
		String fileUrl = null;
		try {
			resource = new ClassPathResource("/resources.properties");
			props = PropertiesLoaderUtils.loadProperties(resource);
			fileUrl = (String) props.get("refund_notify_url_guide");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileUrl;
	}

	public static BigDecimal refund_deductionPrice() {
		Resource resource = null;
		Properties props = null;
		String fileUrl = null;
		BigDecimal dd = null;
		try {
			resource = new ClassPathResource("/resources.properties");
			props = PropertiesLoaderUtils.loadProperties(resource);
			fileUrl = (String) props.get("deductionPrice");
			dd = new BigDecimal(fileUrl);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return dd;
	}

	public static String getAppID() {
		Resource resource = null;
		Properties props = null;
		String appID = null;
		try {
			resource = new ClassPathResource("/resources.properties");
			props = PropertiesLoaderUtils.loadProperties(resource);
			appID = (String) props.get("appID");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return appID;
	}

	public static String getPublicKey() {
		Resource resource = null;
		Properties props = null;
		String publicKey = null;
		try {
			resource = new ClassPathResource("/resources.properties");
			props = PropertiesLoaderUtils.loadProperties(resource);
			publicKey = (String) props.get("publicKey");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return publicKey;
	}

	public static String getPrivateKey() {
		Resource resource = null;
		Properties props = null;
		String privateKey = null;
		try {
			resource = new ClassPathResource("/resources.properties");
			props = PropertiesLoaderUtils.loadProperties(resource);
			privateKey = (String) props.get("privateKey");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return privateKey;
	}

	public static String getSellerId() {
		Resource resource = null;
		Properties props = null;
		String sellerID = null;
		try {
			resource = new ClassPathResource("/resources.properties");
			props = PropertiesLoaderUtils.loadProperties(resource);
			sellerID = (String) props.get("sellerID");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sellerID;
	}

	public static String getNotify_url_Alipay_expense() {
		Resource resource = null;
		Properties props = null;
		String notify_url_Alipay_expense = null;
		try {
			resource = new ClassPathResource("/resources.properties");
			props = PropertiesLoaderUtils.loadProperties(resource);
			notify_url_Alipay_expense = (String) props.get("notify_url_Alipay_expense");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return notify_url_Alipay_expense;
	}

	public static String getNotify_url_Alipay_recharge() {
		Resource resource = null;
		Properties props = null;
		String notify_url_Alipay_recharge = null;
		try {
			resource = new ClassPathResource("/resources.properties");
			props = PropertiesLoaderUtils.loadProperties(resource);
			notify_url_Alipay_recharge = (String) props.get("notify_url_Alipay_recharge");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return notify_url_Alipay_recharge;
	}

	public static String getNotify_url_Alipay_card() {
		Resource resource = null;
		Properties props = null;
		String notify_url_Alipay_card = null;
		try {
			resource = new ClassPathResource("/resources.properties");
			props = PropertiesLoaderUtils.loadProperties(resource);
			notify_url_Alipay_card = (String) props.get("notify_url_Alipay_card");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return notify_url_Alipay_card;
	}

	public static String getNotify_url_Wechat_expense() {
		Resource resource = null;
		Properties props = null;
		String notify_url_Wechat_expense = null;
		try {
			resource = new ClassPathResource("/resources.properties");
			props = PropertiesLoaderUtils.loadProperties(resource);
			notify_url_Wechat_expense = (String) props.get("notify_url_Wechat_expense");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return notify_url_Wechat_expense;
	}

	public static String getNotify_url_Wechat_recharge() {
		Resource resource = null;
		Properties props = null;
		String notify_url_Wechat_recharge = null;
		try {
			resource = new ClassPathResource("/resources.properties");
			props = PropertiesLoaderUtils.loadProperties(resource);
			notify_url_Wechat_recharge = (String) props.get("notify_url_Wechat_recharge");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return notify_url_Wechat_recharge;
	}

	public static String getNotify_url_Wechat_card() {
		Resource resource = null;
		Properties props = null;
		String notify_url_Wechat_card = null;
		try {
			resource = new ClassPathResource("/resources.properties");
			props = PropertiesLoaderUtils.loadProperties(resource);
			notify_url_Wechat_card = (String) props.get("notify_url_Wechat_card");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return notify_url_Wechat_card;
	}

	public static String getPath() {
		Resource resource = null;
		Properties props = null;
		String url = null;
		try {
			resource = new ClassPathResource("/resources.properties");
			props = PropertiesLoaderUtils.loadProperties(resource);
			url = (String) props.get("path");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return url;
	}

	public static String getPGZAdUrl() {
		Resource resource = null;
		Properties props = null;
		String url = null;
		try {
			resource = new ClassPathResource("/resources.properties");
			props = PropertiesLoaderUtils.loadProperties(resource);
			url = (String) props.get("pGZAdUrl");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return url;
	}

}
