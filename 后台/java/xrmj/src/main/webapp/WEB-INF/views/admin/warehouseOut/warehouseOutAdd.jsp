<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    $(function() {
        $('#warehouseOutAddForm').form({
            url : '${path}/warehouseOut/add',
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
                    var form = $('#warehouseOutAddForm');
                    showMsg(result.msg);
                }
            }
        });
    });
</script>
<div class="easyui-layout" data-options="fit:true,border:false" >
    <div data-options="region:'center',border:false" style="overflow: hidden;padding: 3px;" >
        <form id="warehouseOutAddForm" method="post">
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
                    <td>出库类型</td>
                    <td>
                    	<select id="type" name="type" data-options="width:140,height:29,editable:false,panelHeight:'auto'" onchange="balance()">
							<option value="0" selected="selected">正常出库</option>
							<option value="1">冲账出库</option>
                        </select>
					</td>
                </tr> 
                <tr id="balancetr" style="display: none;">
                	<td>冲账出库单</td>
                	<td>
                    	<select id="warehouseOut" name="warehouseOut" data-options="width:140,height:29,editable:false,panelHeight:'auto'">
                            
                        </select>
					</td>
                </tr>
                <tr>
                    <td>出库数量</td>
                    <td><input name="amount" type="number" placeholder="请输入出库数量" class="easyui-validatebox span2" data-options="required:true" value="1"></td>
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
				commodity : $("input[name='commodity']").val()
			};
		
		$.ajax({  
			url: '${path}/warehouseOut/getAllWarehouseOut',  
			// 请求方式  
			type: "POST",  
			// 服务器响应的数据类型  
			dataType: "json",  
			data: param,  
			success: function (returndata) {
				console.log(returndata);
				var inList = '<option value="" >出库冲账请选择对应的出库单</option>';
				for (var i = 0; i < returndata.length; i++) {
					inList +='<option value="'+returndata[i].id+'" >'+returndata[i].outNo+'</option>';
				}
				$('#warehouseOut').append(inList);
			},  
			error: function (returndata) {  
			    alert(returndata);  
			}  
		}); 
		$("#balancetr").show();
	} else {
		$('#warehouseOut').empty();
		$("#balancetr").hide();
	}
		
}