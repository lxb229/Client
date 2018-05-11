package com.guse.httpstock.socket.zk;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CuratorUtil {
	
	
	// stock服务器地址列表
	private static List<String> addsList = new ArrayList<String>();
	
	
	public static void setAddsList(List<String> addsList) {
		CuratorUtil.addsList = addsList;
	}
	public static List<String> getStockAdds() {
		return addsList;
	}
	
	/**
	 * 负载均衡策略(均分)
	 */
	private static final AtomicInteger sequences = new AtomicInteger();

	public static String doSelect() {
		// 取模轮循
		return addsList.isEmpty() ? null : addsList.get(sequences
				.getAndIncrement() % addsList.size());
	}
	
}
