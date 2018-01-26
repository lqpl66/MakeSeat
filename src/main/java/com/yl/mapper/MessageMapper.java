package com.yl.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.yl.bean.Admin;
import com.yl.bean.ExpenseGuideLog;
import com.yl.bean.Message;
import com.yl.bean.Order;
import com.yl.bean.PushMessageDevice;
import com.yl.bean.RefundReason;

import net.sf.json.JSONObject;

public interface MessageMapper {  
	/** 保存消息 */
	void saveSystemMessage(JSONObject interimMap);
	/** 保存用户消息关联 */
	void saveSystemMessageStatus(JSONObject interimMap);
	/** 保存推送消息关联 */
	void savePushmessagelog(JSONObject interimMap);
	// 设备入库
		void savePushMessageDevice(Map map);
		//获取消息推送驱动信息
		List<PushMessageDevice> getPushMessageDevice(Map map);
		// 设备修改
		void updatePushMessageDevice(Map map);

		// 获取消息一对多
		List<Message> getMessageMore(Map map);

		// 获取未读总数
		Map<String, Object> getMessageNum(Map map);

		// 修改消息
		void updateMessage(Map map);
		// 修改消息
		void updateMessageMore(Map map);
		// 修改系统，活动消息已读，未读
		void updateSystemmessageDeviceStatus(Map map);
		int selectSystemmessageDeviceStatus(Map<String , Object> map);
		// 获取系统，活动消息，人在有效中的条数
		Map getMSGTotalOfNewUser(Map map);
		// 保存系统，活动消息已读，未读
		void saveSystemmessageDeviceStatus(Map map);
 }
