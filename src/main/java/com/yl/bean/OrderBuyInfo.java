package com.yl.bean;

import java.math.BigDecimal;
import java.util.List;

import com.yl.Utils.CommonDateParseUtil;

/*
 * @author  lqpl66
 * @date 创建时间：2017年11月23日 下午2:05:14 
 * @function     
 */
public class OrderBuyInfo {
	private String employeeId;
	private String headImg;
	private String orderBuyNo;
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
	private Integer num;

	public String getHeadImg() {
		return headImg;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getOrderBuyNo() {
		return orderBuyNo;
	}

	public void setOrderBuyNo(String orderBuyNo) {
		this.orderBuyNo = orderBuyNo;
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
