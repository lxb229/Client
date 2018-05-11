package com.guse.four_one_nine.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.springframework.stereotype.Repository;

import com.guse.four_one_nine.dao.model.ServerLike;

/** 
* @ClassName: ServerLikeDao 
* @Description: 点赞管理
* @author Fily GUSE
* @date 2018年1月4日11:43:54 
*  
*/
@Repository
public interface ServerLikeDao {
	
	/** 
	* @Title: addServerLike 
	* @Description:新增点赞管理
	* @param @param serverLike
	* @return void 
	* @throws 
	*/
	@Insert("insert into server_like(server_id,like_user,like_time) "
			+ "values(#{server_id},#{like_user},#{like_time})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	public boolean addServerLike(ServerLike serverLike);

}
