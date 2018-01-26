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
import com.yl.Utils.Constant;
import com.yl.Utils.StringUtil;

import net.sf.json.JSONObject;
/**
 * 
* @Description: 帮助
* @date 2017年11月29日 上午9:40:59 
*
 */
@Controller
@RequestMapping(value = "help")
public class HelpController {
	 private static Logger logger = Logger.getLogger(HelpController.class);

	 @Autowired
     private  HelpService helpService;
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
			    jsonparam=StringUtil.decodeJqueryAjaxString(jsonparam);
			    if(jsonparam.substring(jsonparam.length()-1).equalsIgnoreCase("=")){
			    	jsonparam=jsonparam.substring(0, jsonparam.length()-1);
			    }
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
	 * @Title: commonSaveFeedback 
	 * @Description:  新增，修改，删除用户反馈
	 * @param request
	 * @param jsonparam
	 * @return    
	 * JSONObject
	  */
	 @ResponseBody
	 @Transactional
	 @RequestMapping(value = "/commonSaveFeedback",method = {RequestMethod.POST},produces="application/json;charset=UTF-8" )
	 public JSONObject commonSaveFeedback(HttpServletRequest request, @RequestBody String jsonparam) {
		 JSONObject paramsMap = new JSONObject();
		 JSONObject resultMap = new JSONObject(); 
		 try {
			    paramsMap=JSONObject.fromObject(jsonparam);
			    helpService.commonSaveFeedback(paramsMap, resultMap);
		 }catch (Exception e) { 
				logger.error("执行--" + this.getClass().getSimpleName()+"()--异常：" + e);
				e.printStackTrace();
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				resultMap.put("code", Constant.code.CODE_0);
				resultMap.put("msg", Constant.message.MESSAGE_0);
		 }
		 return resultMap;
	 }
	 
	 @RequestMapping(value = "/downloadApp",method = {RequestMethod.GET},produces="application/json;charset=UTF-8" )
	 public String downloadApp(HttpServletRequest request) {
		 return "downloadApp";
	 }
}
