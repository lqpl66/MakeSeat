 package com.yl.pay.Wechat;

public class WechatConfig {
	 /**
	    * 预支付请求地址
	    * https://api.mch.weixin.qq.com/pay/unifiedorder
	    */
	   public static final String  PrepayUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	   /**
	    * 企业转账
	    * https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers
	    */
	   public static final String  TransfersUrl = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";
	   /**
	    * 商户APPID
	    */
	   public static final String  AppId = "wxbdcfe1827f998833";

	   /**
	    * 小程序APPID
	    */
	   public static final String  XCX_AppId = "wxed4ffcd582b29e5b";
	   /**
	    * 商户账户 
	    */
	   public static final String  MchId = "1393632002";

	   /**
	    * 商户秘钥  32位，在微信商户平台中设置
	    */
	   public static final String  AppSercret = "Z0ivPhjvCydn2sW9VjIosGaSbQ7Zkvl2";
	   /**
	    * 小程序秘钥
	    */
	   public static final String  XCX_AppSercret = "8cda457d7e0f43ba8f500c079914fc5d";
	   /**
	    * 查询订单地址
	    */
	   public static final String  OrderUrl = "https://api.mch.weixin.qq.com/pay/orderquery";

	   /**
	    * 关闭订单地址
	    */
	   public static final String  CloseOrderUrl = "https://api.mch.weixin.qq.com/pay/closeorder";

	   /**
	    * 申请退款地址
	    */
	   public static final String  RefundUrl = "https://api.mch.weixin.qq.com/secapi/pay/refund";

	   /**
	    * 查询退款地址
	    */
	   public static final String  RefundQueryUrl = "https://api.mch.weixin.qq.com/pay/refundquery";

	   /**
	    * 下载账单地址
	    */
	   public static final String  DownloadBillUrl = "https://api.mch.weixin.qq.com/pay/downloadbill";

	   /**
	    * 服务器异步通知页面路径
	    */
	   public static String notify_url_1 = "http://wxpay.rongyuecar.com:18080/YlServer/mvc/Wechatpay/expenseNotify";
	   public static String notify_url_2 = "http://wxpay.rongyuecar.com:18080/YlServer/mvc/Wechatpay/rechargeNotify";
	   /**
	    * 页面跳转同步通知页面路径
	    */
//	   public static String return_url = getProperties().getProperty("return_url");

	   /**
	    * 退款通知地址
	    */
//	   public static String refund_notify_url = getProperties().getProperty("refund_notify_url");

	   /**
	    * 退款需要证书文件，证书文件的地址
	    */
//	   public static String refund_file_path = getProperties().getProperty("refund_file_path");

	   /**
	    * 商品名称
	    */
//	   public static String subject =  getProperties().getProperty("subject");

	   /**
	    * 商品描述
	    */
//	   public static String body = "佑途特产";
//
//	   private static  Properties properties;
//
//	   public static synchronized Properties getProperties(){
//	      if(properties == null){
//	         String path = System.getenv(RSystemConfig.KEY_WEB_HOME_CONF) + "/weichart.properties";
//	         properties = PropertiesUtil.getInstance().getProperties(path);
//	      }
//	      return properties;
//	   }

//	}
}
