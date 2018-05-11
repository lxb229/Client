package com.palmjoys.yf1b.act.framework.common.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 模块定义
 */
public enum Module {
	//账号模块
	ACCOUNT("account", 1),
	//钱包模块
	WALLET("wallet", 2),
	//邮件模块
	MAIL("mail", 3),
	//公告消息模块
	NOTICE("notice", 4),
	//聊天消息模块
	CHAT("chat", 5),
	//活动模块
	ACTIVITY("activity", 6),
	//热点模块
	HOTPROMPT("hotprompt", 7),	
	//商城
	MALL("mall", 8),
	//帮会模块
	CORPS("corps", 9),	
	//录像回放模块
	REPLAY("replay", 10),
	//游戏大厅模块
	LOBBY("lobby", 11),
	//德州游戏模块
	DZPKER("dzpker", 12);
	
	
	/* 编号到模块转换 */
	private final static Map<Integer, Module> id2module;
	/* 最大编号 */
	private static int maxId;

	/**
	 * 初始化
	 */
	static {
		id2module = new HashMap<Integer, Module>();

		for (Module module : Module.values()) {
			id2module.put(module.getId(), module);
			if (maxId < module.getId()) {
				maxId = module.id;
			}
		}
	}

	/**
	 * 创建模块
	 */
	public static Module valueOf(int id) {
		Module result = id2module.get(id);
		if (result == null) {
			throw new IllegalArgumentException("无效的模块标识[" + id + "]");
		}
		return result;
	}

	/**
	 * 获取模块个数
	 */
	public static int getMaxId() {
		return maxId;
	}

	/**
	 * 获取模块列表
	 */
	public static Collection<Module> getModules() {
		return id2module.values();
	}

	/**
	 * 模块名
	 */
	private final String value;

	/**
	 * 模块标识
	 */
	private final int id;

	/**
	 * 创建模块枚举
	 * 
	 * @param value
	 *            模块名
	 * @param id
	 *            模块标识
	 */
	private Module(String value, int id) {
		this.value = value;
		this.id = id;
	}

	/**
	 * 获取模块名
	 */
	public String getValue() {
		return value;
	}

	/**
	 * 获取模块标识
	 */
	public int getId() {
		return id;
	}

	/**
	 * 通过模块名获取模块id
	 */
	public static int getId(String moudelname) {
		for (Module module : Module.values()) {

			if (module.getValue().equalsIgnoreCase(moudelname)) {
				return module.getId();
			}
		}
		return -1;
	}
}
