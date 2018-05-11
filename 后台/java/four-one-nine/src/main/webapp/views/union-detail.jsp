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
        <span class="dmui-btn btn-cancel fr rel" style="top: -5px; margin-right: 0px;">删除公会</span>
        <ul class="nav nav-tabs union-tabs">
            <li role="presentation">
            	<a href="javascript:;" path="income">公会收益</a>
            </li>
            <li role="presentation" class="active">
            	<a href="javascript:;" path="manage">公会管理</a>
            </li>
        </ul>
        <!-- 详情选项卡内容 -->
        <div id="union-detail-content">
        	
        </div>
    </div>
</body>
<script>
//选项卡切换效果
$('.union-tabs a').click(function (){
	$(this).parent().siblings().removeClass('active');
	$(this).parent().addClass('active');
	// 当前选项卡指定路径
	var path = $(this).attr('path');
	// 内容访问路径
	var url = '${pageContext.request.contextPath}/union/detail/'+path+'/${id}';
	$.get(url, function(data) {
		$("#union-detail-content").html(data);
	});
});

// 默认选中第一个选项卡
$('.union-tabs .active a').click();
</script>