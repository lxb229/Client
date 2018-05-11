package com.palmjoys.yf1b.act.mall.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.treediagram.nina.network.model.Result;
import org.treediagram.nina.resource.Storage;
import org.treediagram.nina.resource.annotation.Static;

import com.palmjoys.yf1b.act.account.entity.RoleEntity;
import com.palmjoys.yf1b.act.account.manager.RoleEntityManager;
import com.palmjoys.yf1b.act.framework.common.manager.ErrorCodeManager;
import com.palmjoys.yf1b.act.mall.entity.MallBuyOrderEntity;
import com.palmjoys.yf1b.act.mall.entity.MallRateEntity;
import com.palmjoys.yf1b.act.mall.manager.MallOrderManager;
import com.palmjoys.yf1b.act.mall.manager.MallRateManager;
import com.palmjoys.yf1b.act.mall.model.MallDefine;
import com.palmjoys.yf1b.act.mall.model.MallVo;
import com.palmjoys.yf1b.act.mall.resource.MallConfig;

@Service
public class MallServiceImp implements MallService{
	@Autowired
	private MallOrderManager mallOrderManager;
	@Autowired
	private MallRateManager mallRateManager;
	@Autowired
	private ErrorCodeManager errCodeManager;
	@Autowired
	private RoleEntityManager roleEntityManager;

	@Static
	private Storage<Integer, MallConfig> mallCfgs;

	@Override
	public Object mall_get_mall_List(Long accountId) {
		MallVo retVo = new MallVo();
		Collection<MallConfig> cfgs = mallCfgs.getAll();
		for(MallConfig cfg : cfgs){
			retVo.addItem(cfg);
		}
		
		MallRateEntity mallRateEntity = mallRateManager.loadOrCreate();
		retVo.rateCfgVo.rmb2GoldMoney = mallRateEntity.getRmb2GoldMoney();
		retVo.rateCfgVo.goldMoney2Rmb = mallRateEntity.getGoldMoney2Rmb();
		retVo.rateCfgVo.rmb2Diamond = mallRateEntity.getRmb2Diamond();
		retVo.rateCfgVo.diamond2Rmb = mallRateEntity.getDiamond2Rmb();
		retVo.sort();
		return Result.valueOfSuccess(retVo);
	}

	@Override
	public Object mall_buy_item(Long accountId, int itemId) {
		MallConfig mallConfig = mallCfgs.get(itemId, false);
		if(null == mallConfig){
			return Result.valueOfError(MallDefine.MALL_ERROR_ITEM_UNEXIST, 
					errCodeManager.Error2Desc(MallDefine.MALL_ERROR_ITEM_UNEXIST), null);
		}
		RoleEntity roleEntity = roleEntityManager.findOf_accountId(accountId);
		if(null == roleEntity){
			return Result.valueOfError(MallDefine.MALL_ERROR_ITEM_UNEXIST, 
					errCodeManager.Error2Desc(MallDefine.MALL_ERROR_ITEM_UNEXIST), null);
		}
		MallBuyOrderEntity buyOrderEntity = mallOrderManager.createBuyOrder(accountId, roleEntity.getStarNO(), 
				mallConfig.getBuyPrice(), mallConfig.getRoomCardNum(), 
				mallConfig.getGoldMoneyNum(), mallConfig.getDiamondNum());
		
		return Result.valueOfSuccess(buyOrderEntity.getOrderId());
	}

	@Override
	public Object mall_charge_goldmoney(Long accountId, int rmb, int goldmoney) {
		if(rmb <= 0 || goldmoney <= 0){
			return Result.valueOfError(MallDefine.MALL_ERROR_CHARGE_MONEY, 
					errCodeManager.Error2Desc(MallDefine.MALL_ERROR_CHARGE_MONEY), null);
		}
		RoleEntity roleEntity = roleEntityManager.findOf_accountId(accountId);
		if(null == roleEntity){
			return Result.valueOfError(MallDefine.MALL_ERROR_ITEM_UNEXIST, 
					errCodeManager.Error2Desc(MallDefine.MALL_ERROR_ITEM_UNEXIST), null);
		}
		
		MallBuyOrderEntity buyOrderEntity = mallOrderManager.createBuyOrder(accountId, roleEntity.getStarNO(), rmb, 
				0, goldmoney, 0);
		
		return Result.valueOfSuccess(buyOrderEntity.getOrderId());
	}

	@Override
	public Object mall_charge_diamond(Long accountId, int rmb, int diamond) {
		if(rmb <= 0 || diamond <= 0){
			return Result.valueOfError(MallDefine.MALL_ERROR_CHARGE_MONEY, 
					errCodeManager.Error2Desc(MallDefine.MALL_ERROR_CHARGE_MONEY), null);
		}
		RoleEntity roleEntity = roleEntityManager.findOf_accountId(accountId);
		if(null == roleEntity){
			return Result.valueOfError(MallDefine.MALL_ERROR_ITEM_UNEXIST, 
					errCodeManager.Error2Desc(MallDefine.MALL_ERROR_ITEM_UNEXIST), null);
		}
		MallBuyOrderEntity buyOrderEntity = mallOrderManager.createBuyOrder(accountId, roleEntity.getStarNO(), rmb, 
				0, 0, diamond);
		
		return Result.valueOfSuccess(buyOrderEntity.getOrderId());
	}

}
