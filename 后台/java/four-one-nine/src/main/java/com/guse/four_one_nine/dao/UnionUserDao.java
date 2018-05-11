package com.guse.four_one_nine.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.guse.four_one_nine.dao.model.UnionUser;
/** 
* @ClassName: UnionUserDao 
* @Description: �û����ݽӿ�
* @author Fily GUSE
* @date 2018年1月4日11:43:54 
*  
*/
@Repository
public interface UnionUserDao {
	
	/** 
	* @Title: addUnionUser 
	* @Description: ����û���Ϣ 
	* @param @param unionUser
	* @return void 
	* @throws 
	*/
	@Insert("insert into union_user(union_id,user_id,create_time,creater) "
			+ "values(${union_id},#{user_id},#{create_time},#{creater})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	public void addUnionUser(UnionUser unionUser);
	
	/** 
	* @Description: 获取工会成员列表 
	* @param @param unionId
	* @param @return
	* @return List<UnionUser> 
	* @throws 
	*/
	@Select("select t.*, u.nick_name, u.head_picture "
			+ " from union_user t LEFT JOIN `user` u ON u.user_id = t.user_id "
			+ " where t.union_id=#{unionId}")
	public List<Map<String, Object>> findByUnionId(@Param("unionId")long unionId);

	
	/** 
	* @Description: 根据工会id获取工会成员列表 
	* @param @param unionId
	* @param @return
	* @return List<UnionUser> 
	* @throws 
	*/
	@Select("select t.* from union_user t where union_id=#{unionId}")
	public List<UnionUser> findUnionUser(@Param("unionId")long unionId);
	
	/** 
	* @Description: 判断是否存在工会 
	* @param @param param
	* @param @return
	* @return List<UnionUser> 
	* @throws 
	*/
	@Select("select * from union_user where ${param}")
	public List<UnionUser> existUnion(@Param("param")String param);

	/** 
	* @Description: 删除工会成员 
	* @param @param uu
	* @return void 
	* @throws 
	*/
	@Delete("delete from `union_user` where id=#{id}")
	public void deleteUnionUser(UnionUser uu);
	
	/** 
	* @Description: 根据工会删除工会成员 
	* @param @param ids
	* @return void 
	* @throws 
	*/
	@Delete("delete from `union_user` where union_id in(${ids})")
	public void deleteByUnion(@Param("ids")String ids);
	
	
}
