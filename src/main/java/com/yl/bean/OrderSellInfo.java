package com.yl.bean;

import java.math.BigDecimal;
import java.util.List;

import com.yl.Utils.CommonDateParseUtil;

/*
 * @author  lqpl66
 * @date 创建时间：2017年11月28日 下午4:02:49 
 * @function     
 */
public class OrderSellInfo {
	private Integer employeeId;
	private String orderSellNo;
	private Integer trainId;
	private String trainCode;
	private String trainNo;
	private String trainDate;
	private Integer startStationId;
	private String startStationName;
	private String startStationCode;
	private Integer endStationId;
	private String endStationName;
	private String endStationCode;
	private Integer carriageNum;
	private BigDecimal price;
	private Integer isClose;
	private String addTime;
	private Integer trainNum;
	private String employeeCode;
	private String nickName;
	private Integer idCardAuthentication;
	private String loseTime;
	private Integer isSplit;
	private String assignAmount;
	private Integer isAssign;
	private String seatName;
	private Integer num;
	private List<OrderSellStation> list;
	private List<Order> orderList;
	private String headImg;

	public String getHeadImg() {
		return headImg;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}

	public List<Order> getOrderList() {
		return orderList;
	}

	public void setOrderList(List<Order> orderList) {
		this.orderList = orderList;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public String getSeatName() {
		return seatName;
	}

	public void setSeatName(String seatName) {
		this.seatName = seatName;
	}

	public List<OrderSellStation> getList() {
		return list;
	}

	public void setList(List<OrderSellStation> list) {
		this.list = list;
	}

	public String getLoseTime() {
		return loseTime;
	}

	public void setLoseTime(String loseTime) {
		String lose = CommonDateParseUtil.date2string(CommonDateParseUtil.string2date(loseTime));
		this.loseTime = lose;
	}

	public Integer getIsSplit() {
		return isSplit;
	}

	public void setIsSplit(Integer isSplit) {
		this.isSplit = isSplit;
	}

	public String getAssignAmount() {
		return assignAmount;
	}

	public void setAssignAmount(String assignAmount) {
		this.assignAmount = assignAmount;
	}

	public Integer getIsAssign() {
		return isAssign;
	}

	public void setIsAssign(Integer isAssign) {
		this.isAssign = isAssign;
	}

	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}

	public String getOrderSellNo() {
		return orderSellNo;
	}

	public void setOrderSellNo(String orderSellNo) {
		this.orderSellNo = orderSellNo;
	}

	public Integer getTrainId() {
		return trainId;
	}

	public void setTrainId(Integer trainId) {
		this.trainId = trainId;
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

	public Integer getStartStationId() {
		return startStationId;
	}

	public void setStartStationId(Integer startStationId) {
		this.startStationId = startStationId;
	}

	public String getStartStationName() {
		return startStationName;
	}

	public void setStartStationName(String startStationName) {
		this.startStationName = startStationName;
	}

	public String getStartStationCode() {
		return startStationCode;
	}

	public void setStartStationCode(String startStationCode) {
		this.startStationCode = startStationCode;
	}

	public Integer getEndStationId() {
		return endStationId;
	}

	public void setEndStationId(Integer endStationId) {
		this.endStationId = endStationId;
	}

	public String getEndStationName() {
		return endStationName;
	}

	public void setEndStationName(String endStationName) {
		this.endStationName = endStationName;
	}

	public String getEndStationCode() {
		return endStationCode;
	}

	public void setEndStationCode(String endStationCode) {
		this.endStationCode = endStationCode;
	}

	public Integer getCarriageNum() {
		return carriageNum;
	}

	public void setCarriageNum(Integer carriageNum) {
		this.carriageNum = carriageNum;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Integer getIsClose() {
		return isClose;
	}

	public void setIsClose(Integer isClose) {
		this.isClose = isClose;
	}

	public String getAddTime() {
		return addTime;
	}

	public void setAddTime(String addTime) {
		String add = CommonDateParseUtil.date2string(CommonDateParseUtil.string2date(addTime));
		this.addTime = add;
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

}
