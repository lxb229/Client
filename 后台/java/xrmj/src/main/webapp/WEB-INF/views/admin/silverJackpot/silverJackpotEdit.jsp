<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    $(function() {
        $('#silverJackpotEditForm').form({
            url : '${path}/silverJackpot/edit',
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
                    var form = $('#silverJackpotEditForm');
                    showMsg(result.msg);
                }
            }
        });
        
        
    });
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 3px;">
        <form id="silverJackpotEditForm" method="post">
            <table class="grid">
                <tr>
                    <td>奖项名称</td>
                    <td><input name="id" type="hidden"  value="${silverJackpot.id}">
                    <input name="awardName" type="text" placeholder="请输入奖项名称" class="easyui-validatebox" data-options="required:true" value="${silverJackpot.awardName}" readonly="readonly"></td>
                </tr>
                <tr>
                    <td>奖项数量</td>
                    <td>
                    <input name="amount" type="text" placeholder="请输入奖项数量" class="easyui-validatebox" data-options="required:true" value="${silverJackpot.amount}"></td>
                </tr>
            </table>
        </form>
    </div>
</div>