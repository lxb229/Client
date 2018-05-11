package com.guse.stock.dao.model;

/** 
* @ClassName: Game 
* @Description: 游戏信息
* @author Fily GUSE
* @date 2017年8月29日 下午5:15:25 
*  
*/
public class Game {
	
	/** 
	* @Fields game_id : 主键
	*/
	private Long game_id;
	
	/** 
	* @Fields name : 游戏名称
	*/
	private String name;
	
	/** 
	* @Fields identify : 游戏app内部id
	*/
	private String identify;
	
	public Game() { }
	
	public Game(String name, String identify) {
		this.name = name;
		this.identify = identify;
	}

	public Long getGame_id() {
		return game_id;
	}

	public void setGame_id(Long game_id) {
		this.game_id = game_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdentify() {
		return identify;
	}

	public void setIdentify(String identify) {
		this.identify = identify;
	}
	

}
