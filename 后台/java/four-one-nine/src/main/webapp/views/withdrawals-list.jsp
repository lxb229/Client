<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<body>
	<!--主体内容-->
	<div class="page-main">
		<!--统计组-->
		<div class="main-statistics clearfix">
			<div class="item fl border-test">
				<div class="clearfix">
					<dl class="fl item-content">
						<dt class="title">平台流水</dt>
						<dd class="number">&yen;${count.statements }</dd>
					</dl>
				</div>
			</div>
			<div class="item fl border-test">
				<div class="clearfix">
					<dl class="fl item-content">
						<dt class="title">已提现金额</dt>
						<dd class="number">&yen;${count.cash_count }</dd>
					</dl>
				</div>
			</div>
			<div class="item fl border-test">
				<div class="clearfix">
					<dl class="fl item-content">
						<dt class="title">提现次数</dt>
						<dd class="number">${count.cash_num }</dd>
					</dl>
				</div>
			</div>
			<div class="item fl border-test">
				<div class="clearfix">
					<dl class="fl item-content">
						<dt class="title">平台余额</dt>
						<dd class="number">&yen;${count.balance }</dd>
					</dl>
				</div>
			</div>
		</div>
		
		<!--表格信息-->
		<div class="table">
			<form class="form">
				<input type="hidden" name="page" value="1">
				<!-- 搜索条件 -->
				<div class="main-search hasTitle rel">
					<span class="title abs">提现列表</span>
					<div class="input-group">
						<span class="input-group-addon pointer" id="basic-addon1">
							<i class="glyphicon glyphicon-search"></i>
						</span> <input type="text" name="text" class="form-control"
							placeholder="输入关键词" aria-describedby="basic-addon1">
					</div>
				</div>

				<ul class="nav nav-tabs tabs">
					<li role="presentation" class="active"><a href="javascript:" data="1">待处理</a></li>
					<li role="presentation"><a href="javascript:" data="2">已同意</a></li>
					<li role="presentation"><a href="javascript:" data="3">已拒绝</a></li>
					<li role="presentation"><a href="javascript:" data="4">全部</a></li>
					<input type="hidden" id="type" name="type" value="1" />
				</ul>

				<div class="check-box-group">
					<span class="check-all">
						<span class="check-box"></span>全选
					</span> 
					<span class="dmui-btn btn-arguee cash_audit_btn" data="1">同意</span>
					<span class="dmui-btn btn-cancel cash_audit_btn" data="0">拒绝</span>
				</div>

				<!--表格元素-->
				<table class="dmui-table">
					<tr class="menu">
						<th field="id" type="id" colspan="2">ID</th>
						<th field="name" colspan="2" befour_img="picture">申请人</th>
						<th field="phone">电话</th>
						<th field="account_type">账号类型</th>
						<th field="account">账号</th>
						<th field="money">提现金额</th>
						<th field="apply_time" type="date">申请时间</th>
						<th field="status" type="map" map="cash_operation()">操作</th>
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
	return '${pageContext.request.contextPath}/cash/list/';
}
// 获取查询参数
function getParams() {
	return $('.form').serialize();
}
//列表操作列信息
function cash_operation() {
	var data = {
			1: '<span class="dmui-btn btn-sure cash_audit_single" data="1"><i class="glyphicon glyphicon-ok"></i></span><span class="dmui-btn btn-cancel cash_audit_single" data="0"><i class="glyphicon glyphicon-remove"></i></span>',
			2: '<span class="dmui-btn btn-sure cash_audit_single" data="1"></span>',
			'defaultVale':''
	};
	return data;
}
</script>
<script>
// 单个审核通过
$(document).off('click', '.table .table_data .cash_audit_single').on('click', '.table .table_data .cash_audit_single', function(){
	var id = getLineId($(this));
	var data = {"ids": id, "result": $(this).attr('data')};
	var url = '${pageContext.request.contextPath}/cash/audit/';
	if(businessPost(url, data).success) {
		search(getUrl(), getParams()); 
	}
});

// 批量审核
$('.cash_audit_btn').click(function() {
	var ids = getCheckedIds();
	if(ids.length != 0) {
		var data = {"ids":ids,"result": $(this).attr('data')};
		var url = '${pageContext.request.contextPath}/cash/audit/';
		if(businessPost(url, data).success) {
			search(getUrl(), getParams()); 
		}
	}
});

</script>

