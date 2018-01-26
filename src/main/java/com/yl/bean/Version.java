package com.yl.bean;

/*
 * @author  lqpl66
 * @date 创建时间：2017年6月15日 下午5:20:38 
 * @function     
 */
public class Version {
	private String version;
	private Integer forceUpdate;
	private String apkUrl;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Integer getForceUpdate() {
		return forceUpdate;
	}

	public void setForceUpdate(Integer forceUpdate) {
		this.forceUpdate = forceUpdate;
	}

	public String getApkUrl() {
		return apkUrl;
	}

	public void setApkUrl(String apkUrl) {
		this.apkUrl = apkUrl;
	}

}
