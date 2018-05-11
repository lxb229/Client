package com.guse.stock.mongo;

/** 
* @ClassName: MongoConnectUtil 
* @Description: MongoDb 连接工具
* @author Fily GUSE
* @date 2017年10月10日 上午11:54:15 
*  
*/
public class MongoConnectUtil {

	private String host;
	private int port;
	private String dbname;
	private String username;
	private String password;
	
	/** 
	* 构造方法
	*/
	public MongoConnectUtil(String host, int port,String dbname, String username, String password) {
		this.host = host;
		this.port = port;
		this.dbname = dbname;
		this.username = username;
		this.password = password;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getDbname() {
		return dbname;
	}

	public void setDbname(String dbname) {
		this.dbname = dbname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
