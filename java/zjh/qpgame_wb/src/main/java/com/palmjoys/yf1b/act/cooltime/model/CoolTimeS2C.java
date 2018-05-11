package com.palmjoys.yf1b.act.cooltime.model;

import java.util.ArrayList;

/*服务器发送到客户端消息*/
public class CoolTimeS2C {
	
	static CoolTimeS2C cooltimeS2;
	
	private CoolTimeS2C(){
		
	}
	public static CoolTimeS2C getInstance(){
		if(cooltimeS2 == null)
			cooltimeS2 = new CoolTimeS2C();
		
		return cooltimeS2;
	}
	
	public class CoolResetTimeAttrib{
		//下次重置时间
		public long nextTime;
		//冷却余下时间(单位秒)
		public long coolTime;
		//自上次重置到现在经过的时间(单位秒)
		public long interval;
		//本次是否重置
		public boolean bReset;
		//重置功能id
		public int resetId;
	}
	
	public class CoolResetTimeAllInfo{
	
		private ArrayList<CoolResetTimeAttrib> allInfo;

		public ArrayList<CoolResetTimeAttrib> getAllInfo() {
			return allInfo;
		}

		public void setAllInfo(ArrayList<CoolResetTimeAttrib> allInfo) {
			this.allInfo = allInfo;
		}
		
		public void addItem(long nextTime, long coolTime, long interval, boolean bReset, int resetId){
			CoolResetTimeAttrib attrib = new CoolResetTimeAttrib();
			attrib.bReset = bReset;
			attrib.coolTime = coolTime;
			attrib.interval = interval;
			attrib.nextTime = nextTime;
			attrib.resetId = resetId;
			
			allInfo.add(attrib);
		}
		
		public CoolResetTimeAllInfo(){
			allInfo = new ArrayList<CoolResetTimeAttrib>();
		}
	}
	
	public CoolResetTimeAllInfo valueOf(){
		CoolResetTimeAllInfo info = new CoolResetTimeAllInfo();
		
		return info;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
