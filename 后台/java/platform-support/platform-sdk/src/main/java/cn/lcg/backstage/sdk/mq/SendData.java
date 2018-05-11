package cn.lcg.backstage.sdk.mq;

/**
 * 发送数据到消息队列接口
 * @author yanhua
 *
 */
public interface SendData<T> {
	
	/**
	 * 发送数据
	 * @param data 数据对象
	 * @return 返回成功或者失败
	 */
	public boolean send(T data);
	
	/**
	 * 关闭连接
	 */
	public void close();
}
