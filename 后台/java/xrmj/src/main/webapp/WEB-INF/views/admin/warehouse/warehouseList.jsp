<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    var warehouseDataGrid;
    $(function() {
        warehouseDataGrid = $('#warehouseDataGrid').datagrid({
        url : '${path}/warehouse/dataGrid',
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
            width : '140',
            title : '商品名称',
            field : 'commodityName',
            sortable : true
        }, {
            width : '140',
            title : '可用库存',
            field : 'usableAmount',
            sortable : true
        }, {
            width : '140',
            title : '冻结待出库',
            field : 'outboundAmount',
            sortable : true
        }, {
            width : '140',
            title : '总数量',
            field : 'allAmount',
            sortable : true
        }, {
            width : '140',
            title : '已用数量',
            field : 'useAmount',
            sortable : true
        } ] ],
        onLoadSuccess:function(data){
            $('.warehouse-easyui-linkbutton-edit').linkbutton({text:'编辑'});
            $('.warehouse-easyui-linkbutton-del').linkbutton({text:'删除'});
        },
        toolbar : '#warehouseToolbar'
    });
});

/**
 * 添加框
 * @param url
 */
function warehouseAddFun() {
    parent.$.modalDialog({
        title : '添加',
        width : 700,
        height : 600,
        href : '${path}/warehouse/addPage',
        buttons : [ {
            text : '确定',
            handler : function() {
                parent.$.modalDialog.openner_dataGrid = warehouseDataGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
                var f = parent.$.modalDialog.handler.find('#warehouseAddForm');
                f.submit();
            }
        } ]
    });
}


/**
 * 编辑
 */
function warehouseEditFun(id) {
    if (id == undefined) {
        var rows = warehouseDataGrid.datagrid('getSelections');
        id = rows[0].id;
    } else {
        warehouseDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
    }
    parent.$.modalDialog({
        title : '编辑',
        width : 700,
        height : 600,
        href :  '${path}/warehouse/editPage?id=' + id,
        buttons : [ {
            text : '确定',
            handler : function() {
                parent.$.modalDialog.openner_dataGrid = warehouseDataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
                var f = parent.$.modalDialog.handler.find('#warehouseEditForm');
                f.submit();
            }
        } ]
    });
}


/**
 * 删除
 */
 function warehouseDeleteFun(id) {
     if (id == undefined) {//点击右键菜单才会触发这个
         var rows = warehouseDataGrid.datagrid('getSelections');
         id = rows[0].id;
     } else {//点击操作里面的删除图标会触发这个
         warehouseDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
     }
     parent.$.messager.confirm('询问', '您是否要删除当前角色？', function(b) {
         if (b) {
             progressLoad();
             $.post('${path}/warehouse/delete', {
                 id : id
             }, function(result) {
                 if (result.success) {
                     parent.$.messager.alert('提示', result.msg, 'info');
                     warehouseDataGrid.datagrid('reload');
                 }
                 progressClose();
             }, 'JSON');
         }
     });
}


/**
 * 清除
 */
function warehouseCleanFun() {
    $('#warehouseSearchForm input').val('');
    warehouseDataGrid.datagrid('load', {});
}
/**
 * 搜索
 */
function warehouseSearchFun() {
     warehouseDataGrid.datagrid('load', $.serializeObject($('#warehouseSearchForm')));
}
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="warehouseSearchForm">
            <table>
                <tr>
                    <th>商品名称:</th>
                    <td><input name="commodityName" placeholder="商品名称"/></td>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="warehouseSearchFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'fi-x-circle',plain:true" onclick="warehouseCleanFun();">清空</a>
                    </td>
                </tr>
            </table>
        </form>
     </div>
 
    <div data-options="region:'center',border:false">
        <table id="warehouseDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>
<div id="warehouseToolbar" style="display: none;">
    <shiro:hasPermission name="/warehouse/add">
        <a onclick="warehouseAddFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'fi-page-add'">添加</a>
    </shiro:hasPermission>
</div>