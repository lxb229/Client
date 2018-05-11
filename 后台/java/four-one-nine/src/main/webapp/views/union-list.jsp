<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<body>
	<!--主体内容-->
	<div class="page-main">
		<!--表格信息-->
		<div class="table">
			<form class="form">
				<input type="hidden" name="page" value="1">
				<!--搜索组-->
				<div class="main-search rel" style="padding-top: 0;">
					<div class="input-group">
						<span class="input-group-addon pointer" id="basic-addon1">
							<i class="glyphicon glyphicon-search"></i>
						</span> <input name="text" type="text" class="form-control"
							placeholder="输入关键词" aria-describedby="basic-addon1">
					</div>
				</div>

				<div class="check-box-group">
					<span class="check-all">
						<span class="check-box"></span>全选
					</span> 
					<span class="dmui-btn btn-cancel" onclick="deleteUnion()">删除</span>
					<span class="dmui-btn btn-add fr add_union">新建公会</span>
				</div>

				<!--表格-->
				<table class="dmui-table">
					<tr  class="menu">
						<th field="id" type="id">ID</th>
						<th field="logo" class="union_detail" type="img" img_style="table_logo">logo</th>
						<th field="name" class="union_detail">公司名称</th>
						<th field="nick_name">会长</th>
						<th field="user_num">成员数量</th>
						<th field="create_time" type="date">成立时间</th>
						<th data="union_operation">操作</th>
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
	return '${pageContext.request.contextPath}/union/search/';
}
// 获取查询参数
function getParams() {
	return $('.form').serialize();
}
//列表操作列信息
var union_operation = '<span class="dmui-btn btn-edit union_edit_btn"><i class="glyphicon glyphicon-pencil"></i></span>'
	+'<span class="dmui-btn btn-cancel union_delete_btn"><i class="glyphicon glyphicon-trash"></i></span>';
</script>

<!-- 业务信息 -->
<script>

//单个 删除工会
$(document).off('click', '.table .table_data .union_delete_btn').on('click', '.table .table_data .union_delete_btn', function(){
	var id = getLineId($(this));
	var data = {"ids": id};
	var url = '${pageContext.request.contextPath}/union/delete';
	if(businessPost(url, data).success) {
		search(getUrl(), getParams()); 
	}
});

//批量 删除工会
function deleteUnion() {
	var ids = getCheckedIds();
	if(ids.length != 0) {
		var data = {"ids":ids};
		var url = '${pageContext.request.contextPath}/union/delete';
		if(businessPost(url, data).success) {
			search(getUrl(), getParams()); 
		}
	}
}

// 新建工会
$(".add_union").click(function() {
	gotoMenu('新建公会');
});

// 编辑工会
$(document).off('click', '.table .table_data .union_edit_btn').on('click', '.table .table_data .union_edit_btn', function(){
	var id = getLineId($(this));
	gotoPage('${pageContext.request.contextPath}/union/publish-page?unionId='+id);
});

//详情页面
$(document).off('click', '.table .table_data .union_detail').on('click', '.table .table_data .union_detail', function(){
	var id = getLineId($(this));
	gotoPage('${pageContext.request.contextPath}/union/detail/'+id);
});
</script>
