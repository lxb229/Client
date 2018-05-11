package com.guse.stock.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.guse.stock.dao.model.UserAuthorise;

/**
 * 用户设备信息dao
 * @author 不能
 *
 */
@Repository
public interface IUserAuthoriseDao {

	/**
	 * 根据设备编号查询用户设备表
	 * @param deviceNumber 设备号
	 * @return 用户设备信息
	 */
	@Select("select * from pl_user_authorise where equipment_number=#{deviceNumber}")
	public UserAuthorise findByDevice(@Param("deviceNumber")String deviceNumber);
	
}
