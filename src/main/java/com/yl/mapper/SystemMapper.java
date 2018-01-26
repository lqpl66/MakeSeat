package com.yl.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.yl.bean.Admin;
import com.yl.bean.ExpenseGuideLog;
import com.yl.bean.Order;
import com.yl.bean.RefundReason;

import net.sf.json.JSONObject;

public interface SystemMapper {
	void saveLogRecord(Map<String, Object> paramsMap);
    Map<String, Object> selectLogRecord(Map<String, Object> paramsMap);
    List<Map<String,Object>> selectProvince(Map<String, Object> paramsMap);
    List<Map<String,Object>> selectCity(Map<String, Object> paramsMap);

	public List<Map<String, Object>> volidLogin(Map<String, Object> params);

	 List<Map<String,Object>> getAllDeviceInfo(Map<String, Object> paramsMap);
	   
}
