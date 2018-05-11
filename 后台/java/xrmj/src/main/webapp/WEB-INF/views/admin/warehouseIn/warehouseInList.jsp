<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    var warehouseInDataGrid;
    $(function() {
        warehouseInDataGrid = $('#warehouseInDataGrid').datagrid({
        url : '${path}/warehouseIn/dataGrid',
        striped : true,
        rownumbers : true,
        pagination : true,
        singleSelect : true,
        idField : 'id',
        sortName : 'id',
        sortOrder : 'asc',
        pageSize : 20,
        pageList : [ 10, 20, 30, 40, 50, 100, 200, 300, 400, 500],
        frozenColumns : [ [ {
            width : '60',
            title : '编号',
            field : 'id',
            sortable : true
        },{
            width : '90',
            title : '入库单号',
            field : 'inNo',
            sortable : true
        }, {
            width : '100',
            title : '供应商',
            field : 'supplierName',
            sortable : true
        }, {
            width : '100',
            title : '商品',
            field : 'commodityName',
            sortable : true
        }, {
            width : '60',
            title : '类型',
            field : 'type',
            sortable : true,
            formatter : function(value, row, index) {
                switch (value) {
                case 0:
                    return '正常入库';
                case 1:
                    return '入库冲账';
                }
            }
        },{
            width : '120',
            title : '入库冲账对应入库',
            field : 'warehouseInNo',
            sortable : true
        }, {
            width : '80',
            title : '数量',
            field : 'amount',
            sortable : true
        }, {
            width : '90',
            title : '价格',
            field : 'priceAmount',
            sortable : true
        }, {
            width : '140',
            title : '备注',
            field : 'remark',
            sortable : true
        }, {
            width : '80',
            title : '入库人',
            field : 'createName',
            sortable : true
        }, {
            width : '80',
            title : '入库时间',
            field : 'createTime',
            sortable : true
        }, {
            width : '80',
            title : '修改人',
            field : 'updateName',
            sortable : true
        }, {
            width : '80',
            title : '修改时间',
            field : 'updateTime',
            sortable : true
        }, {
            field : 'action',
            title : '操作',
            width : 200,
            formatter : function(value, row, index) {
                var str = '';
                <shiro:hasPermission name="/warehouseIn/edit">
                    str += $.formatString('<a href="javascript:void(0)" class="warehouseIn-easyui-linkbutton-edit" data-options="plain:true,iconCls:\'fi-pencil icon-blue\'" onclick="warehouseInEditFun(\'{0}\');" >编辑</a>', row.id);
                </shiro:hasPermission>
                <shiro:hasPermission name="/warehouseIn/delete">
                    str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
                    str += $.formatString('<a href="javascript:void(0)" class="warehouseIn-easyui-linkbutton-del" data-options="plain:true,iconCls:\'fi-x icon-red\'" onclick="warehouseInDeleteFun(\'{0}\');" >删除</a>', row.id);
                </shiro:hasPermission>
                return str;
            }
        } ] ],
        onLoadSuccess:function(data){
            $('.warehouseIn-easyui-linkbutton-edit').linkbutton({text:'编辑'});
            $('.warehouseIn-easyui-linkbutton-del').linkbutton({text:'删除'});
        },
        toolbar : '#warehouseInToolbar'
    });
});

/**
 * 添加框
 * @param url
 */
function warehouseInAddFun() {
    parent.$.modalDialog({
        title : '添加',
        width : 700,
        height : 600,
        href : '${path}/warehouseIn/addPage',
        buttons : [ {
            text : '确定',
            handler : function() {
                parent.$.modalDialog.openner_dataGrid = warehouseInDataGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
                var f = parent.$.modalDialog.handler.find('#warehouseInAddForm');
                f.submit();
            }
        } ]
    });
}


/**
 * 编辑
 */
function warehouseInEditFun(id) {
    if (id == undefined) {
        var rows = warehouseInDataGrid.datagrid('getSelections');
        id = rows[0].id;
    } else {
        warehouseInDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
    }
    parent.$.modalDialog({
        title : '编辑',
        width : 700,
        height : 600,
        href :  '${path}/warehouseIn/editPage?id=' + id,
        buttons : [ {
            text : '确定',
            handler : function() {
                parent.$.modalDialog.openner_dataGrid = warehouseInDataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
                var f = parent.$.modalDialog.handler.find('#warehouseInEditForm');
                f.submit();
            }
        } ]
    });
}


/**
 * 删除
 */
 function warehouseInDeleteFun(id) {
     if (id == undefined) {//点击右键菜单才会触发这个
         var rows = warehouseInDataGrid.datagrid('getSelections');
         id = rows[0].id;
     } else {//点击操作里面的删除图标会触发这个
         warehouseInDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
     }
     parent.$.messager.confirm('询问', '您是否要删除当前角色？', function(b) {
         if (b) {
             progressLoad();
             $.post('${path}/warehouseIn/delete', {
                 id : id
             }, function(result) {
                 if (result.success) {
                     parent.$.messager.alert('提示', result.msg, 'info');
                     warehouseInDataGrid.datagrid('reload');
                 }
                 progressClose();
             }, 'JSON');
         }
     });
}


/**
 * 清除
 */
function warehouseInCleanFun() {
    $('#warehouseInSearchForm input').val('');
    warehouseInDataGrid.datagrid('load', {});
}
/**
 * 搜索
 */
function warehouseInSearchFun() {
     warehouseInDataGrid.datagrid('load', $.serializeObject($('#warehouseInSearchForm')));
}
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="warehouseInSearchForm">
            <table>
                <tr>
                    <th>商品名称:</th>
                    <td><input name="commodityName" placeholder="搜索商品名称"/></td>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="warehouseInSearchFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'fi-x-circle',plain:true" onclick="warehouseInCleanFun();">清空</a>
                    </td>
                </tr>
            </table>
        </form>
     </div>
 
    <div data-options="region:'center',border:false">
        <table id="warehouseInDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>
<div id="warehouseInToolbar" style="display: none;">
    <shiro:hasPermission name="/warehouseIn/add">
        <a onclick="warehouseInAddFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'fi-page-add'">添加</a>
    </shiro:hasPermission>
</div>