<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    $(function() {
        $('#warehouseInAddForm').form({
            url : '${path}/warehouseIn/add',
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
                    var form = $('#warehouseInAddForm');
                    showMsg(result.msg);
                }
            }
        });
    });
</script>
<div class="easyui-layout" data-options="fit:true,border:false" >
    <div data-options="region:'center',border:false" style="overflow: hidden;padding: 3px;" >
        <form id="warehouseInAddForm" method="post">
            <table class="grid">
            	<tr>
                    <td>供应商</td>
                    <td>
                    	<select name="supplier" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'">
                            <c:forEach items="${supplierList }" var="c">
								<option value="${c.id }" >${c.supplierName }</option>
							</c:forEach>
                        </select>
					</td>
                </tr> 
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
                    <td>入库类型</td>
                    <td>
                    	<select id="type" name="type" data-options="width:140,height:29,editable:false,panelHeight:'auto'" onchange="balance()">
							<option value="0" selected="selected">正常入库</option>
							<option value="1">入库冲账</option>
                        </select>
					</td>
                </tr> 
                <tr id="balancetr" style="display: none;">
                	<td>冲账入库单</td>
                	<td>
                    	<select id="warehouseIn" name="warehouseIn" data-options="width:140,height:29,editable:false,panelHeight:'auto'">
                            
                        </select>
					</td>
                </tr>
                <tr>
                    <td>入库数量</td>
                    <td><input name="amount" type="number" placeholder="请输入入库数量" class="easyui-validatebox span2" data-options="required:true" value="1"></td>
                </tr>
                <tr>
                    <td>采购价格</td>
                    <td><input name="priceAmount" type="text" placeholder="请输入采购价格" class="easyui-validatebox span2" value=""></td>
                </tr> 
                <tr>
                    <td>考号</td>
                    <td><input name="cardNo" type="text" placeholder="请输入考号" class="easyui-validatebox span2" value=""></td>
                </tr>
                <tr>
                    <td>密钥</td>
                    <td><input name="secretKey" type="text" placeholder="请输入密钥" class="easyui-validatebox span2" value=""></td>
                </tr>
                <tr>
                    <td>备注</td>
                    <td>
                    	<textarea name="remark" placeholder="请输入备注" rows="6" cols="50"></textarea>
                    </td>
                </tr> 
            </table>
        </form>
    </div>
</div>

<script type="text/javascript">
function balance() {  
	var type = $("#type").val();
	if(type == 1) {
		var param = {
				supplier : $("input[name='supplier']").val(),
				commodity : $("input[name='commodity']").val()
			};
		
		$.ajax({  
			url: '${path}/warehouseIn/getAllWarehouseIn',  
			// 请求方式  
			type: "POST",  
			// 服务器响应的数据类型  
			dataType: "json",  
			data: param,  
			success: function (returndata) {
				console.log(returndata);
				var inList = '<option value="" >入库冲账请选择对应的入库单</option>';
				for (var i = 0; i < returndata.length; i++) {
					inList +='<option value="'+returndata[i].id+'" >'+returndata[i].inNo+'</option>';
				}
				$('#warehouseIn').append(inList);
			},  
			error: function (returndata) {  
			    alert(returndata);  
			}  
		}); 
		$("#balancetr").show();
	} else {
		$('#warehouseIn').empty();
		$("#balancetr").hide();
	}
		
}
</script>
