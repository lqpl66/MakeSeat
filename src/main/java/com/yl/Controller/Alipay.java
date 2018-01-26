package com.yl.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.yl.Service.ConsumeService;


@Controller
public class Alipay {
//	private static Logger log = Logger.getLogger(Alipay.class);
	private static Logger log = Logger.getLogger(Alipay.class);
//	@Autowired
//	private UserMapper usermapper;
//	@Autowired
//	private ExpenseMapper expenseMapper;
	@Autowired
	private ConsumeService consumeService;
 
	/**
	 * 
	* <pre> 
	* Title: orderPayNotify
	* Description：购买座位，支付宝异步回调方法
	* @param request
	*    
	 * 支付宝回调地址的公共方法（座位购买和余额充值） 
	 * &#64;param request
	 * &#64;param response
	 * &#64;param paymentType  2:支付宝 ;3:账户余额
	 * &#64;param expenseType  1充值，2购买座位，3出售座位，4提现，5退款
	 * &#64;param type         1:消费；2：充值
	 * &#64;return: void
	 * </pre>
	* @param response    
	* @return: void 
	* </pre>
	 */
	@RequestMapping(value = "/Alipay/expenseNotify", method ={ RequestMethod.POST,RequestMethod.GET})
	public void orderPayNotify(HttpServletRequest request, HttpServletResponse response) {
		log.info("[/order/pay/notify]");
		consumeService.orderAliPayNotify(request, response, 2 , 2,"1");

	}
	/**
	 * 订单支付支付宝服务器异步通知(用于用户充值回调地址)
	 * type:1:消费；2：充值
	 * @param request
	 * @param response
	 *             paymentType  2:支付宝 ;3:账户余额 
	 *             expenseType  1充值，2购买座位，3出售座位，4提现，5退款
	 */
	@RequestMapping(value = "/Alipay/rechargeNotify", method ={ RequestMethod.POST,RequestMethod.GET})
	public void orderPayNotifyRecharge(HttpServletRequest request, HttpServletResponse response) {
		log.info("[/order/pay/recharge]");
        
		consumeService.orderAliPayNotify(request, response, 2 , 1,"2");
	}
	/**
	 * (测试)订单支付支付宝服务器异步通知(用于用户充值回调地址)
	 * type:1:消费；2：充值
	 * @param request
	 * @param response
	 *             paymentType  2:支付宝 ;3:账户余额 
	 *             expenseType  1充值，2购买座位，3出售座位，4提现，5退款
	 */
	@RequestMapping(value = "/Alipay/orderAliPayNotifyTest", method ={ RequestMethod.POST,RequestMethod.GET})
	public void orderAliPayNotifyTest(HttpServletRequest request, HttpServletResponse response) {
		log.info("[/order/pay/recharge]");
        
		consumeService.orderAliPayNotifyTest(request, response, 2 , 2,"1");
	}
	
	/**
	 * 订单支付支付宝服务器异步通知(用于用户购买腕带回调地址)
	 * type:1:消费；2：充值
	 * @param request
	 * @param response
	 *            paymentType 1:微信;2:支付宝 ;3:游乐币 
	 *             expenseType 1:充值;2:提现;3:消费; 4:退款
	 */
	@RequestMapping(value = "/Alipay/cardNotify", method = { RequestMethod.POST,RequestMethod.GET})
	public void orderPayNotifyCard(HttpServletRequest request, HttpServletResponse response) {
		log.info("[/order/pay/card]");
		consumeService.orderAliPayNotify(request, response, 2 , 3,"1");
	}
	
 
}
