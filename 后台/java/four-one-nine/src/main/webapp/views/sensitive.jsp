<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
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
						</span> <input type="text" name="text" class="form-control"
							placeholder="输入关键词" aria-describedby="basic-addon1">
					</div>
				</div>

				<div class="check-box-group">
					<span class="check-all"> 
						<span class="check-box"></span>全选
					</span> 
					<span class="dmui-btn btn-cancel">删除</span> 
					<span class="dmui-btn btn-add fr" id="add_sensitive_btn">新建</span>
				</div>

				<!--表格-->
				<table class="dmui-table">
					<tr class="menu">
						<th field="id" type="id">ID</th>
						<th field="word_group">敏感字</th>
						<th field="replace_word">替换字</th>
						<th field="count_no">使用次数</th>
						<th field="create_time" type="date">创建时间</th>
						<th data="sensitive_operation">操作</th>
					</tr>
					<tbody class="table_data"></tbody>
				</table>

				<!--分页元素-->
				<div class="main-page pages"></div>
			</form>
		</div>
	</div>

	<!--模态框-->
	<div class="modal fade" id="save_sensitive_modal">
	<form id="sensitive_info">
		<input type="hidden" name="id">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
				</div>
				<div class="modal-body">
					<p>
						<b>敏感词</b>（多个请用“，”号隔开）
					</p>
					<input name="word_group" type="text" placeholder="输入">
					<p>
						<b>替换词</b>（多个请用“，”号隔开）
					</p>
					<input name="replace_word" type="text" placeholder="输入">
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-primary J_btn-primary" id="save_sensitive_btn">确定</button>
				</div>
			</div>
		</div>
	</form>
	</div>

	<!-- 确认删除模态框 -->
	<div class="modal fade" id="del_sensitive">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header"></div>
				<div class="modal-body text-center">
					<p class="prompt-title">
						<b>温馨提示</b>
					</p>
					<div class="prompt-content">内容删除后不可恢复，你再考虑考虑呗？</div>
				</div>
				<div class="modal-footer">
					<div class="text-center">
						<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
						<button type="button" class="btn btn-primary J_btn-primary" data-dismiss="modal">确定</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
<script src="${pageContext.request.contextPath}/views/libs/js/page.js"></script>
<script type="text/javascript">
	search(getUrl(), getParams());
	// 获取列表访问地址
	function getUrl() {
		return '${pageContext.request.contextPath}/sensitive/search/';
	}
	// 获取查询参数
	function getParams() {
		return $('.form').serialize();
	}
	//列表操作列信息
	var sensitive_operation = '<span class="dmui-btn btn-edit update_sensitive"><i class="glyphicon glyphicon-pencil"></i></span>'
			+ '<span class="dmui-btn btn-cancel"><i class="glyphicon glyphicon-trash"></i></span>';
</script>

<!-- 业务方法 -->
<script>
// 打开新建敏感词模态框
$('#add_sensitive_btn').click(function() {
	$('#sensitive_info')[0].reset();
	$('#save_sensitive_modal').modal('show');
});

// 确认保存敏感词
$('#save_sensitive_btn').click(function() {
	$('#save_sensitive_modal').modal('hide');
	
	var data = $('#sensitive_info').serialize();
	var url = '${pageContext.request.contextPath}/sensitive/save';
	if(businessPost(url, data).success) {
		search(getUrl(), getParams());
	} else {
		$('#save_sensitive_modal').modal('show');
	}
});

//修改敏感词
$(document).off('click', '.table .table_data .update_sensitive').on('click', '.table .table_data .update_sensitive', function(){
	$('#sensitive_info')[0].reset();
	var id = getLineId($(this));
	// 获取信息
	$.post('${pageContext.request.contextPath}/sensitive/info/'+id, function(response){
		$('#sensitive_info input[name=id]').val(response.id);
		$('#sensitive_info input[name=word_group]').val(response.word_group);
		$('#sensitive_info input[name=replace_word]').val(response.replace_word);
		
		$('#save_sensitive_modal').modal('show');
	});
});

</script>