package com.palmjoys.yf1b.act.replay.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.treediagram.nina.console.ConsoleBean;
import org.treediagram.nina.console.ConsoleCommand;

import com.palmjoys.yf1b.act.replay.manager.RecordManager;

@Component
@ConsoleBean
public class ReplayCommand {
	@Autowired
	private RecordManager recordManager;
	
	@ConsoleCommand(name = "replay_command_save_video", 
			description = "测试保存一帧数据")
	public void replay_command_save_video(){
		recordManager.recordReplayFrame(null);
	}
}
