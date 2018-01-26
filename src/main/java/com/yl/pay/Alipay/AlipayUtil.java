package com.yl.pay.Alipay;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConstants;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayFundTransToaccountTransferRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.yl.Utils.GetProperties;

import net.sf.json.JSONObject;

/**
 * 
 * alipay支付
 * 
 */

public class AlipayUtil {
	private static Logger log = Logger.getLogger(AlipayUtil.class);
	public static final String ALIPAY_APPID = GetProperties.getAppID(); // appid
	public static String APP_PRIVATE_KEY = GetProperties.getPrivateKey();// app支付私钥
	public static String ALIPAY_PUBLIC_KEY = GetProperties.getPublicKey(); // 支付宝公钥

	public static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	//
	// static {
	// try {
	// Resource resource = new
	// ClassPathResource("alipay_private_key_pkcs8.pem");
	// APP_PRIVATE_KEY =
	// FileUtil.readInputStream2String(resource.getInputStream());
	// resource = new ClassPathResource("alipay_public_key.pem");
	// ALIPAY_PUBLIC_KEY =
	// FileUtil.readInputStream2String(resource.getInputStream());
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

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

	/**
	 * 
	 * <pre>
	* &#64;Title: orderString 
	* &#64;Description:  
	* &#64;param tradeNo 订单编号
	* &#64;param total_amount  金额
	* &#64;param type 1：购买座位；2：充值； 
	* &#64;param body 消息内容
	* &#64;return
	 * </pre>
	 * 
	 * @returnType: String
	 */
	public static String orderString(String tradeNo, String total_amount, Integer type, String body) {
		String orderString = null;
		Map<String, String> param = new HashMap<>();
		Map<String, String> pcont = new HashMap<>();
		// 公共请求参数
		param.put("app_id", AlipayUtil.ALIPAY_APPID);// 商户订单号
		param.put("method", "alipay.trade.app.pay");// 交易金额
		param.put("format", AlipayConstants.FORMAT_JSON);
		param.put("charset", AlipayConstants.CHARSET_UTF8);
		param.put("timestamp", df.format(new Date()));
		param.put("version", "1.0");
		if (type == 2) {
			param.put("notify_url", GetProperties.getFileUrl("notify_url_Alipay_recharge"));
			pcont.put("enable_pay_channels", "balance,moneyFund,debitCardExpress");
		} else if (type == 1) {
			param.put("notify_url", GetProperties.getFileUrl("notify_url_Alipay_expense"));
		}
		param.put("sign_type", AlipayConstants.SIGN_TYPE_RSA2);
		// 支付业务请求参数
		pcont.put("out_trade_no", tradeNo); // 商户订单号
		pcont.put("total_amount", total_amount);// 交易金额
		pcont.put("subject", "上海佑途物联网有限公司"); // 订单标题
		pcont.put("body", body);// 对交易或商品的描述
		pcont.put("timeout_express", "30m");
		pcont.put("product_code", "QUICK_MSECURITY_PAY");
		JSONObject biz_content = JSONObject.fromObject(pcont);
		System.out.println(biz_content.toString());
		param.put("biz_content", biz_content.toString()); // 业务请求参数
		try {
			param.put("sign", PayUtil.getSign(param, APP_PRIVATE_KEY)); // 业务请求参数
			orderString = PayUtil.getSignEncodeUrl(param, true);
			System.out.println(orderString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return orderString;
	}

	public static JSONObject getWithdraw(String expenseUserNo, String total_amount, String payee_account) {
		JSONObject orderString = new JSONObject();
		try {
			AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", ALIPAY_APPID,
					APP_PRIVATE_KEY, "json", "utf-8", ALIPAY_PUBLIC_KEY, "RSA2");
			AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();
			JSONObject cc = new JSONObject();
			cc.put("out_biz_no", expenseUserNo);
			cc.put("payee_type", "ALIPAY_LOGONID");
			cc.put("payee_account", payee_account);
			cc.put("amount", total_amount);
			cc.put("payer_show_name", "上海佑途物联网");
			cc.put("remark", "提现");
			request.setBizContent(cc.toString());
			AlipayFundTransToaccountTransferResponse response = alipayClient.execute(request);
			if (response.isSuccess()) {
				log.info("提现接口成功：" + response.getBody());
				System.out.println("提现成功");
				orderString.put("serialNo", response.getOrderId());
				orderString.put("expenseUserNo", response.getOutBizNo());
				orderString.put("code", "0001");
			} else {
				log.info("提现接口失败：" + response.getBody());
				System.out.println("提现失败");
				orderString.put("code", "0000");
			}
		} catch (AlipayApiException e) {
			e.printStackTrace();
			log.error("提现接口异常：", e);
			orderString.put("code", "0000");
		}
		return orderString;
	}

	public static JSONObject getRefund(String tradeNo, String total_amount,String out_request_no) {
		JSONObject orderString = new JSONObject();
		Map<String, String> param = new HashMap<>();
		Map<String, String> pcont = new HashMap<>();
		AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", ALIPAY_APPID,
				APP_PRIVATE_KEY, "json", "utf-8", ALIPAY_PUBLIC_KEY, "RSA2");
		AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
		// 支付业务请求参数
		pcont.put("out_trade_no", tradeNo); // 商户订单号
		pcont.put("refund_amount", total_amount);// 交易金额
		pcont.put("refund_reason", "购买座位商品退款"); // 订单标题
		pcont.put("out_request_no", out_request_no);//多笔退款时必传
		JSONObject biz_content = JSONObject.fromObject(pcont);
		System.out.println(biz_content.toString());
		request.setBizContent(biz_content.toString());
		AlipayTradeRefundResponse response;
		try {
			response = alipayClient.execute(request);
		if (response.isSuccess()) {
			log.info("退款接口成功：" + response.getBody());
			orderString.put("orderNo", response.getOutTradeNo());//平台订单号
			orderString.put("serialNo", response.getTradeNo());//支付宝交易号
			orderString.put("refund_fee", response.getRefundFee());
			orderString.put("code", "0001");
		} else {
			log.info("退款接口失败：" + response.getBody());
			orderString.put("code", "0000");
			orderString.put("msg", response.getSubMsg());
		}
		} catch (AlipayApiException e) {
			log.error("退款接口异常：" + e);
			e.printStackTrace();
			orderString.put("code", "0000");
			orderString.put("msg", "服务繁忙");
		}
		return orderString;
	}

	public static void main(String args[]) throws Exception {
		// AlipayClient alipayClient = new
		// DefaultAlipayClient("https://openapi.alipay.com/gateway.do",
		// ALIPAY_APPID,
		// APP_PRIVATE_KEY,
		// "json", "utf-8",
		// ALIPAY_PUBLIC_KEY,
		// "RSA");
		// // AlipayClient aa = new D
		// AlipayFundTransToaccountTransferRequest request = new
		// AlipayFundTransToaccountTransferRequest();
		// JSONObject cc = new JSONObject();
		// cc.put("out_biz_no", "123456789011");
		// cc.put("payee_type", "ALIPAY_LOGONID");
		// cc.put("payee_account", "18651641256");
		// cc.put("amount", "0.01");
		// cc.put("payer_show_name", "给钱");
		// cc.put("remark", "提现");
		// request.setBizContent(cc.toString());
		// AlipayFundTransToaccountTransferResponse response =
		// alipayClient.execute(request);
		// if (response.isSuccess()) {
		// System.out.println(response.getBody());
		// log.info("提现接口成功："+response.getBody());
		// System.out.println("调用成功");
		// } else {
		// log.info("提现接口失败："+response.getBody());
		// System.out.println("调用失败");
		// }
		// JSONObject orderString =
		getWithdraw("158asd921", "0.1", "852077574@qq.com");
		// System.out.println(orderString);
		// Double totalAmount = 0.1;
		// BigDecimal dd = new BigDecimal(totalAmount);
		// System.out.println(dd.compareTo(new BigDecimal(0.10)));
		// if (dd.compareTo(new BigDecimal(0.10)) == -1) {
		// System.out.println("11");
		// }
		// 
		JSONObject orderString = getRefund("18519141515387072537", "0.02",null);
		System.out.println(orderString.toString());
	}

}
