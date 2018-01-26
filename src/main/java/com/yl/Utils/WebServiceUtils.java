package com.yl.Utils;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;

public class WebServiceUtils {
	private static Logger logger = Logger.getLogger(WebServiceUtils.class);

	public static String postByMap(String url, Map<String, Object> pv) {
		logger.debug(String.format("Request url:%s", url));
		String responseMsg = "";
		PostMethod postMethod = null;
		try {
			HttpClient httpClient = new HttpClient();
			httpClient.getParams().setContentCharset("utf-8");
			postMethod = new PostMethod(url);

			Set<String> set = pv.keySet();
			Iterator<String> it = set.iterator();
			while (it.hasNext()) {
				String key = it.next();
				Object value = pv.get(key);
				if (null != value)
					postMethod.addParameter(key, value.toString());
				else
					postMethod.addParameter(key, "");
			}

			httpClient.executeMethod(postMethod);// 200 responseMsg =
			postMethod.getResponseBodyAsString().trim();
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			postMethod.releaseConnection();
		}

		return responseMsg;
	}
//	
//	public String doPost(String url,Map<String,String> map,String charset){  
//        HttpClient httpClient = null;  
//        HttpHost httpPost = null;  
//        String result = null;  
//        try{  
//            httpClient = new HttpClient();  
//            httpPost = new HttpHost(url);  
//            //设置参数   
//            List<NameValuePair> list = new ArrayList<NameValuePair>();  
//            Iterator<Entry<String, String>> iterator = map.entrySet().iterator();  
//            while(iterator.hasNext()){  
//                Entry<String,String> elem = (Entry<String, String>) iterator.next();  
//                list.add(new NameValuePair(elem.getKey(),elem.getValue()));  
//            }  
//            if(list.size() > 0){  
//                UrlEncoded entity = new UrlEncoded(list,charset);  
//                httpPost.setEntity(entity);  
//            }  
//            HttpResponse response = httpClient.execute(httpPost);  
//            if(response != null){  
//                HttpEntity resEntity = response.getEntity();  
//                if(resEntity != null){  
//                    result = EntityUtils.toString(resEntity,charset);  
//                }  
//            }  
//        }catch(Exception ex){  
//            ex.printStackTrace();  
//        }  
//        return result;  
//    }  
   
         
}
