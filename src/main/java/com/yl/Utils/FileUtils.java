package com.yl.Utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import net.sf.json.JSONObject;

public class FileUtils {
	private static Logger log = Logger.getLogger(FileUtils.class);

	public static boolean deleteFile(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		try {
			// 路径为文件且不为空则进行删除
			if (file.isFile() && file.exists()) {
				file.delete();
				flag = true;
			}
		} catch (Exception e) {
			log.error("图片删除失败:" , e);
		}
		return flag;
	}

    /**
     * <p>Description:判断文件夹path是否存在,如果不存在则创建文件夹 </p>
     * <p>company:youtu</p>
     * @author zgt
     * @date 2017-3-13 下午3:49:32
     */
    public static String isExist(String path) {
    	String msg="mkSucc";
    	try {
    	    File file = new File(path);
    	     if (!file.exists()) {
    	      if(file.mkdir()){//创建文件
    	      }else{
    	    	  msg="mkErr";//创建失败
    	      }
    	     }
    	     org.springframework.core.io.Resource resource = null;
 			Properties props = null;
 			resource =  new ClassPathResource("/resources.properties");
 			try {
 				props = PropertiesLoaderUtils.loadProperties(resource);
 			} catch (IOException e1) {
 				e1.printStackTrace();
 			}
 			String System=(String) props.get("System");
 			if(System.equals("Linux")){
 				Runtime.getRuntime().exec("chmod 777 " + file);  
 			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return msg;
    }
    
    public static void main(String[] args) {
		String content="<div class=\"content\"><p>原标题：罗兴亚人危机持续 孟加拉开发孤岛安置难民惹争议</p><img src=\"http://n.sinaimg.cn/translate/w800h450/20171129/v06O-fypceiq6180595.jpg\">图为难民资料图<p>海外网11月29日电 当地时间11月28日，孟加拉批准了2.8亿美元（人民币约18.5亿元）开发孟加拉湾内一座孤岛，用来暂时安置为躲避暴力而逃离邻国缅甸的10万罗兴亚人。但由于这座岛屿时常爆发洪水，该地区也常有海盗出没掠夺渔民，这项计划引发争议。</p><p>据路透社报道，为减缓难民营的压力，孟加拉和缅甸近日签署协议，将在两个月内开始遣返罗兴亚人。孟加拉国总理哈西娜（Sheikh Hasina）在其主持的一个委员会上批准开发巴赞查尔岛（Bhashan Char）计划，用来暂时安置为躲避暴力而逃离邻国缅甸的10万罗兴亚人，该安置计划将在2019年之前完成。孟加拉国计划部长卡马尔（Mustafa Kamal）指出，难民遣返工作旷日废时，此刻孟加拉国必须找到合适的地方对难民予以先行安置。</p><p>但有相关人权工作者提出质疑，表示当地不适宜居住。据了解，巴赞查尔岛距离孟加拉国最大的哈提亚岛船程需要2小时，该岛环境条件非常恶劣，遍地淤泥且地势低洼，6月至9月的雨季期间岛上会发洪水，就算风平浪静之时，该地区也常有海盗出没掠夺渔民。</p><p>罗兴亚难民问题越来越棘手，国际社会对缅甸政府的施压也在不断增大。避难孟加拉逃生的罗兴亚人原是缅甸若开邦的一个穆斯林族群，20世纪末，罗兴亚人被剥夺缅甸公民身份，加之与缅甸军方及在缅甸占多数的佛教徒之间的关系日趋紧张，备受迫害的罗兴亚人离开故土踏上逃亡之旅，在邻国孟加拉避难求生。</p><p>如今，预计有100万罗兴亚人生活在库图巴朗等地由政府提供的临时难民营里，这里生活条件异常艰苦，基础设施缺乏，甚至连自来水都没有。躲过了同胞的迫害，罗兴亚人的前路却依然严峻，联合国将罗兴亚族群列为世界上最受迫害族群之一。（海外网 朱惠悦）</p></div>";
		String fileUrl="D:/TestImg/";
		JSONObject interimMap=new JSONObject();
		JSONObject interimResultMap=new JSONObject();
		interimMap.put("content", content);
		interimMap.put("fileUrl", fileUrl);
		interimMap.put("postfix", ".html");
		saveContentToFile(interimMap, interimResultMap);
	}
    /**
     * 
    * @Title: getFileName 
    * @Description:  
    * @return    
    * fileName
     */
    public static String getFileName(){
    	String fileName  = String.valueOf(Calendar.getInstance().getTimeInMillis());
    	return fileName;
    }
    /**
     * 
    * @Title: saveContentToFile 
    * @Description:  
    * @param interimMap 请求参数集
    * <pre>    
    *     content      保存内容
    *     postfix      保存文件的后缀
    * </pre>      
    * @param interimResultMap  返回结果集  
    * <pre>   
    *    code ERROR    失败
    *         SUCCESS  成功
    *    msg           说明
    *    fileName      文件名+后缀 如： test.html
    * </pre>   
    * void
     */
    public static void saveContentToFile(JSONObject interimMap,JSONObject interimResultMap){
    	String content=interimMap.optString("content");
    	String postfix=interimMap.optString("postfix");//后缀
    	String fileName=getFileName()+postfix;
    	String fileUrl=interimMap.optString("fileUrl") ;
    	  try  
    	    {      
    		  if(postfix.isEmpty()){
    			  interimResultMap.put("code", "ERROR");
    			  interimResultMap.put("msg", "后缀不能为空");
    		  }else{
    			  if(fileUrl.isEmpty()){
        			  interimResultMap.put("code", "ERROR");
        			  interimResultMap.put("msg", "文件保存路径不能为空");
    			  }else{
    				  fileUrl=fileUrl+fileName;
        			  FileOutputStream writerStream = new FileOutputStream(fileUrl);    
            		  BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(writerStream, "gbk")); 
            		  writer.write(content);
            		  writer.close();  
            		  interimResultMap.put("fileName", fileName);
        			  interimResultMap.put("code", "SUCCESS");
        			  interimResultMap.put("msg", "文件创建成功");
    			  }
    		  }
    	    }  
    	    catch (IOException e)  
    	    {  
    	      e.printStackTrace();  
  			  interimResultMap.put("code", "ERROR");
  			  interimResultMap.put("msg", "文件保存失败，请检查路径，及内容");
    	    }  
    }
	
}
