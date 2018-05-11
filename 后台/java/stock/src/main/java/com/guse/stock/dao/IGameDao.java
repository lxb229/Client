package com.guse.stock.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.guse.stock.dao.model.Game;

/** 
* @ClassName: IGameDao 
* @Description: 游戏数据接口
* @author Fily GUSE
* @date 2017年8月29日 下午5:08:22 
*  
*/
@Repository
public interface IGameDao {
	
	/** 
	* @Title: findByIdentify 
	* @Description: 根据游戏id获取游戏信息 
	* @param @param identify
	* @param @return
	* @return Game 
	* @throws 
	*/
	@Select("select * from pl_game where identify=#{identify} limit 1")
	public Game findByIdentify(String identify);
	
	/** 
	* @Title: addGame 
	* @Description: 保存游戏 
	* @param @param game
	* @return void 
	* @throws 
	*/
	@Insert("insert into pl_game(name, identify) values(#{name}, #{identify})")
	@Options(useGeneratedKeys=true, keyProperty="game_id")//添加该行，id将被自动添加
	public void addGame(Game game);
	
	/** 
	* @Title: findAllCount 
	* @Description: 获取总条数 
	* @param @return
	* @return Integer 
	* @throws 
	*/
	@Select("select count(1) total from pl_game")
	public Integer findAllCount();
	
	/** 
	* @Title: findAll 
	* @Description: 获取全部游戏信息  
	* @param @param start 开始条数
	* @param @param showNum 每页显示数
	* @param @return
	* @return List<Map<String,Object>> 
	* @throws 
	*/
	@Select("select IFNULL(name, '未知') name, identify from pl_game order by game_id desc limit #{start}, #{showNum}")
	public List<Map<String, Object>> findAll(@Param("start")int start,@Param("showNum") int showNum);
	

}
