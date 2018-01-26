package com.yl.Pojo;

import java.math.BigDecimal;

public class OrderGuidePojo {
	private BigDecimal price;
	private Integer guideNum;
	private Integer guideUseNum;
	private Integer guideNoUseNum;
	private Integer guideRebackNum;
	private String orderNo;
	private String scenicName;

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Integer getGuideNum() {
		return guideNum;
	}

	public void setGuideNum(Integer guideNum) {
		this.guideNum = guideNum;
	}

	public Integer getGuideUseNum() {
		return guideUseNum;
	}

	public void setGuideUseNum(Integer guideUseNum) {
		this.guideUseNum = guideUseNum;
	}

	public Integer getGuideNoUseNum() {
		return guideNoUseNum;
	}

	public void setGuideNoUseNum(Integer guideNoUseNum) {
		this.guideNoUseNum = guideNoUseNum;
	}

	public Integer getGuideRebackNum() {
		return guideRebackNum;
	}

	public void setGuideRebackNum(Integer guideRebackNum) {
		this.guideRebackNum = guideRebackNum;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getScenicName() {
		return scenicName;
	}

	public void setScenicName(String scenicName) {
		this.scenicName = scenicName;
	}

}
