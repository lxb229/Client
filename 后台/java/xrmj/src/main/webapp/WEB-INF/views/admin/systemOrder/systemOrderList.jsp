<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    var systemOrderDataGrid;
    $(function() {
        systemOrderDataGrid = $('#systemOrderDataGrid').datagrid({
        url : '${path}/systemOrder/dataGrid',
        striped : true,
        rownumbers : true,
        pagination : true,
        singleSelect : true,
        idField : 'id',
        sortName : 's.id',
        sortOrder : 'asc',
        pageSize : 20,
        pageList : [ 10, 20, 30, 40, 50, 100, 200, 300, 400, 500],
        frozenColumns : [ [ {
            width : '60',
            title : '编号',
            field : 'id',
            sortable : true
        }, {
            width : '60',
            title : '购买渠道',
            field : 'purchaseType',
            sortable : true,
            formatter : function(value, row, index) {
                switch (value) {
                case 1:
                    return '线下购买';
                case 2:
                    return 'APP内购';
                case 3:
                    return '网页购买';
                case 4:
                    return '公众号购买';
                }
            }
        }, {
            width : '100',
            title : '订单号',
            field : 'orderNo',
            sortable : true
        }, {
            width : '80',
            title : 'GM操作人',
            field : 'name',
            sortable : true
        }, {
            width : '80',
            title : '玩家明星号',
            field : 'startNo',
            sortable : true
        }, {
            width : '100',
            title : '玩家昵称',
            field : 'nick',
            sortable : true
        }, {
            width : '70',
            title : '支付金额',
            field : 'payPrice',
            sortable : true
        }, {
            width : '60',
            title : '支付方式',
            field : 'payType',
            sortable : true,
            formatter : function(value, row, index) {
                switch (value) {
                case 1:
                    return 'App Store';
                case 2:
                    return '支付宝';
                case 3:
                    return '微信';
                case 4:
                    return '银行转账';
                }
            }
        }, {
            width : '60',
            title : '付款状态',
            field : 'payStatus',
            sortable : true,
            formatter : function(value, row, index) {
                switch (value) {
                case 0:
                    return '待支付';
                case 1:
                    return '已支付';
                }
            }
        }, {
            width : '110',
            title : '付款时间',
            field : 'payTime',
            sortable : true
        }, {
            width : '70',
            title : '房卡单价',
            field : 'roomcardPrice',
            sortable : true
        }, {
            width : '70',
            title : '房卡数量',
            field : 'roomcardAmount',
            sortable : true
        }, {
            width : '140',
            title : '创建时间',
            field : 'createTime',
            sortable : true
        } ] ],
        onLoadSuccess:function(data){
            $('.systemOrder-easyui-linkbutton-edit').linkbutton({text:'编辑'});
            $('.systemOrder-easyui-linkbutton-del').linkbutton({text:'删除'});
        },
        toolbar : '#systemOrderToolbar'
    });
});

/**
 * 添加框
 * @param url
 */
function systemOrderAddFun() {
    parent.$.modalDialog({
        title : '添加',
        width : 700,
        height : 600,
        href : '${path}/systemOrder/addPage',
        buttons : [ {
            text : '确定',
            handler : function() {
                parent.$.modalDialog.openner_dataGrid = systemOrderDataGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
                var f = parent.$.modalDialog.handler.find('#systemOrderAddForm');
                f.submit();
            }
        } ]
    });
}


/**
 * 编辑
 */
function systemOrderEditFun(id) {
    if (id == undefined) {
        var rows = systemOrderDataGrid.datagrid('getSelections');
        id = rows[0].id;
    } else {
        systemOrderDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
    }
    parent.$.modalDialog({
        title : '编辑',
        width : 700,
        height : 600,
        href :  '${path}/systemOrder/editPage?id=' + id,
        buttons : [ {
            text : '确定',
            handler : function() {
                parent.$.modalDialog.openner_dataGrid = systemOrderDataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
                var f = parent.$.modalDialog.handler.find('#systemOrderEditForm');
                f.submit();
            }
        } ]
    });
}


/**
 * 删除
 */
 function systemOrderDeleteFun(id) {
     if (id == undefined) {//点击右键菜单才会触发这个
         var rows = systemOrderDataGrid.datagrid('getSelections');
         id = rows[0].id;
     } else {//点击操作里面的删除图标会触发这个
         systemOrderDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
     }
     parent.$.messager.confirm('询问', '您是否要删除当前角色？', function(b) {
         if (b) {
             progressLoad();
             $.post('${path}/systemOrder/delete', {
                 id : id
             }, function(result) {
                 if (result.success) {
                     parent.$.messager.alert('提示', result.msg, 'info');
                     systemOrderDataGrid.datagrid('reload');
                 }
                 progressClose();
             }, 'JSON');
         }
     });
}


/**
 * 清除
 */
function systemOrderCleanFun() {
    $('#systemOrderSearchForm input').val('');
    systemOrderDataGrid.datagrid('load', {});
}
/**
 * 搜索
 */
function systemOrderSearchFun() {
     systemOrderDataGrid.datagrid('load', $.serializeObject($('#systemOrderSearchForm')));
}
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="systemOrderSearchForm">
            <table>
                <tr>
                    <th>明星号:</th>
                    <td><input name="startNo" placeholder="玩家明星号"/></td>
                    <th>购买渠道:</th>
                    <td>
                    	<select name="purchaseType" placeholder="购买渠道" class="easyui-combobox" data-options="required:true">
                            <option value="0" selected="selected">全部</option>
                            <option value="1" >线下购买</option>
                            <option value="2" >App内购</option>
                            <option value="3" >网页购买</option>
                            <option value="4" >公众号购买</option>
                        </select>
                    </td>
                    <th>支付方式:</th>
                    <td>
                    	<select name="payType" placeholder="支付方式" class="easyui-combobox" data-options="required:true">
                            <option value="0" selected="selected">全部</option>
                            <option value="1" >App Store</option>
                            <option value="2" >支付宝</option>
                            <option value="3" >微信</option>
                            <option value="4" >银行转账</option>
                        </select>
                    </td>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="systemOrderSearchFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'fi-x-circle',plain:true" onclick="systemOrderCleanFun();">清空</a>
                    </td>
                </tr>
            </table>
        </form>
     </div>
 
    <div data-options="region:'center',border:false">
        <table id="systemOrderDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>
<div id="systemOrderToolbar" style="display: none;">
    <shiro:hasPermission name="/systemOrder/add">
        <a onclick="systemOrderAddFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'fi-page-add'">添加</a>
    </shiro:hasPermission>
</div>