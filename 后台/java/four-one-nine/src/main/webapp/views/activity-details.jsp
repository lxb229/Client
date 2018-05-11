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
        <ol class="breadcrumb">
            <li class="goto_activity"><a href="javascript:;">管理活动</a></li>
            <li class="active">活动详情</li>
        </ol>
        <div class="row">
            <div class="col-xs-8">
                <div class="activity-details rel">
                    <img class="img-responsive main-pic" src="${activity.activity_cover }" alt="活动封面" style="height: 500px;">
                    <h3 class="title">${activity.activity_name }</h3>
                    <div class="title-summary">
                        <div class="row ml-0 mr-0">
                            <div class="col-xs-7 pl-0">活动时间：
                            	<span class="value">
                            	<fmt:formatDate value="${activity.start_time }" pattern="yyyy-MM-dd"/>
                            	~
                            	<fmt:formatDate value="${activity.end_time }" pattern="yyyy-MM-dd"/>
                            	</span>
                            </div>
                            <div class="col-xs-5 text-right pr-0">发布者：<span class="value">${activity.creater }</span></div>
                        </div>
                    </div>
                    <div class="content">
                        ${activity.activity_content }
                    </div>
                    <div class="link-form">
                    	<c:if test="${channel != null }">
                    		<h4>渠道链接</h4>
                    		<ul>
                    			<c:forEach items="${channel}" varStatus="i" var="item" >
                    				<li>${item.channel_name }：<a href="javascript:;">${item.channel_url }</a></li>
                    			</c:forEach>
	                        </ul>
                    	</c:if>
                    </div>
                </div>
                <form action="">
                    <div class="form-button-groups">
                        <button type="button" class="btn denger" onclick="activityDelete()">删除</button>
                    </div>
                </form>
            </div>
            <div class="col-xs-4 activity-deatil-page">

                <div class="dmui-panel">
                    <div class="dmui-panel-header rel">
                        <span class="tab-tit">活动数据</span>
                    </div>
                    <div class="dmui-panel-body">
                        <div class="main-statistics clearfix">
                            <div class="item fl border-test">
                                <div class="clearfix">
                                    <dl class="fl item-content">
                                        <dt class="title">活动时长</dt>
                                        <dd class="number">${duration }day</dd>
                                    </dl>
                                </div>
                            </div>
                            <div class="item fl add-strc">
                                <div class="clearfix">
                                    <dl class="fl item-content">
                                        <dt class="title">新增用户（人）</dt>
                                        <dd class="number">${toDayApply }</dd>
                                    </dl>
                                    <div class="view clear">
                                        <div id="picturePlace" style="height: 250px; margin: auto"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="item fl border-test">
                                <div class="clearfix">
                                    <dl class="fl item-content">
                                        <dt class="title">日跃人数</dt>
                                        <dd class="number">${activeNum }</dd>
                                    </dl>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="dmui-panel">
                    <div class="dmui-panel-header rel">
                        <span class="tab-tit">参与用户</span>
                        <span class="tab-tit tab-tit-more fr">查看全部</span>
                    </div>
                    <div class="dmui-panel-body">
                        <ul class="panel-user-list">
                        	<c:forEach items="${applyers}" varStatus="i" var="item" >
                        	<li class="ofh"><img src="${item.head_picture }" alt="">${item.nick_name }</li>
                        	</c:forEach>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
<script>
	// 列表管理
	$('.goto_activity').click(function() {
		gotoMenu('活动管理');
	});
	
	// 删除活动信息
	function activityDelete() {
		var data = {'ids': '${activity.id}'};
		var url = '${pageContext.request.contextPath}/activity/del';
		if(businessPost(url, data).success) {
			gotoMenu('活动管理');
		}
	}
</script>
