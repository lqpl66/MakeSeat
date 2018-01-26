package com.yl.Controller;

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

import com.yl.Service.HelpService;
import com.yl.Service.MessageService;
import com.yl.Service.SystemService;
import com.yl.Utils.Constant;

import net.sf.json.JSONObject;
 
@Controller
@RequestMapping(value = "message")
public class MessageController {
	 private static Logger logger = Logger.getLogger(MessageController.class);

	 @Autowired
     private  HelpService helpService;
	 @Autowired
     private  MessageService messageService;
	 @Autowired
     private  SystemService systemService;
	 /**
	  * 
	 * @Title: getFeedback 
	 * @Description: 获取帮助
	 * @param request
	 * @param jsonparam
	 * @return    
	 * JSONObject
	  */
	 @ResponseBody
	 @RequestMapping(value = "/getFeedback",method = {RequestMethod.POST},produces="application/json;charset=UTF-8" )
	 public JSONObject getFeedback(HttpServletRequest request, @RequestBody String jsonparam) {
		 JSONObject paramsMap = new JSONObject();
		 JSONObject resultMap = new JSONObject(); 
		 //准备注册
		 try {
			    paramsMap=JSONObject.fromObject(jsonparam);
			    helpService.getFeedback(paramsMap, resultMap);
		 }catch (Exception e) { 
				logger.error("执行--" + this.getClass().getSimpleName()+"()--异常：" + e);
				e.printStackTrace();
				resultMap.put("code", Constant.code.CODE_0);
				resultMap.put("msg", Constant.message.MESSAGE_0);
		 }
		 return resultMap;
	 }
	 /**
	  * 
	 * @Title: saveAPPDevice 
	 * @Description:   app唤醒校验
	 * @param request
	 * @param jsonparam
	 * @return    
	 * JSONObject
	  */
	 @ResponseBody
	 @Transactional
	 @RequestMapping(value = "/saveAPPDevice",method = {RequestMethod.POST},produces="application/json;charset=UTF-8" )
	 public JSONObject saveAPPDevice(HttpServletRequest request, @RequestBody String jsonparam) {
		 JSONObject paramsMap = new JSONObject();
		 JSONObject resultMap = new JSONObject(); 
		 try {
			    paramsMap=JSONObject.fromObject(jsonparam);
			    messageService.SaveAPPDevice(paramsMap, resultMap);
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
	 * @Title: getMessageNum 
	 * @Description:   获取消息总数
	 * @param request
	 * @param jsonparam
	 * @return    
	 * JSONObject
	  */
	 @ResponseBody
	 @Transactional
	 @RequestMapping(value = "/getMessageNum",method = {RequestMethod.POST},produces="application/json;charset=UTF-8" )
	 public JSONObject getMessageNum(HttpServletRequest request, @RequestBody String jsonparam) {
		 JSONObject paramsMap = new JSONObject();
		 JSONObject resultMap = new JSONObject(); 
		 try {
			    paramsMap=JSONObject.fromObject(jsonparam);
			    messageService.getMessageNum(paramsMap, resultMap);
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
	 * @Title: getMessage 
	 * @Description:   获取消息列表
	 * @param request
	 * @param jsonparam
	 * @return    
	 * JSONObject
	  */
	 @ResponseBody
	 @Transactional
	 @RequestMapping(value = "/getMessage",method = {RequestMethod.POST},produces="application/json;charset=UTF-8" )
	 public JSONObject getMessage(HttpServletRequest request, @RequestBody String jsonparam) {
		 JSONObject paramsMap = new JSONObject();
		 JSONObject resultMap = new JSONObject(); 
		 try {
			    paramsMap=JSONObject.fromObject(jsonparam);
  
			    messageService.getMessage(paramsMap, resultMap,request);
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
	 * @Title: operateMessage 
	 * @Description:   修改用户消息
	 * @param request
	 * @param jsonparam
	 * @return    
	 * JSONObject
	  */
	 @ResponseBody
	 @Transactional
	 @RequestMapping(value = "/operateMessage",method = {RequestMethod.POST},produces="application/json;charset=UTF-8" )
	 public JSONObject operateMessage(HttpServletRequest request, @RequestBody String jsonparam) {
		 JSONObject paramsMap = new JSONObject();
		 JSONObject resultMap = new JSONObject(); 
		 try {
			    paramsMap=JSONObject.fromObject(jsonparam);
			    messageService.operateMessage(paramsMap, resultMap);
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
	 * @Title: saveMessage 
	 * @Description:   保存消息
	 * @param request
	 * @param jsonparam
	 * @return    
	 * JSONObject
	  */
/*	 @ResponseBody
	 @Transactional
	 @RequestMapping(value = "/saveMessage",method = {RequestMethod.POST},produces="application/json;charset=UTF-8" )
	 public JSONObject saveMessage(HttpServletRequest request, @RequestBody String jsonparam) {
		 JSONObject paramsMap = new JSONObject();
		 JSONObject resultMap = new JSONObject(); 
		 try {
			    paramsMap=JSONObject.fromObject(jsonparam);
			    messageService.saveMessage(paramsMap, resultMap);
		 }catch (Exception e) { 
				logger.error("执行--" + this.getClass().getSimpleName()+"()--异常：" + e);
				e.printStackTrace();
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				resultMap.put("code", Constant.code.CODE_0);
				resultMap.put("msg", Constant.message.MESSAGE_0);
		 }
		 return resultMap;
	 }*/
}
