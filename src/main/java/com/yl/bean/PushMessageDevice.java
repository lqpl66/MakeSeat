package com.yl.bean;

import java.math.BigDecimal;

public class PushMessageDevice {
	private Integer id;
	private BigDecimal placeX;
	private BigDecimal placeY;
	private String device_token;
	private Integer mbSystemType;
	private Integer operateId;
	private Integer operateType;
	private String modifyTime;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public BigDecimal getPlaceX() {
		return placeX;
	}
	public void setPlaceX(BigDecimal placeX) {
		this.placeX = placeX;
	}
	public BigDecimal getPlaceY() {
		return placeY;
	}
	public void setPlaceY(BigDecimal placeY) {
		this.placeY = placeY;
	}
	public String getDevice_token() {
		return device_token;
	}
	public void setDevice_token(String device_token) {
		this.device_token = device_token;
	}
	public Integer getMbSystemType() {
		return mbSystemType;
	}
	public void setMbSystemType(Integer mbSystemType) {
		this.mbSystemType = mbSystemType;
	}
	public Integer getOperateId() {
		return operateId;
	}
	public void setOperateId(Integer operateId) {
		this.operateId = operateId;
	}
	public Integer getOperateType() {
		return operateType;
	}
	public void setOperateType(Integer operateType) {
		this.operateType = operateType;
	}
	public String getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

}
