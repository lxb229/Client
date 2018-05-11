package com.guse.platform.dao.doudou;


import org.apache.ibatis.annotations.Param;

import com.guse.platform.common.base.BaseMapper;
import com.guse.platform.entity.doudou.ClubUser;


/**
 * club_user
 * @see ClubUserMapper.xml
 * @author nbin
 * @date 2017年7月18日 下午2:02:28 
 * @version V1.0
 */
public interface ClubUserMapper extends  BaseMapper<ClubUser, java.lang.Integer>{
	
	/**
	 * 获取俱乐部玩家
	 * @param clubId
	 * @param userId
	 * @return
	 */
	ClubUser getClubUserBy(@Param("clubId")Integer clubId, @Param("userId")Integer userId);
}
