package com.yl.Controller;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yl.Service.ConsumeService;
import com.yl.Service.ExpenseService;
import com.yl.Service.HelpService;
import com.yl.Utils.Constant;
import com.yl.bean.Userinfo;

import net.sf.json.JSONObject;
/**
 * 
* @Description:消费
* @date 2017年12月06日下午16:40:59 
*
 */
@Controller
@RequestMapping(value = "consume")
public class ConsumeController {
	 private static Logger logger = Logger.getLogger(ConsumeController.class);

	 @Autowired
     private  ConsumeService consumeService;
	 @Autowired
     private  ExpenseService expenseService;
	 
	 /**
	  * 
	 * @Title: getRecharge 
	 * @Description:  第三方充值接口
	 * @param request
	 * @param jsonparam
	 * @return    
	 * JSONObject
	  */
	 @ResponseBody
	 @Transactional
	 @RequestMapping(value = "/getRecharge",method = {RequestMethod.POST},produces="application/json;charset=UTF-8" )
	 public JSONObject getRecharge(HttpServletRequest request, @RequestBody String jsonparam) {
		 JSONObject paramsMap = new JSONObject();
		 JSONObject resultMap = new JSONObject(); 
		 try {
			    paramsMap=JSONObject.fromObject(jsonparam);
			    consumeService.getRecharge(paramsMap, resultMap);
		 }catch (Exception e) { 
				logger.error("执行--" + this.getClass().getSimpleName()+"()--异常：" + e);
				e.printStackTrace();
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				resultMap.put("code", Constant.code.CODE_0);
				resultMap.put("msg", Constant.message.MESSAGE_0);
		 }
		 return resultMap;
	 }
	 /**
	  * 
	 * @Title: getRecharge 
	 * @Description:  第三方提现接口
	 * @param request
	 * @param jsonparam
	 * @return    
	 * JSONObject
	  */
	 @ResponseBody
	 @Transactional
	 @RequestMapping(value = "/getWithdrawals",method = {RequestMethod.POST},produces="application/json;charset=UTF-8" )
	 public JSONObject getWithdrawals(HttpServletRequest request, @RequestBody String jsonparam) {
		 JSONObject paramsMap = new JSONObject();
		 JSONObject resultMap = new JSONObject(); 
		 try {
			    paramsMap=JSONObject.fromObject(jsonparam);
			    consumeService.getWithdrawals(paramsMap, resultMap);
		 }catch (Exception e) { 
				logger.error("执行--" + this.getClass().getSimpleName()+"()--异常：" + e);
				e.printStackTrace();
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				resultMap.put("code", Constant.code.CODE_0);
				resultMap.put("msg", Constant.message.MESSAGE_0);
		 }
		 return resultMap;
	 }
	 /**
	  * 
	 * @Title: payOfOrder 
	 * @Description:  订单支付
	 *   
	* type 交易类型
	*   0 余额购买座位
	*   1 取消订单 
	*   
	*     payPWD  支付密码 
	*     uuid  登录唯一凭证（*）
	*     orderNo  订单编号（*）
	 * @param request
	 * @param jsonparam
	 * @return    
	 * JSONObject
	  */
	 @ResponseBody
	 @Transactional
	 @RequestMapping(value = "/payOfOrder",method = {RequestMethod.POST},produces="application/json;charset=UTF-8" )
	 public JSONObject payOfOrder(HttpServletRequest request, @RequestBody String jsonparam) {
		 JSONObject paramsMap = new JSONObject();
		 JSONObject resultMap = new JSONObject(); 
		 try {
			    paramsMap=JSONObject.fromObject(jsonparam);
 			    consumeService.payOfOrder(paramsMap, resultMap);
		 }catch (Exception e) { 
				logger.error("执行--" + this.getClass().getSimpleName()+"()--异常：" + e);
				e.printStackTrace();
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				resultMap.put("code", Constant.code.CODE_0);
				resultMap.put("msg", Constant.message.MESSAGE_0);
		 }
		 return resultMap;
	 }
	 /**
	  * 
	 * <pre> 
	 * Title: payOfOrderByAlipay
	 * Description： 通过支付宝支付订单
	 * @param request
	 * @param jsonparam
	 * @return    
	 * @return: JSONObject 
	 * </pre>
	  */
	 @ResponseBody
	 @Transactional
	 @RequestMapping(value = "/payOfOrderByAlipay",method = {RequestMethod.POST},produces="application/json;charset=UTF-8" )
	 public JSONObject payOfOrderByAlipay(HttpServletRequest request, @RequestBody String jsonparam) {
		 JSONObject paramsMap = new JSONObject();
		 JSONObject resultMap = new JSONObject(); 
		 try {
			    paramsMap=JSONObject.fromObject(jsonparam);
 			    consumeService.payOfOrderByAlipay(paramsMap, resultMap);
		 }catch (Exception e) { 
				logger.error("执行--" + this.getClass().getSimpleName()+"()--异常：" + e);
				e.printStackTrace();
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				resultMap.put("code", Constant.code.CODE_0);
				resultMap.put("msg", Constant.message.MESSAGE_0);
		 }
		 return resultMap;
	 }
	 /**
	  * 
	 * <pre> 
	 * Title: endOfOrder
	 * Description：完成订单
	 * @param request
	 * @param jsonparam
	 * @return    
	 * @return: JSONObject 
	 * </pre>
	  */
	 @ResponseBody
	 @Transactional
	 @RequestMapping(value = "/endOfOrder",method = {RequestMethod.POST},produces="application/json;charset=UTF-8" )
	 public JSONObject endOfOrder(HttpServletRequest request, @RequestBody String jsonparam) {
		 JSONObject paramsMap = new JSONObject();
		 JSONObject resultMap = new JSONObject(); 
		 try {
			    paramsMap=JSONObject.fromObject(jsonparam);
 			    consumeService.endOfOrder(paramsMap, resultMap);
		 }catch (Exception e) { 
				logger.error("执行--" + this.getClass().getSimpleName()+"()--异常：" + e);
				e.printStackTrace();
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				resultMap.put("code", Constant.code.CODE_0);
				resultMap.put("msg", Constant.message.MESSAGE_0);
		 }
		 return resultMap;
	 }
	 /**
	  * 
	 * <pre> 
	 * Title: endOfOrder
	 * Description：完成订单
	 * @param request
	 * @param jsonparam
	 * @return    
	 * @return: JSONObject 
	 * </pre>
	  */
	 @ResponseBody
	 @Transactional
	 @RequestMapping(value = "/saveExpenseUserAndSystemlog",method = {RequestMethod.POST},produces="application/json;charset=UTF-8" )
	 public JSONObject saveExpenseUserAndSystemlog(HttpServletRequest request, @RequestBody String jsonparam) {
		 JSONObject paramsMap = new JSONObject();
		 JSONObject resultMap = new JSONObject(); 
		 try {
			    paramsMap=JSONObject.fromObject(jsonparam); 
			    Userinfo userinfo=new Userinfo();
			    userinfo.setId(1);
			    userinfo.setEmployeeCode("PGZ1516588851");
			    expenseService.saveExpenseUserAndSystemlog(userinfo, "test0001", "test", new BigDecimal("5"),
			    		"", 2, 6);
		 }catch (Exception e) { 
				logger.error("执行--" + this.getClass().getSimpleName()+"()--异常：" + e);
				e.printStackTrace();
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				resultMap.put("code", Constant.code.CODE_0);
				resultMap.put("msg", Constant.message.MESSAGE_0);
		 }
		 return resultMap;
	 }
}
