package com.palmjoys.yf1b.act.message.manager;

import org.treediagram.nina.memcache.service.Filter;

import com.palmjoys.yf1b.act.message.entity.NoticeEntity;

public class NoticeFilterManager{
	private static NoticeFilterManager _instance = new NoticeFilterManager();
	public static NoticeFilterManager Instance(){
		return _instance;
	}
	
	public NoticeFilter_Vaild createFilter(int gmflag, boolean bVaild, long vaildTime){
		NoticeFilter_Vaild ret = new NoticeFilter_Vaild(gmflag, bVaild, vaildTime);
		return ret;
	}
	
	public class NoticeFilter_Vaild implements Filter<NoticeEntity>{
		//true=得到过期消息,过滤未过期消息,false=得到未过期消息,过滤过期消息
		private boolean bVaild;
		private long vaildTime;
		private int gmflag;
		
		public NoticeFilter_Vaild(int gmflag, boolean bVaild, long vaildTime){
			this.bVaild = bVaild;
			this.vaildTime = vaildTime;
			this.gmflag = gmflag;
		}		
		
		@Override
		public boolean isExclude(NoticeEntity entity) {
			if(bVaild){
				if(vaildTime > entity.getEndTime()){
					return false;
				}
				return true;
			}else{
				if(gmflag == 0){
					if(vaildTime > entity.getStartTime() && vaildTime < entity.getEndTime()){
						return false;
					}
				}else{
					if(vaildTime < entity.getEndTime()){
						return false;
					}
				}
				return true;
			}
		}
	}

}
