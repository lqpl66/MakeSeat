package com.yl.Service;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yl.Utils.CodeUtils;
import com.yl.Utils.Constant;
import com.yl.Utils.DateUtil;
import com.yl.Utils.SMSAPI;
import com.yl.mapper.EmployeeMapper;
import com.yl.mapper.SystemMapper;
import net.sf.json.JSONObject;

@Service
public class SystemService {

	private static Logger logger = Logger.getLogger(SystemService.class);
	
	@Autowired
	private EmployeeMapper employeeMapper;

	@Autowired
	private SystemMapper systemMapper;
	@Autowired
	private EmployeeService employeeService;
	//获取验证码
	@SuppressWarnings("unchecked")
	public void getcheckcode(JSONObject paramsMap,JSONObject resultMap){
		String type=paramsMap.optString("type");
		String mobile=paramsMap.optString("mobile");
		paramsMap.put("amount", mobile);
		Map<String, Object> interimMap=new HashMap<>();
		interimMap.put("amount", mobile);
		List<Map<String, Object>> empList=employeeMapper.selectEmplyeeInfo(interimMap);
		 //判断获取验证码类型
		String flag="false";
		
		switch (type) {
		case "0": // 注册
			//校验该手机号是否注册，没注册发送验证码
			if(empList.size()>0 ){//手机号已存在
				resultMap.put("code", Constant.code.CODE_3);
				resultMap.put("msg", Constant.message.MESSAGE_3);
			}else{//不存在，发送短信
				flag="true";
			}
			break;
		case "1": // 修改登录密码 
			if(empList.size()>0){
				String status=""+empList.get(0).get("status");
				if(status.equals("0")){
					flag="true";
				}else{
					resultMap.put("code", Constant.code.CODE_6);
					resultMap.put("msg", Constant.message.MESSAGE_6);
				}
			}else{
				resultMap.put("code", Constant.code.CODE_5);
				resultMap.put("msg", Constant.message.MESSAGE_5);
			}
		case "2": // 修改提现密码
			if(empList.size()>0){
				String status=""+empList.get(0).get("status");
				if(status.equals("0")){
					flag="true";
				}else{
					resultMap.put("code", Constant.code.CODE_6);
					resultMap.put("msg", Constant.message.MESSAGE_6);
				}
			}else{
				resultMap.put("code", Constant.code.CODE_5);
				resultMap.put("msg", Constant.message.MESSAGE_5);
			}
			break;
		case "3": // 手机号登录发送验证码
			if(empList.size()>0){
				String status=""+empList.get(0).get("status");
				if(status.equals("0")){
					flag="true";
				}else{
					resultMap.put("code", Constant.code.CODE_6);
					resultMap.put("msg", Constant.message.MESSAGE_6);
				}
			}else{
				resultMap.put("code", Constant.code.CODE_5);
				resultMap.put("msg", Constant.message.MESSAGE_5);
			}
			break;

		case "4": // 身份验证
			if(empList.size()>0){
				String status=""+empList.get(0).get("status");
				if(status.equals("0")){
					flag="true";
				}else{
					resultMap.put("code", Constant.code.CODE_6);
					resultMap.put("msg", Constant.message.MESSAGE_6);
				}
			}else{
				resultMap.put("code", Constant.code.CODE_5);
				resultMap.put("msg", Constant.message.MESSAGE_5);
			}
			break;
		case "5": // 支付宝绑定
			if(empList.size()>0){
				String status=""+empList.get(0).get("status");
				if(status.equals("0")){
					flag="true";
				}else{
					resultMap.put("code", Constant.code.CODE_6);
					resultMap.put("msg", Constant.message.MESSAGE_6);
				}
			}else{
				resultMap.put("code", Constant.code.CODE_5);
				resultMap.put("msg", Constant.message.MESSAGE_5);
			}
			break;
		default:
			resultMap.put("code", Constant.code.CODE_10);
			resultMap.put("msg", Constant.message.MESSAGE_10);
			break;
		}   
		if(flag=="true"){// 以上成功则发送短信
			String smsCode = CodeUtils.getCode(6);
			JSONObject SMS_re = SMSAPI.Send(smsCode, mobile, Integer.parseInt(type));//测试不开通
//			JSONObject SMS_re=new JSONObject();
//			SMS_re.put("flag", true);
			logger.info("获取验证码的手机号：" + mobile + ";该手机号的验证码：" + smsCode);
			if (!SMS_re.optBoolean("flag")) {
				resultMap.put(Constant.code.CODE, Constant.code.CODE_4);
				resultMap.put("msg", SMS_re.opt("message"));
			}else{
				//保存短信验证码
				paramsMap.clear();
				if(!type.equals("3")){
					paramsMap.put("operateType", type);
					paramsMap.put("mobile", mobile);
					paramsMap.put("smsCode", smsCode);
					paramsMap.put("status", "0");
					Calendar time = Calendar.getInstance();
					paramsMap.put("addTime",  DateUtil.format(time, "yyyy-MM-dd HH:mm:ss"));
					time.add(Calendar.MINUTE, 5);
					paramsMap.put("smsPwdExpiry", DateUtil.format(time, "yyyy-MM-dd HH:mm:ss"));
					systemMapper.saveLogRecord(paramsMap);
				}else{
					paramsMap.put("amount", mobile);
					paramsMap.put("smsCode", smsCode);
					Calendar time = Calendar.getInstance();
					paramsMap.put("addTime",  DateUtil.format(time, "yyyy-MM-dd HH:mm:ss"));
					time.add(Calendar.MINUTE, 5);
					paramsMap.put("smsPwdExpiry", DateUtil.format(time, "yyyy-MM-dd HH:mm:ss"));
				    employeeMapper.updateEmployeeInfo(paramsMap);
				}
				resultMap.put("smsCode", "");
				resultMap.put("code", Constant.code.CODE_1);
				resultMap.put("msg", Constant.message.MESSAGE_1);
			}
		}
	}
	 
	/**
	 * 
	*  
	* Title: validUser
	* Description：<pre>根据uuid校验用户登录信息</pre> 
	* @param paramsMap 请求参数集合
	*  <pre> uuid string 必填 用户登录唯一标示 </pre>  
	* @param resultMap 返回结果集合
	* <pre> 
	* code string 返回状态
	* msg  String 状态说明
	* data JSONObject 成功后返回用户信息集合
	*  </pre>  
	* @return: void 
	*
	 */
	public void validUser(JSONObject paramsMap,JSONObject resultMap){
		String uuid = paramsMap.optString("uuid");
		String model = paramsMap.optString("model");

		JSONObject interimMap=new JSONObject();
		JSONObject interimResultMap=new JSONObject();
		interimMap.put("uuid", uuid);
		if(uuid.equals("")){
			resultMap.put("code", Constant.code.CODE_2);
			resultMap.put("msg", Constant.message.MESSAGE_2);
		}else{
			employeeService.getEmployeeInfo(interimMap, resultMap);
			String code =(String) resultMap.get("code");
			if(code.equals(Constant.code.CODE_1)){
				JSONObject empList=(JSONObject) resultMap.get("data");
				resultMap.remove("data");
				if(empList.size()==0){

			    	switch (model) {
					case "0":
						break;
					case "1":
						Map<String, Object> state=new HashMap<>();
						state.put("state", false);
						resultMap.put("data", state);
						break;
					default:
						break;
					}
					resultMap.put("code", Constant.code.CODE_14);
					resultMap.put("msg", Constant.message.MESSAGE_14);
				}else{
					String uuidExpiry=empList.optString("uuidExpiry");
					String status=empList.optString("status");
					if(!status.equals("0")){
						switch (model) {
						case "0":
							break;
						case "1":
							Map<String, Object> state=new HashMap<>();
							state.put("state", false);
							resultMap.put("data", state);
							break;
						default:
							break;
						}
						resultMap.put("code", Constant.code.CODE_6);
						resultMap.put("msg", Constant.message.MESSAGE_6);
					}else{ 
					    boolean d=DateUtil.getCompareDateOFtime(uuidExpiry, DateUtil.getCurrentTime());
					    if(!d){
					    	switch (model) {
							case "0":
								break;
							case "1":
								Map<String, Object> state=new HashMap<>();
								state.put("state", false);
								resultMap.put("data", state);
								break;
							default:
								break;
							}
							resultMap.put("code", Constant.code.CODE_14);
							resultMap.put("msg", Constant.message.MESSAGE_14);
					    }else{
					    	switch (model) {
							case "0":
								resultMap.put("data", empList);
								break;
							case "1":
								Map<String, Object> state=new HashMap<>();
								state.put("state", true);
								resultMap.put("data", state);
								break;
							default:
								resultMap.put("data", empList);
								break;
							}

							resultMap.put("code", Constant.code.CODE_1);
							resultMap.put("msg", Constant.message.MESSAGE_1);
					    }
					}
				}
			} else{
				switch (model) {
				case "0":
					break;
				case "1":
					Map<String, Object> state=new HashMap<>();
					state.put("state", false);
					resultMap.put("data", state);
					break;
				default:
					break;
				}
			}
		}
	}
	 
	
}
