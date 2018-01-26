package com.yl.bean;
/*
 * @author  lqpl66
 * @date 创建时间：2017年5月9日 下午3:47:11 
 * @function     
 */

import java.math.BigDecimal;

public class ScenicGuideFee {
	private Integer scenicId;
	private String scenicName;
	private BigDecimal price;
	private BigDecimal usePrice;
	private BigDecimal totalCash;

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

	public BigDecimal getTotalCash() {
		return totalCash;
	}

	public void setTotalCash(BigDecimal totalCash) {
		this.totalCash = totalCash;
	}

}
