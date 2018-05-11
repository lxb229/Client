<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    $(function() {
        $('#tableChipEditForm').form({
            url : '${path}/tableChip/edit',
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
                    parent.$.modalDialog.openner_dataGrid.datagrid('reload');//之所以能在这里调用到parent.$.modalDialog.openner_dataGrid这个对象，是因为user.jsp页面预定义好了
                    parent.$.modalDialog.handler.dialog('close');
                } else {
                    var form = $('#tableChipEditForm');
                    showMsg(result.msg);
                    //parent.$.messager.alert('错误', eval(result.msg), 'error');
                }
            }
        });
        
    });
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 3px;">
        <form id="tableChipEditForm" method="post">
            <table class="grid">
                <tr>
                    <td>小盲</td>
                    <td>
                    	<input name="id" type="hidden"  value="${tableChip.id}">
                    	<input name="small" type="text" placeholder="请输入小盲" class="easyui-validatebox" data-options="required:true" value="${tableChip.small}">
                    </td>
                </tr> 
                <tr>
                    <td>大盲</td>
                    <td><input name="big" type="text" placeholder="请输入大盲" class="easyui-validatebox" data-options="required:true" value="${tableChip.big}"></td>
                </tr> 
                <tr>
                    <td>最小加入</td>
                    <td><input name="join" type="text" placeholder="请输入最小加入" class="easyui-validatebox" data-options="required:true" value="${tableChip.join}"></td>
                </tr> 
            </table>
        </form>
    </div>
</div>