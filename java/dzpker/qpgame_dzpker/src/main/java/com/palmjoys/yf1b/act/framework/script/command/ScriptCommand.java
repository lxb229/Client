package com.palmjoys.yf1b.act.framework.script.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.treediagram.nina.console.ConsoleBean;
import org.treediagram.nina.console.ConsoleCommand;
import org.treediagram.nina.network.model.Result;

import com.palmjoys.yf1b.act.framework.script.manager.ScriptManager;

@Component
@ConsoleBean
public class ScriptCommand {
	@Autowired
	private ScriptManager scriptManager;

	@ConsoleCommand(name = "gm_script_reload_script", description = "重新加载指定脚本文件")
	public Object gm_script_reload_script(String scriptName){
		scriptManager.reload(scriptName);
		return Result.valueOfSuccess();
	}
	
	@ConsoleCommand(name = "gm_script_reload_script_all", description = "重新加载所有脚本文件")
	public Object gm_script_reload_script_all(){
		scriptManager.reloadAll();
		return Result.valueOfSuccess();
	}
	
	
	@ConsoleCommand(name = "gm_script_test_script_run", description = "脚本执行测试")
	public Object gm_script_test_script_run(String scriptName, String methodName, Object ...args){
		Object res = scriptManager.callScript(scriptName, methodName, args);
		return Result.valueOfSuccess(res);
	}
}
