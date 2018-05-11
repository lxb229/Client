package com.guse.four_one_nine.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.springframework.stereotype.Repository;

import com.guse.four_one_nine.dao.model.AppInstalled;

/** 
* @ClassName: AppInstalledDao 
* @Description: 装机数据
* @author Fily GUSE
* @date 2018年2月4日11:43:54 
*  
*/
@Repository
public interface AppInstalledDao {
	
	/** 
	* @Title: addAppInstalled 
	* @Description:新增装机数据
	* @param @param serverLike
	* @return void 
	* @throws 
	*/
	@Insert("insert into app_installed(phone_brand,phone_models,iemi,installed_time) "
			+ "values(#{phone_brand},#{phone_models},#{iemi},#{installed_time})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	public boolean addAppInstalled(AppInstalled appInstalled);

}
