package com.guse.apple_reverse.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.guse.apple_reverse.dao.model.AppleSercurity;

@Repository
public interface AppleSercurityDao {
	
	/** 
	* @Title: findCount 
	* @Description: 获取未处理条数 
	* @param @return
	* @return int 
	* @throws 
	*/
	@Select("select count(1) from ab_apple_sercurity t where status=${status}")
	public int findCount(@Param("status")int status);
	
	/** 
	* @Title: findAllImport 
	* @Description: 查询状态为导入的信息 
	* @param @return
	* @return List<AppleIdTable> 
	* @throws 
	*/
	@Select("select * from ab_apple_sercurity t where id=#{id}")
	public AppleSercurity getById(@Param("id")int id);
	
	/** 
	* @Title: findAllImport 
	* @Description: 查询状态为导入的信息 
	* @param @return
	* @return List<AppleIdTable> 
	* @throws 
	*/
	@Select("select * from ab_apple_sercurity t where status=${status} limit ${limit}")
	public List<AppleSercurity> findAllImport(@Param("status")int status, @Param("limit")int limit);
	
	/** 
	* @Title: batchUpdateQueryStatus 
	* @Description: 批量修改数据状态 
	* @param @param query_status
	* @param @param ids
	* @return void 
	* @throws 
	*/
	@Update("update ab_apple_sercurity set status=#{status},status_comment='' where id in(${ids})")
	public void batchUpdateStatus(@Param("status")int query_status,@Param("ids") String ids);

	
	/** 
	* @Title: updateStatus 
	* @Description: 修改状态 
	* @param @param apple
	* @return void 
	* @throws 
	*/
	@Update("update ab_apple_sercurity set status=#{status},status_comment=#{status_comment},modified_time=#{modified_time}"
			+ " where id=#{id}")
	public void updateStatus(AppleSercurity apple);
}
