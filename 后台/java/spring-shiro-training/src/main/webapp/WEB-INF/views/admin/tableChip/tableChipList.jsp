<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    var tableChipDataGrid;
    $(function() {
        tableChipDataGrid = $('#tableChipDataGrid').datagrid({
        url : '${path}/tableChip/dataGrid',
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
            sortable : false
        }, {
            width : '140',
            title : '小盲',
            field : 'small',
            sortable : false
        }, {
            width : '140',
            title : '大盲',
            field : 'big',
            sortable : false
        }, {
            width : '140',
            title : '最小加入',
            field : 'join',
            sortable : false
        }, {
            field : 'action',
            title : '操作',
            width : 200,
            formatter : function(value, row, index) {
                var str = '';
                <shiro:hasPermission name="/tableChip/edit">
                    str += $.formatString('<a href="javascript:void(0)" class="tableChip-easyui-linkbutton-edit" data-options="plain:true,iconCls:\'fi-pencil icon-blue\'" onclick="tableChipEditFun(\'{0}\');" >编辑</a>', row.id);
                </shiro:hasPermission>
                <shiro:hasPermission name="/tableChip/delete">
                    str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
                    str += $.formatString('<a href="javascript:void(0)" class="tableChip-easyui-linkbutton-del" data-options="plain:true,iconCls:\'fi-x icon-red\'" onclick="tableChipDeleteFun(\'{0}\');" >删除</a>', row.id);
                </shiro:hasPermission>
                return str;
            }
        } ] ],
        onLoadSuccess:function(data){
            $('.tableChip-easyui-linkbutton-edit').linkbutton({text:'编辑'});
            $('.tableChip-easyui-linkbutton-del').linkbutton({text:'删除'});
        },
        toolbar : '#tableChipToolbar'
    });
});

/**
 * 添加框
 * @param url
 */
function tableChipAddFun() {
    parent.$.modalDialog({
        title : '添加',
        width : 700,
        height : 600,
        href : '${path}/tableChip/addPage',
        buttons : [ {
            text : '确定',
            handler : function() {
                parent.$.modalDialog.openner_dataGrid = tableChipDataGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
                var f = parent.$.modalDialog.handler.find('#tableChipAddForm');
                f.submit();
            }
        } ]
    });
}


/**
 * 编辑
 */
function tableChipEditFun(id) {
    if (id == undefined) {
        var rows = tableChipDataGrid.datagrid('getSelections');
        id = rows[0].id;
    } else {
        tableChipDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
    }
    parent.$.modalDialog({
        title : '编辑',
        width : 700,
        height : 600,
        href :  '${path}/tableChip/editPage?id=' + id,
        buttons : [ {
            text : '确定',
            handler : function() {
                parent.$.modalDialog.openner_dataGrid = tableChipDataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
                var f = parent.$.modalDialog.handler.find('#tableChipEditForm');
                f.submit();
            }
        } ]
    });
}


/**
 * 删除
 */
 function tableChipDeleteFun(id) {
     if (id == undefined) {//点击右键菜单才会触发这个
         var rows = tableChipDataGrid.datagrid('getSelections');
         id = rows[0].id;
     } else {//点击操作里面的删除图标会触发这个
         tableChipDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
     }
     parent.$.messager.confirm('询问', '您是否要删除当前筹码配置？', function(b) {
         if (b) {
             progressLoad();
             $.post('${path}/tableChip/delete', {
                 id : id
             }, function(result) {
                 if (result.success) {
                     parent.$.messager.alert('提示', result.msg, 'info');
                     tableChipDataGrid.datagrid('reload');
                 }
                 progressClose();
             }, 'JSON');
         }
     });
}


/**
 * 清除
 */
function tableChipCleanFun() {
    $('#tableChipSearchForm input').val('');
    tableChipDataGrid.datagrid('load', {});
}
/**
 * 搜索
 */
function tableChipSearchFun() {
     tableChipDataGrid.datagrid('load', $.serializeObject($('#tableChipSearchForm')));
}
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="tableChipSearchForm">
            <table>
                <tr>
                    <th>名称:</th>
                    <td><input name="name" placeholder="搜索条件"/></td>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="tableChipSearchFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'fi-x-circle',plain:true" onclick="tableChipCleanFun();">清空</a>
                    </td>
                </tr>
            </table>
        </form>
     </div>
 
    <div data-options="region:'center',border:false">
        <table id="tableChipDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>
<div id="tableChipToolbar" style="display: none;">
    <shiro:hasPermission name="/tableChip/add">
        <a onclick="tableChipAddFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'fi-page-add'">添加</a>
    </shiro:hasPermission>
</div>