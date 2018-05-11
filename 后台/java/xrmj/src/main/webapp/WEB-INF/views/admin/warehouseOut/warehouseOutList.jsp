<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    var warehouseOutDataGrid;
    $(function() {
        warehouseOutDataGrid = $('#warehouseOutDataGrid').datagrid({
        url : '${path}/warehouseOut/dataGrid',
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
        }, {
            width : '100',
            title : '出库单号',
            field : 'outNo',
            sortable : true
        }, {
            width : '60',
            title : '类型',
            field : 'type',
            sortable : true,
            formatter : function(value, row, index) {
                switch (value) {
                case 0:
                    return '正常出库';
                case 1:
                    return '冲账出库';
                case 2:
                    return '抽奖出库';
                case 3:
                    return '兑换出库';
                }
            }
        }, {
            width : '110',
            title : '商品名称',
            field : 'commodityName',
            sortable : true
        }, {
            width : '70',
            title : '出库数量',
            field : 'amount',
            sortable : true
        }, {
            width : '140',
            title : '冲账出库对应入库',
            field : 'warehouseOutNo',
            sortable : true
        }, {
            width : '140',
            title : '备注',
            field : 'remark',
            sortable : true
        }, {
            width : '100',
            title : '出库人',
            field : 'userName',
            sortable : true
        }, {
            width : '100',
            title : '出库时间',
            field : 'createTime',
            sortable : true
        } ] ],
        onLoadSuccess:function(data){
            $('.warehouseOut-easyui-linkbutton-edit').linkbutton({text:'编辑'});
            $('.warehouseOut-easyui-linkbutton-del').linkbutton({text:'删除'});
        },
        toolbar : '#warehouseOutToolbar'
    });
});

/**
 * 添加框
 * @param url
 */
function warehouseOutAddFun() {
    parent.$.modalDialog({
        title : '添加',
        width : 700,
        height : 600,
        href : '${path}/warehouseOut/addPage',
        buttons : [ {
            text : '确定',
            handler : function() {
                parent.$.modalDialog.openner_dataGrid = warehouseOutDataGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
                var f = parent.$.modalDialog.handler.find('#warehouseOutAddForm');
                f.submit();
            }
        } ]
    });
}


/**
 * 编辑
 */
function warehouseOutEditFun(id) {
    if (id == undefined) {
        var rows = warehouseOutDataGrid.datagrid('getSelections');
        id = rows[0].id;
    } else {
        warehouseOutDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
    }
    parent.$.modalDialog({
        title : '编辑',
        width : 700,
        height : 600,
        href :  '${path}/warehouseOut/editPage?id=' + id,
        buttons : [ {
            text : '确定',
            handler : function() {
                parent.$.modalDialog.openner_dataGrid = warehouseOutDataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
                var f = parent.$.modalDialog.handler.find('#warehouseOutEditForm');
                f.submit();
            }
        } ]
    });
}


/**
 * 删除
 */
 function warehouseOutDeleteFun(id) {
     if (id == undefined) {//点击右键菜单才会触发这个
         var rows = warehouseOutDataGrid.datagrid('getSelections');
         id = rows[0].id;
     } else {//点击操作里面的删除图标会触发这个
         warehouseOutDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
     }
     parent.$.messager.confirm('询问', '您是否要删除当前角色？', function(b) {
         if (b) {
             progressLoad();
             $.post('${path}/warehouseOut/delete', {
                 id : id
             }, function(result) {
                 if (result.success) {
                     parent.$.messager.alert('提示', result.msg, 'info');
                     warehouseOutDataGrid.datagrid('reload');
                 }
                 progressClose();
             }, 'JSON');
         }
     });
}


/**
 * 清除
 */
function warehouseOutCleanFun() {
    $('#warehouseOutSearchForm input').val('');
    warehouseOutDataGrid.datagrid('load', {});
}
/**
 * 搜索
 */
function warehouseOutSearchFun() {
     warehouseOutDataGrid.datagrid('load', $.serializeObject($('#warehouseOutSearchForm')));
}
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="warehouseOutSearchForm">
            <table>
                <tr>
                    <th>商品名称:</th>
                    <td><input name="commodityName" placeholder="搜索商品名称"/></td>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="warehouseOutSearchFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'fi-x-circle',plain:true" onclick="warehouseOutCleanFun();">清空</a>
                    </td>
                </tr>
            </table>
        </form>
     </div>
 
    <div data-options="region:'center',border:false">
        <table id="warehouseOutDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>
<div id="warehouseOutToolbar" style="display: none;">
    <shiro:hasPermission name="/warehouseOut/add">
        <a onclick="warehouseOutAddFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'fi-page-add'">添加</a>
    </shiro:hasPermission>
</div>