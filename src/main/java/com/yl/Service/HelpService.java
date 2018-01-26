package com.yl.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yl.Utils.BaseParseImage;
import com.yl.Utils.Constant;
import com.yl.Utils.DateUtil;
import com.yl.Utils.FileUtils;
import com.yl.Utils.GetProperties;
import com.yl.Utils.RegexUtil;
import com.yl.mapper.HelpMapper;

import net.sf.json.JSONObject;

@Service
public class HelpService {

	 private static Logger logger = Logger.getLogger(HelpService.class);

	 @Autowired
     private  SystemService systemService;

	 @Autowired
	 private HelpMapper helpMapper;
	 public static boolean isNumeric(String str){
		  for (int i = 0; i < str.length(); i++){
			   if (!Character.isDigit(str.charAt(i))){
			      return false;
			   }
		  }
		  return true;
	}
	 
	 
	 public void getFeedback(JSONObject paramsMap,JSONObject resultMap) throws Exception{
		 //临时参数
		 JSONObject interimMap=new JSONObject();
		 JSONObject interimResultMap=new JSONObject();
		 //校验必填项
		 String uuid = paramsMap.optString("uuid");
		 String rows=paramsMap.optString("rows");
		 String page=paramsMap.optString("page");
		 String searchContent=paramsMap.optString("searchContent");
		 String flag="true";
//		 if(uuid!=null && !uuid.equals("")){
//			 systemService.validUser(paramsMap, interimResultMap);//校验登录
//			 if(interimResultMap.optString("code").equals(Constant.code.CODE_1)){
//				 flag="true";
//			 }else{
//				 flag="false";
//			 }
//		 } 
		 if(flag.equals("true")){
			 if (rows.isEmpty() || page.isEmpty()  ) {
					resultMap.put("code", Constant.code.CODE_2);
					resultMap.put("msg", Constant.message.MESSAGE_2);
				}else{  
					if(  !RegexUtil.match(RegexUtil.IsIntNumber,page) || !RegexUtil.match(RegexUtil.IsIntNumber,rows)){
						resultMap.put("code", Constant.code.CODE_10);
						resultMap.put("msg", Constant.message.MESSAGE_10);
					}else{
						int pageSize = Integer.parseInt(rows);// 条数
						int pageIndex = Integer.parseInt(page);
						List<Map<String,Object>> list =new ArrayList<>();
						if(pageIndex<=0){
						}else{
							interimMap.put("pageSize", pageSize);
							interimMap.put("beginNum", (pageIndex - 1) * pageSize);
							interimMap.put("paramSidx", "");
							interimMap.put("paramSord", "");
							
//							interimMap.put("uuid", uuid);
							interimMap.put("type", "0");
							interimMap.put("searchContent",searchContent);
							//查询数据
							
							 list = helpMapper.getFeedback(interimMap); 
							
						}
					
						String data=com.alibaba.fastjson.JSONObject.toJSONString
					    		(list,SerializerFeature.WriteMapNullValue).replace("null", "\"\"");
						resultMap.put("data", data);
						resultMap.put("code", Constant.code.CODE_1);
						resultMap.put("msg", Constant.message.MESSAGE_1);
					
					}
				}
		 }
	 }
	 public void commonSaveFeedback(JSONObject paramsMap,JSONObject resultMap) throws Exception{
		//临时参数
		 JSONObject interimMap=new JSONObject();
		 JSONObject interimResultMap=new JSONObject();
		 //校验必填项
		 String uuid = paramsMap.optString("uuid");
		 String type = "0";
		 String fbType = "1";
		 String feedbackTitle = paramsMap.optString("feedbackTitle");
		 String feedbackContent = paramsMap.optString("feedbackContent");
		 System.out.println(feedbackContent);
		 String feedbackMobile = paramsMap.optString("feedbackMobile");
		 String imgList = paramsMap.optString("imgList");
		 interimMap.put("uuid",uuid);
		 systemService.validUser(interimMap, interimResultMap);//校验登录
		 if(interimResultMap.optString("code").equals(Constant.code.CODE_1)){
			 if(type.equals("") || fbType.equals("")){
				 interimResultMap.put("code", Constant.code.CODE_2);
				 interimResultMap.put("msg", Constant.message.MESSAGE_2);
			 }else{
				if(!isNumeric(fbType) ){
					interimResultMap.put("code", Constant.code.CODE_10);
					interimResultMap.put("msg", Constant.message.MESSAGE_10);
				}else{
					if(fbType.equals("0") || fbType.equals("1")){
						switch (type) {
						case "0"://新增
							/**
							 * 新增
							 *  注意：如果图片list有图片，有则保存
							 */
							//2-1:校验
							if(feedbackTitle.isEmpty() || feedbackContent.isEmpty()){
								interimResultMap.put("code", Constant.code.CODE_2);
								interimResultMap.put("msg", Constant.message.MESSAGE_2);
							}else{
								imgList.split(",");
								Gson gson = new Gson(); 
								List<String> imgMapList= gson.fromJson(imgList, new TypeToken<List<String>>(){}.getType());  
								if(imgMapList!= null && imgMapList.size()>6){
									interimResultMap.put("code", Constant.code.CODE_10);
									interimResultMap.put("msg", Constant.message.MESSAGE_10);
								} else{
									String imgFlag="true";
									/*for(String imgStr:imgMapList){
										System.out.println(imgStr.length());
										if(imgStr.length()>=30){
											imgFlag="false";
										}
									}
									if(!imgFlag.equals("true")){
										interimResultMap.put("code", Constant.code.CODE_24);
										interimResultMap.put("msg", Constant.message.MESSAGE_24);
									}else{*/
										interimMap.put("userId",  ((Map<String, Object >)interimResultMap.get("data")).get("id").toString());
										interimMap.put("employeeCode",  ((Map<String, Object >)interimResultMap.get("data")).get("employeeCode").toString());
										interimMap.put("feedbackTitle", feedbackTitle);
										interimMap.put("feedbackContent", feedbackContent);
										interimMap.put("feedbackMobile", feedbackMobile);
										interimMap.put("imgList", imgMapList);
										interimMap.put("type", fbType);
										interimMap.put("addTime", DateUtil.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
										interimMap.put("isDel", "0");
										interimMap.put("sort", "0");
										addFeedback(interimMap, interimResultMap);  
//									}
								}
							}
							break;
						/*case "1"://删除
							delFeedback(interimMap, interimResultMap);

							break;
						case "2"://更改
							updateFeedback(interimMap, interimResultMap);

							break;*/
						default:
							interimResultMap.put("code", Constant.code.CODE_10);
							interimResultMap.put("msg", Constant.message.MESSAGE_10);
							break;
						}
					}else{
						 interimResultMap.put("code", Constant.code.CODE_10);
						 interimResultMap.put("msg", Constant.message.MESSAGE_10);
					}
				}
			 }
		 }
		 
		 resultMap.put("code", interimResultMap.optString("code"));
		 resultMap.put("msg", interimResultMap.optString("msg"));
	 }
	 /**
	  * 
	 * @Title: addFeedback 
	 * @Description:  新增
	 * @param interimMap
	 * @param interimResultMap
	 * @throws Exception    
	 * void
	  */
	 public void addFeedback(JSONObject interimMap,JSONObject interimResultMap) throws Exception{

		 //保存反馈内容，返回fd_id 
		 helpMapper.insertFeedback(interimMap);
		 String fdId=interimMap.optString("id");
		 String employeeCode=interimMap.optString("employeeCode");
		 //保存多图
		 List<String> imgList=(List<String>) interimMap.get("imgList");
		 List<Map> fbImgList=new ArrayList<>();
		 String fileUrl=GetProperties.getFileUrl("PGZFeedBackUrl");
		 String flag=FileUtils.isExist(fileUrl);
		 if(flag.equals("mkSucc")){
			 fileUrl+=employeeCode+"/";
			 flag=FileUtils.isExist(fileUrl);
			 if(flag.equals("mkSucc")){
				 if(imgList!=null && imgList.size()>0){
					 	 Map<String, Object> imgMap=null;
					 	 
						 for(String imgStr:imgList){
							 imgMap=new HashMap<>();
							 String imgName = BaseParseImage.generateImage(imgStr, fileUrl, ".jpg",null);
							 imgMap.put("imgName", imgName);
							 imgMap.put("fdId", fdId);
							 fbImgList.add(imgMap);
						 }
						 helpMapper.insertFeedbackImg(fbImgList);
				 }
			 }
		 } 
		 
		 //保存图片
		 interimResultMap.put("code", Constant.code.CODE_1);
		 interimResultMap.put("msg", Constant.message.MESSAGE_1);
	 } 
	 /**
	  * 
	 * @Title: delFeedback 
	 * @Description:  删除
	 * @param interimMap
	 * @param interimResultMap
	 * @throws Exception    
	 * void
	  */
	 public void delFeedback(JSONObject interimMap,JSONObject interimResultMap) throws Exception{
		 
	 } 
	 /**
	  * 
	 * @Title: updateFeedback 
	 * @Description:  修改
	 * @param interimMap
	 * @param interimResultMap
	 * @throws Exception    
	 * void
	  */
	 public void updateFeedback(JSONObject interimMap,JSONObject interimResultMap) throws Exception{
		 
	 } 
	 
}
