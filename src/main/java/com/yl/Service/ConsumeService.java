package com.yl.Service;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.alipay.api.AlipayConstants;
import com.alipay.api.internal.util.AlipaySignature;
import com.google.gson.JsonObject;
import com.yl.Mail.MailConfig;
import com.yl.Mail.MailUtil;
import com.yl.Utils.CodeUtils;
import com.yl.Utils.CommonDateParseUtil;
import com.yl.Utils.Constant;
import com.yl.Utils.DateUtil;
import com.yl.Utils.GetProperties;
import com.yl.Utils.MD5Utils;
import com.yl.Utils.MSeatUtil;
import com.yl.Utils.RegexUtil;
import com.yl.Utils.SystemMessageTemplate.content;
import com.yl.bean.Admin;
import com.yl.bean.ExpenseUserLog;
import com.yl.bean.Mail;
import com.yl.bean.Order;
import com.yl.bean.OrderSellInfo;
import com.yl.bean.OrderSellStation;
import com.yl.bean.OrderStation;
import com.yl.bean.TemporaryOrder;
import com.yl.bean.UserPayAccount;
import com.yl.bean.Userinfo;
import com.yl.mapper.EmployeeMapper;
import com.yl.mapper.ExpenseMapper;
import com.yl.mapper.TrainOrderMapper;
import com.yl.pay.Alipay.AlipayUtil;
import com.yl.pay.Wechat.WechatConfig;
import net.sf.json.JSONObject;

@Service
public class ConsumeService {

	private static Logger log = Logger.getLogger(ConsumeService.class);
	@Autowired
	private ExpenseService expenseService;
	@Autowired
	private ExpenseMapper expenseMapper;
	@Autowired
	private EmployeeMapper employeeMapper;
	@Autowired
	private MessageService messageService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private TrainOrderMapper trainOrderMapper;

	DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static boolean isNumeric(String str) {
		return RegexUtil.match(RegexUtil.IsIntNumber, str);
	}

	/**
	 * 
	 * <pre>
	 * &#64;Title: getRecharge 
	 * &#64;Description: 
	 *    目前仅支持支付宝充值
	 *    
	 * &#64;param paramsMap
	 *  uuid  string 用户登录凭证
	 *  totalAmount double  金额
	 * &#64;param resultMap
	 * &#64;throws Exception
	 * </pre>
	 * 
	 * @returnType: void
	 */
	public void getRecharge(JSONObject paramsMap, JSONObject resultMap) throws Exception {
		// 临时参数
		JSONObject result = new JSONObject();
		JSONObject jsonobject = new JSONObject();
		jsonobject = JSONObject.fromObject(paramsMap);
		String uuid = jsonobject.optString("uuid");
		Double totalAmount = jsonobject.optDouble("totalAmount");
		Map<String, Object> map = new HashMap<String, Object>();
		if (uuid != null && !uuid.isEmpty() && totalAmount != null 
				 ) {
			if( new BigDecimal(totalAmount+"").compareTo(new BigDecimal("1"))<0 ){
				result.put("code", Constant.code.CODE_10);
				result.put("msg", Constant.message.MESSAGE_10);
			}else{
				map.put("uuid", uuid);
				Userinfo userinfo = employeeMapper.Getuserinfo(map);
				JSONObject interimResultMap= messageService.checkUser(userinfo);
				if (interimResultMap.optBoolean("flag")) {
					Date nowdate = CommonDateParseUtil.date2date(new Date());
					Date uuIDExpiry = CommonDateParseUtil.string2date(userinfo.getUuidExpiry());
					if (nowdate.getTime() >= uuIDExpiry.getTime()) {
						result.put("code", Constant.code.CODE_14);
						result.put("msg", Constant.message.MESSAGE_14);
					} else {
						BigDecimal total_Amount = new BigDecimal(totalAmount);
						total_Amount = total_Amount.setScale(2, BigDecimal.ROUND_HALF_UP);

						map.clear();
						String tradeNo = CodeUtils.gettradeNo();
						String addTime = df.format(new Date());
						map.put("tradeNo", tradeNo);
						map.put("userId", userinfo.getId());
						map.put("paymentAmount", total_Amount);
						map.put("addTime", addTime);
						map.put("body", "充值");
						JSONObject data = new JSONObject();
						expenseMapper.saveTemporaryOrder(map);
						String total_Amount_Str = total_Amount.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
						String orderString = AlipayUtil.orderString(tradeNo, total_Amount_Str, 2, "账户充值");
						if (orderString == null || orderString.isEmpty()) {
							result.put("code", Constant.code.CODE_0);
							result.put("msg", Constant.message.MESSAGE_0);
						} else {
							data.put("orderString", orderString);
							data.put("tradeNo", tradeNo);
							result.put("data", data);
							result.put("code", Constant.code.CODE_1);
							result.put("msg", Constant.message.MESSAGE_1);
						}

					}
				}else{

					result.put("code",  interimResultMap.optString("code"));
					result.put("msg",  interimResultMap.optString("msg"));
				}
			}
		} else {
			result.put("code", Constant.code.CODE_2);
			result.put("msg", Constant.message.MESSAGE_2);
		}
		resultMap.put("data", result.optString("data"));
		resultMap.put("code", result.optString("code"));
		resultMap.put("msg", result.optString("msg"));
	}

	 
	/**
	 * 
	 * <pre>
	 *  
	 * 支付宝回调地址的公共方法（座位购买和余额充值） 
	 * &#64;param request
	 * &#64;param response
	 * &#64;param paymentType  2:支付宝 ;3:账户余额
	 * &#64;param expenseType  1充值，2购买座位，3出售座位，4提现，5退款
	 * &#64;param type         1:消费；2：充值
	 * &#64;return: void
	 * </pre>
	 */
	@Transactional
	public void orderAliPayNotify(HttpServletRequest request, HttpServletResponse response, Integer paymentType,
			Integer expenseType, String type) {
		log.info("[/order/pay/orderAliPayNotify]");
		String resultResponse = "failure";
		PrintWriter printWriter = null;
		// 获取到返回的所有参数 先判断是否交易成功trade_status 再做签名校验
		// 1、商户需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
		// 2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
		// 3、校验通知中的seller_id（或者seller_email)
		// 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email），
		// 4、验证app_id是否为该商户本身。上述1、2、3、4有任何一个验证不通过，则表明本次通知是异常通知，务必忽略。
		// 在上述验证通过后商户必须根据支付宝不同类型的业务通知，正确的进行不同的业务处理，并且过滤重复的通知结果数据。
		// 在支付宝的业务通知中，只有交易通知状态为TRADE_SUCCESS或TRADE_FINISHED时，支付宝才会认定为买家付款成功。
		// if ("TRADE_SUCCESS".equals(request.getParameter("trade_status"))) {
		System.out.println(request);
		Enumeration<?> pNames = request.getParameterNames();
		Map<String, String> param = new HashMap<String, String>();
		Map<String, Object> map = new HashMap<String, Object>();
		String trade_no = null;
		String tradeNo = null;
		String buyer_logon_id = null;
		String buyer_id = null;
		try {
			while (pNames.hasMoreElements()) {
				String pName = (String) pNames.nextElement();
				param.put(pName, request.getParameter(pName));
			}
			if (param != null && !param.isEmpty()) {
				// AlipaySignature.
				boolean signVerified = AlipaySignature.rsaCheckV1(param, AlipayUtil.ALIPAY_PUBLIC_KEY,
						AlipayConstants.CHARSET_UTF8, "RSA2");
//				boolean signVerified = AlipaySignature.rsaCheckV2(param, AlipayUtil.ALIPAY_PUBLIC_KEY,
//						AlipayConstants.CHARSET_UTF8);
				// // 校验签名是否正确
				printWriter = response.getWriter();
				tradeNo = param.get("out_trade_no");
				BigDecimal total_amount = new BigDecimal(param.get("total_amount"));
				String app_id = param.get("app_id");
				String seller_id = param.get("seller_id");
				trade_no = param.get("trade_no");
				buyer_logon_id = param.get("buyer_logon_id");
				buyer_id = param.get("buyer_id");
				System.out.println("buyer_logon_id:" + buyer_logon_id);
				System.out.println("buyer_id:" + param.get("buyer_id"));
				if (signVerified) {
					// 验签成功后
					// 按照支付结果异步通知中的描述，对支付结果中的业务内容进行1\2\3\4二次校验，校验成功后在response中返回success，校验失败返回failure
					if ("TRADE_SUCCESS".equals(request.getParameter("trade_status"))) {
						map.put("tradeNo", tradeNo);
						if (type.equals("1")) {// 消费 
							List<Order> orderList = trainOrderMapper.getOrder(map);
							if (orderList != null && !orderList.isEmpty()) {
								map.clear();
								System.out.println("1a2");
								map.put("id", orderList.get(0).getEmployeeId());
								
								Userinfo userinfo = employeeMapper.Getuserinfo(map);
								if (checktradeNo(total_amount, orderList, app_id, seller_id, paymentType, null)
										&& userinfo != null) {
									for (Order o : orderList) {
										if (o.getState() == 1) {// 如果订单未支付，则改状态
											/**
											 * 如果订单未支付，则更改订单状态，
											 * 买家流水，平台流水，
											 * 卖家流水
											 * 卖家通知，买家通知
											 */
											map.clear();
											map.put("state1", "2");
											map.put("orderNo", tradeNo);
											//更改订单状态
											trainOrderMapper.updateOrder(map);
											//买家，流水
											expenseService.saveExpenseUserAndSystemlog(userinfo, trade_no, null, total_amount, 
													o.getOrderNo(), paymentType, expenseType);
											log.info("订单支付成功：" + param.toString());

											resultResponse = "success";
											// 消息推送 买家
											JSONObject paramsMap = new JSONObject(), resultMap = new JSONObject();
											paramsMap.put("messageType", "3");
											paramsMap.put("userId", userinfo.getId());
											paramsMap.put("modelType", "ACZF1");
											paramsMap.put("money", total_amount);
											paramsMap.put("orderNo", o.getOrderNo());
											messageService.saveMessage(paramsMap, resultMap);
											
											// 消息推送 卖家
											paramsMap.clear();
											paramsMap.put("orderSellNo", o.getOrderSellNo());
											List<OrderSellInfo> osList=trainOrderMapper.getOrderSellInfo(paramsMap);
											if(osList!=null && osList.size()>0){
												OrderSellInfo osi =osList.get(0);
												
												paramsMap.clear();
												paramsMap.put("messageType", "1");
												paramsMap.put("sellerId", osi.getEmployeeId()); 
												paramsMap.put("modelType", "OD000");
												paramsMap.put("orderSellNo", osi.getOrderSellNo());
												paramsMap.put("orderNo", o.getOrderNo());
												paramsMap.put("dispatchTime", osi.getTrainDate());
												paramsMap.put("site", osi.getStartStationName()+"-"+osi.getEndStationName());
												paramsMap.put("trainNo", osi.getTrainNo());
												 
												messageService.saveMessage(paramsMap, resultMap);
											}
										 }
									}
								}
							}
						} else if (type.equals("2")) {// 充值
							TemporaryOrder to = expenseMapper.getTemporaryOrder(map);
							map.clear();
							System.out.println(to.getExpenseUserNo());

							if (to != null && to.getExpenseUserNo() == null) {
								map.put("id", to.getUserId());
								Userinfo userinfo = employeeMapper.Getuserinfo(map);
								if (userinfo != null) {
									System.out.println("1:");
									if (to.getPaymentAmount().compareTo(total_amount) == 0) {
										JSONObject payMnet = payment(userinfo, total_amount.doubleValue(), null, "2");
										if (payMnet != null && payMnet.getBoolean("flag")) {// 交易成功
											log.info("支付宝充值成功：" + param.toString());
											System.out.println("2:");
											
											expenseService.saveExpenseUserAndSystemlog(userinfo, trade_no, tradeNo, total_amount, 
													null, paymentType, expenseType);
											
											JSONObject interimMap=new JSONObject();
											interimMap.put("accountBuyId", buyer_id);
											//判断是否存在。不存在则保存 只做记录
											List<UserPayAccount>  accountDetilList=employeeMapper.getUserPayAccount(interimMap);
											if(accountDetilList.size()==0){
												interimMap.put("userId", to.getUserId());
												interimMap.put("accountBuyId", buyer_id);
												interimMap.put("accountName", buyer_logon_id);
												interimMap.put("status", "1");
												interimMap.put("typeId", "1");// 1支付宝
												interimMap.put("isDel", "0");
												interimMap.put("addTime", DateUtil.getCurrentTime(new Date()));
												interimMap.put("isDefault", "0");
												employeeMapper.saveBindingAccount(interimMap);
											}
											
											// 消息推送
											JSONObject paramsMap = new JSONObject(), resultMap = new JSONObject();
											paramsMap.put("messageType", "3");
											paramsMap.put("userId", userinfo.getId());
											paramsMap.put("modelType", "AC001");
											paramsMap.put("money", total_amount);
											messageService.saveMessage(paramsMap, resultMap);
											resultResponse = "success";
										}
									}
								}
							}
						}
					}
				}
			}

		} catch (Exception e) {
			log.error("alipay notify error :", e);
			// 失败发送邮箱
			if (resultResponse.equals("fail")) {
				Mail mail = new Mail();
				mail.setHost(MailConfig.host); // 设置邮件服务器,如果不用163的,自己找找看相关的
				mail.setSender(MailConfig.sender);
				mail.setReceiver(MailConfig.receiver); // 接收人
				mail.setUsername(MailConfig.sender); // 登录账号,一般都是和邮箱名一样吧
				mail.setPassword(MailConfig.password); // 发件人邮箱的登录密码
				mail.setSubject("支付宝回调接口异常");
				if (type.equals("2")) {// 充值失败
					mail.setMessage("	充值失败：第三方交易流水号（trade_no）:" + trade_no + ";平台合并订单号（tradeNo）" + tradeNo);
				} else {// 消费
					mail.setMessage("	消费失败：第三方交易流水号（trade_no）:" + trade_no + ";平台合并订单号（tradeNo）" + tradeNo);
				}
				MailUtil.send(mail);
			}
			printWriter.close();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		} finally {
			if (printWriter != null) {
				printWriter.print(resultResponse);
			}
		}

	}

	/**
	 * 
	 * <pre>
	 *  
	 * 支付宝回调地址的公共方法（座位购买和余额充值） 
	 * &#64;param request
	 * &#64;param response
	 * &#64;param paymentType  2:支付宝 ;3:账户余额
	 * &#64;param expenseType  1充值，2购买座位，3出售座位，4提现，5退款
	 * &#64;param type         1:消费；2：充值
	 * &#64;return: void
	 * </pre>
	 */
	@Transactional
	public void orderAliPayNotifyTest(HttpServletRequest request, HttpServletResponse response, Integer paymentType,
			  Integer expenseType, String type) {
		log.info("[/order/pay/notify]");
		String resultResponse = "failure";
		PrintWriter printWriter = null;
		// 获取到返回的所有参数 先判断是否交易成功trade_status 再做签名校验
		// 1、商户需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
		// 2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
		// 3、校验通知中的seller_id（或者seller_email)
		// 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email），
		// 4、验证app_id是否为该商户本身。上述1、2、3、4有任何一个验证不通过，则表明本次通知是异常通知，务必忽略。
		// 在上述验证通过后商户必须根据支付宝不同类型的业务通知，正确的进行不同的业务处理，并且过滤重复的通知结果数据。
		// 在支付宝的业务通知中，只有交易通知状态为TRADE_SUCCESS或TRADE_FINISHED时，支付宝才会认定为买家付款成功。
		// if ("TRADE_SUCCESS".equals(request.getParameter("trade_status"))) {
		System.out.println(request);
		Enumeration<?> pNames = request.getParameterNames();
		Map<String, String> param = new HashMap<String, String>();
		Map<String, Object> map = new HashMap<String, Object>();
		String trade_no = null;
		String tradeNo = null;
		String buyer_logon_id = null;
		String buyer_id = null;
		try {
			/*
			 * while (pNames.hasMoreElements()) { String pName = (String)
			 * pNames.nextElement(); param.put(pName,
			 * request.getParameter(pName)); }
			 */
			if (true/* param != null && !param.isEmpty() */) {
				// AlipaySignature.
				/*
				 * boolean signVerified = AlipaySignature.rsaCheckV1(param,
				 * AlipayUtil.ALIPAY_PUBLIC_KEY, AlipayConstants.CHARSET_UTF8);
				 */ // 校验签名是否正确
				printWriter = response.getWriter();
				tradeNo = "68549711515041240761";
				Double m = 0.01;
				BigDecimal total_amount = new BigDecimal(m).setScale(2, BigDecimal.ROUND_HALF_UP);

				String app_id = "2017122001000623";
				String seller_id = "2088521165068976";
				// buyer_logon_id ="001";
				// buyer_id = "002";
				// System.out.println("buyer_logon_id:" + buyer_logon_id);
				// System.out.println("buyer_id:" + param.get("buyer_id"));
				if (true) {
					// 验签成功后
					// 按照支付结果异步通知中的描述，对支付结果中的业务内容进行1\2\3\4二次校验，校验成功后在response中返回success，校验失败返回failure
					if (true) {
						map.put("tradeNo", tradeNo);
						if (type.equals("1")) {// 消费
							List<Order> orderList = trainOrderMapper.getOrder(map);
							if (orderList != null && !orderList.isEmpty()) {
								map.clear();
								System.out.println("1a2");
								map.put("id", orderList.get(0).getEmployeeId());
								
								Userinfo userinfo = employeeMapper.Getuserinfo(map);
								if (checktradeNo(total_amount, orderList, app_id, seller_id, paymentType, null)
										&& userinfo != null) {
									for (Order o : orderList) {
										if (o.getState() == 1) {// 如果订单未支付，则改状态
											/**
											 * 如果订单未支付，则更改订单状态，
											 * 买家流水，平台流水，
											 * 卖家流水
											 * 卖家通知，买家通知
											 */
											map.clear();
											map.put("state1", "2");
											map.put("tradeNoWhere", tradeNo);
											//更改订单状态
											trainOrderMapper.updateOrder(map);
											//买家，流水
											expenseService.saveExpenseUserAndSystemlog(userinfo, trade_no, tradeNo, total_amount, 
													o.getOrderNo(), paymentType, expenseType);
											log.info("订单支付成功：" + param.toString());

											resultResponse = "success";
											// 消息推送 买家
											JSONObject paramsMap = new JSONObject(), resultMap = new JSONObject();
											paramsMap.put("messageType", "3");
											paramsMap.put("userId", userinfo.getId());
											paramsMap.put("modelType", "ACZF1");
											paramsMap.put("money", total_amount);
											paramsMap.put("orderNo", o.getOrderNo());
											messageService.saveMessage(paramsMap, resultMap);
											
											// 消息推送 卖家
											paramsMap.clear();
											paramsMap.put("orderSellNo", o.getOrderSellNo());
											List<OrderSellInfo> osList=trainOrderMapper.getOrderSellInfo(paramsMap);
											if(osList!=null && osList.size()>0){
												OrderSellInfo osi =osList.get(0);
												
												paramsMap.clear();
												paramsMap.put("messageType", "1");
												paramsMap.put("sellerId", osi.getEmployeeId()); 
												paramsMap.put("modelType", "OD000");
												paramsMap.put("orderSellNo", osi.getOrderSellNo());
												paramsMap.put("dispatchTime", osi.getTrainDate());
												paramsMap.put("orderNo", o.getOrderNo());
												paramsMap.put("site", osi.getStartStationName()+"-"+osi.getEndStationName());
												paramsMap.put("trainNo", osi.getTrainNo());
												 
												messageService.saveMessage(paramsMap, resultMap);
											}
										 }
									}
								}
							}
						} else if (type.equals("2")) {// 充值
							TemporaryOrder to = expenseMapper.getTemporaryOrder(map);
							map.clear();
							if (to != null && to.getExpenseUserNo() == null) {
								map.put("id", to.getUserId());
								Userinfo userinfo = employeeMapper.Getuserinfo(map);
								if (userinfo != null) {
									System.out.println("1:");
									if (to.getPaymentAmount().compareTo(total_amount) == 0) {
										JSONObject payMnet = payment(userinfo, total_amount.doubleValue(), null, "2");
										if (payMnet != null && payMnet.getBoolean("flag")) {// 交易成功
											log.info("支付宝充值成功：" + param.toString());
											System.out.println("2:");
											/*JSONObject paySuccess = paySuccess(userinfo, trade_no, tradeNo,
													total_amount, null, paymentType, useType, expenseType,buyer_logon_id,buyer_id);
											System.out.println("3:" + paySuccess.get("code"));
											resultResponse = "success";*/
											/// ++++++++++++++++++++++++++++++++
											
											expenseService.saveExpenseUserAndSystemlog(userinfo, "sh00021444", tradeNo, total_amount, 
													null, paymentType, expenseType);
											
											JSONObject interimMap=new JSONObject();
											interimMap.put("accountBuyId", buyer_id);
											//判断是否存在。不存在则保存
											List<UserPayAccount>  accountDetilList=employeeMapper.getUserPayAccount(interimMap);
											if(accountDetilList.size()==0){
												interimMap.put("userId", buyer_id);
												interimMap.put("accountName", buyer_logon_id);
												interimMap.put("status", "1");
												interimMap.put("typeId", "1");// 1支付宝
												interimMap.put("isDel", "0");
												interimMap.put("addTime", DateUtil.getCurrentTime(new Date()));
												interimMap.put("isDefault", "0");
												employeeMapper.saveBindingAccount(interimMap);
											}
											
											/// ___________________________________
											
											// 消息推送
											JSONObject paramsMap = new JSONObject(), resultMap = new JSONObject();
											paramsMap.put("messageType", "3");
											paramsMap.put("userId", userinfo.getId());
											paramsMap.put("modelType", "AC001");
											paramsMap.put("money", total_amount);
											messageService.saveMessage(paramsMap, resultMap);
											resultResponse = "success";
										}
									}
								}
							}
						}
					}
				}
			}

		} catch (Exception e) {
			log.error("alipay notify error :", e);
			// 失败发送邮箱
			if (resultResponse.equals("fail")) {
				Mail mail = new Mail();
				mail.setHost(MailConfig.host); // 设置邮件服务器,如果不用163的,自己找找看相关的
				mail.setSender(MailConfig.sender);
				mail.setReceiver(MailConfig.receiver); // 接收人
				mail.setUsername(MailConfig.sender); // 登录账号,一般都是和邮箱名一样吧
				mail.setPassword(MailConfig.password); // 发件人邮箱的登录密码
				mail.setSubject("支付宝回调接口异常");
				if (type.equals("2")) {// 充值失败
					mail.setMessage("	充值失败：第三方交易流水号（trade_no）:" + trade_no + ";平台合并订单号（tradeNo）" + tradeNo);
				} else {// 消费
					mail.setMessage("	消费失败：第三方交易流水号（trade_no）:" + trade_no + ";平台合并订单号（tradeNo）" + tradeNo);
				}
				MailUtil.send(mail);
			}
			printWriter.close();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		} finally {
			if (printWriter != null) {
				printWriter.print(resultResponse);
			}
		}

	}

	/**
	 * 
	 * <pre>
	 *  
	* Title: checktradeNo
	* Description：校验订单信息
	* Examples: 
	*        1:如果支付金额和订单金额相同，商户id，app_id都系统 则返回true
	*        2:否则返回false
	 * &#64;param total_amount 总金额
	 * &#64;param list 订单列表
	 * &#64;param app_id appid
	 * &#64;param seller_id 商户id
	 * &#64;param paymentType 1:微信；2：支付宝
	 * &#64;param trade_type
	 *            交易类型 JSAPI：小程序 ；APP：手机App支付
	* &#64;return    
	* &#64;return: boolean
	 * </pre>
	 */
	public boolean checktradeNo(BigDecimal total_amount, List<Order> list, String app_id, String seller_id,
			Integer paymentType, String trade_type) {
		boolean flag = false;
		String appID = GetProperties.getFileUrl("appID");
		String sellerID =GetProperties.getFileUrl("sellerID");
		if (paymentType == 1) {
			if (trade_type != null && trade_type.equals("JSAPI")) {
				appID = WechatConfig.XCX_AppId;
			} else {
				appID = WechatConfig.AppId;
			}
			sellerID = WechatConfig.MchId;
		} else {
			appID = GetProperties.getAppID();
			sellerID = GetProperties.getSellerId();
		}
		BigDecimal totalAmont = new BigDecimal("0.00");
		for (Order o : list) {
			totalAmont = o.getAcount().add(totalAmont);
		}
		if (total_amount.compareTo(totalAmont) == 0 && appID.equals(app_id) && sellerID.equals(seller_id)) {
			flag = true;
		}
		return flag;
	}

	// 平台钱包
	/**
	 * 
	 * <pre>
	*校验支付信息，成功则进行交易
	* &#64;param userinfo 用户信息
	* &#64;param totalAmount 总金额
	* &#64;param payPwd  支付密码
	* &#64;param type  1:消费，2:充值到余额，3:提现第三方平台
	* &#64;return
	 * </pre>
	 * 
	 * @returnType: JSONObject
	 */
	public JSONObject payment(Userinfo userinfo, Double totalAmount, String payPwd, String type) {
		JSONObject result = new JSONObject();
		Map<String, Object> map = new HashMap<String, Object>();
		BigDecimal dd = new BigDecimal(String.valueOf(totalAmount));
		BigDecimal balance = new BigDecimal("0.00");
		dd = dd.setScale(2, BigDecimal.ROUND_HALF_DOWN);
		if (userinfo.getStatus() == 2) {
			result.put("code", "0015");
			result.put("flag", false);
			result.put("message", "账户冻结，无法支付！");
			return result;
		}
		if (type.equals("1")) {// 支付消费
			// 判断是否开启0:关闭；1：开启// 免付金额小于支付金额
			if (userinfo.getIsOpen() == 1 && userinfo.getMinAmount().compareTo(dd) == -1) {
				if (payPwd == null || payPwd.isEmpty()) {
					result.put("flag", false);
					result.put("code", "0002");
					result.put("message", "支付密码不能为空！");
					return result;
				} else {
					if (!userinfo.getPayPwd().equals(MD5Utils.string2MD5(payPwd))) {
						result.put("flag", false);
						result.put("code", "0054");
						result.put("message", "支付密码不对！");
						return result;
					}
				}
			}
			// 判断账户余额是否充足
			if (userinfo.getBalance().compareTo(dd) == -1) {
				result.put("flag", false);
				result.put("code", "0014");
				result.put("message", "余额不足！");
				return result;
			}
			balance = userinfo.getBalance().subtract(dd);
		} else if (type.equals("2")) {// 充值到账
			balance =new BigDecimal(totalAmount);
		} else {// 提現
			if (payPwd == null || payPwd.isEmpty()) {
				result.put("flag", false);
				result.put("code", "0002");
				result.put("message", "支付密码不能为空！");
				return result;
			} else {
				System.out.println(dd);
				if (userinfo.getPayPwd() != null && !userinfo.getPayPwd().isEmpty()) {
					if (!userinfo.getPayPwd().equals(MD5Utils.string2MD5(payPwd))) {
						result.put("flag", false);
						result.put("code", "0054");
						result.put("message", "支付密码不对！");
						return result;
					} else if (dd.compareTo(new BigDecimal("0.10")) == -1) {
						result.put("flag", false);
						result.put("code", "0061");
						result.put("message", "提现最低金额不能低于0.1元！");
						return result;
					}
				} else {
					result.put("flag", false);
					result.put("code", "0002");
					result.put("message", "支付密码不能为空，无法提现！");
					return result;
				}
			}
			balance = userinfo.getBalance().subtract(dd);
		}
		map.clear();
		map.put("id", userinfo.getId());
		map.put("status", 0);
		balance = balance.setScale(2, BigDecimal.ROUND_HALF_DOWN);
		map.put("acount", balance);
		employeeMapper.updateEmployeeInfo(map);
		result.put("flag", true);
		result.put("code", "0001");
		result.put("message", "操作成功！");
		return result;
	}
 


	// 设置用户
	public void updateLevel(Userinfo userinfo) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("type", 1);
		map.put("userId", userinfo.getId());
		String score = expenseMapper.gettotalScore(map);
		Integer level = getLevel(Integer.valueOf(score));
		if (level != userinfo.getLeavel() && userinfo.getLeavel() != null && level > userinfo.getLeavel()) {
			userinfo.setLeavel(level);
			// wxUserMapper.Updateuserinfo(userinfo);
		}
	}

	public Integer getLevel(Integer score) {
		Integer level = 0;
		if (score > 0 && score < 500) {
			level = 1;
		} else if (score >= 500 && score < 1000) {
			level = 2;
		} else if (score > 1000 && score < 2000) {
			level = 3;
		} else if (score >= 2000 && score < 3000) {
			level = 4;
		} else if (score >= 3000) {
			level = 5;
		}
		return level;
	}

	/**
	 * 
	 * @param userinfo
	 * @param serialNo
	 *            第三方流水
	 * @param membershipId
	 *            临时用户Id
	 * @param expenseType
	 *            1 充值 2提现 3 消费 4 收入 5退款 6 押金退还 7 损坏扣除 8 全部退还9 押金消费 10 使用费消费 11
	 *            使用费退还（退款）
	 * @param paymentAmount
	 *            金额
	 * @param paymentType
	 *            1 微信 2 支付宝 3 游乐币 4 现金支付 5 微信服务号支付
	 * @param userType
	 *            1 微信服务号用户1 2 游乐APP用户 3 现金用户(导览笔) 4 现金用户(临时会员，用于腕带) 5 微信服务号用户2
	 * @param orderNo
	 *            订单号
	 * @param remark
	 * @param openID
	 * @param refundNo
	 * @param scenicId
	 *            所属景区
	 * @param operateId
	 *            操作人
	 * @param refundReasonId
	 *            退还原因
	 * @param deductionPrice
	 *            折扣金额
	 */
	public void saveExpenseCardLog(Userinfo userinfo, String serialNo, String membershipId, Integer expenseType,
			BigDecimal paymentAmount, Integer paymentType, Integer userType, String orderNo, String remark,
			String openID, String refundNo, Integer scenicId, Integer operateId, Integer refundReasonId,
			BigDecimal deductionPrice) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (userinfo != null) {
			String expenseCardNo = CodeUtils
					.gettransactionFlowCode(userinfo.getUserCode().substring(userinfo.getUserCode().length() - 6));
			map.put("expenseCardNo", expenseCardNo);
			map.put("userId", userinfo.getId());
		} else {
			String expenseCardNo = CodeUtils.gettransactionFlowCode(membershipId.substring(membershipId.length() - 6));
			map.put("expenseCardNo", expenseCardNo);
			map.put("membershipId", membershipId);
		}

		if (expenseType == 5 || expenseType == 6 || expenseType == 11 || expenseType == 8) {
			map.put("paymentAmount", new BigDecimal("-" + paymentAmount));
		} else {
			map.put("paymentAmount", paymentAmount);
		}
		map.put("serialNo", serialNo);
		map.put("paymentType", paymentType);
		map.put("expenseType", expenseType);
		map.put("userType", userType);
		map.put("orderNo", orderNo);
		map.put("remark", remark);
		map.put("openID", openID);
		map.put("refundNo", refundNo);
		map.put("scenicId", scenicId);
		map.put("operateId", operateId);
		map.put("refundReasonId", refundReasonId);
		map.put("deductionPrice", deductionPrice);
		map.put("addTime", df.format(new Date()));
		expenseMapper.saveExpenseCardlog(map);
	}

	/*
	 * public void savelog(String orderNo, Integer operateId, Integer status,
	 * Integer operateType) { Map<String, Object> map = new HashMap<String,
	 * Object>(); map.put("orderNo", orderNo); map.put("operateId", operateId);
	 * map.put("status", status); map.put("operateTime", df.format(new Date()));
	 * map.put("operateType", operateType); expenseMapper.saveOrderLog(map); }
	 */
	/**
	 * 
	 * <pre>
	 *  
	* Title: getWithdrawals
	* Description： 第三方提现接口 type: 2：支付宝Withdrawals
	* &#64;param json
	* &#64;return    
	* &#64;return: String
	 * </pre>
	 */
	public JSONObject getWithdrawals(JSONObject paramsMap, JSONObject resultMap) {

		/**
		 * 1：用户登录状态
		 * 1.1：登录失效-禁止操作
		 * 1.2：登录成功 ，判断支付密码
		 *      1.2.1：支付密码错误,余额小于提现金额,支付密码未设置-禁止操作
		 *      1.2.2：支付密码正确 ,余额大于提现金额，，判断当天提现次数
		 *         1.2.2.1：当天已有提现 -禁止操作
		 *         1.2.2.2：当天没有提现 ，核对账单
		 *           1.2.2.2.1：账单总和和余额有出入-禁止操作
		 *           1.2.2.2.2：账单总和和余额相同，进行提现 （支付宝转账，更改用户余额，记录用户、平台流水，推送用户通知）
		 *          
		 */

		String uuid = paramsMap.optString("uuid");
		String payPwd = paramsMap.optString("payPwd");
		Double Amount = paramsMap.optDouble("Amount");
		Double type = paramsMap.optDouble("type");//1 微信
		
		JSONObject interimMap = new JSONObject();
		JSONObject interimResultMap = new JSONObject();
		
		String chargeStr=GetProperties.getFileUrl("charge");
		
		if(Amount<0.1){
			interimResultMap.put("code", Constant.code.CODE_51);
			interimResultMap.put("msg", Constant.message.MESSAGE_51);
		}else{
			if(uuid.isEmpty() || payPwd.isEmpty() ){
				interimResultMap.put("code", Constant.code.CODE_2);
				interimResultMap.put("msg", Constant.message.MESSAGE_2);
			}else{
	            // 1：用户登录状态
				interimMap.put("uuid", uuid);
				systemService.validUser(interimMap, interimResultMap);
				if(interimResultMap.get("code").equals(Constant.code.CODE_1)){//1.2：登录成功 ，判断支付密码
					JSONObject  emMap =(JSONObject) interimResultMap.get("data");
					int userId=emMap.optInt("id");
					String payPWD=emMap.optString("payPwd");
					
					if(payPWD.isEmpty()){//1.2.1：支付密码错误,支付密码未设置-禁止操作
						interimResultMap.put("code", Constant.code.CODE_45);
						interimResultMap.put("msg", Constant.message.MESSAGE_45);
					}else{
						if(!payPWD.equals(MD5Utils.string2MD5(payPwd))){//1.2.1：支付密码错误,支付密码未设置-禁止操作
							interimResultMap.put("code", Constant.code.CODE_43);
							interimResultMap.put("msg", Constant.message.MESSAGE_43);
						}else{//1.2.2：支付密码正确 ，判断当天提现次数
							//   判断账户余额
							if(Amount<= emMap.getDouble("balance")){
								interimMap.clear();
								interimMap.put("employeeId", userId);
								interimMap.put("expenseType", 4);
								interimMap.put("addTime", df.format(new Date()));
								List<ExpenseUserLog> expenseList = expenseMapper.getexpenselog(interimMap);
								if (expenseList != null && !expenseList.isEmpty()) {
									//1.2.2.1：当天已有提现 -禁止操作
									interimResultMap.put("code", Constant.code.CODE_36);
									interimResultMap.put("msg", Constant.message.MESSAGE_36);
								}else{
									//1.2.2.2：当天没有提现 ，核对账单
									interimMap.clear();
									interimMap.put("userId",userId);
									interimMap.put("type", 1);
									String amount = expenseMapper.gettotalAmount(interimMap);
									//1.2.2.2.1：账单总和和余额有出入-禁止操作
									if ( (new BigDecimal(emMap.optString("balance"))).compareTo(new BigDecimal(amount)) != 0) {
										interimResultMap.put("code", Constant.code.CODE_49);
										interimResultMap.put("msg", Constant.message.MESSAGE_49);
									}else{
										//1.2.2.2.2：账单总和和余额相同，进行提现 （支付宝转账，更改用户余额，记录用户、平台流水，推送用户通知）
										interimMap.clear();
										interimMap.put("userId", userId);
										interimMap.put("typeId", 1);
										interimMap.put("isDel", 0);
										interimMap.put("isDefault", 1);
										List<UserPayAccount> upa = employeeMapper.getUserPayAccount(interimMap);
										// 是否绑定提现帐号
										if (upa != null && !upa.isEmpty()) {
											//提现帐号状态是否正常
											if (upa.get(0).getStatus() !=1) {
												interimResultMap.put("code", Constant.code.CODE_38);
												interimResultMap.put("message", Constant.message.MESSAGE_38);
											}else{
												// 支付宝单笔转账
												String expenseUserNo = CodeUtils.gettransactionFlowCode(
														emMap.optString("employeeCode").substring(emMap.optString("employeeCode").length() - 6));
												
												Map<String, Object>  payInfo=MSeatUtil.getUserWithDrawCash( new BigDecimal(Amount.toString()),new BigDecimal(chargeStr));
												JSONObject rr = AlipayUtil.getWithdraw(expenseUserNo, payInfo.get("paymentAmount").toString(),
														upa.get(0).getAccountName()); 
												/*JSONObject rr =new JSONObject();
												rr.put("code", "01");
												rr.put("serialNo", "test");*/
												if (rr.getString("code").equals("0000")) {
													interimResultMap.put("code", Constant.code.CODE_40);
													interimResultMap.put("msg", Constant.message.MESSAGE_40);
													TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
												} else {
													//更改用户余额
													interimMap.clear();
													interimMap.put("id", userId);
													interimMap.put("acount", new BigDecimal("-"+Amount).setScale(2, BigDecimal.ROUND_HALF_DOWN));
													employeeMapper.updateEmployeeInfo(interimMap);
													//记录流水
													Userinfo userinfo=new Userinfo();
													userinfo.setId(userId);
													userinfo.setEmployeeCode(emMap.optString("employeeCode"));
													expenseService.saveExpenseUserAndSystemlog(userinfo, rr.optString("serialNo"), expenseUserNo,
															new BigDecimal("+"+Amount).setScale(2, BigDecimal.ROUND_HALF_DOWN), 
															null, 3, 4);
													 
													//推送用户通知
													try {
														interimMap.clear();
														interimMap.put("messageType",3);
														interimMap.put("modelType","AC002");
														interimMap.put("userId",userId);
														interimMap.put("orderNo",expenseUserNo);
														interimMap.put("money", payInfo.get("paymentAmount").toString());
														messageService.saveMessage(interimMap, interimResultMap);
													} catch (Exception e) {
														e.printStackTrace();
													}
													//返回用户余额
													interimMap.clear();
													interimMap.put("uuid", uuid);
													List<Map<String, Object>>  EMList=employeeMapper.selectEmplyeeInfo(interimMap);
													if(EMList!=null && EMList.size()>0){
														
														interimResultMap.put("amount",((Map)EMList.get(0)).get("balance"));
														interimResultMap.put("code",Constant.code.CODE_1);
														interimResultMap.put("msg",Constant.message.MESSAGE_1);
													}else{
														interimResultMap.put("code",Constant.code.CODE_12);
														interimResultMap.put("msg",Constant.message.MESSAGE_12);
													}
													
													
												}
											}
										} else {
											interimResultMap.put("code", Constant.code.CODE_50);
											interimResultMap.put("message", Constant.message.MESSAGE_50);
										}
									}
								}
							}else{
								interimResultMap.put("code", Constant.code.CODE_37);
								interimResultMap.put("msg", Constant.message.MESSAGE_37);
							}
						}
					}
					
				} 
//				 * 1.1：登录失效-禁止操作
			}
		}
		Map<String, Object> amount=new HashMap<>();
		amount.put("amount", interimResultMap.optString("amount"));
		resultMap.put("data",amount);
		resultMap.put("code", interimResultMap.get("code"));
		resultMap.put("msg", interimResultMap.get("msg"));
		return resultMap;
		 
	}
	/**
	 * 
	* <pre> 
	* Title: payOfOrder
	* Description：订单支付 
	* @param interimMap
	* <pre>
	* type 交易类型
	*   0 余额购买座位
	*   1 取消订单
	 
	*   
	*   
	*     payPWD  支付密码 
	*     uuid  登录唯一凭证（*）
	*     orderNo  订单编号（*）
	*</pre>
	* @param interimResultMap    
	* @return: void 
	* </pre>
	 * @throws Exception 
	 */
	public void payOfOrder(JSONObject paramsMap, JSONObject resultMap) throws Exception {
		JSONObject interimMap=new JSONObject();
		JSONObject interimResultMap=new JSONObject();

		String type=paramsMap.optString("type");
		String payPWD=paramsMap.optString("payPWD");
		String uuid=paramsMap.optString("uuid");
		String orderNo=paramsMap.optString("orderNo");
		
		if( uuid.equals("") || orderNo.equals("")   ){
			interimResultMap.put("cod",Constant.code.CODE_2);
			interimResultMap.put("msg",Constant.message.MESSAGE_2);
		}else{
			switch (type) {
			
			case "0"://余额支付
				interimMap.put("payPWD", payPWD);
				interimMap.put("uuid", uuid);
				interimMap.put("orderNo", orderNo);
				payOfCoin(interimMap, interimResultMap);
				break;
			case "1"://取消订单
				interimMap.put("uuid", uuid);
				interimMap.put("orderNo", orderNo);
				interimMap.put("type", type);
				cancelOrderAndRefund(interimMap, interimResultMap);
				break;
			 
			default:
				interimResultMap.put("code",Constant.code.CODE_10);
				interimResultMap.put("msg",Constant.message.MESSAGE_10);
				break;
			}
		}
		Map<String, Object> amount=new HashMap<>();
		amount.put("amount", interimResultMap.optString("amount"));
		resultMap.put("data",amount);
		resultMap.put("code", interimResultMap.get("code"));
		resultMap.put("msg", interimResultMap.get("msg"));
	}

	/**
	 * 
	* <pre> 
	* Title: cancelOrderAndRefund
	* Description： 取消订单和 退款
	*   订单状态更改，座位状态更改
	* @param interimMap
	*   String type 1
		String uuid=interimMap.optString("uuid");
		String orderNo=interimMap.optString("orderNo");
	* @param interimResultMap    
	* @return: void 
	* </pre>
	 */
	@SuppressWarnings("unchecked")
	public void cancelOrderAndRefund(JSONObject interimMap,JSONObject interimResultMap){
		String uuid=interimMap.optString("uuid");
		String orderNo=interimMap.optString("orderNo");
		//1：获取用户登录状态
		systemService.validUser(interimMap, interimResultMap);
		//1.2：用户登录成功
		if(interimResultMap.getString("code").equals(Constant.code.CODE_1)){
			/** 登录者信息 */
			JSONObject empMap=(JSONObject) interimResultMap.get("data");
			interimMap.put("orderNo", orderNo);
			interimMap.put("employeeId", empMap.get("id"));
			//获取订单信息
			List<Order> orderList=trainOrderMapper.getOrder(interimMap);
			if(orderList!=null && orderList.size()>0){
			    //获取订单信息
				Order orderInfo=orderList.get(0);
				Integer orderState=orderInfo.getState();
				String payType="";
				if(orderState==1){
					payType="1";
				}else if(orderState==2){
					payType="2";
				}
				System.out.println(orderState);
				switch (payType) {
				case "1"://未支付，取消
					// 1.2.2.1：订单状态不正常
					if(orderInfo.getState()!=1){
						interimResultMap.put("code",Constant.code.CODE_35);
						interimResultMap.put("msg",Constant.message.MESSAGE_35);
					//1.2.2.2：订单状态正常
					}else{
						//  取消订单状态 ， 更改订单座位状态
						interimMap.clear();
						interimMap.put("state1", "5");
						interimMap.put("id", orderInfo.getId());
						interimMap.put("orderNo", orderNo);
						interimMap.put("employeeId", empMap.get("id"));
						trainOrderMapper.updateOrder(interimMap);//取消订单状态

						//查询出出售订单，的座位，然后把座位改为未出售

						List<OrderStation> osList=trainOrderMapper.getOrderStation(interimMap);//获取订单下的座位
						List<Object> stList=new ArrayList<>();
						for(OrderStation os:osList){
							stList.add(os.getSellStationId());
						}
						
						interimMap.clear();
						interimMap.put("orderSellNo", orderInfo.getOrderSellNo());
						interimMap.put("isAssign","1");
						interimMap.put("assignAmount", "0");
						trainOrderMapper.updateOrderSell(interimMap);//取消指定人
						
						interimMap.clear();
						interimMap.put("isSell","0");
						interimMap.put("stationIdStr",stList);
 						interimMap.put("orderSellNo", orderInfo.getOrderSellNo());
						trainOrderMapper.updateOrderSellStation(interimMap);//根据用户的订单编号，更改座位状态
						//  给卖家推送消息，
						try {
							//  根据订单编号获取 ，卖家信息

							// 消息推送 卖家
							interimMap.clear();
							interimMap.put("orderSellNo", orderInfo.getOrderSellNo());
							List<OrderSellInfo> osiList=trainOrderMapper.getOrderSellInfo(interimMap);
							if(osiList!=null && osiList.size()>0){
								OrderSellInfo osi =osiList.get(0);
								
								interimMap.clear();
								interimMap.put("messageType", "1");
								interimMap.put("sellerId", osi.getEmployeeId()); 
								interimMap.put("modelType", "OD001");
								interimMap.put("orderSellNo", osi.getOrderSellNo());
								interimMap.put("orderNo", orderNo);
								interimMap.put("dispatchTime", osi.getTrainDate());
								interimMap.put("site", osi.getStartStationName()+"-"+osi.getEndStationName());
								interimMap.put("trainNo", osi.getTrainNo());
								messageService.saveMessage(interimMap, interimResultMap);
							}
							
							interimResultMap.put("code",Constant.code.CODE_1);
							interimResultMap.put("msg",Constant.message.MESSAGE_1);
						} catch (Exception e) {
							e.printStackTrace();
						}
						
					}
					break;
				case "2"://已支付，退款
					// 1.2.2.1：订单状态不正常
					BigDecimal refundAmoun=new BigDecimal("0");
					if(orderInfo.getState()!=2){
						interimResultMap.put("code",Constant.code.CODE_35);
						interimResultMap.put("msg",Constant.message.MESSAGE_35);
					//1.2.2.2：订单状态正常
					}else{
						refundAmoun=orderInfo.getAcount();
						//   退款1 更改订单 ，2 订单座位状态，3更改买家账户余额，
					    //  取消订单状态 ， 更改订单座位状态
						interimMap.clear();
						interimMap.put("state1", "7");
						interimMap.put("id", orderInfo.getId());
						interimMap.put("orderNo", orderNo);
						interimMap.put("employeeId", empMap.get("id"));
						trainOrderMapper.updateOrder(interimMap);//1取消订单状态

						List<OrderStation> osList=trainOrderMapper.getOrderStation(interimMap);//获取订单下的座位
						List<Object> stList=new ArrayList<>();
						for(OrderStation os:osList){
							stList.add(os.getSellStationId());
						}
						
						interimMap.clear();
						interimMap.put("isSell","0");
						interimMap.put("stationIdStr",stList);
//						interimMap.put("orderSellNo", orderInfo.getOrderSellNo());
						trainOrderMapper.updateOrderSellStation(interimMap);//根据用户的订单编号，更改座位状态

						interimMap.clear();
						interimMap.put("orderSellNo", orderInfo.getOrderSellNo());
						interimMap.put("isAssign","1");
						interimMap.put("assignAmount", "0");
						trainOrderMapper.updateOrderSell(interimMap);//取消指定人
						
						// TODO 如果是支付宝支付，则原路退回
						/**
						 * 获取支付类型，如果是支付宝则原路返回
						 * 
						 *            更改订单状态，更改座位状态，返回到支付宝余额
						 *          如果是余额，则更改买家账户余额，。。。
						 *          
						 */
						//
						interimMap.clear();
						interimMap.put("employeeId", empMap.get("id"));
						interimMap.put("orderNo", orderNo);
						List<ExpenseUserLog> ulog=expenseMapper.getexpenselog(interimMap);
						if(ulog!=null && ulog.size()>0){
							ExpenseUserLog eul=ulog.get(0);
							Integer pmt= eul.getPaymentType();
							if(pmt==2){//支付宝

								String chargeStr=GetProperties.getFileUrl("charge");
								Map chargeInfo=MSeatUtil.getUserWithDrawCash(orderInfo.getAcount(), new BigDecimal(chargeStr));
								String refundCode=CodeUtils.getRefundCode();
								refundAmoun=new BigDecimal(chargeInfo.get("paymentAmount")+"");
								JSONObject auRJ=AlipayUtil.getRefund(orderNo, refundAmoun+"",refundCode);
								if(auRJ.get("code").equals(Constant.code.CODE_1)){
									//如果返回订单编号，和金额系统。则记录
									/**
									 * 退款成功，记录平台流水，记录用户流水，给买家、卖家推送消息
									 */
									if(auRJ.get("orderNo").equals(orderNo) && 
											new BigDecimal(auRJ.get("refund_fee").toString()).compareTo(orderInfo.getAcount())<=0
											){ 

										Userinfo userinfo=new Userinfo();
										userinfo.setId(empMap.optInt("id")); 
										userinfo.setEmployeeCode(empMap.optString("employeeCode"));
										 
										//记录用户，系统流水
										
										// TODO 退款流水
										expenseService.saveExpenseUserAndSystemlog(userinfo, auRJ.get("serialNo").toString(), refundCode,
												 new BigDecimal("+"+orderInfo.getAcount().toString()).setScale(2, BigDecimal.ROUND_HALF_DOWN), 
												orderNo, 2, 6);
										//给买家、卖家推送消息
										
									}else{
										interimResultMap.put("code",Constant.code.CODE_35);
										interimResultMap.put("msg",Constant.message.MESSAGE_35);
									}
								}else{
									interimResultMap.put("code",auRJ.get("code"));
									interimResultMap.put("msg",auRJ.get("msg"));
								}
							}else if(pmt==3){//余额
								
								interimMap.clear();
								interimMap.put("id",  empMap.get("id"));
								interimMap.put("acount",  new BigDecimal("+"+orderInfo.getAcount().toString()).setScale(2, BigDecimal.ROUND_HALF_DOWN));
								employeeMapper.updateEmployeeInfo(interimMap); // 3更改买家账户余额，
								

								Userinfo userinfo=new Userinfo();
								userinfo.setId(empMap.optInt("id")); 
								userinfo.setEmployeeCode(empMap.optString("employeeCode"));
								//记录用户流水
								expenseService.saveExpenseUserAndSystemlog(userinfo, null, null,
										 new BigDecimal("+"+orderInfo.getAcount().toString()).setScale(2, BigDecimal.ROUND_HALF_DOWN), 
										orderNo, 3, 6);
							}
						//  给卖家，买家推送消息，
							try {
								
								// 消息推送 卖家
								interimMap.clear();
								interimMap.put("orderSellNo", orderInfo.getOrderSellNo());
								List<OrderSellInfo> osiList=trainOrderMapper.getOrderSellInfo(interimMap);
								if(osiList!=null && osiList.size()>0){
									OrderSellInfo osi =osiList.get(0);
									
									interimMap.clear();
									interimMap.put("messageType", "1");
									interimMap.put("sellerId", osi.getEmployeeId()); 
									interimMap.put("modelType", "OD004");
									interimMap.put("orderSellNo", osi.getOrderSellNo());
									interimMap.put("orderNo", orderNo);
									interimMap.put("dispatchTime", osi.getTrainDate());
									interimMap.put("site", osi.getStartStationName()+"-"+osi.getEndStationName());
									interimMap.put("trainNo", osi.getTrainNo());
									 
									messageService.saveMessage(interimMap, interimResultMap);
								}
								
								interimMap.clear();
								interimMap.put("messageType","3");
								interimMap.put("modelType","ACTK1");
								interimMap.put("money",refundAmoun.setScale(2, BigDecimal.ROUND_HALF_DOWN));
								interimMap.put("orderNo", orderInfo.getOrderNo());
								interimMap.put("userId", empMap.optInt("id"));
								messageService.saveMessage(interimMap, interimResultMap);//买家
							 } catch (Exception e) {
								e.printStackTrace();
							}

							interimResultMap.put("code",Constant.code.CODE_1);
							interimResultMap.put("msg",Constant.message.MESSAGE_1);
							
						}else{
							interimResultMap.put("code",Constant.code.CODE_35);
							interimResultMap.put("msg",Constant.message.MESSAGE_35);
						}
					}
					break;
				default:
					interimResultMap.put("code",Constant.code.CODE_10);
					interimResultMap.put("msg",Constant.message.MESSAGE_10);
					break;
				}
	  	    //查询不到订单
			}else{
				interimResultMap.put("code",Constant.code.CODE_35);
				interimResultMap.put("msg",Constant.message.MESSAGE_35);
			}
		}
	}
	/**
	 * 
	* <pre> 
	* Title: payOfCoin
	* Description： 余额支付
	* @param interimMap
	*	String payPWD  支付密码
	*	String uuid   用户登录唯一凭证
	*	String orderNo 订单编号
	* @param interimResultMap    
	* @return: void 
	* </pre>
	 */
	public void payOfCoin(JSONObject interimMap,JSONObject interimResultMap){
		/**
		 *1：获取用户登录状态
		 *1.1：用户登录失效 -禁止交易
		 *1.2：用户登录成功
		 *   1.2.1：用户支付密码不正确  -禁止交易
		 *   1.2.2：支付密码正确  -获取订单信息
		 *       1.2.2.1：订单状态不正常  -禁止交易（订单关闭，订单取消，订单易支付）
		 *       1.2.2.2：订单状态正常
		 *           1.2.2.2.1：账户余额不够支付订单价格  -禁止交易
		 *           1.2.2.2.2：账户余额足够支付订单价格  -开始交易（   更改订单状态，更改用户金额，记录用户消费流水， 记录推送消息。）
		 *      
		 */

		String payPWD=interimMap.optString("payPWD");
		String uuid=interimMap.optString("uuid");
		String orderNo=interimMap.optString("orderNo");
		if(payPWD.isEmpty() || uuid.isEmpty() ||orderNo.isEmpty()){
			interimResultMap.put("code",Constant.code.CODE_2);
			interimResultMap.put("msg",Constant.message.MESSAGE_2);
		}else{
			//1：获取用户登录状态
			systemService.validUser(interimMap, interimResultMap);
			//1.2：用户登录成功
			if(interimResultMap.getString("code").equals(Constant.code.CODE_1)){
				JSONObject empMap=(JSONObject) interimResultMap.get("data");
				String payPwd=empMap.optString("payPwd");
				BigDecimal balance= new BigDecimal(empMap.opt("balance").toString());
				// 1.2.1：用户支付密码不正确  -禁止交易
				if(payPwd.equals("")){
						interimResultMap.put("code",Constant.code.CODE_45);
						interimResultMap.put("msg",Constant.message.MESSAGE_45);
				}else{
					if(!payPwd.equals(MD5Utils.string2MD5(payPWD))){
						interimResultMap.put("code",Constant.code.CODE_43);
						interimResultMap.put("msg",Constant.message.MESSAGE_43);
					//1.2.2：支付密码正确 
					}else{
						interimMap.put("orderNo", orderNo);
						interimMap.put("employeeId", empMap.get("id"));
						//获取订单信息
						List<Order> orderList=trainOrderMapper.getOrder(interimMap);
						if(orderList!=null && orderList.size()>0){
						    //判断订单状态
							Order orderInfo=orderList.get(0);
							// 1.2.2.1：订单状态不正常
							if(orderInfo.getState()!=1 && orderInfo.getAcount().compareTo(new BigDecimal("0"))>=0 ){
								interimResultMap.put("code",Constant.code.CODE_35);
								interimResultMap.put("msg",Constant.message.MESSAGE_35);
							//1.2.2.2：订单状态正常
							}else{
								//1.2.2.2.1：账户余额不够支付订单价格  -禁止交易
								if(balance.compareTo(orderInfo.getAcount())==-1){//余额 大于订单金额
									interimResultMap.put("code",Constant.code.CODE_46);
									interimResultMap.put("msg",Constant.message.MESSAGE_46);
									//账户余额足够支付订单价格  -开始交易（   更改订单状态，更改用户金额，记录用户消费流水， 记录推送消息。）
								}else{
									//更改订单状态
									interimMap.clear();
									interimMap.put("orderNo", orderNo);
									interimMap.put("state1", "2");
									trainOrderMapper.updateOrder(interimMap);
									
									//更改用户余额
									interimMap.clear();
									interimMap.put("id",  empMap.get("id"));
									interimMap.put("acount",  new BigDecimal("-"+orderInfo.getAcount().toString()).setScale(2, BigDecimal.ROUND_HALF_DOWN));
									employeeMapper.updateEmployeeInfo(interimMap);
									
									//记录用户消费流水
									Userinfo userinfo=new Userinfo();
									userinfo.setId(empMap.optInt("id")); 
									userinfo.setEmployeeCode(empMap.optString("employeeCode"));
									expenseService.saveExpenseUserAndSystemlog(userinfo, null, null, new BigDecimal(orderInfo.getAcount().toString())
											, orderNo, 3, 2);
									
									//推送消息记录-买家
									interimMap.clear();
									interimMap.put("messageType", 3);
									interimMap.put("modelType", "ACZF1");
									interimMap.put("money", orderInfo.getAcount());
									interimMap.put("orderNo", orderInfo.getOrderNo());
									interimMap.put("userId", empMap.optInt("id"));
									try {
										messageService.saveMessage(interimMap, interimResultMap);
									} catch (Exception e) {
										e.printStackTrace();
									}

									//用户id,卖家订单编号，卖家始发站，返回用户余额
									//推送消息记录-卖家
									interimMap.clear();
									interimMap.put("orderSellNo", orderInfo.getOrderSellNo());
									List<OrderSellInfo> osList=trainOrderMapper.getOrderSellInfo(interimMap);
									
									if(osList!=null && osList.size()>0){
										OrderSellInfo os=osList.get(0);
										
										interimMap.clear();
										interimMap.put("messageType", 1);
										interimMap.put("modelType", "OD000");
										interimMap.put("trainNo", os.getTrainNo());
										interimMap.put("site", os.getStartStationName()+"-"+os.getEndStationName());
										interimMap.put("dispatchTime", os.getTrainDate());
										interimMap.put("orderSellNo", os.getOrderSellNo());
										interimMap.put("orderNo", orderNo);
										interimMap.put("sellerId", os.getEmployeeId());
										try {
											messageService.saveMessage(interimMap, interimResultMap);
										} catch (Exception e) {
											e.printStackTrace();
										}
										//返回用户余额
										interimMap.clear();
										interimMap.put("id", empMap.get("id"));
										List<Map<String, Object>>  EMList=employeeMapper.selectEmplyeeInfo(interimMap);
										if(EMList!=null && EMList.size()>0){
											
											interimResultMap.put("amount",((Map)EMList.get(0)).get("balance"));
											interimResultMap.put("code",Constant.code.CODE_1);
											interimResultMap.put("msg",Constant.message.MESSAGE_1);
										}else{
											interimResultMap.put("code",Constant.code.CODE_12);
											interimResultMap.put("msg",Constant.message.MESSAGE_12);
										}
										
									}else{
										interimResultMap.put("code",Constant.code.CODE_35);
										interimResultMap.put("msg",Constant.message.MESSAGE_35);
									}
									
									
								}
							}
				  	    //查询不到订单
						}else{
							interimResultMap.put("code",Constant.code.CODE_35);
							interimResultMap.put("msg",Constant.message.MESSAGE_35);
						}
					}
				}
			}
			//1.1：用户登录失效 -禁止交易
		}
		
	}
	/**
	 * 
	* <pre> 
	* Title: payOfOrderByAlipay
	* Description： 通过支付宝支付订单
	* 1:用户登录状态校验
	*   1.1：校验失败 -停止操作
	*   1.2：校验成功，获取订单信息，校验订单
	*      1.2.1：校验订单失败-停止操作
	*      1.2.2：校验订单成功-调用支付宝支付
	* @param paramsMap
	* @param resultMap
	* @throws Exception    
	* @return: void 
	* </pre>
	 */
	public void payOfOrderByAlipay(JSONObject paramsMap, JSONObject resultMap) throws Exception {
		// 临时参数
		JSONObject jsonobject = new JSONObject();
		jsonobject = JSONObject.fromObject(paramsMap);
		String uuid = jsonobject.optString("uuid");
		String orderNo = jsonobject.optString("orderNo");
		JSONObject interimMap=new JSONObject(), 
				interimResultMap=new JSONObject();
		if(uuid.isEmpty() || orderNo.isEmpty()){
			interimResultMap.put("code",Constant.code.CODE_2);
			interimResultMap.put("msg",Constant.message.MESSAGE_2);
		}else{
			interimMap.put("uuid",uuid);
			systemService.validUser(interimMap, interimResultMap);
			//1.2：用户登录成功
			if(interimResultMap.getString("code").equals(Constant.code.CODE_1)){
				JSONObject empMap=(JSONObject) interimResultMap.get("data");
				interimResultMap.remove("data");
				//获取订单信息
				interimMap.put("orderNo", orderNo);
				interimMap.put("employeeId", empMap.get("id"));
				//获取订单信息
				List<Order> orderList=trainOrderMapper.getOrder(interimMap);
				if(orderList!=null && orderList.size()>0  ){  
					Order orderInfo=orderList.get(0);
					// 1.2.1：订单状态不正常
					if(orderInfo.getState()!=1 && orderInfo.getAcount().compareTo(new BigDecimal("0"))>=0){
						interimResultMap.put("code",Constant.code.CODE_35);
						interimResultMap.put("msg",Constant.message.MESSAGE_35);
					
					} else{
						if(orderInfo.getAcount().compareTo(new BigDecimal("0"))==0){
							interimResultMap.put("code",Constant.code.CODE_56);
							interimResultMap.put("msg",Constant.message.MESSAGE_56);
						}else{
							//1.2.2：校验订单成功-调用支付宝支付
							BigDecimal total_Amount = orderInfo.getAcount();
							Map<String, Object> map = new HashMap<String, Object>();
							total_Amount = total_Amount.setScale(2, BigDecimal.ROUND_HALF_UP);

							map.clear();
							String tradeNo = CodeUtils.gettradeNo();
							String addTime = df.format(new Date());
							map.put("tradeNo", tradeNo);
							map.put("userId", empMap.get("id"));
							map.put("paymentAmount", total_Amount);
							map.put("addTime", addTime);
							map.put("body", "购买座位");
							JSONObject data = new JSONObject();
//							expenseMapper.saveTemporaryOrder(map);
							interimMap.clear();
//							interimMap.put("id", orderInfo.getId());
//							interimMap.put("orderNo",orderNo);
//							interimMap.put("tradeNo",orderNo);
//							trainOrderMapper.updateOrder(interimMap);
							String total_Amount_Str = total_Amount.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
							String orderString = AlipayUtil.orderString(orderNo, total_Amount_Str, 1, "购买座位");
							if (orderString == null || orderString.isEmpty()) {
								interimResultMap.put("code", Constant.code.CODE_0);
								interimResultMap.put("msg", Constant.message.MESSAGE_0);
							} else {
								data.put("orderString", orderString);
								data.put("tradeNo", orderNo);
								interimResultMap.put("data", data);
								interimResultMap.put("code", Constant.code.CODE_1);
								interimResultMap.put("msg", Constant.message.MESSAGE_1);
							}
						}
					}
				}else{
					interimResultMap.put("code",Constant.code.CODE_35);
					interimResultMap.put("msg",Constant.message.MESSAGE_35);
				}
			}else{
				interimResultMap.remove("data");
			}
		}
		resultMap.put("data", interimResultMap.optString("data"));
		resultMap.put("code", interimResultMap.optString("code"));
		resultMap.put("msg", interimResultMap.optString("msg"));
	}
	/**
	 * 
	*  
	* Title: endOfOrder
	* Description：<pre>完成订单
	* 订单完成
	* 
	*   更改订单状态。给卖家
	* 根据订单编号，和用户uuid完成订单
	* 
	*1：校验用户登录状态，
	* 2：校验订单状态
	* 3：校验订单和用户关系
	* 
	* 完成交易，卖家加流水、买家，卖家推送消息、订单状态改为完成
	* 
	* </pre> 
	* @param paramsMap
	* @param resultMap
	* @throws Exception <pre>  </pre>  
	* @return: void 
	*
	 */
	public void endOfOrder(JSONObject paramsMap, JSONObject resultMap) throws Exception {
		JSONObject jsonobject = new JSONObject();
		jsonobject = JSONObject.fromObject(paramsMap);
		String uuid = jsonobject.optString("uuid");
		String orderNo = jsonobject.optString("orderNo");
		
		JSONObject interimMap=new JSONObject(), 
				interimResultMap=new JSONObject();
		if(uuid.isEmpty() || orderNo.isEmpty()){
			interimResultMap.put("code",Constant.code.CODE_2);
			interimResultMap.put("msg",Constant.message.MESSAGE_2);
		}else{
			//校验用户登录状态
			interimMap.clear();
			interimMap.put("uuid",uuid);
			systemService.validUser(interimMap, interimResultMap);
			//1:校验用户登录状态，
			
			if(interimResultMap.get("code").equals(Constant.code.CODE_1)){
				//
				JSONObject empMap=(JSONObject) interimResultMap.get("data");
				interimResultMap.remove("data");
				interimMap.put("orderNo", orderNo);
				interimMap.put("employeeId", empMap.get("id"));
				//获取订单信息
				List<Order> orderList=trainOrderMapper.getOrder(interimMap);
				
				if(orderList!=null && orderList.size()>0){
					Order orderInfo=orderList.get(0);
					//* 2：校验订单状态
					if(orderInfo.getState()!=2){
						interimResultMap.put("code",Constant.code.CODE_35);
						interimResultMap.put("msg",Constant.message.MESSAGE_35);
					}else{
						if(orderInfo.getEmployeeId().equals(empMap.get("id"))){//是本人，完成支付
							// 完成交易，  卖家加流水、买家，卖家推送消息、订单状态改为完成
							//更改订单状态
							interimMap.clear();
							interimMap.put("orderNo", orderNo);
							interimMap.put("state1", "3");
							trainOrderMapper.updateOrder(interimMap);
							
							//更改用户余额
							interimMap.clear();
							interimMap.put("id",  orderInfo.getSellEmployeeId());
							interimMap.put("acount",  new BigDecimal("+"+orderInfo.getAcount().toString()).setScale(2, BigDecimal.ROUND_HALF_DOWN));
							employeeMapper.updateEmployeeInfo(interimMap);
							
							//记录卖家流水
							Userinfo userinfo=new Userinfo();
							userinfo.setId(orderInfo.getSellEmployeeId()); 
							userinfo.setEmployeeCode(orderInfo.getSellEmployeeCode());
							expenseService.saveExpenseUserAndSystemlog(userinfo, null, null, new BigDecimal(orderInfo.getAcount().toString())
									, orderNo, 3, 6);
							
							//推送消息记录-买家
							interimMap.clear();
							interimMap.put("messageType", 3);
							interimMap.put("modelType", "AC000");
							interimMap.put("money", orderInfo.getAcount());
							interimMap.put("orderNo", orderInfo.getOrderNo());
							interimMap.put("userId", empMap.optInt("id"));
							try {
								messageService.saveMessage(interimMap, interimResultMap);
							} catch (Exception e) {
								e.printStackTrace();
							}
							
							//推送消息记录-卖家
							interimMap.clear();
							interimMap.put("messageType", 1);
							interimMap.put("modelType", "OD002");
							interimMap.put("trainNo", orderInfo.getTrainNo());
							interimMap.put("site", orderInfo.getStartStationName()+"-"+orderInfo.getEndStationName());
							interimMap.put("dispatchTime", orderInfo.getTrainDate());
							interimMap.put("orderNo", orderInfo.getOrderNo());
							interimMap.put("orderSellNo", orderInfo.getOrderSellNo());
							interimMap.put("sellerId",orderInfo.getSellEmployeeId());
							try {
								messageService.saveMessage(interimMap, interimResultMap);
							} catch (Exception e) {
								e.printStackTrace();
							}

							interimResultMap.put("code",Constant.code.CODE_1);
							interimResultMap.put("msg",Constant.message.MESSAGE_1);
						}else{
							interimResultMap.put("code",Constant.code.CODE_35);
							interimResultMap.put("msg",Constant.message.MESSAGE_35);
						}
					}
				}else{
					interimResultMap.put("code",Constant.code.CODE_35);
					interimResultMap.put("msg",Constant.message.MESSAGE_35);
				}
			}else{
				interimResultMap.remove("data");
			}
		}
		resultMap.put("code", interimResultMap.get("code"));
		resultMap.put("msg", interimResultMap.get("msg"));
	}
}
