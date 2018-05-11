package com.palmjoys.yf1b.act.framework.common.manager;

import java.util.Collection;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;


@Component
public class ApplicationCloseManager implements ApplicationContextAware{
	private static ApplicationCloseManager _instance = new ApplicationCloseManager();
	private static ApplicationContext g_context = null;
	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		g_context = context;	
	}
	
	private ApplicationCloseManager(){
	}
	
	public static  ApplicationCloseManager Instance(){
		return _instance;
	}
	
	public void closeApplication(){
		/*
		Collection<OrderSellManager> orderSellmanagers = g_context.getBeansOfType(OrderSellManager.class).values();
		OrderSellManager sellManager = null;
		if(null != orderSellmanagers){
			for(OrderSellManager ss : orderSellmanagers){
				sellManager = ss;
				break;
			}
		}
		*/
	}
	
}
