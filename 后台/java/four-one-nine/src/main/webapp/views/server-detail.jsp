<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<style>
.swiper-slide {
	float: left;
}
</style>
<body>
    <!--主体内容-->
    <div class="page-main">
        <div class="user-info-panel rel service-detail-page">
            <img class="abs user-photo" src="${publish.head_picture }">
            <span class="dmui-btn btn-cancel fr mr-0">删除服务</span>
            <h3>${publish.nick_name }</h3>
            <p>创建时间: <fmt:formatDate value="${server.publish_time}" pattern="yyyy-MM-dd HH:mm:ss"/></p>
            <div class="row">
                <div class="col-lg-9">
                    <h4 class="service-name"><img src="${pageContext.request.contextPath}/views/libs/images/tag.png" alt="">${server.name }</h4>
                    <p class="service-summary">${server.remarks }</p>
                </div>
                <div class="col-lg-3 text-right service-price">
                    <b>&yen;${server.price/100 }</b>单价/${server.unit }
                </div>
            </div>
        </div>

        <div class="service-strc">
            <ul class="row">
                <li class="col-xs-2">交易次数<b>${count.sell_num }</b></li>
                <li class="col-xs-2">总收益<b>${count.total/100 }</b></li>
                <li class="col-xs-2">小费收益<b>${count.tip_money/100 }</b></li>
                <li class="col-xs-2">已入账金额<b>${count.gain/100 }</b></li>
                <li class="col-xs-2">未入账金额<b>${count.lose/100 }</b></li>
                <li class="col-xs-2">药丸收入<b>${count.like_num }</b></li>
            </ul>
        </div>

        <!--封面图-->
        <div class="coverPic-panel">
            <h3 class="title">服务图片</h3>
            <div class="coverPic text-center">
                <div class="swiper-container">
                    <div class="swiper-wrapper">
                        <div class="swiper-slide">
                            <img src="https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=115883252,1551253581&fm=27&gp=0.jpg" alt="">
                            <span class="dmui-btn btn-cancel"><i class="glyphicon glyphicon-trash"></i></span>
                        </div>
                        <div class="swiper-slide">
                            <img src="https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=115883252,1551253581&fm=27&gp=0.jpg" alt="">
                            <span class="dmui-btn btn-cancel"><i class="glyphicon glyphicon-trash"></i></span>
                        </div>
                        <div class="swiper-slide">
                            <img src="https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=115883252,1551253581&fm=27&gp=0.jpg" alt="">
                            <span class="dmui-btn btn-cancel"><i class="glyphicon glyphicon-trash"></i></span>
                        </div>
                        <div class="swiper-slide">
                            <img src="https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=115883252,1551253581&fm=27&gp=0.jpg" alt="">
                            <span class="dmui-btn btn-cancel"><i class="glyphicon glyphicon-trash"></i></span>
                        </div>
                    </div>
                    <div class="swiper-button-prev"></div>
                    <div class="swiper-button-next"></div>
                </div>
            </div>
        </div>

        <ul class="nav nav-tabs server-tabs">
            <li role="presentation" class="active">
            	<a href="javascript:;" path="income">收支明细</a>
            </li>
            <li role="presentation">
            	<a href="javascript:;" path="comment">评价管理</a>
            </li>
        </ul>
        
        <!-- 详情选项卡内容 -->
        <div id="server-detail-content">
        	
        </div>
    </div>
</body>
<script>
//选项卡切换效果
$('.server-tabs a').click(function (){
	$(this).parent().siblings().removeClass('active');
	$(this).parent().addClass('active');
	// 当前选项卡指定路径
	var path = $(this).attr('path');
	// 内容访问路径
	var url = '${pageContext.request.contextPath}/server/detail/'+path+'/${id}';
	$.get(url, function(data) {
		$("#server-detail-content").html(data);
	});
});

// 默认选中第一个选项卡
$('.server-tabs .active a').click();
</script>