package com.yl.bean;

import java.math.BigDecimal;

/*
 * @author  lqpl66
 * @date 创建时间：2017年5月13日 下午4:23:40 
 * @function     
 */
public class OrderGuideVo {
	private String orderNo;
	private BigDecimal price;
	private BigDecimal usePrice;
	private Integer scenicId;
	private String scenicName;
	private Integer source;
	private String guideNo;
	private Integer guideStatus;
	private Integer guideIds;

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getUsePrice() {
		return usePrice;
	}

	public void setUsePrice(BigDecimal usePrice) {
		this.usePrice = usePrice;
	}

	public Integer getScenicId() {
		return scenicId;
	}

	public void setScenicId(Integer scenicId) {
		this.scenicId = scenicId;
	}

	public String getScenicName() {
		return scenicName;
	}

	public void setScenicName(String scenicName) {
		this.scenicName = scenicName;
	}

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	public String getGuideNo() {
		return guideNo;
	}

	public void setGuideNo(String guideNo) {
		this.guideNo = guideNo;
	}

	public Integer getGuideStatus() {
		return guideStatus;
	}

	public void setGuideStatus(Integer guideStatus) {
		this.guideStatus = guideStatus;
	}

	public Integer getGuideIds() {
		return guideIds;
	}

	public void setGuideIds(Integer guideIds) {
		this.guideIds = guideIds;
	}

}
