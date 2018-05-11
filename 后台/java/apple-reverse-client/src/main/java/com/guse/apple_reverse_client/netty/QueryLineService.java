package com.guse.apple_reverse_client.netty;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import com.guse.apple_reverse_client.Main.ServiceStart;
import com.guse.apple_reverse_client.netty.handler.Message;
import com.guse.apple_reverse_client.netty.handler.QueryLine;

/**
 * @ClassName: QueryLineService
 * @Description: 初始化查询线程
 * @author Fily GUSE
 * @date 2017年11月29日 下午3:45:12
 * 
 */
public class QueryLineService {
	
	// 查询线路集合
	private List<QueryLine> queryList = new ArrayList<QueryLine>();

	// 发送线程数和使用线程数
	public void sendClientThreadNum() {
		int useNum = 0;
		for (QueryLine line : queryList) {
			if (line.getStatus() == QueryLine.STATUS_BUSY) {
				useNum++;
			}
		}
		
		JSONObject json = new JSONObject();
		json.put("threadNum", queryList.size());
		json.put("useNum", useNum);
		json.put("queryType", queryType);
		json.put("name", ServiceStart.factory.getBean(EchoClient.class).getName());
		json.put("connectDate", EchoClient.connectTime);
		json.put("cIPTime", ServiceStart.factory.getBean(ChangeIPService.class).getChangeTime());
		json.put("nowDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		Message.sendMessage(new Message(Message.CLIENT_THREAD_NUM, json.toString()));
	}
	
	// 获取当前线程使用时数
	public int findUseNum() {
		int useNum = 0;
		for (QueryLine line : queryList) {
			if (line.getStatus() == QueryLine.STATUS_BUSY) {
				useNum++;
			}
		}
		return useNum;
	}

	// 中断查询控制器
	public boolean breakOff = false;
	/**
	 * @Title: getLine
	 * @Description: 获取一条查询线路
	 * @param @return
	 * @return QueryLine
	 * @throws
	 */
	public synchronized QueryLine getLine() {
		int count = 1;
		// 循环等待获取闲置线路
		while (!queryList.isEmpty()) {
			if(breakOff) {
				try {
					Thread.sleep(1000 * 10);
				} catch (InterruptedException e) {}
				continue;
			}
			for (int r = 0; r < queryList.size(); r++) {
				QueryLine line = queryList.get(r);
				// 空闲线路
				if (line.getStatus() == QueryLine.STATUS_IDLE) {
					line.setStatus(QueryLine.STATUS_BUSY);
					return line;
				}
			}
			count++;
			if (count > 15) {
				return null;
			}
			try {
				Thread.sleep(1000 * 2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * @Title: getQueryType
	 * @Description: 获取查询类型
	 * @param @param code
	 * @param @return
	 * @return String
	 * @throws
	 */
	public String getQueryType(int code) {
		if (StringUtils.isNotBlank(queryType)) {
			return JSONObject.fromObject(queryType).getString(code + "");
		}
		return null;
	}

	/**
	 * @Title: init
	 * @Description: 初始化方法
	 * @param
	 * @return void
	 * @throws
	 */
	public void init() {
		// 初始化本地查询端口
		for (int r = 0; r < threadNum; r++) {
			queryList.add(new QueryLine(12300 + r));
		}
		ServiceStart.CONSOLE_LOG.info("open query port. no.{}", queryList.size());
	}

	// 配置查询线程数
	private int threadNum;
	// 配置查询类型
	private String queryType;

	/* get/set */
	public void setThreadNum(int threadNum) {
		this.threadNum = threadNum;
	}
	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}
	public int getThreadNum() {
		return threadNum;
	}
	public String getQueryType() {
		return queryType;
	}
}
