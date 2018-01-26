package com.yl.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.yl.bean.Admin;
import com.yl.bean.ExpenseGuideLog;
import com.yl.bean.Order;
import com.yl.bean.RefundReason;
import com.yl.bean.Version;

import net.sf.json.JSONObject;

public interface HelpMapper { 
 	/** 获取热点问题 */
	List<Map<String, Object>> getFeedback(Map interimMap);
	/** 新增用户反馈  */
	void insertFeedback(Map interimMap); 
	/** 新增用户反馈图片  */
	void insertFeedbackImg(List<Map> interimMap); 
	
	Version getVersion(Map<String, Object> map);
 }
