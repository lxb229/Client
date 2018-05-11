<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    $(function() {
        $('#warehouseInEditForm').form({
            url : '${path}/warehouseIn/edit',
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
                    var form = $('#warehouseInEditForm');
                    showMsg(result.msg);
                }
            }
        });
        
        $("#supplier").val('${warehouseIn.supplier}'); 
        $("#commodity").val('${warehouseIn.commodity}'); 
        $("#type").val('${warehouseIn.type}'); 
        
    });
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 3px;">
        <form id="warehouseInEditForm" method="post">
            <table class="grid">
                <tr>
                    <td>供应商</td>
                    <td><input name="id" type="hidden"  value="${warehouseIn.id}">
                    	<select id="supplier" name="supplier" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'" readonly="readonly">
                            <c:forEach items="${supplierList }" var="c">
								<option value="${c.id }" >${c.supplierName }</option>
							</c:forEach>
                        </select>
					</td>
                </tr> 
                <tr>
                    <td>商品</td>
                    <td>
                    	<select id="commodity" name="commodity" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'" readonly="readonly">
                            <c:forEach items="${commodityList }" var="c">
								<option value="${c.id }" >${c.commodityName }</option>
							</c:forEach>
                        </select>
					</td>
                </tr> 
                <tr>
                    <td>入库类型</td>
                    <td>
                    	<select id="type" name="type" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'" readonly="readonly">
							<option value="0">正常入库</option>
							<option value="1">入库冲账</option>
                        </select>
					</td>
                </tr> 
                <tr>
                    <td>入库数量</td>
                    <td><input name="amount" type="number" placeholder="请输入入库数量" class="easyui-validatebox span2" data-options="required:true" value="${warehouseIn.amount}" readonly="readonly"></td>
                </tr>
                <tr>
                    <td>采购价格</td>
                    <td><input name="priceAmount" type="text" placeholder="请输入采购价格" class="easyui-validatebox span2" data-options="required:true" value="${warehouseIn.priceAmount}"></td>
                </tr> 
                <tr>
                    <td>备注</td>
                    <td>
                    	<textarea name="remark" placeholder="请输入备注" rows="6" cols="50" >${warehouseIn.remark}</textarea>
                    </td>
                </tr>  
            </table>
        </form>
    </div>
</div>