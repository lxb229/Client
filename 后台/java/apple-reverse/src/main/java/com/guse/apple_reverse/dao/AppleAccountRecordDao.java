package com.guse.apple_reverse.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.guse.apple_reverse.dao.model.AppleAccountRecord;

@Repository
public interface AppleAccountRecordDao {
	
	/** 
	* @Title: addRecord 
	* @Description: 添加帐号信息 
	* @param @param record
	* @return void 
	* @throws 
	*/
	@Insert("insert into ab_apple_account_record(id, country, payment, family_sharing, bill_time, balance, bill, bill_content, debt, agreement, query_time) "
			+ " values(#{id}, #{country}, #{payment}, #{family_sharing}, #{bill_time}, #{balance}, #{bill}, #{bill_content}, #{debt}, #{agreement}, #{query_time})")
	public void addRecord(AppleAccountRecord record);
	
	/** 
	* @Title: delRecord 
	* @Description: 根据ID 删除记录 
	* @param @param id
	* @return void 
	* @throws 
	*/
	@Delete("delete from ab_apple_account_record where id=#{id}")
	public void delRecord(@Param("id")int id);
	
	/** 
	* @Title: findById 
	* @Description: 根据id查询信息 
	* @param @param id
	* @param @return
	* @return AppleAccountRecord 
	* @throws 
	*/
	@Select("select * from ab_apple_account_record where id=#{id}")
	public AppleAccountRecord findById(@Param("id") int id);
	
	/** 
	* @Title: updateBillContent 
	* @Description: 更新账单详情 
	* @param @param record
	* @return void 
	* @throws 
	*/
	@Update("update ab_apple_account_record set bill_content=#{bill_content},bill=#{bill},last_invoice_date=#{last_invoice_date} "
			+ "where id=#{id}")
	public void updateBillContent(AppleAccountRecord record);

}
