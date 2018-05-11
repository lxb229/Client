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
        <form class="template-add-page">
        	<input type="hidden" name="user_type" />
            <label for=""><b>选择推送用户</b>（必选）</label>
            <div>
                <div class="chose-groups user_type">
                    <span class="dmui-btn btn-blur">全部用户</span>
                    <span class="dmui-btn btn-default">已认证用户</span>
                    <span class="dmui-btn btn-default">未认证用户</span>
                    <span class="dmui-btn btn-default">部分用户</span>
                </div>
                <div class="chose-people" style="display: none;">
                	<input type="hidden" name="users">
                    <div class="guild-members">
                        <div class="swiper-container">
                            <div class="swiper-wrapper">
                                <div class="swiper-slide chose_user_element">
                                    <img class="btn_add_user" onclick="choiceTemplateMemberModal()" src="${pageContext.request.contextPath}/views/libs/images/btn_add_user.png" alt="">
                                </div>
                            </div>
                        </div>
                    </div>
                    <p class="chose-summary">*推送模板为固定触发模板，如需发送单条不重复推送消息，请到“发布推送”页面发送</p>
                </div>

            </div>
            <label for=""><b>触发条件</b>（必选）</label>
            <div class="row">
                <div class="col-xs-5">
                    <select name="trigger_condition" >
                        <option value="注册成功">注册成功</option>
                        <option value="购买成功">购买成功</option>
                        <option value="付款成功">付款成功</option>
                        <option value="订单完成">订单完成</option>
                    </select>
                </div>
            </div>
            <label for=""><b>是否固定时间发送</b>（不选表示否）</label>
            <div class="row">
                <div class="col-xs-3">
                    <select name="send_time" >
                    	<option disabled selected></option>
                        <option value="每天8:00">每天8:00</option>
                        <option value="每天9:00">每天9:00</option>
                        <option value="每天10:00">每天10:00</option>
                    </select>
                </div>
            </div>
            <label for=""><b>推送内容</b>（必填）</label>
            <div class="textarea-box">
                <textarea name="template_content" placeholder="请输入" onkeyup="checkedContent()"></textarea>
                <p class="text-right"><span id="contentNum">0</span>/500</p>
            </div>
            <div class="form-button-groups">
                <button type="button" class="btn btn-primary J_btn-primary" onclick="publish_template()" >确定</button>
            </div>
        </form>
    </div>
    <!-- 弹窗选择用户 -->
	<div class="modal fade" id="choice_user_template">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                </div>
                <div class="modal-body">
                    
                </div>
                <div class="modal-footer"></div>
            </div>
        </div>
    </div>
    
</body>

<script>
// 推送用户切换效果
$('.template-add-page .user_type span').click(function() {
	$(this).siblings().removeClass('btn-blur').addClass('btn-default');
	$(this).removeClass('btn-default').addClass('btn-blur');
	
	$(".template-add-page .chose-people").hide();
	if($(this).text() == '部分用户') {
		$(".template-add-page .chose-people").show();
	}
	// 请出已选中用户
	 $('.template-add-page .chose-people .btn_add_user').parent().siblings().remove();
});
// 选择用户弹窗
function choiceTemplateMemberModal() {
	var where = '1=1';
	// 排除已选中用户
	var ids = '';
	$('input[name=userId]').each(function() {
		ids += ids.length > 0 ? ',' : '';
		ids += $(this).val();
	});
	if(ids.length > 0) {
		where += ' and t.user_id NOT IN('+ids+')';
	}
	// 获取弹框内容
	var url = "${pageContext.request.contextPath}/user/choice_user_page";
	var data = {'callbackFunction':'choiceTemplateMember', 'where':where};
	$.post(url, data, function(response) {
		$('#choice_user_template .modal-body').html(response);
		$('#choice_user_template').modal('show');
	});
} 
// 用户选中回调
function choiceTemplateMember(element) {
	var id = $(element).find('td').first().find('span').attr('data');
	var head = $(element).find('img.user-head-30').attr('src');
	var userDiv = '<div class="swiper-slide">';
	userDiv += '<input type="hidden" name="userId" value="'+id+'">';
	userDiv += '<img src="'+head+'" alt="">';
	userDiv += '<span class="member-del"></span></div>';
    $('.template-add-page .chose-people .btn_add_user').parent().before(userDiv);
}

//删除选中用户信息
$(document).off('click', '.template-add-page .swiper-slide .member-del').on('click', '.template-add-page .swiper-slide .member-del', function(){
	// 移除元素
	$(this).parent().remove();
});

// 检查输入内容
function checkedContent() {
	var content = $('.template-add-page textarea[name=template_content]').val();
	if(content.length > 500) {
		content = content.substring(0, 500);
		$('.template-add-page textarea[name=template_content]').val(content);
	}
	$('.template-add-page #contentNum').html(content.length);
}

// 发布模版
function publish_template() {
	// 获取目标用户
	var user_type = $('.template-add-page .user_type .btn-blur').text();
	$('.template-add-page input[name=user_type]').val(user_type);
	
	var data = $('.template-add-page').serialize();
	var url = '${pageContext.request.contextPath}/template/publish';
	if(businessPost(url, data).success) {
		gotoMenu('推送模板');
	}
};
</script>
