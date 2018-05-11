package com.guse.platform.common.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;


/**
 * 分页处理
 * @author nbin
 * @date 2017年7月17日 上午9:35:16 
 * @version V1.0
 */
public class PageResult<T> implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    public static final int   PAGE_NO          = 1;
    public static final int   PAGE_SIZE        = 20;

    private Integer           pageNo;               //当前页
    private Integer           pageSize;             //每页记录数
    private Integer           pageCount;            //总页数
    private Long              recordCount;          //总记录数
    private List<T>           list;                 //数据集
    
    private Integer           startIndex       = 0; //limit
    private String            orderByClause = null; //排序字段设置
    
    
    public PageResult(){}
    /**
     *  分页
     * @param pageNo
     * @param pageSize
     * @param recordCount
     */
    public PageResult(Integer pageNo, Integer pageSize ,Long recordCount) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.recordCount = recordCount;
        if(recordCount != null && recordCount > pageSize){
        	this.pageCount = (int) (recordCount / pageSize);
        }else{
        	this.pageCount = 1;
        }
        startIndex = (this.pageNo - 1) * pageSize;
    }
    
    /**
     * 可以设置排序字段
     * @param pageNo
     * @param pageSize
     * @param recordCount
     * @param orderBy
     */
    public PageResult(Integer pageNo, Integer pageSize,Long recordCount,String orderBy) {
    	if (pageNo == null || pageNo < 1) {
            pageNo = PAGE_NO;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = PAGE_SIZE;
        }
        this.pageSize = pageSize;
        this.pageNo = pageNo;
        this.recordCount = recordCount.longValue();
        if(recordCount != null && recordCount > pageSize){
        	this.pageCount = (int) (recordCount / pageSize) + 1;
        }else{
        	this.pageCount = 1;
        }
        	
        if(StringUtils.isNotBlank(orderBy)){
        	 this.orderByClause = orderBy;
        }
        
        startIndex = (this.pageNo - 1) * pageSize;
    }
    
    /**
     * 空的分页对象
     * @Title: initNullPage 
     * @param @return 
     * @return PageResult<T>
     */
    public PageResult<T> initNullPage() {
        PageResult<T> pageResult = new PageResult<T>(PAGE_NO, PAGE_SIZE, 0L);
        pageResult.setList(new ArrayList<T>());
        return pageResult;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
    	if (pageNo < 1) {
            pageNo = PAGE_NO;
        }
        this.pageNo = pageNo;
    }

    public Integer getRecordCount() {
        if (recordCount != null) {
            return recordCount.intValue();
        }
        return 0;
    }

    public void setRecordCount(Long recordCount) {
        this.recordCount = recordCount;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public Integer getPageCount() {
        if (pageCount != null) {
            return pageCount.intValue();
        }
        return 0;
    }
    
    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public Integer getCurrentPage() {
        return this.pageNo;
    }
    
    public Integer getTotalCount() {
        if (recordCount != null) {
            return this.recordCount.intValue();
        }
        return 0;
    }

	public Integer getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(Integer startIndex) {
		this.startIndex = startIndex;
	}

	public String getOrderByClause() {
		return orderByClause;
	}

	public void setOrderByClause(String orderByClause) {
		this.orderByClause = orderByClause;
	}
    
    
}
