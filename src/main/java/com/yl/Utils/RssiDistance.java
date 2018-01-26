package com.yl.Utils;
/*
 * @author  lqpl66
 * @date 创建时间：2017年9月28日 下午4:22:34 
 * @function 
 */

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.yl.bean.Ibeacon;
import com.yl.bean.KalmanFiltering;

import net.sf.json.JSONArray;
import redis.clients.jedis.Jedis;

public class RssiDistance {
	// 根据强弱计算距离
	public static float getRssiDistance(int rssi) {
		float distantce = 0;
		int iRssi = Math.abs(rssi);
		distantce = (float) Math.pow(10, (float) ((iRssi - 59) / (10 * 2.0)));

//	      int ratio = rssi /-59;
//	      if (ratio < 1) {
//	    	  distantce =  (float) Math.pow(ratio, 10);
//	      } else {
//	    	  distantce = (float) ((0.89976) * Math.pow(ratio, 7.7095) + 0.111);
//	      }
		return distantce;
	}

	// 赋值当前坐标值
	public static Ibeacon getIbeacon(Ibeacon ib) {
		switch (ib.getMac()) {
		case "C41E2E06E585":
			ib.setX(0);
			ib.setY((float) 8.5);
			break;
		case "FC3F584B3819":
			ib.setX((float) 7.2);
			ib.setY((float) 8.5);
			break;
		case "D3128BCACD8C":
			ib.setX((float) 7.2);
			ib.setY(0);
			break;
		case "E010179FF81D":
			ib.setX(0);
			ib.setY(0);
			break;
		}
		return ib;
	}

	/**
	 * 
	 * @description 计算xy
	 * @param 距离和蓝牙基站的坐标数组
	 * @return
	 */
	public static JSONObject getXY(List<Ibeacon> list) {
		JSONObject js = new JSONObject();
		float x = 0;
		float y = 0;
		float w0 = 0;
		float w1 = 0;
		float w2 = 0;
		float w3 = 0;
		if (!list.isEmpty() && list.size() > 0) {
			getSort(list);
			int mode = getMode(list);
			switch (mode) {
			case 1:
				x = list.get(0).getX();
				y = list.get(0).getY();
				break;
			case 2:
				w0 = list.get(1).getD() / (list.get(1).getD() + list.get(0).getD());
				w1 = list.get(0).getD() / (list.get(1).getD() + list.get(0).getD());
				x = list.get(0).getX() * w0 + list.get(1).getX() * w1;
				y = list.get(0).getY() * w0 + list.get(1).getY() * w1;
				break;
			case 3:
				w0 = (1 / list.get(0).getD())
						/ (1 / list.get(0).getD() + 1 / list.get(1).getD() + 1 / list.get(2).getD());
				w1 = (1 / list.get(1).getD())
						/ (1 / list.get(0).getD() + 1 / list.get(1).getD() + 1 / list.get(2).getD());
				w2 = (1 / list.get(2).getD())
						/ (1 / list.get(0).getD() + 1 / list.get(1).getD() + 1 / list.get(2).getD());
				x = list.get(0).getX() * w0 + list.get(1).getX() * w1 + list.get(2).getX() * w2;
				y = list.get(0).getY() * w0 + list.get(1).getY() * w1 + list.get(2).getY() * w2;
				break;
			case 4:
				w0 = (1 / list.get(0).getD()) / (1 / list.get(0).getD() + 1 / list.get(1).getD()
						+ 1 / list.get(2).getD() + 1 / list.get(3).getD());
				w1 = (1 / list.get(1).getD()) / (1 / list.get(0).getD() + 1 / list.get(1).getD()
						+ 1 / list.get(2).getD() + 1 / list.get(3).getD());
				w2 = (1 / list.get(2).getD()) / (1 / list.get(0).getD() + 1 / list.get(1).getD()
						+ 1 / list.get(2).getD() + 1 / list.get(3).getD());
				w3 = (1 / list.get(3).getD()) / (1 / list.get(0).getD() + 1 / list.get(1).getD()
						+ 1 / list.get(2).getD() + 1 / list.get(3).getD());
				x = list.get(0).getX() * w0 + list.get(1).getX() * w1 + list.get(2).getX() * w2
						+ list.get(3).getX() * w3;
				y = list.get(0).getY() * w0 + list.get(1).getY() * w1 + list.get(2).getY() * w2
						+ list.get(3).getX() * w3;
				break;
			}
		}
		// 卡尔曼滤波
		JSONObject js1 = getKalmanFiltering(x, y, list.get(0).getMu_mac());
		if (js1.isEmpty()) {
			js.put("x", x);
			js.put("y", y);
		} else {
			js.put("x", js1.get("x"));
			js.put("y", js1.get("y"));
		}
//		js.put("x", x);
//		js.put("y", y);
		js.put("postAddTime",  list.get(0).getAddTime());
		return js;
	}

	/**
	 * 
	 * @description 卡尔曼滤波，减少误差
	 * @param 观测的坐标，腕带的MAC地址
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "deprecation" })
	public static JSONObject getKalmanFiltering(float x, float y, String mac) {
		JSONObject js = new JSONObject();
		Jedis jedis = new Jedis("127.0.0.1", 6379);
		List<String> listString = jedis.lrange(mac, 0, -1);
		JSONArray ls = JSONArray.fromObject(listString);
		List<KalmanFiltering> list = JSONArray.toList((JSONArray) ls, KalmanFiltering.class);
		if (list.isEmpty()) {// 首次定位
			KalmanFiltering kf = new KalmanFiltering();
			kf.setForecast_covar(10);
			kf.setKalman_gain((float) 0.5);
			kf.setLast_covar(10);
			kf.setProcess_noise_covar((float) 0.00001);
			kf.setMeasure_noise_covar((float) 0.1);
			kf.setLast_position_x(x);
			kf.setLast_position_y(y);
			if (kf.getKalman_gain() >= 0 && kf.getKalman_gain() < 1) {
				kf.setLast_covar((1 - kf.getKalman_gain() * kf.getForecast_covar()));
			}
			String a = JSONObject.toJSONString(kf);
			jedis.lpush(mac, a);
			System.out.println("首："+a);
		} else {
			KalmanFiltering kf1 = list.get(0);
			kf1.setProcess_noise_covar((float) 0.00001);
			kf1.setMeasure_noise_covar((float) 0.1);
			/* 依据上一个定位点预测当前时刻定位点 */
			// 设dist为观测的定位点observe_position与上一个定位点last_position的欧式距离
			float dist = (float) Math
					.sqrt(Math.pow(x-kf1.getLast_position_x(),2 ) + Math.pow(y-kf1.getLast_position_y(), 2));
			// 设dist_thresh为距离阈值，默认值为2.3
			kf1.setDist_thresh((float) 2.3);
			// if dist<dist_thresh
			// then forecast_position<-last_position_
			// else {
			// forecast_position_x<-last_position_x+(dist_thresh/dist)*(observe_position_x-last_position_x)
			// forecast_position_y<-last_position_y+(dist_thresh/dist)*(observe_position_y-last_position_y)
			// }
			float forecast_position_x = 0;
			float forecast_position_y = 0;
			if (dist < kf1.getDist_thresh()) {
				forecast_position_x = kf1.getLast_position_x();
				forecast_position_y = kf1.getLast_position_y();
			} else {
				forecast_position_x = kf1.getLast_position_x()
						+ (kf1.getDist_thresh() / dist) * (x - kf1.getLast_position_x());
				forecast_position_y = kf1.getLast_position_y()
						+ (kf1.getDist_thresh() / dist) * (y - kf1.getLast_position_y());
			}

			/* 更新forecast_covar_ */
			kf1.setForecast_covar(kf1.getLast_covar() + kf1.getProcess_noise_covar());
			/* 更新kalman_gain_ */
			kf1.setKalman_gain(kf1.getForecast_covar() / (kf1.getForecast_covar() + kf1.getMeasure_noise_covar()));
			if (kf1.getKalman_gain() < 0.3) {
				kf1.setKalman_gain((float) 0.5);
			}
			/* 依据观测值与预测值估计最优值 */
			float filtered_position_x = 0;
			float filtered_position_y = 0;
			filtered_position_x = forecast_position_x + kf1.getKalman_gain() * (x - forecast_position_x);
			filtered_position_y = forecast_position_y + kf1.getKalman_gain() * (y - forecast_position_y);
			
			/* 更新last_position_ */
			kf1.setLast_position_x(filtered_position_x);
			kf1.setLast_position_y(filtered_position_y);
			/* 更新last_covar_ */
			if (kf1.getKalman_gain() >= 0 && kf1.getKalman_gain() > 1) {
				kf1.setLast_covar((1 - kf1.getKalman_gain() * kf1.getForecast_covar()));
			}
			System.out.println("k:"+kf1.getKalman_gain());
			kf1.setLast_position_x(forecast_position_x);
			kf1.setLast_position_y(forecast_position_y);
			js.put("x", forecast_position_x);
			js.put("y", forecast_position_y);
			String a = JSONObject.toJSONString(kf1);
			jedis.del(mac, a);
			jedis.lpush(mac, a);
			System.out.println("多："+a);
		}
	
		return js;
	}

	/**
	 * 
	 * @description
	 * @param
	 * @return 获取定位模式
	 */
	public static int getMode(List<Ibeacon> list) {
		int mode = 1;
		if (list.size() == 1) {
			mode = 1;
		} else if (list.size() == 2) {
			mode = 2;
		} else if (list.size() == 3) {
			mode = 3;
		} else {
			if (list.get(0).getD() / list.get(1).getD() <= 0.33) {
				mode = 3;
			} else {
				mode = 4;
			}
		}
		return mode;
	}

	/**
	 * 
	 * @description
	 * @param
	 * @return 距离的排序
	 */
	public static List<Ibeacon> getSort(List<Ibeacon> list) {
		List<Ibeacon> list1 = new ArrayList<Ibeacon>();
		if (list != null && list.size() > 0) {
			if (list.size() > 4) {
				for (int i = 0; i < list.size(); i++) {
					boolean flag = true;
					for (int j = i; j < list.size() - 1; j++) {
						if (list.get(j).getD() > list.get(j + 1).getD()) {
							list1.add(0, list.get(j + 1));
							list.set(j + 1, list.get(j));
							list.set(j, list1.get(0));
							flag = false;
						}
						if (flag) {
							break;
						}
					}
				}
			}
		}
		return list;
	}

	/**
	 * 
	 * @description
	 * @param
	 * @return 时间的排序
	 * @throws ParseException
	 */
	public static List<Ibeacon> getTimeSort(List<Ibeacon> list) throws ParseException {
		List<Ibeacon> list1 = new ArrayList<Ibeacon>();
		if (list != null && list.size() > 0) {
			if (list.size() > 4) {
				for (int i = 0; i < list.size(); i++) {
					boolean flag = true;
					for (int j = i; j < list.size() - 1; j++) {
						DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						if (df.parse(list.get(j).getAddTime()).getTime() > df.parse(list.get(j + 1).getAddTime())
								.getTime()) {
							list1.add(0, list.get(j + 1));
							list.set(j + 1, list.get(j));
							list.set(j, list1.get(0));
							flag = false;
						}
						if (flag) {
							break;
						}
					}
				}
			}
		}
		return list;
	}

	/**
	 * 
	 * @description 获取腕带附近基站的数据
	 * @param 腕带的mac地址
	 * @return
	 */
	@SuppressWarnings({ "deprecation", "unchecked", "resource" })
	public static List<Ibeacon> getList(String mu_mac) throws ParseException {
		Jedis jedis = new Jedis("127.0.0.1", 6379);
		List<Ibeacon> list = new ArrayList<Ibeacon>();
		List<Ibeacon> list1 = new ArrayList<Ibeacon>();// 所有的数据
		List<Ibeacon> list2 = new ArrayList<Ibeacon>();// 某个腕带的所有的数据
		List<Ibeacon> list3 = new ArrayList<Ibeacon>();
		List<Ibeacon> list4 = new ArrayList<Ibeacon>();
		List<Ibeacon> list5 = new ArrayList<Ibeacon>();
		List<Ibeacon> list6 = new ArrayList<Ibeacon>();
		List<String> listString = jedis.lrange("macData", 0, -1);
		JSONArray ls = JSONArray.fromObject(listString);
		list1 = JSONArray.toList((JSONArray) ls, Ibeacon.class);
		for (Ibeacon ib : list1) {
			if (ib.getMu_mac().equals(mu_mac)) {// 获取某一个腕带的信息
				list2.add(ib);
			}
		}
		for (Ibeacon ib : list2) {// 根据蓝牙基站不同分组
			switch (ib.getMac()) {
			case "C41E2E06E585":
				list3.add(ib);
				break;
			case "FC3F584B3819":
				list4.add(ib);
				break;
			case "D3128BCACD8C":
				list5.add(ib);
				break;
			case "E010179FF81D":
				list6.add(ib);
				break;
			}
		}
		getTimeSort(list3);
		getTimeSort(list4);
		getTimeSort(list5);
		getTimeSort(list6);
		if (!list3.isEmpty()) {
			list.add(list3.get(0));
		}
		if (!list4.isEmpty()) {
			list.add(list4.get(0));
		}
		if (!list5.isEmpty()) {
			list.add(list5.get(0));
		}
		if (!list6.isEmpty()) {
			list.add(list6.get(0));
		}
		return list;
	}

	public static void main(String args[]) throws ParseException {
		JSONObject js = getXY(getList("FF7E0FD0D7D7"));
		System.out.println("js:" + js.toString());
	}
}
