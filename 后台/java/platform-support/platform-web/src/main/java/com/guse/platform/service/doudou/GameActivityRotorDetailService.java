package com.guse.platform.service.doudou;

import com.guse.platform.common.base.BaseService;
import com.guse.platform.common.base.Result;
import com.guse.platform.common.page.PageBean;
import com.guse.platform.common.page.PageResult;
import com.guse.platform.entity.doudou.GameActivityRotorDetail;
import com.guse.platform.entity.system.Users;

/**
 * game_activity_rotor_detail
 * @author nbin
 * @date 2017年7月18日 下午2:14:09 
 * @version V1.0
 */
public interface GameActivityRotorDetailService extends BaseService<GameActivityRotorDetail,java.lang.String>{

	Result<PageResult<GameActivityRotorDetail>> queryLuckyWheelDetailPageList(GameActivityRotorDetail gameActivityRotorDetail, PageBean pageBean, Users user);
}
