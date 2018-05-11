package com.guse.platform.common.base;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.guse.platform.common.page.PageBean;
import com.guse.platform.common.page.PageResult;

/**
 * 通用service方法实现 
 * 主要用于处理通用方法
 * @author nbin
 * @date 2017年8月1日 下午3:53:37 
 * @version V1.0
 */
public abstract  class BaseServiceImpl<T,PK extends java.io.Serializable> implements BaseService<T,PK> {
	
	private BaseMapper<T,PK> baseMapper;
	
    public void setBaseMapper(BaseMapper<T, PK> baseMapper) {
    	this.baseMapper = baseMapper;
    }
	
	@Override
	public Result<PageResult<T>> queryPageList(PageBean pageBean,T t,String orderBy) {
		Long count = baseMapper.countByParam(t);
		if (count <= 0) {
             return new Result<PageResult<T>>(new PageResult<T>().initNullPage());
        }
		List<T> list = baseMapper.selectPageByParam(t, new PageResult<T>(pageBean.getPageNo(), pageBean.getPageSize(), count, orderBy));
		if(CollectionUtils.isEmpty(list)){
			 return new Result<PageResult<T>>(new PageResult<T>().initNullPage());
		}
		
		PageResult<T> pageResult = null;
		pageResult = new PageResult<T>(pageBean.getPageNo(), pageBean.getPageSize(),count ,"");
		pageResult.setList(list);
		return new Result<PageResult<T>>(pageResult);
	}

	@Override
	public List<T> queryExportExcel(T t) {
		List<T> list = baseMapper.select(t);
		return list;
	}
	
	@Override
	public PK insert(T t){
		return baseMapper.insert(t);
	}

}
