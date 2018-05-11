package com.palmjoys.yf1b.act.framework.gameobject.manager;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.palmjoys.yf1b.act.framework.gameobject.model.GameObject;
import com.palmjoys.yf1b.act.framework.gameobject.model.ServiceType;
import com.palmjoys.yf1b.act.framework.gameobject.service.GameObjectService;

@Component
public class GameObjectManager implements ApplicationContextAware{
	private ApplicationContext _applicationContext;
	private Map<ServiceType, GameObjectService> serviceList = new HashMap<ServiceType, GameObjectService>();
	
	
	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		_applicationContext = context;
	}

	@PostConstruct
	private void init(){
		for(GameObjectService service : _applicationContext.getBeansOfType(GameObjectService.class).values()){
			if(serviceList.containsKey(service.serviceType()) == false){
				serviceList.put(service.serviceType(), service);
			}
		}
	}
	
	//检查物体是否够
	public int checkEnough(Long traget, GameObject ...gos){
		int result = 0;
		for(GameObject gameObject : gos){
			ServiceType serviceType = ServiceType.valueOf(ServiceType.getId(gameObject.type));
			if(null == serviceType){
				continue;
			}
			GameObjectService service = serviceList.get(serviceType);
			if(null == service){
				continue;
			}
			int ret = service.checkEnough(traget, gameObject);
			result += ret;
		}
		if(result == gos.length){
			return 0;
		}
		return -1;
	}
	
	//增加物体
	public int increase(Long traget, GameObject ...gos){
		for(GameObject gameObject : gos){
			ServiceType serviceType = ServiceType.valueOf(ServiceType.getId(gameObject.type));
			if(null == serviceType){
				continue;
			}
			GameObjectService service = serviceList.get(serviceType);
			if(null == service){
				continue;
			}
			service.increase(traget, gameObject);
		}
		return 0;
	}
	//减少物体
	public int decrease(Long traget, GameObject ...gos){
		int result = 0;
		for(GameObject gameObject : gos){
			ServiceType serviceType = ServiceType.valueOf(ServiceType.getId(gameObject.type));
			if(null == serviceType){
				continue;
			}
			GameObjectService service = serviceList.get(serviceType);
			if(null == service){
				continue;
			}
			int ret = service.decrease(traget, gameObject);
			result += ret;
		}
		if(result == gos.length){
			return 0;
		}
		return -1;
	}
	
	
	
}
