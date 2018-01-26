package com.yl.bean;

import com.yl.Utils.CommonDateParseUtil;

/*
 * @author  lqpl66
 * @date 创建时间：2017年11月16日 上午11:35:50 
 * @function     
 */
public class Station {
	private Integer id;
	private String stationName;
	private String stationCode;
	private String stationNameFull;
	private String stationNum;
	private String stationNameAttr;
	private String stationNameAttr1;
   private String addTime;
   
	public String getAddTime() {
	return addTime;
}

public void setAddTime(String addTime) {
	String add = CommonDateParseUtil.date2string(CommonDateParseUtil.string2date(addTime));
	this.addTime = add;
}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

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

	public String getStationNum() {
		return stationNum;
	}

	public void setStationNum(String stationNum) {
		this.stationNum = stationNum;
	}

	public String getStationNameAttr() {
		return stationNameAttr;
	}

	public void setStationNameAttr(String stationNameAttr) {
		this.stationNameAttr = stationNameAttr;
	}

	public String getStationNameAttr1() {
		return stationNameAttr1;
	}

	public void setStationNameAttr1(String stationNameAttr1) {
		this.stationNameAttr1 = stationNameAttr1;
	}

}
