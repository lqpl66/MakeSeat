package com.yl.Utils;

import org.apache.log4j.Logger;
import com.yunpian.sdk.model.ResultDO;
import com.yunpian.sdk.model.SendSingleSmsInfo;
import com.yunpian.sdk.service.SmsOperator;
import com.yunpian.sdk.service.YunpianRestClient;
import net.sf.json.JSONObject;

public class SMSAPI {
	private static Logger log = Logger.getLogger(SMSAPI.class);
	private static final String apikey = "1bc6be0279905504d32547975f9c3a0c";

	public static JSONObject Send(String smsCode, String mobile, int type) {
		JSONObject result = new JSONObject();
		String text = "";
		if (type == 0) {// 注册
			text = "【拼个座】欢迎使用拼个座APP应用，您的验证码是" + smsCode + "，有效时间5分钟，如非本人操作请忽略。";
		} else if (type == 1) {// 修改密码(找回)
			text = "【拼个座】您的验证码是" + smsCode + "，您正在进行密码修改操作，有效时间5分钟，如非本人操作建议立即更改账户密码。";
		} else if (type == 2) {// 支付密码
			text = "【拼个座】您的验证码是" + smsCode + "，您正在进行支付密码修改操作，有效时间5分钟，如非本人操作建议立即更改账户密码。";
		} else if (type == 3) {// 短信登录
			text = "【拼个座】您的短信登录验证码是" + smsCode + "，有效时间5分钟，如非本人操作请忽略。";
		} else if (type == 4) {// 身份信息
			text = "【拼个座】您的验证码是" + smsCode + "，有效时间5分钟，如非本人操作请忽略。";
		} else if (type == 5) {// 账号绑定
			text = "【拼个座】您的验证码是" + smsCode + "，您正在进行提现账号的修改操作，有效时间5分钟，如非本人操作请忽略。";
		} else {
			result.put("flag", false);
			result.put("message", "服务暂未开通！");
		}
		YunpianRestClient client = new YunpianRestClient(apikey);// 用apikey生成client,可作为全局静态变量
		SmsOperator smsOperator = client.getSmsOperator();// 获取所需操作类
		ResultDO<SendSingleSmsInfo> result_SMS = smsOperator.singleSend(mobile, text);
		JSONObject a = new JSONObject();
		if (result_SMS.isSuccess()) {
			result.put("flag", true);
		} else {
			Throwable s = result_SMS.getE();
			// System.err.println(s.getMessage());
			a = JSONObject.fromObject(s.getMessage());
			String code = a.optString("code");
			String message = returnMessage(code);
			result.put("flag", false);
			result.put("message", message);
		}
		log.info("云片短信回调信息：" + result_SMS);
		return result;
	}

	public static String returnMessage(String code) {
		String message = "";
		switch (code) {
		// 同一手机号30秒内重复提交相同的内容 请检查是否同一手机号在30秒内重复提交相同的内容
		case "8":
			message = "该手机号获取验证码30秒内不可重复提交,请稍候再试！";
			break;
		// 同一手机号5分钟内重复提交相同的内容超过3次 为避免重复发送骚扰用户，同一手机号5分钟内相同内容最多允许发3次
		case "9":
			message = "该手机号5分钟内不可重复获取验证超过3次,请稍候再试";
			break;
		// 手机号黑名单过滤 手机号在黑名单列表中（你可以把不想发送的手机号添加到黑名单列表）
		case "10":
			message = "您的手机号已进入黑名单，请联系客服！";
			break;
		// 24小时内同一手机号发送次数超过限制 请检查程序是否有异常或者系统是否被恶意攻击
		case "17":
			message = "24小时内该手机号发送次数已超过限制！";
			break;
		// 不支持的国家地区
		case "20":
			message = "暂不支持的国家地区的手机号";
			break;
		// 1小时内同一手机号发送次数超过限制
		case "22":
			message = "1小时内该手机号发送次数超过限制,请稍候再试";
			break;
		// 同一个手机号同一验证码模板每30秒只能发送一条。此规则用于防止验证码轰炸。 可在后台-系统设置了解详情及设置。
		case "33":
			message = "手机号同一验证码获取每30秒只能发送一条,请稍候再试！";
			break;
		default:
			message = "验证码获取失败，请稍候再试！";
			break;
		}
		return message;
	}

	public static void main(String args[]) {
		String smsCode = CodeUtils.getCode(6);
		JSONObject s = Send(smsCode, "18083751610", 2);
		System.out.println(s);
	}

}
