package com.wangzhixuan.model;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-02
 */
@TableName("player_lottery")
public class PlayerLottery extends Model<PlayerLottery> {

    private static final long serialVersionUID = 1L;

    /**
     * 表主键id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 玩家明星号
     */
	@TableField("start_no")
	private String startNo;
    /**
     * 奖品jsonlist
     */
	@TableField("lottery_json")
	private String lotteryJson;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getStartNo() {
		return startNo;
	}

	public void setStartNo(String startNo) {
		this.startNo = startNo;
	}

	public String getLotteryJson() {
		return lotteryJson;
	}

	public void setLotteryJson(String lotteryJson) {
		this.lotteryJson = lotteryJson;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
