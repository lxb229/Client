package com.wangzhixuan.service;

import com.wangzhixuan.commons.result.PageInfo;
import com.wangzhixuan.model.Wechat;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2017-12-18
 */
public interface IWechatService extends IService<Wechat> {
	void selectDataGrid(PageInfo pageInfo);
}
