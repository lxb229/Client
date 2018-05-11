package com.guse.stock.dao.model;

/** 
* @ClassName: StockCer 
* @Description: 凭证信息
* @author Fily GUSE
* @date 2017年8月30日 下午8:05:11 
*  
*/
public class StockCer {
	/** 
	* @Fields id : 主键
	*/
	private Long id;
	/** 
	* @Fields stock_id : 库存id
	*/
	private Long stock_id;
	/** 
	* @Fields ios_6 : ios凭证
	*/
	private String ios_6;
	/** 
	* @Fields ios_7 : ios7凭证
	*/
	private String ios_7;
	/** 
	* @Fields hash : md5(ios_6)防止凭证重复
	*/
	private String hash;
	/** 
	* @Fields tran_id : 凭证交易ID
	*/
	private String tran_id;
	/** 
	* @Fields status : 状态，0：未使用，1：已使用
	*/
	public int status = 0;
	/** 
	* @Fields create_time : 生成时间
	*/
	public Long create_time;
	
	
	public Long getStock_id() {
		return stock_id;
	}
	public void setStock_id(Long stock_id) {
		this.stock_id = stock_id;
	}
	public String getIos_6() {
		return ios_6;
	}
	public void setIos_6(String ios_6) {
		this.ios_6 = ios_6;
	}
	public String getIos_7() {
		return ios_7;
	}
	public void setIos_7(String ios_7) {
		this.ios_7 = ios_7;
	}
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	public String getTran_id() {
		return tran_id;
	}
	public void setTran_id(String tran_id) {
		this.tran_id = tran_id;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Long getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Long create_time) {
		this.create_time = create_time;
	}
	
}
