package com.guse.platform.common.page;

/**
 * @author nbin
 * @date 2017年7月17日 下午8:46:22 
 * @version V1.0
 */
public class PageBean {

	private int pageSize = 20;

	private int pageNo = 1;

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
}
