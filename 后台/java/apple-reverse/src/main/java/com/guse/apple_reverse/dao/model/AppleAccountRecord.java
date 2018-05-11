package com.guse.apple_reverse.dao.model;

import org.apache.ibatis.type.Alias;


/** 
* @ClassName: AppleAccountRecord 
* @Description: 
* @author Fily GUSE
* @date 2017年11月11日 下午4:10:33 
*  
*/
@Alias("appleAccountRecord")
public class AppleAccountRecord {
	
	/* appleIdTable 主键 */
	private int id;
	/* 国家 */
	private String country;
	/* 支付方式 */
	private String payment;
	/* 家庭共享 */
	private String family_sharing;
	/* 最后一次购买日期 */
	private String bill_time;
	/* 余额 */
	private String balance;
	/* 是否有账单 */
	private String bill;
	/* 最后一次的消费记录时间年份 */
	private String last_invoice_date;
	/* 账单内容 */
	private String bill_content;
	/* 是否有欠款 */
	private String debt;
	/* 是否有协议 */
	private String agreement;
	/* 查询时间 */
	private Long query_time;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getPayment() {
		return payment;
	}
	public void setPayment(String payment) {
		this.payment = payment;
	}
	public String getFamily_sharing() {
		return family_sharing;
	}
	public void setFamily_sharing(String family_sharing) {
		this.family_sharing = family_sharing;
	}
	public String getBill_time() {
		return bill_time;
	}
	public void setBill_time(String bill_time) {
		this.bill_time = bill_time;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getBill() {
		return bill;
	}
	public void setBill(String bill) {
		this.bill = bill;
	}
	public String getBill_content() {
		return bill_content;
	}
	public void setBill_content(String bill_content) {
		this.bill_content = bill_content;
	}
	public String getDebt() {
		return debt;
	}
	public void setDebt(String debt) {
		this.debt = debt;
	}
	public String getAgreement() {
		return agreement;
	}
	public void setAgreement(String agreement) {
		this.agreement = agreement;
	}
	public Long getQuery_time() {
		return query_time;
	}
	public void setQuery_time(Long query_time) {
		this.query_time = query_time;
	}
	public String getLast_invoice_date() {
		return last_invoice_date;
	}
	public void setLast_invoice_date(String last_invoice_date) {
		this.last_invoice_date = last_invoice_date;
	}
	
}
