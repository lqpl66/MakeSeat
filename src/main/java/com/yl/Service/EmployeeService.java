package com.yl.Service;

import java.math.BigDecimal;
import java.util.Formatter;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.yl.Utils.CodeUtils;
import com.yl.Utils.Constant;
import com.yl.Utils.GetProperties;
import com.yl.Utils.RegexUtil;
import com.yl.bean.PageData;
import com.yl.bean.PageInfo;
import com.yl.mapper.EmployeeMapper;

import net.sf.json.JSONObject;

@Service
public class EmployeeService {

	private static Logger logger = Logger.getLogger(EmployeeService.class);
	
	@Autowired
	private EmployeeMapper employeeMapper;
	 public static boolean isNumeric(String str){
		  for (int i = 0; i < str.length(); i++){
			   if (!Character.isDigit(str.charAt(i))){
			      return false;
			   }
		  }
		  return true;
	}
	/**
	 * 
	* @Title: getEmployeeInfo 
	* @Description:  根据uuid获取用户信息
	* @param paramsMap
	* @param resultMap   data 中封装用户信息  
	* void
	 */
	public void getEmployeeInfo(JSONObject paramsMap,JSONObject resultMap){
		resultMap.put("uuid", paramsMap.get("uuid"));
		resultMap.put("mobile", paramsMap.get("mobile"));
		resultMap.put("id", paramsMap.get("id"));
		List<Map<String, Object>> empList=employeeMapper.selectEmplyeeInfo(resultMap);
		resultMap.clear();
		if(empList.size()==0){
			resultMap.put("code", Constant.code.CODE_14);
			resultMap.put("msg", Constant.message.MESSAGE_14);
		}else{
			for(Map<String, Object> emp:empList){

				if(emp.get("accountName")==null){
		    		emp.put("accountName", "");
				}else{
		    		emp.put("accountName", CodeUtils.getAccountName(emp.get("accountName")+""));
				}
		    	String headImg=emp.get("headImg")+"";
		    	String sex=(emp.get("headImg")+"");
		    	if(headImg!=null && !headImg.equals("null")){
		    		 String url=GetProperties.getFileUrl("pGZUserUrl");
		    		 String employeeCode=(String) emp.get("employeeCode");
		    		 if(url!=null && !employeeCode.equals("")  ){
				    		emp.put("headImgUrl", (url+ employeeCode+"/"+headImg).replaceAll(" ",""));
		    		 }else{
				    		emp.put("headImgUrl", "");
		    		 }
		    	}else{
		    		emp.put("headImgUrl", "");
		    	}
		    	if(emp.get("idCard")!=null && !emp.get("idCard").equals("")){
		    		emp.put("idCard", CodeUtils.getIdCard(emp.get("idCard")+""));
				}
		    	if(!("").equals(emp.get("amount"))){ 
		    		emp.put("amountHide", CodeUtils.getAccountName(emp.get("amount")+""));
		    	}
		    	
		    }
			
			
		    String data=com.alibaba.fastjson.JSONObject.toJSONString
		    		(empList.get(0),SerializerFeature.WriteMapNullValue);
		    data=data.replaceAll("null","\"\"");
		    
			resultMap.put("data", data);
			resultMap.put("code", Constant.code.CODE_1);
			resultMap.put("msg", Constant.message.MESSAGE_1);
		}
	}
	/**
	 * 
	* @Title: getAccountDetil 
	* @Description:  
	* @param json
	* @param params
	 */
	public void getAccountDetil(JSONObject paramsMap,JSONObject resultMap ) {
		String rows=paramsMap.optString("rows");
		String page=paramsMap.optString("page");
		if (rows.isEmpty() || page.isEmpty()  ) {
			resultMap.put("code", Constant.code.CODE_2);
			resultMap.put("msg", Constant.message.MESSAGE_2);
		}else{
			if( !RegexUtil.match(RegexUtil.IsIntNumber,page) || !RegexUtil.match(RegexUtil.IsIntNumber,rows)){
				resultMap.put("code", Constant.code.CODE_10);
				resultMap.put("msg", Constant.message.MESSAGE_10);
			}else{
				int pageSize = Integer.parseInt(rows);// 条数
				int pageIndex = Integer.parseInt(page);
				if(pageIndex<=0){
					pageIndex=1;
				} 
				paramsMap.put("pageSize", pageSize);
				paramsMap.put("beginNum", (pageIndex - 1) * pageSize);
				// 查询列表数据
				List<Map<String,Object>> list = employeeMapper.getAccountDetil(paramsMap);
				String data=com.alibaba.fastjson.JSONObject.toJSONString
			    		(list,SerializerFeature.WriteMapNullValue);
			    data=data.replaceAll("null","\"\"");
			    
				resultMap.put("data", data);
				resultMap.put("code", Constant.code.CODE_1);
				resultMap.put("msg", Constant.message.MESSAGE_1);
			}
		}
	}

}
