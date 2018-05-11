package com.guse.apple_reverse_client.netty;

import java.util.Date;

import com.guse.apple_reverse_client.Main.ServiceStart;
import com.guse.apple_reverse_client.common.HttpUtil;

public class ChangeIPService {
	// 自动切换时间
	private int changeTime;
	
	public void run() {
		ServiceStart.CONSOLE_LOG.info("start auto change IP. time:{}", changeTime);
		// 查询线程服务器
		QueryLineService lineService = ServiceStart.factory.getBean(QueryLineService.class);
		while(true) {
			try {
				Thread.sleep(1000 * 60 * changeTime);
				ServiceStart.CONSOLE_LOG.info("break off query");
				lineService.breakOff = true;
				int useNum = 0;
				do{
					useNum = lineService.findUseNum();
					if(useNum == 0) {
						HttpUtil.sendGet("http://localhost:999/cip", new Date().getTime()+"");
					}
					Thread.sleep(1000 * 5);
				} while(useNum > 0);
				ServiceStart.CONSOLE_LOG.info("recover on query");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				lineService.breakOff = false;
			}
		}
	}

	/* get/set */
	public int getChangeTime() {
		return changeTime;
	}
	public void setChangeTime(int changeTime) {
		this.changeTime = changeTime;
	}
}
