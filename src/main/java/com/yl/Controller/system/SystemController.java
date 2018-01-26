package com.yl.Controller.system;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import com.yl.Service.SystemService;
import com.yl.Utils.CodeUtils;
import com.yl.Utils.Constant;
import com.yl.Utils.DateUtil;
import com.yl.Utils.Easemob;
import com.yl.Utils.MD5Utils;
import com.yl.Utils.RegexUtil;
import com.yl.mapper.EmployeeMapper;
import com.yl.mapper.SystemMapper;

import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "system")
public class SystemController {

	private static Logger logger = Logger.getLogger(SystemController.class);

	@Autowired
	private SystemService systemService;
	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private EmployeeMapper employeeMapper;
	@Autowired
	private SystemMapper systemMapper;

	/***
	 * http://192.168.1.23:8080/MSeat/system/getcheckcode
	 * {"mobile":"18262911489","operateType":"1" }
	 * 
	 * @Title: getcheckcode
	 * @Description: 获取验证码
	 * @param request
	 * @param jsonparam
	 * @return JSONObject
	 */
	@ResponseBody
	@Transactional
	@RequestMapping(value = "/getcheckcode", method = {
			RequestMethod.POST }, produces = "application/json;charset=UTF-8")
	public JSONObject getcheckcode(HttpServletRequest request, @RequestBody String jsonparam) {
		JSONObject paramsMap = new JSONObject();
		JSONObject resultMap = new JSONObject();
		// 准备注册
		try {
			paramsMap = JSONObject.fromObject(jsonparam);
			String mobile = paramsMap.optString("mobile");
			String type = paramsMap.optString("type");
			paramsMap.remove("uuid");
			if (!mobile.isEmpty() && !type.isEmpty()) {
				// 校验手机号
				if (RegexUtil.isPhone(mobile) && isNumeric(type)) {
					systemService.getcheckcode(paramsMap, resultMap);

				} else {
					resultMap.put("code", Constant.code.CODE_10);
					resultMap.put("msg", Constant.message.MESSAGE_10);
				}
			} else {
				resultMap.put("code", Constant.code.CODE_2);
				resultMap.put("msg", Constant.message.MESSAGE_2);
			}
		} catch (Exception e) {
			logger.error("执行--" + this.getClass().getSimpleName() + "()--异常：" + e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			e.printStackTrace();
			resultMap.put("code", Constant.code.CODE_0);
			resultMap.put("msg", Constant.message.MESSAGE_0);
		}
		return resultMap;
	}

	public static boolean isNumeric(String str) {
		for (int i = 0; i < str.length(); i++) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * <pre>
	 * &#64;Title: registerEasemon 
	 * &#64;Description:  
	 * &#64;param ease  必须有以下值
	 *    username  用户唯一标示 （不能重复）
		  password  环信密码
		  nickname  用户昵称
												 
	 * &#64;param resultMap
	 * </pre>
	 * 
	 * @returnType: void
	 */
	public static void registerEasemon(JSONObject ease, Map<String, Object> resultMap) {
		/*Thread smsThread = new Thread(new Runnable() {
			public void run() {*/
				// 如果出现异常则抛出
				String username = ease.optString("username");
				String password = ease.optString("password");
				String nickname = ease.optString("nickname");
				try {
//					System.out.println("开始休息");
//					new java.lang.Thread().sleep(12000);
//					System.out.println("休息完成");
					if (username.isEmpty() || password.isEmpty() || nickname.isEmpty()) {
//						resultMap.put("code", Constant.code.CODE_2);
//						resultMap.put("msg", Constant.message.MESSAGE_2);
					} else {
						Easemob.registerEasemon(ease.toString(), null);
//						resultMap.put("code", Constant.code.CODE_1);
//						resultMap.put("msg", Constant.message.MESSAGE_1);
					}
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("环信注册失败：username:"+username+" password："+password+" nickname："+nickname);
		
//					resultMap.put("code", Constant.code.CODE_0);
//					resultMap.put("msg", Constant.message.MESSAGE_0);
				}
				
		/*	}
		});
		smsThread.start();*/
	}

	/***
	 * 
	 * @Title: regist
	 * @Description: 注册
	 * @param request
	 * @param jsonparam
	 * @return JSONObject
	 */
	@ResponseBody
	@Transactional
	@RequestMapping(value = "/regist", method = { RequestMethod.POST }, produces = "application/json;charset=UTF-8")
	public JSONObject regist(HttpServletRequest request, @RequestBody String jsonparam) {
		JSONObject paramsMap = new JSONObject();
		JSONObject resultMap = new JSONObject();
		// 准备登录
		try {
			paramsMap = JSONObject.fromObject(jsonparam);
			String mobile = paramsMap.optString("mobile");
			String pwd = paramsMap.optString("pwd");
			String smsCode = paramsMap.optString("smsCode");
			//
			if (!mobile.isEmpty() && !pwd.isEmpty()) {
				// 校验手机号
				if (RegexUtil.isPhone(mobile) && !pwd.isEmpty()) {

					if (pwd.length() >= 6) {
						// 校验验证码
						if (smsCode.equals("")) {
							resultMap.put("code", Constant.code.CODE_2);
							resultMap.put("msg", Constant.message.MESSAGE_2);
						} else {
							Map<String, Object> interimMap = new HashMap<>();
							interimMap.put("amount", mobile);
							interimMap.put("operateType", "0");// 短信类型
							Map<String, Object> lr = systemMapper.selectLogRecord(interimMap);
							if (lr != null) {
								String dbSmsCode = "" + lr.get("smsCode");
								if (!dbSmsCode.equals(smsCode)) {
									resultMap.put("code", Constant.code.CODE_9);
									resultMap.put("msg", Constant.message.MESSAGE_9);
								} else {
									String smsPwdExpiry = lr.get("smsPwdExpiry").toString();
									boolean d = DateUtil.getCompareDateOFtime(smsPwdExpiry, DateUtil.getCurrentTime());
									if (!d) {
										resultMap.put("code", Constant.code.CODE_19);
										resultMap.put("msg", Constant.message.MESSAGE_19);
									} else {
										// 校验手机号是否已经注册
										paramsMap.remove("uuid");
										List<Map<String, Object>> empList = employeeMapper.selectEmplyeeInfo(paramsMap);

										if (empList.size() > 0) {
											resultMap.remove("data");
											resultMap.put("code", Constant.code.CODE_3);
											resultMap.put("msg", Constant.message.MESSAGE_3);
										} else {
											// 保存用户
											JSONObject interim1Map = new JSONObject();
											interim1Map.put("getLastId", "true");
											List<Map<String, Object>> empLastList = employeeMapper
													.selectEmplyeeInfo(interim1Map);
											int userId=0;
											if(empLastList!=null && empLastList.size()>0){
												userId = (int) (empLastList.get(0)).get("id");
											} 

											paramsMap.put("employeeCode", CodeUtils.getuserCodeOfPGZ(userId));
											String userName = "PGZ" + mobile.substring(3, mobile.length());
											paramsMap.put("nickName", userName);
											paramsMap.put("easemobPwd", mobile.substring(5, mobile.length()));
											paramsMap.put("status", "0");
											paramsMap.put("gender", "1");
											paramsMap.put("createTime", DateUtil.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
											// 用户密码采用加密
											paramsMap.put("pwd", MD5Utils.string2MD5(pwd));
											int i = employeeMapper.saveEmployeeInfo(paramsMap);
											if (i == 0) {
												resultMap.put("code", Constant.code.CODE_3);
												resultMap.put("msg", Constant.message.MESSAGE_3);
												return resultMap;
											}
											empList = employeeMapper.selectEmplyeeInfo(interimMap);
											Map EM = empList.get(0);
											String id = "" + EM.get("id");
											String easemobPwd = "" + EM.get("easemobPwd");
											String nickName = "" + EM.get("nickName");
											JSONObject ease = new JSONObject();
											ease.put("username", id);
											ease.put("password", easemobPwd);
											ease.put("nickname", nickName);
											System.out.println("2-1:" + "");
											JSONObject resultNotUseMap = new JSONObject();

											registerEasemon(ease, resultNotUseMap);
											
											//   登录返回信息 
											// 登录成功 保存对应时间到数据库中
											Calendar time = Calendar.getInstance();
											paramsMap.put("loginTime", DateUtil.format(time, "yyyy-MM-dd HH:mm:ss"));
											paramsMap.remove("gender");
											time.add(Calendar.DATE, 7);
											String uuid = UUID.randomUUID().toString().replaceAll("-", "");
											paramsMap.put("uuid", uuid);
											paramsMap.put("status", "0");
											paramsMap.put("amount", mobile);
											paramsMap.put("uuidExpiry", DateUtil.format(time, "yyyy-MM-dd HH:mm:ss"));
											paramsMap.remove("pwd");
											paramsMap.remove("smsCode");
											employeeMapper.updateEmployeeInfo(paramsMap);

											employeeService.getEmployeeInfo(paramsMap, resultMap);
											/*String data = com.alibaba.fastjson.JSONObject.toJSONString(dbEmpList.get(0),
													SerializerFeature.WriteMapNullValue);
											data = data.replaceAll("null", "\"\"");*/
											resultMap.put("data", resultMap.get("data"));
											
											resultMap.put("code", Constant.code.CODE_1);
											resultMap.put("msg", Constant.message.MESSAGE_1);
										}
									}
								}
							} else {
								resultMap.put("code", Constant.code.CODE_9);
								resultMap.put("msg", Constant.message.MESSAGE_9);
							}
						}

					} else {
						resultMap.put("code", Constant.code.CODE_7);
						resultMap.put("msg", Constant.message.MESSAGE_7);
					}
				} else {
					resultMap.put("code", Constant.code.CODE_10);
					resultMap.put("msg", Constant.message.MESSAGE_10);
				}
			} else {
				resultMap.put("code", Constant.code.CODE_2);
				resultMap.put("msg", Constant.message.MESSAGE_2);
			}
		} catch (Exception e) {
			logger.error("执行--" + this.getClass().getSimpleName() + "()--异常：" + e);
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			resultMap.clear();
			resultMap.put("code", Constant.code.CODE_0);
			resultMap.put("msg", Constant.message.MESSAGE_0);
		}
		return resultMap;
	}

	/***
	 * http://192.168.1.23:8080/MSeat/system/login
	 * {"mobile":"18262911489","operateType":"1" }
	 * 
	 * @Title: login
	 * @Description: 登录验证
	 * @param request
	 * @param jsonparam
	 * @return JSONObject
	 */
	@ResponseBody
	@Transactional
	@RequestMapping(value = "/login", method = { RequestMethod.POST }, produces = "application/json;charset=UTF-8")
	public JSONObject login(HttpServletRequest request, @RequestBody String jsonparam) {
		JSONObject paramsMap = new JSONObject();
		JSONObject resultMap = new JSONObject();
		// 准备登录
		try {
			paramsMap = JSONObject.fromObject(jsonparam);
			String mobile = paramsMap.optString("mobile");
			String pwd = paramsMap.optString("pwd");
			String type = paramsMap.optString("type");// 0密码1短信验证码
			String cityName = paramsMap.optString("cityName");
			String smsCode = paramsMap.optString("smsCode");
			paramsMap.remove("uuid");
			// 根据城市名称获取城市id
			List<Map<String, Object>> cityList = null;
			if (!cityName.equals("")) {
				cityList = systemMapper.selectCity(paramsMap);
			}
			if (cityList != null && cityList.size() > 0) {
				paramsMap.put("loginCity", cityList.get(0).get("id"));
			}
			if (!mobile.isEmpty() && !type.isEmpty()) {
				if (RegexUtil.isPhone(mobile) && isNumeric(type)) {// 校验手机号 type
																	// 类型
					// 准备登录
					Map<String, Object> seiMap = new HashMap<>();
					seiMap.put("mobile", mobile);
					List<Map<String, Object>> empList = employeeMapper.selectEmplyeeInfo(seiMap);
					if (empList.size() == 0) {
						resultMap.put("code", Constant.code.CODE_5);
						resultMap.put("msg", Constant.message.MESSAGE_5);
					} else {
						String flag = "false";
						if (type.equals("0")) {
							if (pwd.isEmpty()) {
								resultMap.put("code", Constant.code.CODE_2);
								resultMap.put("msg", Constant.message.MESSAGE_2);
							} else {	
								if( empList.get(0).get("status").toString().equals("2")){
									resultMap.put("code", Constant.code.CODE_6);
									resultMap.put("msg", Constant.message.MESSAGE_6);
								}else{
									String dbPwd = "" + empList.get(0).get("pwd");
									if (dbPwd.equals(MD5Utils.string2MD5(pwd))) {
										flag = "true";
									} else {
										resultMap.put("code", Constant.code.CODE_8);
										resultMap.put("msg", Constant.message.MESSAGE_8);
									}
								}
							}

						} else if (type.equals("1")) {// 短信验证码登录
							// 校验短信
							if (smsCode.equals("")) {
								resultMap.put("code", Constant.code.CODE_2);
								resultMap.put("msg", Constant.message.MESSAGE_2);
							} else {

								// 先短信校验
								// 查询用户表，找出短信码，和有效时间
								Map<String, Object> pm = new HashMap<>();
								pm.put("amount", mobile);
								List<Map<String, Object>> empInfoList = employeeMapper.selectEmplyeeInfo(pm);
								if (empInfoList.size() > 0) {
									String dbsmsCode = empInfoList.get(0).get("smsCode") + "";
									String smsPwdExpiry = empInfoList.get(0).get("smsPwdExpiry") + "";
									if (!dbsmsCode.equals(smsCode)) {
										resultMap.put("code", Constant.code.CODE_9);
										resultMap.put("msg", Constant.message.MESSAGE_9);
									} else {
										if( empList.get(0).get("status").toString().equals("2")){
											resultMap.put("code", Constant.code.CODE_6);
											resultMap.put("msg", Constant.message.MESSAGE_6);
										}else{
											if (dbsmsCode != null && smsPwdExpiry != null) {
												boolean d = DateUtil.getCompareDateOFtime(smsPwdExpiry,
														DateUtil.getCurrentTime());
												if (!d) {
													resultMap.put("code", Constant.code.CODE_19);
													resultMap.put("msg", Constant.message.MESSAGE_19);
												} else {

													flag = "true";
													resultMap.put("code", Constant.code.CODE_1);
													resultMap.put("msg", Constant.message.MESSAGE_1);
												}
											} else {
												resultMap.put("code", Constant.code.CODE_9);
												resultMap.put("msg", Constant.message.MESSAGE_9);
											}
										}
									}
								} else {
									resultMap.put("code", Constant.code.CODE_5);
									resultMap.put("msg", Constant.message.MESSAGE_5);
								}
							}
						} else {
							resultMap.put("code", Constant.code.CODE_10);
							resultMap.put("msg", Constant.message.MESSAGE_10);
						}
						if (flag.equals("true")) {
							// 登录成功 保存对应时间到数据库中
							Calendar time = Calendar.getInstance();
							paramsMap.put("loginTime", DateUtil.format(time, "yyyy-MM-dd HH:mm:ss"));
							paramsMap.remove("gender");
							time.add(Calendar.DATE, 7);
							String uuid = UUID.randomUUID().toString().replaceAll("-", "");
							paramsMap.put("uuid", uuid);
							paramsMap.put("status", "0");
							paramsMap.put("amount", mobile);
							paramsMap.put("uuidExpiry", DateUtil.format(time, "yyyy-MM-dd HH:mm:ss"));
							paramsMap.remove("pwd");
							paramsMap.remove("smsCode");
							employeeMapper.updateEmployeeInfo(paramsMap);

							employeeService.getEmployeeInfo(paramsMap, resultMap);
							/*String data = com.alibaba.fastjson.JSONObject.toJSONString(dbEmpList.get(0),
									SerializerFeature.WriteMapNullValue);
							data = data.replaceAll("null", "\"\"");*/
							resultMap.put("data", resultMap.get("data"));
							resultMap.put("code", Constant.code.CODE_1);
							resultMap.put("msg", Constant.message.MESSAGE_1);
						}
					}
				} else {
					resultMap.put("code", Constant.code.CODE_10);
					resultMap.put("msg", Constant.message.MESSAGE_10);
				}
			} else {
				resultMap.put("code", Constant.code.CODE_2);
				resultMap.put("msg", Constant.message.MESSAGE_2);
			}
		} catch (Exception e) {
			logger.error("执行--" + this.getClass().getSimpleName() + "()--异常：" + e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			e.printStackTrace();
			resultMap.put("code", Constant.code.CODE_0);
			resultMap.put("msg", Constant.message.MESSAGE_0);
		}
		return resultMap;
	}

	/***
	 * 
	 * @Title: login
	 * @Description: 校验用户
	 * @param request
	 * @param jsonparam
	 * @return JSONObject
	 */
	@ResponseBody
	@RequestMapping(value = "/validUser", method = { RequestMethod.POST }, produces = "application/json;charset=UTF-8")
	public JSONObject validUser(HttpServletRequest request, @RequestBody String jsonparam) {
		JSONObject paramsMap = new JSONObject();
		JSONObject resultMap = new JSONObject();
		try {
			paramsMap = JSONObject.fromObject(jsonparam);
			String uuid = paramsMap.optString("uuid");
			// 根据用户uuid 校验当前用户登录状态

			systemService.validUser(paramsMap, resultMap);

		} catch (Exception e) {
			logger.error("执行--" + this.getClass().getSimpleName() + "()--异常：" + e);
			e.printStackTrace();
			resultMap.put("code", Constant.code.CODE_0);
			resultMap.put("msg", Constant.message.MESSAGE_0);
		}
		return resultMap;
	}

	/**
	 * 
	 * @Title: validSMSCode
	 * @Description: 校验短信验证码
	 * @param request
	 * @param jsonparam
	 * @return JSONObject
	 */
	@ResponseBody
	@Transactional
	@RequestMapping(value = "/validSMSCode", method = {
			RequestMethod.POST }, produces = "application/json;charset=UTF-8")
	public JSONObject validSMSCode(HttpServletRequest request, @RequestBody String jsonparam) {
		JSONObject paramsMap = new JSONObject();
		JSONObject resultMap = new JSONObject();
		try {
			paramsMap = JSONObject.fromObject(jsonparam);
			String amount = paramsMap.optString("amount");
			String smsCode = paramsMap.optString("smsCode");
			String operateType = paramsMap.optString("operateType");
			if (operateType.equals("") || amount.equals("") || smsCode.equals("")) {
				resultMap.put("code", Constant.code.CODE_2);
				resultMap.put("msg", Constant.message.MESSAGE_2);
			} else {
				String flag = "common";
				switch (operateType) {
				case "0":// 注册

					break;
				case "1":// 修改登录密码

					break;
				case "2":// 修改提现密码

					break;
				case "3":// 短信登录 查找employee表
					flag = "false";
					employeeService.getEmployeeInfo(paramsMap, resultMap);
					JSONObject employee = (JSONObject) resultMap.get("data");
					resultMap.clear();
					if (employee.optString("smsCode").equals(smsCode)) {
						String smsPwdExpiry = employee.optString("smsPwdExpiry").toString();
						boolean d = DateUtil.getCompareDateOFtime(smsPwdExpiry, DateUtil.getCurrentTime());
						if (!d) {
							resultMap.put("code", Constant.code.CODE_19);
							resultMap.put("msg", Constant.message.MESSAGE_19);
						} else {
							resultMap.put("code", Constant.code.CODE_1);
							resultMap.put("msg", Constant.message.MESSAGE_1);
						}
					} else {
						resultMap.put("code", Constant.code.CODE_9);
						resultMap.put("msg", Constant.message.MESSAGE_9);
					}
					break;
				/*
				 * case "4"://身份验证 break;
				 */
				default:
					resultMap.put("code", Constant.code.CODE_10);
					resultMap.put("msg", Constant.message.MESSAGE_10);
					break;
				}
				if (flag.equals("common")) {
					// 校验短信记录
					Map<String, Object> lr = systemMapper.selectLogRecord(paramsMap);
					if (lr != null) {
						String dbSmsCode = "" + lr.get("smsCode");
						String dbSmsPwdExpiry = "" + lr.get("smsPwdExpiry");
						if (!dbSmsCode.equals(smsCode)) {
							resultMap.put("code", Constant.code.CODE_9);
							resultMap.put("msg", Constant.message.MESSAGE_9);
						} else {
							String smsPwdExpiry = lr.get("smsPwdExpiry").toString();
							boolean d = DateUtil.getCompareDateOFtime(smsPwdExpiry, DateUtil.getCurrentTime());
							if (!d) {
								resultMap.put("code", Constant.code.CODE_19);
								resultMap.put("msg", Constant.message.MESSAGE_19);
							} else {
								resultMap.put("code", Constant.code.CODE_1);
								resultMap.put("msg", Constant.message.MESSAGE_1);
							}
						}
					} else {
						resultMap.put("code", Constant.code.CODE_9);
						resultMap.put("msg", Constant.message.MESSAGE_9);
					}
				}
			}
		} catch (Exception e) {
			logger.error("执行--" + this.getClass().getSimpleName() + "()--异常：" + e);
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			resultMap.put("code", Constant.code.CODE_0);
			resultMap.put("msg", Constant.message.MESSAGE_0);
		}
		return resultMap;
	}
}
