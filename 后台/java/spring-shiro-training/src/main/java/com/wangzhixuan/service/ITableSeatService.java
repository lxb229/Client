package com.wangzhixuan.service;

import com.wangzhixuan.commons.result.PageInfo;
import com.wangzhixuan.model.TableSeat;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 桌子手牌 服务类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-23
 */
public interface ITableSeatService extends IService<TableSeat> {

	/**
	 * 页面查询
	 * @param pageInfo
	 */
	void selectDataGrid(PageInfo pageInfo);
	
	/**
	 * 从游戏服务器获取所有的桌子配置
	 * @return
	 */
	List<TableSeat> getAllTableSeatBy(int roomId);
	
}
