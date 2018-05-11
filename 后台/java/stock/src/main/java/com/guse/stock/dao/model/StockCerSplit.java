package com.guse.stock.dao.model;

/** 
* @ClassName: StockCerSplit 
* @Description: 凭证表拆分记录表
* @author Fily GUSE
* @date 2017年9月8日 上午11:08:19 
*  
*/
public class StockCerSplit {
	
	// 主键
	private Long id;
	// 凭证子表下标
	private int cer_index;
	// 凭证子表名称
	private String cer_name;
	// 当前表数据量(新建表时，数据量为0)
	private int count = 0;
	// 表数据上限(默认100万条数据)
	private int ceiling = 1000000;
	// 数据结构源表
	private String source_table;
	
	public StockCerSplit(){
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getCer_index() {
		return cer_index;
	}

	public void setCer_index(int cer_index) {
		this.cer_index = cer_index;
	}

	public String getCer_name() {
		return cer_name;
	}

	public void setCer_name(String cer_name) {
		this.cer_name = cer_name;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getCeiling() {
		return ceiling;
	}

	public void setCeiling(int ceiling) {
		this.ceiling = ceiling;
	}

	public String getSource_table() {
		return source_table;
	}

	public void setSource_table(String source_table) {
		this.source_table = source_table;
	}

}
