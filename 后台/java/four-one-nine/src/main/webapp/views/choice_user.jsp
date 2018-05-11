<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!--表格信息-->
<div class="table">
	<form class="form" id="choice_user_form">
		<input type="hidden" name="page" value="1">
		<input type="hidden" name="where" value="${where }" />
		<h3 style="margin-top: 0">选择用户</h3>
		<div class="main-search rel">
			<div class="input-group">
				<span class="input-group-addon pointer"> <i
					class="glyphicon glyphicon-search"></i>
				</span> <input type="text" name="text" class="form-control"
					placeholder="输入关键词" aria-describedby="basic-addon1">
			</div>
		</div>
		<div class="check-box-group" style="margin-top: 0px;">
			<span class="check-all"><span class="check-box"></span>全选</span> <span
				class="dmui-btn btn-arguee mr-0" onclick="return_choice_users()">确认添加</span>
		</div>
		<!--表格-->
		<table class="dmui-table">
			<tr class="menu">
				<th field="user_id" type="id" colspan="2">ID</th>
				<th field="nick_name" colspan="2" befour_img="head_picture">用户名</th>
				<th field="phone">电话</th>
				<th field="seller_certification" type="map" map="{1:'卖家',0:'买家'}">用户类型</th>
				<th field="registe_time" type="date">注册时间</th>
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
		return '${pageContext.request.contextPath}/user/choice_user/search';
	}
	// 获取查询参数
	function getParams() {
		return $('.form').serialize();
	}
</script>

<script>
	// 确认选中信息
	function return_choice_users() {
		var choice = true;
		$('#choice_user_form .table_data .checked').each(function() {
			${callbackFunction}($(this).closest('tr'));
			choice = false;
		});

		if (choice) {
			$('#base_show_message_modal .modal-body').html('请选择信息');
			$('#base_show_message_modal').modal('show');
		} else {
			// 关闭弹窗
			$('#choice_user_form').closest('.modal.fade').modal('hide');
		}
	}
</script>