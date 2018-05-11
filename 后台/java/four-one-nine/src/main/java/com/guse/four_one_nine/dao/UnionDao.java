package com.guse.four_one_nine.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.guse.four_one_nine.dao.model.Union;

/** 
* @ClassName: UnionDao 
* @Description: �û����ݽӿ�
* @author Fily GUSE
* @date 2018年1月4日11:43:54 
*  
*/
@Repository
public interface UnionDao {
	
	/** 
	* @Title: addUnion 
	* @Description: 新增工会
	* @param @param user
	* @return void 
	* @throws 
	*/
	@Insert("insert into `union`(union_name,union_logo,clo,income_ratio_clo,income_ratio_member,status,create_time,creater) "
			+ "values(#{union_name},#{union_logo},#{clo},#{income_ratio_clo},#{income_ratio_member},#{status},#{create_time},#{creater})")
	@Options(useGeneratedKeys = true, keyProperty = "union_id")
	public void addUnion(Union union);
	
	/** 
	* @Description: 修改工会信息 
	* @param @param union
	* @return void 
	* @throws 
	*/
	@Update({"<script>"
			+ "update `union` set union_id=#{union_id}"
			+ "<if test='union_name != null'>,union_name=#{union_name}</if>"
			+ "<if test='union_logo != null'>,union_logo=#{union_logo}</if>"
			+ "<if test='clo != null'>,clo=#{clo}</if>"
			+ "<if test='income_ratio_clo != null'>,income_ratio_clo=#{income_ratio_clo}</if>"
			+ "<if test='income_ratio_member != null'>,income_ratio_member=#{income_ratio_member}</if>"
			+ "<if test='status != null'>,status=#{status}</if>"
			+ "<if test='update_time != null'>,update_time=#{update_time}</if>"
			+ "<if test='updater != null'>,updater=#{updater}</if>"
			+ " where union_id=#{union_id}</script>"})
	public void updateUnion(Union union);
	
	/** 
	* @Description: 根据id获取信息 
	* @param @param id
	* @param @return
	* @return Union 
	* @throws 
	*/
	@Select("SELECT * FROM `union` WHERE union_id =#{id}")
	public Union getById(@Param("id")long id);
	
	/** 
	* @Description: 获取工会集合 
	* @param @param ids
	* @param @return
	* @return List<Union> 
	* @throws 
	*/
	@Select("select * from `union` WHERE union_id in(${ids})")
	public List<Union> findByIds(@Param("ids")String ids);
	
	/** 
	* @Description: 修改成删除状态 
	* @param @param ids
	* @return void 
	* @throws 
	*/
	@Update("update `union` set status=0 where union_id in(${ids})")
	public void updateDelete(@Param("ids") String ids);
	
	/** 
	* @Description: 更新工会会长 
	* @param @param id
	* @param @param userId
	* @param @return
	* @return int 
	* @throws 
	*/
	@Update("update `union` set clo=#{userId} where union_id=#{unionId}")
	public int updateClo(@Param("unionId")long unionId, @Param("userId")Long userId);

}
