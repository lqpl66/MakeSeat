package com.yl.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yl.Utils.CodeUtils;
import com.yl.Utils.CommonDateParseUtil;
import com.yl.Utils.Constant;
import com.yl.bean.Order;
import com.yl.bean.OrderSellInfo;
import com.yl.bean.OrderSellStation;
import com.yl.bean.OrderTrade;
import com.yl.bean.User;
import com.yl.bean.Userinfo;
import com.yl.mapper.EmployeeMapper;
import com.yl.mapper.TrainMapper;
import com.yl.mapper.TrainOrderMapper;
import net.sf.json.JSONObject;

/**
 * 
 * @author Administrator
 *
 */
@Service
public class StationOrderService {
	@Autowired
	private EmployeeMapper employeeMapper;
	@Autowired
	private TrainOrderMapper trainOrderMapper;
	@Autowired
	private MessageService messageService;
	@Autowired
	private ExpenseService expenseService;
	@Autowired
	private EmployeeService employeeService;

	/**
	 * 
	 * @description 校验是否可以下单
	 * @param
	 * @return
	 */
	public JSONObject checkOrder(String orderSellNo, User user, List<Integer> list) {
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject result = new JSONObject();
		// 座位是上架状态,还有座位余站，用户只可以一单（未支付，已支付），，车站连续()
		map.clear();
		map.put("orderSellNo", orderSellNo);
		map.put("isClose", 0);
		List<OrderSellInfo> oslist = trainOrderMapper.getOrderSellInfo(map);
		if ((oslist.size() > 0 && oslist.get(0).getNum() > 0)) {
			// 当时指定购买人时，指定人信息和购买人信息一致
			if (oslist.get(0).getIsAssign() == 0 && !oslist.get(0).getAssignAmount().equals(user.getEmployeeCode())) {
				result.clear();
				result.put(Constant.code.CODE, Constant.code.CODE_32);
				result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_32);
				return result;
			} // 发布者不能和购买者为同一人
			if (oslist.get(0).getEmployeeId().equals(user.getId())) {
				result.clear();
				result.put(Constant.code.CODE, Constant.code.CODE_42);
				result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_42);
				return result;
			}
			map.clear();
			map.put("orderSellNo", orderSellNo);
			map.put("employeeId", user.getId());
			List<Order> olist = trainOrderMapper.getOrder(map);
			boolean flag = true;
			if (olist.size() == 0
					|| (olist.size() > 0 && olist.get(0).getState() != 2 && olist.get(0).getState() != 1)) {
				map.clear();
				map.put("orderSellNo", orderSellNo);
				List<OrderSellStation> osslist = trainOrderMapper.getOrderSellStation(map);
				if (osslist.size() > 0) {
					for (int i = 0; i <= list.size() - 1; i++) {
						map.clear();
						map.put("orderSellNo", orderSellNo);
						map.put("id", list.get(i));
						List<OrderSellStation> os = trainOrderMapper.getOrderSellStation(map);
						if (os.size() > 0 && os.get(0).getIsSell().equals(0)) {// 校验站点存在且未售
							// id连续
							if (list.size() > 1) {
								if (i<=list.size()-2&&(list.get(i + 1).intValue() - list.get(i).intValue()) != 1) {
									flag = false;
									break;
								}
							}
						} else {
							flag = false;
							break;
						}
					}
					if (flag) {
						//当天购买退款次数上限为3
						map.clear();
						map.put("employeeId", user.getId());
						Integer num = trainOrderMapper.getOrderRefund(map);
						if(num>=3){
							result.clear();
							result.put(Constant.code.CODE, Constant.code.CODE_55);
							result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_55);
							return result;
						}
						result.clear();
						result.put("price", oslist.get(0).getPrice());
						result.put(Constant.code.CODE, Constant.code.CODE_1);
						result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_1);
					} else {
						result.clear();
						result.put(Constant.code.CODE, Constant.code.CODE_30);
						result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_30);
					}
				} else {
					result.clear();
					result.put(Constant.code.CODE, Constant.code.CODE_28);
					result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_28);
				}
			} else {
				result.clear();
				result.put(Constant.code.CODE, Constant.code.CODE_29);
				result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_29);
			}
		} else {
			result.clear();
			result.put(Constant.code.CODE, Constant.code.CODE_28);
			result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_28);
		}
		return result;
	}

	/**
	 * 
	 * @description 下单
	 * @param
	 * @return
	 */
	public JSONObject saveOrder(String orderSellNo, User user, List<Integer> list, double price) {
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject result = new JSONObject();
		String orderNo = CodeUtils.getOrderBuy(user.getEmployeeCode());
		map.put("orderSellNo", orderSellNo);
		map.put("acount", price * list.size());
		map.put("orderNo", orderNo);
		map.put("employeeId", user.getId());
		map.put("state", 1);
		Calendar time = Calendar.getInstance();
		time.add(Calendar.MINUTE, 15);
		map.put("loseTime", CommonDateParseUtil.date2string(time.getTime()));
		trainOrderMapper.saveOrder(map);
		for (Integer ii : list) {
			map.clear();
			map.put("orderNo", orderNo);
			map.put("sellStationId", ii);
			trainOrderMapper.saveOrderStation(map);
			map.clear();
			map.put("orderSellNo", orderSellNo);
			map.put("id", ii);
			map.put("isSell", 1);
			trainOrderMapper.updateOrderSellStation(map);
		}
		JSONObject data = new JSONObject();
		data.put("orderNo", orderNo);
		data.put("amount", price * list.size());
		result.put("data", data);
		result.put(Constant.code.CODE, Constant.code.CODE_1);
		result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_1);
		return result;
	}

	/**
	 * 
	 * @description 校验是否可以交易
	 * @param
	 * @return
	 */
	public JSONObject checkOrderTrade(String orderSellNo, User user, String orderNo) {
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject result = new JSONObject();
		// 订单当天有效，买家订单已支付状态
		map.clear();
		map.put("orderSellNo", orderSellNo);
		map.put("userId", user.getId());
		map.put("orderNo", orderNo);
		List<OrderTrade> list = trainOrderMapper.getOrderTradeInfo(map);
		if (list.size() > 0) {
			result.put("orderTrade", list.get(0));
			result.put(Constant.code.CODE, Constant.code.CODE_1);
			result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_1);
		} else {
			result.put(Constant.code.CODE, Constant.code.CODE_35);
			result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_35);
		}
		return result;
	}

	/**
	 * 
	 * @description 完成订单
	 * @param
	 * @return
	 */
	public JSONObject orderDone(OrderTrade ot, User user) {
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject result = new JSONObject();
		// 订单完成状态，卖家流水和金额添加消息推送
		map.put("orderNo", ot.getOrderNo());
		map.put("state1", 3);
		map.put("employeeId", user.getId());
		trainOrderMapper.updateOrder(map);
		map.clear();
		map.put("acount", ot.getAcount());
		map.put("id", ot.getSellUserId());
		employeeMapper.updateEmployeeInfo(map);
		try {// 消息推送(订单通知，账户通知)
			JSONObject js = new JSONObject();
			js.put("trainNo", ot.getTrainCode());
			js.put("site", ot.getSite());
			js.put("dispatchTime", ot.getTrainDate());
			js.put("orderSellNo", ot.getOrderSellNo());
			js.put("orderNo", ot.getOrderNo());
			js.put("sellerId", ot.getSellUserId());
			js.put("modelType", "OD002");
			js.put("messageType", 5);
			js.put("uuid", user.getUuid());
			messageService.saveMessage(js, js);// 卖家交易成功
			js.put("orderNo", ot.getOrderSellNo());
			js.put("modelType", "AC000");
			js.put("messageType", 3);
			js.put("date", CommonDateParseUtil.date2string(new Date()));
			js.put("money", ot.getAcount());
			js.put("userId", ot.getSellUserId());
			messageService.saveMessage(js, js);// 卖家余额变动

		} catch (Exception e) {
			e.printStackTrace();
		}
		// 卖家用户信息
		JSONObject paramsMap = new JSONObject();
		JSONObject resultMap = new JSONObject();
		paramsMap.put("id", ot.getSellUserId());
		employeeService.getEmployeeInfo(paramsMap, resultMap);
		Userinfo userSellInfo = (Userinfo) JSONObject.toBean(resultMap.getJSONObject("data"), Userinfo.class);
		// 插入流水
		expenseService.saveExpenseUserAndSystemlog(userSellInfo, null, null, ot.getAcount(), null, 3, 3);
		result.put(Constant.code.CODE, Constant.code.CODE_1);
		result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_1);
		return result;
	}

	/**
	 * 
	 * @description 订单关闭
	 * @param
	 * @return
	 */
	public void orderClose() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("type", "1");
		List<OrderTrade> list = trainOrderMapper.getOrderCloseTradeInfo(map);
		if (list.size() > 0) {// 订单
			for (OrderTrade ot : list) {
				// 获取该订单已支付状态下的购买订单，，
				map.clear();
				map.put("orderSellNo", ot.getOrderSellNo());
				List<OrderTrade> list1 = trainOrderMapper.getOrderCloseTradeInfo(map);
				if (list1.size() > 0) {
					double acount = 0;
					for (OrderTrade ott : list1) {
						// 统计总价,购买订单完成
						if (ott.getOrderNo() != null && !ott.getOrderNo().isEmpty() && ott.getState() == 2) {
							map.clear();
							map.put("orderNo", ott.getOrderNo());
							map.put("state1", 3);
							map.put("employeeId", ott.getBuyUserId());
							trainOrderMapper.updateOrder(map);
							acount = acount + ott.getAcount().doubleValue();
						}
					} // 卖家获取金额，座位信息关闭，和
					if (acount > 0) {
						map.clear();
						map.put("acount", acount);
						map.put("id", ot.getSellUserId());
						employeeMapper.updateEmployeeInfo(map);
						// 插入流水
						// 卖家用户信息
						JSONObject paramsMap = new JSONObject();
						JSONObject resultMap = new JSONObject();
						paramsMap.put("id", ot.getSellUserId());
						employeeService.getEmployeeInfo(paramsMap, resultMap);
						Userinfo userSellInfo = (Userinfo) JSONObject.toBean(resultMap.getJSONObject("data"),
								Userinfo.class);
						// 插入流水
						expenseService.saveExpenseUserAndSystemlog(userSellInfo, null, null, ot.getAcount(), null, 3,
								3);
						// 消息
						try {// 消息推送(账户通知)
							JSONObject js = new JSONObject();
							js.put("modelType", "AC000");
							js.put("messageType", 3);
							js.put("date", CommonDateParseUtil.date2string(new Date()));
							js.put("money", acount);
							js.put("userId", ot.getSellUserId());
							messageService.saveMessage(js, js);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					map.clear();
					map.put("isClose1", 2);
					map.put("orderSellNo", ot.getOrderSellNo());
					trainOrderMapper.updateOrderSell(map);
					try {// 消息推送(订单通知)
						JSONObject js = new JSONObject();
						js.put("trainNo", ot.getTrainCode());
						js.put("site", ot.getSite());
						js.put("dispatchTime", ot.getTrainDate());
						js.put("orderSellNo", ot.getOrderSellNo());
						js.put("orderNo", ot.getOrderNo());
						js.put("sellerId", ot.getSellUserId());
						js.put("modelType", "OD003");
						js.put("messageType", 1);
						messageService.saveMessage(js, js);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * 
	 * @description 求购信息关闭
	 * @param
	 * @return
	 */
	public void orderBuyClose() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("close", "1");
		map.put("isClose", 2);
		trainOrderMapper.updateOrderBuy(map);
	}
}
