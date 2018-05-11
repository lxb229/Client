<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<body>
	<!--主体内容-->
	<div class="page-main">
		<!--统计组-->
		<div class="main-statistics clearfix">
			<div class="item fl border-test">
				<div class="clearfix">
					<dl class="fl item-content">
						<dt class="title">总用户</dt>
						<dd class="number">${count.register_num }</dd>
					</dl>
				</div>
			</div>
			<div class="item fl border-test">
				<div class="clearfix">
					<dl class="fl item-content">
						<dt class="title">认证用户</dt>
						<dd class="number">${count.real_num }</dd>
					</dl>
				</div>
			</div>
			<div class="item fl border-test">
				<div class="clearfix">
					<dl class="fl item-content">
						<dt class="title">今日新增人数</dt>
						<dd class="number">${toDayCount.register_num }</dd>
					</dl>
				</div>
			</div>
		</div>

		<!--表格信息-->
		<div class="table">
			<form class="form">
				<input type="hidden" name="page" value="1">
				<!--搜索组-->
				<div class="main-search hasTitle rel">
					<span class="title abs">用户列表</span>
					<div class="input-group">
						<span class="input-group-addon pointer" id="basic-addon1">
							<i class="glyphicon glyphicon-search"></i>
						</span> <input name="text" type="text" class="form-control"
							placeholder="输入关键词" aria-describedby="basic-addon1">
					</div>
				</div>

				<ul class="nav nav-tabs tabs">
					<li role="presentation" <c:if test="${type ==0 }"> class="active" </c:if>><a href="javascript:" data="0">全部</a></li>
					
					<li role="presentation" <c:if test="${type ==1 }"> class="active" </c:if>><a href="javascript:" data="1">卖家</a></li>
					<li role="presentation" <c:if test="${type ==2 }"> class="active" </c:if>><a href="javascript:" data="2">已冻结</a></li>
					<input type="hidden" id="type" name="type" value="${type }" />
				</ul>

				<div class="check-box-group">
					<span class="check-all">
						<span class="check-box"></span>全选</span>
					<span class="dmui-btn btn-arguee" onclick="freeze(1)">解冻</span>
					<span class="dmui-btn btn-cancel" onclick="freeze(0)">冻结</span>
				</div>

				<!--表格-->
				<table class="dmui-table">
					<tr>
						<th field="id" type="id" colspan="2">ID</th>
						<th field="nick_name" class="user-td user_detail " colspan="2" befour_img="picture" >用户名</th>
						<th field="phone">电话</th>
						<th field="real_certification" type="map" map="{1:'认证用户',0:'未认证用户'}">用户类型</th>
						<th field="alive_day">活跃天数</th>
						<th field="total">平台花销</th>
						<th field="registe_time" type="date">注册时间</th>
						<th field="status" type="map" map="user_operation()">操作</th>
					</tr>
					<tbody class="table_data"></tbody>
				</table>
				<!--分页元素-->
				<div class="main-page pages"></div>
			</form>
		</div>
	</div>
</body>
<script src="${pageContext.request.contextPath}/views/libs/js/page.js"></script>
<script type="text/javascript">
	search(getUrl(), getParams());
	// 获取列表访问地址
	function getUrl() {
		return '${pageContext.request.contextPath}/user/search';
	}
	// 获取查询参数
	function getParams() {
		return $('.form').serialize();
	}
	//列表操作列信息
	function user_operation() {
		var data = {
				1:'<img class="freeze_operation_btn" data="0" src="${pageContext.request.contextPath}/views/libs/images/freeze.png"; title="冻结"/>', 
				0:'<img class="freeze_operation_btn" data="1" src="${pageContext.request.contextPath}/views/libs/images/unfreeze.png" title="解冻"/>'
		};
		return data;
	}
</script>

<script>
//单个用户冻结/解冻
$(document).off('click', '.table .table_data .freeze_operation_btn').on('click', '.table .table_data .freeze_operation_btn', function(){
	var id = getLineId($(this));
	var data = {"ids": id, "result": $(this).attr('data')};
	var url = '${pageContext.request.contextPath}/user/freeze';
	if(businessPost(url, data).success) {
		search(getUrl(), getParams()); 
	}
});

// 批量 冻结/解冻
function freeze(result) {
	var ids = getCheckedIds();
	if(ids.length != 0) {
		var data = {"ids":ids,"result": result};
		var url = '${pageContext.request.contextPath}/user/freeze';
		if(businessPost(url, data).success) {
			search(getUrl(), getParams()); 
		}
	}
}

// 跳转详情
$(document).off('click', '.table .table_data .user_detail').on('click', '.table .table_data .user_detail', function(){
	var id = getLineId($(this));
	gotoPage('${pageContext.request.contextPath}/user/detail/'+id);
});
</script>