package com.yl.bean;

import java.math.BigDecimal;

public class TemporaryOrder {
	private Integer id;
	private String tradeNo;
	private String expenseUserNo;
	private Integer userId;
	private BigDecimal paymentAmount;
	private String addTime;

	public String getExpenseUserNo() {
		return expenseUserNo;
	}

	public void setExpenseUserNo(String expenseUserNo) {
		this.expenseUserNo = expenseUserNo;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public BigDecimal getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(BigDecimal paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public String getAddTime() {
		return addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}

}
