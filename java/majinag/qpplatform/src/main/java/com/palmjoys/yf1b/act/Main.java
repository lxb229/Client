package com.palmjoys.yf1b.act;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.treediagram.nina.console.Console;

import com.palmjoys.yf1b.act.majiang.model.CardAttrib;
import com.palmjoys.yf1b.act.majiang.model.GameDefine;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;


/**
 * @author neal
 *
 */
public class Main {

	/** 日志 */
	private static final Logger logger = LoggerFactory.getLogger(Main.class);
	/** 应用程序上下文位置 */
	private static final String DEFAULT_APPLICATION_CONTEXT = "file:res/app-context.xml";
	
	/**
	 * 入口方法
	 */
	public static void main(String[] args) {
		int test = 0;
		if(test==1){
			
			List<List<CardAttrib>> resultCards = new ArrayList<>();			
			List<CardAttrib> allSuitCards = GameDefine.getSuitCards(GameDefine.SUIT_TYPE_TIAO);
			allSuitCards.addAll(GameDefine.getSuitCards(GameDefine.SUIT_TYPE_TONG));
			int allN = allSuitCards.size();
			int allM = 2;
			int b[] = new int[allM];			
			GameDefine.combine(allSuitCards, allN, allM, b, allM, resultCards);  
			
			return;
		}
		
		String sConsoleState = "";
		if(args.length > 0){
			sConsoleState = args[0];
		}
		
		// 配置LOGBack
		LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
		try {
			JoranConfigurator configurator = new JoranConfigurator();
			configurator.setContext(context);
			context.reset();
			configurator.doConfigure("res/logback.xml");
		} catch (JoranException e) {
		}
		
		StatusPrinter.printInCaseOfErrorsOrWarnings(context);

		final ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				DEFAULT_APPLICATION_CONTEXT);
		try {
			logger.info("游戏服务启动完毕, 开启服务器控制台, 控制台控制状态={}", sConsoleState);
			// 启动控制台
			Console console = new Console(applicationContext);
			console.start(sConsoleState);
		} catch (Exception e) {
			e.printStackTrace();

			if (applicationContext.isRunning()) {
				applicationContext.destroy();
			}
		}
	}
}
