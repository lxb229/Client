package com.guse.platform.dao.doudou;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.guse.platform.common.base.BaseMapper;
import com.guse.platform.entity.doudou.UserRoomcards;


/**
 * user_roomcards
 * @see UserRoomcardsMapper.xml
 * @author nbin
 * @date 2017年7月18日 下午2:02:28 
 * @version V1.0
 */
public interface UserRoomcardsMapper extends  BaseMapper<UserRoomcards, java.lang.Integer>{
	/**
	 * 根据名称获取产品
	 * @Title: getProductByName 
	 * @param @param productName
	 * @param @return 
	 * @return List<SystemProduct>
	 */
	List<UserRoomcards> getRoomcardByUser(@Param("userId") Integer userId);
}
