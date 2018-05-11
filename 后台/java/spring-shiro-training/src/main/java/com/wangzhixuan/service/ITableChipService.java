package com.wangzhixuan.service;

import com.wangzhixuan.commons.result.PageInfo;
import com.wangzhixuan.commons.result.Result;
import com.wangzhixuan.model.TableChip;

import java.io.UnsupportedEncodingException;
import java.util.List;

import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 桌子配置 服务类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-23
 */
public interface ITableChipService extends IService<TableChip> {
	
	/**
	 * 页面查询
	 * @param pageInfo
	 */
	void selectDataGrid(PageInfo pageInfo);
	
	/**
	 * 从游戏服务器获取所有的桌子配置
	 * @return
	 */
	List<TableChip> getAllTableChip();
	
	/**
	 * 根据id获取筹码设置
	 * @param id
	 * @return
	 */
	TableChip getTableById(int id);
	
	/**
	 * 设置筹码配置
	 * @param tableChip 待更新的筹码配置
	 * @param type 1：新增筹码配置 2：编辑筹码配置 3：删除筹码配置
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	Result setTableChip(TableChip tableChip, int type) throws UnsupportedEncodingException;
	
}
