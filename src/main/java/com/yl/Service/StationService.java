package com.yl.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yl.Utils.CodeUtils;
import com.yl.Utils.CommonDateParseUtil;
import com.yl.Utils.Constant;
import com.yl.Utils.GetProperties;
import com.yl.bean.OrderBuyInfo;
import com.yl.bean.OrderBuyStation;
import com.yl.bean.OrderSellInfo;
import com.yl.bean.OrderSellStation;
import com.yl.bean.Station;
import com.yl.bean.StationTrain;
import com.yl.bean.User;
import com.yl.mapper.EmployeeMapper;
import com.yl.mapper.TrainMapper;
import com.yl.mapper.TrainOrderMapper;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 * @author Administrator
 *
 */
@Service
public class StationService {
	@Autowired
	private TrainMapper trainMapper;
	@Autowired
	private TrainOrderMapper trainOrderMapper;
	@Autowired
	private MessageService messageService;
	@Autowired
	private EmployeeMapper employeeMapper;

	/**
	 * 
	 * @description 操作求购信息
	 * @param type
	 *            1:保存入库 2：修改下架（用户自己操作）
	 * @return
	 */
	public JSONObject operateOrderBuy(JSONObject re, User user, String type) {
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject result = new JSONObject();
		if (type.equals("1")) {
			String fromStationCode = re.getString("fromStationCode");
			String toStationCode = re.getString("toStationCode");
			String trainDate = re.getString("trainDate");
			String train_no = re.getString("train_no");
			String stationTrainCode = re.getString("stationTrainCode");
			BigDecimal price = new BigDecimal(re.opt("price").toString());
			Station fromStation = getStation(fromStationCode, "2");
			Station toStation = getStation(toStationCode, "2");
			// map.put("train_no", train_no);
			// map.put("trainDate", trainDate);
			// map.put("limit", 10);
			// List<StationTrain> list = trainMapper.getStationTrain(map);
			JSONArray returnlist = re.getJSONArray("data");
			if (fromStation != null && toStation != null && returnlist.size() > 0) {// 入库
				// if (list.get(0).getStation_train_code().contains("Z")
				// || list.get(0).getStation_train_code().contains("T")
				// || list.get(0).getStation_train_code().contains("K")
				// || list.get(0).getStation_train_code().contains("D")) {
				String orderBuyNo = CodeUtils.getOrderBuy(user.getEmployeeCode());
				// 附属表（车站插入）isEnabled station_name
				List<OrderBuyStation> orderBuyStaionList = new ArrayList<OrderBuyStation>();
				int fromNum = 0;
				int toNum = 0;
				for (Object ob : returnlist) {
					JSONObject obj = JSONObject.fromObject(ob);
					if (obj.opt("station_name").toString().equals(fromStation.getStationName())) {
						fromNum = Integer.valueOf(obj.opt("station_no").toString());
					}
					if (obj.opt("station_name").toString().equals(toStation.getStationName())) {
						toNum = Integer.valueOf(obj.opt("station_no").toString());
					}
				}
				boolean flag = true;
				for (Object ob : returnlist) {
					JSONObject obj = JSONObject.fromObject(ob);
					int num = Integer.valueOf(obj.opt("station_no").toString());
					if (num >= fromNum && num <= toNum) {
						// if (obj.optBoolean("isEnabled")) {
						Station s = getStation(obj.opt("station_name").toString(), "1");
						OrderBuyStation or = new OrderBuyStation();
						or.setOrderBuyNo(orderBuyNo);
						or.setStationCode(s.getStationCode());
						or.setStationId(s.getId());
						or.setStationName(s.getStationName());
						orderBuyStaionList.add(or);
						// } else {
						// flag = false;
						// break;
						// }
					}
				}
				if (orderBuyStaionList.size() == 0) {
					flag = false;
				}
				if (!flag) {
					result.clear();
					result.put(Constant.code.CODE, Constant.code.CODE_24);
					result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_24);
					return result;
				}
				trainOrderMapper.insertOrderBuyStation(orderBuyStaionList);
				StationTrain s = new StationTrain();
				s.setId(0);
				s.setStation_train_code("");
				s.setTrain_no(train_no);
				s.setDate(trainDate);
				s.setStation_train_code(stationTrainCode);
				saveOrderBuy(orderBuyNo, fromStation, toStation, s, user, price);
				result.clear();
				result.put(Constant.code.CODE, Constant.code.CODE_1);
				result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_1);
				// }else {
				// result.clear();
				// result.put(Constant.code.CODE, Constant.code.CODE_20);
				// result.put(Constant.message.MESSAGE,
				// Constant.message.MESSAGE_20);
				// }
			} else {
				result.clear();
				result.put(Constant.code.CODE, Constant.code.CODE_17);
				result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_17);
			}
		} else if (type.equals("2")) {
			String orderBuyNo = re.optString("orderNo");
			map.clear();
			map.put("employeeId", user.getId());
			map.put("orderBuyNo", orderBuyNo);
			List<OrderBuyInfo> orderBuyInfo = trainOrderMapper.getOrderBuyInfo(map);
			if (orderBuyInfo.size() > 0 && orderBuyInfo.get(0).getIsClose() == 0) {
				map.clear();
				map.put("employeeId", user.getId());
				map.put("orderBuyNo", orderBuyNo);
				map.put("isClose", 1);
				trainOrderMapper.updateOrderBuy(map);
				result.clear();
				result.put(Constant.code.CODE, Constant.code.CODE_1);
				result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_1);
			} else {
				result.clear();
				result.put(Constant.code.CODE, Constant.code.CODE_18);
				result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_18);
			}
		} else {
			result.clear();
			result.put(Constant.code.CODE, Constant.code.CODE_11);
			result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_11);
		}
		return result;
	}

	/**
	 * 
	 * @description 操作座位信息
	 * @param type
	 *            1:保存入库 2：修改下架（用户自己操作）
	 * @return
	 */
	public JSONObject operateOrderSell(JSONObject re, User user, String type) {
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject result = new JSONObject();
		if (type.equals("1")) {
			String fromStationCode = re.getString("fromStationCode");
			String toStationCode = re.getString("toStationCode");
			String trainDate = re.getString("trainDate");
			String train_no = re.getString("train_no");
			BigDecimal price = new BigDecimal(re.opt("price").toString());
			String seatName = re.getString("seatName");
			Integer carriageNum = re.getInt("carriageNum");
			Integer isSplit = re.getInt("isSplit");
			Integer isAssign = re.getInt("isAssign");
			String assignAmount = re.getString("assignAmount");
			Station fromStation = getStation(fromStationCode, "2");
			Station toStation = getStation(toStationCode, "2");
			String stationTrainCode = re.getString("stationTrainCode");
			// map.put("train_no", train_no);
			// map.put("trainDate", trainDate);
			// map.put("limit", 10);
			// List<StationTrain> list = trainMapper.getStationTrain(map);
			JSONArray returnlist = re.getJSONArray("data");
			if (fromStation != null && toStation != null && returnlist.size() > 0) {// 入库
				// if (list.get(0).getStation_train_code().contains("Z")
				// || list.get(0).getStation_train_code().contains("T")
				// || list.get(0).getStation_train_code().contains("K")
				// || list.get(0).getStation_train_code().contains("D")) {
				map.clear();
				map.put("employeeId", user.getId());
				map.put("train_no", train_no);
				map.put("trainDate", trainDate);
				map.put("seatName", seatName);
				map.put("carriageNum", carriageNum);
				String liststr = "";
				// 附属表isEnabled station_name
				for (int i = 0; i < returnlist.size() - 1; i++) {
					JSONObject obj11 = JSONObject.fromObject(returnlist.get(i));
					JSONObject obj22 = JSONObject.fromObject(returnlist.get(i + 1));
					// if (obj11.optBoolean("isEnabled") &&
					// obj22.optBoolean("isEnabled")) {
					liststr = liststr + obj11.opt("station_name").toString() + "-"
							+ obj22.opt("station_name").toString() + ",";
					// }
				}
				liststr = liststr.substring(0, liststr.length() - 1);
				map.put("stationName", liststr);
				// 上架的不允许重叠，下架的只允许未被购买的
				map.put("isClose", 0);
				List<OrderSellInfo> d1 = trainOrderMapper.checkOrderSell(map);
				map.put("isClose", 1);
				map.put("isSell", 1);
				List<OrderSellInfo> d2 = trainOrderMapper.checkOrderSell(map);
				if (d1.size() > 0 || d2.size() > 0) {
					result.clear();
					result.put(Constant.code.CODE, Constant.code.CODE_27);
					result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_27);
					return result;
				}
				String orderSellNo = CodeUtils.getOrderBuy(user.getEmployeeCode());
				StationTrain s = new StationTrain();
				s.setId(0);
				s.setStation_train_code("");
				s.setTrain_no(train_no);
				s.setDate(trainDate);
				s.setStation_train_code(stationTrainCode);
				saveOrderSell(orderSellNo, fromStation, toStation, s, user, price, seatName, carriageNum, isSplit,
						isAssign, assignAmount);
				// 附属表（车站插入）isEnabled station_name
				List<OrderSellStation> orderSellStaionList = new ArrayList<OrderSellStation>();
				int j = 0, k = 0;
				for (int i = 0; i < returnlist.size(); i++) {
					JSONObject obj1 = JSONObject.fromObject(returnlist.get(i));
					if (obj1.opt("station_name").toString().equals(fromStation.getStationName())) {
						j = i;
					}
					if (obj1.opt("station_name").toString().equals(toStation.getStationName())) {
						k = i;
					}
				}
				for (int i = j; i < k; i++) {
					JSONObject obj1 = JSONObject.fromObject(returnlist.get(i));
					JSONObject obj2 = JSONObject.fromObject(returnlist.get(i + 1));
					// if (obj1.optBoolean("isEnabled") &&
					// obj2.optBoolean("isEnabled")) {
					Station fs = getStation(obj1.opt("station_name").toString(), "1");
					Station ts = getStation(obj2.opt("station_name").toString(), "1");
					OrderSellStation or = new OrderSellStation();
					or.setOrderSellNo(orderSellNo);
					or.setArriveTime(obj2.opt("arrive_time").toString());
					or.setStartTime(obj1.opt("start_time").toString());
					or.setFromStationCode(fs.getStationCode());
					or.setFromStationId(fs.getId());
					or.setFromStationName(fs.getStationName());
					or.setToStationCode(ts.getStationCode());
					or.setToStationId(ts.getId());
					or.setToStationName(ts.getStationName());
					or.setIsSell(0);
					or.setDiffrentDay("-1");
					orderSellStaionList.add(or);
					// }
				}
				trainOrderMapper.insertOrderSellStation(orderSellStaionList);
				try {// 消息推送
					if (isAssign == 0) {
						JSONObject js = new JSONObject();
						js.put("trainNo", s.getStation_train_code());
						js.put("site", fromStation.getStationName() + "-" + toStation.getStationName());
						js.put("dispatchTime", trainDate);
						js.put("orderSellNo", orderSellNo);
						map.put("employeeCode", assignAmount);
						List<Map<String, Object>> u = employeeMapper.selectEmplyeeInfo(map);
						js.put("toUserId", u.get(0).get("id"));
						js.put("modelType", "ST000");
						js.put("messageType", 0);
						js.put("uuid", user.getUuid());
						messageService.saveMessage(js, js);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				result.clear();
				result.put(Constant.code.CODE, Constant.code.CODE_1);
				result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_1);
				// } else {
				// result.clear();
				// result.put(Constant.code.CODE, Constant.code.CODE_20);
				// result.put(Constant.message.MESSAGE,
				// Constant.message.MESSAGE_20);
				// }
			} else {
				result.clear();
				result.put(Constant.code.CODE, Constant.code.CODE_24);
				result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_24);
			}
		} else if (type.equals("2")) {
			String orderSellNo = re.optString("orderNo");
			map.clear();
			map.put("employeeId", user.getId());
			map.put("orderSellNo", orderSellNo);
			map.put("isClose", 0);
			List<OrderSellInfo> orderSellInfo = trainOrderMapper.getOrderSellInfo(map);
			if (orderSellInfo.size() > 0 && orderSellInfo.get(0).getIsClose() == 0) {
				map.clear();
				map.put("employeeId", user.getId());
				map.put("orderSellNo", orderSellNo);
				map.put("isClose1", 1);
				trainOrderMapper.updateOrderSell(map);
				result.clear();
				result.put(Constant.code.CODE, Constant.code.CODE_1);
				result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_1);
			} else {
				result.clear();
				result.put(Constant.code.CODE, Constant.code.CODE_44);
				result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_44);
			}
		} else {
			result.clear();
			result.put(Constant.code.CODE, Constant.code.CODE_11);
			result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_11);
		}
		return result;
	}

	/**
	 * 
	 * @description
	 * @param type
	 *            1:根据车站名称查询 2：根据车站标识查询
	 * @return
	 */
	public Station getStation(String station, String type) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (type.equals("1")) {
			map.put("stationName", station);
		} else if (type.equals("2")) {
			map.put("stationCode", station);
		}
		List<Station> list = trainMapper.getStation(map);
		return list.get(0);
	}

	/**
	 * 
	 * @description
	 * @param
	 * @return
	 */
	public void saveOrderBuy(String orderBuyNo, Station f, Station t, StationTrain s, User u, BigDecimal price) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("employeeId", u.getId());
		map.put("orderBuyNo", orderBuyNo);
		map.put("trainId", s.getId());
		map.put("trainCode", s.getStation_train_code());
		map.put("trainNo", s.getTrain_no());
		map.put("trainDate", s.getDate());
		map.put("startStationId", f.getId());
		map.put("startStationName", f.getStationName());
		map.put("startStationCode", f.getStationCode());
		map.put("endStationId", t.getId());
		map.put("endStationName", t.getStationName());
		map.put("endStationCode", t.getStationCode());
		map.put("price", price);
		trainOrderMapper.saveOrderBuy(map);
	}

	/**
	 * 
	 * @description
	 * @param
	 * @return
	 */

	public void saveOrderSell(String orderSellNo, Station f, Station t, StationTrain s, User u, BigDecimal price,
			String seatName, Integer carriageNum, Integer isSplit, Integer isAssign, String assignAmount) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("employeeId", u.getId());
		map.put("orderSellNo", orderSellNo);
		map.put("trainId", s.getId());
		map.put("trainCode", s.getStation_train_code());
		map.put("trainNo", s.getTrain_no());
		map.put("trainDate", s.getDate());
		map.put("startStationId", f.getId());
		map.put("startStationName", f.getStationName());
		map.put("startStationCode", f.getStationCode());
		map.put("endStationId", t.getId());
		map.put("endStationName", t.getStationName());
		map.put("endStationCode", t.getStationCode());
		map.put("price", price);
		map.put("seatName", seatName);
		map.put("carriageNum", carriageNum);
		map.put("isSplit", isSplit);
		map.put("isAssign", isAssign);
		map.put("assignAmount", assignAmount);
		Calendar time = Calendar.getInstance();
		if (isAssign == 0) {
			time.add(Calendar.MINUTE, 15);
		}
		map.put("loseTime", CommonDateParseUtil.date2string(time.getTime()));
		trainOrderMapper.saveOrderSell(map);
	}

	/**
	 * 
	 * @description 获取求购信息列表
	 * @param sortType
	 *            1:按时间倒序；2：符合车站数倒序；3：价格倒序 ;4:按时间正序;5:符合车站数正序;6:价格正序
	 * @return
	 */
	public JSONObject getSeektoPurchase(Integer num, Integer pageNum, JSONObject re, String sortType, String train_no,
			String trainDate, String fromStationCode, String toStationCode) {
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject result = new JSONObject();
		JSONArray returnlist = re.getJSONArray("data");
		Station fromStation = getStation(fromStationCode, "2");
		Station toStation = getStation(toStationCode, "2");
		String list = "";
		// 附属表isEnabled station_name
		int fromNum = 0;
		int toNum = 0;
		for (int i = 0; i < returnlist.size(); i++) {
			JSONObject obj = JSONObject.fromObject(returnlist.get(i));
			if (obj.opt("station_name").toString().equals(fromStation.getStationName())) {
				fromNum = i;
			}
			if (obj.opt("station_name").toString().equals(toStation.getStationName())) {
				toNum = i;
			}
		}
		for (int i = fromNum; i <=toNum; i++) {
			JSONObject obj = JSONObject.fromObject(returnlist.get(i));
			list = list + obj.opt("station_name").toString() + ",";
		}
		list = list.substring(0, list.length() - 1);
		map.clear();
		Integer start = (pageNum - 1) * num;
		map.put("start", start);
		map.put("num", num);
		map.put("stationName", list);
		map.put("type", sortType);
		map.put("train_no", train_no);
		map.put("trainDate", trainDate);
		List<OrderBuyInfo> list1 = trainOrderMapper.getOrderBuyForSearch(map);
		if (list1.size() > 0) {
			for (OrderBuyInfo ob : list1) {
				String url = GetProperties.getFileUrl("pGZUserUrl");
				ob.setHeadImg(url + ob.getEmployeeCode() + "/" + ob.getHeadImg());
				ob.setTrainNum(ob.getTrainNum()-1);
				ob.setNum(ob.getNum()-1);
			}
		}
		result.put("data", list1);
		result.put(Constant.code.CODE, Constant.code.CODE_1);
		result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_1);
		return result;
	}

	/**
	 * 
	 * @description 获取座位信息列表
	 * @param sortType
	 *            0：综合排序 1:按车厢倒序；2：符合车站数倒序；3：价格倒序 ;4:按车厢正序;5:符合车站数正序;6:价格正序
	 * @return
	 */
	public JSONObject getSeat(Integer num, Integer pageNum, JSONObject re, String sortType, String train_no,
			String trainDate, String fromStationCode, String toStationCode) {
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject result = new JSONObject();
		JSONArray returnlist = re.getJSONArray("data");
		String list = "";
		Station fromStation = getStation(fromStationCode, "2");
		Station toStation = getStation(toStationCode, "2");
		// 附属表isEnabled station_name
		int j = 0, k = 0;
		for (int i = 0; i < returnlist.size(); i++) {
			JSONObject obj1 = JSONObject.fromObject(returnlist.get(i));
			if (obj1.opt("station_name").toString().equals(fromStation.getStationName())) {
				j = i;
			}
			if (obj1.opt("station_name").toString().equals(toStation.getStationName())) {
				k = i;
			}
		}
		for (int i = j; i < k; i++) {
			JSONObject obj1 = JSONObject.fromObject(returnlist.get(i));
			JSONObject obj2 = JSONObject.fromObject(returnlist.get(i + 1));
			// if (obj1.optBoolean("isEnabled") && obj2.optBoolean("isEnabled"))
			// {
			list = list + obj1.opt("station_name").toString() + "-" + obj2.opt("station_name").toString() + ",";
			// }
		}
		list = list.substring(0, list.length() - 1);
		map.clear();
		Integer start = (pageNum - 1) * num;
		map.put("start", start);
		map.put("num", num);
		map.put("stationName", list);
		map.put("type", sortType);
		map.put("train_no", train_no);
		map.put("trainDate", trainDate);
		List<OrderSellInfo> list1 = trainOrderMapper.getOrderSellForSearch(map);
		// for (OrderSellInfo os : list1) {
		// os.setTrainCode(os.getTrainCode().substring(0,
		// os.getTrainCode().indexOf("(")));
		// map.clear();
		// map.put("orderSellNo", os.getOrderSellNo());
		// List<OrderSellStation> list2 =
		// trainOrderMapper.getOrderSellStation(map);
		// os.setList(list2);
		// }
		JSONObject data = new JSONObject();
		data.put("list", list1);
		data.put("num", k - j);
		result.put("data", data);
		result.put(Constant.code.CODE, Constant.code.CODE_1);
		result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_1);
		return result;
	}
}
