package com.guse.platform.service.doudou;

import com.guse.platform.common.base.BaseService;
import com.guse.platform.common.base.Result;
import com.guse.platform.common.page.PageBean;
import com.guse.platform.common.page.PageResult;
import com.guse.platform.entity.doudou.Wechat;

/**
 * wechat
 * @author nbin
 * @date 2017年7月18日 下午2:14:09 
 * @version V1.0
 */
public interface WechatService extends BaseService<Wechat,java.lang.Integer>{

	/**
	 * 从游戏服务器获取客服信息集合
	 * @return
	 */
	public Result<PageResult<Wechat>> queryWechatList(Wechat wechat, PageBean pageBean);
	
	/**
	 * 新增更新客服信息
	 * @Title: saveOrUpdateProduct 
	 * @param @param product
	 * @param @return 
	 * @return Result<Integer>
	 */
	Result<Integer> updateWechat(Wechat wechat);
}
