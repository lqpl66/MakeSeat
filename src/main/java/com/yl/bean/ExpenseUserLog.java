package com.yl.bean;

import java.math.BigDecimal;

public class ExpenseUserLog {
	private String expenseUserNo;
	private String serialNo;
	private Integer userId;
	private BigDecimal paymentAmount;
	private Integer useType;
	private Integer paymentType;
	private Integer expenseType;
	private Integer sourceType;
	private String addTime;
	private String remark;
	private Integer status;

	public String getExpenseUserNo() {
		return expenseUserNo;
	}

	public void setExpenseUserNo(String expenseUserNo) {
		this.expenseUserNo = expenseUserNo;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
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

	public Integer getUseType() {
		return useType;
	}

	public void setUseType(Integer useType) {
		this.useType = useType;
	}

	public Integer getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(Integer paymentType) {
		this.paymentType = paymentType;
	}

	public Integer getExpenseType() {
		return expenseType;
	}

	public void setExpenseType(Integer expenseType) {
		this.expenseType = expenseType;
	}

	public Integer getSourceType() {
		return sourceType;
	}

	public void setSourceType(Integer sourceType) {
		this.sourceType = sourceType;
	}

	public String getAddTime() {
		return addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
