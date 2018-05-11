package com.guse.four_one_nine.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.guse.four_one_nine.dao.model.ServerComment;

/** 
* @ClassName: ServerCommentDao 
* @Description: 服务评价
* @author Fily GUSE
* @date 2018年1月4日11:43:54 
*  
*/
@Repository
public interface ServerCommentDao {
	
	/** 
	* @Title: addServerComment 
	* @Description:新增服务评价
	* @param @param serverComment
	* @return void 
	* @throws 
	*/
	@Insert("insert into server_comment values(server_id,comment_user,content,comment_time)"+
			"#{server_id},#{comment_user},#{content},#{comment_time})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	public void addServerComment(ServerComment serverComment);
	
	/** 
	* @Title: findByServerId 
	* @Description: 获取服务评价 
	* @param @param serverId
	* @param @return
	* @return List<Map<String,Object>> 
	* @throws 
	*/
	@Select("SELECT t.id, u.nick_name, t.comment_time, t.content"
			+ " FROM server_comment t LEFT JOIN `user` u ON u.user_id = t.comment_user"
			+ " WHERE t.server_id = #{serverId}")
	public List<Map<String, Object>> findByServerId(@Param("serverId")long serverId);

}
