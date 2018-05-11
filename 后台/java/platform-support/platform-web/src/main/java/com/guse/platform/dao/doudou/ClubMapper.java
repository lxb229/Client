package com.guse.platform.dao.doudou;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.guse.platform.common.base.BaseMapper;
import com.guse.platform.entity.doudou.Club;


/**
 * club
 * @see ClubMapper.xml
 * @author nbin
 * @date 2017年7月18日 下午2:02:28 
 * @version V1.0
 */
public interface ClubMapper extends  BaseMapper<Club, java.lang.Integer>{
	
	/**
	 * 根据主键查询
	 * @Title: selectByPrimaryKey 
	 * @param @param userId
	 * @param @return 
	 * @return Users
	 */
	Club selectByPrimaryKey(Integer cid);
	/**
	 * 根据编号获取俱乐部
	 * @param accountId
	 * @return
	 */
	Club getClubByAccount(@Param("accountId") String accountId);
	/**
	 * 获取俱乐部下一个主键id
	 * @return
	 */
	Integer getNextClubId();
	
	/**
	 * 查询用户是否还有俱乐部
	 * @param agency
	 * @return
	 */
	List<Club> selectClubOut(Club club);
	
}
