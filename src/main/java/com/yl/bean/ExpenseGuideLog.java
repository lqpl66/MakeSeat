package com.yl.bean;

import java.math.BigDecimal;

/*
 * @author  lqpl66
 * @date 创建时间：2017年5月11日 下午4:25:15 
 * @function     
 */
public class ExpenseGuideLog {
	private String expenseGuideNo;
	private String serialNo;
	private Integer userId;
	private BigDecimal paymentAmount;
	private Integer userType;
	private Integer paymentType;
	private String orderNo;
	private String remark;
	private String openID;
	private String refundNo;
	private Integer scenicId;
	private Integer operateId;
	private Integer refundReasonId;
	private BigDecimal deductionPrice;
	private Integer expenseType;

	public String getExpenseGuideNo() {
		return expenseGuideNo;
	}

	public void setExpenseGuideNo(String expenseGuideNo) {
		this.expenseGuideNo = expenseGuideNo;
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

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public Integer getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(Integer paymentType) {
		this.paymentType = paymentType;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getOpenID() {
		return openID;
	}

	public void setOpenID(String openID) {
		this.openID = openID;
	}

	public String getRefundNo() {
		return refundNo;
	}

	public void setRefundNo(String refundNo) {
		this.refundNo = refundNo;
	}

	public Integer getScenicId() {
		return scenicId;
	}

	public void setScenicId(Integer scenicId) {
		this.scenicId = scenicId;
	}

	public Integer getOperateId() {
		return operateId;
	}

	public void setOperateId(Integer operateId) {
		this.operateId = operateId;
	}

	public Integer getRefundReasonId() {
		return refundReasonId;
	}

	public void setRefundReasonId(Integer refundReasonId) {
		this.refundReasonId = refundReasonId;
	}

	public BigDecimal getDeductionPrice() {
		return deductionPrice;
	}

	public void setDeductionPrice(BigDecimal deductionPrice) {
		this.deductionPrice = deductionPrice;
	}

	public Integer getExpenseType() {
		return expenseType;
	}

	public void setExpenseType(Integer expenseType) {
		this.expenseType = expenseType;
	}

}
