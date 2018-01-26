package com.yl.bean;

/*
 * @author  lqpl66
 * @date 创建时间：2017年11月22日 下午2:23:25 
 * @function     
 */
public class OrderBuyStation {
	private Integer id;
	private String orderBuyNo;
	private Integer stationId;
	private String stationName;
	private String stationCode;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOrderBuyNo() {
		return orderBuyNo;
	}

	public void setOrderBuyNo(String orderBuyNo) {
		this.orderBuyNo = orderBuyNo;
	}

	public Integer getStationId() {
		return stationId;
	}

	public void setStationId(Integer stationId) {
		this.stationId = stationId;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public String getStationCode() {
		return stationCode;
	}

	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}

}
