package com.guse.platform.controller.doudou;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.guse.platform.common.base.AjaxResponse;
import com.guse.platform.common.base.BaseController;
import com.guse.platform.common.base.Constant;
import com.guse.platform.common.base.Result;
import com.guse.platform.common.page.PageBean;
import com.guse.platform.common.page.PageResult;
import com.guse.platform.common.utils.DateUtils;
import com.guse.platform.common.utils.excel.ExcelUtils;
import com.guse.platform.common.utils.excel.JsGridReportBase;
import com.guse.platform.common.utils.excel.TableData;
import com.guse.platform.common.utils.excel.TableHeaderMetaData;
import com.guse.platform.entity.doudou.GameActivityRotorDetail;
import com.guse.platform.entity.doudou.GameActivityRotorStatistics;
import com.guse.platform.entity.doudou.OperationBaseUser;
import com.guse.platform.service.doudou.GameActivityRotorDetailService;
import com.guse.platform.service.doudou.GameActivityRotorStatisticsService;
import com.guse.platform.service.task.BaseUserTask;
import com.guse.platform.vo.QueryVo;

/**
 * 用户
 * @author yal
 * @version 1.0
 * @CreateDate 2017-8-14
 */
@Controller
@RequestMapping("/backstage/game")
public class GameController extends BaseController {
	@Autowired
	private BaseUserTask baseUserTask;
	@Autowired
	private GameActivityRotorDetailService gameActivityRotorDetailService;
	@Autowired
	private GameActivityRotorStatisticsService gameActivityRotorStatisticsService;
	
	/**
	 * 查询用户明细
	 * @param baseUser
	 * @param pageBean
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/queryUserDetails", method = RequestMethod.POST)
    public @ResponseBody Object deleteMailWater(OperationBaseUser baseUser,PageBean pageBean) throws Exception {
		//从redis中取数据
		List<OperationBaseUser> redisList1 = baseUserTask.getOperationBaseUsers();
		List<OperationBaseUser> redisList2 = redisList1;
		List<OperationBaseUser> redisList = redisList1;
		if(baseUser.getObuUserid() != null){
			redisList2 = new ArrayList<OperationBaseUser>();
			for(int i=0;i<redisList1.size();i++){
				if(redisList1.get(i).getObuUserid().longValue() == baseUser.getObuUserid().longValue()){
					redisList2.add(redisList1.get(i));
				}
			}
			redisList = redisList2;
		}
		if(!StringUtils.isEmpty(baseUser.getObuUserNick())){
			redisList = new ArrayList<OperationBaseUser>();
			for(int i=0;i<redisList2.size();i++){
				if(baseUser.getObuUserNick().equals(redisList2.get(i).getObuUserNick())){
					redisList.add(redisList2.get(i));
				}
			}
		}
		
		int fromIndex= (pageBean.getPageNo()-1)*pageBean.getPageSize();
		int toIndex = pageBean.getPageNo()*pageBean.getPageSize();
		if(redisList.size()<toIndex)
			toIndex = redisList.size();
		List<OperationBaseUser> list = redisList.subList(fromIndex, toIndex);
		
		Long count = (long) redisList.size();
		PageResult<OperationBaseUser> pageResult = new PageResult<OperationBaseUser>(pageBean.getPageNo(),
				pageBean.getPageSize(),count, "");
		pageResult.setList(list);
		Result<PageResult<OperationBaseUser>> result = new Result<PageResult<OperationBaseUser>>(pageResult);
		
    	if (!result.isOk()) {
    		return new AjaxResponse(Constant.CODE_ERROR, result.getErrorMsg());
    	}
        return new AjaxResponse(result.getData());
    }
    /**
     * 幸运大转盘统计信息
     * @param gameGameStatistics
     * @param pageBean
     * @return
     */
    @RequestMapping(value = "/queryLuckyWheelPageList", method = RequestMethod.POST)
    public @ResponseBody Object queryLuckyWheelPageList(PageBean pageBean) throws Exception{
    	Result<PageResult<GameActivityRotorStatistics>> result=gameActivityRotorStatisticsService.queryLuckyWheelPageList(pageBean);
    	if (!result.isOk()) {
    		return new AjaxResponse(Constant.CODE_ERROR, result.getErrorMsg());
    	}
    	return new AjaxResponse(result.getData());
    }
    /**
     * 幸运大转盘明细
     * @param gameGameStatistics
     * @param pageBean
     * @return
     */
    @RequestMapping(value = "/queryLuckyWheelDetailPageList", method = RequestMethod.POST)
    public @ResponseBody Object queryLuckyWheelDetailPageList(GameActivityRotorDetail gameActivityRotorDetail,PageBean pageBean) throws Exception{
    	Result<PageResult<GameActivityRotorDetail>> result=gameActivityRotorDetailService.queryLuckyWheelDetailPageList(gameActivityRotorDetail,pageBean);
    	if (!result.isOk()) {
    		return new AjaxResponse(Constant.CODE_ERROR, result.getErrorMsg());
    	}
    	return new AjaxResponse(result.getData());
    }
}
