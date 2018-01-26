package com.yl.Controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.yl.Service.StationOrderService;
import com.yl.Service.StationService;
import com.yl.Service.SystemService;
import com.yl.Utils.CommonDateParseUtil;
import com.yl.Utils.Constant;
import com.yl.Utils.GetProperties;
import com.yl.bean.Order;
import com.yl.bean.OrderSellInfo;
import com.yl.bean.OrderStation;
import com.yl.bean.OrderTrade;
import com.yl.bean.User;
import com.yl.mapper.TrainMapper;
import com.yl.mapper.TrainOrderMapper;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/stationOrder")
public class StationOrderController {
	private Logger log = Logger.getLogger(StationOrderController.class);
	@Autowired
	private TrainMapper trainMapper;
	@Autowired
	private SystemService systemService;
	@Autowired
	private StationService stationService;
	@Autowired
	private TrainOrderMapper trainOrderMapper;
	@Autowired
	private StationOrderService stationOrderService;

	private static String url = GetProperties.getFileUrl("pGZUserUrl");

	/**
	 * 
	 * @description 座位下单
	 * @param
	 * @return
	 */
	@SuppressWarnings({ "finally", "unchecked" })
	@Transactional
	@ResponseBody
	@RequestMapping(value = "/orderConfirm", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public String orderConfirm(HttpServletRequest request, @RequestBody String jsonparam) {
		JSONObject result = new JSONObject();
		JSONObject paramsMap = new JSONObject();
		try {
			JSONObject json = JSONObject.fromObject(jsonparam);
			String uuid = json.getString("uuid");
			String orderSellNo = json.getString("orderSellNo");
			List<Integer> list = json.getJSONArray("list");
			if (uuid != null && !uuid.isEmpty() && orderSellNo != null && !orderSellNo.isEmpty() && list != null
					&& !list.isEmpty() && list.size() > 0) {
				// 验证登录状态
				paramsMap.put("uuid", uuid);
				systemService.validUser(paramsMap, result);
				if (result.get("code").equals("0001")) {
					User user = (User) JSONObject.toBean(result.getJSONObject("data"), User.class);
					result = stationOrderService.checkOrder(orderSellNo, user, list);
					if (result.get("code").equals("0001")) {// 生成订单
						result = stationOrderService.saveOrder(orderSellNo, user, list, result.getDouble("price"));
					}
				}
			} else {
				result.clear();
				result.put(Constant.code.CODE, Constant.code.CODE_2);
				result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_2);
			}
		} catch (Exception e) {
			log.error("购买订单：", e);
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
	 * @description 获取购买订单列表
	 * @param
	 * @return
	 */
	@SuppressWarnings({ "finally" })
	@ResponseBody
	@RequestMapping(value = "/getOrderList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public String getOrderList(HttpServletRequest request, @RequestBody String jsonparam) {
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject result = new JSONObject();
		JSONObject paramsMap = new JSONObject();
		try {
			JSONObject json = JSONObject.fromObject(jsonparam);
			String uuid = json.getString("uuid");
			Integer num = json.getInt("num");
			Integer pageNum = json.getInt("pageNum");
			if (uuid != null && !uuid.isEmpty() && num != null && num > 0 && pageNum != null && pageNum > 0) {
				// 验证登录状态
				paramsMap.put("uuid", uuid);
				systemService.validUser(paramsMap, result);
				if (result.get("code").equals("0001")) {
					User user = (User) JSONObject.toBean(result.getJSONObject("data"), User.class);
					Integer start = (pageNum - 1) * num;
					map.put("start", start);
					map.put("num", num);
					map.put("employeeId", user.getId());
					List<Order> list = trainOrderMapper.getOrder(map);
					if (list.size() > 0) {
						for (Order ob : list) {
							map.clear();
							map.put("orderNo", ob.getOrderNo());
							List<OrderStation> list4 = trainOrderMapper.getOrderStation(map);
							ob.setStartStationName(list4.get(0).getFromStationName());
							ob.setEndStationName(list4.get(list4.size() - 1).getToStationName());
						}
					}
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
			log.error("购买订单列表：", e);
			result.clear();
			result.put(Constant.code.CODE, Constant.code.CODE_0);
			result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_0);
		} finally {
			return result.toString();
		}
	}

	/**
	 * 
	 * @description 获取购买订单详情
	 * @param
	 * @return
	 */
	@SuppressWarnings({ "finally" })
	@ResponseBody
	@RequestMapping(value = "/getOrderDetails", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public String getOrderDetails(HttpServletRequest request, @RequestBody String jsonparam) {
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject result = new JSONObject();
		JSONObject paramsMap = new JSONObject();
		try {
			JSONObject json = JSONObject.fromObject(jsonparam);
			String uuid = json.getString("uuid");
			String orderNo = json.getString("orderNo");
			if (uuid != null && !uuid.isEmpty() && orderNo != null && !orderNo.isEmpty()) {
				// 验证登录状态
				paramsMap.put("uuid", uuid);
				systemService.validUser(paramsMap, result);
				if (result.get("code").equals("0001")) {
					User user = (User) JSONObject.toBean(result.getJSONObject("data"), User.class);
					map.put("orderNo", orderNo);
					map.put("employeeId", user.getId());
					List<Order> list = trainOrderMapper.getOrder(map);
					if (list.size() > 0) {
						for (Order ob : list) {
							Date nowTime = new Date();
							ob.setNowTime(CommonDateParseUtil.date2string(nowTime));
							map.clear();
							map.put("orderNo", ob.getOrderNo());
							List<OrderStation> list4 = trainOrderMapper.getOrderStation(map);
							ob.setStartStationName(list4.get(0).getFromStationName());
							ob.setEndStationName(list4.get(list4.size() - 1).getToStationName());
							ob.setHeadImg(url + ob.getEmployeeCode() + "/" + ob.getHeadImg());
							map.clear();
							map.put("orderSellNo", list.get(0).getOrderSellNo());
							List<OrderSellInfo> list1 = trainOrderMapper.getOrderSellInfo(map);
							if (list1.size() > 0) {
								ob.setSellEmployeeCode(list1.get(0).getEmployeeCode());
								ob.setSellEmployeeId(list1.get(0).getEmployeeId());
								ob.setSellIdCardAuthentication(list1.get(0).getIdCardAuthentication());
								ob.setSellHeadImg(
										url + list1.get(0).getEmployeeCode() + "/" + list1.get(0).getHeadImg());
							}
						}
						result.clear();
						result.put("data", list.get(0));
						result.put(Constant.code.CODE, Constant.code.CODE_1);
						result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_1);
					} else {
						result.clear();
						result.put(Constant.code.CODE, Constant.code.CODE_34);
						result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_34);
					}
				}
			} else {
				result.clear();
				result.put(Constant.code.CODE, Constant.code.CODE_2);
				result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_2);
			}
		} catch (Exception e) {
			log.error("购买订单详情：", e);
			result.clear();
			result.put(Constant.code.CODE, Constant.code.CODE_0);
			result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_0);
		} finally {
			return result.toString();
		}
	}

	/**
	 * 
	 * @description 获取发布订单列表(二维码交互前列表)
	 * @param 订单未失效（购买）
	 * @return
	 */
	@SuppressWarnings({ "finally" })
	@ResponseBody
	@RequestMapping(value = "/getOrderSellTradeList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public String getOrderSellTradeList(HttpServletRequest request, @RequestBody String jsonparam) {
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
					User user = (User) JSONObject.toBean(result.getJSONObject("data"), User.class);
					map.put("employeeId", user.getId());
					List<OrderTrade> list = trainOrderMapper.getOrderTradeInfo(map);
					if (list.size() > 0) {
						for (OrderTrade ot : list) {
							map.clear();
							map.put("orderNo", ot.getOrderNo());
							List<OrderStation> list1 = trainOrderMapper.getOrderStation(map);
							ot.setTrainNum(list1.size());
							ot.setEndStationName(list1.get(list1.size() - 1).getToStationName());
							ot.setStartStationName(list1.get(0).getFromStationName());
						}
					}
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
			log.error("发布订单列表(交易未完成)：", e);
			result.clear();
			result.put(Constant.code.CODE, Constant.code.CODE_0);
			result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_0);
		} finally {
			return result.toString();
		}
	}

	/**
	 * 
	 * @description 座位信息交易完成
	 * @param
	 * @return
	 */
	@SuppressWarnings({ "finally" })
	@Transactional
	@ResponseBody
	@RequestMapping(value = "/orderTradeConfirm", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public String orderTradeConfirm(HttpServletRequest request, @RequestBody String jsonparam) {
		JSONObject result = new JSONObject();
		JSONObject paramsMap = new JSONObject();
		try {
			JSONObject json = JSONObject.fromObject(jsonparam);
			String uuid = json.getString("uuid");
			String orderSellNo = json.getString("orderSellNo");
			String orderNo = json.getString("orderNo");
			if (uuid != null && !uuid.isEmpty() && orderSellNo != null && !orderSellNo.isEmpty() && orderNo != null
					&& !orderNo.isEmpty()) { 
				// 验证登录状态
				paramsMap.put("uuid", uuid);
				systemService.validUser(paramsMap, result);
				if (result.get("code").equals("0001")) {
					User user = (User) JSONObject.toBean(result.getJSONObject("data"), User.class);
					result = stationOrderService.checkOrderTrade(orderSellNo, user, orderNo);
					if (result.get("code").equals("0001")) {// 完成订单，插入流水，发送消息
						OrderTrade ot = (OrderTrade) JSONObject.toBean(result.getJSONObject("orderTrade"),
								OrderTrade.class);
						result = stationOrderService.orderDone(ot, user);
					}
				}
			} else {
				result.clear();
				result.put(Constant.code.CODE, Constant.code.CODE_2);
				result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_2);
			}
		} catch (Exception e) {
			log.error("订单交易：", e);
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
	 * @description 座位信息交易完成
	 * @param
	 * @return
	 */
	@SuppressWarnings({ "finally", "unchecked" })
	@ResponseBody
	@RequestMapping(value = "/orderTradeCheck", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public String orderTradeCheck(HttpServletRequest request, @RequestBody String jsonparam) {
		JSONObject result = new JSONObject();
		JSONObject paramsMap = new JSONObject();
		try {
			JSONObject json = JSONObject.fromObject(jsonparam);
			String uuid = json.getString("uuid");
			String orderSellNo = json.getString("orderSellNo");
			String orderNo = json.getString("orderNo");
			if (uuid != null && !uuid.isEmpty() && orderSellNo != null && !orderSellNo.isEmpty() && orderNo != null
					&& !orderNo.isEmpty()) {
				// 验证登录状态
				paramsMap.put("uuid", uuid);
				systemService.validUser(paramsMap, result);
				if (result.get("code").equals("0001")) {
					User user = (User) JSONObject.toBean(result.getJSONObject("data"), User.class);
					paramsMap.clear();
					paramsMap.put("orderNo", orderNo);
					List<Order> order = trainOrderMapper.getOrder(paramsMap);
					paramsMap.clear();
					paramsMap.put("orderSellNo", orderSellNo);
					paramsMap.put("employeeId", user.getId());
					List<OrderSellInfo> orderSell = trainOrderMapper.getOrderSellInfo(paramsMap);
					if (order.size() > 0 && orderSell.size() > 0 && order.get(0).getOrderSellNo().equals(orderSellNo)) {
						result.clear();
						result.put(Constant.code.CODE, Constant.code.CODE_1);
						result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_1);
						result.put("data", order.get(0).getState());
					} else {
						result.clear();
						result.put(Constant.code.CODE, Constant.code.CODE_34);
						result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_34);
					}
				}
			} else {
				result.clear();
				result.put(Constant.code.CODE, Constant.code.CODE_2);
				result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_2);
			}
		} catch (Exception e) {
			log.error("订单交易校验状态：", e);
			result.clear();
			result.put(Constant.code.CODE, Constant.code.CODE_0);
			result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_0);
		} finally {
			return result.toString();
		}
	}

}
