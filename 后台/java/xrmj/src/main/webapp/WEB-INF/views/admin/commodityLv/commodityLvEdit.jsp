<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    $(function() {
        $('#commodityLvEditForm').form({
            url : '${path}/commodityLv/edit',
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
                    var form = $('#commodityLvEditForm');
                    showMsg(result.msg);
                }
            }
        });
        
        $("#editStatus").val('${commodityLv.status}'); 
        
    });
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 3px;">
        <form id="commodityLvEditForm" method="post">
            <table class="grid">
                <tr>
                    <td>等级名称</td>
                    <td><input name="id" type="hidden"  value="${commodityLv.id}">
                    <input name="lvName" type="text" placeholder="请输入等级名称" class="easyui-validatebox" data-options="required:true" value="${commodityLv.lvName}"></td>
                </tr>
                <tr>
                    <td>等级</td>
                    <td>
                    <input name="lv" type="number" placeholder="请输入等级" class="easyui-validatebox" data-options="required:true" value="${commodityLv.lv}"></td>
                </tr>
                <tr>
                    <td>等级排序</td>
                    <td>
                    <input name="rank" type="number" placeholder="请输入等级排序" class="easyui-validatebox" data-options="required:true" value="${commodityLv.rank}"></td>
                </tr>
            </table>
        </form>
    </div>
</div>