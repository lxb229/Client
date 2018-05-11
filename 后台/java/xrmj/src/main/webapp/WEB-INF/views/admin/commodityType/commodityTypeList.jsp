<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    var commodityTypeDataGrid;
    $(function() {
        commodityTypeDataGrid = $('#commodityTypeDataGrid').datagrid({
        url : '${path}/commodityType/dataGrid',
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
            title : '类型名称',
            field : 'typeName',
            sortable : true
        }, {
            width : '140',
            title : '排序',
            field : 'rank',
            sortable : true
        }, {
            field : 'action',
            title : '操作',
            width : 200,
            formatter : function(value, row, index) {
                var str = '';
                <shiro:hasPermission name="/commodityType/edit">
                    str += $.formatString('<a href="javascript:void(0)" class="commodityType-easyui-linkbutton-edit" data-options="plain:true,iconCls:\'fi-pencil icon-blue\'" onclick="commodityTypeEditFun(\'{0}\');" >编辑</a>', row.id);
                </shiro:hasPermission>
                <shiro:hasPermission name="/commodityType/delete">
                    str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
                    str += $.formatString('<a href="javascript:void(0)" class="commodityType-easyui-linkbutton-del" data-options="plain:true,iconCls:\'fi-x icon-red\'" onclick="commodityTypeDeleteFun(\'{0}\');" >删除</a>', row.id);
                </shiro:hasPermission>
                return str;
            }
        } ] ],
        onLoadSuccess:function(data){
            $('.commodityType-easyui-linkbutton-edit').linkbutton({text:'编辑'});
            $('.commodityType-easyui-linkbutton-del').linkbutton({text:'删除'});
        },
        toolbar : '#commodityTypeToolbar'
    });
});

/**
 * 添加框
 * @param url
 */
function commodityTypeAddFun() {
    parent.$.modalDialog({
        title : '添加',
        width : 700,
        height : 600,
        href : '${path}/commodityType/addPage',
        buttons : [ {
            text : '确定',
            handler : function() {
                parent.$.modalDialog.openner_dataGrid = commodityTypeDataGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
                var f = parent.$.modalDialog.handler.find('#commodityTypeAddForm');
                f.submit();
            }
        } ]
    });
}


/**
 * 编辑
 */
function commodityTypeEditFun(id) {
    if (id == undefined) {
        var rows = commodityTypeDataGrid.datagrid('getSelections');
        id = rows[0].id;
    } else {
        commodityTypeDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
    }
    parent.$.modalDialog({
        title : '编辑',
        width : 700,
        height : 600,
        href :  '${path}/commodityType/editPage?id=' + id,
        buttons : [ {
            text : '确定',
            handler : function() {
                parent.$.modalDialog.openner_dataGrid = commodityTypeDataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
                var f = parent.$.modalDialog.handler.find('#commodityTypeEditForm');
                f.submit();
            }
        } ]
    });
}


/**
 * 删除
 */
 function commodityTypeDeleteFun(id) {
     if (id == undefined) {//点击右键菜单才会触发这个
         var rows = commodityTypeDataGrid.datagrid('getSelections');
         id = rows[0].id;
     } else {//点击操作里面的删除图标会触发这个
         commodityTypeDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
     }
     parent.$.messager.confirm('询问', '您是否要删除当前角色？', function(b) {
         if (b) {
             progressLoad();
             $.post('${path}/commodityType/delete', {
                 id : id
             }, function(result) {
                 if (result.success) {
                     parent.$.messager.alert('提示', result.msg, 'info');
                     commodityTypeDataGrid.datagrid('reload');
                 }
                 progressClose();
             }, 'JSON');
         }
     });
}


/**
 * 清除
 */
function commodityTypeCleanFun() {
    $('#commodityTypeSearchForm input').val('');
    commodityTypeDataGrid.datagrid('load', {});
}
/**
 * 搜索
 */
function commodityTypeSearchFun() {
     commodityTypeDataGrid.datagrid('load', $.serializeObject($('#commodityTypeSearchForm')));
}
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="commodityTypeSearchForm">
            <table>
                <tr>
                    <th>类型名称:</th>
                    <td><input name="typeName" placeholder="搜索类型名称"/></td>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="commodityTypeSearchFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'fi-x-circle',plain:true" onclick="commodityTypeCleanFun();">清空</a>
                    </td>
                </tr>
            </table>
        </form>
     </div>
 
    <div data-options="region:'center',border:false">
        <table id="commodityTypeDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>
<div id="commodityTypeToolbar" style="display: none;">
    <shiro:hasPermission name="/commodityType/add">
        <a onclick="commodityTypeAddFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'fi-page-add'">添加</a>
    </shiro:hasPermission>
</div>