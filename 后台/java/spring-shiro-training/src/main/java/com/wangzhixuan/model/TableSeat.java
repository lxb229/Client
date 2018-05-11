package com.wangzhixuan.model;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 桌子手牌
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-23
 */
@TableName("table_seat")
public class TableSeat extends Model<TableSeat> {

    private static final long serialVersionUID = 1L;

    /**
     * 表主键
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 昵称
     */
	private String nick;
    /**
     * 手牌
     */
	private String cards;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getCards() {
		return cards;
	}

	public void setCards(String cards) {
		this.cards = cards;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
