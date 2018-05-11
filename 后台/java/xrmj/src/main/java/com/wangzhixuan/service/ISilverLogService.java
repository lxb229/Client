package com.wangzhixuan.service;

import com.wangzhixuan.commons.result.PageInfo;
import com.wangzhixuan.commons.result.Result;
import com.wangzhixuan.model.SilverLog;
import com.wangzhixuan.model.WarehouseOut;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 银币抽奖记录 服务类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-11
 */
public interface ISilverLogService extends IService<SilverLog> {
	
	/**
	 * 页面查询
	 * @param pageInfo
	 */
	void selectDataGrid(PageInfo pageInfo);
	
	/**
	 * 新增一条抽中恭喜发财的记录
	 * @param startNo 玩家明星号
	 * @return
	 */
	Result insertSilverZero(String startNo);
	/**
	 * 新增一条抽中对应奖品对应等级的记录
	 * @param startNo 玩家明星号
	 * @param commodity 抽中商品
	 * @param awardLv 奖项等级
	 * @param warehouseOut 出库单
	 * @return
	 */
	Result insertSilverByLv(String startNo, Integer commodity, Integer awardLv, WarehouseOut warehouseOut);
}
