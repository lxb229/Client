package com.guse.apple_reverse.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import com.guse.apple_reverse.dao.model.AppleQueryServers;

/** 
* @ClassName: AppleQueryServersDao 
* @Description: 查询线路服务接口
* @author Fily GUSE
* @date 2017年11月14日 下午2:46:05 
*  
*/
@Repository
public interface AppleQueryServersDao {
	
	/** 
	* @Title: getQuery 
	* @Description: 获取指定数量 查询线路 
	* @param @param limit
	* @param @return
	* @return List<AppleQueryServers> 
	* @throws 
	*/
	@Select("select * from ab_apple_query_servers where status = 0 limit ${limit}")
	public List<AppleQueryServers> getQuery(@Param("limit")int limit);
	
	/** 
	* @Title: getOne 
	* @Description: 获取一条查询线路 
	* @param @return
	* @return AppleQueryServers 
	* @throws 
	*/
	@Select("select * from ab_apple_query_servers where status = 0 limit 1")
	public AppleQueryServers getOne();
	
	/** 
	* @Title: batchUpdateStatus 
	* @Description: 批量更新状态 
	* @param @param status
	* @param @param ids
	* @return void 
	* @throws 
	*/
	@Update("update ab_apple_query_servers set status=#{status} where id in (${ids})")
	public void batchUpdateStatus(@Param("status")int status,@Param("ids") String ids);

}
