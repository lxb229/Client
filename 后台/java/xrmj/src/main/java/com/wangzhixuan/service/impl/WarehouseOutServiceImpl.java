package com.wangzhixuan.service.impl;

import com.wangzhixuan.model.Commodity;
import com.wangzhixuan.model.CommodityImage;
import com.wangzhixuan.model.CommodityList;
import com.wangzhixuan.model.GoldCommodity;
import com.wangzhixuan.model.Player;
import com.wangzhixuan.model.PlayerLottery;
import com.wangzhixuan.model.ValidataBean;
import com.wangzhixuan.model.Warehouse;
import com.wangzhixuan.model.WarehouseOut;
import com.wangzhixuan.model.vo.ExchangeDetailsVo;
import com.wangzhixuan.model.vo.GoldExchangeVo;
import com.wangzhixuan.model.vo.LuckVo;
import com.wangzhixuan.model.vo.PlayerLotteryVo;
import com.wangzhixuan.model.vo.SilverCommodityVo;
import com.wangzhixuan.model.vo.WarehouseOutVo;
import com.wangzhixuan.commons.base.Constant;
import com.wangzhixuan.commons.result.PageInfo;
import com.wangzhixuan.commons.result.Result;
import com.wangzhixuan.commons.utils.StringUtils;
import com.wangzhixuan.mapper.WarehouseOutMapper;
import com.wangzhixuan.service.ICommodityImageService;
import com.wangzhixuan.service.ICommodityListService;
import com.wangzhixuan.service.ICommodityService;
import com.wangzhixuan.service.IGoldCommodityService;
import com.wangzhixuan.service.IGoldLogService;
import com.wangzhixuan.service.IPlayerLotteryService;
import com.wangzhixuan.service.IPlayerMoneyService;
import com.wangzhixuan.service.IPropLogService;
import com.wangzhixuan.service.ISilverCommodityService;
import com.wangzhixuan.service.ISilverJackpotService;
import com.wangzhixuan.service.ISilverLogService;
import com.wangzhixuan.service.ISystemOrderService;
import com.wangzhixuan.service.IWarehouseOutService;
import com.wangzhixuan.service.IWarehouseService;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 出库单 服务实现类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-02
 */
@Service
public class WarehouseOutServiceImpl extends ServiceImpl<WarehouseOutMapper, WarehouseOut> implements IWarehouseOutService {
	private static Logger logger = LoggerFactory.getLogger(WarehouseOutServiceImpl.class);
	private Gson gson = new Gson();
	@Autowired
	private WarehouseOutMapper warehouseOutMapper;
	@Autowired
	private IPlayerLotteryService playerLotteryService;
	@Autowired
	private ISilverJackpotService silverJackpotService;
	@Autowired
	private IWarehouseService warehouseService;
	@Autowired
	private ICommodityService commodityService;
	@Autowired
	private ISilverCommodityService silverCommodityService;
	@Autowired
	private IGoldCommodityService goldCommodityService;
	@Autowired
	private ISystemOrderService systemOrderService;
	@Autowired
	private ICommodityListService commodityListService;
	@Autowired
	private IWarehouseOutService warehouseOutService;
	@Autowired
	private ISilverLogService silverLogService;
	@Autowired
	private ICommodityImageService commodityImageService;
	@Autowired
	private IPlayerMoneyService playerMoneyService;
	@Autowired
	private IPropLogService propLogService;
	@Autowired
	private IGoldLogService goldLogService;
	/**数据访问操作锁*/
	private Lock _lock = new ReentrantLock();
	
	@Override
	public Object exchange(Integer cmd, String start_no, Integer itemId, String name, String phone, String addr) {
		Object object = new Object();
		switch (cmd) {
			/**cmd=1获取所有可兑换的物品信息*/
			case 1:
				List<GoldExchangeVo> vo  = getAllGold();
				object = vo;
				break;
				/**cmd=2获取兑换商品详情*/
			case 2:
				ExchangeDetailsVo detailVo  = detaiCommodity(itemId);
				object = detailVo;
				break;
				/**cmd=3兑换商品*/
			case 3:
				LuckVo luckVo  = exchangeCommodity(start_no ,itemId, name, phone, addr);
				object = luckVo;
				break;
			default:
				break;
		}
		return object;
	}

	@Override
	public List<GoldExchangeVo> getAllGold() {
		List<GoldExchangeVo> list = commodityService.getAllGoldCommodity();
		return list;
	}

	@Override
	public ExchangeDetailsVo detaiCommodity(Integer itemId) {
		ExchangeDetailsVo vo = commodityService.exchangeCommodity(itemId);
		/**获取商品展示图片*/
		if(vo != null) {
			List<String> descIcon = new ArrayList<>();
			List<CommodityImage> detailList = commodityImageService.getDetailsList(itemId);
			for (int i = 0; i < detailList.size(); i++) {
				descIcon.add(detailList.get(i).getImageUrl());
			}
			vo.setDescIcon(descIcon);
		}
		return vo;
	}
	

	@Override
	public LuckVo exchangeCommodity(String start_no, Integer itemId, String name, String phone, String addr) {
		
		/**获取兑换商品*/
		GoldCommodity goldCommodity = goldCommodityService.getGoldCommodityBy(itemId);

		/**检查兑换商品剩余数量，剩余数量为0直接返回null*/
		/**获取商品库存列表*/
		List<CommodityList> list = commodityListService.getListByCommodity(itemId);
		/**商品兑换剩余库存不为0*/
		if(goldCommodity.getResidue() > 0 && list != null && list.size() > 0) {
			_lock.lock();
//			/**扣除金币*/
//			Result result = playerMoneyService.playerUserGold(start_no, goldCommodity.getExchangePrice());
//			/**生成道具日志*/
//			if(result != null && result.isSuccess()) {
//				result = propLogService.insertPropLogUseGold(start_no, goldCommodity.getExchangePrice());
//			} else {
//				logger.info(result.getMsg());
//				_lock.unlock();
//				return null;
//			}
			/**生成出库单*/
			WarehouseOut out = warehouseOutService.insertByGold(itemId);
			/**生成金币抽奖记录*/
			goldLogService.insertGold(start_no, itemId, out, name, phone, addr);
			/**获取兑换商品信息*/
			GoldCommodity goldCommodityVo = goldCommodityService.getGoldCommodityBy(itemId);
			LuckVo vo = new LuckVo();
			vo.setTitle("福星高照");
			String emailContent = appEmailContent(goldCommodityVo.getEmailContent());
			vo.setContent(emailContent);
			_lock.unlock();
			return vo;
		} else {
			return null;
		}
	}
	
	@Override
	public LuckVo outputCommodity(String startNo, Integer lv, LuckVo luckVo) {
		if(lv == 0) {
			/**减少一个奖项*/
			silverJackpotService.lessenSilverJackpot(lv);
			/**获取玩家抽奖项*/
			PlayerLottery playerLottery =playerLotteryService.getPlayerLotteryBy(startNo);
			List<PlayerLotteryVo> voList = gson.fromJson(playerLottery.getLotteryJson(), new TypeToken<List<PlayerLotteryVo>>(){}.getType());
			/**随机0和1中的一个数字，如果等于0就获取第一个零等级奖项*/
			Random random = new Random();
			Integer randomNumber = random.nextInt(2);
			
			PlayerLotteryVo vo = null;
			for (int i = 0; i < voList.size(); i++) {
				if(lv == voList.get(i).getCommodityLv()) {
					if(randomNumber==0) {
						vo = voList.get(i);
						break;
					}else {
						randomNumber = 0;
						continue;
					}
				}
			}
			
			luckVo.setRewareIndex(vo.getIndex());
			luckVo.setWinning(0);
			luckVo.setTitle("恭喜发财");
			luckVo.setContent("恭喜发财");
			luckVo.setAttachment(null);
			/**生成银币抽奖记录*/
			silverLogService.insertSilverZero(startNo);
		} else {
			PlayerLottery playerLottery =playerLotteryService.getPlayerLotteryBy(startNo);
			List<PlayerLotteryVo> voList = gson.fromJson(playerLottery.getLotteryJson(), new TypeToken<List<PlayerLotteryVo>>(){}.getType());
			PlayerLotteryVo vo = null;
			for (int i = 0; i < voList.size(); i++) {
				if(voList.get(i).getCommodityLv() > 0 && lv == voList.get(i).getCommodityLv()) {
					vo = voList.get(i);
					break;
				}
			}
			/**获取商品库存*/
			Warehouse warehouse = warehouseService.getWarehouseBy(vo.getId());
			/**获取商品库存列表*/
			List<CommodityList> list = commodityListService.getListByCommodity(vo.getId());
			/**商品库存不为0*/
			if(warehouse.getUsableAmount() > 0 && list != null && list.size() > 0) {
				/**生成出库单*/
				WarehouseOut out = warehouseOutService.insertBySilver(vo.getId());
				/**生成银币抽奖记录*/
				silverLogService.insertSilverByLv(startNo, vo.getId(), lv, out);
				/**获取商品的抽奖奖品*/
				SilverCommodityVo silverCommodityVo = silverCommodityService.getSilverCommodityBy(vo.getId());
				/**根据出库单获取对应的商品库存列表*/
				List<CommodityList> silverList = commodityListService.getListByWarehouseOut(out.getId());
				/**获取商品库存列表中的考号和密钥*/
				String keyValue = "";
				/**减少一个奖项*/
				silverJackpotService.lessenSilverJackpot(lv);
				luckVo.setRewareIndex(vo.getIndex());
				luckVo.setWinning(1);
				luckVo.setTitle("福星高照");
				if(silverList != null && silverList.size() > 0) {
					keyValue="&lt;b&gt;考号:"+silverList.get(0).getCardNo()+"&lt;/b&gt;&lt;br/&gt;&lt;b&gt;密钥:"+silverList.get(0).getSecretKey()+"&lt;/b&gt;&lt;br/&gt;";
				}
				String emailContent = appEmailContent(keyValue+silverCommodityVo.getEmailContent());
				luckVo.setContent(emailContent);
				luckVo.setAttachment(null);
				/**查到最后一级奖项，直接设置为零等奖*/
			} else if(lv == 5) {
				outputCommodity(startNo, 0, luckVo);
			} else {
				outputCommodity(startNo, lv+1, luckVo);
			}
		}
		return luckVo;
	}

	@Override
	public WarehouseOut insertBySilver(Integer commodity) {
		WarehouseOut out = null;
		Commodity commodity2 = commodityService.selectById(commodity);
		if(commodity != null && commodity2 != null) {
			out = new WarehouseOut();
			/**设置类型为抽奖出库*/
			out.setType(2);
			out.setCommodity(commodity);
			out.setAmount(1);
			out.setUserId(1);
			out.setCreateTime(new Date());
			Result result = insertWarehouseOut(out);
			if(result == null || !result.isSuccess()) {
				return null;
			}
		}
		return out;
	}

	@Override
	public WarehouseOut insertByGold(Integer commodity) {
		WarehouseOut out = null;
		Commodity commodity2 = commodityService.selectById(commodity);
		if(commodity != null && commodity2 != null) {
			out = new WarehouseOut();
			/**设置类型为兑换出库*/
			out.setType(3);
			out.setCommodity(commodity);
			out.setAmount(1);
			out.setUserId(1);
			out.setCreateTime(new Date());
			Result result = insertGoldOut(out);
			if(result == null || !result.isSuccess()) {
				return null;
			}
		}
		return out;
	}

	@Override
	public Result insertGoldOut(WarehouseOut warehouseOut) {
		Result result = null;
		/**生成出库单号*/
		String outNo = systemOrderService.randomOrder(Constant.RANDOM_WAREHOUSE_OUT);
		if(StringUtils.isNotBlank(outNo)) {
			warehouseOut.setOutNo(outNo);
		}
		ValidataBean validata = warehouseOut.validateModel();
		if (!validata.isFlag()) {
			return new Result(false, validata.getMsg());
		}
		/**获取兑换商品*/
		GoldCommodity goldCommodity = goldCommodityService.getGoldCommodityBy(warehouseOut.getCommodity());
		if(goldCommodity.getResidue() < warehouseOut.getAmount()) {
			return new Result(false, "可兑换库存不足");
		}
		/**获取商品库存列表*/
		List<CommodityList> list = commodityListService.getListByCommodity(warehouseOut.getCommodity());
		if(list == null || list.size() < warehouseOut.getAmount()) {
			return new Result(false, "商品库存列表不足");
		}
		boolean success = this.insert(warehouseOut);
		if(success) {
			/**更新兑换商品*/
			result = goldCommodityService.outGoldCommodity(warehouseOut.getCommodity(), warehouseOut.getAmount());
			/**兑换商品-更新库存*/
			if(result != null && result.isSuccess()) {
				result = warehouseService.outGoldWarehouse(warehouseOut.getCommodity(), warehouseOut.getAmount());
			}
			/**更新商品库存列表*/
			if(result != null && result.isSuccess()) {
				result = commodityListService.outCommodityList(warehouseOut);
			}
		}
		return result;
	}

	@Override
	public Result insertWarehouseOut(WarehouseOut warehouseOut) {
		Result result = null;
		/**生成出库单号*/
		String outNo = systemOrderService.randomOrder(Constant.RANDOM_WAREHOUSE_OUT);
		if(StringUtils.isNotBlank(outNo)) {
			warehouseOut.setOutNo(outNo);
		}
		ValidataBean validata = warehouseOut.validateModel();
		if (!validata.isFlag()) {
			return new Result(false, validata.getMsg());
		}
		/**如果是正常出库、抽奖出库、兑换出库；判断库存是否满足出库数量*/
		if(warehouseOut.getType() == 0 || warehouseOut.getType() == 2 || warehouseOut.getType() == 3) {
			/**获取商品可用库存*/
			Warehouse warehouse = warehouseService.getWarehouseBy(warehouseOut.getCommodity());
			if(warehouse.getUsableAmount() < warehouseOut.getAmount()) {
				return new Result(false, "可用库存不足");
			}
			/**获取商品库存列表*/
			List<CommodityList> list = commodityListService.getListByCommodity(warehouseOut.getCommodity());
			if(list == null || list.size() < warehouseOut.getAmount()) {
				return new Result(false, "商品库存列表不足");
			}
			boolean success = this.insert(warehouseOut);
			if(success) {
				/**减少可用库存*/
				result = warehouseService.outWarehouse(warehouseOut.getCommodity(), warehouseOut.getAmount());
				/**更新商品库存列表*/
				if(result != null && result.isSuccess()) {
					result = commodityListService.outCommodityList(warehouseOut);
				}
			}
			
			/**如果是出库冲账需要判断冲账数量是否合理*/
		} else if(warehouseOut.getType() == 1) {
			List<CommodityList> list = commodityListService.getListByWarehouseOut(warehouseOut.getWarehouseOut());
			Integer amount = Math.abs(warehouseOut.getAmount());
			if(list == null || list.size() < amount) {
				return new Result(false, "出库冲账数量错误");
			}
			boolean success = this.insert(warehouseOut);
			if(success) {
				/**更新对应商品库存列表*/
				result = commodityListService.updateByWarehouseOut(warehouseOut);
			}
			if(result != null && result.isSuccess()) {
				/**更新商品库存*/
				result = warehouseService.outWarehouse(warehouseOut.getCommodity(), warehouseOut.getAmount());
			}
		}
		return result;
	}

	@Override
	public void selectDataGrid(PageInfo pageInfo) {
		Page<Player> page = new Page<Player>(pageInfo.getNowpage(), pageInfo.getSize());
        page.setOrderByField(pageInfo.getSort());
        page.setAsc(pageInfo.getOrder().equalsIgnoreCase("asc"));
        List<WarehouseOutVo> list = warehouseOutMapper.selectWarehouseOutVoPage(page, pageInfo.getCondition());
        pageInfo.setRows(list);
        pageInfo.setTotal(page.getTotal());
		
	}

	@Override
	public List<WarehouseOut> getAllWarehouseOut(Integer commodity) {
		return warehouseOutMapper.getAllWarehouseOut( commodity);
	}

	@Override
	public String appEmailContent(String emailContent) {
		if(StringUtils.isNotBlank(emailContent)) {
			emailContent = emailContent.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("\r\n", "<br/>");
		}
		return emailContent;
	}

}
