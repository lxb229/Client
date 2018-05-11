package com.guse.four_one_nine.controller.page;

import org.apache.commons.lang3.StringUtils;


/** 
* @ClassName: RequestPage 
* @Description: 页面表格请求对象
* @author Fily GUSE
* @date 2018年1月8日 下午4:04:59 
*  
*/
public class RequestPage {

	// 关键字
	private String text;
	// 选项卡
	private Integer type;
	// 页码
	private Integer page = 1;
	// 显示条数
	private Integer showNum = 10;
	
	public String getText() {
		if(StringUtils.isNotBlank(text)) {
			text = text.trim();
		}
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	public Integer getShowNum() {
		return showNum;
	}
	public void setShowNum(Integer showNum) {
		this.showNum = showNum;
	}
	
}
