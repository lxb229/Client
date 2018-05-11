<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    $(function() {
        $('#commodityAddForm').form({
            url : '${path}/commodity/add',
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
                    var form = $('#commodityAddForm');
                    showMsg(result.msg);
                }
            }
        });
    });
</script>
<div class="easyui-layout" data-options="fit:true,border:false" >
    <div data-options="region:'center',border:false" style="overflow: hidden;padding: 3px;" >
        <form id="commodityAddForm" method="post">
            <table class="grid">
                <tr>
                    <td>条形码</td>
                    <td><input name="barCode" type="text" placeholder="请输入条形码" class="easyui-validatebox span2" data-options="required:true" value=""></td>
                </tr> 
                <tr>
                    <td>商品类型</td>
                    <td>
                    	<select name="commodityType" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'">
                            <c:forEach items="${typeList }" var="c">
								<option value="${c.id }" >${c.typeName }</option>
							</c:forEach>
                        </select>
					</td>
                </tr> 
                <tr>
                    <td>商品名称</td>
                    <td><input name="commodityName" type="text" placeholder="请输入商品名称" class="easyui-validatebox span2" data-options="required:true" value=""></td>
                </tr> 
                <tr>
                    <td>商品规格</td>
                    <td><input name="specification" type="text" placeholder="请输入商品规格" class="easyui-validatebox span2" data-options="required:true" value=""></td>
                </tr> 
                <tr>
                    <td>库存预警</td>
                    <td><input name="alarm" type="number" placeholder="请输入库存预警" class="easyui-validatebox span2" data-options="required:true" value=""></td>
                </tr> 
                <tr>
                    <td>产地</td>
                    <td><input name="origin" type="text" placeholder="请输入产地" class="easyui-validatebox span2" data-options="required:true" value=""></td>
                </tr> 
                <tr>
                    <td>品牌</td>
                    <td><input name="brand" type="text" placeholder="请输入品牌" class="easyui-validatebox span2" data-options="required:true" value=""></td>
                </tr> 
                <tr>
                    <td>制造商</td>
                    <td><input name="manufacturer" type="text" placeholder="请输入制造商" class="easyui-validatebox span2" data-options="required:true" value=""></td>
                </tr> 
                <tr>
                    <td>备注</td>
                    <td><input name="remark" type="text" placeholder="请输入备注" class="easyui-validatebox span2" value=""></td>
                </tr> 
                
            </table>
        </form>
    </div>
</div>