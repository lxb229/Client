package com.wangzhixuan.service;

import com.wangzhixuan.commons.result.PageInfo;
import com.wangzhixuan.commons.result.Result;
import com.wangzhixuan.commons.shiro.ShiroUser;
import com.wangzhixuan.model.GoldLog;
import com.wangzhixuan.model.WarehouseOut;
import com.wangzhixuan.model.vo.GoldLogVo;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 金币兑换商品记录 服务类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-11
 */
public interface IGoldLogService extends IService<GoldLog> {
	
	/**
	 * 页面查询
	 * @param pageInfo
	 */
	void selectDataGrid(PageInfo pageInfo);
	/**
	 * 编辑界面
	 * @param id 兑换记录id
	 * @return
	 */
	GoldLogVo selectGoldLogVoBy(Integer id);
	/**
	 * 兑换商品出库
	 * @param goldLog 兑换记录
	 * @return
	 */
	Result disposeGoldLog(GoldLog goldLog, ShiroUser user);
	/**
	 * 新增一条兑换奖品的记录
	 * @param startNo 玩家明星号
	 * @param commodity 兑换商品
	 * @param warehouseOut 出库单
	 * @return
	 */
	Result insertGold(String startNo, Integer commodity, WarehouseOut warehouseOut, 
			String name, String phone, String address);

}
