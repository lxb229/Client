<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<script src="http://malsup.github.io/jquery.form.js" type="text/javascript"></script>

<div style="display: none;">
	<form name="file_upload" id="file_upload_form" method="post" enctype="multipart/form-data">
		<h1>使用spring mvc提供的类的方法上传文件</h1>
		<input type="file" name="file"> 
		<input type="submit" id="submit" />
	</form>
</div>

<script>

// 目标显示内容input
var forInput;
//文件上传触发信息
$(document).off('click', '#page_content .click_file_upload').on('click', '#page_content .click_file_upload', function(){
	forInput = $(this).attr('for');
	$('#file_upload_form input[type=file]').click();
});

// 文件选择完毕，直接提交到后台服务器
$('#file_upload_form input[type=file]').change(function() {
	 var options = {
             url : "${pageContext.request.contextPath}/file_upload/springUpload",
             dataType : "json",
             type : "post",
             success : function(data){
                 $('#'+forInput).val(data.path);
                 $('#base_show_message_modal .modal-body').html("文件上传成功!");
 				 $('#base_show_message_modal').modal('show');
             }
         };
	$('#file_upload_form').ajaxSubmit(options);
});

</script>

