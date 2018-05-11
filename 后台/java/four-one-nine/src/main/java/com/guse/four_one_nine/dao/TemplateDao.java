package com.guse.four_one_nine.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.guse.four_one_nine.dao.model.Template;

/** 
* @ClassName: TemplateDao 
* @Description:模板管理
* @author Fily GUSE
* @date 2018年1月4日11:43:54 
*  
*/
@Repository
public interface TemplateDao {
	
	/** 
	* @Title: addTemplate 
	* @Description: 新增模板
	* @param @param template
	* @return void 
	* @throws 
	*/
	@Insert("insert into template(user_type, template_content, trigger_condition, users) "
			+ "values(#{user_type},#{template_content},#{trigger_condition},#{users})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	public void addTemplate(Template template);

	
	/** 
	* @Title: getTemplate 
	* @Description: 查询模板
	* @param Id
	* @return void 
	* @throws 
	*/
	@Select("SELECT * FROM `template` "
			+ "WHERE id = #{id}")
	public Template getTemplate(@Param("id") long id);
	
	/** 
	* @Title: save 
	* @Description: 保存信息 
	* @param 
	* @return void 
	* @throws 
	*/
	@Update("update template set user_type=#{user_type},template_content=#{template_content},"
			+ "trigger_condition=#{trigger_condition},users=#{users},"
			+ "count=#{count},status=#{status},update_time=#{update_time},updater=#{updater}"
			+ " where id=#{id}")
	public void save(Template template);
	
	
	
	/** 
	* @Title: templateCount 
	* @Description: 保存信息 
	* @param 
	* @return void 
	* @throws 
	*/
	@Update("update template set count=#{count},update_time=#{update_time}"
			+ " where id=#{id}")
	public void templateCount(Template template);
	
	/** 
	* @Title: findByParams 
	* @Description: 根据自定义条件查询 
	* @param @param params
	* @param @return
	* @return List<Template> 
	* @throws 
	*/
	@Select("select * from template where ${params}")
	public List<Template> findByParams(@Param("params")String params);
}
