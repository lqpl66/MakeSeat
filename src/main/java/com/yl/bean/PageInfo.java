package com.yl.bean;
/**
 * 描述：该类描述了分页记录集中的关于页的信息
 * @author 百度文库(http://wenku.baidu.com/view/2a634807eff9aef8941e0650.html)
 * @version 1.0 Create at 2012-08-04
 * 修订：张建
 * 修订说明：
 */
public class PageInfo {
	
	private int totalRow;	//总记录数

	private int totalPage;	//总页数
	private int currentPage = 1; //当前页，默认为1
	private int pageSize = 10;   //每页记录数
	private boolean hasPrevious;//是否有前一页
	private boolean hasNext;	// 是否有下一页
	private boolean bof;		//是否到最前
	private boolean eof;		//是否到最后
    
	/**
	 * 构造方法
	 * @param totalRow 总记录数
	 * @param pageSize 页的大小
	 * @param pageNum 页码
     */
	public PageInfo(int totalRow, int pageSize, int pageNum) {
		if(pageSize < 1){
			pageSize = 1;
		}
		if(pageNum < 1){
			pageNum = 1;
		}
		this.totalRow = totalRow;
		this.pageSize = pageSize;
		// 根据页大小和总记录数计算出总页数
		this.totalPage = countTotalPage(this.pageSize, this.totalRow);
		// 修正当前页
		setCurrentPage(pageNum);
		init();
	}
	
	public PageInfo(){}
	
	public void setTotalRow(int totalRow) {
		this.totalRow = totalRow;
	}
	public int getTotalRow(){
		return totalRow;
	}
	
	public int getTotalPage() {
		return totalPage;
	}
	public int getCurrentPage() {
		return this.currentPage;
	}
	public void setCurrentPage(int currentPage) {
        if(currentPage>this.totalPage){
        	this.currentPage=this.totalPage;        	
        }else if (currentPage<1){
        	this.currentPage=1;
        }
        else{
        	this.currentPage=currentPage;
        }
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	// 获取当前页记录的开始索引
	public int getBeginIndex() {
		int beginIndex = (currentPage - 1) * pageSize; // 索引下标从0开始
		return beginIndex;
	}
	
	// 计算总页数
	public int countTotalPage(int pageSize, int totalRow) {
		int totalPage = totalRow % pageSize == 0 ? totalRow / pageSize : totalRow / pageSize + 1;
		return totalPage;
	}
	
	// 返回下一页的页码
	public int getNextPage() {
		if (currentPage + 1 >= this.totalPage) { // 如果当前页已经是最后页 则返回最大页
			return this.totalPage;
		}
		return currentPage + 1; 
	}
	
	// 返回前一页的页码
	public int getPreviousPage() {
		if (currentPage - 1 <= 1) {
			return 1;
		} else {
			return currentPage - 1;
		}
	}
	public boolean isHasPrevious() {
		return hasPrevious;
	}
	public boolean isHasNext() {
		return hasNext;
	}
	public boolean isBof() {
		return bof;
	}
	public boolean isEof() {
		return eof;
	}
	public boolean hasNext() {
		return currentPage < this.totalPage;
	}
	public boolean hasPrevious() {
		return currentPage > 1;
	}
	public boolean isFirst() {
		return currentPage == 1;
	}
	public boolean isLast() {
		return currentPage >= this.totalPage;
	}
	// 初始化信息
	private void init() {
		this.hasNext = hasNext();
		this.hasPrevious = hasPrevious();
		this.bof = isFirst();
		this.eof = isLast();
	}
}
