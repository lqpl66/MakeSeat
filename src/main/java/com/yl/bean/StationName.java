package com.yl.bean;

/*
 * @author  lqpl66
 * @date 创建时间：2017年11月14日 下午12:59:50 
 * @function     
 */
public class StationName {
	private String stationName;
	private String stationCode;
	private String stationNameFull;
	private String stationNameAttr;
	private String stationNameAttr1;
	private Integer stationNum;

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

	public String getStationNameFull() {
		return stationNameFull;
	}

	public void setStationNameFull(String stationNameFull) {
		this.stationNameFull = stationNameFull;
	}

	public String getStationNameAttr() {
		return stationNameAttr;
	}

	public void setStationNameAttr(String stationNameAttr) {
		this.stationNameAttr = stationNameAttr;
	}

	public Integer getStationNum() {
		return stationNum;
	}

	public void setStationNum(Integer stationNum) {
		this.stationNum = stationNum;
	}

	public String getStationNameAttr1() {
		return stationNameAttr1;
	}

	public void setStationNameAttr1(String stationNameAttr1) {
		this.stationNameAttr1 = stationNameAttr1;
	}

	@Override
	public String toString() {
		return "StationName [stationName=" + stationName + ", stationCode=" + stationCode + ", stationNameFull="
				+ stationNameFull + ", stationNameAttr=" + stationNameAttr + ", stationNameAttr1=" + stationNameAttr1
				+ ", stationNum=" + stationNum + "]";
	}

}
