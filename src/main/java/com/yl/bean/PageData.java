package com.yl.bean;

/**
 * 该类描述了一个分页数据<br/> 
 * list中是查询的数据集合<br/>
 * pageInfo则描述了附加的页相关的信息<br/>
*/

import java.util.List;
import java.util.Map;

public class PageData<T>{

	private List<T> list;  //当前页的数据信息
	private PageInfo pageInfo; //当前页的信息
	private List<Double> countList;//返回当前查询总数
	private int totalAmount;
	private double totalMoney;
	private Map<String,Object> dataMap;
	public List<T> getList() {
		return list;
	}
	public void setList(List<T> list) {
		this.list = list;
	}
	public PageInfo getPageInfo() {
		return pageInfo;
	}
	public void setPageInfo(PageInfo pageInfo) {
		this.pageInfo = pageInfo;
	}
	public List<Double> getCountList() {
		return countList;
	}
	public void setCountList(List<Double> countList) {
		this.countList = countList;
	}
	public int getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(int totalAmount) {
		this.totalAmount = totalAmount;
	}
	public double getTotalMoney() {
		return totalMoney;
	}
	public void setTotalMoney(double totalMoney) {
		this.totalMoney = totalMoney;
	}
	public Map<String, Object> getDataMap() {
		return dataMap;
	}
	public void setDataMap(Map<String, Object> dataMap) {
		this.dataMap = dataMap;
	}
	
}

