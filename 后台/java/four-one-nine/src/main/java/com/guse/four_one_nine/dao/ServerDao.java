package com.guse.four_one_nine.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.guse.four_one_nine.dao.model.Server;

/** 
* @ClassName: ServerDao 
* @Description: 服务管理
* @author Fily GUSE
* @date 2018年1月4日11:43:54 
*  
*/
@Repository
public interface ServerDao {
	
	/** 
	* @Title: addServer 
	* @Description: 新增服务
	* @param @param server
	* @return void 
	* @throws 
	*/
	@Insert("insert into server(id,publish_user,publish_time,name,"
			+ "price,unit,remarks,picture,classify_id) "
			+ "values(#{id},#{publish_user},#{publish_time},"
			+ "#{name},#{price},#{unit},#{remarks},#{picture},#{classify_id})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	public boolean addServer(Server server);

	
	/** 
	* @Title: getServerOrder 
	* @Description: 查询服务信息
	* @param Id
	* @return void 
	* @throws 
	*/
	@Select("SELECT * FROM `server` "
			+ "WHERE id = #{id}")
	public Server getServer(@Param("id") long id);

	/** 
	* @Title: updateServer 
	* @Description: 服务修改
	* @param @param server
	* @return void 
	* @throws 
	*/
	
	@Update({"<script>"
		+"update `server` set "
		+ "<if test='publish_user != null'>publish_user=#{publish_user},</if>"
		+ "<if test='publish_time != null'>publish_time=#{publish_time},</if>"
		+ "<if test='name != null'>name=#{name},</if>"
		+ "<if test='price != null'>price=#{price},</if>"
		+ "<if test='unit != null'>unit=#{unit},</if>"
		+ "<if test='describe != null'>describe=#{describe},</if>"
		+ "<if test='picture != null'>picture=#{picture},</if>"
		+ "<if test='classify_id != null'>classify_id=#{classify_id},</if>"
		+ "<if test='status != null'>status=#{status},</if>"
			+ " where id=#{id}</script>"})
	public void updateServer(Server server);
	
	/** 
	* @Title: findById 
	* @Description: 根据ID获取服务信息 
	* @param @param id
	* @param @return
	* @return Server 
	* @throws 
	*/
	@Select("select * from server where id=#{id}")
	public Server findById(@Param("id")long id);
	
	/** 
	* @Description: 获取未分类服务 
	* @param @return
	* @return List<Server> 
	* @throws 
	*/
	@Select("select * from server where classify_id IS NULL")
	public List<Server> findUnClassify();
	
	/** 
	* @Title: updateClassify 
	* @Description: 更新服务分类 
	* @param @param classifyId
	* @param @param ids
	* @return void 
	* @throws 
	*/
	@Update("update server set classify_id=#{classifyId} where id in(${ids})")
	public void updateClassify(@Param("classifyId")long classifyId, @Param("ids")String ids);
	
	/** 
	* @Title: serverCount 
	* @Description: 服务信息统计 
	* @param @param serverId
	* @param @return
	* @return Map<String,Object> 
	* @throws 
	*/
	@Select("SELECT COUNT(o.id) sell_num, IFNULL(SUM(o.total),0) total, IFNULL(SUM(o.tip_money),0) tip_money"
			+ ", IFNULL(SUM(IF(o.`status`=1, o.total, 0)),0) gain,IFNULL(SUM(IF(o.`status`=0, o.total, 0)),0) lose "
			+ ",(SELECT count(1) FROM server_like WHERE server_id = 1) like_num "
			+ " FROM `server` t "
			+ " LEFT JOIN server_order o ON o.server_id = t.id "
			+ " WHERE t.id = #{serverId}")
	public Map<String, Integer> serverCount(@Param("serverId")long serverId);
	
}
