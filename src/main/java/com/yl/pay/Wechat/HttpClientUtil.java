package com.yl.pay.Wechat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class HttpClientUtil {

	public static String post(String url, Map<String, String> headMap, Map<String, String> params) {
		try {
			HttpClient httpclient = new HttpClient();
			httpclient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
			PostMethod httpPost = new PostMethod(url);
			if (null != headMap) {
				for (String key : headMap.keySet()) {
					httpPost.setRequestHeader(key, headMap.get(key));
				}
			}

			if (null != params) {
				for (String pkey : params.keySet()) {
					httpPost.addParameter(pkey, params.get(pkey));
				}
			}
			httpclient.executeMethod(httpPost);

			BufferedReader reader = new BufferedReader(new InputStreamReader(httpPost.getResponseBodyAsStream()));
			StringBuffer stringBuffer = new StringBuffer();
			String str = "";
			while ((str = reader.readLine()) != null) {
				stringBuffer.append(str);
			}
			reader.close();
			return stringBuffer.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String postHttplient(String url, String xmlInfo) {
		try {
			HttpClient httpclient = new HttpClient();
			httpclient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
			PostMethod httpPost = new PostMethod(url);
			httpPost.setRequestEntity(new StringRequestEntity(xmlInfo));
			httpclient.executeMethod(httpPost);

			BufferedReader reader = new BufferedReader(new InputStreamReader(httpPost.getResponseBodyAsStream()));
			StringBuffer stringBuffer = new StringBuffer();
			String str = "";
			while ((str = reader.readLine()) != null) {
				stringBuffer.append(str);
			}
			reader.close();
			return stringBuffer.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 需要加密执行的
	 * 
	 * @param url
	 * @param xmlInfo
	 * @return
	 * @throws Exception
	 */
	// public static String postHttplientNeedSSL(String url,
	// String xmlInfo,
	// String cretPath,
	// String mrchId)
	// throws Exception{
	// //选择初始化密钥文件格式
	// KeyStore keyStore = KeyStore.getInstance("PKCS12");
	// //得到密钥文件流
	// FileInputStream instream = new FileInputStream(new File(cretPath));
	// try{
	// //用商户的ID 来解读文件
	// keyStore.load(instream, mrchId.toCharArray());
	// }finally{
	// instream.close();
	// }
	// //用商户的ID 来加载
	// SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore,
	// mrchId.toCharArray()).build();
	// // Allow TLSv1 protocol only
	// SSLConnectionSocketFactory sslsf = new
	// SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" }, null,
	// SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
	// //用最新的httpclient 加载密钥
	// CloseableHttpClient httpclient =
	// HttpClients.custom().setSSLSocketFactory(sslsf).build();
	// StringBuffer ret = new StringBuffer();
	// try{
	// HttpPost httpPost = new HttpPost(url);
	// httpPost.setEntity(new StringEntity(xmlInfo));
	// CloseableHttpResponse response = httpclient.execute(httpPost);
	// try{
	// HttpEntity entity = response.getEntity();
	// if(entity != null){
	// BufferedReader bufferedReader = new BufferedReader(new
	// InputStreamReader(entity.getContent()));
	// String text;
	// while((text = bufferedReader.readLine()) != null){
	// ret.append(text);
	// }
	// }
	// EntityUtils.consume(entity);
	// }finally{
	// response.close();
	// }
	// }finally{
	// httpclient.close();
	// }
	// return ret.toString();
	// }
	//
	// public static String postHtpps(String urlStr,
	// String xmlInfo){
	// try{
	// URL url = new URL(urlStr);
	// URLConnection con = url.openConnection();
	// con.setDoOutput(true);
	// con.setRequestProperty("Pragma:", "no-cache");
	// con.setRequestProperty("Cache-Control", "no-cache");
	// con.setRequestProperty("Content-Type", "text/xml;charset=utf-8");
	// OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream(),
	// "utf-8");
	// out.write(xmlInfo);
	// out.flush();
	// out.close();
	// BufferedReader br = new BufferedReader(new
	// InputStreamReader(con.getInputStream()));
	// StringBuffer lines = new StringBuffer();
	// String line = "";
	// for(line = br.readLine(); line != null; line = br.readLine()){
	// lines.append(line);
	// }
	// return lines.toString();
	// }catch(MalformedURLException e){
	// e.printStackTrace();
	// }catch(IOException e){
	// e.printStackTrace();
	// }
	// return null;
	// }

}
