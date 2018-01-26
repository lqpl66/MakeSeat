package com.yl.Utils;

public interface Constant {
	// 0000:服务繁忙
	// 0001:操作成功
	// 0002：参数不全
	// 0003:手机号已被注册！
	// 0004: 短信服务端异常，返回第三方错误信息
	// 0005: 该手机号暂未注册，请注册
	// 0006：该账号不是子管理员账号,无法登录该APP
	// 0007:密码过于简单，请重新设置密码
	// 0008:两次密码不一致，请重新设置密码
	// 0009: 订单为无效订单，无法查看
	// 0010: 参数不合法
	public interface code {
		public static final String CODE = "code";
		/** 服务繁忙！ */
		public static final String CODE_0 = "0000";
		/** 操作成功！ */
		public static final String CODE_1 = "0001";
		/** 参数不全！ */
		public static final String CODE_2 = "0002";
		/** 手机号已被注册！ */
		public static final String CODE_3 = "0003";
		/** 短信服务端异常，返回第三方错误信息 */
		public static final String CODE_4 = "0004";
		/** 该手机号暂未注册，请注册 */
		public static final String CODE_5 = "0005";
		/** 帐号已冻结，请联系管理员 */
		public static final String CODE_6 = "0006";
		/** 密码请保持在6~20位 */
		public static final String CODE_7 = "0007";
		/** 密码错误 */
		public static final String CODE_8 = "0008";
		/** 验证码错误 */
		public static final String CODE_9 = "0009";
		/** 参数不合法 */
		public static final String CODE_10 = "0010";
		/** 暂未提供该服务 */
		public static final String CODE_11 = "0011";
		/** 暂无该用户信息 */
		public static final String CODE_12 = "0012";
		/** 身份校验失败 */
		public static final String CODE_13 = "0013";
		/** 登录失效，请重新登录 */
		public static final String CODE_14 = "0014";
		/** 新密码不能和历史密码相同，请重新设置新密码 */
		public static final String CODE_15 = "0015";
		/** 原密码错误，请重新输入 */
		public static final String CODE_16 = "0016";
		/** 车次信息有误，无法发布求购信息，请稍候操作 */
		public static final String CODE_17 = "0017";
		/** 求购订单信息有误，无权操作 */
		public static final String CODE_18 = "0018";
		/** 验证码已失效 */
		public static final String CODE_19 = "0019";
		/** 暂不支持该类型车次，请重新操作 */
		public static final String CODE_20 = "0020";
		/** 上传图片失败 */
		public static final String CODE_21 = "0021";
		/** 身份证已使用 */
		public static final String CODE_22 = "0022";
		/** 该功能暂未开放 */
		public static final String CODE_23 = "0023";
		/** 车次信息有误，无法发布座位信息，请稍候操作 */
		public static final String CODE_24 = "0024";
		/** 座位信息不符合规定，请重新操作 */
		public static final String CODE_25 = "0025";
		/** 指定购买人信息异常，无法指定，请重新操作 */
		public static final String CODE_26 = "0026";
		/** 站点不可重复发布，请重新操作 */
		public static final String CODE_27 = "0027";
		/** 座位信息异常，无法购买 */
		public static final String CODE_28 = "0028";
		/** 用户不具备购买该座位资格，无法购买 */
		public static final String CODE_29 = "0029";
		/** 购买的站点不符合规定，无法购买 */
		public static final String CODE_30 = "0030";
		/** 指定购买人信息与当前查看人信息不符，无法查看 */
		public static final String CODE_31 = "0031";
		/** 指定购买人信息与当前购买人信息不符，无法购买 */
		public static final String CODE_32 = "0032";
		/** 座位信息异常，无法查看 */
		public static final String CODE_33 = "0033";
		/** 购买座位订单不存在，无法查看 */
		public static final String CODE_34 = "0034";
		/** 订单信息异常，无法完成交易 */
		public static final String CODE_35 = "0035";
		/** 当天提现次数达到上限，无法提现！ */
		public static final String CODE_36 = "0036";
		/** 余额不足，无法提现！ */
		public static final String CODE_37 = "0037";
		/** 提现账号校验失败，不可提现！ */
		public static final String CODE_38 = "0038";
		/** 提现账号不存在，不可提现！ */
		public static final String CODE_39 = "0039";
		/** 提现失败！ */
		public static final String CODE_40 = "0040";
		/** 提现成功！ */
		public static final String CODE_41 = "0041";
		/** 发布者不可购买自身发布座位信息，无法购买 */
		public static final String CODE_42 = "0042";
		/** 支付密码错误 */
		public static final String CODE_43 = "0043";
		/** 座位信息有误，无权操作 */
		public static final String CODE_44 = "0044";
		/** 请设置支付密码 */
		public static final String CODE_45 = "0045";
		/** 余额不足请充值 */
		public static final String CODE_46 = "0046";
		/** 发布者不可指定自身发布座位信息，无法发布 */
		public static final String CODE_47 = "0047";
		/** 帐号未绑定设备 */
		public static final String CODE_48 = "0048";
		/** 余额信息错误，请联系客服 */
		public static final String CODE_49 = "0049";
		/** 请绑定提现帐号 */
		public static final String CODE_50 = "0050";
		/** 提现金额不能低于0.11元 */
		public static final String CODE_51 = "0051";
		/** 不是合法的手机号 */
		public static final String CODE_52 = "0052";
		/** 不是合法的邮箱 */
		public static final String CODE_53 = "0053";
		/** 不是合法的座位号*/
		public static final String CODE_54 = "0054";
		/** 用户当天退款次数达到上限，无法购买新的座位*/
		public static final String CODE_55 = "0055";
		/** 该功能不可用 */
		public static final String CODE_56 = "0056";
	}

	// 0000:服务繁忙
	// 0001:操作成功
	// 0002：参数不全
	// 0003:手机号已被注册！
	// 0004:账号或密码错误,无法登录
	// 0005: 该手机号暂未注册，请注册
	// 0006：该账号不是子管理员账号,无法登录该APP
	// 0007:密码过于简单，请重新设置密码
	// 0008:两次密码不一致，请重新设置密码
	// 0009: 订单为无效订单,请联系景区管理员
	// 0010: 订单为无效订单,请联系景区管理员
	public interface message {
		public static final String MESSAGE = "msg";
		public static final String MESSAGE_0 = "服务繁忙";
		public static final String MESSAGE_1 = "操作成功";
		public static final String MESSAGE_2 = "参数不全";
		public static final String MESSAGE_3 = "手机号已被注册！";
		public static final String MESSAGE_4 = "短信服务端异常，返回第三方错误信息";
		public static final String MESSAGE_5 = "该手机号暂未注册，请注册";
		public static final String MESSAGE_6 = "帐号已冻结，请联系管理员";
		public static final String MESSAGE_7 = "密码请保持在6~20位 ";
		public static final String MESSAGE_8 = "密码错误";
		public static final String MESSAGE_9 = "验证码错误";
		public static final String MESSAGE_10 = "参数不合法";
		public static final String MESSAGE_11 = "暂未提供该服务";
		public static final String MESSAGE_12 = "暂无该用户信息";
		public static final String MESSAGE_13 = "身份校验失败";
		public static final String MESSAGE_14 = "登录失效，请重新登录";
		public static final String MESSAGE_15 = "新密码不能和历史密码相同，请重新设置新密码";
		public static final String MESSAGE_16 = "原密码错误，请重新输入";
		public static final String MESSAGE_17 = "车次信息有误，无法发布求购信息，请稍候操作";
		public static final String MESSAGE_18 = "求购订单信息有误，无权操作";
		public static final String MESSAGE_19 = "验证码已失效";
		public static final String MESSAGE_20 = "暂不支持该类型车次，请重新操作";
		public static final String MESSAGE_21 = "上传图片失败";
		public static final String MESSAGE_22 = "身份证已使用";
		public static final String MESSAGE_23 = "该功能暂未开放";

		public static final String MESSAGE_24 = "车次信息有误，无法发布座位信息，请稍候操作";
		public static final String MESSAGE_25 = "座位信息不符合规定，请重新操作";
		public static final String MESSAGE_26 = "指定购买人信息异常，无法指定，请重新操作";
		public static final String MESSAGE_27 = "站点不可重复发布，请重新操作";
		public static final String MESSAGE_28 = "座位信息异常，无法购买";
		public static final String MESSAGE_29 = "用户不具备购买该座位资格，无法购买";
		public static final String MESSAGE_30 = "购买的站点不符合规定，无法购买";
		public static final String MESSAGE_31 = "指定购买人信息与当前查看人信息不符，无法查看";
		public static final String MESSAGE_32 = "指定购买人信息与当前购买人信息不符，无法购买";
		public static final String MESSAGE_33 = "座位信息异常，无法查看";
		public static final String MESSAGE_34 = "购买座位订单不匹配，无法查看";
		public static final String MESSAGE_35 = "订单信息异常，无法完成交易";
		public static final String MESSAGE_36 = "当天提现次数达到上限，无法提现！";
		public static final String MESSAGE_37 = "余额不足，无法提现！";
		public static final String MESSAGE_38 = "提现账号校验失败，不可提现！";
		public static final String MESSAGE_39 = "提现账号不存在，不可提现！";
		public static final String MESSAGE_40 = "提现失败！";
		public static final String MESSAGE_41 = "提现成功！";
		public static final String MESSAGE_42 = "发布者不可购买自身发布座位信息，无法购买";
		public static final String MESSAGE_43 = "支付密码错误";
		public static final String MESSAGE_44 = "座位信息有误，无权操作";
		public static final String MESSAGE_45 = "请设置支付密码";
		public static final String MESSAGE_46 = "余额不足请充值";
		public static final String MESSAGE_47 = "发布者不可指定自身发布座位信息，无法发布";
		public static final String MESSAGE_48 = "帐号未绑定设备";
		public static final String MESSAGE_49 = "余额信息错误，请联系客服";
		public static final String MESSAGE_50 = "请绑定提现帐号";
		public static final String MESSAGE_51 = "提现金额不能低于0.11元";
		public static final String MESSAGE_52 = "不是合法的手机号";
		public static final String MESSAGE_53 = "不是合法的邮箱";
		public static final String MESSAGE_54 = "不是合法的座位号";
		public static final String MESSAGE_55 = "用户当天退款次数达到上限，无法购买新的座位";
		public static final String MESSAGE_56 = "该功能不可用";

	}

}
