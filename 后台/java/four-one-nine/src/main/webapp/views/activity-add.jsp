<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<head>
<link
	href="${pageContext.request.contextPath}/views/libs/bootstrop/css/bootstrap-datetimepicker.min.css"
	rel="stylesheet">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/Swiper/4.0.2/css/swiper.min.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/views/libs/css/activityDetl.css">
<script src="https://cdn.bootcss.com/less.js/3.0.0-pre.4/less.min.js"></script>
<style>
.datetimepicker-dropdown-bottom-right {
	z-index: 99999
}
</style>
</head>
<body>
	<!--主体内容-->
	<div class="page-main">
		<form id="publish_activity">
			<label for="guild-name">活动名称（必填）</label> <input id="guild-name"
				name="activity_name" type="text" placeholder="请填写活动名称"
				onkeyup="this.value=this.value.replace(/[^\u2E80-\u9FFF]/g,'')">
			<label>活动封面（必选）</label>
			<div class="row">
				<div class="col-xs-3 pr-0">
					<input readonly name="activity_cover" id="activity_activity_cover"
						type="text" placeholder="请选择">
				</div>
				<div class="col-xs-9">
					<span class="rel btn-up text-center click_file_upload"
						for="activity_activity_cover">选择</span>
				</div>
			</div>
			<label for="guild-name">活动是否置顶</label>
			<div class="row">
				<div class="col-xs-3">
					<select name="mark_top">
						<option value="1">是</option>
						<option value="0" selected>否</option>
					</select>
				</div>
			</div>
			<label for="guild-logo">活动类型（必填）</label>
			<div class="row">
				<div class="col-xs-3">
					<select name="type">
						<option value="1">普通活动</option>
					</select>
				</div>
			</div>
			<label for="guild-logo">日期（必选）</label>
			<div class="row" style="line-height: 40px;">
				<div class="col-xs-3 pr-0">
					<input readonly name="start_time" type="text" placeholder="选择日期"
						id="datetimepicker-start" data-date-format="yyyy-mm-dd">
				</div>
				<span class="fl" style="margin-left: 15px;">---</span>
				<div class="col-xs-3 pr-0">
					<input readonly name="end_time" type="text" placeholder="选择日期"
						id="datetimepicker-end" data-date-format="yyyy-mm-dd">
				</div>
			</div>
			<label for="guild-logo">参与人数限制（非必填）</label>
			<div class="row" style="line-height: 40px;">
				<div class="col-xs-3 pr-0">
					<input name="restrict_no" type="text" placeholder="输入数字"
						onkeyup="this.value=this.value.replace(/[^\-?\d)]/g,'')">
				</div>
				&nbsp;&nbsp; 人
			</div>
			<label>活动内容（必填）</label> <input type="hidden" name="activity_content" />
			<div id="editor">
				<!--<p>欢迎使用 <b>wangEditor</b> 富文本编辑器</p>-->
			</div>
			<label>转发渠道</label>
			<div class="row ">
				<div class="col-xs-3 pr-0">
					<input name="channel_name" type="text" placeholder="渠道名称">
				</div>
				<div class="col-xs-9">
					<input name="channel_url" type="text" placeholder="链接">
				</div>
			</div>
			<div class="row" style="margin-top: 15px;">
				<div class="col-xs-3 pr-0">
					<input name="channel_name" type="text" placeholder="渠道名称">
				</div>
				<div class="col-xs-9">
					<input name="channel_url" type="text" placeholder="链接">
				</div>
			</div>
			<div class="form-button-groups">
				<button type="button" class="btn cancel"
					onclick="activity_preview()">预览</button>
				<button type="button" class="btn btn-primary"
					onclick="publish_activity()">确定</button>
			</div>

		</form>

	</div>
	<!--活动内容预览 模态框-->
	<div class="modal fade" id="activity_preview_modal" style="z-index: 150000">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">活动预览</div>
				<div class="modal-body">
					<div class="container-fluid">
						<div id="J_activityDetl" class="activityDetl">
							<img class="top-image" id="activityicon"  src="" alt="">
							<div class="container">
								<h3 id="activityname"></h3>
								<div class="summary">
									<div>
										<span>活动时间</span> <span id="activitystartTime"></span> ~
										<span id="activityendTime"></span>
									</div>
									<div>
										<span>发布者</span><span id="activitypublisher">${login_name }</span>
									</div>
								</div>
								<div class="content" id="activitycontent"></div>
							</div>
						</div>
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-primary" data-dismiss="modal">确定</button>
				</div>
			</div>
		</div>
	</div>

</body>

<script>
	$.fn.datetimepicker.dates['ch'] = {
		days : [ "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日" ],
		daysShort : [ "日", "一", "二", "三", "四", "五", "六", "日" ],
		daysMin : [ "日", "一", "二", "三", "四", "五", "六", "日" ],
		months : [ "一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月",
				"十一月", "十二月" ],
		monthsShort : [ "一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一",
				"十二" ],
		meridiem : [ "上午", "下午" ],
		//suffix:      ["st", "nd", "rd", "th"],
		today : "今天",
	};
	var date = new Date();
	var config = {
		format : 'yyyy-mm-dd',
		language : "ch",
		startDate : date,
		autoclose : true,
		minView : "month",
	};
	$('#datetimepicker-start').datetimepicker(config);
	$('#datetimepicker-end').datetimepicker(config);
</script>

<!-- 业务方法 -->
<script>
	// 初始化富文本编辑器
	var editor = new wangEditor('#editor');
	editor.create();
	// 获取文本编辑器内容
	function getEditor() {
		return editor.txt.html();
	}

	// 活动预览
	function activity_preview() {
		//$('#activity_preview_modal .modal-body').html(getEditor());
		var DataDeal = {
          formToJson: function (data) {
              data=data.replace(/&/g,"\",\"");
              data=data.replace(/=/g,"\":\"");
              data="{\""+data+"\"}";
              return data;
           },
		};
		var data = $('#publish_activity').serialize(); 
		data= decodeURIComponent(data,true);//防止中文乱码
		var json=JSON.parse(DataDeal.formToJson(data));//转化为json
		var activity_cover=json.activity_cover;
		if(activity_cover==null||activity_cover==undefined||activity_cover==""){
			$('#activityicon').hide();
		}else{
			$('#activityicon').show();
		}
		$('#activityicon').attr("src",json.activity_cover);
		$('#activityname').html(json.activity_name);
		$('#activitystartTime').html(json.start_time);
		$('#activityendTime').html(json.end_time);
		$('#activitycontent').html(getEditor());
		
		$('#activity_preview_modal').modal('show'); 
	}

	// 发布活动
	function publish_activity() {
		// 文本信息
		$('#publish_activity input[name=activity_content]').val(getEditor());
		// 数据验证

		// 请求参数
		var data = $('#publish_activity').serialize();
		if (businessPost('${pageContext.request.contextPath}/activity/publish',
				data).success) {
			gotoMenu('活动管理');
		}
	}
</script>
