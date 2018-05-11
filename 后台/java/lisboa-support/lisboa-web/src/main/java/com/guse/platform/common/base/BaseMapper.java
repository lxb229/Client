package com.guse.platform.common.base;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.guse.platform.common.page.PageResult;


/**
 * 
 * @author nbin
 * @date 2017年7月17日 下午7:03:36 
 * @version V1.0
 */
public interface BaseMapper<T, PK extends java.io.Serializable> {
	
	/**
	 * 通用分页查询
	 * @Title: selectPageByParam 
	 * @param @param bParam
	 * @param @param pParam
	 * @param @return 
	 * @return List<T>
	 */
	List<T> selectPageByParam(@Param("bParam") T bParam, @Param("pParam") PageResult<T> pParam);
	/**
	 * 通用分页总数查询
	 * @Title: countByParam 
	 * @param @param bParam
	 * @param @return 
	 * @return Integer
	 */
	Long countByParam(@Param("bParam") T bParam);
	/**
	 * 通用查询
	 * @Title: select 
	 * @param @param bParam
	 * @param @return 
	 * @return List<T>
	 */
	List<T> select(@Param("bParam") T bParam);
	
	/**
	 * 根据Id查询
	 * @Title: selectById 
	 * @date 2017年8月1日 下午7:53:56 
	 * @version V1.0
	 */
	T selectById(PK modelPK);
	
	/**
	 * 插入
	 * @Title: insert 
	 * @date 2017年8月1日 下午7:54:10 
	 * @version V1.0
	 */
	PK insert(T model);
	/**
	 * 删除
	 * @Title: delete 
	 * @date 2017年8月1日 下午7:54:31 
	 * @version V1.0
	 */
	int deleteById(PK modelPK);
	/**
	 * 更新
	 * @Title: updateByIdSelective 
	 * @date 2017年8月1日 下午7:54:38 
	 * @version V1.0
	 */
	int updateByIdSelective(T model);
	
}
