package com.wangzhixuan.model;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 麻将馆轮数
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-17
 */
@TableName("mahjong_round")
public class MahjongRound extends Model<MahjongRound> {

    private static final long serialVersionUID = 1L;

    /**
     * 表主键id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 麻将馆明星号
     */
	@TableField("mahjong_no")
	private String mahjongNo;
    /**
     * 玩法类型
     */
	@TableField("game_type")
	private Integer gameType;
    /**
     * 轮数
     */
	private Integer round;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMahjongNo() {
		return mahjongNo;
	}

	public void setMahjongNo(String mahjongNo) {
		this.mahjongNo = mahjongNo;
	}

	public Integer getGameType() {
		return gameType;
	}

	public void setGameType(Integer gameType) {
		this.gameType = gameType;
	}

	public Integer getRound() {
		return round;
	}

	public void setRound(Integer round) {
		this.round = round;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
