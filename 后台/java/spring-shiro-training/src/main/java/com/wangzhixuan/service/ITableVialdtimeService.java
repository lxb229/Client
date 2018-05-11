package com.wangzhixuan.service;

import com.wangzhixuan.commons.result.PageInfo;
import com.wangzhixuan.commons.result.Result;
import com.wangzhixuan.model.TableVialdtime;

import java.io.UnsupportedEncodingException;
import java.util.List;

import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 房间解散时长设置 服务类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-23
 */
public interface ITableVialdtimeService extends IService<TableVialdtime> {
	/**
	 * 页面查询
	 * @param pageInfo
	 */
	void selectDataGrid(PageInfo pageInfo);
	
	/**
	 * 从游戏服务器获取所有的桌子过期配置
	 * @return
	 */
	List<TableVialdtime> getAllTableVialdtime();
	
	/**
	 * 根据id获取桌子过期设置
	 * @param id
	 * @return
	 */
	TableVialdtime getTableVialdtimeById(int id);
	
	/**
	 * 设置筹码配置
	 * @param tableChip 待更新的筹码配置
	 * @param type 1：新增桌子过期配置 2：编辑桌子过期配置 3：删除桌子过期配置
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	Result setTableVialdtime(TableVialdtime tableVialdtime, int type) throws UnsupportedEncodingException;
}
