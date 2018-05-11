package com.palmjoys.yf1b.act.framework.gameobject.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public enum ServiceType {
	//钱包类型
	WALLET("WALLET", 1);

	/* 编号到模块转换 */
	private final static Map<Integer, ServiceType> id2module;
	/* 最大编号 */
	private static int maxId;

	/**
	 * 初始化
	 */
	static {
		id2module = new HashMap<Integer, ServiceType>();

		for (ServiceType module : ServiceType.values()) {
			id2module.put(module.getId(), module);
			if (maxId < module.getId()) {
				maxId = module.id;
			}
		}
	}

	/**
	 * 创建模块
	 */
	public static ServiceType valueOf(int id) {
		ServiceType result = id2module.get(id);
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
	public static Collection<ServiceType> getModules() {
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
	private ServiceType(String value, int id) {
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
		for (ServiceType module : ServiceType.values()) {
			if (module.getValue().equalsIgnoreCase(moudelname)) {
				return module.getId();
			}
		}
		return -1;
	}

}
