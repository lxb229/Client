package com.guse.apple_reverse.service.query;

import io.netty.channel.Channel;

import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;

import com.guse.apple_reverse.netty.handler.session.ClientSession;
import com.guse.apple_reverse.netty.handler.session.Message;

/** 
* @ClassName: ClientComparator 
* @Description: 客服端排序
* @author Fily GUSE
* @date 2017年11月30日 下午3:02:32 
*  
*/
public class ClientComparator implements Comparator<Map.Entry<Channel, ClientSession>>{
	private int query_type;
	public ClientComparator(int query_type) {
		this.query_type = query_type;
	}
	//升序排序
    public int compare(Entry<Channel, ClientSession> o1,
            Entry<Channel, ClientSession> o2) {
    	ClientSession c1 = o1.getValue();
    	ClientSession c2 = o2.getValue();
    	// 计算权重
    	Integer o1Num = (c1.getThreadNum() - c1.getUseNum());
    	if((c1.checkedQueryType(query_type)) 
    			|| (query_type==Message.SERVER_QUERY_APPLE && c1.checkedQueryType(Message.SERVER_QUERY_ALL))) {
    		o1Num += 1000; // 满足查询条件 加分
    	}
    	// 计算权重
    	Integer o2Num = (c2.getThreadNum() - c2.getUseNum());
    	if((c2.checkedQueryType(query_type)) 
    			|| (query_type==Message.SERVER_QUERY_APPLE && c2.checkedQueryType(Message.SERVER_QUERY_ALL))) {
    		o2Num += 1000; // 满足查询条件 加分
    	}
    	return o2Num.compareTo(o1Num);
    }
}
