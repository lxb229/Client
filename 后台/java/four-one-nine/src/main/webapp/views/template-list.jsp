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
		<!--搜索组-->
		<div class="main-search rel">
			<div class="input-group">
				<span class="input-group-addon pointer" id="basic-addon1"> 
					<i class="glyphicon glyphicon-search"></i>
				</span> 
				<input type="text" class="form-control" placeholder="输入关键词" aria-describedby="basic-addon1">
			</div>
		</div>

		<!--模板list-->
		<div class="row template-list">
			<div class="col-xs-4">
				<div class="item add text-center pointer publish_template">新建模板</div>
			</div>
			<!-- 已发布模版 -->
			<c:forEach items="${templates }" var="item">
			<div class="col-xs-4">
				<div class="item">
					<h4 class="fl title">${item.trigger_condition }</h4>
					<c:if test="${item.send_time != null }">
					<div class="fr time">${item.send_time }</div>
					</c:if>
					<div class="row strc clear">
						<div class="col-xs-6">
							<div class="strc-tit">
								<b>${item.count }</b>次
							</div>
							发送次数
						</div>
						<div class="col-xs-6">
							<div class="strc-tag">${item.user_type }</div>
							目标群体
						</div>
					</div>
					<div class="template-texts">
						${item.template_content }
					</div>
				</div>
			</div>
			</c:forEach>
		</div>
	</div>
</body>

<script>
$('.template-list .publish_template').click(function() {
	gotoPage('${pageContext.request.contextPath}/template/publish_template');
});
</script>
