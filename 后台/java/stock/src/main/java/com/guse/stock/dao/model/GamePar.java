package com.guse.stock.dao.model;

/** 
* @ClassName: GamePar 
* @Description: 游戏档位
* @author Fily GUSE
* @date 2017年8月30日 下午3:35:26 
*  
*/
public class GamePar {
	
	/** 
	* @Fields par_id : 档位id
	*/
	private Long par_id;
	/** 
	* @Fields game_id : 游戏id
	*/
	private Long game_id;
	/** 
	* @Fields identify : 档位app内部id
	*/
	private String identify;
	/** 
	* @Fields par : 面值
	*/
	private Double par;
	/** 
	* @Fields virtual_currency : 虚拟货币
	*/
	private String virtual_currency;
	/** 
	* @Fields is_show : 是否显示 1：显示 0：不显示
	*/
	private int is_show;
	
	public GamePar(){}
	
	public GamePar(Long game_id, String identify) {
		this.game_id = game_id;
		this.identify = identify;
	}

	public Long getPar_id() {
		return par_id;
	}

	public void setPar_id(Long par_id) {
		this.par_id = par_id;
	}

	public Long getGame_id() {
		return game_id;
	}

	public void setGame_id(Long game_id) {
		this.game_id = game_id;
	}

	public String getIdentify() {
		return identify;
	}

	public void setIdentify(String identify) {
		this.identify = identify;
	}

	public String getVirtual_currency() {
		return virtual_currency;
	}

	public void setVirtual_currency(String virtual_currency) {
		this.virtual_currency = virtual_currency;
	}

	public int getIs_show() {
		return is_show;
	}

	public void setIs_show(int is_show) {
		this.is_show = is_show;
	}

	public Double getPar() {
		return par;
	}

	public void setPar(Double par) {
		this.par = par;
	}

}
