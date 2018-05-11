package com.guse.four_one_nine.dao.util;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/** 
* @ClassName: SearchDao 
* @Description: 列表查询
* @author Fily GUSE
* @date 2018年1月8日 下午4:58:02 
*  
*/
@Repository
public interface SearchDao {
	
	/** 
	* @Title: searchAll 
	* @Description: 查询总条数 
	* @param @param tableName 表名
	* @param @param params 查询条件
	* @param @return
	* @return int 
	* @throws 
	*/
	@Select("SELECT COUNT(1) FROM ${tableName} WHERE ${params}")
	public int searchAll(@Param("tableName")String tableName,@Param("params") String params);

	/** 
	* @Title: search 
	* @Description: 执行数据查询 
	* @param @param tableName 表名
	* @param @param field 查询列
	* @param @param params 查询参数
	* @param @param limit 分页信息
	* @param @return
	* @return List<Map<String,String>> 
	* @throws 
	*/
	@Select("SELECT ${field} from ${tableName} WHERE ${params} ${limit}")
	public List<Map<String, Object>> search(@Param("tableName")String tableName,@Param("field")String field
			,@Param("params") String params,@Param("limit") String limit);
}
