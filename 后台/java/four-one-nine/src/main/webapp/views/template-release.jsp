<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<style>
.swiper-slide {
	float: left;
}
</style>

<body>
    <!--主体内容-->
    <div class="page-main">
        <form id="template-send-form">
            <label for=""><b>选择推送用户</b>（必选）</label>
            <div>
            	<input type="hidden" name="user_type" />
                <div class="chose-groups user_type">
                    <span class="dmui-btn btn-blur">全部用户</span>
                    <span class="dmui-btn btn-default">已认证用户</span>
                    <span class="dmui-btn btn-default">未认证用户</span>
                    <span class="dmui-btn btn-default">部分用户</span>
                </div>
                <div class="chose-people" style="display: none;">
                    <div class="guild-members">
                        <div class="swiper-container">
                            <div class="swiper-wrapper">
                                <div class="swiper-slide chose_user_element">
                                    <img class="btn_add_user" onclick="choiceTemplateSendMemberModal()" src="${pageContext.request.contextPath}/views/libs/images/btn_add_user.png" alt="">
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <label for=""><b>推送内容</b>（必填）</label>
            <div class="textarea-box">
                <textarea name="template_content" placeholder="请输入" onkeyup="checkedContent()"></textarea>
                <p class="text-right"><span class="contentNum">0</span>/500</p>
            </div>
            <div class="form-button-groups">
                <button type="button" class="btn btn-primary" onclick="send_template()" >发布</button>
            </div>
        </form>
    </div>
    <!-- 弹窗选择用户 -->
	<div class="modal fade" id="choice_user_template_send">
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
$('#template-send-form .user_type span').click(function() {
	$(this).siblings().removeClass('btn-blur').addClass('btn-default');
	$(this).removeClass('btn-default').addClass('btn-blur');
	
	$("#template-send-form .chose-people").hide();
	if($(this).text() == '部分用户') {
		$("#template-send-form .chose-people").show();
	}
	// 清除已选中用户
	 $('#template-send-form .chose-people .btn_add_user').parent().siblings().remove();
});
// 选择用户弹窗
function choiceTemplateSendMemberModal() {
	var where = '1=1';
	// 排除已选中用户
	var ids = '';
	$('#template-send-form input[name=userId]').each(function() {
		ids += ids.length > 0 ? ',' : '';
		ids += $(this).val();
	});
	if(ids.length > 0) {
		where += ' and t.user_id NOT IN('+ids+')';
	}
	// 获取弹框内容
	var url = "${pageContext.request.contextPath}/user/choice_user_page";
	var data = {'callbackFunction':'choiceTemplateSendMember', 'where':where};
	$.post(url, data, function(response) {
		$('#choice_user_template_send .modal-body').html(response);
		$('#choice_user_template_send').modal('show');
	});
} 
// 用户选中回调
function choiceTemplateSendMember(element) {
	var id = $(element).find('td').first().find('span').attr('data');
	var head = $(element).find('img.user-head-30').attr('src');
	var userDiv = '<div class="swiper-slide">';
	userDiv += '<input type="hidden" name="userId" value="'+id+'">';
	userDiv += '<img src="'+head+'" alt="">';
	userDiv += '<span class="member-del"></span></div>';
    $('#template-send-form .chose-people .btn_add_user').parent().before(userDiv);
}

//删除选中用户信息
$(document).off('click', '#template-send-form .swiper-slide .member-del').on('click', '#template-send-form .swiper-slide .member-del', function(){
	// 移除元素
	$(this).parent().remove();
});

// 检查输入内容
function checkedContent() {
	var content = $('#template-send-form textarea[name=template_content]').val();
	if(content.length > 500) {
		content = content.substring(0, 500);
		$('#template-send-form textarea[name=template_content]').val(content);
	}
	$('#template-send-form .contentNum').html(content.length);
}

// 发布模版
function send_template() {
	// 获取目标用户
	var user_type = $('#template-send-form .user_type .btn-blur').text();
	$('#template-send-form input[name=user_type]').val(user_type);
	
	var data = $('#template-send-form').serialize();
	var url = '${pageContext.request.contextPath}/template/send_template';
	if(businessPost(url, data).success) {
		gotoMenu('推送模板');
	}
};
</script>
