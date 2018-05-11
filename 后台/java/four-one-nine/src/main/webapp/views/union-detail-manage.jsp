<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<div class="user-info-panel rel">
	<img class="abs user-photo" src="https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=8232468,2916696848&fm=27&gp=0.jpg" alt=""> 
	<span class="dmui-btn btn-blur fr rel" id="update_union_btn" style="top: 0px; margin-right: 0px;">修改</span>
	<h3>${union.union_name }</h3>
	<p>
		创建时间:
		<fmt:formatDate value="${union.create_time }" pattern="yyyy-MM-dd" />
	</p>
	<h4>分红比例</h4>
	<div class="row clearfix">
		<div class="col-lg-3">
			<div class="bonus">
				<span class="user-role">会长</span> <span class="number fr">${union.income_ratio_clo }%</span>
			</div>
		</div>
		<div class="col-lg-3">
			<div class="bonus">
				<span class="user-role">成员</span> <span class="number fr">${union.income_ratio_member }%</span>
			</div>
		</div>
	</div>

</div>
<!--表格信息-->
<div class="table">
	<form class="form">
		<input type="hidden" name="page" value="1">
		<!--搜索组-->
		<div class="main-search hasTitle rel">
			<span class="title abs">成员列表</span>
			<div class="input-group">
				<span class="input-group-addon pointer" id="basic-addon1"> <i
					class="glyphicon glyphicon-search"></i>
				</span> <input name="text" type="text" class="form-control"
					placeholder="输入关键词" aria-describedby="basic-addon1">
			</div>
		</div>

		<div class="check-box-group">
			<span class="check-all"><span class="check-box"></span>全选</span> <span
				class="dmui-btn btn-blur fr mr-0">邀请成员</span>
		</div>

		<!--表格-->
		<table class="dmui-table">
			<tr>
				<th field="id" type="id">ID</th>
				<th field="nick_name" colspan="2" befour_img="picture">成员名称</th>
				<th field="phone">电话</th>
				<th field="title" class="position">职位</th>
				<th field="income">累积收入</th>
				<th field="create_time" type="date">加入时间</th>
			</tr>
			<tbody class="table_data"></tbody>
		</table>
		<!--分页元素-->
		<div class="main-page pages"></div>
	</form>
</div>

<!--模态框-->
<div class="modal fade" id="edit_position_model">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
			</div>
			<div class="modal-body">
				<p class="text-center">修改职位</p>
				<div class="dmui-btn btn-blur">会长</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary set-position-btn">确定</button>
			</div>
		</div>
	</div>
</div>
<script src="${pageContext.request.contextPath}/views/libs/js/page.js"></script>
<script type="text/javascript">
	search(getUrl(), getParams());
	// 获取列表访问地址
	function getUrl() {
		return '${pageContext.request.contextPath}/union/users/${id}';
	}
	// 获取查询参数
	function getParams() {
		return $('.form').serialize();
	}
</script>

<script>
// 编辑工会信息
$('#update_union_btn').click(function() {
	gotoPage('${pageContext.request.contextPath}/union/publish-page?unionId=${id}');
});

var clo_user;
// 职位修改模态框
$(document).off('click', '.table .table_data .position').on('click', '.table .table_data .position', function(){
	clo_user = getLineId($(this));
	$('#edit_position_model').modal('show');
});
// 确认设置为会长
$('#edit_position_model .set-position-btn').click(function() {
	$('#edit_position_model').modal('hide');
	var data = {'userId': clo_user};
	var url = '${pageContext.request.contextPath}/union/${id}/set_position';
	if(businessPost(url, data).success) {
		search(getUrl(), getParams());
	}
});
</script>