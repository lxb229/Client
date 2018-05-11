package com.palmjoys.yf1b.act.gm.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.server.Request;
import org.json.JSONArray;
import org.springframework.stereotype.Service;
import org.treediagram.nina.core.lang.JsonUtils;
import org.treediagram.nina.network.handler.JettyRequestHandler;
import org.treediagram.nina.network.model.Result;
import com.palmjoys.yf1b.act.majiang.model.CardAttrib;
import com.palmjoys.yf1b.act.majiang.model.GameDefine;

@Service
public class GmImp_PeiPai implements JettyRequestHandler{
	
	@Override
	public String getPath() {
		return "/gm/peipai";
	}

	@Override
	public byte[] handleRequest(String target, Request baseRequest, HttpServletRequest request) {
		String errorStr = "";
		try{
			while(true){
				String ss = request.getParameter("cards");
				JSONArray jsonArry = new JSONArray(ss);
				if(jsonArry.length() != 5){
					errorStr = JsonUtils.object2String(Result.valueOfError(-1, "配置牌参数错误", null));
					break;
				}
				
				//初始化一副完整的新牌
				int removedSuit = -1;
				List<CardAttrib> fullCards = GameDefine.initCards(removedSuit);
				
				int mapKey = 1;
				for(int index=0; index<jsonArry.length(); index++){
					List<CardAttrib> tmpCards = new ArrayList<>();
					JSONArray seatCards = jsonArry.getJSONArray(index);
					for(int cardIndex=0; cardIndex<seatCards.length(); cardIndex++){
						int cardVal = seatCards.getInt(cardIndex);
						int suit = cardVal/10+1;
						int point = cardVal%10;
						CardAttrib findCard = GameDefine.findOnceBySuitPoint(fullCards, suit, point);
						if(null != findCard){
							GameDefine.removeOnceByCardId(fullCards, findCard.cardId);
							tmpCards.add(findCard);
						}
					}
					GameDefine.tabletPeiPaiMap.put(mapKey, tmpCards);
					mapKey++;
				}
				errorStr = JsonUtils.object2String(Result.valueOfSuccess("配置牌数据成功"));
				break;
			}
		}catch(Exception e){
			errorStr = JsonUtils.object2String(Result.valueOfError(-1, "配置牌参数错误", null));
		}
		return errorStr.getBytes();
	}
}
