package com.yl.bean;

/*
 * @author  lqpl66
 * @date 创建时间：2017年4月13日 上午10:44:52 
 * @function     
 */
public class WxAuth {
	private String openid;
	private String access_token;
	private String refresh_token;
	private String expires_in;

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public String getRefresh_token() {
		return refresh_token;
	}

	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}

	public String getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(String expires_in) {
		this.expires_in = expires_in;
	}

	@Override
	public String toString() {
		return "WxAuth [openid=" + openid + ", access_token=" + access_token + ", refresh_token=" + refresh_token
				+ ", expires_in=" + expires_in + "]";
	}

}
