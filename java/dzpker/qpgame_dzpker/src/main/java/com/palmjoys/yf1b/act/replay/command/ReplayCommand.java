package com.palmjoys.yf1b.act.replay.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.treediagram.nina.console.ConsoleBean;
import org.treediagram.nina.console.ConsoleCommand;

import com.palmjoys.yf1b.act.replay.manager.ReplayManager;

@Component
@ConsoleBean
public class ReplayCommand {
	@Autowired
	private ReplayManager recordManager;
	
	@ConsoleCommand(name = "gm_replay_save_video", 
			description = "测试保存一帧数据")
	public void gm_replay_save_video(){
	}
}
