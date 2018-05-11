package com.palmjoys.yf1b.act.framework.socket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.palmjoys.yf1b.act.framework.common.model.Module;

/**
 * 
 * @author sjz
 * 
 */
@Component
public class SocketAttachment {

	/** 记录所有模块通信的附件 */
	private Map<Long, List<Boolean>> attachments = new ConcurrentHashMap<Long, List<Boolean>>();

	public String getAttachment(Long playerId) {
		StringBuilder builder = new StringBuilder();
		List<Boolean> attachment = getAttachmentList(playerId);

		for (boolean hasNew : attachment) {
			builder.append(hasNew ? "1" : "0");
		}

		//清除所有状态
		clearNewAll(playerId);
		
		return builder.toString();
	}

	private List<Boolean> getAttachmentList(Long playerId) {
		if (attachments.containsKey(playerId)) {
			return attachments.get(playerId);
		} else {
			int maxId = Module.getMaxId();
			List<Boolean> attachment = new ArrayList<Boolean>(maxId);
			//线程同步的
			attachment = Collections.synchronizedList(attachment);

			for (int i = 0; i < maxId; i++) {
				attachment.add(i, false);
			}

			attachments.put(playerId, attachment);

			return attachment;
		}
	}

	/**
	 * 标记需求更新状态
	 */
	public void setNew(Long playerId, Module... modules) {
		List<Boolean> attachment = getAttachmentList(playerId);
		for (Module module : modules) {
			attachment.set(module.getId() - 1, true);
		}
	}

	/**
	 * 清除需求更新状态
	 */
	public void clearNew(Long playerId, Module... modules) {
		List<Boolean> attachment = getAttachmentList(playerId);
		for (Module module : modules) {
			attachment.set(module.getId() - 1, false);
		}
	}
	
	/**
	 * 清除所有更新状态
	 * */
	public void clearNewAll(Long playerId){
		List<Boolean> attachment = attachments.get(playerId);
		if(attachment != null){
			int size = attachment.size(), index;
			for(index=0; index<size; index++){
				attachment.set(index, false);
			}
		}
	}
}
