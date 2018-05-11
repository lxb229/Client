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
						<dt class="title">引流人数</dt>
						<dd class="number">${attract.participants }</dd>
					</dl>
				</div>
			</div>
			<div class="item fl border-test">
				<div class="clearfix">
					<dl class="fl item-content">
						<dt class="title">板块活跃人数</dt>
						<dd class="number">${attract.interview }</dd>
					</dl>
				</div>
			</div>
		</div>

		<!--表格信息-->
		<div class="table">
			<form class="form">
				<input type="hidden" name="page" value="1">

				<!--搜索组-->
				<div class="main-search rel" style="padding-top: 0;">
					<div class="input-group">
						<span class="input-group-addon pointer" id="basic-addon1">
							<i class="glyphicon glyphicon-search"></i>
						</span> <input type="text" name="text" class="form-control" placeholder="输入关键词"
							aria-describedby="basic-addon1">
					</div>
				</div>

				<div class="check-box-group">
					<span class="check-all">
						<span class="check-box"></span>全选</span> <span class="dmui-btn btn-cancel" id="activity_batch_delete">删除</span>
					<span class="dmui-btn btn-add fr" id="publishBtn">发布活动</span>
				</div>

				<!--表格-->
				<table class="dmui-table">
					<tr class="menu">
						<th field="id" type="id">ID</th>
						<th field="name" class="activity_details">活动名称</th>
						<th field="participants">参与人数</th>
						<th field="interview">浏览数</th>
						<th field="criticism">评论数</th>
						<th field="create_time" type="date">发布时间</th>
						<th data="activity_operation">操作</th>
					</tr>
					<tbody class="table_data"></tbody>
				</table>

				<!--分页-->
				<div class="main-page pages"></div>
			</form>
		</div>
	</div>
</body>
<script src="${pageContext.request.contextPath}/views/libs/js/page.js"></script>
<!-- 列表相关 -->
<script type="text/javascript">
	search(getUrl(), getParams());
	// 获取列表访问地址
	function getUrl() {
		return '${pageContext.request.contextPath}/activity/list/';
	}
	// 获取查询参数
	function getParams() {
		return $('.form').serialize();
	}
	//列表操作列信息
	var activity_operation = '<span class="dmui-btn btn-cancel activity_delete"><i class="glyphicon glyphicon-trash"></i></span>';
</script>
<script>
	// 发布活动页面
	$('#publishBtn').click(function() {
		gotoMenu('发布活动');
	});
	
	// 进入详情页面
	$(document).off('click', '.table .table_data .activity_details').on('click', '.table .table_data .activity_details', function(){
		var id = getLineId($(this));
		if(id != null && id != undefined) {
			gotoPage("${pageContext.request.contextPath}/activity/activity_info/?id="+id);
		}
	});
	
	// 批量删除活动
	$('#activity_batch_delete').click(function() {
		var ids = getCheckedIds();
		if(ids.length > 0) {
			activityDelete(ids);
		}
	});
	
	// 单个删除
	$(document).off('click', '.table .table_data .activity_delete').on('click', '.table .table_data .activity_delete', function(){
		var id = getLineId($(this));
		activityDelete(id);
	});
	
	// 删除活动信息
	function activityDelete(ids) {
		var data = {'ids': ids};
		var url = '${pageContext.request.contextPath}/activity/del';
		if(businessPost(url, data).success) {
			search(getUrl(), getParams());
		}
	}
	
</script>
