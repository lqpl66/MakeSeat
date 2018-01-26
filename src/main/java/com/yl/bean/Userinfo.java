package com.yl.bean;

import java.io.Serializable;
import java.math.BigDecimal;

public class Userinfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String employeeCode;
	private String userName;
	private String nickName;
	private String userCode;
	private String headImg;
	private String userPwd;
	private String uuid;
	private String uuidExpiry;
	private Integer leavel;
	private String birthday;
	private Integer gender;
	private String smsCode;
	private String loginTime;
	private String smsPwdExpiry;
	private Integer loginCity;
	private String createTime;
	private Integer useramountId;
	private BigDecimal balance;
	private String payPwd;
	private BigDecimal minAmount;
	private Integer userStatus;
	private Integer status;
	private Integer isOpen;
	private String offReadNum;
	private String offReadTotalNum;
	private String totalScore;
	private Integer addressId;
	private String adprovinceName;
	private String adcityName;
	private String addistrictName;
	private String addressInfo;
	private String adzip;
	private String adrecipientMobile;
	private String adrecipient;
	private String easemobPwd;
	private String customerService;
	private String amount;
	private String device_token;
	private String mbSystemType;

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	@Override
	public String toString() {
		return "Userinfo [id=" + id + ", userName=" + userName + ", nickName=" + nickName + ", userCode=" + userCode
				+ ", headImg=" + headImg + ", userPwd=" + userPwd + ", uuID=" + uuid + ", uuIDExpiry=" + uuidExpiry
				+ ", leavel=" + leavel + ", birthday=" + birthday + ", gender=" + gender + ", smsCode=" + smsCode
				+ ", loginTime=" + loginTime + ", smsPwdExpiry=" + smsPwdExpiry + ", loginCity=" + loginCity
				+ ", createTime=" + createTime + ", useramountId=" + useramountId + ", balance=" + balance + ", payPwd="
				+ payPwd + ", minAmount=" + minAmount + ", userStatus=" + userStatus + ", status=" + status
				+ ", isOpen=" + isOpen + ", offReadNum=" + offReadNum + ", offReadTotalNum=" + offReadTotalNum
				+ ", totalScore=" + totalScore + ", addressId=" + addressId + ", adprovinceName=" + adprovinceName
				+ ", adcityName=" + adcityName + ", addistrictName=" + addistrictName + ", addressInfo=" + addressInfo
				+ ", adzip=" + adzip + ", adrecipientMobile=" + adrecipientMobile + ", adrecipient=" + adrecipient
				+ ", easemobPwd=" + easemobPwd + ", customerService=" + customerService + "]";
	}

	public String getDevice_token() {
		return device_token;
	}

	public void setDevice_token(String device_token) {
		this.device_token = device_token;
	}

	public String getMbSystemType() {
		return mbSystemType;
	}

	public void setMbSystemType(String mbSystemType) {
		this.mbSystemType = mbSystemType;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getUuidExpiry() {
		return uuidExpiry;
	}

	public void setUuidExpiry(String uuidExpiry) {
		this.uuidExpiry = uuidExpiry;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getCustomerService() {
		return customerService;
	}

	public void setCustomerService(String customerService) {
		this.customerService = customerService;
	}

	public String getOffReadTotalNum() {
		return offReadTotalNum;
	}

	public void setOffReadTotalNum(String offReadTotalNum) {
		this.offReadTotalNum = offReadTotalNum;
	}

	public Integer getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(Integer userStatus) {
		this.userStatus = userStatus;
	}

	public String getAdprovinceName() {
		return adprovinceName;
	}

	public void setAdprovinceName(String adprovinceName) {
		this.adprovinceName = adprovinceName;
	}

	public String getAdcityName() {
		return adcityName;
	}

	public void setAdcityName(String adcityName) {
		this.adcityName = adcityName;
	}

	public String getAddistrictName() {
		return addistrictName;
	}

	public void setAddistrictName(String addistrictName) {
		this.addistrictName = addistrictName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getHeadImg() {
		return headImg;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	 
	public Integer getLeavel() {
		return leavel;
	}

	public void setLeavel(Integer leavel) {
		this.leavel = leavel;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public String getSmsCode() {
		return smsCode;
	}

	public void setSmsCode(String smsCode) {
		this.smsCode = smsCode;
	}

	public String getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(String loginTime) {
		this.loginTime = loginTime;
	}

	public String getSmsPwdExpiry() {
		return smsPwdExpiry;
	}

	public void setSmsPwdExpiry(String smsPwdExpiry) {
		this.smsPwdExpiry = smsPwdExpiry;
	}

	public Integer getLoginCity() {
		return loginCity;
	}

	public void setLoginCity(Integer loginCity) {
		this.loginCity = loginCity;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public Integer getUseramountId() {
		return useramountId;
	}

	public void setUseramountId(Integer useramountId) {
		this.useramountId = useramountId;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public String getPayPwd() {
		return payPwd;
	}

	public void setPayPwd(String payPwd) {
		this.payPwd = payPwd;
	}

	public BigDecimal getMinAmount() {
		return minAmount;
	}

	public void setMinAmount(BigDecimal minAmount) {
		this.minAmount = minAmount;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(Integer isOpen) {
		this.isOpen = isOpen;
	}

	public String getOffReadNum() {
		return offReadNum;
	}

	public void setOffReadNum(String offReadNum) {
		this.offReadNum = offReadNum;
	}

	public String getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(String totalScore) {
		this.totalScore = totalScore;
	}

	public Integer getAddressId() {
		return addressId;
	}

	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
	}

	public String getAddressInfo() {
		return addressInfo;
	}

	public void setAddressInfo(String addressInfo) {
		this.addressInfo = addressInfo;
	}

	public String getAdzip() {
		return adzip;
	}

	public void setAdzip(String adzip) {
		this.adzip = adzip;
	}

	public String getAdrecipientMobile() {
		return adrecipientMobile;
	}

	public void setAdrecipientMobile(String adrecipientMobile) {
		this.adrecipientMobile = adrecipientMobile;
	}

	public String getAdrecipient() {
		return adrecipient;
	}

	public void setAdrecipient(String adrecipient) {
		this.adrecipient = adrecipient;
	}

	public String getEasemobPwd() {
		return easemobPwd;
	}

	public void setEasemobPwd(String easemobPwd) {
		this.easemobPwd = easemobPwd;
	}

}
