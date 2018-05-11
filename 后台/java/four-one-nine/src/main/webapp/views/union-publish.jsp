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
        <form id="save_union_form">
        	<input type="hidden" name="union_id" value="${union.union_id }">
            <label for="guild-name">公会名称（必填）</label>
            <input id="guild-name" type="text" placeholder="请填写工会名称" name="union_name" value="${union.union_name }">
            <label for="guild-logo">上传logo（必选）</label>
            <div class="row">
                <div class="col-xs-3">
                    <input readonly name="union_logo" id="union_union_logo" type="text" placeholder="请选择" value="${union.union_logo }">
                </div>
                <div class="col-xs-9">
                    <span class="rel btn-up text-center click_file_upload" for="union_union_logo">选择 </span>
                </div>
            </div>
            <label for="guild-logo">选择成员（必选）</label>
            <div class="guild-members" id="unionUserDiv">
                <div class="swiper-container">
                    <div class="swiper-wrapper">
                    	<c:forEach items="${unionUser }" var="item">
                    	<div class="swiper-slide">
                    		<input type="hidden" name="unionUserId" value="${item.user_id }">
                            <img src="${item.head_picture }" alt="">
                            <span class="member-del"></span>
                        </div>
                    	</c:forEach>
                        <div class="swiper-slide">
                            <img class="btn_add_user" onclick="choiceUnionMemberModal()" src="${pageContext.request.contextPath}/views/libs/images/btn_add_user.png" alt="">
                        </div>
                    </div>
                </div>
            </div>
            
            <label for="guild-logo">设置一个会长（必选）${clo_user.head_picture }</label>
            <input type="hidden" name="clo" value="${union.clo }">
            <div class="guild-members">
            	<div class="swiper-container">
	            	<div class="swiper-wrapper">
	            		<div class="swiper-slide clo_user_picture">
	            			<c:if test="${not empty clo_user.head_picture }">
			            		<img src="${clo_user.head_picture }" alt="">
			            		<span class="member-del"></span>
		            		</c:if>
		            	</div>
		            	<div class="swiper-slide">
		                	<img class="btn_add_user" onclick="choiceCloModal()" src="${pageContext.request.contextPath}/views/libs/images/btn_add_user.png">
		                </div>
	                </div>
	             </div>
            </div>
            
            <label for="guild-bonus-set">分红比例设置</label>
            <ul class="bonus-set-group">
                <li class="rel">会长 <input type="text"  name="income_ratio_clo"  onkeyup="this.value=this.value.replace(/[^\d]+/g,'')" value="${union.income_ratio_clo }" ><span class="abs unit">%</span></li>
                <li class="rel">成员 <input type="text"  name="income_ratio_member"  onkeyup="this.value=this.value.replace(/[^\d]+/g,'')" value="${union.income_ratio_member }"><span class="abs unit">%</span></li>
            </ul>
            <div class="form-button-groups">
                <button type="button" class="btn btn-primary" onclick="saveUnion()">确定</button>
            </div>
        </form>
    </div>
    
    <!-- 选择会长模态框 -->
	<div class="modal fade" id="choice_clo_modal" style="z-index:2147483647;">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<img class="logo" src="${pageContext.request.contextPath}/views/libs/images/top_logo.png" alt="logo">
				</div>
				<div class="modal-body"></div>
			</div>
		</div>
	</div>
	
	<!-- 弹窗选择用户 -->
	<div class="modal fade" id="choice_user">
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
// 选择成员
function choiceUnionMemberModal() {
	// 获取弹框内容
	var url = "${pageContext.request.contextPath}/user/choice_user_page";
	var where = 't.user_id NOT IN(SELECT user_id from union_user<c:if test="${union != null }"> where union_id <> ${union.union_id}</c:if>)';
	// 排除已选中用户
	var ids = '';
	$('input[name=unionUserId]').each(function() {
		ids += ids.length > 0 ? ',' : '';
		ids += $(this).val();
	});
	if(ids.length > 0) {
		where += ' and t.user_id NOT IN('+ids+')';
	}
	
	var data = {'callbackFunction':'choiceMember', 'where':where};
	$.post(url, data, function(response) {
		$('#choice_user .modal-body').html(response);
		$('#choice_user').modal('show');
	});
}
// 确认选择用户回调方法
function choiceMember(element) {
	var id = $(element).find('td').first().find('span').attr('data');
	var head = $(element).find('img.user-head-30').attr('src');
	var userDiv = '<div class="swiper-slide">';
	userDiv += '<input type="hidden" name="unionUserId" value="'+id+'">';
	userDiv += '<img src="'+head+'" alt="">';
	userDiv += '<span class="member-del"></span></div>';
    $('#unionUserDiv .btn_add_user').parent().before(userDiv);
}

// 选择会长 弹窗
function choiceCloModal() {
	$('#choice_clo_modal .modal-body').html('<div class="guild-members">'+$('#unionUserDiv').html()+'</div>');
	$('#choice_clo_modal .modal-body .member-del').remove();
	$('#choice_clo_modal .modal-body .swiper-slide:last').remove();
	$('.clo_user_picture').show();
	
	$('#choice_clo_modal').modal("show");
}
// 会长选中
$(document).off('click', '#choice_clo_modal .modal-body img').on('click', '#choice_clo_modal .modal-body img', function(){
	// 设置id
	$('#save_union_form input[name=clo]').val($(this).parent().find("input").val());
	// 显示头像
	$('#save_union_form .clo_user_picture').html($(this));
	$('#save_union_form .clo_user_picture').append('<span class="member-del"></span>');
	// 关闭模态框
	$('#choice_clo_modal').modal("hide");
});

// 删除选中用户信息
$(document).off('click', '#save_union_form .swiper-slide .member-del').on('click', '#save_union_form .swiper-slide .member-del', function(){
	// 会长删除
	if($(this).parent().hasClass('clo_user_picture')) {
		$('.clo_user_picture').empty();
		$('#save_union_form input[name=clo]').val('');
	} else {
		// 删除的是会长
		if($(this).parent().find("input").val() == $('#save_union_form input[name=clo]').val()) {
			$('.clo_user_picture').empty();
			$('#save_union_form input[name=clo]').val('');
		}
		// 移除元素
		$(this).parent().remove();
	}
});


// 保存工会信息
function saveUnion() {
	var url = '${pageContext.request.contextPath}/union/save';
	var data = $('#save_union_form').serialize();
	if(businessPost(url, data).success) {
		gotoMenu('管理公会');
	}
}
</script>