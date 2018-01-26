package yl.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yl.Http.HttpDemo;
import com.yl.bean.Station;
import com.yl.bean.StationTrain;

/*
 * @author  lqpl66
 * @date 创建时间：2017年12月12日 上午10:05:13 
 * @function     
 */
public class T5 {
 public static void main(String[] args){
	 String jsonStr = "{xxx}";
		String url = "https://kyfw.12306.cn/otn/resources/js/query/train_list.js?scriptVersion=1.0";
		String httpOrgCreateTestRtn = HttpDemo.doGet(url, jsonStr, "utf-8");
		JsonObject obj = new JsonParser().parse(httpOrgCreateTestRtn.substring(16)).getAsJsonObject();
		Map<String, Object> map = new HashMap<String, Object>();
		for (Entry<String, JsonElement> pair : obj.entrySet()) {
			map.clear();
			map.put("trainDate", pair.getKey().toString());
//			List<StationTrain> list = trainMapper.getStationTrain(map);
			// 比对是否可入库
//			if (list.size() == 0) {
				JsonObject obj1 = new JsonParser().parse(pair.getValue().toString()).getAsJsonObject();
				for (Entry<String, JsonElement> pair1 : obj1.entrySet()) {
					JsonArray js = null;
					if (pair1.getValue().isJsonArray()) {
						if (pair1.getKey().contains("Z") || pair1.getKey().contains("T") || pair1.getKey().contains("K")
								|| pair1.getKey().contains("D")) {
							js = pair1.getValue().getAsJsonArray();
							for (int i = 0; i < js.size(); i++) {
								String station_train_code = js.get(i).getAsJsonObject().get("station_train_code")
										.getAsString();
								String train_no = js.get(i).getAsJsonObject().get("train_no").getAsString();
								String fromName = station_train_code.substring(station_train_code.indexOf("(") + 1,
										station_train_code.indexOf("-"));
								String toName = station_train_code.substring(station_train_code.indexOf("-") + 1,
										station_train_code.indexOf(")"));
								map.clear();
								String station_train_code1 = station_train_code.substring(0,
										station_train_code.indexOf("("));
								map.put("stationName", fromName);
								map.put("limit", 10);
//								List<Station> station1 = trainMapper.getStation(map);
								map.clear();
								map.put("stationName", toName);
								map.put("limit", 10);
//								List<Station> station2 = trainMapper.getStation(map);
								map.clear();
								map.put("station_train_code", station_train_code1);
								map.put("train_no", train_no);
								map.put("stationFlag", pair1.getKey());
								map.put("date", pair.getKey());
//								if (station1.size() > 0 && station2.size() > 0) {
//									map.put("fromStationName", station1.get(0).getStationName());
//									map.put("fromStationCode", station1.get(0).getStationCode());
//									map.put("toStationName", station2.get(0).getStationName());
//									map.put("toStationCode", station2.get(0).getStationCode());
//								}
//								trainMapper.saveTrain(map);
							}
						}
					}
				}
			}
		}
//	}
// }
}
