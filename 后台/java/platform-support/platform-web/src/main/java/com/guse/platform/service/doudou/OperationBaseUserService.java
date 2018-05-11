package com.guse.platform.service.doudou;

import java.util.List;

import com.guse.platform.common.base.BaseService;
import com.guse.platform.common.base.Result;
import com.guse.platform.common.page.PageBean;
import com.guse.platform.common.page.PageResult;
import com.guse.platform.entity.doudou.OperationBaseUser;

/**
 * operation_base_user
 * @author nbin
 * @date 2017年7月18日 下午2:14:09 
 * @version V1.0
 */
public interface OperationBaseUserService extends BaseService<OperationBaseUser,java.lang.Long>{

	
	/**
	 * 历史排行分页
	 * @Title: queryHistoryPageList 
	 * @date 2017年8月22日 下午5:33:03 
	 * @version V1.0
	 */
	Result<PageResult<OperationBaseUser>> queryHistoryPageList(PageBean pageBean,OperationBaseUser obu,String orderBy);
	
	List<OperationBaseUser> selectHistory(OperationBaseUser obu);
	
}
