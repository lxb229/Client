package com.guse.stock.netty.handler.dispose;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.guse.stock.netty.handler.dispose.service.CheckGearsDispose;
import com.guse.stock.netty.handler.dispose.service.CheckVerDispose;
import com.guse.stock.netty.handler.dispose.service.GetgameDispose;
import com.guse.stock.netty.handler.dispose.service.GetgearsDispose;
import com.guse.stock.netty.handler.dispose.service.IdleDispose;
import com.guse.stock.netty.handler.dispose.service.InputDispose;
import com.guse.stock.netty.handler.dispose.service.LoginDispose;
import com.guse.stock.netty.handler.dispose.service.LogoutDispose;
import com.guse.stock.netty.handler.dispose.service.OutputDispose;
import com.guse.stock.netty.handler.dispose.service.RollbackDispose;
import com.guse.stock.netty.handler.dispose.service.TapeoutDispose;
import com.guse.stock.netty.handler.dispose.service.UpdateDispose;

/** 
* @ClassName: DisposeType 
* @Description: 消息类型泛型
* @author Fily GUSE
* @date 2017年8月20日 上午1:01:33 
*  
*/
public enum DisposeType {
	
//	// 心跳协议
	Idle(0, IdleDispose.class),
	// 登录协议
	Login(100, LoginDispose.class),
	// 入库
	Input(1, InputDispose.class),
	// 出库
	Output(2, OutputDispose.class),
	// 获取游戏
	Getgame(3, GetgameDispose.class),
	// 获取档位
	Getgears(4, GetgearsDispose.class),
	// 修改信息(废弃)
	Update(5, UpdateDispose.class),
	// 版本监测(未定义)
	Checkver(6, CheckVerDispose.class),
	// 库存监测
	Checkgears(7, CheckGearsDispose.class),
	// 库存回滚
	Rollback(8,RollbackDispose.class),
	// 通知下线(httpStock服务器调用接口)
	Tapeout(20, TapeoutDispose.class),
	// 注销
	Logout(200, LogoutDispose.class)
	;
	
	private Integer pt;
	private Class<? extends AbstractDispose> disposeClass;
	private static Map<Integer,Class<? extends AbstractDispose>> DISPOSE_CLASS_MAP = new HashMap<Integer,Class<? extends AbstractDispose>>();

	// 初始化加载协议
	static {
		Set<Integer> typeSet = new HashSet<Integer>();
		Set<Class<?>> disposes = new HashSet<Class<?>>();
		for(DisposeType d:DisposeType.values()){
			Integer pid = d.getPt();
			if(typeSet.contains(pid)){
				throw new IllegalStateException("dispose type 协议类型重复"+pid);
			}
			Class<?> dispose = d.getDisposeClass();
			if (disposes.contains(dispose)) {
				throw new IllegalStateException("dispose定义重复"+d);
			}
			DISPOSE_CLASS_MAP.put(pid,d.getDisposeClass());
			typeSet.add(pid);
			disposes.add(dispose);
		}
	}
	
	DisposeType(Integer pt, Class<? extends AbstractDispose> disposeClass) {
		this.pt = pt;
		this.disposeClass = disposeClass;
	}

	public Integer getPt() {
		return pt;
	}

	public void setPt(Integer pt) {
		this.pt = pt;
	}

	public Class<? extends AbstractDispose> getDisposeClass() {
		return disposeClass;
	}

	public void setDisposeClass(Class<? extends AbstractDispose> disposeClass) {
		this.disposeClass = disposeClass;
	}
	
	public static  Class<? extends AbstractDispose> getDisposeClassBy(Integer pid){
		return DISPOSE_CLASS_MAP.get(pid);
	}


}
