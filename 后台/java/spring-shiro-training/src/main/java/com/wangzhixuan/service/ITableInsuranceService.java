package com.wangzhixuan.service;

import com.wangzhixuan.commons.result.PageInfo;
import com.wangzhixuan.commons.result.Result;
import com.wangzhixuan.model.TableInsurance;

import java.io.UnsupportedEncodingException;
import java.util.List;

import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 桌子保险配置 服务类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-23
 */
public interface ITableInsuranceService extends IService<TableInsurance> {
	/**
	 * 页面查询
	 * @param pageInfo
	 */
	void selectDataGrid(PageInfo pageInfo);
	
	/**
	 * 从游戏服务器获取所有的保险配置
	 * @return
	 */
	List<TableInsurance> getAllTableInsurance();
	
	/**
	 * 根据id获取保险设置
	 * @param id
	 * @return
	 */
	TableInsurance getTableInsuranceById(int id);
	
	/**
	 * 设置保险配置
	 * @param tableChip 待更新的保险配置
	 * @param type 1：新增保险配置 2：编辑保险配置 3：删除保险配置
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	Result setTableInsurance(TableInsurance tableInsurance, int type) throws UnsupportedEncodingException;

}
