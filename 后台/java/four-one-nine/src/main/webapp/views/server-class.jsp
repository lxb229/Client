<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<script src="${pageContext.request.contextPath}/views/libs/js/utils.js"></script>
<body>
    <!--主体内容-->
    <div class="page-main">
        <!--统计组-->
        <div class="main-statistics clearfix">
            <div class="item fl border-test">
                <div class="clearfix">
                    <dl class="fl item-content">
                        <dt class="title">交易次数</dt>
                        <dd class="number">${count.order_num/100 }</dd>
                    </dl>
                </div>
            </div>
            <div class="item fl border-test">
                <div class="clearfix">
                    <dl class="fl item-content">
                        <dt class="title">成交金额</dt>
                        <dd class="number">${count.order_count/100 }</dd>
                    </dl>
                </div>
            </div>
        </div>

        <div class="row service-class">
        	<c:forEach items="${classifyCount }" var="item">
        	<div class="col-xs-6">
                <div class="dmui-panel">
                    <div class="dmui-panel-header rel">
                        <span class="tab-tit">${item.name }</span>
                        <span class="tab-tit tab-tit-more fr" onclick="gotoMenu('服务列表')">查看服务列表</span>
                    </div>
                    <div class="dmui-panel-body">
                        <div class="row user-controller-strc">
                            <div class="col-lg-4"><b>${item.userNum }</b>卖家人数</div>
                            <div class="col-lg-4"><b>${item.serverNum }</b>服务数量</div>
                            <div class="col-lg-4"><b>${item.serverMoney /100 }</b>成交金额</div>
                        </div>
                        <div id="chart-${item.id }" style="width: 500px; height: 300px;"></div>
                        <script>
                     	// 初始化echarts实例
                        var myChart${item.id } = echarts.init(document.getElementById('chart-${item.id }'));
                        // 使用刚指定的配置项和数据显示图表。
                        myChart${item.id }.setOption(getEchart(${item.sales}));
                        </script>
                    </div>
                </div>
            </div>
        	</c:forEach>
            <div class="col-xs-6">
                <div class="dmui-panel">
                    <div class="add text-center pointer" onclick="$('#add_classify_model').modal('show')">新建分类</div>
                </div>
            </div>
        </div>


    </div>

    <!--增加分类 模态框-->
    <div class="modal fade" id="add_classify_model">
    	<form id="add_classify_form">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header"></div>
                <div class="modal-body">
                    <p><b>分类名称</b>（必填）</p>
                    <input name="className" type="text" placeholder="输入" maxlength="6">
                    <p><b>分类编码</b>（必填）</p>
                    <input name="classCode" type="text" placeholder="输入" maxlength="6">
                  <%--   <p><b>选择服务</b></p>
                    <div class="service-chose">
                    	<input type="hidden" name="ids" />
                        <ul>
                        <c:forEach items="${unClassify }" var="item">
                        	<li><span class="check-box" data="${item.id }"></span>${item.name }</li>
                        </c:forEach>
                        </ul>
                    </div> --%>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary J_btn-primary" onclick="add_classify_submit()">确定</button>
                </div>
            </div>
        </div>
        </form>
    </div>
</body>

<script>
function add_classify_submit() {
	// 获取选中服务
	var ids = '';
	$('#add_classify_form ul li .checked').each(function() {
		ids += ','+$(this).attr('data');
	});
	if(ids.length > 0) {
		$('#add_classify_form input[name=ids]').val(ids.substring(1));
	}
	var data = $('#add_classify_form').serialize();
	var url = '${pageContext.request.contextPath}/server/add_classify';
	$('#add_classify_model').modal('hide');
	if(businessPost(url, data).success) {
		gotoMenu('服务分类');
	}
}
</script>
