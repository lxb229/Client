<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    $(function() {
        $('#commodityImageAddForm').form({
            url : '${path}/commodityImage/add',
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
                    var form = $('#commodityImageAddForm');
                    showMsg(result.msg);
                }
            }
        });
    });
</script>
<div class="easyui-layout" data-options="fit:true,border:false" >
    <div data-options="region:'center',border:false" style="overflow: hidden;padding: 3px;" >
        <form id="commodityImageAddForm" method="post">
           	<table class="grid">
                <tr>
                    <td>商品</td>
                    <td>
                    	<input type="text" id="imageUrl" name="imageUrl" value= "" style="display:none" />
                    	<select name="commodity" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'">
                            <c:forEach items="${commodityList }" var="c">
								<option value="${c.id }" >${c.commodityName }</option>
							</c:forEach>
                        </select>
					</td>
                </tr> 
                <tr>
                    <td>图片类型</td>
                    <td>
                    	<select name="imageType" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'">
							<option value="1" >icon图片</option>
							<option value="2" >详情图片</option>
                        </select>
					</td>
                </tr> 
            </table>
        </form>
		<form id= "uploadContentForm">  
	        <table class="grid">
				<tr>
					<td>内容图片</td>
					<td>
						<p>上传文件： <input type="file" name="file" onchange="uploadContent()"/></p>  
					</td>
				</tr> 
			</table>
		</form> 
    </div>
</div>

<script type="text/javascript">

function uploadContent() {  
    var formData = new FormData($("#uploadContentForm" )[0]);  
    $.ajax({  
         url: '${path}/commodityImage/uploadImage',  
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
        	 $("#imageUrl").val(returndata.path)
        	 alert("上传成功");
         },  
         error: function (returndata) {  
             alert(returndata);  
         }  
    });  
}
</script>