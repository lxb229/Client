<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<html lang="ch">
<head>
    <meta charset="utf-8">
    <meta name="author" content="huaicheng.151201@163.com">
    <meta name="keywords" content="顾色,顾色科技,四川顾色科技,四川顾色科技有限公司">
    <meta name="description" content="四川顾色科技有限公司">
    <title>平台用户分析</title>
    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/libs/bootstrop/css/bootstrap.min.css">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/libs/nav-tool/nav.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/libs/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/Swiper/4.0.2/css/swiper.min.css">
    <link rel="stylesheet/less" type="text/css" href="${pageContext.request.contextPath}/views/libs/less/master.less">
    
    
    <!--[if lt IE 9]>
    <script src="https://cdn.bootcss.com/html5shiv/3.7.3/html5shiv.min.js"></script>
    <script src="https://cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body>
    <!--页眉-->
    <header class="page-header fixed border-test">
        <span class="left-logo fl">
            <img class="logo" src="${pageContext.request.contextPath}/views/libs/images/top_logo.png" alt="logo">
        </span>
        <span class="router-title fl" id="title">平台用户分析</span>
        <span class="user-panel fr">${login_name } <a href="${pageContext.request.contextPath}/login/logout">【退出】</a>
        </span>
    </header>
    <!--左侧主菜单-->
    <nav class="page-nav fixed menu">
        <ul>
            <li class="nav-item nav-show">
                <a href="javascript:" title="平台用户分析" path="index/index_count"><i class="icon icon-strc"></i> 数据统计 </a>
            </li>
            <li class="nav-item">
                <a href="javascript:" title="提现管理" path="cash"><i class="icon icon-money"></i> 提现管理 </a>
            </li>
            <li class="nav-item">
                <a href="javascript:"><i class="icon icon-activity"></i> 活动管理 <i class="right"></i></a>
                <ul>
                    <li class="nav-item"><a href="javascript:" title="活动管理" path="activity">管理活动</a></li>
                    <li class="nav-item"><a href="javascript:" title="发布活动" path="activity/publish_activity">发布活动</a></li>
                </ul>
            </li>
            <li class="nav-item">
                <a href="javascript:"><i class="icon icon-push"></i> 推送管理 <i class="right"></i></a>
                <ul>
                    <li class="nav-item"><a href="javascript:" title="推送模板" path="template">推送模板</a></li>
                    <li class="nav-item"><a href="javascript:" title="发布推送" path="template/release_page">发布推送</a></li>
                </ul>
            </li>
            <li class="nav-item">
                <a href="javascript:"><i class="icon icon-serve"></i> 服务管理 <i class="right"></i></a>
                <ul>
                    <li class="nav-item"><a href="javascript:" title="服务分类" path="server/classify_page">服务分类</a></li>
                    <li class="nav-item"><a href="javascript:" title="服务列表" path="server/list_page">服务列表</a></li>
                </ul>
            </li>
            <li class="nav-item">
                <a href="javascript:" title="用户管理" path="user"><i class="icon icon-member"></i> 用户管理</a>
            </li>
            <li class="nav-item">
                <a href="javascript:"><i class="icon icon-class"></i> 公会 <i class="right"></i></a>
                <ul>
                    <li class="nav-item"><a href="javascript:" title="管理公会" path="union">管理公会</a></li>
                    <li class="nav-item"><a href="javascript:" title="新建公会" path="union/publish-page">新建公会</a></li>
                </ul>
            </li>
            <li class="nav-item">
                <a href="javascript:" title="敏感词管理" path="sensitive"><i class="icon icon-font"></i> 敏感词管理</a>
            </li>
        </ul>
    </nav>
    <!--主体内容-->
    <div id="page_content"></div>
    
    <!-- 消息显示 模态框-->
	<div class="modal fade" id="base_show_message_modal" style="z-index:2147483647;">
		<div class="modal-dialog" style="margin: 235px auto 40px;  width:  300px;">
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
    <script src="${pageContext.request.contextPath}/views/libs/bootstrop/js/bootstrap.js"></script>
    <script src="${pageContext.request.contextPath}/views/libs/nav-tool/nav.js"></script>
    <script src="${pageContext.request.contextPath}/views/libs/echarts/echarts.simple.min.js"></script>
    <script src="${pageContext.request.contextPath}/views/libs/js/tool.js"></script>
    <script src="${pageContext.request.contextPath}/views/libs/bootstrop/js/bootstrap-datetimepicker.min.js"></script>
    <script src="//unpkg.com/wangeditor/release/wangEditor.min.js"></script>
    <script src="https://cdn.bootcss.com/less.js/3.0.0-pre.4/less.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/Swiper/4.0.2/js/swiper.min.js"></script>
    
    <!-- 引用文件上传  -->
    <c:import url="file_upload.jsp" />
    
    <script>
    	// 左侧菜单事件
    	$('.menu ul li a').click(function () {
    		if($(this).attr('title') != undefined && $(this).attr('title') != null) {
    			// 标题
    			$("#title").html($(this).attr('title'));
    			// 加载内容
    			gotoPage("${pageContext.request.contextPath}/"+$(this).attr('path') + "/");
    		}
    	});
    	// 页面跳转
    	function gotoPage(url) {
    		$.get(url, function(data) {
    			$("#page_content").empty();
    			$("#page_content").html(data);
			});
    	}
    	// 跳转到指定菜单
    	function gotoMenu(name) {
    		$('.menu ul li a[title='+name+']').click();
    	}
    	// 初始化加载页面
    	gotoMenu('平台用户分析');
    	
    	/**
    	 * 业务信息发送post请求
    	 * @param url : 请求路径(不包含根路径)
    	 * @param data: 业务参数
    	 */
    	function businessPost(url, data) {
    		var result = null;
    		$.ajax({
    			type: "post",
    			url: url,
    			data: data,
    			async: false,
    			success: function(response) {
    				result = response;
    				var message = response.message;
    				if(message == undefined || message == null) {
    					message = response.success ? '成功' : '失败';
    				}
    				$('#base_show_message_modal .modal-body').html(message);
    				$('#base_show_message_modal').modal('show');
    			}
    		});
			return result;
    	}
    	// 模态框消失监听
    	$('#base_show_message_modal').on('hidden.bs.modal', function() {
    		
    	});
    	
    	// 表单回车事件监听
    	$(document).on("keydown", "input", function(e){
    		if(e.keyCode == 13) {
    			(e || event).preventDefault();
    			var oNav_span = $(this).closest('form.form').length;
    			if(oNav_span==1){
    				search(getUrl(), getParams());
    			};
    		}
    	});
    </script>
</body>
</html>