package com.yl.bean;

import com.yl.Utils.CommonDateParseUtil;

/*
 * @author  lqpl66
 * @date 创建时间：2017年12月1日 下午2:23:08 
 * @function     
 */
public class OrderSellStation {
	private Integer id;
	private String orderSellNo;
	private Integer fromStationId;
	private String fromStationName;
	private String fromStationCode;
	private Integer isSell;
	private Integer toStationId;
	private String toStationName;
	private String toStationCode;
	private String diffrentDay;
	private String startTime;
	private String arriveTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOrderSellNo() {
		return orderSellNo;
	}

	public void setOrderSellNo(String orderSellNo) {
		this.orderSellNo = orderSellNo;
	}

	public Integer getFromStationId() {
		return fromStationId;
	}

	public void setFromStationId(Integer fromStationId) {
		this.fromStationId = fromStationId;
	}

	public Integer getToStationId() {
		return toStationId;
	}

	public void setToStationId(Integer toStationId) {
		this.toStationId = toStationId;
	}

	public String getFromStationName() {
		return fromStationName;
	}

	public void setFromStationName(String fromStationName) {
		this.fromStationName = fromStationName;
	}

	public String getFromStationCode() {
		return fromStationCode;
	}

	public void setFromStationCode(String fromStationCode) {
		this.fromStationCode = fromStationCode;
	}

	public String getToStationName() {
		return toStationName;
	}

	public void setToStationName(String toStationName) {
		this.toStationName = toStationName;
	}

	public String getToStationCode() {
		return toStationCode;
	}

	public void setToStationCode(String toStationCode) {
		this.toStationCode = toStationCode;
	}

	public String getDiffrentDay() {
		return diffrentDay;
	}

	public void setDiffrentDay(String diffrentDay) {
		this.diffrentDay = diffrentDay;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		String aa = CommonDateParseUtil.hhmmTostring(CommonDateParseUtil.string2hhmm(startTime));
		this.startTime = aa;
	}

	public String getArriveTime() {
		return arriveTime;
	}

	public void setArriveTime(String arriveTime) {
		String bb = CommonDateParseUtil.hhmmTostring(CommonDateParseUtil.string2hhmm(arriveTime));
		this.arriveTime = bb;
	}

	public Integer getIsSell() {
		return isSell;
	}

	public void setIsSell(Integer isSell) {
		this.isSell = isSell;
	}

}
