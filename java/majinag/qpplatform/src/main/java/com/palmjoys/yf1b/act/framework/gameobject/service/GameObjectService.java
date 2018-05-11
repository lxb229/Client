package com.palmjoys.yf1b.act.framework.gameobject.service;

import com.palmjoys.yf1b.act.framework.gameobject.model.GameObject;
import com.palmjoys.yf1b.act.framework.gameobject.model.ServiceType;

public interface GameObjectService {
	//物体服务类型
	public ServiceType serviceType();
	//检查物体是否够
	public int checkEnough(Long traget, GameObject gameObject);
	//增加物体
	public int increase(Long traget, GameObject gameObject);
	//减少物体
	public int decrease(Long traget, GameObject gameObject);
}
