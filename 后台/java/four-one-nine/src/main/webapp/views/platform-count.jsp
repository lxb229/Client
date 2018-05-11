<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!--主体内容-->
<div class="page-main platform-page">
	<div class="row">
		<div class="col-xs-4">
			<div class="dmui-panel">
				<div class="dmui-panel-header rel">
					<span class="tab-tit">入驻用户</span> 
					<span class="tab-tit fr tab-tit-more" onclick="gotoMenu('用户管理')">查看用户列表</span>
				</div>
				<div class="dmui-panel-body">
					<div class="row user-controller-strc">
						<div class="col-lg-6">
							<b>${userNum }</b>平台总人数(人)
						</div>
						<div class="col-lg-6">
							<b>${installerNum }</b>装机量(次)
						</div>
					</div>
					<div id="user-view" class="clear join-user-stac" style="height: 307px;"></div>
					<script>
						option = {
							series : [ {
								name : '访问来源', type : 'pie', radius : [ 50, 70 ], x : '60%',
								width : '35%', funnelAlign : 'left', max : 10480,
								color : [ '#80C269', '#4FC1CB', '#F18334', '#BFBFBF', "#F182C7", "#B09BDE" ],
								textStyle : { color : '#333' },
								data : eval('(${source})')
							} ]
						};
					
						setTimeout(function() {
							$("#user-view").css('width', $("#user-view").width());
							//初始化echarts实例
							var myChart = echarts.init(document.getElementById('user-view'));
							//使用制定的配置项和数据显示图表
							myChart.setOption(option);
						}, 200)
					</script>
				</div>
			</div>
		</div>

		<div class="col-xs-8">
			<div class="dmui-panel">
				<div class="dmui-panel-header rel">
					<span class="tab-tit">活动板块</span>
				</div>
				<div class="dmui-panel-body">
					<div class="main-statistics clearfix">
						<div class="item fl story">
							<div class="clearfix">
								<img class="fl" src="../views/img/gushi.jpg" alt="">
								<dl class="fl item-content">
									<dd class="number">${installerNum }</dd>
									<dt class="title">故事接龙访问量（次）</dt>
								</dl>
							</div>
						</div>
						<div class="item fl transaction">
							<div class="clearfix">
								<img class="fl" src="../views/img/jiaoyi.jpg" alt="">
								<dl class="fl item-content">
									<dd class="number">${installerNum }</dd>
									<dt class="title">交易访问量（次）</dt>
								</dl>
							</div>
						</div>
						<div class="item fl graffiti">
							<div class="clearfix">
								<img class="fl" src="../views/img/tuya.jpg" alt="">
								<dl class="fl item-content">
									<dd class="number">${installerNum }</dd>
									<dt class="title">涂鸦访问量（次）</dt>
								</dl>
							</div>
						</div>
						<div class="item fl sign">
							<div class="clearfix">
								<img class="fl" src="../views/img/riqian.jpg" alt="">
								<dl class="fl item-content">
									<dd class="number">${installerNum }</dd>
									<dt class="title">日签访问量（次）</dt>
								</dl>
							</div>
						</div>
						<div class="item fl activity">
							<div class="clearfix">
								<img class="fl" src="../views/img/huodong.jpg" alt="">
								<dl class="fl item-content">
									<dd class="number">${installerNum }</dd>
									<dt class="title">活动访问量（次）</dt>
								</dl>
							</div>
						</div>
						<div class="item fl poster">
							<div class="clearfix">
								<img class="fl" src="../views/img/haibao.jpg" alt="">
								<dl class="fl item-content">
									<dd class="number">${installerNum }</dd>
									<dt class="title">海报访问量（次）</dt>
								</dl>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>

	</div>

	<div class="row user-stac" style="margin-top: 30px;">

		<div class="col-xs-8 strtic">
			<div class="dmui-panel">
				<div class="dmui-panel-header rel">
					<span class="tab-tit">交易数据</span>
				</div>
				<div class="dmui-panel-body">
					<div class="main-statistics clearfix">
						<div class="item fl border-test">
							<div class="clearfix">
								<dl class="fl item-content">
									<dt class="title">平台流水</dt>
									<dd class="number">&yen;${platformStatements }</dd>
								</dl>
							</div>
						</div>
						<div class="item fl border-test"  onclick='gotoPage("${pageContext.request.contextPath}/user/?type=1");'>
							<div class="clearfix arrow">
								<dl class="fl item-content">
									<dt class="title">卖家人数</dt>
									<dd class="number">${sellerNum }</dd>
								</dl>
							</div>
						</div>
						<div class="item fl border-test" onclick="gotoMenu('服务列表')">
							<div class="clearfix arrow">
								<dl class="fl item-content">
									<dt class="title">服务数量</dt>
									<dd class="number">${serverNum }</dd>
								</dl>
							</div>
						</div>
						<div class="item fl border-test">
							<div class="clearfix">
								<dl class="fl item-content">
									<dt class="title">成交量</dt>
									<dd class="number">${orderNum }</dd>
								</dl>
							</div>
						</div>
						<div class="item fl border-test">
							<div class="clearfix">
								<dl class="fl item-content">
									<dt class="title">单笔最大提现</dt>
									<dd class="number">&yen;${maxCash }</dd>
								</dl>
							</div>
						</div>
					</div>
				</div>
			</div>

			<div class="dmui-panel">
				<div class="dmui-panel-header rel">
					<span class="tab-tit">用户数据</span>
				</div>
				<div class="dmui-panel-body">
					<div class="row">
						<div class="col-xs-6 user-address-strc">
							<h4>用户分布</h4>
							<ul>
								<c:forEach items="${city }" var="item">
								<li class="rel">
									<span class="abs address">${item.name }：</span>
									<span class="range" style="width:${item.num * 100 / userNum}%;"></span>
									<span class="number rel">${item.num }人</span>
								</li>
								</c:forEach>
							</ul>
						</div>
						<div class="col-xs-6 user-age-strc">
							<h4>用户年龄</h4>
							<ul>
								<c:forEach items="${age }" var="item">
								<li>
									<span class="fl age">${item.stage }</span>
									<span class="fr number"><b>${item.num }</b>人</span>
								</li>
								</c:forEach>
							</ul>
						</div>
					</div>
				</div>
			</div>

		</div>

		<div class="col-xs-4 ranking">
			<div class="dmui-panel">
				<div class="dmui-panel-header rel">
					<span class="tab-tit">买家交易排行</span>
				</div>
				<div class="dmui-panel-body">
					<div class="dmui-panel-body">
						<ul class="panel-user-list">
						<c:forEach items="${rankingBuy }" var="item">
							<li class="ofh rel">
								<img src="${item.head_picture }" alt="">${item.nick_name }<span class="abs money">&yen;${item.total/100 }</span>
							</li>
						</c:forEach>
						</ul>
					</div>
				</div>
			</div>

			<div class="dmui-panel">
				<div class="dmui-panel-header rel">
					<span class="tab-tit">卖家交易排行</span>
				</div>
				<div class="dmui-panel-body">
					<div class="dmui-panel-body">
						<ul class="panel-user-list">
						<c:forEach items="${rankingSell }" var="item">
							<li class="ofh rel">
								<img src="${item.head_picture }" alt="">${item.nick_name }<span class="abs money">&yen;${item.total/100 }</span>
							</li>
						</c:forEach>
						</ul>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
