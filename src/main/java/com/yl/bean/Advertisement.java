package com.yl.bean;

/*
 * @author  lqpl66
 * @date 创建时间：2017年12月11日 上午10:20:02 
 * @function     
 */
public class Advertisement {

	private String title;
	private String url;
	private Integer grade;
	private String img;
	private Integer isSkip;

	public Integer getIsSkip() {
		return isSkip;
	}

	public void setIsSkip(Integer isSkip) {
		this.isSkip = isSkip;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getGrade() {
		return grade;
	}

	public void setGrade(Integer grade) {
		this.grade = grade;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

}
