package com.guse.stock.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/** 
* @ClassName: IUserDao 
* @Description: 用户信息
* @author Fily GUSE
* @date 2017年10月3日 下午11:30:10 
*  
*/
@Repository
public interface IUserDao {
	
	/** 
	* @Title: getUidByHash 
	* @Description: 根据用户hash获取用户id 
	* @param @param hash
	* @param @return
	* @return Long 
	* @throws 
	*/
	@Select("select id from rp_user where hash=#{hash}")
	public Integer getUidByHash(@Param("hash")String hash);

}
