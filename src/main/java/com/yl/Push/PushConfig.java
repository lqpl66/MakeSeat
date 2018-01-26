package com.yl.Push;

public class PushConfig {
	// appkey：应用唯一标识。友盟消息推送服务提供的appkey和友盟统计分析平台使用的同一套appkey。
	// app_master_secret：服务器秘钥，用于服务器端调用API请求时对发送内容做签名验证。
	// device_token: 友盟消息推送服务对设备的唯一标识。Android的device_token是44位字符串,
	// iOS的device-token是64位。
	// alias: 开发者自有账号, 开发者可以在SDK中调用setAlias(alias,
	// alias_type)接口将alias+alias_type与device_token做绑定,
	// 之后开发者就可以根据自有业务逻辑筛选出alias进行消息推送。
	// 任务: 除单播、列播外的其它类型的播类型均称为任务。任务支持查询、撤销操作。
	// 通知-Android(notification): 消息送达到用户设备后，由友盟SDK接管处理并在通知栏上显示通知内容。
	// 消息-Android(message): 消息送达到用户设备后，消息内容透传给应用自身进行解析处理。
	// 通知/消息-iOS: 和APNs定义一致。
	// 测试模式: 在广播、组播等大规模发送消息的情况下，为了防止开发者误将测试消息大面积发给线上用户，特增加了测试模式。
	// 测试模式下，只会将消息发送给测试设备。测试设备需要到网站上手工添加。
	// 测试模式-Android: Android的测试设备是正式设备的一个子集
	// 测试模式-iOS: iOS的测试模式对应APNs的开发环境(sandbox),
	// 正式模式对应APNs的生产环境(prod)，测试设备和正式设备完全隔离。
	// 签名:
	// 为了保证调用API的请求是合法者发送且参数没有被篡改，需要在调用API时对发送的所有内容进行签名。签名附加在调用地址后面，签名的计算方式参见附录K。

//	public static String IOS_appkey = "5846295b75ca353187002286";
//	public static String IOS_app_master_secret = "wdnr81r7cw07ppxwckxnn5hyzhswdcv7";
//	
//	public static String Android_appkey = "587c5fbb8f4a9d501d000230";
//	public static String Android_app_master_secret = "jxtmejo8nepuuhkdetvmvhf23tgypsja";
	
	public static String IOS_appkey = "5a3224a28f4a9d7b25000102";
	public static String IOS_app_master_secret = "9ypyxunjajw1682podmsz84mf22sx9dq";
	
	public static String Android_appkey = "5a3212c1b27b0a4204000217";
	public static String Android_app_master_secret = "qihy2ywpkry37uouc40oyizvmfyqqt8n";
	public static String postUrl = "http://msg.umeng.com/api/send";

	// 单播(unicast): 向指定的设备发送消息，包括向单个device_token或者单个alias发消息。
	public static String unicast = "unicast";

	// 列播(listcast): 向指定的一批设备发送消息，包括向多个device_token或者多个alias发消息。
	public static String listcast = "listcast";

	// 组播(groupcast): 向满足特定条件的设备集合发送消息，例如:
	// "特定版本"、"特定地域"等。友盟消息推送所支持的维度筛选和友盟统计分析所提供的数据展示维度是一致的，后台数据也是打通的
	public static String groupcast = "groupcast";

	// 广播(broadcast): 向安装该App的所有设备发送消息。
	public static String broadcast = "broadcast";

	// 文件播(filecast): 开发者将批量的device_token或者alias存放到文件, 通过文件ID进行消息发送。
	public static String filecast = "filecast";

	// 自定义播(customizedcast): 开发者通过自有的alias进行推送,
	// 可以针对单个或者一批alias进行推送，也可以将alias存放到文件进行发送。
	public static String customizedcast = "customizedcast";

}
