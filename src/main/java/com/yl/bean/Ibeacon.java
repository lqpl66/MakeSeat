package com.yl.bean;

/*
 * @author  lqpl66
 * @date 创建时间：2017年9月21日 下午1:33:43 
 * @function     
 */
public class Ibeacon {
	private String mu_mac;// 无线mac地址
	private Integer mu_rssi;// 无线设备信号强度
	private String mac;// 基站mac地址
	private String addTime;// 添加时间
	private String device_time;
	private float x;// 基站所处X轴位置
	private float y;// 基站所处Y轴位置
	private float d;// 无线设备到当前基站的距离

	public String getMu_mac() {
		return mu_mac;
	}

	public void setMu_mac(String mu_mac) {
		this.mu_mac = mu_mac;
	}

	public Integer getMu_rssi() {
		return mu_rssi;
	}

	public void setMu_rssi(Integer mu_rssi) {
		this.mu_rssi = mu_rssi;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getAddTime() {
		return addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}

	public String getDevice_time() {
		return device_time;
	}

	public void setDevice_time(String device_time) {
		this.device_time = device_time;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getD() {
		return d;
	}

	public void setD(float d) {
		this.d = d;
	}

	@Override
	public String toString() {
		return "Ibeacon [mu_mac=" + mu_mac + ", mu_rssi=" + mu_rssi + ", mac=" + mac + ", addTime=" + addTime
				+ ", device_time=" + device_time + ", x=" + x + ", y=" + y + ", d=" + d + "]";
	}
  
}
