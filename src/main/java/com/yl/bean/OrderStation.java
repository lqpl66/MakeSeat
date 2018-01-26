package com.yl.bean;

/*
 * @author  lqpl66
 * @date 创建时间：2017年12月6日 下午2:00:57 
 * @function     
 */
public class OrderStation {
	private Integer sellStationId;
	private String orderNo;
	private String orderSellNo;
	private Integer fromStationId;
	private String fromStationName;
	private String fromStationCode;
	private Integer isSell;
	private Integer toStationId;
	private String toStationName;
	private String toStationCode;
	public Integer getSellStationId() {
		return sellStationId;
	}

	public void setSellStationId(Integer sellStationId) {
		this.sellStationId = sellStationId;
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

	public Integer getFromStationId() {
		return fromStationId;
	}

	public void setFromStationId(Integer fromStationId) {
		this.fromStationId = fromStationId;
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

	public Integer getIsSell() {
		return isSell;
	}

	public void setIsSell(Integer isSell) {
		this.isSell = isSell;
	}

	public Integer getToStationId() {
		return toStationId;
	}

	public void setToStationId(Integer toStationId) {
		this.toStationId = toStationId;
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

}
