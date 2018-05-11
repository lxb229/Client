package com.palmjoys.yf1b.act.corps.model;

import java.util.ArrayList;
import java.util.List;

import com.palmjoys.yf1b.act.framework.account.entity.AccountEntity;
import com.palmjoys.yf1b.act.majiang.model.SeatAttrib;
import com.palmjoys.yf1b.act.majiang.model.TableAttrib;

public class CorpsTableVo {
	//帮会Id
	public String corpsId;
	//帮会公告
	public String corpsNotice;
	//房卡数量
	public String cardNum;
	//桌子列表
	public List<CorpsTableItemAttribVo> tables;
	
	public CorpsTableVo(){
		this.tables = new ArrayList<>();
		this.corpsNotice = "";
		this.cardNum = "0";
	}
	
	public void addTable(TableAttrib table, String gameName){
		CorpsTableItemAttribVo vo = new CorpsTableItemAttribVo();
		vo.tableId = table.tableId;
		vo.gameName = gameName;
		vo.ruleShowDesc = table.ruleShowDesc;
		vo.password = 0;
		if(null != table.password && table.password.isEmpty() == false){
			vo.password = 1;
		}
		
		for(SeatAttrib seat : table.seats){
			CorpsTablePlayerAttribVo seatPlayer = new CorpsTablePlayerAttribVo();
			if(0!=seat.accountId){
				AccountEntity accountEntity = table.accountManager.load(seat.accountId);
				if(null != accountEntity){
					seatPlayer.accountId = ""+seat.accountId;
					seatPlayer.nick = accountEntity.getNick();
					seatPlayer.headImg = accountEntity.getHeadImg();
					seatPlayer.pingVal = table.accountManager.getClientPing(seat.accountId);
				}
			}
			
			vo.seats.add(seatPlayer);
		}
		this.tables.add(vo);
	}
}
