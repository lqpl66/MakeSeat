package com.yl.Utils;

import java.util.Date;

import com.yl.Service.MessageService;

import net.sf.json.JSONObject;

/**
 * 
* @Description: 系统消息提示
* @date 2017年12月12日 下午2:53:48 
*
 */
public interface SystemMessageTemplate {

	/** 消息标题  */
	public interface title {
		public final static String title_ST000="您有新的指定座位信息";
		public final static String title_OD000="买家已支付";
		public final static String title_OD001="买家已取消";
		public final static String title_OD002="交易已完成";
		public final static String title_OD003="交易已关闭";
		public final static String title_OD004="买家已退款";
		public final static String title_AC_COMMON="余额变动通知";
		public final static String title_AC003="支付密码设置成功";
		public final static String title_AC004="登录密码设置成功";
		public final static String title_AC005="已提交身份验证信息";
		public final static String title_AC006="用户信息已修改";
		public final static String title_AC007="成功绑定支付宝";
	}

	/** 消息内容  */
	public interface content {
		public final static String content_ST000="座位信息";
		public final static String content_OD_COMMON="您发布的座位信息变化啦！";
	}
	public static void getContent(JSONObject paramsMap, JSONObject interimMap, JSONObject interimResultMap){
		String date=DateUtil.getCurrentTime("yyyy-MM-dd HH:mm:ss");
		String contentType=paramsMap.optString("contentType");
		String content="";
		interimResultMap.put("code", Constant.code.CODE_1);
		interimResultMap.put("msg", Constant.message.MESSAGE_1);
		switch (contentType) {
		case "content_ST000":
			content="座位信息";
			interimResultMap.put("data", content);
			break;
		case "content_OD_COMMON":
			content="您发布的座位信息变化啦！";
			interimResultMap.put("data", content);
			break;
		case "content_AC003":
			content="您的支付密码已于"+date+"设置成功，请牢记密码，并妥善保管。";
			interimResultMap.put("data", content);
			break;
		case "content_AC004":
			content="您的登陆密码已于"+date+"重置成功，请牢记密码，并妥善保管。";
			interimResultMap.put("data", content);
			break;
		case "content_AC005":
			content="您的身份认证信息已于"+date+"提交成功，如有疑问，请联系客服。";
			interimResultMap.put("data", content);
			break;
		case "content_AC006":
			content="您的用户信息信息已于"+date+"修改成功，如有疑问，请联系客服。";
			interimResultMap.put("data", content);
			break;
		case "content_AC007":
			content="您的支付宝信息已于"+date+"修改成功，如有疑问，请联系客服。";
			interimResultMap.put("data", content);
			break;
		default:
			interimResultMap.put("code", Constant.code.CODE_10);
			interimResultMap.put("msg", Constant.message.MESSAGE_10);
			break;
		}
	}
	/**
	 * <pre>
	* @Title: getAccountChangeContent 
	* @Description:  账户金额变动
	* 
	* @param paramsMap 请求参数集
	* 
	*      date      日期
	*      money     变动金额
	*      modelType 消息类型   
	*         AC000   订单交易成功    
	*         AC001   充值
	*         AC002   提现  
	* @param interimMap  临时参数集，结束清空
	* @param interimResultMap    返回结果集
	*        code  状态码
	*        msg   说明
	*        data  消息
	* 
	* </pre>
	*@returnType: void
	* 
	 */
	public static void getAccountChangeContent(JSONObject paramsMap, JSONObject interimMap, JSONObject interimResultMap){
		String date=DateUtil.getCurrentTime("yyyy-MM-dd HH:mm:ss");
		String money=paramsMap.optString("money");
		String modelType=paramsMap.optString("modelType");
		paramsMap.put("messageTitle", title.title_AC_COMMON);
		String content="";
		switch (modelType) {
		case "AC000"://订单交易成功
			content="您于"+date+"订单交易成功，获得"+money+"元，已存入余额";
			paramsMap.put("messageContent", content);
			break;
		case "AC001"://充值
			content="您于"+date+"充值成功，充值金额"+money+"元。";
			paramsMap.put("messageContent", content);
			break;
		case "AC002"://提现
			content="您于"+date+"提现成功，提现金额"+money+"元。";
			paramsMap.put("messageContent", content);
			break; 
		case "ACZF1"://支付
			content="您于"+date+"支付成功，支付金额"+money+"元。";
			paramsMap.put("messageContent", content);
			break; 
		case "ACTK1"://退款
			content="您于"+date+"退款成功，退款金额"+money+"元。";
			paramsMap.put("messageContent", content);
			break; 
		default:
			interimResultMap.put("code", Constant.code.CODE_10);
			interimResultMap.put("msg", Constant.message.MESSAGE_10);
			break;
		}
		
	}
 
}
