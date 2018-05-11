package com.guse.chessgame.resethandler.dispose;

/** 
* @ClassName: IdleDispose 
* @Description: 处理心跳信息
* @author Fily GUSE
* @date 2017年8月20日 上午12:51:09 
*  
*/
public class IdleDispose extends AbstractDispose{

	
	/* 
	 * 重写父类方法，心跳包不需要解析数据
	 * @see com.guse.chessgame.resethandler.dispose.AbstractDispose#decodeMessage()
	 */
	@Override
	protected void decodeMessage() {
	}

	@Override
	void processing() {
		msg.setData("");
	}

}
