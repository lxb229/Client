package com.guse.platform.service.doudou;

import com.guse.platform.common.base.BaseService;
import com.guse.platform.common.base.Result;
import com.guse.platform.common.page.PageBean;
import com.guse.platform.common.page.PageResult;
import com.guse.platform.entity.system.Users;
import com.guse.platform.vo.doudou.AccountVo;

/**
 * system_cash
 * @author nbin
 * @date 2017年7月18日 下午2:14:09 
 * @version V1.0
 */
public interface PresentService extends BaseService<Users,java.lang.Integer>{

	public Result<PageResult<Users>> queryByStarNO(Users users,PageBean pageBean, Users user);
	
	public Result<Integer> present(AccountVo vo, Users user);
	
	/**
	 * 系统赠送房卡
	 * @param vo
	 * @param user
	 * @return
	 */
	public Result<Integer> presentBySystem(AccountVo vo, Users user);
	/**
	 * 玩家之间赠送房卡
	 * @param vo
	 * @param user
	 * @return
	 */
	public Result<Integer> presentByPlayer(AccountVo vo, Users user);
}
