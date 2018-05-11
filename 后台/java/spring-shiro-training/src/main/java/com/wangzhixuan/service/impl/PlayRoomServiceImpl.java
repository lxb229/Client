package com.wangzhixuan.service.impl;

import com.wangzhixuan.model.JoinRoomLog;
import com.wangzhixuan.model.PlayRoom;
import com.wangzhixuan.model.Player;
import com.wangzhixuan.model.Room;
import com.wangzhixuan.commons.result.PageInfo;
import com.wangzhixuan.commons.utils.PropertyConfigurer;
import com.wangzhixuan.mapper.PlayRoomMapper;
import com.wangzhixuan.service.IPlayRoomService;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 玩家在房间中的明细 服务实现类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-15
 */
@Service
public class PlayRoomServiceImpl extends ServiceImpl<PlayRoomMapper, PlayRoom> implements IPlayRoomService {
	@Autowired
	private PropertyConfigurer configurer;
	@Autowired
	private PlayRoomMapper playRoomMapper;
	
	@Override
	public void selectDataGrid(PageInfo pageInfo) {
		Page<Player> page = new Page<Player>(pageInfo.getNowpage(), pageInfo.getSize());
        page.setOrderByField(pageInfo.getSort());
        page.setAsc(pageInfo.getOrder().equalsIgnoreCase("asc"));
        List<PlayRoom> list = playRoomMapper.selectPlayRoomPage(page, pageInfo.getCondition());
        pageInfo.setRows(list);
        pageInfo.setTotal(page.getTotal());
	}
	
	@Override
	public PlayRoom getPlayRoomBy(String playerId, String houseOwner, int roomId, Date joinTime) {
		
		return playRoomMapper.getPlayRoomBy(playerId, houseOwner, roomId, joinTime, configurer.getProperty("roomDays"));
	}

	@Override
	public Integer insertPlayRoom(Player player, Room room, Player houseOwner, JoinRoomLog joinRoomLog) {
		PlayRoom playRoom = new PlayRoom();
		playRoom.setPlayId(player.getPlayId());
		playRoom.setPlayNick(player.getPlayNick());
		playRoom.setRoomId(room.getRoomId());
		playRoom.setHouseOwner(room.getHouseOwner());
		playRoom.setOwnerName(houseOwner.getPlayNick());
		playRoom.setJoinTime(joinRoomLog.getJoinTime());
		playRoom.setRoomCreateTime(room.getCreateTime());
		playRoom.setRoomPartyNum(0);
		playRoom.setJoinPartyNum(0);
		playRoom.setJettonNum(0);
		playRoom.setFinancialWater(0);
		playRoom.setPlayerFinancialWater(0);
		playRoom.setInsuranceWater(0);
		playRoom.setPlayerInsuranceWater(0);
		playRoom.setProfitNum(0);
		playRoom.setInsuranceProfitNum(0);
		boolean success = this.insert(playRoom);
		if(success) {
			return playRoom.getId();
		} else {
			return null;
		}
	}

	@Override
	public int updateRoomPartyNum(int partyNum, String houseOwner, int roomId, Date createTime) {
		return playRoomMapper.updateRoomPartyNum(partyNum, houseOwner, roomId, createTime, configurer.getProperty("roomDays"));
	}

	@Override
	public int updateRoomFinancialWater(int financialWater, String houseOwner, int roomId, Date createTime) {
		return playRoomMapper.updateRoomFinancialWater(financialWater, houseOwner, roomId, createTime, configurer.getProperty("roomDays"));
	}

	@Override
	public int updateRoomInsuranceWater(int insuranceWater, String houseOwner, int roomId, Date createTime) {
		return playRoomMapper.updateRoomInsuranceWater(insuranceWater, houseOwner, roomId, createTime, configurer.getProperty("roomDays"));
	}

	@Override
	public int updateRoomDisappaearTime(Date disappaearTime, String houseOwner, int roomId, Date createTime) {
		return playRoomMapper.updateRoomDisappaearTime(disappaearTime, houseOwner, roomId, createTime, configurer.getProperty("roomDays"));
	}
	
}
