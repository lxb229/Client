<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    $(function() {
        $('#playerEditForm').form({
            url : '${path}/player/edit',
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
                    var form = $('#playerEditForm');
                    showMsg(result.msg);
                }
            }
        });
        
        
    });
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 3px;">
        <form id="playerEditForm" method="post">
            <table class="grid">
                <tr>
                    <td>玩家明星号</td>
                    <td><input name="id" type="hidden"  value="${player.id}">
                    <input name="startNo" type="text" placeholder="请输入玩家明星号" class="easyui-validatebox" data-options="required:true" value="${player.startNo}" readonly="readonly"></td>
                </tr>
                <tr>
                    <td>玩家昵称</td>
                    <td><input name="nick" type="text" placeholder="请输入玩家昵称" class="easyui-validatebox" data-options="required:true" value="${player.nick}" readonly="readonly"></td>
                </tr>
                <tr>
                    <td>玩家电话</td>
                    <td><input name="phone" type="text" placeholder="请输入玩家电话" class="easyui-validatebox" value="${player.phone}"></td>
                </tr>
            </table>
        </form>
    </div>
</div>