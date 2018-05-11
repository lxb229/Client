<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    $(function() {
        $('#supplierAddForm').form({
            url : '${path}/supplier/add',
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
                    var form = $('#supplierAddForm');
                    showMsg(result.msg);
                }
            }
        });
    });
</script>
<div class="easyui-layout" data-options="fit:true,border:false" >
    <div data-options="region:'center',border:false" style="overflow: hidden;padding: 3px;" >
        <form id="supplierAddForm" method="post">
            <table class="grid">
                <tr>
                    <td>公司名字</td>
                    <td><input name="companyName" type="text" placeholder="请输入公司名字" class="easyui-validatebox span2" data-options="required:true" value=""></td>
                </tr> 
                <tr>
                    <td>商家名称</td>
                    <td><input name="supplierName" type="text" placeholder="请输入名称" class="easyui-validatebox span2" data-options="required:true" value=""></td>
                </tr> 
                <tr>
                    <td>商家性别</td>
                    <td>
                    	<select name="supplierSex" placeholder="性别" class="easyui-combobox" data-options="required:true">
                            <option value="1" >男士</option>
                            <option value="2" >女士</option>
                        </select>
                    </td>
                </tr> 
                <tr>
                    <td>供应商电话</td>
                    <td><input name="supplierPhone" type="text" placeholder="请输入电话" class="easyui-validatebox span2" data-options="required:true" value=""></td>
                </tr> 
                <tr>
                    <td>供应商微信</td>
                    <td><input name="supplierWechat" type="text" placeholder="请输入微信" class="easyui-validatebox span2" data-options="required:true" value=""></td>
                </tr> 
                <tr>
                    <td>供应商QQ</td>
                    <td><input name="supplierQq" type="number" placeholder="请输入QQ" class="easyui-validatebox span2" data-options="required:true" value=""></td>
                </tr> 
                <tr>
                    <td>供应商邮箱</td>
                    <td><input name="supplierEmail" type="text" placeholder="请输入邮箱" class="easyui-validatebox span2" data-options="required:true" value=""></td>
                </tr> 
                <tr>
                    <td>官网地址</td>
                    <td><input name="supplierUrl" type="text" placeholder="请输入官网地址" class="easyui-validatebox span2" data-options="required:true" value=""></td>
                </tr> 
                <tr>
                    <td>公司地址</td>
                    <td><input name="companyAddress" type="text" placeholder="请输入公司地址" class="easyui-validatebox span2" data-options="required:true" value=""></td>
                </tr> 
                <tr>
                    <td>备注</td>
                    <td><input name="remark" type="text" placeholder="请输入备注" class="easyui-validatebox span2" data-options="required:true" value=""></td>
                </tr> 
                
            </table>
        </form>
    </div>
</div>