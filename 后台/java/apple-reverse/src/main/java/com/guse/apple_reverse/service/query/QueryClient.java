package com.guse.apple_reverse.service.query;

import io.netty.channel.Channel;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.guse.apple_reverse.Main.ServiceStart;
import com.guse.apple_reverse.netty.handler.session.ClientSession;
import com.guse.apple_reverse.netty.handler.session.ClientSessionUtil;
import com.guse.apple_reverse.netty.handler.session.Message;

/** 
* @ClassName: QueryClient 
* @Description: TODO
* @author Fily GUSE
* @date 2017年12月27日 下午4:37:05 
*  
*/
public class QueryClient {
	
	// 需要执行查询的信息
	List<Message> list; 
	// 回调信息监听接口
	ListeningQuery listen;
	public QueryClient(List<Message> list, ListeningQuery listen) {
		this.list = list;
		this.listen = listen;
	}
	
	public void run() {
		// 开始执行查询
		for(Message msg : list) {
			// 发送成功标记
			sendQuery(msg);
			// 第一次发送的时候都要记录
			listen.markQuery(msg, new Date().getTime());
		}
		// 等待5秒后判断信息是否有回调
		try {
			Thread.sleep(1000 * 5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// 获取监听列表
		List<QueryBean> querys = listen.getQuerys();
		if(querys != null && querys.size() > 0) {
			boolean waitRespond = true;
			while(waitRespond) {
				waitRespond = false;
				for(QueryBean bean : querys) {
					// 没有响应
					if(!bean.isRespond()) {
						// 3次请求失败
						if(bean.getQueryNum() > 3) {
							bean.setRespond(true);
							// 发送失败信息
							Message msg = bean.getMsg();
							msg.setResutlCode(Message.RESULT_FAIL);
							msg.setResultCodeMsg("查询服务器未开启");
							sendQuery(msg);
						} else {
							waitRespond = true;
							// 超过5秒没有响应，重发请求
							if((new Date().getTime() - bean.getQueryTime()) > 1000 * 5 * bean.getQueryNum()) {
								bean.setQueryNum(bean.getQueryNum() + 1);
								sendQuery(bean.getMsg());
							}
						}
					}
				}
			}
			
		}
	}
	
	/** 
	* @Title: sendQuery 
	* @Description: 发送查询信息 
	* @param @param msg
	* @return void 
	* @throws 
	*/
	private boolean sendQuery(Message msg) {
		if(msg.getResutlCode() != null && msg.getResutlCode() == Message.RESULT_FAIL) {
			listen.queryFail(msg);
			return false;
		}
		// 获取一条查询路线
		Map<String, Object> json = ClientSessionUtil.getQueryChannel(msg.getType());
		if(json != null) {
			// 设置查询连接
			Channel channel = (Channel) json.get("channel");
			msg.setType((int)json.get("query_type"));
			// 发送命令到客服端
			ClientSession client = (ClientSession) json.get("client");
			ServiceStart.INFO_LOG.info("start exector query.client:{}, query:{}, data:{}"
					,client.getName(), client.findQueryTypeName(msg.getType()), msg.getData());
			Message.sendMessage(channel, msg);
			
			return true;
		} else { // 没有查询线路时
			msg.setResutlCode(Message.RESULT_FAIL);
			msg.setResultCodeMsg("查询服务器未开启");
			listen.queryFail(msg);
		}
		return false;
	}

}
