package com.yl.Controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.yl.Http.HttpGetStationTrain;
import com.yl.Service.StationOrderService;
import com.yl.Service.StationService;
import com.yl.Service.SystemService;
import com.yl.Utils.CommonDateParseUtil;
import com.yl.Utils.Constant;
import com.yl.Utils.GetProperties;
import com.yl.bean.Order;
import com.yl.bean.OrderBuyInfo;
import com.yl.bean.OrderSellInfo;
import com.yl.bean.OrderSellStation;
import com.yl.bean.OrderStation;
import com.yl.bean.Station;
import com.yl.bean.StationTrain;
import com.yl.bean.User;
import com.yl.mapper.EmployeeMapper;
import com.yl.mapper.TrainMapper;
import com.yl.mapper.TrainOrderMapper;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/train")
public class StationController {
	private Logger log = Logger.getLogger(StationController.class);
	@Autowired
	private TrainMapper trainMapper;
	@Autowired
	private SystemService systemService;
	@Autowired
	private StationService stationService;
	@Autowired
	private TrainOrderMapper trainOrderMapper;
	@Autowired
	private EmployeeMapper employeeMapper;
	@Autowired
	private StationOrderService stationOrderService;

	private static String url = GetProperties.getFileUrl("pGZUserUrl");

	/**
	 * 
	 * @description 获取起始城市的所有车次
	 * @param
	 * @return
	 */
	@SuppressWarnings("finally")
	@ResponseBody
	@RequestMapping(value = "/getTrainList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public String getTrainList(HttpServletRequest request, @RequestBody String jsonparam) {
		JSONObject result = new JSONObject();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			JSONObject json = JSONObject.fromObject(jsonparam);
			String fromStationCode = json.getString("fromStationCode");
			String toStationCode = json.getString("toStationCode");
			String trainDate = json.getString("trainDate");
			if (fromStationCode != null && !fromStationCode.isEmpty() && toStationCode != null
					&& !toStationCode.isEmpty() && trainDate != null && !trainDate.isEmpty()) {
				System.out.println("1:" + CommonDateParseUtil.date2string(new Date()));
				// do {
				// result = HttpGetStationTrain.getQueryTrain(trainDate,
				// fromStationCode, toStationCode);
				// } while (!);
				for (int i = 0; i < 5; i++) {
					result = HttpGetStationTrain.getQueryTrain(trainDate, fromStationCode, toStationCode);
					if (result.get("code").equals("0001")) {
						break;
					}
				}
			} else {
				result.put(Constant.code.CODE, Constant.code.CODE_2);
				result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_2);
			}
		} catch (Exception e) {
			log.error("获取起始城市的所有车次：", e);
			result.put(Constant.code.CODE, Constant.code.CODE_0);
			result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_0);
		} finally {
			return result.toString();
		}
	}

	/**
	 * 
	 * @description 查询某一车次的所有站点（12306）
	 * @param
	 * @return
	 */
	@SuppressWarnings("finally")
	@ResponseBody
	@RequestMapping(value = "/getQueryByTrainNo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public String getQueryByTrainNo(HttpServletRequest request, @RequestBody String jsonparam) {
		JSONObject result = new JSONObject();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			JSONObject json = JSONObject.fromObject(jsonparam);
			String type = json.getString("type");
			if (!type.isEmpty() && type != null) {
				if (type.equals("1")) {
					String fromStationCode = json.getString("fromStationCode");
					String toStationCode = json.getString("toStationCode");
					String trainDate = json.getString("trainDate");
					String train_no = json.getString("train_no");
					if (fromStationCode != null && !fromStationCode.isEmpty() && toStationCode != null
							&& !toStationCode.isEmpty() && trainDate != null && !trainDate.isEmpty() && train_no != null
							&& !train_no.isEmpty()) {
						result = HttpGetStationTrain.getQueryByTrainNoUrl(train_no, toStationCode, toStationCode,
								trainDate);
					} else {
						result.put(Constant.code.CODE, Constant.code.CODE_2);
						result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_2);
					}
				} else if (type.equals("2")) {
					String trainDate = json.getString("trainDate");
					String stationTrainCode = json.getString("stationTrainCode");
					String fromStationName = json.getString("fromStationName");
					String toStationName = json.getString("toStationName");
					if (stationTrainCode != null && !stationTrainCode.isEmpty() && fromStationName != null
							&& !fromStationName.isEmpty() && trainDate != null && !trainDate.isEmpty()
							&& toStationName != null && !toStationName.isEmpty()) {
						map.put("station_train_code", stationTrainCode);
						map.put("trainDate", trainDate);
						map.put("fromStationName", fromStationName);
						map.put("toStationName", toStationName);
						List<StationTrain> stationTrainlist = trainMapper.getStationTrain(map);
						result = HttpGetStationTrain.getQueryByTrainNoUrl(stationTrainlist.get(0).getTrain_no(),
								stationTrainlist.get(0).getToStationCode(), stationTrainlist.get(0).getToStationCode(),
								trainDate);
					} else {
						result.put(Constant.code.CODE, Constant.code.CODE_2);
						result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_2);
					}
				} else {
					result.put(Constant.code.CODE, Constant.code.CODE_11);
					result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_11);
				}
			} else {
				result.put(Constant.code.CODE, Constant.code.CODE_2);
				result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_2);
			}
		} catch (Exception e) {
			log.error("查询某一车次的所有站点：", e);
			result.put(Constant.code.CODE, Constant.code.CODE_0);
			result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_0);
		} finally {
			return result.toString();
		}
	}

	/**
	 * 
	 * @description 模糊查询起始城市或者车次
	 * @param type
	 *            1 起始城市查询 ；2 车次; 3:
	 * @return
	 */
	@SuppressWarnings("finally")
	@ResponseBody
	@RequestMapping(value = "/getFuzzyQueryTrain", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public String getFuzzyQueryTrain(HttpServletRequest request, @RequestBody String jsonparam) {
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject result = new JSONObject();
		try {
			JSONObject json = JSONObject.fromObject(jsonparam);
			String type = json.getString("type");
			if (type != null && !type.isEmpty()) {
				if (type.equals("1")) {// 车站城市
					String nameStr = json.getString("nameStr");
					if (nameStr != null && !nameStr.isEmpty()) {
						map.clear();
						map.put("nameStr", nameStr);
						map.put("limit", 10);
						List<Station> list = trainMapper.getStation(map);
						result.put(Constant.code.CODE, Constant.code.CODE_1);
						result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_1);
						result.put("data", list);
					} else {
						result.put(Constant.code.CODE, Constant.code.CODE_2);
						result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_2);
					}
				} else if (type.equals("2")) {// 车次
					String trainDate = json.getString("trainDate");
					String station_train_code = json.getString("station_train_code");
					if (trainDate != null && !trainDate.isEmpty() && station_train_code != null
							&& !station_train_code.isEmpty()) {
						map.clear();
						map.put("station_train_code", station_train_code);
						map.put("trainDate", trainDate);
						map.put("limit", 10);
						List<StationTrain> list = trainMapper.getStationTrain(map);
						result.put(Constant.code.CODE, Constant.code.CODE_1);
						result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_1);
						result.put("data", list);
					} else {
						result.put(Constant.code.CODE, Constant.code.CODE_2);
						result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_2);
					}
				} else {
					result.put(Constant.code.CODE, Constant.code.CODE_11);
					result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_11);
				}
			} else {
				result.put(Constant.code.CODE, Constant.code.CODE_2);
				result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_2);
			}
		} catch (Exception e) {
			log.error("模糊查询起始城市或者车次：", e);
			result.put(Constant.code.CODE, Constant.code.CODE_0);
			result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_0);
		} finally {
			return result.toString();
		}
	}

	/*
	 * 
	 */
	@RequestMapping(value = "/main", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = "application/json;charset=UTF-8")
	public void get(HttpServletRequest request, HttpServletResponse response) {
		try {
			// Map<String, Object> map = new HashMap<String, Object>();
			// List<StationTrain> list = trainMapper.getStationTrain(map);
			// if (list.size() > 0) {
			// for (StationTrain st : list) {
			// String str = st.getStation_train_code();
			// String fromName = str.substring(str.indexOf("(") + 1,
			// str.indexOf("-"));
			// String toName = str.substring(str.indexOf("-") + 1,
			// str.indexOf(")"));
			// str = str.substring(0, str.indexOf("("));
			// map.clear();
			// map.put("stationName", fromName);
			// map.put("limit", 10);
			// List<Station> station1 = trainMapper.getStation(map);
			// map.clear();
			// map.put("stationName", toName);
			// map.put("limit", 10);
			// List<Station> station2 = trainMapper.getStation(map);
			// if (station1.size() > 0 && station2.size() > 0) {
			// map.clear();
			// map.put("id", st.getId());
			// map.put("fromStationName", station1.get(0).getStationName());
			// map.put("fromStationCode", station1.get(0).getStationCode());
			// map.put("toStationName", station2.get(0).getStationName());
			// map.put("toStationCode", station2.get(0).getStationCode());
			// map.put("station_train_code", str);
			// trainMapper.UpdateTrainlist(map);
			// }
			// }
			// }

			stationOrderService.orderClose();
		} catch (Exception e) {
			System.out.println("异常");
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @description 搜索求购信息
	 * @param type
	 * 
	 * @return
	 */
	@SuppressWarnings("finally")
	@ResponseBody
	@RequestMapping(value = "/getSeektoPurchase", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public String getSeektoPurchase(HttpServletRequest request, @RequestBody String jsonparam) {
		JSONObject result = new JSONObject();
		try {
			JSONObject json = JSONObject.fromObject(jsonparam);
			// String type = json.getString("type");
			Integer num = json.getInt("num");
			Integer pageNum = json.getInt("pageNum");
			String sortType = json.getString("sortType");
			if (sortType != null && !sortType.isEmpty() && num != null && num > 0 && pageNum != null && pageNum > 0) {
				// if (type.equals("1")) {// 精确查询（车次，日期，和起始城市）
				String fromStationCode = json.getString("fromStationCode");
				String toStationCode = json.getString("toStationCode");
				String trainDate = json.getString("trainDate");
				String train_no = json.getString("train_no");
				if (fromStationCode != null && !fromStationCode.isEmpty() && toStationCode != null
						&& !toStationCode.isEmpty() && trainDate != null && !trainDate.isEmpty() && train_no != null
						&& !train_no.isEmpty()) {
					Date nowdate = CommonDateParseUtil.string2day(CommonDateParseUtil.birthTostring(new Date()));
					Date trainDate1 = CommonDateParseUtil.string2day(trainDate);
					if (nowdate.getTime() <= trainDate1.getTime()) {// 求购日期小于车次日期
						// 通过第三方获取起始区间所有车站
						result = HttpGetStationTrain.getQueryByTrainNoUrl(train_no, toStationCode, toStationCode,
								trainDate);
						if (result.opt("code").equals("0001")) {
							result = stationService.getSeektoPurchase(num, pageNum, result, sortType, train_no,
									trainDate, fromStationCode, toStationCode);
						}
					} else {
						result.put(Constant.code.CODE, Constant.code.CODE_17);
						result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_17);
					}
				} else {
					result.put(Constant.code.CODE, Constant.code.CODE_2);
					result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_2);
				}
			}
			// else if (type.equals("2")) {// 模糊查询（日期，起始城市）
			// String trainDate = json.getString("trainDate");
			// String stationTrainCode = json.getString("stationTrainCode");
			// String fromStationName = json.getString("fromStationName");
			// String toStationName = json.getString("toStationName");
			// if (stationTrainCode != null && !stationTrainCode.isEmpty()
			// && fromStationName != null
			// && !fromStationName.isEmpty() && trainDate != null &&
			// !trainDate.isEmpty()
			// && toStationName != null && !toStationName.isEmpty()) {
			// map.put("station_train_code", stationTrainCode);
			// map.put("trainDate", trainDate);
			// map.put("fromStationName", fromStationName);
			// map.put("toStationName", toStationName);
			// List<StationTrain> stationTrainlist =
			// trainMapper.getStationTrain(map);
			// result =
			// HttpGetStationTrain.getQueryByTrainNoUrl(stationTrainlist.get(0).getTrain_no(),
			// stationTrainlist.get(0).getFromStationCode(),
			// stationTrainlist.get(0).getToStationCode(), trainDate);
			// if (result.opt("code").equals("0001")) {
			// result = stationService.getSeektoPurchase(num, pageNum,
			// result, sortType);
			// }
			// } else {
			// result.clear();
			// result.put(Constant.code.CODE, Constant.code.CODE_2);
			// result.put(Constant.message.MESSAGE,
			// Constant.message.MESSAGE_2);
			// }
			// }
			// else {
			// result.clear();
			// result.put(Constant.code.CODE, Constant.code.CODE_11);
			// result.put(Constant.message.MESSAGE,
			// Constant.message.MESSAGE_11);
			// }
			// }
			else {
				result.clear();
				result.put(Constant.code.CODE, Constant.code.CODE_2);
				result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_2);
			}
		} catch (Exception e) {
			log.error("搜索求购信息：", e);
			result.clear();
			result.put(Constant.code.CODE, Constant.code.CODE_0);
			result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_0);
		} finally {
			return result.toString();
		}
	}

	/**
	 * 
	 * @description 发布求购信息
	 * @param 车次，日期，和起始城市
	 * @return
	 */
	@SuppressWarnings("finally")
	@Transactional
	@ResponseBody
	@RequestMapping(value = "/publishSeektoPurchase", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public String publishSeektoPurchase(HttpServletRequest request, @RequestBody String jsonparam) {
		JSONObject result = new JSONObject();
		JSONObject paramsMap = new JSONObject();
		try {
			JSONObject json = JSONObject.fromObject(jsonparam);
			String uuid = json.getString("uuid");
			if (uuid != null && !uuid.isEmpty()) {
				// 验证登录状态
				paramsMap.put("uuid", uuid);
				systemService.validUser(paramsMap, result);
				if (result.get("code").equals("0001")) {
					String fromStationCode = json.getString("fromStationCode");
					String toStationCode = json.getString("toStationCode");
					String trainDate = json.getString("trainDate");
					String train_no = json.getString("train_no");
					String stationTrainCode = json.getString("stationTrainCode");
					BigDecimal price = new BigDecimal(json.opt("price").toString());
					User user = (User) JSONObject.toBean(result.getJSONObject("data"), User.class);
					if (fromStationCode != null && !fromStationCode.isEmpty() && toStationCode != null
							&& !toStationCode.isEmpty() && trainDate != null && !trainDate.isEmpty() && train_no != null
							&& !train_no.isEmpty() && price != null && price.doubleValue() >= 0
							&& stationTrainCode != null && !stationTrainCode.isEmpty()) {
						Date nowdate = CommonDateParseUtil.string2day(CommonDateParseUtil.birthTostring(new Date()));
						Date trainDate1 = CommonDateParseUtil.string2day(trainDate);
						if (nowdate.getTime() <= trainDate1.getTime()) {// 发布日期小于车次日期
							// 通过第三方获取起始区间所有车站
							result = HttpGetStationTrain.getQueryByTrainNoUrl(train_no, toStationCode, toStationCode,
									trainDate);
							if (result.opt("code").equals("0001")) {
								result.put("fromStationCode", fromStationCode);
								result.put("train_no", train_no);
								result.put("toStationCode", toStationCode);
								result.put("trainDate", trainDate);
								result.put("price", price);
								result.put("stationTrainCode", stationTrainCode);
								result = stationService.operateOrderBuy(result, user, "1");
							}
						} else {
							result.clear();
							result.put(Constant.code.CODE, Constant.code.CODE_17);
							result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_17);
						}
					} else {
						result.clear();
						result.put(Constant.code.CODE, Constant.code.CODE_2);
						result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_2);
					}
				}
			} else {
				result.clear();
				result.put(Constant.code.CODE, Constant.code.CODE_2);
				result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_2);
			}
		} catch (Exception e) {
			log.error("发布求购信息：", e);
			result.clear();
			result.put(Constant.code.CODE, Constant.code.CODE_0);
			result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_0);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		} finally {
			return result.toString();
		}
	}

	/**
	 * 
	 * @description 获取发布求购信息列表
	 * @param type
	 *            1:全部；2：上架；3：下架；4：关闭
	 * @return
	 */
	@SuppressWarnings({ "finally" })
	@ResponseBody
	@RequestMapping(value = "/getOrderBuyList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public String getOrderBuyList(HttpServletRequest request, @RequestBody String jsonparam) {
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject result = new JSONObject();
		JSONObject paramsMap = new JSONObject();
		try {
			JSONObject json = JSONObject.fromObject(jsonparam);
			String uuid = json.getString("uuid");
			if (uuid != null && !uuid.isEmpty()) {
				// 验证登录状态
				paramsMap.put("uuid", uuid);
				systemService.validUser(paramsMap, result);
				if (result.get("code").equals("0001")) {
					Integer num = json.getInt("num");
					Integer pageNum = json.getInt("pageNum");
					String type = json.getString("type");
					User user = (User) JSONObject.toBean(result.getJSONObject("data"), User.class);
					if (num != null && num > 0 && pageNum != null && pageNum > 0 && type != null && !type.isEmpty()) {
						Integer start = (pageNum - 1) * num;
						map.put("start", start);
						map.put("num", num);
						map.put("employeeId", user.getId());
						if (type.equals("2")) {
							map.put("isClose", 0);
						} else if (type.equals("3")) {
							map.put("isClose", 1);
						} else if (type.equals("4")) {
							map.put("isClose", 2);
						}
						List<OrderBuyInfo> list = trainOrderMapper.getOrderBuyInfo(map);
						for (OrderBuyInfo ob : list) {
							ob.setHeadImg(url + ob.getEmployeeCode() + "/" + ob.getHeadImg());
							ob.setTrainNum(ob.getTrainNum() - 1);
						}
						result.clear();
						result.put("data", list);
						result.put(Constant.code.CODE, Constant.code.CODE_1);
						result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_1);
					} else {
						result.clear();
						result.put(Constant.code.CODE, Constant.code.CODE_2);
						result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_2);
					}
				}
			} else {
				result.clear();
				result.put(Constant.code.CODE, Constant.code.CODE_2);
				result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_2);
			}
		} catch (Exception e) {
			log.error("发布求购信息列表：", e);
			result.clear();
			result.put(Constant.code.CODE, Constant.code.CODE_0);
			result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_0);
		} finally {
			return result.toString();
		}
	}

	/**
	 * 
	 * @description 求购信息或发布订单下架
	 * @param type:
	 *            1:求购；2 ：发布订单
	 * @return
	 */
	@SuppressWarnings("finally")
	@Transactional
	@ResponseBody
	@RequestMapping(value = "/operateOrderBuyOrSell", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public String operateOrderBuyOrSell(HttpServletRequest request, @RequestBody String jsonparam) {
		JSONObject result = new JSONObject();
		JSONObject paramsMap = new JSONObject();
		try {
			JSONObject json = JSONObject.fromObject(jsonparam);
			String uuid = json.getString("uuid");
			if (uuid != null && !uuid.isEmpty()) {
				// 验证登录状态
				paramsMap.put("uuid", uuid);
				systemService.validUser(paramsMap, result);
				if (result.get("code").equals("0001")) {
					String type = json.getString("type");
					String orderNo = json.getString("orderNo");
					User user = (User) JSONObject.toBean(result.getJSONObject("data"), User.class);
					if (type != null && !type.isEmpty() && orderNo != null && !orderNo.isEmpty()) {
						if (type.equals("1")) {
							result.put("orderNo", orderNo);
							result = stationService.operateOrderBuy(result, user, "2");
						} else if (type.equals("2")) {
							result.put("orderNo", orderNo);
							result = stationService.operateOrderSell(result, user, "2");
						} else {
							result.clear();
							result.put(Constant.code.CODE, Constant.code.CODE_11);
							result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_11);
						}
					} else {
						result.clear();
						result.put(Constant.code.CODE, Constant.code.CODE_2);
						result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_2);
					}
				}
			} else {
				result.clear();
				result.put(Constant.code.CODE, Constant.code.CODE_2);
				result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_2);
			}
		} catch (Exception e) {
			log.error("下架求购信息或发布的订单：", e);
			result.clear();
			result.put(Constant.code.CODE, Constant.code.CODE_0);
			result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_0);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		} finally {
			return result.toString();
		}
	}

	/**
	 * 
	 * @description 热门城市推荐
	 * @param type
	 * @return
	 */
	@SuppressWarnings("finally")
	@ResponseBody
	@RequestMapping(value = "/getHotCity", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public String getHotCity(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject result = new JSONObject();
		try {
			map.clear();
			List<Station> list = trainMapper.getHotCity(map);
			result.put(Constant.code.CODE, Constant.code.CODE_1);
			result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_1);
			result.put("data", list);
		} catch (Exception e) {
			log.error("热门城市推荐：", e);
			result.put(Constant.code.CODE, Constant.code.CODE_0);
			result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_0);
		} finally {
			return result.toString();
		}
	}

	/**
	 * 
	 * @description 发布座位信息
	 * @param 车次，日期，和起始城市
	 *            isSplit 0:允许分站；1：不允许分站； isAssign 0:指定购买人；1：不指定购买人；
	 * @return
	 */
	@SuppressWarnings("finally")
	@Transactional
	@ResponseBody
	@RequestMapping(value = "/publishSeat", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public String publishSeat(HttpServletRequest request, @RequestBody String jsonparam) {
		JSONObject result = new JSONObject();
		JSONObject paramsMap = new JSONObject();
		try {
			JSONObject json = JSONObject.fromObject(jsonparam);
			String uuid = json.getString("uuid");
			if (uuid != null && !uuid.isEmpty()) {
				// 验证登录状态
				paramsMap.put("uuid", uuid);
				systemService.validUser(paramsMap, result);
				if (result.get("code").equals("0001")) {
					String fromStationCode = json.getString("fromStationCode");
					String toStationCode = json.getString("toStationCode");
					String trainDate = json.getString("trainDate");
					String train_no = json.getString("train_no");
					String seatName = json.getString("seatName");
					Integer carriageNum = json.getInt("carriageNum");
					BigDecimal price = new BigDecimal(json.opt("price").toString());
					Integer isSplit = json.getInt("isSplit");
					Integer isAssign = json.getInt("isAssign");
					String assignAmount = json.getString("assignAmount");
					String stationTrainCode = json.getString("stationTrainCode");
					User user = (User) JSONObject.toBean(result.getJSONObject("data"), User.class);
					if (fromStationCode != null && !fromStationCode.isEmpty() && toStationCode != null
							&& !toStationCode.isEmpty() && trainDate != null && !trainDate.isEmpty() && train_no != null
							&& !train_no.isEmpty() && price != null && price.doubleValue() >= 0 && carriageNum != null
							&& carriageNum > 0 && isSplit != null && isSplit >= 0 && isAssign != null && isAssign >= 0
							&& seatName != null && !seatName.isEmpty() && stationTrainCode != null
							&& !stationTrainCode.isEmpty()) {
						// seatName大写 ，座位正则
						seatName = seatName.toUpperCase();
						Pattern mPattern1 = Pattern.compile("^[1-9]+[a,b,c,d,f,A,B,C,D,F,1-9]$");
						Matcher m = mPattern1.matcher(seatName);
						if (!m.matches() || seatName.length() >3) {
							result.clear();
							result.put(Constant.code.CODE, Constant.code.CODE_54);
							result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_54);
							return result.toString();
						}
						// 指定购买人时，不允许分站；允许分站时，无法指定购买人
						if ((isSplit == 0 && isAssign == 0)) {
							result.clear();
							result.put(Constant.code.CODE, Constant.code.CODE_25);
							result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_25);
							return result.toString();
						} else {
							if (isAssign == 0) {
								if (assignAmount != null && !assignAmount.isEmpty() && !assignAmount.equals("0")) {
									paramsMap.clear();
									paramsMap.put("employeeCode", assignAmount);
									List<Map<String, Object>> empList = employeeMapper.selectEmplyeeInfo(paramsMap);
									if (empList.size() == 0) {
										result.put(Constant.code.CODE, Constant.code.CODE_26);
										result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_26);
										return result.toString();
									} // 指定购买人时，不可指定给自己
									if (empList.get(0).get("employeeCode").equals(user.getEmployeeCode())) {
										result.put(Constant.code.CODE, Constant.code.CODE_47);
										result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_47);
										return result.toString();
									}
								} else {
									result.put(Constant.code.CODE, Constant.code.CODE_2);
									result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_2);
									return result.toString();
								}
							}
						}
						Date nowdate = CommonDateParseUtil.string2day(CommonDateParseUtil.birthTostring(new Date()));
						Date trainDate1 = CommonDateParseUtil.string2day(trainDate);
						if (nowdate.getTime() <= trainDate1.getTime()) {// 发布日期小于车次日期
							// 通过第三方获取起始区间所有车站
							result = HttpGetStationTrain.getQueryByTrainNoUrl(train_no, toStationCode, toStationCode,
									trainDate);
							if (result.opt("code").equals("0001")) {
								result.put("fromStationCode", fromStationCode);
								result.put("train_no", train_no);
								result.put("toStationCode", toStationCode);
								result.put("trainDate", trainDate);
								result.put("price", price);
								result.put("seatName", seatName);
								result.put("carriageNum", carriageNum);
								result.put("isSplit", isSplit);
								result.put("isAssign", isAssign);
								result.put("assignAmount", assignAmount);
								result.put("stationTrainCode", stationTrainCode);
								result = stationService.operateOrderSell(result, user, "1");
							}
						} else {
							result.clear();
							result.put(Constant.code.CODE, Constant.code.CODE_24);
							result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_24);
						}
					} else {
						result.clear();
						result.put(Constant.code.CODE, Constant.code.CODE_2);
						result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_2);
					}
				}
			} else {
				result.clear();
				result.put(Constant.code.CODE, Constant.code.CODE_2);
				result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_2);
			}
		} catch (Exception e) {
			log.error("发布座位信息：", e);
			result.clear();
			result.put(Constant.code.CODE, Constant.code.CODE_0);
			result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_0);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		} finally {
			return result.toString();
		}
	}

	/**
	 * 
	 * @description 获取发布座位信息列表
	 * @param type
	 *            1:个人中心的列表 ；2：未下架的座位信息（）
	 * @return
	 */
	@SuppressWarnings({ "finally" })
	@ResponseBody
	@RequestMapping(value = "/getOrderSellList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public String getOrderSellList(HttpServletRequest request, @RequestBody String jsonparam) {
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject result = new JSONObject();
		JSONObject paramsMap = new JSONObject();
		try {
			JSONObject json = JSONObject.fromObject(jsonparam);
			String uuid = json.getString("uuid");
			String type = json.getString("type");
			Integer num = json.getInt("num");
			Integer pageNum = json.getInt("pageNum");
			if (uuid != null && !uuid.isEmpty() && type != null && !type.isEmpty() && num != null && num > 0
					&& pageNum != null && pageNum > 0) {
				// 验证登录状态
				paramsMap.put("uuid", uuid);
				systemService.validUser(paramsMap, result);
				if (result.get("code").equals("0001")) {
					User user = (User) JSONObject.toBean(result.getJSONObject("data"), User.class);
					Integer start = (pageNum - 1) * num;
					map.put("start", start);
					map.put("num", num);
					map.put("employeeId", user.getId());
					if (type.equals("1")) {
					} else if (type.equals("2")) {
						map.put("isClose", 0);
					} else {
						result.clear();
						result.put(Constant.code.CODE, Constant.code.CODE_11);
						result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_11);
					}
					List<OrderSellInfo> list = trainOrderMapper.getOrderSellInfo(map);
					// for (OrderSellInfo ob : list) {
					// ob.setTrainCode(ob.getTrainCode().substring(0,
					// ob.getTrainCode().indexOf("(")));
					// map.clear();
					// map.put("orderSellNo", ob.getOrderSellNo());
					// List<OrderSellStation> list2 =
					// trainOrderMapper.getOrderSellStation(map);
					// ob.setList(list2);
					// }
					result.clear();
					result.put("data", list);
					result.put(Constant.code.CODE, Constant.code.CODE_1);
					result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_1);
				}
			} else {
				result.clear();
				result.put(Constant.code.CODE, Constant.code.CODE_2);
				result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_2);
			}
		} catch (Exception e) {
			log.error("座位信息列表：", e);
			result.clear();
			result.put(Constant.code.CODE, Constant.code.CODE_0);
			result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_0);
		} finally {
			return result.toString();
		}
	}

	/**
	 * 
	 * @description 搜索座位信息列表
	 * @param sortType
	 *            0：综合排序 1:按车厢倒序；2：符合车站数倒序；3：价格倒序 ;4:按车厢正序;5:符合车站数正序;6:价格正序
	 * @return
	 */
	@SuppressWarnings("finally")
	@ResponseBody
	@RequestMapping(value = "/getSeatInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public String getSeatInfo(HttpServletRequest request, @RequestBody String jsonparam) {
		JSONObject result = new JSONObject();
		try {
			JSONObject json = JSONObject.fromObject(jsonparam);
			Integer num = json.getInt("num");
			Integer pageNum = json.getInt("pageNum");
			String sortType = json.getString("sortType");
			if (sortType != null && !sortType.isEmpty() && num != null && num > 0 && pageNum != null && pageNum > 0) {
				String fromStationCode = json.getString("fromStationCode");
				String toStationCode = json.getString("toStationCode");
				String trainDate = json.getString("trainDate");
				String train_no = json.getString("train_no");
				if (fromStationCode != null && !fromStationCode.isEmpty() && toStationCode != null
						&& !toStationCode.isEmpty() && trainDate != null && !trainDate.isEmpty() && train_no != null
						&& !train_no.isEmpty()) {
					Date nowdate = CommonDateParseUtil.string2day(CommonDateParseUtil.birthTostring(new Date()));
					Date trainDate1 = CommonDateParseUtil.string2day(trainDate);
					if (nowdate.getTime() <= trainDate1.getTime()) {// 搜索日期小于车次日期
						// 通过第三方获取起始区间所有车站
						result = HttpGetStationTrain.getQueryByTrainNoUrl(train_no, toStationCode, toStationCode,
								trainDate);
						if (result.opt("code").equals("0001")) {
							result = stationService.getSeat(num, pageNum, result, sortType, train_no, trainDate,
									fromStationCode, toStationCode);
						}
					} else {
						result.put(Constant.code.CODE, Constant.code.CODE_17);
						result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_17);
					}
				} else {
					result.put(Constant.code.CODE, Constant.code.CODE_2);
					result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_2);
				}
			} else {
				result.clear();
				result.put(Constant.code.CODE, Constant.code.CODE_2);
				result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_2);
			}
		} catch (Exception e) {
			log.error("搜索座位信息：", e);
			result.clear();
			result.put(Constant.code.CODE, Constant.code.CODE_0);
			result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_0);
		} finally {
			return result.toString();
		}
	}

	/**
	 * 
	 * @description 获取发布座位信息详情
	 * @param type
	 *            1:个人中心 ；2：用于购买信息详情（聊天记录入口，指定购买人通知入口）
	 * @return
	 */
	@SuppressWarnings({ "finally" })
	@ResponseBody
	@RequestMapping(value = "/getOrderSellDetails", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public String getOrderSellDetails(HttpServletRequest request, @RequestBody String jsonparam) {
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject result = new JSONObject();
		JSONObject paramsMap = new JSONObject();
		try {
			JSONObject json = JSONObject.fromObject(jsonparam);
			String uuid = json.getString("uuid");
			String type = json.getString("type");
			String orderSellNo = json.getString("orderSellNo");
			if (uuid != null && !uuid.isEmpty() && type != null && !type.isEmpty() && orderSellNo != null
					&& !orderSellNo.isEmpty()) {
				// 验证登录状态
				paramsMap.put("uuid", uuid);
				systemService.validUser(paramsMap, result);
				if (result.get("code").equals("0001")) {
					User user = (User) JSONObject.toBean(result.getJSONObject("data"), User.class);
					map.put("orderSellNo", orderSellNo);
					if (type.equals("1")) {
						map.put("employeeId", user.getId());
					} else if (type.equals("2")) {
						map.put("isClose", 0);
					} else {
						result.clear();
						result.put(Constant.code.CODE, Constant.code.CODE_11);
						result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_11);
					}
					List<OrderSellInfo> list = trainOrderMapper.getOrderSellInfo(map);
					if (list.size() == 0) {// 座位信息不存在
						result.clear();
						result.put(Constant.code.CODE, Constant.code.CODE_33);
						result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_33);
						return result.toString();
					}
					// 当是指定购买人时查看人的信息与购买人的信息一致
					if (list.get(0).getIsAssign() == 0 && type.equals("2")
							&& !list.get(0).getAssignAmount().equals(user.getEmployeeCode())) {
						result.clear();
						result.put(Constant.code.CODE, Constant.code.CODE_31);
						result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_31);
						return result.toString();
					}
					if (type.equals("1")) {// 是否有购买信息
						for (OrderSellInfo ob : list) {
							map.clear();
							map.put("orderSellNo", ob.getOrderSellNo());
							map.put("start", 0);
							map.put("num", 50);
							List<Order> list3 = trainOrderMapper.getOrder(map);
							for (Order o : list3) {
								// o.setTrainCode(o.getTrainCode().substring(0,
								// o.getTrainCode().indexOf("(")));
								map.clear();
								map.put("orderNo", o.getOrderNo());
								List<OrderStation> list4 = trainOrderMapper.getOrderStation(map);
								o.setStartStationName(list4.get(0).getFromStationName());
								o.setEndStationName(list4.get(list4.size() - 1).getToStationName());
								o.setHeadImg(url + o.getEmployeeCode() + "/" + o.getHeadImg());
							}
							ob.setHeadImg(url + ob.getEmployeeCode() + "/" + ob.getHeadImg());
							ob.setOrderList(list3);
						}
					}
					for (OrderSellInfo ob : list) {
						// ob.setTrainCode(ob.getTrainCode().substring(0,
						// ob.getTrainCode().indexOf("(")));
						map.clear();
						map.put("orderSellNo", ob.getOrderSellNo());
						List<OrderSellStation> list2 = trainOrderMapper.getOrderSellStation(map);
						ob.setList(list2);
					}
					result.clear();
					result.put("data", list.get(0));
					result.put(Constant.code.CODE, Constant.code.CODE_1);
					result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_1);
				}
			} else {
				result.clear();
				result.put(Constant.code.CODE, Constant.code.CODE_2);
				result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_2);
			}
		} catch (Exception e) {
			log.error("座位信息详情：", e);
			result.clear();
			result.put(Constant.code.CODE, Constant.code.CODE_0);
			result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_0);
		} finally {
			return result.toString();
		}
	}

	/**
	 * 
	 * @description 获取发布座位信息详情（搜索界面查看）
	 * @param
	 * @return
	 */
	@SuppressWarnings({ "finally" })
	@ResponseBody
	@RequestMapping(value = "/getSeatInfoDetails", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public String getSeatInfoDetails(HttpServletRequest request, @RequestBody String jsonparam) {
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject result = new JSONObject();
		try {
			JSONObject json = JSONObject.fromObject(jsonparam);
			String orderSellNo = json.getString("orderSellNo");
			if (orderSellNo != null && !orderSellNo.isEmpty()) {
				map.put("orderSellNo", orderSellNo);
				map.put("isClose", 0);
				List<OrderSellInfo> list = trainOrderMapper.getOrderSellInfo(map);
				if (list.size() == 0) {// 座位信息不存在
					result.clear();
					result.put(Constant.code.CODE, Constant.code.CODE_33);
					result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_33);
					return result.toString();
				} else {
					if (list.get(0).getIsAssign() == 0) {
						result.clear();
						result.put(Constant.code.CODE, Constant.code.CODE_34);
						result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_34);
						return result.toString();
					}
				}
				for (OrderSellInfo ob : list) {
					// ob.setTrainCode(ob.getTrainCode().substring(0,
					// ob.getTrainCode().indexOf("(")));
					map.clear();
					map.put("orderSellNo", ob.getOrderSellNo());
					List<OrderSellStation> list2 = trainOrderMapper.getOrderSellStation(map);
					ob.setHeadImg(url + ob.getEmployeeCode() + "/" + ob.getHeadImg());
					ob.setList(list2);
				}
				result.clear();
				result.put("data", list.get(0));
				result.put(Constant.code.CODE, Constant.code.CODE_1);
				result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_1);
			} else {
				result.clear();
				result.put(Constant.code.CODE, Constant.code.CODE_2);
				result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_2);
			}
		} catch (Exception e) {
			log.error("搜索座位信息详情：", e);
			result.clear();
			result.put(Constant.code.CODE, Constant.code.CODE_0);
			result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_0);
		} finally {
			return result.toString();
		}
	}

}
