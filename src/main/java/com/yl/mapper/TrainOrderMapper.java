package com.yl.mapper;

import java.util.List;
import java.util.Map;

import com.yl.bean.Order;
import com.yl.bean.OrderBuyInfo;
import com.yl.bean.OrderBuyStation;
import com.yl.bean.OrderSellInfo;
import com.yl.bean.OrderSellStation;
import com.yl.bean.OrderStation;
import com.yl.bean.OrderTrade;

public interface TrainOrderMapper {
	/**
	 * 
	 * @description 保存求购信息主表
	 * @param
	 * @return
	 */
	void saveOrderBuy(Map<String, Object> map);

	/**
	 * 
	 * @description 修改求购信息状态
	 * @param
	 * @return
	 */
	void updateOrderBuy(Map<String, Object> map);

	/**
	 * 
	 * @description 保存求购信息附属表车站信息
	 * @param
	 * @return
	 */
	void saveOrderBuyStation(Map<String, Object> map);

	/**
	 * 
	 * @description 批量保存求购信息附属表车站信息
	 * @param
	 * @return
	 */
	void insertOrderBuyStation(List<OrderBuyStation> list);

	/**
	 * 
	 * @description 获取求购信息（详情分页）
	 * @param
	 * @return
	 */
	List<OrderBuyInfo> getOrderBuyInfo(Map<String, Object> map);

	/**
	 * 
	 * @description 获取求购信息（搜索）
	 * @param
	 * @return
	 */
	List<OrderBuyInfo> getOrderBuyForSearch(Map<String, Object> map);

	/**
	 * 
	 * @description 保存座位信息主表
	 * @param
	 * @return
	 */
	void saveOrderSell(Map<String, Object> map);

	/**
	 * 
	 * @description 修改求购信息状态
	 * @param
	 * @return
	 */
	void updateOrderSell(Map<String, Object> map);

	/**
	 * 
	 * @description 批量保存求购信息附属表车站信息
	 * @param
	 * @return
	 */
	void insertOrderSellStation(List<OrderSellStation> list);

	/**
	 * 
	 * @description 获取求购信息（个人分页）
	 * @param
	 * @return
	 */
	List<OrderSellInfo> getOrderSellInfo(Map<String, Object> map);

	/**
	 * 
	 * @description 获取求购信息（搜索）
	 * @param
	 * @return
	 */
	List<OrderSellInfo> getOrderSellForSearch(Map<String, Object> map);

	/**
	 * 
	 * @description 座位附属表修改
	 * @param
	 * @return
	 */
	void updateOrderSellStation(Map<String, Object> map);

	/**
	 * 
	 * @description 座位附属表查询
	 * @param
	 * @return
	 */

	List<OrderSellStation> getOrderSellStation(Map<String, Object> map);

	/**
	 * 
	 * @description 校验是否相同一天，相同车次，相同座位，相同车厢，途径站点是否相同
	 * @param
	 * @return
	 */
	List<OrderSellInfo> checkOrderSell(Map<String, Object> map);

	/**
	 * 
	 * @description 购买订单查询
	 * @param
	 * @return
	 */
	List<Order> getOrder(Map<String, Object> map);

	/**
	 * 
	 * @description 订单附属表
	 * @param
	 * @return
	 */
	List<OrderStation> getOrderStation(Map<String, Object> map);

	/**
	 * 
	 * @description 订单入库
	 * @param
	 * @return
	 */
	void saveOrder(Map<String, Object> map);

	/**
	 * 
	 * @description 订单附属入库
	 * @param
	 * @return
	 */
	void saveOrderStation(Map<String, Object> map);

	/**
	 * 
	 * @description 订单修改
	 * @param
	 * @return
	 */
	void updateOrder(Map<String, Object> map);

	/**
	 * 
	 * @description 首页最新发布
	 * @param
	 * @return
	 */
	List<OrderSellInfo> getOrderSellForMain(Map<String, Object> map);

	/**
	 * 
	 * @description 当天可交易的座位信息
	 * @param
	 * @return
	 */
	List<OrderTrade> getOrderTradeInfo(Map<String, Object> map);
	/**
	 * 
	 * @description 获取未关闭座位信息（但时间已关闭的）
	 * @param
	 * @return
	 */
	List<OrderTrade> getOrderCloseTradeInfo(Map<String, Object> map);
	
	
	Integer getOrderRefund(Map<String, Object> map);
}
