package com.yl.bean;

/*
 * @author  lqpl66
 * @date 创建时间：2017年11月16日 上午11:58:04 
 * @function     
 */
public class StationTrain {
	private Integer id;
	private String station_train_code;
	private String train_no;
	private String date;
	private String stationFlag;
	private String fromStationName;
	private String fromStationCode;
	private String toStationName;
	private String toStationCode;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getStation_train_code() {
		return station_train_code;
	}

	public void setStation_train_code(String station_train_code) {
		this.station_train_code = station_train_code;
	}

	public String getTrain_no() {
		return train_no;
	}

	public void setTrain_no(String train_no) {
		this.train_no = train_no;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getStationFlag() {
		return stationFlag;
	}

	public void setStationFlag(String stationFlag) {
		this.stationFlag = stationFlag;
	}

	public String getFromStationName() {
		return fromStationName;
	}

	public void setFromStationName(String fromStationName) {
		this.fromStationName = fromStationName;
	}

	public String getFromStationCode() {
		return fromStationCode;
	}

	public void setFromStationCode(String fromStationCode) {
		this.fromStationCode = fromStationCode;
	}

	public String getToStationName() {
		return toStationName;
	}

	public void setToStationName(String toStationName) {
		this.toStationName = toStationName;
	}

	public String getToStationCode() {
		return toStationCode;
	}

	public void setToStationCode(String toStationCode) {
		this.toStationCode = toStationCode;
	}

}
