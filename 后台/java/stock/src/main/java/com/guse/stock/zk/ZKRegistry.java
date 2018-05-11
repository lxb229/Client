package com.guse.stock.zk;

import java.util.Date;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName: ZKRegistry
 * @Description: zookeeper 注册信息
 * @author Fily GUSE
 * @date 2017年9月13日 下午7:11:52
 * 
 */
public class ZKRegistry {
	private final static Logger logger = LoggerFactory.getLogger(ZKRegistry.class);
	
	// zookeeper 服务地址
	private String zkAddress;
	// zookeeper 根节点
	private String zkRootPath;
	// zookeeper session过期时间
	private int zkSessionTimeout;
	// 当前服务ip地址
	private String localIP;
	// 当前服务访问端口
	private int nettyPort;

	/**
	 * @Title: register
	 * @Description: 注册到zookeeper
	 * @param @param nodes
	 * @return void
	 * @throws
	 */
	public void register() throws Exception{
		logger.info("Zookeeper connect. connectString:{},sessionTimeout:{}", zkAddress, zkSessionTimeout);
		// 创建一个与服务器的连接
		ZooKeeper zk = new ZooKeeper(zkAddress, zkSessionTimeout, new Watcher() {
					public void process(WatchedEvent event) {
						logger.info("Zookeeper Watcher trigger");
					}
				});
		if (zk != null) {
			// 判断目录节点是否存在
			Stat stat = zk.exists(zkRootPath, true);
			if (stat == null) {
				String path = zk.create(zkRootPath, "this is stock nodes".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
				logger.info("Zookeeper create CreateMode.PERSISTENT Source node. nodePath:{}", path);
			}
			String node = zkRootPath + "/" + localIP;
			String data = localIP + ":" + nettyPort;
			String path = zk.create(node, data.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
			logger.info("Zookkeeper create node. nodePath:{}, data:{}", path, data);
		}
	}

	public String getZkAddress() {
		return zkAddress;
	}

	public void setZkAddress(String zkAddress) {
		this.zkAddress = zkAddress;
	}

	public String getZkRootPath() {
		return zkRootPath;
	}

	public void setZkRootPath(String zkRootPath) {
		this.zkRootPath = zkRootPath;
	}

	public int getZkSessionTimeout() {
		return zkSessionTimeout;
	}

	public void setZkSessionTimeout(int zkSessionTimeout) {
		this.zkSessionTimeout = zkSessionTimeout;
	}

	public String getLocalIP() {
		return localIP;
	}

	public void setLocalIP(String localIP) {
		this.localIP = localIP;
	}

	public int getNettyPort() {
		return nettyPort;
	}

	public void setNettyPort(int nettyPort) {
		this.nettyPort = nettyPort;
	}
	
	
	public static void main(String[] args) {
		ZKRegistry zk = new ZKRegistry();
		zk.setZkAddress("127.0.0.1:2181");
		zk.setZkRootPath("/stock");
		zk.setZkSessionTimeout(5000);
		zk.setLocalIP(new Date().getTime()+"");
		zk.setNettyPort(8111);
		try {
			zk.register();
			System.in.read();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
