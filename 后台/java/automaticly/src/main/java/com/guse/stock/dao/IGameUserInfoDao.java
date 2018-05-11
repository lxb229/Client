package com.guse.stock.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.guse.stock.dao.model.GameUserInfo;

/** 
* @ClassName: IGameUserInfoDao 
* @Description: 保存用户游戏信息
* @author Fily GUSE
* @date 2017年9月27日 下午5:42:22 
*  
*/
@Repository
public interface IGameUserInfoDao {
	
	/** 
	* @Title: addGamePar 
	* @Description: 添加游戏档位 
	* @param @param gamePar
	* @return void 
	* @throws 
	*/
	@Insert("insert into pl_game_user_info(user_id,game_id, qq, weixin, info) "
			+ "values(#{user_id},#{game_id},#{qq},#{weixin},#{info})")
	@Options(useGeneratedKeys=true, keyProperty="id")//添加该行，id将被自动添加
	public void addGameUserInfo(GameUserInfo gameInfo); 
	
	/** 
	* @Title: updateInfo 
	* @Description: 更新信息 
	* @param @param info
	* @return void 
	* @throws 
	*/
	@Update("update pl_game_user_info set info=#{info}, qq=#{qq},weixin=#{weixin} where id=#{id}")
	public void updateInfo(GameUserInfo gameInfo);
	
	/** 
	* @Title: getUserInfo 
	* @Description: 获取用户信息，根据用户id、游戏id和 微信或QQ帐号(自己拼装) 
	* @param @param user_id
	* @param @param game_id
	* @param @param account
	* @param @return
	* @return GameUserInfo 
	* @throws 
	*/
	@Select("select * from pl_game_user_info where user_id=#{user_id} and game_id=#{game_id}"
			+ " and ${account} order by id desc limit 1")
	public GameUserInfo getUserInfo(@Param("user_id")long user_id, @Param("game_id")long game_id
			,@Param("account")String account);
	
	/** 
	* @Title: getById 
	* @Description: 根据id获取信息 
	* @param @param id
	* @param @return
	* @return GameUserInfo 
	* @throws 
	*/
	@Select("select * from pl_game_user_info where id=#{id}")
	public GameUserInfo getById(@Param("id")long id);

}
