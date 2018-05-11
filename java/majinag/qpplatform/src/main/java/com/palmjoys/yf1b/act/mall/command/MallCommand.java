package com.palmjoys.yf1b.act.mall.command;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.treediagram.nina.console.ConsoleBean;
import org.treediagram.nina.console.ConsoleCommand;
import org.treediagram.nina.network.model.Result;

import com.palmjoys.yf1b.act.mall.entity.MallEntity;
import com.palmjoys.yf1b.act.mall.manager.MallManager;
import com.palmjoys.yf1b.act.mall.model.MallItemAttrib;
import com.palmjoys.yf1b.act.mall.model.MallProxyAttrib;
import com.palmjoys.yf1b.act.mall.model.MallVo;

@Component
@ConsoleBean
public class MallCommand {
	@Autowired
	private MallManager mallManager;
	
	//获取商城物品数据
	@ConsoleCommand(name = "gm_mall_get_mall_list", description = "获取商城物品数据")
	public Object gm_mall_get_mall_list(){
		MallVo retVo = new MallVo();
		MallEntity mallEntity = mallManager.loadOrCreate();
		retVo.mallItems.addAll(mallEntity.getMallList());
		retVo.sortMallItem();
		
		return Result.valueOfSuccess(retVo);
	}
	
	//获取商城代理数据
	@ConsoleCommand(name = "gm_mall_get_proxy_list", description = "获取商城代理数据")
	public Object gm_mall_get_proxy_list(){
		MallVo retVo = new MallVo();
		MallEntity mallEntity = mallManager.loadOrCreate();
		retVo.proxyItems.addAll(mallEntity.getProxyList());
		
		return Result.valueOfSuccess(retVo);
	}
	
	
	//设置商城物品数据
	@ConsoleCommand(name = "gm_mall_set_mall_list", description = "设置商城配置数据")
	public Object gm_mall_set_mall_list(MallItemAttrib []mallItems){
		MallEntity mallEntity = mallManager.loadOrCreate();
		List<MallItemAttrib> mallList = mallEntity.getMallList();
		mallList.clear();
		for(MallItemAttrib mallItemAttrib : mallItems){
			mallList.add(mallItemAttrib);
		}
		mallEntity.setMallList(mallList);
		
		return Result.valueOfSuccess();
	}
	
	//设置商城代理数据
	@ConsoleCommand(name = "gm_mall_set_proxy_list", description = "设置商城代理数据")
	public Object gm_mall_set_proxy_list(MallProxyAttrib []proxyItems){
		MallEntity mallEntity = mallManager.loadOrCreate();
		List<MallProxyAttrib> proxyList = mallEntity.getProxyList();
		proxyList.clear();
		for(MallProxyAttrib mallProxyAttrib : proxyItems){
			proxyList.add(mallProxyAttrib);
		}
		mallEntity.setProxyList(proxyList);
		
		return Result.valueOfSuccess();
	}
	
}
