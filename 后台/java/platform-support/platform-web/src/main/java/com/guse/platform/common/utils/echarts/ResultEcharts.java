package com.guse.platform.common.utils.echarts;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * echarts 返回数据
 * 
 * 这里做简单的封装 如果数据太繁琐，前端处理起来困难 可使用 com.github.abel533.echarts.Option
 * 来直接封装全部option部分
 * 
 * @author nbin
 * @date 2017年7月27日 下午2:59:25
 * @version V1.0
 */
public class ResultEcharts implements java.io.Serializable {

	private static final long serialVersionUID = 4885400039195764019L;

	/** 横坐标 */
	private List<String> xAxis;//

	/** 系列 多条 值 （int类型的值） */
	private List<List<Integer>> seriesInt;
	/** 系列 多条 值 （Long类型的值） */
	private List<List<Long>> seriesLong;
	/** 系列 多条 值 （string类型的值） */
	private List<List<String>> seriesStr;
	/** 系列 多条 值 （Float类型的值） */
	private List<List<Float>> seriesFloat;
	/** 系列 多条 值 （Double类型的值） */
	private List<List<Double>> seriesDouble;
	
	private List<List<Object>> seriesObj;
	/**
	 * 系列的值为 list map结构
	 */
	private List<SeriesData> seriesData;
	
	private List<List<SeriesData>> seriesKvData;
	
	private List<String> legend;
	
	public List<String> getLegend() {
		return legend;
	}

	public void setLegend(List<String> legend) {
		this.legend = legend;
	}

	public List<String> getxAxis() {
		return xAxis;
	}

	public void setxAxis(List<String> xAxis) {
		this.xAxis = xAxis;
	}

	public List<List<Integer>> getSeriesInt() {
		return seriesInt;
	}

	public void setSeriesInt(List<List<Integer>> seriesInt) {
		this.seriesInt = seriesInt;
	}

	public List<List<String>> getSeriesStr() {
		return seriesStr;
	}

	public void setSeriesStr(List<List<String>> seriesStr) {
		this.seriesStr = seriesStr;
	}
	public List<List<Long>> getSeriesLong() {
		return seriesLong;
	}

	public void setSeriesLong(List<List<Long>> seriesLong) {
		this.seriesLong = seriesLong;
	}
	public List<SeriesData> getSeriesData() {
		return seriesData;
	}

	public void setSeriesData(List<SeriesData> seriesData) {
		this.seriesData = seriesData;
	}
	
	public List<List<Float>> getSeriesFloat() {
		return seriesFloat;
	}

	public void setSeriesFloat(List<List<Float>> seriesFloat) {
		this.seriesFloat = seriesFloat;
	}

	public List<List<Double>> getSeriesDouble() {
		return seriesDouble;
	}

	public void setSeriesDouble(List<List<Double>> seriesDouble) {
		this.seriesDouble = seriesDouble;
	}
	
	public List<List<Object>> getSeriesObj() {
		return seriesObj;
	}

	public void setSeriesObj(List<List<Object>> seriesObj) {
		this.seriesObj = seriesObj;
	}
	
	public List<List<SeriesData>> getSeriesKvData() {
		return seriesKvData;
	}

	public void setSeriesKvData(List<List<SeriesData>> seriesKvData) {
		this.seriesKvData = seriesKvData;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
