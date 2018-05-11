<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
$(function() {
    $('#noticeAddForm').form({
        url : '${path}/notice/add',
        onSubmit : function() {
        	progressLoad();
            var isValid = $(this).form('validate');
            if (!isValid) {
                progressClose();
            }
            return isValid;
        },
        
        success : function(result) {
            progressClose();
            result = $.parseJSON(result);
            if (result.success) {
                //之所以能在这里调用到parent.$.modalDialog.openner_dataGrid这个对象，是因为user.jsp页面预定义好了
                parent.$.modalDialog.openner_dataGrid.datagrid('reload');
                parent.$.modalDialog.handler.dialog('close');
            } else {
                var form = $('#noticeAddForm');
                parent.$.messager.alert('错误', eval(result.msg), 'error');
            }
        }
    });
});   
</script>
<div class="easyui-layout" data-options="fit:true,border:false" >
    <div data-options="region:'center',border:false" style="overflow: hidden;padding: 3px;" >
            <table class="grid">
                <tr>
                    <td>公告名称</td>
                    <td>
		        		<form id="noticeAddForm" method="post">
		                    <input name="name" id="name" type="text" placeholder="请输入名称" class="easyui-validatebox span2" data-options="required:true" value="">
							<input type="text" id="titleImg" name="titleImg" value= "" style="display:none" />
							<input type="text" id="contentImg" name="contentImg" value= "" style="display:none" />
		        		</form>
                    </td>
                </tr> 

                <tr>
                    <td>标题图片</td>
                    <td>
						 <form id="uploadTitleForm">  
						      <p>上传文件： <input type="file" name="file" onchange="uploadTitle()"/></p>  
						      <!-- 
						      <input type="button" value="上传" onclick="uploadTitle()" />  
						       -->
						</form> 
                    </td>
                </tr> 
                <tr>
                    <td>内容图片</td>
                    <td>
						 <form id= "uploadContentForm">  
						      <p>上传文件： <input type="file" name="file" onchange="uploadContent()"/></p>  
						      <!-- 
						      <input type="button" value="上传" onclick="uploadContent()" />  
						       -->
						</form> 
                    </td>
                </tr> 
            </table>
    </div>
</div>
<script type="text/javascript">

function uploadTitle() {  
    var formData = new FormData($("#uploadTitleForm" )[0]);  
    $.ajax({  
         url: '${path}/notice/uploadImage',  
      // 请求方式  
         type: "POST",  
         // 服务器响应的数据类型  
         dataType: "json",  
         data: formData,  
         async: false,  
         cache: false,  
         contentType: false,  
         processData: false,  
         success: function (returndata) {  
        	 $("#titleImg").val(returndata.path)
        	 alert("上传成功");
         },  
         error: function (returndata) {  
             alert(returndata);  
         }  
    });  
}

function uploadContent() {  
    var formData = new FormData($("#uploadContentForm" )[0]);  
    $.ajax({  
         url: '${path}/notice/uploadImage',  
      // 请求方式  
         type: "POST",  
         // 服务器响应的数据类型  
         dataType: "json",  
         data: formData,  
         async: false,  
         cache: false,  
         contentType: false,  
         processData: false,  
         success: function (returndata) {  
        	 $("#contentImg").val(returndata.path)
        	 alert("上传成功");
         },  
         error: function (returndata) {  
             alert(returndata);  
         }  
    });  
}
</script>