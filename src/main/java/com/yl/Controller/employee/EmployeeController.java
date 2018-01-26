package com.yl.Controller.employee;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

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

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.yl.Service.EmployeeService;
import com.yl.Service.MessageService;
import com.yl.Service.SystemService;
import com.yl.Utils.BaseParseImage;
import com.yl.Utils.Constant;
import com.yl.Utils.DateUtil;
import com.yl.Utils.FileUtils;
import com.yl.Utils.GetProperties;
import com.yl.Utils.MD5Utils;
import com.yl.Utils.RegexUtil;
import com.yl.mapper.EmployeeMapper;
import com.yl.mapper.SystemMapper;

import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "employee")
public class EmployeeController {

	 private static Logger logger = Logger.getLogger(EmployeeController.class);

	 @Autowired
     private  SystemService systemService;
	 @Autowired
     private  EmployeeMapper employeeMapper;
	 @Autowired
	 private EmployeeService employeeService;
	 @Autowired
     private  SystemMapper systemMapper;

	 @Autowired
	 private MessageService messageService;
	 public static boolean isNumeric(String str){
		  for (int i = 0; i < str.length(); i++){
			   if (!Character.isDigit(str.charAt(i))){
			      return false;
			   }
		  }
		  return true;
	}
	 /***
	  * 
	 * @Title: getEmployeeInfo
	 * @Description: 获取用户信息
	 * @param request
	 * @param jsonparam
	 * @return    
	 * JSONObject
	  */
	 @ResponseBody
	 @RequestMapping(value = "/getEmployeeInfo",method = {RequestMethod.POST},produces="application/json;charset=UTF-8" )
	 public JSONObject getEmployeeInfo(HttpServletRequest request, @RequestBody String jsonparam) {
		 JSONObject paramsMap = new JSONObject();
		 	JSONObject resultMap = new JSONObject(); 
			try {
				paramsMap=JSONObject.fromObject(jsonparam);
				//根据用户uuid 校验当前用户登录状态
				systemService.validUser(paramsMap, resultMap);
				 
			} catch (Exception e) {
				logger.error("执行--" + this.getClass().getSimpleName()+"()--异常：" + e);
				e.printStackTrace();
				resultMap.put("code", Constant.code.CODE_0);
				resultMap.put("msg", Constant.message.MESSAGE_0);
			}
			 return resultMap;
	 }
	 
	 
	 /**
	  * 
	 * @Title: updateEmployeeInfo 
	 * @Description: 修改用户信息
	 * @param request
	 * @param jsonparam
	 * @return    
	 * JSONObject
	  */
	 @ResponseBody
	  @Transactional
	 @RequestMapping(value = "/updateEmployeeInfo",method = {RequestMethod.POST},produces="application/json;charset=UTF-8" )
	 public JSONObject updateEmployeeInfo(HttpServletRequest request, @RequestBody String jsonparam) {
		JSONObject paramsMap = new JSONObject();
	 	JSONObject resultMap = new JSONObject(); 
		try {
			paramsMap=JSONObject.fromObject(jsonparam);
			String amount=paramsMap.optString("amount");
			paramsMap.remove("mobile");
			String uuid=paramsMap.optString("uuid");
			String oldPwd=paramsMap.optString("oldPwd");
			String pwd=paramsMap.optString("pwd");
			String idCard=paramsMap.optString("idCard");
			String realName=paramsMap.optString("realName");
			String smsCode=paramsMap.optString("smsCode");
			String type=paramsMap.optString("type");
			
			String headImg=paramsMap.optString("headImg");
			String gender=paramsMap.optString("gender");
			String birthday=paramsMap.optString("birthday");
			String accountType=paramsMap.optString("accountType");
			String accountName=paramsMap.optString("accountName");
			
			
			
			//如果operate 为0，2，3，4，5 校验uuid
			String employeeId="0";
			String uuidValid="false";
			if(!type.equals("1")  ){
				if(uuid.equals("")){
					resultMap.put("code", Constant.code.CODE_2);
					resultMap.put("msg", Constant.message.MESSAGE_2);
				}else{
					systemService.validUser(paramsMap, resultMap);
					if(resultMap.optString("code").equals(Constant.code.CODE_1) ){
						paramsMap.remove("mobile");
						amount=((JSONObject)resultMap.get("data")).optString("amount");
						employeeId=((JSONObject)resultMap.get("data")).optString("id");
						amount=((JSONObject)resultMap.get("data")).optString("amount");
						resultMap.remove("data");
						uuidValid="true";
					}
				}
			}else{
				resultMap.remove("data");
				paramsMap.remove("uuid");
				//根据手机号，查出用户id；
				JSONObject interimMap=new JSONObject();
				interimMap.put("amount", amount);
				
				List<Map<String, Object>> emL=employeeMapper.selectEmplyeeInfo(interimMap);
				if(emL.size()>0){
					employeeId=""+((Map<String, Object>)emL.get(0)).get("id");
					amount=""+((Map<String, Object>)emL.get(0)).get("amount");
				}
				uuidValid="true";
			}
			
			//如果operate 为1，2，4，5
			String smsCodeValid="false";
			if(!type.equals("0") && !type.equals("3") && uuidValid.equals("true") ){
			   //校验短信验证码
				if( smsCode.equals("") || amount.equals("")){
					resultMap.put("code", Constant.code.CODE_2);
					resultMap.put("msg", Constant.message.MESSAGE_2);
				}else{
					//先短信校验 手机号不能为空
					Map<String,Object> interimMap=new HashMap<>();
					interimMap.put("amount", amount);
					interimMap.put("operateType", type);//
					Map<String, Object> lr= systemMapper.selectLogRecord(interimMap);
					if(lr!=null ){
						String dbSmsCode=""+lr.get("smsCode");
//						System.out.println("数据库验证码："+dbSmsCode+" 用户输入验证码："+smsCode);
						if(!dbSmsCode.equals(smsCode)){
							resultMap.put("code", Constant.code.CODE_9);
							resultMap.put("msg", Constant.message.MESSAGE_9);
						}else{
							String smsPwdExpiry=lr.get("smsPwdExpiry").toString();
							boolean d=DateUtil.getCompareDateOFtime(smsPwdExpiry, DateUtil.getCurrentTime());
							if(!d){
								resultMap.put("code", Constant.code.CODE_19);
								resultMap.put("msg", Constant.message.MESSAGE_19);
							}else{
								smsCodeValid="true";
								resultMap.put("code", Constant.code.CODE_1);
								resultMap.put("msg", Constant.message.MESSAGE_1);
							}
						}
					}else{
						resultMap.put("code", Constant.code.CODE_9);
						resultMap.put("msg", Constant.message.MESSAGE_9);
					}
				}
			}else{
				smsCodeValid="true";
			}
			if(smsCodeValid.equals("true") && uuidValid.equals("true")){
				//如果短信校验成功，则进行
				if(type!=""  ){
					switch (type) {
					case "0"://修改密码
						if(oldPwd.equals("") || pwd.equals("") ){
							resultMap.put("code", Constant.code.CODE_2);
							resultMap.put("msg", Constant.message.MESSAGE_2);
						}else{
							if(oldPwd.equals(pwd)){
								resultMap.put("code", Constant.code.CODE_15);
								resultMap.put("msg", Constant.message.MESSAGE_15);
							}else{
								if(pwd.length()<6 || pwd.length()>20 ){
									resultMap.put("code", Constant.code.CODE_7);
									resultMap.put("msg", Constant.message.MESSAGE_7);
								}else{
									List<Map<String, Object>> endEmpList=employeeMapper.selectEmplyeeInfo(paramsMap);
									if(endEmpList.size()==0){
										resultMap.put("code", Constant.code.CODE_12);
										resultMap.put("msg", Constant.message.MESSAGE_12);
									}else{
										String dbPwd=endEmpList.get(0).get("pwd").toString();
										String md5Pwd=MD5Utils.string2MD5(oldPwd);
										if(!dbPwd.equals(md5Pwd)){//旧密码是否正确
											resultMap.put("code", Constant.code.CODE_16);
											resultMap.put("msg", Constant.message.MESSAGE_16);
										}else{
											paramsMap.clear();
											paramsMap.put("amount", amount);
											paramsMap.put("status", "0");
											paramsMap.put("pwd", MD5Utils.string2MD5(pwd));
											paramsMap.put("uuidExpiry", DateUtil.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
											employeeMapper.updateEmployeeInfo(paramsMap);
											resultMap.put("code", Constant.code.CODE_1);
											resultMap.put("msg", Constant.message.MESSAGE_1);
											JSONObject interimMap=new JSONObject();
											JSONObject interimResultMap=new JSONObject();
											interimMap.put("modelType", "AC004");
											interimMap.put("messageType", "3");
											interimMap.put("userId", endEmpList.get(0).get("id"));
											messageService.saveMessage(interimMap, interimResultMap);
										}
									}
								}
							}
						}
						break;
					case "1"://短信校验找回密码
						//手机号，密码必填
						List<Map<String, Object>> endEmpList=null;
						if( pwd.equals("") ){
							resultMap.put("code", Constant.code.CODE_2);
							resultMap.put("msg", Constant.message.MESSAGE_2);
						}else{ 
							if(pwd.length()<6 && pwd.length()>20 ){
								resultMap.put("code", Constant.code.CODE_7);
								resultMap.put("msg", Constant.message.MESSAGE_7);
							}else{
								Map<String, Object> interimMap=new HashMap<>();
								interimMap.put("amount", amount);
								interimMap.put("operateType", type);//
								endEmpList=employeeMapper.selectEmplyeeInfo(interimMap);
								if(endEmpList.size()==0){
									resultMap.put("code", Constant.code.CODE_12);
									resultMap.put("msg", Constant.message.MESSAGE_12);
								}else{
									String dbPwd=endEmpList.get(0).get("pwd").toString();
									String md5Pwd=MD5Utils.string2MD5(pwd);
									if(dbPwd.equals(md5Pwd)){//新密码和旧密码相同
										resultMap.put("code", Constant.code.CODE_15);
										resultMap.put("msg", Constant.message.MESSAGE_15);
									}else{
										paramsMap.clear();
										paramsMap.put("amount", amount);
										paramsMap.put("status", "0" );
										paramsMap.put("uuidExpiry", DateUtil.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
										paramsMap.put("pwd", MD5Utils.string2MD5(pwd));
										employeeMapper.updateEmployeeInfo(paramsMap);
										resultMap.put("code", Constant.code.CODE_1);
										resultMap.put("msg", Constant.message.MESSAGE_1);
										interimMap.clear();
										JSONObject interimMap1=new JSONObject();
										JSONObject interimResultMap=new JSONObject();
										interimMap1.put("modelType", "AC004");
										interimMap1.put("messageType", "3");
										interimMap1.put("userId", endEmpList.get(0).get("id"));
										messageService.saveMessage(interimMap1, interimResultMap);
									
									}
								}
							}
						}
						
						break;
					case "2"://修改支付密码
						//短信校验 密码不能为空，且6为纯数字
						if( pwd.equals("") ){
							resultMap.put("code", Constant.code.CODE_2);
							resultMap.put("msg", Constant.message.MESSAGE_2);
						}else{ 
							if(pwd.length()!=6){
								resultMap.put("code", Constant.code.CODE_10);
								resultMap.put("msg", Constant.message.MESSAGE_10);
							}else{
								if(!isNumeric(pwd)){//必须为数字6
									resultMap.put("code", Constant.code.CODE_10);
									resultMap.put("msg", Constant.message.MESSAGE_10);
								}else{
									paramsMap.clear();
									paramsMap.put("amount", amount);
									paramsMap.put("payPwd", MD5Utils.string2MD5(pwd));
									paramsMap.put("status", "0");
									employeeMapper.updateEmployeeInfo(paramsMap);
									resultMap.put("code", Constant.code.CODE_1);
									resultMap.put("msg", Constant.message.MESSAGE_1);
									
									JSONObject interimMap1=new JSONObject();
									JSONObject interimResultMap=new JSONObject();
									interimMap1.put("modelType", "AC003");
									interimMap1.put("messageType", "3");
									interimMap1.put("userId",  employeeId); 
									messageService.saveMessage(interimMap1, interimResultMap);
								
								}
							}
						}
						break;
					case "3": //修改用户信息
						//判断登录情况
						Map<String, Object> interimMap=new HashMap<>();
						interimMap.put("amount", amount);
						interimMap.put("operateType", type);//
						endEmpList=employeeMapper.selectEmplyeeInfo(interimMap);
						if(endEmpList.size()==0){
							resultMap.put("code", Constant.code.CODE_12);
							resultMap.put("msg", Constant.message.MESSAGE_12);
						}else{
							//头像，性别，生日
							paramsMap.clear();
							amount=(String) endEmpList.get(0).get("amount");
							String empCode=(String) endEmpList.get(0).get("employeeCode");
							paramsMap.put("amount", amount);
							paramsMap.put("headImg", headImg);
							//  图片处理  如果存在图片，则把图片复制到对应的文件中
							String updateFlag="fasle";
							if(!headImg.equals("")){
								//读取配置文件。复制图片
								String fileUrl=GetProperties.getFileUrl("PGZUserUrl");

								String flag=FileUtils.isExist(fileUrl);
								if(flag.equals("mkSucc")){
									fileUrl+=empCode+"/";
									flag=FileUtils.isExist(fileUrl);
									if(flag.equals("mkSucc")){
										//如果之前存在头像图片，则删除图片
										String headImgName = BaseParseImage.generateImage(headImg, fileUrl, ".jpg",
												null);
										paramsMap.put("headImg", headImgName);

										String dbHeadImg=endEmpList.get(0).get("headImg")+"";
										if( !dbHeadImg.equals("null") && !dbHeadImg.equals("")){
											//删除图片
											FileUtils.deleteFile(fileUrl+"/"+dbHeadImg);
										} 
										updateFlag="true";
									}else{
										resultMap.put("code", Constant.code.CODE_21);
										resultMap.put("msg", Constant.message.MESSAGE_21);
									}
								}else{
									resultMap.put("code", Constant.code.CODE_21);
									resultMap.put("msg", Constant.message.MESSAGE_21);
								}
							}else{
								updateFlag="true";
							}
							if(updateFlag.equals("true")){
								if(!gender.equals("")){
									//必须为数字
									if(isNumeric(gender) && (gender.equals("1") || gender.equals("0"))){
										updateFlag="true";
									}else{
										updateFlag="false";
										resultMap.put("code", Constant.code.CODE_10);
										resultMap.put("msg", Constant.message.MESSAGE_10);
									}
								}else{
									updateFlag="true";
								}
								
							}
							if(updateFlag.equals("true")){
								paramsMap.put("gender", gender);
								paramsMap.put("birthday", birthday);
								paramsMap.put("status", "0");
								employeeMapper.updateEmployeeInfo(paramsMap);
								paramsMap.clear();
								paramsMap.put("uuid", uuid);
								systemService.validUser(paramsMap, resultMap); 
							/*	JSONObject interimMap1=new JSONObject();
								JSONObject interimResultMap=new JSONObject();
								interimMap1.put("modelType", "AC006");// 修改用户信息
								interimMap1.put("messageType", "3");
								interimMap1.put("userId",  employeeId); 
								messageService.saveMessage(interimMap1, interimResultMap); 
							*/
							}
						}
						break;
						//region 
					case "4"://身份校验
						if(realName.isEmpty() || idCard.isEmpty()  ){
							resultMap.put("code", Constant.code.CODE_2);
							resultMap.put("msg", Constant.message.MESSAGE_2);
						}else{
							//先短信校验
							String idCardRegex="^(\\d{6})(19|20)(\\d{2})(1[0-2]|0[1-9])(0[1-9]|[1-2][0-9]|3[0-1])(\\d{3})(\\d|X|x)?$";
							boolean flag=Pattern.matches(idCardRegex, idCard); 
							if(flag){
								//查询该身份证是否是被注册过  只查询身份证
								interimMap= new HashMap<>();
								interimMap.put("idCard", idCard);
								List<Map<String, Object>> em=employeeMapper.selectEmplyeeInfo(interimMap);
								if(em.size()>0){//身份证已被注册
									resultMap.put("code", Constant.code.CODE_22);
									resultMap.put("msg", Constant.message.MESSAGE_22);
								}else{
									//保存身份信息到对应账户
									paramsMap.clear();
									paramsMap.put("amount", amount);
									paramsMap.put("realName", realName);
									paramsMap.put("idCard", idCard);
									paramsMap.put("idCardAuthentication", "1");
									paramsMap.put("status", "0");
									employeeMapper.updateEmployeeInfo(paramsMap);
									resultMap.put("code", Constant.code.CODE_1);
									resultMap.put("msg", Constant.message.MESSAGE_1);
									
									JSONObject interimMap1=new JSONObject();
									JSONObject interimResultMap=new JSONObject();
									interimMap1.put("modelType", "AC005");
									interimMap1.put("messageType", "3");
									interimMap1.put("userId",  employeeId); 
									messageService.saveMessage(interimMap1, interimResultMap);
								
								}
							}else{
								resultMap.put("code", Constant.code.CODE_13);
								resultMap.put("msg", Constant.message.MESSAGE_13);
							}
						}
						break;
                    //endregion
					case "5"://绑定支付宝
						if( !accountType.equals("") && !accountName.equals("") ){
							String validAccountName="false";
							// TODO 支付宝帐号校验   如果是手机号必须11位，  如果是邮箱，必须符合要求
							if(accountName.indexOf("@")==-1){//手机号
								if(RegexUtil.match(RegexUtil.SIMPLE_PHONE_CHECK, accountName)){
									 validAccountName="true";
								}else{
									resultMap.put("code", Constant.code.CODE_52);
									resultMap.put("msg", Constant.message.MESSAGE_52);
								}
							}else{//邮箱
								if(RegexUtil.match(RegexUtil.SIMPLE_MAIL_CHECK, accountName)){
									 validAccountName="true";
								}else{
									resultMap.put("code", Constant.code.CODE_53);
									resultMap.put("msg", Constant.message.MESSAGE_53);
								}
							}
							if(validAccountName.equals("true")){
								if(isNumeric(accountType) && (accountType.equals("1") || accountType.equals("2"))){
									if(accountType.equals("2")){
										resultMap.put("code", Constant.code.CODE_23);
										resultMap.put("msg", Constant.message.MESSAGE_23);
									}else{
										//根据手机号获取用户id
										interimMap=new HashMap<>();
										interimMap.put("uuid", uuid);
										endEmpList=employeeMapper.selectEmplyeeInfo(interimMap);
										if(endEmpList.size()==0){
											resultMap.put("code", Constant.code.CODE_12);
											resultMap.put("msg", Constant.message.MESSAGE_12);
										}else{
											paramsMap.clear();
											String userId=""+endEmpList.get(0).get("id");
											paramsMap.put("userId",userId);
											//其他帐号均为不绑定，
											paramsMap.put("isDefault", "0");
											employeeMapper.updateBindingAccount(paramsMap);
											
											//查询该账户是否存在
											paramsMap.put("isDefault", "");
											paramsMap.put("accountName", accountName);
											List<Map<String, Object>> baList =employeeMapper.selectBindingAccount(paramsMap);
											if(baList.size()>0){//该帐号已存在，直接修改该帐号状态
												interimMap.clear();
												
												interimMap.put("isDefault", "1");
												interimMap.put("id", baList.get(0).get("id"));
												employeeMapper.updateBindingAccount(interimMap);//该帐号绑定
											}else{//帐号不存在，直接新增
												paramsMap.put("isDefault", "1");
												paramsMap.put("typeId", accountType);
												paramsMap.put("isDel", "0");
												paramsMap.put("addTime", DateUtil.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
												paramsMap.put("status", "1");
												//新增为绑定
												employeeMapper.saveBindingAccount(paramsMap);
											}
											resultMap.put("code", Constant.code.CODE_1);
											resultMap.put("msg", Constant.message.MESSAGE_1);

											JSONObject interimMap1=new JSONObject();
											JSONObject interimResultMap=new JSONObject();
											interimMap1.put("modelType", "AC007");
											interimMap1.put("messageType", "3");
											interimMap1.put("userId",  employeeId); 
											messageService.saveMessage(interimMap1, interimResultMap);
										}
									}
								}else{
									resultMap.put("code", Constant.code.CODE_10);
									resultMap.put("msg", Constant.message.MESSAGE_10);
								}
							}
						}else{
							resultMap.put("code", Constant.code.CODE_2);
							resultMap.put("msg", Constant.message.MESSAGE_2);
						}
						break;
					default:
						 resultMap.put("code", Constant.code.CODE_10);
						 resultMap.put("msg", Constant.message.MESSAGE_10);
						break;
					}
				}else{ 
					 resultMap.put("code", Constant.code.CODE_2);
					 resultMap.put("msg", Constant.message.MESSAGE_2);
				}
			} 
		} catch (Exception e) {
			logger.error("执行--" + this.getClass().getSimpleName()+"()--异常：" + e);
			e.printStackTrace();  
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			resultMap.put("code", Constant.code.CODE_0);
			resultMap.put("msg", Constant.message.MESSAGE_0);
		}
		 return resultMap;
	 } 
	 
	 
	 /***
	  * 
	 * @Title: getBindingAccount
	 * @Description: 获取账户绑定信息
	 * @param request
	 * @param jsonparam
	 * @return    
	 * JSONObject
	  */
	 @ResponseBody
	 @RequestMapping(value = "/getBindingAccount",method = {RequestMethod.POST},produces="application/json;charset=UTF-8" )
	 public JSONObject getBindingAccount(HttpServletRequest request, @RequestBody String jsonparam) {
		    JSONObject paramsMap = new JSONObject();
		 	JSONObject resultMap = new JSONObject(); 
			try {
				paramsMap=JSONObject.fromObject(jsonparam);
				String isDefault=paramsMap.optString("isDefault");
				String flag="true";
				if(!isDefault.equals("")){
					if(!isNumeric(isDefault)){
						flag="false";
						resultMap.put("code", Constant.code.CODE_10);
						resultMap.put("msg", Constant.message.MESSAGE_10);
					} 
				}
				if(flag.equals("true")){
					//根据用户uuid 校验当前用户登录状态
					
					systemService.validUser(paramsMap, resultMap);
					String code=resultMap.optString("code");
					if(code.equals(Constant.code.CODE_1)){
						//查询出绑定账户信息
						paramsMap.put("userId", ((JSONObject)resultMap.get("data")).get("id"));
						List<Map<String, Object>> baList=employeeMapper.selectBindingAccount(paramsMap);
						for(Map<String, Object> baMap:baList){
							baMap.put("", "");
						}
					    String data=com.alibaba.fastjson.JSONObject.toJSONString
					    		(baList,SerializerFeature.WriteMapNullValue);
					    data=data.replaceAll("null","\"\"");
					    
						resultMap.put("data", data);
					} 
				}
			} catch (Exception e) {
				logger.error("执行--" + this.getClass().getSimpleName()+"()--异常：" + e);
				e.printStackTrace();
				resultMap.put("code", Constant.code.CODE_0);
				resultMap.put("msg", Constant.message.MESSAGE_0);
			}
			 return resultMap;
	 }
	 
	 /***
	  * 
	 * @Title: getAccountDetil
	 * @Description: 获取账户明细
	 * @param request
	 * @param jsonparam
	 * @return    
	 * JSONObject
	  */
	 @ResponseBody
	 @RequestMapping(value = "/getAccountDetil",method = {RequestMethod.POST},produces="application/json;charset=UTF-8" )
	 public JSONObject getAccountDetil(HttpServletRequest request, @RequestBody String jsonparam) {
		 JSONObject paramsMap = new JSONObject();
		 	JSONObject resultMap = new JSONObject(); 
			try {
				paramsMap=JSONObject.fromObject(jsonparam);
				//根据用户uuid 校验当前用户登录状态
				systemService.validUser(paramsMap, resultMap);
				if(resultMap.optString("code").equals(Constant.code.CODE_1)){
					resultMap.remove("data");
					employeeService.getAccountDetil(paramsMap, resultMap);
					
					//获取账户明细
//					List<Map<String, Object>> ba=employeeMapper.getAccountDetil(paramsMap);
//					
//					String data=com.alibaba.fastjson.JSONObject.toJSONString
//				    		(ba,SerializerFeature.WriteMapNullValue);
//				    data=data.replaceAll("null","\"\"");
//				    resultMap.put("data",data );
				}
			} catch (Exception e) {
				logger.error("执行--" + this.getClass().getSimpleName()+"()--异常：" + e);
				e.printStackTrace();
				resultMap.put("code", Constant.code.CODE_0);
				resultMap.put("msg", Constant.message.MESSAGE_0);
			}
			 return resultMap;
	 }
	 /**
	  * //查询所有设备信息
						List<Map> allDeviceInfo =systemMapper.getAllDeviceInfo(paramsMap);
						
						//保存系统通知
						interimMap.clear();
						interimMap.put("messageType", messageType);
						interimMap.put("messageTitle", messageTitle);
						interimMap.put("messageContent", messageContent);
						interimMap.put("messageImage", messageImageName);
						interimMap.put("url", url);
						interimMap.put("showType", messageType);
						interimMap.put("isDel","0"); 
						interimMap.put("addTime", DateUtil.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
						messageMapper.saveSystemMessage(interimMap);
						String sysMSGId=interimMap.optString("sysMSGId");
						//如果存在图片，则保存图片
						if(!messageImage.isEmpty()){
							
						}
						 
						int i=0;
						for(Map deviceInfo:allDeviceInfo){
							String device_token=deviceInfo.get("device_token")+"";
							String mbSystemType=deviceInfo.get("mbSystemType")+"";

							//保存推送消息
							operateId=""+ ((Map)interimResultMap.get("data")).get("adminId");
							interimMap.clear();
							interimMap.put("messageType", messageType);
							interimMap.put("messageContent", messageContent);
							interimMap.put("operateType", operateType);
							interimMap.put("operateId", operateId);
							interimMap.put("sysMSGId", sysMSGId);
							interimMap.put("addTime", DateUtil.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
							messageMapper.savePushmessagelog(interimMap);
							
						    //发送通知
							interimMap.clear();
							interimMap.put("device_token", device_token);
							interimMap.put("messageContent", messageContent);
							interimMap.put("tagType", messageType);
							interimMap.put("mbSystemType", mbSystemType);
							pushUserMessageUnicastOfPGZ(interimMap, interimResultMap);
							interimResultMap.put("code", Constant.code.CODE_1);
							interimResultMap.put("msg", Constant.message.MESSAGE_1);
							if(i++==1000){
								Thread.sleep(1000);
							}
						}
	  */
	 
	 /***
	  * 
	 * @Title: getEmployeeInfo
	 * @Description: 获取用户信息(用于环信信息更新)
	 * @param request
	 * @param jsonparam
	 * @return    
	 * JSONObject
	  */
	 @ResponseBody
	 @RequestMapping(value = "/getEasemobInfo",method = {RequestMethod.POST},produces="application/json;charset=UTF-8" )
	 public JSONObject getEasemobInfo(HttpServletRequest request, @RequestBody String jsonparam) {
		 JSONObject paramsMap = new JSONObject();
		 	JSONObject resultMap = new JSONObject(); 
			try {
				paramsMap=JSONObject.fromObject(jsonparam);
				//根据用户uuid 校验当前用户登录状态
//				systemService.validUser(paramsMap, resultMap);
				resultMap.put("id", paramsMap.get("userId"));
				List<Map<String, Object>> empList=employeeMapper.selectEmplyeeInfo(resultMap);
					if(empList.size()==0){
						resultMap.put("code", Constant.code.CODE_12);
						resultMap.put("msg", Constant.message.MESSAGE_12);
					}else{
						resultMap.clear();
				    	String headImg=empList.get(0).get("headImg")+"";
				    	String sex=(empList.get(0).get("headImg")+"");
				    	if(headImg!=null && !headImg.equals("null")){
				    		 String url=GetProperties.getFileUrl("pGZUserUrl");
				    		 String employeeCode=(String) empList.get(0).get("employeeCode");
				    		 if(url!=null && !employeeCode.equals("")  ){
				    			 empList.get(0).put("headImgUrl", (url+ employeeCode+"/"+headImg).replaceAll(" ",""));
				    		 }else{
				    			 empList.get(0).put("headImgUrl", "");
				    		 }
				    	}else{
				    		empList.get(0).put("headImgUrl", "");
				    	}
				    	JSONObject data  = new JSONObject();
				    	data.put("headImg", empList.get(0).get("headImgUrl"));
				    	data.put("employeeCode", empList.get(0).get("employeeCode"));
				    	data.put("idCardAuthentication", empList.get(0).get("idCardAuthentication"));
				    	resultMap.put("code", Constant.code.CODE_1);
						resultMap.put("msg", Constant.message.MESSAGE_1);
						resultMap.put("data", data);
					}
			} catch (Exception e) {
				logger.error("执行--" + this.getClass().getSimpleName()+"()--异常：" + e);
				e.printStackTrace();
				resultMap.put("code", Constant.code.CODE_0);
				resultMap.put("msg", Constant.message.MESSAGE_0);
			}
			 return resultMap;
	 }
	 
	 
	 
}
