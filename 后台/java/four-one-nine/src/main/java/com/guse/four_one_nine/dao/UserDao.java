package com.guse.four_one_nine.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.guse.four_one_nine.dao.model.User;

/** 
* @ClassName: UserDao 
* @Description: 用户管理接口
* @author Fily GUSE
* @date 2018年1月4日11:43:54 
*  
*/
@Repository
public interface UserDao {
	
	/** 
	* @Title: addUser 
	* @Description: 用户新增
	* @param @param user
	* @return void 
	* @throws 
	*/
	@Insert("insert into user(user_id,nick_name,phone,head_picture,cover_picture,sex,age,birth_time,user_source,ip,city,registe_time) values"+
	"(#{user_id},#{nick_name},#{phone},#{head_picture},#{cover_picture},#{sex},#{age},#{birth_time},#{user_source},#{ip},#{city},#{registe_time})")
	@Options(useGeneratedKeys = true, keyProperty = "user_id")
	public boolean addUser(User user);
	

	/** 
	* @Title: updateUser 
	* @Description: 用户修改
	* @param @param user
	* @return void 
	* @throws 
	*/
	@Update({"<script>"
			+"update `user` set "
			+ "<if test='nick_name != null'>nick_name=#{nick_name},</if>"
			+ "<if test='phone != null'>phone=#{phone},</if>"
			+ "<if test='head_picture != null'>head_picture=#{head_picture},</if>"
			+ "<if test='cover_picture != null'>cover_picture=#{cover_picture},</if>"
			+ "<if test='sex != null'>sex=#{sex},</if>"
			+ "<if test='age != null'>age=#{age},</if>"
			+ "<if test='birth_time != null'>birth_time=#{birth_time}</if>"
			+ " where user_id=#{user_id}</script>"})
	
	public void updateUser(User user);
	
	/** 
	* @Title: userCertification 
	* @Description: 用户认证
	* @param user
	* @return void 
	* @throws 
	*/
	@Update("update `user` set real_certification=#{real_certification}"
			+ " where user_id=#{user_id}")
	public void userCertification(User user);


	/** 
	* @Title: userMerchantsCertification 
	* @Description: 用户卖家认证
	* @param user
	* @return void 
	* @throws 
	*/
	@Update("update `user` set seller_certification=#{seller_certification}"
			+ " where user_id=#{user_id}")
	public void userMerchantsCertification(User user);
	
	
	/** 
	* @Description: 获取用户信息 
	* @param @param userId
	* @param @return
	* @return User 
	* @throws 
	*/
	@Select("select * from `user` where user_id=#{userId}")
	public User getUser(@Param("userId")long userId);
	
	/** 
	* @Description: 统计用户来源 
	* @param @return
	* @return JSONObject 
	* @throws 
	*/
	@Select("select IFNULL(user_source, '官方') name, count(1) value from `user` group by user_source")
	public List<Map<String, Integer>> countSource();
	
	/** 
	* @Description: 统计用户分布信息 
	* @param @return
	* @return List<Map<String,Integer>> 
	* @throws 
	*/
	@Select("select IFNULL(city, '未知') name, count(1) num from `user` group by city")
	public List<Map<String, Integer>> countCity();


	/** 
	* @Description: 统计年龄段 
	* @param @param stageSql 年龄段拼装sql
	* @param @return
	* @return List<Map<String,Integer>> 
	* @throws 
	*/@Select("SELECT stage, count(1) num FROM (SELECT CASE ${stageSql} END stage FROM `user`) "
			+ " T GROUP BY stage ORDER BY num DESC")
	public List<Map<String, Integer>> countAge(@Param("stageSql")String stageSql);

	/** 
	* @Description: 根据id集合查询用户 
	* @param @param ids
	* @param @return
	* @return List<User> 
	* @throws 
	*/
	@Select("select * from `user` where user_id in(${ids})")
	public List<User> findByIds(@Param("ids")String ids);
	
	/** 
	* @Description: 修改用户状态 
	* @param @param ids
	* @param @param status
	* @return void 
	* @throws 
	*/
	@Update("update `user` set status = #{status} where user_id in(${ids})")
	public void freeze(@Param("ids")String ids,@Param("status") int status);
}
