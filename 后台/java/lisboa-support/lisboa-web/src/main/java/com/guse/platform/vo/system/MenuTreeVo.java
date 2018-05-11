package com.guse.platform.vo.system;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 菜单tree vo
 * @author Administrator
 * @date 2017年7月18日 下午5:17:53 
 * @version V1.0
 */
public class MenuTreeVo implements Serializable {

	private static final long serialVersionUID = 8489133837322226021L;
	
	/**
	 * id
	 */
	private String id;
	
	/**
	 * 父级id
	 */
	private String parent;
	/**
	 * 标题
	 */
	private String text;
	
	/**
	 * tree图标
	 */
	private String icon;
	
	/**
	 * 排序
	 */
	private int sort;
	
	/**
	 * URL
	 */
	private String url;
		
	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}
	
}
