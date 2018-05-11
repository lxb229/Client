package com.guse.chessgame.resethandler.dispose;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/** 
* @ClassName: DisposeType 
* @Description: 消息类型泛型
* @author Fily GUSE
* @date 2017年8月20日 上午1:01:33 
*  
*/
public enum DisposeType {
	
	// 心跳协议
	Idle(10000, IdleDispose.class),
	// 登录协议
	Login(10001, LoginDispose.class),
	// 服务器心跳
	IdleServer(20000, IdleServerDispose.class)
	;
	
	private Integer pid;
	private Class<? extends AbstractDispose> disposeClass;
	private static Map<Integer,Class<? extends AbstractDispose>> DISPOSE_CLASS_MAP = new HashMap<Integer,Class<? extends AbstractDispose>>();

	public static void initDisposes() {
		Set<Integer> typeSet = new HashSet<Integer>();
		Set<Class<?>> disposes = new HashSet<>();
		for(DisposeType d:DisposeType.values()){
			Integer pid = d.getPid();
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
	
	DisposeType(Integer pid, Class<? extends AbstractDispose> disposeClass) {
		this.pid = pid;
		this.disposeClass = disposeClass;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
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
