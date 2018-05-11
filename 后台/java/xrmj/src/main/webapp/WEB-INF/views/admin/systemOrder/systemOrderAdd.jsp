<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    $(function() {
        $('#systemOrderAddForm').form({
            url : '${path}/systemOrder/add',
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
                    var form = $('#systemOrderAddForm');
                    showMsg(result.msg);
                }
            }
        });
    });
</script>
<div class="easyui-layout" data-options="fit:true,border:false" >
    <div data-options="region:'center',border:false" style="overflow: hidden;padding: 3px;" >
        <form id="systemOrderAddForm" method="post">
            <table class="grid">
                <tr>
                    <td>玩家明星号</td>
                    <td><input name="startNo" type="text" placeholder="请输入正确玩家明星号" class="easyui-validatebox span2" data-options="required:true" value=""></td>
                </tr> 
                <tr>
                    <td>付款金额</td>
                    <td><input name="payPrice" type="number" placeholder="请输入付款金额" class="easyui-validatebox span2" data-options="required:true" value=""></td>
                </tr> 
                <tr>
                    <td>房卡数量</td>
                    <td><input name="roomcardAmount" type="number" placeholder="请输入房卡数量" class="easyui-validatebox span2" data-options="required:true" value=""></td>
                </tr> 
                <tr>
                    <td>付款方式</td>
                    <td>
                    	<select name="payType" class="easyui-combobox" data-options="required:true">
                            <option value="2" selected="selected">支付宝</option>
                            <option value="3" >微信</option>
                            <option value="4" >银行转账</option>
                        </select>
                    </td>
                </tr> 
                <tr>
                    <td>付款时间</td>
                    <td><input name="payTime" placeholder="点击选择时间" onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" data-options="required:true" /></td>
                </tr> 
                
            </table>
        </form>
    </div>
</div>