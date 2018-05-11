package com.guse.stock.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.guse.stock.dao.model.GamePar;

/** 
* @ClassName: IGameParDao 
* @Description: 游戏档位数据接口
* @author Fily GUSE
* @date 2017年8月30日 下午3:39:53 
*  
*/
@Repository
public interface IGameParDao {
	
	/** 
	* @Title: findByIdentify 
	* @Description: 根据档位app内部id获取档位信息 
	* @param @param identify
	* @param @return
	* @return GamePar 
	* @throws 
	*/
	@Select("select * from pl_game_par t where t.identify = #{identify} limit 1")
	public GamePar findByIdentify(@Param("identify")String identify);
	
	/** 
	* @Title: findById 
	* @Description: 根据id查询 
	* @param @param id
	* @param @return
	* @return GamePar 
	* @throws 
	*/
	@Select("select * from pl_game_par where par_id=#{id}")
	public GamePar findById(@Param("id")long id);
	
	/** 
	* @Title: findByParam 
	* @Description: 自定义参数查询 
	* @param @param param
	* @param @return
	* @return GamePar 
	* @throws 
	*/
	@Select("select * from pl_game_par where ${param} limit 1")
	public GamePar findByParam(@Param("param")String param);
	
	/** 
	* @Title: findByGameAndIdentify 
	* @Description: 游戏id和档位app内部id获取档位信息 
	* @param @param game_id
	* @param @param identify
	* @param @return
	* @return GamePar 
	* @throws 
	*/
	@Select("select * from pl_game_par t where t.game_id=#{0} and t.identify=#{1} limit 1")
	public GamePar findByGameAndIdentify(Long game_id, String identify);
	
	/** 
	* @Title: addGamePar 
	* @Description: 添加游戏档位 
	* @param @param gamePar
	* @return void 
	* @throws 
	*/
	@Insert("insert into pl_game_par(game_id, identify, par, virtual_currency, is_show) "
			+ "values(#{game_id},#{identify},#{par},#{virtual_currency}, #{is_show})")
	@Options(useGeneratedKeys=true, keyProperty="par_id")//添加该行，id将被自动添加
	public void addGamePar(GamePar gamePar); 
	
	/** 
	* @Title: findByGame 
	* @Description: 根据游戏id获取档位信息 
	* @param @param game_id
	* @param @return
	* @return List<GamePar> 
	* @throws 
	*/
	@Select("select IFNULL(par, '0') par,IFNULL(virtual_currency, '未知') par_des from pl_game_par t "
			+ "where t.game_id=#{game_id}")
	public List<Map<String, Object>> findByGame(@Param("game_id")Long game_id);
	

}
