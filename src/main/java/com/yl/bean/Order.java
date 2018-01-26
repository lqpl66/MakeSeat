package com.yl.bean;

import java.math.BigDecimal;

import com.yl.Utils.CommonDateParseUtil;

public class Order {
	private Integer id;
	private String orderNo;
	private String orderSellNo;
	private Integer employeeId;
	private String headImg;
	private BigDecimal acount;
	private Integer state;
	private String addTime;
	private String modifyTime;
	private String loseTime;
	private String seatName;
	private Integer trainNum;
	private String employeeCode;
	private String nickName;
	private Integer idCardAuthentication;
	private Integer carriageNum;
	private String trainCode;
	private String trainNo;
	private String trainDate;
	private String startStationName;
	private String endStationName;
	private BigDecimal price;
	private String sellEmployeeCode;
	private Integer sellIdCardAuthentication;
	private Integer sellEmployeeId;
	private String sellHeadImg;
	private String nowTime;

	public String getNowTime() {
		return nowTime;
	}

	public void setNowTime(String nowTime) {
		this.nowTime = nowTime;
	}

	public String getSellEmployeeCode() {
		return sellEmployeeCode;
	}

	public void setSellEmployeeCode(String sellEmployeeCode) {
		this.sellEmployeeCode = sellEmployeeCode;
	}

	public Integer getSellIdCardAuthentication() {
		return sellIdCardAuthentication;
	}

	public void setSellIdCardAuthentication(Integer sellIdCardAuthentication) {
		this.sellIdCardAuthentication = sellIdCardAuthentication;
	}

	public Integer getSellEmployeeId() {
		return sellEmployeeId;
	}

	public void setSellEmployeeId(Integer sellEmployeeId) {
		this.sellEmployeeId = sellEmployeeId;
	}

	public String getSellHeadImg() {
		return sellHeadImg;
	}

	public void setSellHeadImg(String sellHeadImg) {
		this.sellHeadImg = sellHeadImg;
	}

	public String getHeadImg() {
		return headImg;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getOrderSellNo() {
		return orderSellNo;
	}

	public void setOrderSellNo(String orderSellNo) {
		this.orderSellNo = orderSellNo;
	}

	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}

	public BigDecimal getAcount() {
		return acount;
	}

	public void setAcount(BigDecimal acount) {
		this.acount = acount;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getAddTime() {
		return addTime;
	}

	public void setAddTime(String addTime) {
		String aa = CommonDateParseUtil.date2string(CommonDateParseUtil.string2date(addTime));
		this.addTime = aa;
	}

	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		if (modifyTime != null && !modifyTime.isEmpty()) {
			String bb = CommonDateParseUtil.date2string(CommonDateParseUtil.string2date(modifyTime));
			this.modifyTime = bb;
		} else {
			this.modifyTime = modifyTime;
		}
	}

	public String getLoseTime() {
		return loseTime;
	}

	public void setLoseTime(String loseTime) {
		String lose = CommonDateParseUtil.date2string(CommonDateParseUtil.string2date(loseTime));
		this.loseTime = lose;
	}

	public String getSeatName() {
		return seatName;
	}

	public void setSeatName(String seatName) {
		this.seatName = seatName;
	}

	public Integer getTrainNum() {
		return trainNum;
	}

	public void setTrainNum(Integer trainNum) {
		this.trainNum = trainNum;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Integer getIdCardAuthentication() {
		return idCardAuthentication;
	}

	public void setIdCardAuthentication(Integer idCardAuthentication) {
		this.idCardAuthentication = idCardAuthentication;
	}

	public Integer getCarriageNum() {
		return carriageNum;
	}

	public void setCarriageNum(Integer carriageNum) {
		this.carriageNum = carriageNum;
	}

	public String getTrainCode() {
		return trainCode;
	}

	public void setTrainCode(String trainCode) {
		this.trainCode = trainCode;
	}

	public String getTrainNo() {
		return trainNo;
	}

	public void setTrainNo(String trainNo) {
		this.trainNo = trainNo;
	}

	public String getTrainDate() {
		return trainDate;
	}

	public void setTrainDate(String trainDate) {
		this.trainDate = trainDate;
	}

	public String getStartStationName() {
		return startStationName;
	}

	public void setStartStationName(String startStationName) {
		this.startStationName = startStationName;
	}

	public String getEndStationName() {
		return endStationName;
	}

	public void setEndStationName(String endStationName) {
		this.endStationName = endStationName;
	}

}
