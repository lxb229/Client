<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<div class="row guild-detaile">
	<div class="col-xs-3">
		<div class="main-statistics clearfix">
			<div class="item fl border-test">
				<div class="clearfix">
					<dl class="fl item-content">
						<dt class="title">总收益</dt>
						<dd class="number">&yen;${count.total }</dd>
					</dl>
				</div>
			</div>
			<div class="item fl border-test">
				<div class="clearfix">
					<dl class="fl item-content">
						<dt class="title">已入账</dt>
						<dd class="number">&yen;${count.collected }</dd>
					</dl>
				</div>
			</div>
			<div class="item fl border-test">
				<div class="clearfix">
					<dl class="fl item-content">
						<dt class="title">未入账</dt>
						<dd class="number">&yen;${count.uncollected }</dd>
					</dl>
				</div>
			</div>
		</div>
	</div>
	<div class="col-xs-9">
		<div class="dmui-panel">
			<div class="dmui-panel-header rel">
				<span class="tab-tit active">本周收益</span> <span class="tab-tit ">本月收益</span>
				<span class="tab-tit ">本年收益</span>
			</div>
			<div class="dmui-panel-body">
				<div id="main" style="width: 800px; height:260px;"></div>
			</div>
		</div>
	</div>
</div>


<!--表格信息-->
<div class="table">
	<form class="form">
		<input type="hidden" name="page" value="1">
		
		<div class="check-box-group">
			<span class="check-all"> <span class="check-box"></span>全选
			</span> <span class="dmui-btn btn-blur">导出表格</span>
		</div>
		<!--表格-->
		<table class="dmui-table">
			<tr>
				<th field="id" type="id">ID</th>
				<th field="buy_name">用户名称</th>
				<th field="server_name">所购服务</th>
				<th field="publish_name">服务者</th>
				<th field="total">购买金额</th>
				<th field="tip_money">小费</th>
				<th field="settle_status" type="map" map="{0:'未入账',1:'已入账'}">收款状态</th>
				<th field="buy_time" type="date">发布时间</th>
				<th field="order_status" type="map" map="{0:'未完成',1:'已完成'}">订单状态</th>
			</tr>
			<tbody class="table_data"></tbody>
		</table>
		<!--分页元素-->
		<div class="main-page pages"></div>
	</form>
</div>

<script>
	$(function() {
		console.log($("#main").width());
		// 基于准备好的dom，初始化echarts实例
		var myChart = echarts.init(document.getElementById('main'));

		data = [ [ "1", 56 ], [ "2", 86 ], [ "3", 126 ], [ "4", 89 ],
				[ "5", 110 ], [ "6", 150 ], [ "7", 129 ], [ "8", 80 ],
				[ "9", 150 ], [ "10", 129 ], [ "11", 80 ], [ "12", 80 ],
				[ "13", 150 ], [ "14", 129 ], [ "15", 80 ], ];

		var dateList = data.map(function(item) {
			return item[0];
		});
		var valueList = data.map(function(item) {
			return item[1];
		});

		option = {
			// Make gradient line here
			visualMap : [ {
				show : false,
				type : 'continuous',
				seriesIndex : 0,
				min : 0,
				max : 400
			} ],

			title : [ {
				left : 'center',
				text : 'Gradient along the y axis'
			} ],
			tooltip : {
				trigger : 'axis'
			},
			xAxis : [ {
				data : dateList
			} ],
			yAxis : [ {
				splitLine : {
					show : false
				}
			} ],
			grid : [ {
				bottom : '20',
				top : "10",
			} ],
			series : [ {
				type : 'line',
				smooth : true,
				lineStyle : {
					normal : {
						color : "#F182C7",
						width : 3,
						shadowColor : 'rgba(241,130,199,.8)',
						shadowBlur : 30,
						shadowOffsetY : 15
					}
				},
				showSymbol : false,
				data : valueList
			} ]
		};

		// 使用刚指定的配置项和数据显示图表。
		myChart.setOption(option);
	});
</script>

<script src="${pageContext.request.contextPath}/views/libs/js/page.js"></script>
<script type="text/javascript">
	search(getUrl(), getParams());
	// 获取列表访问地址
	function getUrl() {
		return '${pageContext.request.contextPath}/union/orders/${id}';
	}
	// 获取查询参数
	function getParams() {
		return $('.form').serialize();
	}
</script>