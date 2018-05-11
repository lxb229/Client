package com.palmjoys.yf1b.act.dzpker.manager;

import java.util.List;

import org.springframework.stereotype.Component;
import org.treediagram.nina.memcache.annotation.Inject;
import org.treediagram.nina.memcache.service.EntityBuilder;
import org.treediagram.nina.memcache.service.EntityMemcache;

import com.palmjoys.yf1b.act.dzpker.entity.DzpkerCfgEntity;
import com.palmjoys.yf1b.act.dzpker.model.ChipAttrib;
import com.palmjoys.yf1b.act.dzpker.model.DzpkerCfgVo;
import com.palmjoys.yf1b.act.dzpker.model.InsuranceCfgAttrib;

@Component
public class DzpkerCfgManager {
	@Inject
	private EntityMemcache<Integer, DzpkerCfgEntity> dzpkerCfgCache;

	public DzpkerCfgEntity loadOrCreate(){
		int id = 1;
		return dzpkerCfgCache.loadOrCreate(id, new EntityBuilder<Integer, DzpkerCfgEntity>(){
			@Override
			public DzpkerCfgEntity createInstance(Integer pk) {
				DzpkerCfgEntity entity = DzpkerCfgEntity.valueOf(id);
				initEntity(entity);
				return entity;
			}
		});
	}
	
	private void initEntity(DzpkerCfgEntity entity){
		List<ChipAttrib> chipsList = entity.getChipsList();
		chipsList.clear();
		ChipAttrib chipAttrib = new ChipAttrib();
		chipAttrib.small = 1;
		chipAttrib.big = 2;
		chipAttrib.join = 200;
		chipsList.add(chipAttrib);
		
		chipAttrib = new ChipAttrib();
		chipAttrib.small = 2;
		chipAttrib.big = 4;
		chipAttrib.join = 400;
		chipsList.add(chipAttrib);
		
		chipAttrib = new ChipAttrib();
		chipAttrib.small = 5;
		chipAttrib.big = 10;
		chipAttrib.join = 1000;
		chipsList.add(chipAttrib);
		
		chipAttrib = new ChipAttrib();
		chipAttrib.small = 10;
		chipAttrib.big = 20;
		chipAttrib.join = 2000;
		chipsList.add(chipAttrib);
		
		chipAttrib = new ChipAttrib();
		chipAttrib.small = 25;
		chipAttrib.big = 50;
		chipAttrib.join = 5000;
		chipsList.add(chipAttrib);
		
		entity.setChipsList(chipsList);
		
		List<Integer> vildTimeList = entity.getVildTimeList();
		vildTimeList.clear();
		vildTimeList.add(30);
		vildTimeList.add(60);
		vildTimeList.add(120);
		vildTimeList.add(180);
		vildTimeList.add(240);
		vildTimeList.add(300);
		vildTimeList.add(360);
		
		entity.setVildTimeList(vildTimeList);
		
		List<Integer> buyChipList = entity.getBuyChipList();
		buyChipList.clear();
		buyChipList.add(15000);
		buyChipList.add(20000);
		buyChipList.add(25000);
		buyChipList.add(35000);
		buyChipList.add(50000);
		buyChipList.add(1000000);
		entity.setBuyChipList(buyChipList);
		
		List<InsuranceCfgAttrib> insuranceList = entity.getInsuranceList();
		InsuranceCfgAttrib cfgAttrib = new InsuranceCfgAttrib();
		cfgAttrib.cardNum = 1;
		cfgAttrib.rate = "30";
		insuranceList.add(cfgAttrib);
		
		cfgAttrib = new InsuranceCfgAttrib();
		cfgAttrib.cardNum = 2;
		cfgAttrib.rate = "16";
		insuranceList.add(cfgAttrib);
		
		cfgAttrib = new InsuranceCfgAttrib();
		cfgAttrib.cardNum = 3;
		cfgAttrib.rate = "10";
		insuranceList.add(cfgAttrib);
		
		cfgAttrib = new InsuranceCfgAttrib();
		cfgAttrib.cardNum = 4;
		cfgAttrib.rate = "8";
		insuranceList.add(cfgAttrib);
		
		cfgAttrib = new InsuranceCfgAttrib();
		cfgAttrib.cardNum = 5;
		cfgAttrib.rate = "6";
		insuranceList.add(cfgAttrib);
		
		cfgAttrib = new InsuranceCfgAttrib();
		cfgAttrib.cardNum = 6;
		cfgAttrib.rate = "5";
		insuranceList.add(cfgAttrib);
		
		cfgAttrib = new InsuranceCfgAttrib();
		cfgAttrib.cardNum = 7;
		cfgAttrib.rate = "4";
		insuranceList.add(cfgAttrib);
		
		cfgAttrib = new InsuranceCfgAttrib();
		cfgAttrib.cardNum = 8;
		cfgAttrib.rate = "3.5";
		insuranceList.add(cfgAttrib);
		
		cfgAttrib = new InsuranceCfgAttrib();
		cfgAttrib.cardNum = 9;
		cfgAttrib.rate = "3";
		insuranceList.add(cfgAttrib);
		
		cfgAttrib = new InsuranceCfgAttrib();
		cfgAttrib.cardNum = 10;
		cfgAttrib.rate = "2.5";
		insuranceList.add(cfgAttrib);
		
		cfgAttrib = new InsuranceCfgAttrib();
		cfgAttrib.cardNum = 11;
		cfgAttrib.rate = "2.2";
		insuranceList.add(cfgAttrib);
		
		cfgAttrib = new InsuranceCfgAttrib();
		cfgAttrib.cardNum = 12;
		cfgAttrib.rate = "2";
		insuranceList.add(cfgAttrib);
		
		cfgAttrib = new InsuranceCfgAttrib();
		cfgAttrib.cardNum = 13;
		cfgAttrib.rate = "1.8";
		insuranceList.add(cfgAttrib);
		
		cfgAttrib = new InsuranceCfgAttrib();
		cfgAttrib.cardNum = 14;
		cfgAttrib.rate = "1.6";
		insuranceList.add(cfgAttrib);
		
		cfgAttrib = new InsuranceCfgAttrib();
		cfgAttrib.cardNum = 15;
		cfgAttrib.rate = "1.4";
		insuranceList.add(cfgAttrib);
		entity.setInsuranceList(insuranceList);
	}
	
	public DzpkerCfgVo getCfg(){
		DzpkerCfgVo retVo = new DzpkerCfgVo();
		DzpkerCfgEntity entity = this.loadOrCreate();
		retVo.chips = entity.getChipsList();
		//retVo.buyChips = entity.getBuyChipList();
		retVo.vaildTimes = entity.getVildTimeList();
		
		return retVo;
	}
}
