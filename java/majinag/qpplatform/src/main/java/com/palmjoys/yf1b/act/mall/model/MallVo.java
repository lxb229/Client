package com.palmjoys.yf1b.act.mall.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MallVo {
	//商品列表
	public List<MallItemAttrib> mallItems = new ArrayList<>();
	//代理商列表
	public List<MallProxyAttrib> proxyItems = new ArrayList<>();
	
	public void sortMallItem(){
		this.mallItems.sort(new Comparator<MallItemAttrib>(){
			@Override
			public int compare(MallItemAttrib arg0, MallItemAttrib arg1) {
				if(arg0.itemId > arg1.itemId){
					return 1;
				}else if(arg0.itemId < arg1.itemId){
					return -1;
				}
				return 0;
			}
		});
	}
}
