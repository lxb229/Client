package com.wangzhixuan.model;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import java.io.Serializable;

/**
 * <p>
 * 商品仓库
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-02
 */
public class Warehouse extends Model<Warehouse> {

    private static final long serialVersionUID = 1L;

    /**
     * 表主键id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 商品
     */
	private Integer commodity;
    /**
     * 可用数量
     */
	@TableField("usable_amount")
	private Integer usableAmount;
    /**
     * 冻结待出库数量
     */
	@TableField("outbound_amount")
	private Integer outboundAmount;
    /**
     * 总数量
     */
	@TableField("all_amount")
	private Integer allAmount;
    /**
     * 已使用数量
     */
	@TableField("use_amount")
	private Integer useAmount;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCommodity() {
		return commodity;
	}

	public void setCommodity(Integer commodity) {
		this.commodity = commodity;
	}

	public Integer getUsableAmount() {
		return usableAmount;
	}

	public void setUsableAmount(Integer usableAmount) {
		this.usableAmount = usableAmount;
	}

	public Integer getOutboundAmount() {
		return outboundAmount;
	}

	public void setOutboundAmount(Integer outboundAmount) {
		this.outboundAmount = outboundAmount;
	}

	public Integer getAllAmount() {
		return allAmount;
	}

	public void setAllAmount(Integer allAmount) {
		this.allAmount = allAmount;
	}

	public Integer getUseAmount() {
		return useAmount;
	}

	public void setUseAmount(Integer useAmount) {
		this.useAmount = useAmount;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
