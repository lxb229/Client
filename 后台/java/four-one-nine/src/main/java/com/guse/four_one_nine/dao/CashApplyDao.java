package com.guse.four_one_nine.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.guse.four_one_nine.dao.model.CashApply;

/** 
* @ClassName: CashApplyDao 
* @Description: 提现管理
* @author Fily GUSE
* @date 2018年1月4日11:43:54 
*  
*/
@Repository
public interface CashApplyDao {
	
	/** 
	* @Title: addCashApply 
	* @Description: 提现申请
	* @param @param cashApply
	* @return void 
	* @throws 
	*/
	@Insert("insert into cash_apply values(id,user_id,money,phone,account_type,account,apply_time)"+
			"(#{id},#{user_id},#{money},#{phone},#{account_type},#{account},#{apply_time})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	public void addCashApply(CashApply cashApply);

	
	
	/** 
	* @Description: 获取待处理数据信息 
	* @param @param ids
	* @param @return
	* @return List<CashApply> 
	* @throws 
	*/
	@Select("select * from cash_apply where id in(${ids})")
	public List<CashApply> findByIds(@Param("ids")String ids);
	
	/** 
	* @Title: audit 
	* @Description: 提现审核 
	* @param @param ids
	* @param @param status
	* @param @return
	* @return int 
	* @throws 
	*/
	@Update("update cash_apply set status=#{status}, audit_time=now() where id in(${ids})")
	public int audit(@Param("ids")String ids,@Param("status") int status);
	
	/** 
	* @Title: remit 
	* @Description: 打款通知 
	* @param @param ids
	* @param @param status
	* @param @return
	* @return int 
	* @throws 
	*/
	@Update("update cash_apply set status=#{status}, remit_time=now() where id in(${ids})")
	public int remit(@Param("ids")String ids,@Param("status") int status);

}
