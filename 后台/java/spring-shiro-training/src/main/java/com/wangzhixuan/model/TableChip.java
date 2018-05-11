package com.wangzhixuan.model;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 桌子配置
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-23
 */
@TableName("table_chip")
public class TableChip extends Model<TableChip> {

    private static final long serialVersionUID = 1L;

    /**
     * 表主键
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 小盲
     */
	private Integer small;
    /**
     * 大盲
     */
	private Integer big;
    /**
     * 最小加入
     */
	private Integer join;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSmall() {
		return small;
	}

	public void setSmall(Integer small) {
		this.small = small;
	}

	public Integer getBig() {
		return big;
	}

	public void setBig(Integer big) {
		this.big = big;
	}

	public Integer getJoin() {
		return join;
	}

	public void setJoin(Integer join) {
		this.join = join;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
