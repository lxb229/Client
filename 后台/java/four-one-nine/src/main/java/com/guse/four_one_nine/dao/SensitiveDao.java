package com.guse.four_one_nine.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.guse.four_one_nine.dao.model.Sensitive;

/** 
* @ClassName: SensitiveDao 
* @Description:敏感字管理
* @author Fily GUSE
* @date 2018年1月4日11:43:54 
*  
*/
@Repository
public interface SensitiveDao {
	
	/** 
	* @Title: addSensitive 
	* @Description: 新增敏感字
	* @param @param sensitive
	* @return void 
	* @throws 
	*/
	@Insert("insert into `sensitive`(word_group,replace_word,count_no,creater) "
			+ "values(#{word_group},#{replace_word},#{count_no},#{creater})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	public void addSensitive(Sensitive sensitive);
	
	/** 
	* @Title: getSensitive 
	* @Description: 查询敏感字
	* @param Id
	* @return void 
	* @throws 
	*/
	@Select("SELECT * FROM `sensitive` " + "WHERE id = #{id}")
	public Sensitive getSensitive(@Param("id") long id);
	
	/** 
	* @Title: save 
	* @Description: 保存信息 
	* @param 
	* @return void 
	* @throws 
	*/
	@Update({"<script>"
			+ "update `sensitive` set id=id"
			+ "<if test='word_group != null'>,word_group=#{word_group}</if>"
			+ "<if test='replace_word != null'>,replace_word=#{replace_word}</if>"
			+ "<if test='count_no != null'>,count_no=#{count_no}</if>"
			+ "<if test='update_time != null'>,update_time=#{update_time}</if>"
			+ "<if test='updater != null'>,updater=#{updater}</if>"
			+ "<if test='create_time != null'>,create_time=#{create_time}</if>"
			+ "<if test='creater != null'>,creater=#{creater}</if>"
			+ " where id=#{id}</script>"})
	public void save(Sensitive sensitive);

}
