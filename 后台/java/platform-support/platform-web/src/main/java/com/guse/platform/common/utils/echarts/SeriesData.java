package com.guse.platform.common.utils.echarts;

import java.io.Serializable;

public class SeriesData implements Serializable {
	
	private static final long serialVersionUID = -8759596123706086651L;
	
	private String name;
	
	private Integer value;
	
	private Object objValue;
	
	public SeriesData(String name,Integer value){
		this.name = name;
		this.value = value;
	}
	public SeriesData(String name,Object objValue){
		this.name = name;
		this.objValue = objValue;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
	}
	public Object getObjValue() {
		return objValue;
	}
	public void setObjValue(Object objValue) {
		this.objValue = objValue;
	}
	
}
