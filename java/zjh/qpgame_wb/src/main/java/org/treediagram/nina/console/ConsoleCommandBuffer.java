package org.treediagram.nina.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 控制台命令获取缓冲
 * */
public class ConsoleCommandBuffer {
	private int runMode;
	private BufferedReader _in;
	private static String _line = "";
	
	public ConsoleCommandBuffer(Console _console){
		runMode = _console.getRunMode();
		if(runMode == 1){
			//前台方式运行
			_in = new BufferedReader(new InputStreamReader(System.in));
		}
	}
	
	public static void setNetLine(String line){
		_line = line;
	}	
	
	public String readLine() throws IOException{
		String rLine = "";
		if(runMode == 1){
			//前台方式运行
			rLine = _in.readLine();
			if(rLine == null || rLine.isEmpty()){
				rLine = _line;
				_line = "";
			}
		}else{
			//后台方式运行
			rLine = _line;
			_line = "";
		}
		return rLine;
	}
	
}
