package com.guse.four_one_nine.controller.page;

import java.util.List;
import java.util.Map;

/** 
* @ClassName: BasePage 
* @Description: 信息对象
* @author Fily GUSE
* @date 2018年1月8日 下午3:55:38 
*  
*/
public class ResponsePage {
	
	/** 
	* @Fields data : 数据对象
	*/
	private List<Map<String, Object>> data;
	// 数据总数
	private int total;
	// 总页数
	private int pages = 0;
	// 当前分页数
	private int nowPage = 1;
	
	public List<Map<String, Object>> getData() {
		return data;
	}
	public void setData(List<Map<String, Object>> data) {
		this.data = data;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getPages() {
		return pages;
	}
	public void setPages(int pages) {
		this.pages = pages;
	}
	public int getNowPage() {
		return nowPage;
	}
	public void setNowPage(int nowPage) {
		this.nowPage = nowPage;
	}
	
}
