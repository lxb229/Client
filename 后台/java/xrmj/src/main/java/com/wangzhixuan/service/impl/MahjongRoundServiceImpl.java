package com.wangzhixuan.service.impl;

import com.wangzhixuan.model.MahjongRound;
import com.wangzhixuan.mapper.MahjongRoundMapper;
import com.wangzhixuan.service.IMahjongRoundService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

/**
 * <p>
 * 麻将馆轮数 服务实现类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-17
 */
@Service
public class MahjongRoundServiceImpl extends ServiceImpl<MahjongRoundMapper, MahjongRound> implements IMahjongRoundService {

	@Override
	public MahjongRound selectMahjongRoundBy(String mahjongNo, Integer gameType) {
		Map<String, Object> map = new HashMap<>();
		map.put("mahjong_no", mahjongNo);
		map.put("game_type", gameType);
		List<MahjongRound> playerList = this.selectByMap(map);
		if(playerList != null && playerList.size() > 0) {
			return playerList.get(0);
		} else {
			return null;
		}
	}
	
}
