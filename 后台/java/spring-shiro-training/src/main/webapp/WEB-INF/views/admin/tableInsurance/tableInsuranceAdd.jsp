<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    $(function() {
        $('#tableInsuranceAddForm').form({
            url : '${path}/tableInsurance/add',
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
                    var form = $('#tableInsuranceAddForm');
                    showMsg(result.msg);
                    //parent.$.messager.alert('错误', eval(result.msg), 'error');
                }
            }
        });
    });
</script>
<div class="easyui-layout" data-options="fit:true,border:false" >
    <div data-options="region:'center',border:false" style="overflow: hidden;padding: 3px;" >
        <form id="tableInsuranceAddForm" method="post">
            <table class="grid">
                <tr>
                    <td>牌数</td>
                    <td><input name="cardNum" type="text" placeholder="请输入牌数" class="easyui-validatebox span2" data-options="required:true" value=""></td>
                </tr> 
                <tr>
                    <td>赔率</td>
                    <td><input name="rate" type="text" placeholder="请输入赔率" class="easyui-validatebox span2" data-options="required:true" value=""></td>
                </tr> 
            </table>
        </form>
    </div>
</div>