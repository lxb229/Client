package org.chessgame.dao.bean;

import java.io.Serializable;

public class Product implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5105441945935982239L;
	
	/**
	 * 产品类型 
	 * 1：房卡
	 */
	private int product_type;
	
	/**
	 * 名称
	 */
	private String name;
	
	/**
	 * 价格
	 */
	private double price;
	
	/**
	 * 数量
	 */
	private int number;

	/**
	 * 状态 
	 * 1：上架 0：下架
	 */
	private int status;

	public int getProduct_type() {
		return product_type;
	}

	public void setProduct_type(int product_type) {
		this.product_type = product_type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}


}
