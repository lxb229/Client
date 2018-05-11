<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<style>
.swiper-container {
    overflow: unset;
}
</style>
<body>
	<!--主体内容-->
	<div class="page-main">
		<ol class="breadcrumb">
            <li class="goto_user"><a href="javascript:;">用户管理</a></li>
            <li class="active">用户详情</li>
        </ol>
		<div class="user-info-panel rel">
			<img class="abs user-photo" src="${user.head_picture }" alt="">
			<c:if test="${user.status eq 1}">
            <span class="dmui-btn btn-cancel fr mr-0" onclick="freeze(0)">冻结账号</span>
            </c:if>
            <c:if test="${user.status eq 0 }">
            <span class="dmui-btn btn-cancel fr mr-0" onclick="freeze(1)">解冻账号</span>
            </c:if>
			<span class="dmui-btn btn-blur fr " onclick="resetPassword()">重置密码</span>
			<h3>${user.nick_name }</h3>
			<div class="row clearfix" style="margin-top: 20px;">
				<div class="col-lg-3">
					<div class="bonus">
						<span class="user-role">身份</span> 
						<span class="number fr">
							<c:if test="${user.seller_certification == 1}">卖家</c:if>
                        	<c:if test="${user.seller_certification != 1}">买家</c:if>
						</span>
					</div>
				</div>
				<div class="col-lg-3">
					<div class="bonus">
						<span class="user-role">手机号</span> <span class="number fr">${user.phone }</span>
					</div>
				</div>
				<div class="col-lg-3">
					<div class="bonus">
						<span class="user-role">所在地</span> <span class="number fr">${user.city }</span>
					</div>
				</div>
				<div class="col-lg-3">
					<div class="bonus">
						<span class="user-role">注册时间</span>
						<span class="number fr">
							<fmt:formatDate value="${user.registe_time }" pattern="yyyy-MM-dd"/>
						</span>
					</div>
				</div>
			</div>
			<div class="row clearfix" style="margin-top: 20px;">
				<div class="col-lg-3">
					<div class="bonus">
						<span class="user-role">来源</span> <span class="number fr">${user.user_source }</span>
					</div>
				</div>
				<div class="col-lg-3">
					<div class="bonus">
						<span class="user-role">性别</span>
						<span class="number fr">
							<c:choose>
	                       		<c:when test="${user.sex == 1}">男</c:when>
	                       		<c:when test="${user.sex == 0}">女</c:when>
	                       	</c:choose>
						</span>
					</div>
				</div>
				<div class="col-lg-3">
					<div class="bonus">
						<span class="user-role">年龄</span> <span class="number fr">${user.age }</span>
					</div>
				</div>
				<div class="col-lg-3">
					<div class="bonus">
						<span class="user-role">消费金额</span> <span class="number fr">${expenditure }</span>
					</div>
				</div>
			</div>
		</div>

		<!--封面图-->
		<div class="coverPic-panel">
			<h4 class="title">用户封面图</h4>
			<div class="coverPic text-center">
				<div class="swiper-container">
					<div class="swiper-wrapper">
						<c:forEach items="${covers }" var="temp">
                    	<div class="swiper-slide">
                            <img src="${temp }" alt="">
                            <span class="dmui-btn btn-cancel">
                            	<i class="glyphicon glyphicon-trash" onclick="delUserCover('${temp}')"></i>
                            </span>
                        </div>
                   		</c:forEach>
					</div>
					<div class="swiper-button-prev"></div>
					<div class="swiper-button-next"></div>
				</div>
			</div>
		</div>
	</div>

</body>
<script>
	var mySwiper = new Swiper('.swiper-container', {
		width : 168,
		spaceBetween : 22,
		navigation : {
			nextEl : '.swiper-button-next',
			prevEl : '.swiper-button-prev',
		},
	});
</script>

<script>
	// 列表管理
	$('.goto_user').click(function() {
		gotoMenu('用户管理');
	});
	
	// 冻结/解冻
	function freeze(result) {
		if(ids.length != 0) {
			var data = {"ids":${id},"result": result};
			var url = '${pageContext.request.contextPath}/user/freeze';
			if(businessPost(url, data).success) {
				gotoPage('${pageContext.request.contextPath}/user/detail/'+id);
			}
		}
	}
	
	// 重置密码
	function resetPassword() {
		var url = '${pageContext.request.contextPath}/user/resetPwd';
		if(businessPost(url).success) {
			gotoPage('${pageContext.request.contextPath}/user/detail/${id}');
		}
	}
	
	// 删除封面
	function delUserCover(cover) {
		var url = '${pageContext.request.contextPath}/user/${id}/del_cover';
		var data = {'picture': cover};
		if(businessPost(url, data).success) {
			gotoPage('${pageContext.request.contextPath}/user/detail/${id}');
		}
	}
</script>