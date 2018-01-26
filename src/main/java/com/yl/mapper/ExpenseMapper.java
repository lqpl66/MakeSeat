package com.yl.mapper;
 
import java.util.List;
import java.util.Map;

import com.yl.bean.Admin;
import com.yl.bean.CardExpand;
import com.yl.bean.ExpenseUserLog;
import com.yl.bean.Order;
import com.yl.bean.TemporaryOrder;
 

public interface ExpenseMapper {
	// 保存临时订单
	void saveTemporaryOrder(Map map);
	 
	/**
	 *  
	* <pre>
	* @Title: saveExpenseSystemlog 
	* @Description: 系统平台流水记录的入库 
	* @param map
	* <pre>
	* expenseSystemNo String 流水编号
	* expenseNo       String 用户消费流水编号
	* amount          BigDecimal 金额
	* addTime         String 添加时间
	* </pre>   
	* @return: void
	* </pre>  
	*/
	void saveExpenseSystemlog(Map map);
 
	/**
	 * 
	* <pre>
	* @Title: saveExpenseUserlog 
	* @Description: 用户消费流水记录的保存
	* @param map   
	* <pre>
	* expenseNo     流水号
	* serialNo      第三方流水号
	* employeeId    用户id
	* paymentAmount 实付金额
	* paymentType   交易类型（0支付宝，1钱包）
	* expenseType   流水类型（0充值，1购买座位，2出售座位，3提现，4退款）
	* orderNo       订单号（支付订单，取消订单时用）
	* remark        备注，提现失败时必填项，填写失败原因
	* addTime       支付时间
	* </pre>
	* @return: void 
	* </pre>
	 */ 
	void saveExpenseUserlog(Map map);


	// 查询订单列表（基础表）
	List<Order> getOrder(Map map);
	// 临时订单（用于充值）
	TemporaryOrder getTemporaryOrder(Map map);
	// 修改临时订单表
	void updateTemporaryOrder(Map map);
	// 获取平台管理员，商家管理员信息
	Admin getAdmin(Map map);
	// 订单修改（基础表）
	void updateOrder(Map map);
	void saveExpenseCardlog(Map<String, Object> map);
	// 订单操作记录
	void saveOrderLog(Map map);

// TODO
	// 插入提现账号
	void saveUserPayAccount(Map map);

	// 修改用户余额账户
	void updateUserAmount(Map map);

	// 订单的附属
	List<CardExpand> getCardExpand(Map map);

	// 积分保存操作
	void savescore(Map map);

	// 积分统计 type 1:统计历史总积分
	String gettotalScore(Map map);

	// 消费明细 .
	List<ExpenseUserLog> getexpenselog(Map map);

	// 用户账户明细流水统计.
	String gettotalAmount(Map map);

}
