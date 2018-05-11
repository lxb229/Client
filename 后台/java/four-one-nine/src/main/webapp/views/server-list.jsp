<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<style>
.classify_color {
	color: blue;
}
</style>
<body>
	<!--主体内容-->
	<div class="page-main">

		<!--表格信息-->
		<div class="table">
			<form class="form">
				<input type="hidden" name="page" value="1">
				<!--搜索组-->
				<div class="main-search rel">
					<div class="input-group">
						<span class="input-group-addon pointer" id="basic-addon1">
							<i class="glyphicon glyphicon-search"></i>
						</span> <input name="text" type="text" class="form-control" placeholder="输入关键词"
							aria-describedby="basic-addon1">
					</div>
				</div>

				<ul class="nav nav-tabs tabs">
					<c:forEach items="${classifys }" var="item">
					<li role="presentation"><a href="javascript:" data="${item.id }">${item.classify_name }</a></li>
					</c:forEach>
					<li role="presentation"><a href="javascript:" data="-1">未分类</a></li>
					<li role="presentation" class="active"><a href="javascript:" data="0">全部</a></li>
					<input type="hidden" id="type" name="type" value="0" />
				</ul>

				<div class="check-box-group">
					<span class="check-all">
						<span class="check-box"></span>全选
					</span>
					<span class="dmui-btn btn-arguee" onclick="setClassify()">归类</span>
				</div>

				<!--表格-->
				<table class="dmui-table">
					<tr class="menu">
						<th field="id" type="id">ID</th>
						<th field="name" class="server_detail">服务名称</th>
						<th field="classify_name" class="classify_color">分类</th>
						<th field="nick_name">卖家名</th>
						<th field="sell_num">成交单数</th>
						<th field="price">单价</th>
						<th field="comment_num">评价</th>
						<th field="tip_money">累计小费</th>
						<th field="publish_time" type="date">发布时间</th>
						<th data="server_operation">操作</th>
					</tr>
					<tbody class="table_data"></tbody>
				</table>

				<!--分页-->
				<div class="main-page pages"></div>
			</form>
		</div>

	</div>

	<!--模态框-->
	<div class="modal fade" id="classification_model">
		<form id="add_classify_form">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header"></div>
				<div class="modal-body">
					<p>
						<b>分类名称</b>（必填）
					</p>
					<select name="classCode" >
					<c:forEach items="${classifys }" var="item">
                        <option value="${item.classify_code }">${item.classify_name }</option>
                        </c:forEach>
                     </select>
					<input type="hidden" name="ids" />
					<p>
						<b>已选择服务</b>
					</p>
					<ul class="clearfix"></ul>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-primary" onclick="add_classify_submit()">确定</button>
				</div>
			</div>
		</div>
		</form>
	</div>
</body>

<script src="${pageContext.request.contextPath}/views/libs/js/page.js"></script>
<script>
search(getUrl(), getParams()); 
//获取列表访问地址
function getUrl() {
	return '${pageContext.request.contextPath}/server/list';
}
//获取查询参数
function getParams() {
	return $('.form').serialize();
}
//列表操作列信息
var server_operation = '<span class="dmui-btn btn-cancel"><i class="glyphicon glyphicon-trash"></i></span>';
</script>

<script>
// 归类
function setClassify() {
	// 获取选择的信息
	var ids = getCheckedIds();
	if(ids.length > 0) {
		// 设置模态框信息
		$('#add_classify_form')[0].reset();
		// 设置内容
		$('#add_classify_form input[name=ids]').val(ids);
		$('#add_classify_form .modal-body ul').empty();
		$('.table .table_data .checked').each(function() {
			var serverName = $(this).closest('tr').children('td').eq(1).text();
			var li = '<li class="col-xs-3"><span class="serve-name ofh">'+serverName+'</span>'
				+'<i class="icon-del" data="'+$(this).attr('data')+'">X</i></li>';
			$('#add_classify_form .modal-body ul').append(li);
		});
		// 显示模态框
		$('#classification_model').modal('show');
	}
};
// 提交数据
function add_classify_submit() {
	var data = $('#add_classify_form').serialize();
	var url = '${pageContext.request.contextPath}/server/add_serverClassify';
	$('#classification_model').modal('hide');
	if(businessPost(url, data).success) {
		gotoMenu('服务列表');
	}
}
// 归类时删除选中服务
$(document).off('click', '#add_classify_form .modal-body ul .icon-del').on('click', '#add_classify_form .modal-body ul .icon-del', function(){
	// 从ids 删除id
	var id = ","+$(this).attr('data')+",";
	var ids = ","+$('#add_classify_form input[name=ids]').val()+",";
	ids = ids.replace(id, ',');
	if(ids.length == 1) {
		$('#add_classify_form input[name=ids]').val('');
	} else {
		$('#add_classify_form input[name=ids]').val(ids.substring(1, ids.length - 1));
	}
	// 移除当前元素
	$(this).parent().remove();
});


// 服务详情跳转
$(document).off('click', '.table .table_data .server_detail').on('click', '.table .table_data .server_detail', function(){
	var id = getLineId($(this));
	gotoPage('${pageContext.request.contextPath}/server/detail_page/'+id);
});

</script>
