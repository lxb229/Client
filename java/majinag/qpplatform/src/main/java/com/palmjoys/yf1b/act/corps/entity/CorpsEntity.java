package com.palmjoys.yf1b.act.corps.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;
import org.treediagram.nina.core.lang.JsonUtils;
import org.treediagram.nina.core.time.DateUtils;
import org.treediagram.nina.memcache.IEntity;
import org.treediagram.nina.memcache.annotation.Enhance;
import org.treediagram.nina.memcache.annotation.Memcached;

import com.palmjoys.yf1b.act.corps.model.CorpsJoinAttrib;

@Entity
@Memcached
public class CorpsEntity implements IEntity<String>, Lifecycle{
	//帮会Id
	@Id
	private String corpsId;
	//帮会名称
	@Column(nullable = false)
	private String corpsName;
	//帮主
	@Column(nullable = false)
	private long createPlayer;
	//创建时间
	@Column(nullable = false)
	private long createTime;
	//房卡使用状态(0=关闭,1=开放)
	@Column(nullable = false)
	private int roomCardState;
	//帮会成员列表Json
	@Lob
	@Column(nullable = false)
	private String memberListJson;
	//加入申请列表
	@Lob
	@Column(nullable = false)
	private String joinListJson;
	//帮会状态(-1=冻结,0=正常)
	@Column(nullable = false)
	private int corpsState;
	//帮会房卡数量
	@Column(nullable = false)
	private long roomCard;
	//帮会微信群号
	@Column(nullable = false)
	private String wxNO;
	//帮会公告
	@Lob
	@Column(nullable = false)
	private String corpsNotice;
	//帮会黑名单列表Json
	@Lob
	@Column(nullable = false)
	private String blackListJson;
	//帮会可见状态(0=可见,1=隐藏)
	@Column(nullable = false)
	private int hidde;
		
	//成员列表List
	@Transient
	private Map<Long, Long> memberList;	
	//加入申请列表List
	@Transient
	private Map<Long, CorpsJoinAttrib> joinList;
	//帮会黑名单列表
	@Transient
	private Map<Long, Long> blackList;
	
	
	public static CorpsEntity valueOf(String corpsId, String corpsName, long createPlayer){
		CorpsEntity entity = new CorpsEntity();
		entity.corpsId = corpsId;
		entity.corpsName = corpsName;
		entity.createPlayer = createPlayer;
		entity.createTime = DateUtils.getTime(-1, -1, -1, -1, -1, -1);
		entity.roomCardState = 1;
		entity.memberListJson = null;
		entity.memberList = new HashMap<>();
		entity.memberList.put(createPlayer, createPlayer);
		
		entity.joinListJson = null;
		entity.joinList = new HashMap<>();
		entity.corpsState = 0;
		entity.roomCard = 0;
		entity.wxNO = "";
		entity.corpsNotice = "";
		entity.blackListJson = null;
		entity.blackList = new HashMap<>();
		entity.hidde = 0;
		
		return entity;
	}
	
	public String getCorpsId() {
		return corpsId;
	}

	@Enhance
	public void setCorpsId(String corpsId) {
		this.corpsId = corpsId;
	}

	public String getCorpsName() {
		return corpsName;
	}

	@Enhance
	public void setCorpsName(String corpsName) {
		this.corpsName = corpsName;
	}

	public long getCreatePlayer() {
		return createPlayer;
	}

	@Enhance
	public void setCreatePlayer(long createPlayer) {
		this.createPlayer = createPlayer;
	}

	public long getCreateTime() {
		return createTime;
	}

	@Enhance
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}	

	public Map<Long, Long> getMemberList() {
		return memberList;
	}

	@Enhance
	public void setMemberList(Map<Long, Long> memberList) {
		this.memberList = memberList;
	}

	public int getRoomCardState() {
		return roomCardState;
	}

	@Enhance
	public void setRoomCardState(int roomCardState) {
		this.roomCardState = roomCardState;
	}	

	public int getCorpsState() {
		return corpsState;
	}

	@Enhance
	public void setCorpsState(int corpsState) {
		this.corpsState = corpsState;
	}

	public long getRoomCard() {
		return roomCard;
	}

	@Enhance
	public void setRoomCard(long roomCard) {
		this.roomCard = roomCard;
	}

	public String getWxNO() {
		return wxNO;
	}

	@Enhance
	public void setWxNO(String wxNO) {
		this.wxNO = wxNO;
	}	

	public String getCorpsNotice() {
		return corpsNotice;
	}

	@Enhance
	public void setCorpsNotice(String corpsNotice) {
		this.corpsNotice = corpsNotice;
	}

	public Map<Long, CorpsJoinAttrib> getJoinList() {
		return joinList;
	}

	@Enhance
	public void setJoinList(Map<Long, CorpsJoinAttrib> joinList) {
		this.joinList = joinList;
	}

	public Map<Long, Long> getBlackList() {
		return blackList;
	}

	@Enhance
	public void setBlackList(Map<Long, Long> blackList) {
		this.blackList = blackList;
	}

	public int getHidde() {
		return hidde;
	}

	@Enhance
	public void setHidde(int hidde) {
		this.hidde = hidde;
	}

	@Override
	public String getId() {
		return corpsId;
	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {
		return false;
	}
	
	@Override
	public void onLoad(Session arg0, Serializable arg1) {
		if(StringUtils.isBlank(memberListJson)){
			memberList = new HashMap<>();
		}else{
			memberList = JsonUtils.string2Map(memberListJson, Long.class, Long.class);
		}
		
		if(StringUtils.isBlank(joinListJson)){
			joinList = new HashMap<>();
		}else{
			joinList = JsonUtils.string2Map(joinListJson, Long.class, CorpsJoinAttrib.class);
		}
		
		if(StringUtils.isBlank(blackListJson)){
			blackList = new HashMap<>();
		}else{
			blackList = JsonUtils.string2Map(blackListJson, Long.class, Long.class);
		}
	}

	@Override
	public boolean onSave(Session arg0) throws CallbackException {
		memberListJson = JsonUtils.object2String(memberList);
		joinListJson = JsonUtils.object2String(joinList);
		blackListJson = JsonUtils.object2String(blackList);
		return false;
	}

	@Override
	public boolean onUpdate(Session arg0) throws CallbackException {
		return onSave(arg0);
	}
}
