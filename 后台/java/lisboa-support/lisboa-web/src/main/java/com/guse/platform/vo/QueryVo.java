package com.guse.platform.vo;

/**
 * 公共查询参数
 * @author nbin
 * @date 2017年7月27日 下午3:10:17 
 * @version V1.0
 */
public class QueryVo implements java.io.Serializable {

	private static final long serialVersionUID = 4590093885758336337L;
	
	//日期范围
	/** 开始日期*/
    private String            startDate;
    /** 结束日期*/
    private String            endDate;
    //多个日期范围
    /**
     * 对比日期  前
     */
    private String 	contrastBefourDate;
    /**
     * 对比日期 后
     */
    private String 	contrastAfterDate;
    //单个日期
    /**
     * 查询某天日期
     */
    private String  somedayDate;
    
    
    //留存相关
    /**
     * 统计的留存类型  RemainTypeEnum
     */
    private Integer remainType;
    
    /**
     * 日 周 月
     */
    private Integer remainDimensionality;
    
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getContrastBefourDate() {
		return contrastBefourDate;
	}
	public void setContrastBefourDate(String contrastBefourDate) {
		this.contrastBefourDate = contrastBefourDate;
	}
	public String getContrastAfterDate() {
		return contrastAfterDate;
	}
	public void setContrastAfterDate(String contrastAfterDate) {
		this.contrastAfterDate = contrastAfterDate;
	}
	public String getSomedayDate() {
		return somedayDate;
	}
	public void setSomedayDate(String somedayDate) {
		this.somedayDate = somedayDate;
	}
	public Integer getRemainType() {
		return remainType;
	}
	public void setRemainType(Integer remainType) {
		this.remainType = remainType;
	}
	public Integer getRemainDimensionality() {
		return remainDimensionality;
	}
	public void setRemainDimensionality(Integer remainDimensionality) {
		this.remainDimensionality = remainDimensionality;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
