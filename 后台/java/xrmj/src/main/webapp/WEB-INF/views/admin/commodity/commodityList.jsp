<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    var commodityDataGrid;
    $(function() {
        commodityDataGrid = $('#commodityDataGrid').datagrid({
        url : '${path}/commodity/dataGrid',
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
            title : '商品编号',
            field : 'commodityNo',
            sortable : true
        }, {
            width : '90',
            title : '条形码',
            field : 'barCode',
            sortable : true
        }, {
            width : '60',
            title : '商品类型',
            field : 'typeName',
            sortable : true
        }, {
            width : '100',
            title : '商品名称',
            field : 'commodityName',
            sortable : true
        }, {
            width : '70',
            title : '商品规格',
            field : 'specification',
            sortable : true
        }, {
            width : '50',
            title : '库存预警',
            field : 'alarm',
            sortable : true
        }, {
            width : '60',
            title : '产地',
            field : 'origin',
            sortable : true
        }, {
            width : '60',
            title : '品牌',
            field : 'brand',
            sortable : true
        }, {
            width : '50',
            title : '制造商',
            field : 'manufacturer',
            sortable : true
        }, {
            width : '60',
            title : '备注',
            field : 'remark',
            sortable : true
        }, {
            field : 'action',
            title : '操作',
            width : 200,
            formatter : function(value, row, index) {
                var str = '';
                <shiro:hasPermission name="/commodity/edit">
                    str += $.formatString('<a href="javascript:void(0)" class="commodity-easyui-linkbutton-edit" data-options="plain:true,iconCls:\'fi-pencil icon-blue\'" onclick="commodityEditFun(\'{0}\');" >编辑</a>', row.id);
                </shiro:hasPermission>
                <shiro:hasPermission name="/commodity/delete">
                    str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
                    str += $.formatString('<a href="javascript:void(0)" class="commodity-easyui-linkbutton-del" data-options="plain:true,iconCls:\'fi-x icon-red\'" onclick="commodityDeleteFun(\'{0}\');" >删除</a>', row.id);
                </shiro:hasPermission>
                return str;
            }
        } ] ],
        onLoadSuccess:function(data){
            $('.commodity-easyui-linkbutton-edit').linkbutton({text:'编辑'});
            $('.commodity-easyui-linkbutton-del').linkbutton({text:'删除'});
        },
        toolbar : '#commodityToolbar'
    });
});

/**
 * 添加框
 * @param url
 */
function commodityAddFun() {
    parent.$.modalDialog({
        title : '添加',
        width : 700,
        height : 600,
        href : '${path}/commodity/addPage',
        buttons : [ {
            text : '确定',
            handler : function() {
                parent.$.modalDialog.openner_dataGrid = commodityDataGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
                var f = parent.$.modalDialog.handler.find('#commodityAddForm');
                f.submit();
            }
        } ]
    });
}


/**
 * 编辑
 */
function commodityEditFun(id) {
    if (id == undefined) {
        var rows = commodityDataGrid.datagrid('getSelections');
        id = rows[0].id;
    } else {
        commodityDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
    }
    parent.$.modalDialog({
        title : '编辑',
        width : 700,
        height : 600,
        href :  '${path}/commodity/editPage?id=' + id,
        buttons : [ {
            text : '确定',
            handler : function() {
                parent.$.modalDialog.openner_dataGrid = commodityDataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
                var f = parent.$.modalDialog.handler.find('#commodityEditForm');
                f.submit();
            }
        } ]
    });
}


/**
 * 删除
 */
 function commodityDeleteFun(id) {
     if (id == undefined) {//点击右键菜单才会触发这个
         var rows = commodityDataGrid.datagrid('getSelections');
         id = rows[0].id;
     } else {//点击操作里面的删除图标会触发这个
         commodityDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
     }
     parent.$.messager.confirm('询问', '您是否要删除当前角色？', function(b) {
         if (b) {
             progressLoad();
             $.post('${path}/commodity/delete', {
                 id : id
             }, function(result) {
                 if (result.success) {
                     parent.$.messager.alert('提示', result.msg, 'info');
                     commodityDataGrid.datagrid('reload');
                 }
                 progressClose();
             }, 'JSON');
         }
     });
}


/**
 * 清除
 */
function commodityCleanFun() {
    $('#commoditySearchForm input').val('');
    commodityDataGrid.datagrid('load', {});
}
/**
 * 搜索
 */
function commoditySearchFun() {
     commodityDataGrid.datagrid('load', $.serializeObject($('#commoditySearchForm')));
}
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="commoditySearchForm">
            <table>
                <tr>
                    <th>商品名称:</th>
                    <td><input name="commodityName" placeholder="商品名称"/></td>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="commoditySearchFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'fi-x-circle',plain:true" onclick="commodityCleanFun();">清空</a>
                    </td>
                </tr>
            </table>
        </form>
     </div>
 
    <div data-options="region:'center',border:false">
        <table id="commodityDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>
<div id="commodityToolbar" style="display: none;">
    <shiro:hasPermission name="/commodity/add">
        <a onclick="commodityAddFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'fi-page-add'">添加</a>
    </shiro:hasPermission>
</div>