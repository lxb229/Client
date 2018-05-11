package com.guse.platform.dao.doudou;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.guse.platform.common.base.BaseMapper;
import com.guse.platform.common.page.PageResult;
import com.guse.platform.entity.doudou.OperationBaseUser;


/**
 * operation_base_user
 * @see OperationBaseUserMapper.xml
 * @author nbin
 * @date 2017年7月18日 下午2:02:28 
 * @version V1.0
 */
public interface OperationBaseUserMapper extends  BaseMapper<OperationBaseUser, java.lang.Long>{

	List<OperationBaseUser> selectUsersByUserIds(List<Long> userIds);
	
	int batchInsert(List<OperationBaseUser> users);
	
	int batchUpdate(List<OperationBaseUser> users);
	
	/**
	 * 历史排行分页查询
	 */
	List<Map<String,Object>> selectHistoryPageByParam(@Param("bParam") OperationBaseUser bParam, @Param("pParam") PageResult<OperationBaseUser> pParam);
	
	Long countHistoryByParam(@Param("bParam") OperationBaseUser bParam);
	
	List<Map<String, Object>> selectHistory(@Param("bParam") OperationBaseUser bParam);
	
}
