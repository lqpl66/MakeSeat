package com.yl.Http;

import java.sql.SQLException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yl.Utils.Constant;

import net.sf.json.JSONObject;

/*
 * @author  lqpl66
 * @date 创建时间：2017年11月15日 上午11:01:34 
 * @function     
 */
public class HttpGetStationTrain {
	public static String queryUrl1 = "https://kyfw.12306.cn/otn/";
	public static String queryUrl2 = "leftTicket/queryO?";
	public static String queryByTrainNoUrl = "https://kyfw.12306.cn/otn/czxx/queryByTrainNo?";

	public static String doGet(String url, String charset) {
		HttpClient httpClient = null;
		HttpGet httpGet = null;
		String result = null;
		try {
			httpClient = new SSLClient();
			httpGet = new HttpGet(url);
			// httpGet.addHeader("Content-Type", "text/plain;charset=utf-8");
			httpGet.addHeader("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			httpGet.addHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
			HttpResponse response = httpClient.execute(httpGet);
			if (response != null) {
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					result = EntityUtils.toString(resEntity, charset);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	public static String doGet(String url, String jsonstr, String charset) {
		HttpClient httpClient = null;
		HttpGet httpGet = null;
		String result = null;
		try {
			httpClient = new SSLClient();
			httpGet = new HttpGet(url);
			httpGet.addHeader("Content-Type", "application/json");
			StringEntity se = new StringEntity(jsonstr);
			se.setContentType("text/json");
			se.setContentEncoding(new BasicHeader("Content-Type", "application/json"));
			// httpGet.setEntity(se);
			HttpResponse response = httpClient.execute(httpGet);
			if (response != null) {
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					result = EntityUtils.toString(resEntity, charset);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	@SuppressWarnings("deprecation")
	public static String doGet1(String url, String charset) {
		HttpClient httpClient = null;
		HttpGet httpGet = null;
		String result = null;
		try {
			httpClient = new SSLClient();
			httpGet = new HttpGet(url);
			// httpGet.addHeader("Content-Type", "text/plain;charset=utf-8");
			httpGet.addHeader("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			// httpGet.addHeader("Cache-Control", "no-cache");
			httpGet.addHeader("Connection", "close");
			httpGet.addHeader("Cache-Control", "max-age=0");
			httpGet.addHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
			HttpResponse response = httpClient.execute(httpGet);
			if (response != null) {
				HttpEntity resEntity = response.getEntity();
				System.out.println(resEntity.getContentLength());
				if (resEntity != null) {
					result = EntityUtils.toString(resEntity, charset);
					// httpClient.getConnectionManager().shutdown();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return result;
	}

	/**
	 * 
	 * @description 查询所有车次（12306）
	 * @param
	 * @return
	 */
	@SuppressWarnings("finally")
	public static JSONObject getQueryTrain(String train_date, String from_station, String to_station) {
		JSONObject result = new JSONObject();
		String postQueryUrl = queryUrl1 + queryUrl2 + "leftTicketDTO.train_date=" + train_date
				+ "&leftTicketDTO.from_station=" + from_station + "&leftTicketDTO.to_station=" + to_station
				+ "&purpose_codes=ADULT";
		String httpOrgCreateTestRtn = doGet(postQueryUrl, "utf-8");
		System.out.println(httpOrgCreateTestRtn);
		try {
		JsonObject obj = new JsonParser().parse(httpOrgCreateTestRtn).getAsJsonObject();
		JsonArray data = new JsonArray();
			if (obj.has("status")&&!obj.get("status").getAsBoolean()) {
				queryUrl2 = obj.get("c_url").getAsString() + "?";
				postQueryUrl = queryUrl1 + queryUrl2 + "leftTicketDTO.train_date=" + train_date
						+ "&leftTicketDTO.from_station=" + from_station + "&leftTicketDTO.to_station=" + to_station
						+ "&purpose_codes=ADULT";
				httpOrgCreateTestRtn = doGet(postQueryUrl, "utf-8");
				obj = new JsonParser().parse(httpOrgCreateTestRtn).getAsJsonObject();
			}
			if (obj.get("httpstatus").getAsString().equals("200")) {
				if (obj.has("data")) {
					JsonArray obj1 = obj.get("data").getAsJsonObject().get("result").getAsJsonArray();
					JsonObject obj2 = obj.get("data").getAsJsonObject().get("map").getAsJsonObject();
					for (int i = 0; i < obj1.size(); i++) {
						JsonObject ob = new JsonObject();
						String[] list = obj1.get(i).getAsString().split("\\|");
						if (list[3].contains("K") || list[3].contains("Z") || list[3].contains("T")
								|| list[3].contains("D")) {
							// System.out.println(obj1.get(i).getAsString());
							ob.addProperty("train_no", list[2]);
							ob.addProperty("station_train_code", list[3]);
							ob.addProperty("fromStationCode", list[6]);
							ob.addProperty("toStationCode", list[7]);
							ob.addProperty("startTime", list[8]);
							ob.addProperty("endTime", list[9]);
							String[] a = list[10].split(":");
							String hh = "";
							int h = Integer.valueOf(a[0]);
							if (h > 0) {
								hh = h + "时";
							}
							hh = hh + Integer.valueOf(a[1]) + "分";
							ob.addProperty("timeConsuming", hh);
							String startTime = list[8].replace(":", "");
							String timeConsuming = list[10].replace(":", "");
							int s1 = Integer.valueOf(startTime.substring(0, 2));
							int s2 = Integer.valueOf(startTime.substring(2, 4));
							int t1 = Integer.valueOf(timeConsuming.substring(0, 2));
							int t2 = Integer.valueOf(timeConsuming.substring(2, 4));
							int hours = s1 + t1;
							if (s2 + t2 >= 60) {
								hours = hours + 1;
							}
							if (hours < 24) {
								ob.addProperty("differentDay", "0");
							} else if (hours >= 24 && hours < 48) {
								ob.addProperty("differentDay", "1");
							} else if (hours >= 48 && hours < 72) {
								ob.addProperty("differentDay", "2");
							} else if (hours >= 72) {
								ob.addProperty("differentDay", "3");
							}
							if (obj2.has(list[6])) {
								ob.addProperty("fromStationName", obj2.get(list[6]).getAsString());
							}
							if (obj2.has(list[7])) {
								ob.addProperty("toStationName", obj2.get(list[7]).getAsString());
							}
							data.add(ob);
						}
					}
					result.put(Constant.code.CODE, Constant.code.CODE_1);
					result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_1);
					result.put("data", data.toString());
				} else {
					result.put(Constant.code.CODE, Constant.code.CODE_11);
					result.put(Constant.message.MESSAGE,
							obj.get("messages").getAsJsonArray().get(0).getAsString().replaceAll("''", ""));
				}
			}
		} catch (Exception e) {
//			queryUrl2 = obj.get("c_url").getAsString() + "?";
			e.printStackTrace();
			result.put(Constant.code.CODE, Constant.code.CODE_0);
			result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_0);
		} finally {
			return result;
		}
	}

	// train_no=240000K1171D&from_station_telecode=BXP&to_station_telecode=CDW&depart_date=2017-11-16
	/**
	 * 
	 * @description 查询某一车次的所有站点（12306）
	 * @param
	 * @return
	 */
	@SuppressWarnings("finally")
	public static JSONObject getQueryByTrainNoUrl(String train_no, String from_station_telecode,
			String to_station_telecode, String depart_date) {
		JSONObject result = new JSONObject();
		String postQueryByTrainNoUrl = queryByTrainNoUrl + "train_no=" + train_no + "&from_station_telecode="
				+ from_station_telecode + "&to_station_telecode=" + to_station_telecode + "&depart_date=" + depart_date;
		String httpOrgCreateTestRtn = doGet(postQueryByTrainNoUrl, "utf-8");
		JsonObject obj = new JsonParser().parse(httpOrgCreateTestRtn).getAsJsonObject();
		try {
			System.out.println(obj);
			if (obj.get("httpstatus").getAsString().equals("200")) {
				if (obj.has("data")) {
					JsonArray data = obj.get("data").getAsJsonObject().get("data").getAsJsonArray();
					result.put(Constant.code.CODE, Constant.code.CODE_1);
					result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_1);
					result.put("data", data.toString());
				} else {
					result.put(Constant.code.CODE, Constant.code.CODE_11);
					result.put(Constant.message.MESSAGE,
							obj.get("messages").getAsJsonArray().get(0).getAsString().replaceAll("''", ""));
				}
			}
		} catch (Exception e) {
			result.put(Constant.code.CODE, Constant.code.CODE_0);
			result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_0);
		} finally {
			return result;
		}
	}

	public static void main(String args[]) throws SQLException, Exception {
		// leftTicketDTO.train_date:2017-11-16
		// leftTicketDTO.from_station:BJP
		// leftTicketDTO.to_station:CDW
		// purpose_codes:ADULT
		JSONObject result = new JSONObject();
		// result = getQueryTrain("2018-01-26", "SHH", "BJP");
		// System.out.println(result);
		// train_no:
		// from_station_telecode:BXP
		// to_station_telecode:CDW
		// depart_date:2017-11-16
		// {"fromStationCode":"JNK","station_train_code":"K15","toStationCode":"CUW","trainDate":"2017-12-11","train_no":"4700000K1505","type":"1"}
		result = getQueryByTrainNoUrl("24000000D10W", "BJP", "SOT", "2018-01-22");
		System.out.println(result.toString());
		// String str = "D29(北京-齐齐哈尔南)";
		// String fromName = str.substring(str.indexOf("(") + 1,
		// str.indexOf("-"));
		// String toName = str.substring(str.indexOf("-") + 1,
		// str.indexOf(")"));
		// System.out.println(fromName);
		// System.out.println(toName);

	}
}
