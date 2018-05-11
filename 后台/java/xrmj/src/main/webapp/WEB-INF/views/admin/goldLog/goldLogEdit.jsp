<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    $(function() {
        $('#goldLogEditForm').form({
            url : '${path}/goldLog/edit',
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
                    var form = $('#goldLogEditForm');
                    showMsg(result.msg);
                }
            }
        });
        
        $("#status").val('${goldLog.status}'); 
        
    });
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 3px;">
        <form id="goldLogEditForm" method="post">
            <table class="grid">
                <tr>
                    <td>明星号</td>
                    <td><input name="id" type="hidden"  value="${goldLog.id}">
                    <input name="startNo" type="text" placeholder="请输入明星号" class="easyui-validatebox" data-options="required:true" value="${goldLog.startNo}" readonly="readonly"></td>
                </tr>
                <tr>
                    <td>玩家昵称</td>
                    <td><input name="playerNick" type="text" placeholder="请输入玩家昵称" class="easyui-validatebox" data-options="required:true" value="${goldLog.playerNick}" readonly="readonly"></td>
                </tr>
                <tr>
                    <td>消耗数量</td>
                    <td><input name="consume" type="text" placeholder="请输入消耗数量" class="easyui-validatebox" data-options="required:true" value="${goldLog.consume}" readonly="readonly"></td>
                </tr>
                <tr>
                    <td>商品名称</td>
                    <td><input name="commodityName" type="text" placeholder="请输入商品名称" class="easyui-validatebox" data-options="required:true" value="${goldLog.commodityName}" readonly="readonly"></td>
                </tr>
                <tr>
                    <td>出库单号</td>
                    <td><input name="warehouseOutNo" type="text" placeholder="请输入出库单号" class="easyui-validatebox" data-options="required:true" value="${goldLog.warehouseOutNo}" readonly="readonly"></td>
                </tr>
                <tr>
                    <td>状态</td>
                    <td >
                        <select id="status" name="status" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'">
                            <option value="0">待发货</option>
                            <option value="1">已发货</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>订单人姓名</td>
                    <td><input name="playerName" type="text" placeholder="请输入订单人姓名" class="easyui-validatebox" data-options="required:true" value="${goldLog.playerName}"></td>
                </tr>
                <tr>
                    <td>订单人电话</td>
                    <td><input name="playerPhone" type="text" placeholder="请输入订单人电话" class="easyui-validatebox" data-options="required:true" value="${goldLog.playerPhone}"></td>
                </tr>
                <tr>
                    <td>订单人地址</td>
                    <td><input name="playerAddress" type="text" placeholder="请输入订单人地址" class="easyui-validatebox" data-options="required:true" value="${goldLog.playerAddress}"></td>
                </tr>
                <tr>
                    <td>快递公司</td>
                    <td><input name="express" type="text" placeholder="请输入快递公司" class="easyui-validatebox" value="${goldLog.express}"></td>
                </tr>
                <tr>
                    <td>快递单号</td>
                    <td><input name="expressCode" type="text" placeholder="请输入快递单号" class="easyui-validatebox" value="${goldLog.expressCode}"></td>
                </tr>
            </table>
        </form>
    </div>
</div>