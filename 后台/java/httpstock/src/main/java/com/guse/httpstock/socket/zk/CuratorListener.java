package com.guse.httpstock.socket.zk;

import java.util.Collections;
import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.InitializingBean;

/** 
* @ClassName: CuratorListener 
* @Description: Curator监听事件
* @author Fily GUSE
* @date 2017年9月19日 下午3:31:31 
*  
*/
public class CuratorListener implements InitializingBean{
	
	// zk服务器连接信息
	private String zkConnectionString;
	// 监听地址
	private String path;

	public void setZkConnectionString(String zkConnectionString) {
		this.zkConnectionString = zkConnectionString;
	}

	public void setPath(String path) {
		this.path = path;
	}

	/* 
	 * spring启动初始化执行信息
	 */
	@SuppressWarnings("resource")
	@Override
	public void afterPropertiesSet() throws Exception {
		CuratorFramework zkClient = CuratorFrameworkFactory.newClient(zkConnectionString, new RetryNTimes(10, 5000));
		zkClient.start();
		try {
			// 订阅某一个服务
			 Stat stat = zkClient.checkExists().forPath(path);
			 if (stat == null) {
				 // 如果父节点不存在就先创建父节点
				 zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path);
			 }

			// 初始化
			updateAddsList(zkClient);

			PathChildrenCache cache = new PathChildrenCache(zkClient, path, true);
			// 在初始化的时候就进行缓存监听
			cache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
			cache.getListenable().addListener(new PathChildrenCacheListener() {
				public void childEvent(CuratorFramework client,
						PathChildrenCacheEvent event) throws Exception {
					// 重新获取子节点
					updateAddsList(client);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/** 
	* @Title: updateAddsList 
	* @Description: 更新节点信息 
	* @param @param client
	* @return void 
	* @throws 
	*/
	private void updateAddsList(CuratorFramework client) {
		// 重新获取子节点
		try {
			List<String> addsList = client.getChildren().forPath(path);
			if (!addsList.isEmpty()) {
				for (int r = 0; r < addsList.size(); r++) {
					addsList.set(r, new String(client.getData().forPath(path + "/" + addsList.get(r))));
				}
				// 排序一下子节点
				Collections.sort(addsList);
				System.out.println("======================");
				System.out.println(addsList);
			} else {
				System.out.println("-----------------------");
			}
			CuratorUtil.setAddsList(addsList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		CuratorListener cu = new CuratorListener();
		cu.path = "/stock";
		cu.zkConnectionString = "127.0.0.1:2181";
		
		try {
			cu.afterPropertiesSet();
			System.in.read();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
