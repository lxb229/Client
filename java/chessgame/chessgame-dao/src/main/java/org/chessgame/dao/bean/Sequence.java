package org.chessgame.dao.bean;

import java.io.Serializable;

/**
 * 各个集合的序列
 * @author 不能
 *
 */
public class Sequence implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9207342732456742154L;

	/**
	 * 其它collection名字
	 */
	private String collectionName;
	
	/**
	 * 其它collection的自增长字段最大序号
	 */
	private int cnt;

	public String getCollectionName() {
		return collectionName;
	}

	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}

	public int getCnt() {
		return cnt;
	}

	public void setCnt(int cnt) {
		this.cnt = cnt;
	}
	
	
}
