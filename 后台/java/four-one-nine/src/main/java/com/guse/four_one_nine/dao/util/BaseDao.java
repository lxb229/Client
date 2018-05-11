package com.guse.four_one_nine.dao.util;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/** 
* @ClassName: BaseDao 
* @Description: 数据基础操作
* @author Fily GUSE
* @date 2018年1月19日 下午4:05:22 
*  
*/
@Repository
public interface BaseDao {
	
	/** 
	* @Description: 通用数据添加接口 
	* @param @param t
	* @param @param script
	* @param @return
	* @return long 
	* @throws 
	*/
	@Insert({"<script>${script}</script>"})
	@Options(useGeneratedKeys = true, keyProperty="id")
	public long addT(@Param("t")Object t, @Param("script")String script, @Param("id")String id);
	
	/** 
	* @Description: 通用修改数据接口 
	* @param @param t
	* @param @param script
	* @param @return
	* @return int 
	* @throws 
	*/
	@Update({"<script>${script}</script>"})
	public int updateT(@Param("t")Object t, @Param("script")String script);

}
