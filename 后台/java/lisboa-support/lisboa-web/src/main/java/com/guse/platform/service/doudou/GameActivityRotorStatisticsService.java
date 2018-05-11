package com.guse.platform.service.doudou;

import com.guse.platform.common.base.BaseService;
import com.guse.platform.common.base.Result;
import com.guse.platform.common.page.PageBean;
import com.guse.platform.common.page.PageResult;
import com.guse.platform.entity.doudou.GameActivityRotorStatistics;

/**
 * game_activity_rotor_statistics
 * @author nbin
 * @date 2017年7月18日 下午2:14:09 
 * @version V1.0
 */
public interface GameActivityRotorStatisticsService extends BaseService<GameActivityRotorStatistics,java.lang.Long>{

	Result<PageResult<GameActivityRotorStatistics>> queryLuckyWheelPageList(PageBean pageBean);
}
