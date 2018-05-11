<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<html lang="ch" style="background: #EDECF9; width: 100%; height: 100%;">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="author" content="huaicheng.151201@163.com">
<meta name="keywords" content="顾色,顾色科技,四川顾色科技,四川顾色科技有限公司">
<meta name="description" content="四川顾色科技有限公司">
<title>管理后台</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/views/libs/bootstrop/css/bootstrap.min.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/views/libs/css/style.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/views/libs/nav-tool/nav.css">

</head>
<body style="background: transparent;" onkeydown="keyLogin();">
	<div class="container">
		<div class="row dmui-table">
			<div class="dmui-table-cell">
				<div class="login-box">
					<div class="col-xs-6">
						<div class="login-left rel">
							<img class="abs left-image" src="${pageContext.request.contextPath}/views/libs/images/image.png" alt="">
							<div class="rel left-content text-center">
								<img class="head" src="${pageContext.request.contextPath}/views/libs/images/head_portrait.PNG" alt="">
								<h3>4月19日管理系统</h3>
								<p>亲爱的管理员，你辛苦啦！</p>
								<p>今天也要元气满满的管理平台哦</p>
							</div>
						</div>
					</div>
					<div class="col-xs-6">
						<form id="login_form">
						<div class="login-right">
							<label class="rel" for=""> 
								<span class="abs">账号</span> 
								<input type="text" name="username">
							</label> 
							<label class="rel" for=""> 
								<span class="abs">密码</span> 
								<input type="password" name="password">
							</label>
							<div class="form-button-groups">
								<button type="button" class="btn btn-primary submit_btn">登录</button>
							</div>
						</div>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 消息显示 模态框-->
	<div class="modal fade" id="base_show_message_modal">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<img class="logo" src="${pageContext.request.contextPath}/views/libs/images/top_logo.png" alt="logo">
				</div>
				<div class="modal-body"></div>
				<div class="modal-footer">
					<button type="button" class="btn btn-primary" data-dismiss="modal">确定</button>
				</div>
			</div>
		</div>
	</div>

	<script src="${pageContext.request.contextPath}/views/libs/bootstrop/js/jquery-1.11.3.js"></script>
	<script src="${pageContext.request.contextPath}/views/libs/bootstrop/js/bootstrap.min.js"></script>
	<script>
	    $(".dmui-table").height(window.innerHeight- 40);
	</script>
</body>
<script type="text/javascript">
	// 表单回车事件监听
	function keyLogin(){    
	    if (event.keyCode==13){  //回车键的键值为13  
	    	$('.submit_btn').click();
	     }    
	 } 
	$('.submit_btn').click(function() {
		var data = $('#login_form').serialize();
		var url = '${pageContext.request.contextPath}/login/login';
		$.ajax({
			type: "post",
			url: url,
			data: data,
			async: false,
			success: function(response) {
				if(response.success) {
					window.location.href='${pageContext.request.contextPath}/index/';
				} else {
					$('#base_show_message_modal .modal-body').html(response.message);
					$('#base_show_message_modal').modal('show');
				}
				
			}
		});
	});
   
	
</script>
</html>
