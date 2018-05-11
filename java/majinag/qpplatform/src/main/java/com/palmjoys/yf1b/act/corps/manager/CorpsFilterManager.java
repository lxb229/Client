package com.palmjoys.yf1b.act.corps.manager;

import org.treediagram.nina.memcache.service.Filter;

import com.palmjoys.yf1b.act.corps.entity.CorpsEntity;

public class CorpsFilterManager {
	private static CorpsFilterManager _instance = new CorpsFilterManager();
	
	public static CorpsFilterManager Instance(){
		return _instance;
	}
	
	public CorpsFilter_Id createFilter_Id(String corpsId){
		return new CorpsFilter_Id(corpsId);
	}
	
	public CorpsFilter_Name createFilter_Name(String corpsName){
		return new CorpsFilter_Name(corpsName);
	}
	
	public CorpsFilter_CreatePlayer createFilter_CreatePlayer(long createPlayer){
		return new CorpsFilter_CreatePlayer(createPlayer);
	}
	
	public CorpsFilter_Hidde createFilter_Hidde(int hidde){
		return new CorpsFilter_Hidde(hidde);
	}
		
	public class CorpsFilter_Id implements Filter<CorpsEntity>{
		private String corpsId;
		
		public CorpsFilter_Id(String corpsId){
			this.corpsId = corpsId;
		}

		@Override
		public boolean isExclude(CorpsEntity entity) {
			if(corpsId.equalsIgnoreCase(entity.getCorpsId())){
				return false;
			}
			return true;
		}
	}
	
	public class CorpsFilter_Name implements Filter<CorpsEntity>{
		private String corpsName;
		
		public CorpsFilter_Name(String corpsName){
			this.corpsName = corpsName;
		}

		@Override
		public boolean isExclude(CorpsEntity entity) {
			if(this.corpsName.equalsIgnoreCase(entity.getCorpsId())){
				return false;
			}
			return true;
		}
	}
	
	public class CorpsFilter_CreatePlayer implements Filter<CorpsEntity>{
		private long createPlayer;
		
		public CorpsFilter_CreatePlayer(long createPlayer){
			this.createPlayer = createPlayer;
		}

		@Override
		public boolean isExclude(CorpsEntity entity) {
			if(this.createPlayer == entity.getCreatePlayer()){
				return false;
			}
			return true;
		}
	}
	
	public class CorpsFilter_Hidde implements Filter<CorpsEntity>{
		private int hidde;
		
		public CorpsFilter_Hidde(int hidde){
			this.hidde = hidde;
		}

		@Override
		public boolean isExclude(CorpsEntity entity) {
			if(this.hidde == entity.getHidde()){
				return false;
			}
			return true;
		}
	}

}
