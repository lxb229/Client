<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

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
				<th field="nick_name" colspan="2" befour_img="picture">用户名称</th>
				<th field="total">购买金额</th>
				<th field="tip_money">小费</th>
				<th field="income_status" type="map" map="{0:'未入账',1:'已入账'}">收款状态</th>
				<th field="buy_time" type="date">购买时间</th>
				<th field="order_status" type="map" map="{0:'未完成',1:'已完成'}">订单状态</th>
			</tr>
			<tbody class="table_data"></tbody>
		</table>
		<!--分页元素-->
		<div class="main-page pages"></div>
	</form>
</div>


<script src="${pageContext.request.contextPath}/views/libs/js/page.js"></script>
<script type="text/javascript">
	search(getUrl(), getParams());
	// 获取列表访问地址
	function getUrl() {
		return '${pageContext.request.contextPath}/server/orders/${id}';
	}
	// 获取查询参数
	function getParams() {
		return $('.form').serialize();
	}
</script>