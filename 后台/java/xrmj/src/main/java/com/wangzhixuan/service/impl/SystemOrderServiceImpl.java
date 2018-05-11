package com.wangzhixuan.service.impl;

import com.wangzhixuan.model.Player;
import com.wangzhixuan.model.SystemOrder;
import com.wangzhixuan.model.SystemTask;
import com.wangzhixuan.model.ValidataBean;
import com.wangzhixuan.model.vo.AccountVo;
import com.wangzhixuan.model.vo.SystemOrderVo;
import com.wangzhixuan.commons.base.Constant;
import com.wangzhixuan.commons.result.PageInfo;
import com.wangzhixuan.commons.result.Result;
import com.wangzhixuan.commons.utils.GameGMUtils;
import com.wangzhixuan.commons.utils.PropertyConfigurer;
import com.wangzhixuan.commons.utils.StringUtils;
import com.wangzhixuan.mapper.SystemOrderMapper;
import com.wangzhixuan.service.IJackpotService;
import com.wangzhixuan.service.IOperatingStatisticsService;
import com.wangzhixuan.service.IPlayerMoneyService;
import com.wangzhixuan.service.IPlayerService;
import com.wangzhixuan.service.IPlayerWishService;
import com.wangzhixuan.service.IPropLogService;
import com.wangzhixuan.service.ISystemOrderService;
import com.wangzhixuan.service.ISystemTaskService;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统订单 服务实现类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-03-23
 */
@Service
public class SystemOrderServiceImpl extends ServiceImpl<SystemOrderMapper, SystemOrder> implements ISystemOrderService {
	private static Logger logger = LoggerFactory.getLogger(SystemOrderServiceImpl.class);
	@Autowired
	private GameGMUtils gameGMUtils;
	@Autowired
	private PropertyConfigurer configurer;
	@Autowired
	private SystemOrderMapper orderMapper;
	@Autowired
	private ISystemTaskService taskService;
	@Autowired
	private IJackpotService jackpotService;
	@Autowired
	private IOperatingStatisticsService operatingStatisticsService;
	@Autowired
	private IPropLogService propLogService;
	@Autowired
	private IPlayerMoneyService playerMoneyService;
	@Autowired
	private IPlayerWishService wishService;
	@Autowired
	private IPlayerService playerService;
	
	@Override
	public void selectDataGrid(PageInfo pageInfo) {
		Page<Player> page = new Page<Player>(pageInfo.getNowpage(), pageInfo.getSize());
        page.setOrderByField(pageInfo.getSort());
        page.setAsc(pageInfo.getOrder().equalsIgnoreCase("asc"));
        List<SystemOrderVo> list = orderMapper.selectSystemOrderVoPage(page, pageInfo.getCondition());
        pageInfo.setRows(list);
        pageInfo.setTotal(page.getTotal());
	}
	
	@Override
	public void taskOrder(SystemTask task) {
		//JSON格式转换
        JSONObject obj = JSONObject.parseObject(task.getTaskContent());
		Result success = processingOrder(obj);
		if(success != null && success.isSuccess()) {
			task.setTaskStatus(1);
		} else {
			logger.info(success.getMsg());
			task.setTaskStatus(2);
		}
		task.setTaskNum(task.getTaskNum()+1);
		taskService.updateById(task);		
	}

	@Override
	public Result processingOrder(JSONObject obj) {
		SystemOrder order = null;
		if(obj == null) {
			return new Result(false,"数据异常!");
		} else {
			
			order = new SystemOrder();
			order.setPurchaseType(2);
			order.setOrderNo(obj.getString("order_no"));
			order.setUserId(1);/**APP内购时GM操作人直接为admin*/
			order.setStartNo(obj.getString("start_no"));
			order.setPayPrice(new BigDecimal(obj.getString("pay_price")));
			order.setPayType(1);/**APP内购时只有App Store购买*/
			order.setPayStatus(1);/**只有购买成功的数据才会传送过来*/
			order.setPayTime(obj.getDate("pay_time"));
			order.setRoomcardAmount(Integer.parseInt(obj.getString("roomcard_amount")));
			order.setCreateTime(obj.getDate("pay_time"));
			return  this.insertOrder(order);
		}
	}
	

	@Override
	public Result addOrder(SystemOrder order) {
		/**查询对应玩家是否存在*/
		Player player = null;
		if(StringUtils.isNotBlank(order.getStartNo())) {
			player = playerService.selectPlayerBy(order.getStartNo());
		} 
		if(player == null) {
			return new Result(false, "查无此人");
		}
		/**线下支付付款时间必须填写*/
		if(order.getPayTime() == null) {
			return new Result(false, "付款时间为空");
		}
		/**生成订单编号*/
		String orderNo = randomOrder(Constant.RANDOM_ORDER);
		if(StringUtils.isBlank(orderNo)) {
			return new Result(false, "订单编号错误");
		} else {
			order.setOrderNo(orderNo);
		}
		/**设置购买渠道为线下购买*/
		order.setPurchaseType(1);
		/**线下购买订单，订单付款状态直接设置为已付款*/
		order.setPayStatus(1);
		/**处理增加订单相关业务*/
		Result result = insertOrder(order);
		
		/**通知游戏服务器*/
		if(result != null && result.isSuccess()) {
			AccountVo accountVo = new AccountVo();
			accountVo.setCmd(5);
			accountVo.setStarNo(player.getStartNo());
			/**增加的货币类型为房卡*/
			accountVo.setType(1);
			accountVo.setMoney(order.getRoomcardAmount());
			result = gameGMUtils.accountService(accountVo);
		}
		return result;
	}

	@Override
	public Result insertOrder(SystemOrder order) {
		ValidataBean validata = order.validateModel();
		if (!validata.isFlag()) {
			return new Result(false, validata.getMsg());
		} else {
			BigDecimal roomcardPrice = order.getPayPrice().divide(new BigDecimal(order.getRoomcardAmount()),2, BigDecimal.ROUND_HALF_DOWN);
			/**房卡单价必须大于房卡成本*/
			/**房卡成本*/
			BigDecimal roomcardCost = new BigDecimal(configurer.getProperty("roomcardCost"));
			if(roomcardPrice.compareTo(roomcardCost) == -1) {
				return new Result(false, "售卖价格过低");
			}
			order.setRoomcardPrice(roomcardPrice);
		}
		Result result = null;
		boolean success = this.insert(order);
		/**每一笔订单都需要更新玩家货币*/
		if(success) {
			result = playerMoneyService.updatePlayerMoney(order);
		}
		/**每一笔订单都需要冲入奖池*/
		if(result != null && result.isSuccess()) {
			result = jackpotService.supplementJackpot(order);
		}
		/**每一笔订单都需要计入统计*/
		if(result != null && result.isSuccess()) {
			result = operatingStatisticsService.supplementOperating(order);
		}
		/**生成道具日志*/
		if(result != null && result.isSuccess()) {
			result = propLogService.insertPropLog(order);
		}
		/**更新玩家祝福值*/
		if(result != null && result.isSuccess()) {
			result = wishService.wishOrder(order);
		}
		return result;
	}

	@Override
	public String randomOrder(int type) {
		List<String> yearList = new ArrayList<String>();
		/**房卡*/
		yearList.add("FK");
		/**商品*/
		yearList.add("SP");
		/**兑换码*/
		yearList.add("DHM");
		/**入库*/
		yearList.add("RK");
		/**出库*/
		yearList.add("CK");
		
		Calendar now = Calendar.getInstance();
		String start = yearList.get(type-1);
		String year = Integer.toString(now.get(Calendar.YEAR));
		String month = now.get(Calendar.MONTH)+1 > 9 ? Integer.toString(now.get(Calendar.MONTH)+1) : "0"+(now.get(Calendar.MONTH)+1);
		String day = now.get(Calendar.DAY_OF_MONTH) > 9 ? Integer.toString(now.get(Calendar.DAY_OF_MONTH)) : "0"+now.get(Calendar.DAY_OF_MONTH);
		String hour = now.get(Calendar.HOUR_OF_DAY) > 9 ? Integer.toString(now.get(Calendar.HOUR_OF_DAY)) : "0"+now.get(Calendar.HOUR_OF_DAY);
		String minute = now.get(Calendar.MINUTE) > 9 ? Integer.toString(now.get(Calendar.MINUTE)) : "0"+now.get(Calendar.MINUTE);
		String second = now.get(Calendar.SECOND) > 9 ? Integer.toString(now.get(Calendar.SECOND)) : "0"+now.get(Calendar.SECOND);
		Random random=new Random();
		String randomNumber = Integer.toString(random.nextInt(90000)+10000); 
		String orderNum = start+year+month+day+hour+minute+second+randomNumber;
		return orderNum;
				
	}
}
