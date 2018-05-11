<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    $(function() {
        $('#jackpotEditForm').form({
            url : '${path}/jackpot/edit',
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
                    var form = $('#jackpotEditForm');
                    showMsg(result.msg);
                }
            }
        });
    });
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 3px;">
        <form id="jackpotEditForm" method="post">
            <table class="grid">
                <tr>
                    <td>每轮奖金</td>
                    <td><input name="id" type="hidden"  value="${jackpot.id}">
                    <input name="bonus" type="number" placeholder="请输入每轮奖金" class="easyui-validatebox" data-options="required:true" value="${jackpot.bonus}"></td>
                </tr>
            </table>
        </form>
    </div>
</div>