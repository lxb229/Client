package com.palmjoys.yf1b.act.framework.script.manager;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.springframework.stereotype.Component;

import com.palmjoys.yf1b.act.framework.script.model.ScriptResult;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

@Component
public class ScriptManager {
	//脚本引擎
	private ScriptEngine engine = null;
	//脚本数据(KEY=脚本文件,VALUE=脚本内容)
	private Map<String, String> scriptMap = null; 
	
	@PostConstruct
	protected void init(){
		engine = new ScriptEngineManager().getEngineByName("JavaScript");
		scriptMap = new HashMap<>();
		
		reloadAll();
	}
	
	private void listAllFile(File file){
		try{			
			File flist[] = file.listFiles();
			if (flist == null || flist.length == 0) {
			    return;
			 }
			for (File f : flist) {
			    if (f.isFile()) {
			    	String fName = f.getName();
			    	int dot = fName.lastIndexOf('.');
			    	if(dot < 0 || dot >= fName.length()){
			    		continue;
			    	}
			    	String exName = fName.substring(dot);
			    	if(exName.equalsIgnoreCase(".js") == false){
			    		continue;
			    	}
			    	
			    	reload(f);
			    }else if(f.isDirectory()){
			    	listAllFile(f);
			    }
			 }
		}catch(Exception e){
		}
	}
	
	public void reload(File f){
		try{
			int fLen = (int) f.length();
	    	byte []buf = new byte[fLen];
	    	FileInputStream in = new FileInputStream(f);			    	
	    	in.read(buf);
	    	String s = new String(buf, "utf8");
	    	scriptMap.put(f.getName(), s);
	    	in.close();
	    	in = null;
	    	buf = null;
		}catch(Exception e){
		}
	}
	
	public void reload(String scriptName){
		String scriptPath = "res/resources/script/"+scriptName;
		File f = new File(scriptPath);
		reload(f);
	}
	
	public void reloadAll(){
		String scriptPath = "res/resources/script";
		File file = new File(scriptPath);
		listAllFile(file);
	}
	
	
	/**
	 * 执行指定脚本方法
	 * scriptName 脚本名称
	 * methodName 方法名称
	 * args 方法参数列表
	 * */
	public Object callScript(String scriptName, String methodName, Object... args){
		ScriptResult retObj = new ScriptResult();
		String script = scriptMap.get(scriptName);
		if(null == script){
			retObj.result = -1;
			retObj.resultObj = "未找到指定名称脚本";
			return retObj;
		}
		try{
			engine.eval(script);
			Invocable inv = (Invocable) engine;
			Object resObj = inv.invokeFunction(methodName, args);
			if(null != resObj){
				ScriptObjectMirror rsObj = (ScriptObjectMirror)resObj;
				Set<String> keys = rsObj.keySet();
				for(String key : keys){
					Object tmp = rsObj.get(key);
					tmp = null;
				}
			}
			
			retObj.result = 0;
			retObj.resultObj = resObj;
		}catch(Exception e){
			retObj.result = -1;
			retObj.resultObj = "指定脚本方法执行异常";
			return retObj;
		}
		
		return retObj;
	}
}
