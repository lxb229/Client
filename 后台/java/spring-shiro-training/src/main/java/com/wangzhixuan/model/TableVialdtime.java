package com.wangzhixuan.model;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 房间解散时长设置
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-23
 */
@TableName("table_vialdtime")
public class TableVialdtime extends Model<TableVialdtime> {

    private static final long serialVersionUID = 1L;

    /**
     * 表主键
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 过期时间(分钟数)
     */
	@TableField("vild_times")
	private Integer vildTimes;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getVildTimes() {
		return vildTimes;
	}

	public void setVildTimes(Integer vildTimes) {
		this.vildTimes = vildTimes;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
