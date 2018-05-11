package com.palmjoys.yf1b.act.dzpker.command;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.treediagram.nina.console.ConsoleBean;
import org.treediagram.nina.console.ConsoleCommand;
import org.treediagram.nina.network.model.Result;

import com.palmjoys.yf1b.act.dzpker.entity.DzpkerCfgEntity;
import com.palmjoys.yf1b.act.dzpker.manager.DzpkerCfgManager;
import com.palmjoys.yf1b.act.dzpker.manager.GameLogicManager;
import com.palmjoys.yf1b.act.dzpker.manager.TableIdManager;
import com.palmjoys.yf1b.act.dzpker.model.ChipAttrib;
import com.palmjoys.yf1b.act.dzpker.model.GmSeatHandCardsVo;
import com.palmjoys.yf1b.act.dzpker.model.InsuranceCfgAttrib;
import com.palmjoys.yf1b.act.dzpker.model.SeatAttrib;
import com.palmjoys.yf1b.act.dzpker.model.TableAttrib;
import com.palmjoys.yf1b.act.account.entity.RoleEntity;
import com.palmjoys.yf1b.act.account.manager.RoleEntityManager;

@Component
@ConsoleBean
public class DzpkerCommand {
	@Autowired
	private DzpkerCfgManager dzpkerCfgManager;
	@Autowired
	private GameLogicManager logicManager;
	@Autowired
	private RoleEntityManager roleEntityManager;
	@Autowired
	private TableIdManager tableIdManager;
	
	
	@ConsoleCommand(name = "gm_dzpker_get_table_chip_cfg", description = "获取筹码设置配置")
	public Object gm_dzpker_get_table_chip_cfg(){
		DzpkerCfgEntity cfgEntity = dzpkerCfgManager.loadOrCreate();
		List<ChipAttrib> chipsList = cfgEntity.getChipsList();
		return Result.valueOfSuccess(chipsList);
	}
	
	@ConsoleCommand(name = "gm_dzpker_set_table_chip_cfg", description = "设置筹码设置配置")
	public Object gm_dzpker_set_table_chip_cfg(ChipAttrib []chips){
		DzpkerCfgEntity cfgEntity = dzpkerCfgManager.loadOrCreate();
		List<ChipAttrib> chipsList = cfgEntity.getChipsList();
		chipsList.clear();
		for(ChipAttrib attrib : chips){
			chipsList.add(attrib);
		}
		cfgEntity.setChipsList(chipsList);
		
		return Result.valueOfSuccess();
	}

	@ConsoleCommand(name = "gm_dzpker_get_table_vialdtime_cfg", description = "获取桌子过期时间配置")
	public Object gm_dzpker_get_table_vialdtime_cfg(){
		DzpkerCfgEntity cfgEntity = dzpkerCfgManager.loadOrCreate();
		List<Integer> vildTimeList = cfgEntity.getVildTimeList();
		return Result.valueOfSuccess(vildTimeList);
	}
	
	@ConsoleCommand(name = "gm_dzpker_set_table_vialdtime_cfg", description = "设置桌子过期时间配置")
	public Object gm_dzpker_set_table_vialdtime_cfg(Integer []vildTimes){
		DzpkerCfgEntity cfgEntity = dzpkerCfgManager.loadOrCreate();
		List<Integer> vildTimeList = cfgEntity.getVildTimeList();
		vildTimeList.clear();
		for(Integer vildTime : vildTimes){
			vildTimeList.add(vildTime);
		}		
		cfgEntity.setVildTimeList(vildTimeList);
		return Result.valueOfSuccess();
	}
	
	@ConsoleCommand(name = "gm_dzpker_get_table_buyChipList_cfg", description = "获取桌子筹码购买配置")
	public Object gm_dzpker_get_table_buyChipList_cfg(){
		DzpkerCfgEntity cfgEntity = dzpkerCfgManager.loadOrCreate();
		List<Integer> buyChipList = cfgEntity.getBuyChipList();
		return Result.valueOfSuccess(buyChipList);
	}
	
	@ConsoleCommand(name = "gm_dzpker_set_table_buyChipList_cfg", description = "设置桌子筹码购买配置")
	public Object gm_dzpker_set_table_buyChipList_cfg(Integer []buyChips){
		DzpkerCfgEntity cfgEntity = dzpkerCfgManager.loadOrCreate();
		List<Integer> buyChipList = cfgEntity.getBuyChipList();
		buyChipList.clear();
		for(Integer buyChip : buyChips){
			buyChipList.add(buyChip);
		}
				
		cfgEntity.setBuyChipList(buyChipList);
		return Result.valueOfSuccess();
	}
	
	@ConsoleCommand(name = "gm_dzpker_get_table_insuranceList_cfg", description = "获取保险购买配置")
	public Object gm_dzpker_get_table_insuranceList_cfg(){
		DzpkerCfgEntity cfgEntity = dzpkerCfgManager.loadOrCreate();
		List<InsuranceCfgAttrib> insuranceList = cfgEntity.getInsuranceList();
		return Result.valueOfSuccess(insuranceList);
	}
	
	@ConsoleCommand(name = "gm_dzpker_set_table_insuranceList_cfg", description = "设置保险购买配置")
	public Object gm_dzpker_set_table_insuranceList_cfg(InsuranceCfgAttrib []insurances){
		DzpkerCfgEntity cfgEntity = dzpkerCfgManager.loadOrCreate();
		List<InsuranceCfgAttrib> insuranceList = cfgEntity.getInsuranceList();
		insuranceList.clear();
		for(InsuranceCfgAttrib insurance : insurances){
			insuranceList.add(insurance);
		}
		cfgEntity.setInsuranceList(insuranceList);
		return Result.valueOfSuccess();
	}
	
	@ConsoleCommand(name = "gm_dzpker_get_table_seat_handcards", description = "获取指定桌子的所有参加了游戏的座位底牌")
	public Object gm_dzpker_get_table_seat_handcards(int tableId){
		int err = -1;
		String errStr = "桌子不存在";
		GmSeatHandCardsVo retVo = new GmSeatHandCardsVo();
		logicManager.lock();
		try{
			while(true){
				TableAttrib table = logicManager.getTable(tableId);
				if(null == table){
					break;
				}
				for(int seatIndex=0; seatIndex<table.seats.size(); seatIndex++){
					SeatAttrib seat = table.getSeat(seatIndex);
					if(seat.accountId > 0
							&& seat.bGamed){
						if(seat.handCars.size() == 2){
							String cardsStr = "";
							String nick = "";
							RoleEntity roleEntity = roleEntityManager.findOf_accountId(seat.accountId);
							if(null != roleEntity){
								nick = roleEntity.getNick();
							}
							cardsStr += seat.handCars.get(0).toString();
							cardsStr += ",";
							cardsStr += seat.handCars.get(1).toString();
							retVo.addSeat(nick, cardsStr);
						}
					}
				}
				
				err = 0;
				break;
			}
		}finally{
			logicManager.unlock();
		}
		if(err < 0){
			return Result.valueOfError(err, errStr, null);
		}
		
		return Result.valueOfSuccess(retVo);
	}
}
