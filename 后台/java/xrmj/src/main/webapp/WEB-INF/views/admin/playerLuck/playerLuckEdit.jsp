<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    $(function() {
        $('#playerLuckEditForm').form({
            url : '${path}/playerLuck/edit',
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
                    var form = $('#playerLuckEditForm');
                    showMsg(result.msg);
                }
            }
        });
        
    });
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 3px;">
        <form id="playerLuckEditForm" method="post">
            <table class="grid">
                <tr>
                    <td>玩家明星号</td>
                    <td><input name="id" type="hidden"  value="${playerLuck.id}">
                    <input name="startNo" type="text" placeholder="请输入玩家明星号" class="easyui-validatebox span2" data-options="required:true" value="${playerLuck.startNo}" readonly="readonly"></td>
                </tr> 
                <tr>
                    <td>幸运值</td>
                    <td><input name="luck" type="number" placeholder="请输入幸运值" class="easyui-validatebox span2" data-options="required:true" value="${playerLuck.luck}"></td>
                </tr> 
                <tr>
                    <td>幸运次数</td>
                    <td><input name="luckCount" type="number" placeholder="请输入幸运次数" class="easyui-validatebox span2" data-options="required:true" value="${playerLuck.luckCount}"></td>
                </tr> 
                <tr>
                	<td>幸运开始时间</td>
                    <td><input name="luckStart" placeholder="点击选择幸运开始时间" onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" data-options="required:true" value="${luckStart }" /></td>
                </tr>
                <tr>
                	<td>幸运结束时间</td>
                    <td><input name="luckEnd" placeholder="点击选择幸运开始时间" onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" data-options="required:true" value="${luckEnd }" /></td>
                </tr>
            </table>
        </form>
    </div>
</div>