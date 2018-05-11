package com.guse.apple_reverse.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.guse.apple_reverse.dao.model.AppleIdTable;

@Repository
public interface AppleIdTableDao {
	
	
	/** 
	* @Title: findCount 
	* @Description: 获取条数 
	* @param @return
	* @return int 
	* @throws 
	*/
	@Select("select count(1) from ab_apple_id_table t where query_status=0")
	public int findCount();
	
	/** 
	* @Title: findAllImport 
	* @Description: 查询状态为导入的信息 
	* @param @return
	* @return List<AppleIdTable> 
	* @throws 
	*/
	@Select("select * from ab_apple_id_table t where query_status=0 limit ${limit}")
	public List<AppleIdTable> findAllImport(@Param("limit")int limit);
	
	/** 
	* @Title: batchUpdateQueryStatus 
	* @Description: 批量修改数据状态 
	* @param @param query_status
	* @param @param ids
	* @return void 
	* @throws 
	*/
	@Update("update ab_apple_id_table set query_status=#{query_status},account_status='' where id in(${ids})")
	public void batchUpdateQueryStatus(@Param("query_status")int query_status,@Param("ids") String ids);
	
	/** 
	* @Title: updateQueryStatus 
	* @Description: 修改状态 
	* @param @param apple
	* @return void 
	* @throws 
	*/
	@Update("update ab_apple_id_table set query_status=#{query_status},account_status=#{account_status} where id=#{id}")
	public void updateQueryStatus(AppleIdTable apple);
	
	/** 
	* @Title: getById 
	* @Description: 根据id查询 
	* @param @param id
	* @param @return
	* @return AppleIdTable 
	* @throws 
	*/
	@Select("select * from ab_apple_id_table t where id=#{id}")
	public AppleIdTable getById(@Param("id") int id);

}
