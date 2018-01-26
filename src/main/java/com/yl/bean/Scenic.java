package com.yl.bean;

import java.io.Serializable;
import java.math.BigDecimal;

public class Scenic implements Serializable {

	/**
	 * 景区详情
	 */
	private static final long serialVersionUID = 1L;
	private Integer scenicId;
	private String scenicName;
	private String coverImg;
	private String fileCode;
	private BigDecimal price;
	private BigDecimal usePrice;
	private BigDecimal longTime;

	public BigDecimal getLongTime() {
		return longTime;
	}

	public void setLongTime(BigDecimal longTime) {
		this.longTime = longTime;
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

	public String getCoverImg() {
		return coverImg;
	}

	public void setCoverImg(String coverImg) {
		this.coverImg = coverImg;
	}

	public String getFileCode() {
		return fileCode;
	}

	public void setFileCode(String fileCode) {
		this.fileCode = fileCode;
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

}
