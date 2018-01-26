package com.yl.bean;

import java.math.BigDecimal;

import com.yl.Utils.CommonDateParseUtil;

public class OrderTrade {
	private String orderNo;
	private String orderSellNo;
	private String addTime;
	private String seatName;
	private Integer trainNum;
	private Integer carriageNum;
	private String trainCode;
	private String trainDate;
	private String startStationName;
	private String endStationName;
	private BigDecimal acount;
	private Integer buyUserId;
	private Integer SellUserId;
	private String site;
	private Integer state;
	private Integer isSplit;

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public Integer getBuyUserId() {
		return buyUserId;
	}

	public void setBuyUserId(Integer buyUserId) {
		this.buyUserId = buyUserId;
	}

	public Integer getSellUserId() {
		return SellUserId;
	}

	public void setSellUserId(Integer sellUserId) {
		SellUserId = sellUserId;
	}

	public BigDecimal getAcount() {
		return acount;
	}

	public void setAcount(BigDecimal acount) {
		this.acount = acount;
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

	public String getAddTime() {
		return addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
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

	public Integer getIsSplit() {
		return isSplit;
	}

	public void setIsSplit(Integer isSplit) {
		this.isSplit = isSplit;
	}

}
