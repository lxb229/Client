<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    $(function() {
        $('#goldCommodityAddForm').form({
            url : '${path}/goldCommodity/add',
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
                    var form = $('#goldCommodityAddForm');
                    showMsg(result.msg);
                }
            }
        });
    });
</script>
<div class="easyui-layout" data-options="fit:true,border:false" >
    <div data-options="region:'center',border:false" style="overflow: hidden;padding: 3px;" >
        <form id="goldCommodityAddForm" method="post">
            <table class="grid">
                <tr>
                	<td>商品</td>
                    <td>
                    	<select name="commodity" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'">
                            <c:forEach items="${commodityList }" var="c">
								<option value="${c.id }" >${c.commodityName }</option>
							</c:forEach>
                        </select>
					</td>
                </tr> 
                <tr>
                    <td>市场价</td>
                    <td><input name="marketPrice" type="number" placeholder="请输入市场价" class="easyui-validatebox span2" data-options="required:true" value=""></td>
                </tr> 
                <tr>
                    <td>兑换价</td>
                    <td><input name="exchangePrice" type="number" placeholder="请输入兑换价" class="easyui-validatebox span2" data-options="required:true" value=""></td>
                </tr> 
                <tr>
                    <td>邮件内容</td>
                    <td>
                    	<textarea name="emailContent" placeholder="请输入邮件内容" rows="6" cols="50"></textarea>
                    </td>
                </tr> 
                <tr>
                    <td>兑换数量</td>
                    <td><input name="amount" type="number" placeholder="请输入兑换数量" class="easyui-validatebox span2" data-options="required:true" value=""></td>
                </tr> 
                <tr>
                	<td>上架时间</td>
                    <td><input name="issuedTime" placeholder="点击选择上架时间" onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" data-options="required:true" /></td>
                </tr>
                <tr>
                	<td>下架时间</td>
                    <td><input name="soldoutTime" placeholder="点击选择下架时间" onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" data-options="required:true" /></td>
                </tr>
                <tr>
                    <td>商品介绍</td>
                    <td><input name="introduce" type="text" placeholder="请输入商品地址" class="easyui-validatebox span2"  value=""></td>
                </tr> 
                <tr>
                    <td>官网地址</td>
                    <td><input name="onlineUrl" type="text" placeholder="请输入官网地址" class="easyui-validatebox span2"  value=""></td>
                </tr> 
            </table>
        </form>
    </div>
</div>