package com.yl.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.yl.bean.Admin;
import com.yl.bean.CardExpand;
import com.yl.bean.ExpenseGuideLog;
import com.yl.bean.ExpenseUserLog;
import com.yl.bean.Order;
import com.yl.bean.RefundReason;
import com.yl.bean.UserPayAccount;
import com.yl.bean.Userinfo;

import net.sf.json.JSONObject;

public interface EmployeeMapper {
	/** 查询人员信息 */
	List<Map<String, Object>> selectEmplyeeInfo(Map paramsMap);

	/** 保存用户信息 */
	int saveEmployeeInfo(Map paramsMap);

	/** 修改用户信息 */
	void updateEmployeeInfo(Map paramsMap);

	/** 查询绑定信息 */
	List<Map<String, Object>> selectBindingAccount(Map paramsMap);

	/** 新增绑定信息 */
	void saveBindingAccount(Map paramsMap);

	/** 修改绑定信息 */
	void updateBindingAccount(Map paramsMap);

	/** 获取账户明细统计 */
	int getAccountDetilTotal(Map paramsMap);

	/** 获取账户明细 */
	List<Map<String, Object>> getAccountDetil(Map paramsMap);

	/** 获取用户绑定设备信息 */
	Map<String, Object> getUserDeviceInfo(Map paramsMap);

	Userinfo Getuserinfo(Map map);

	// 查询提现账号
	List<UserPayAccount> getUserPayAccount(Map map);

}
