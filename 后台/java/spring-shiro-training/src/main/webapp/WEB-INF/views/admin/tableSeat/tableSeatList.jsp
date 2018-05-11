<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    var tableSeatDataGrid;
    $(function() {
        tableSeatDataGrid = $('#tableSeatDataGrid').datagrid({
        url : '${path}/tableSeat/dataGrid',
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
            title : '昵称',
            field : 'nick',
            sortable : true
        }, {
            width : '140',
            title : '手牌',
            field : 'cards',
            sortable : true
        } ] ],
        onLoadSuccess:function(data){
            $('.tableSeat-easyui-linkbutton-edit').linkbutton({text:'编辑'});
            $('.tableSeat-easyui-linkbutton-del').linkbutton({text:'删除'});
        },
        toolbar : '#tableSeatToolbar'
    });
});

/**
 * 添加框
 * @param url
 */
function tableSeatAddFun() {
    parent.$.modalDialog({
        title : '添加',
        width : 700,
        height : 600,
        href : '${path}/tableSeat/addPage',
        buttons : [ {
            text : '确定',
            handler : function() {
                parent.$.modalDialog.openner_dataGrid = tableSeatDataGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
                var f = parent.$.modalDialog.handler.find('#tableSeatAddForm');
                f.submit();
            }
        } ]
    });
}


/**
 * 编辑
 */
function tableSeatEditFun(id) {
    if (id == undefined) {
        var rows = tableSeatDataGrid.datagrid('getSelections');
        id = rows[0].id;
    } else {
        tableSeatDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
    }
    parent.$.modalDialog({
        title : '编辑',
        width : 700,
        height : 600,
        href :  '${path}/tableSeat/editPage?id=' + id,
        buttons : [ {
            text : '确定',
            handler : function() {
                parent.$.modalDialog.openner_dataGrid = tableSeatDataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
                var f = parent.$.modalDialog.handler.find('#tableSeatEditForm');
                f.submit();
            }
        } ]
    });
}


/**
 * 删除
 */
 function tableSeatDeleteFun(id) {
     if (id == undefined) {//点击右键菜单才会触发这个
         var rows = tableSeatDataGrid.datagrid('getSelections');
         id = rows[0].id;
     } else {//点击操作里面的删除图标会触发这个
         tableSeatDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
     }
     parent.$.messager.confirm('询问', '您是否要删除当前角色？', function(b) {
         if (b) {
             progressLoad();
             $.post('${path}/tableSeat/delete', {
                 id : id
             }, function(result) {
                 if (result.success) {
                     parent.$.messager.alert('提示', result.msg, 'info');
                     tableSeatDataGrid.datagrid('reload');
                 }
                 progressClose();
             }, 'JSON');
         }
     });
}


/**
 * 清除
 */
function tableSeatCleanFun() {
    $('#tableSeatSearchForm input').val('');
    tableSeatDataGrid.datagrid('load', {});
}
/**
 * 搜索
 */
function tableSeatSearchFun() {
     tableSeatDataGrid.datagrid('load', $.serializeObject($('#tableSeatSearchForm')));
}
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="tableSeatSearchForm">
            <table>
                <tr>
                    <th>房间id:</th>
                    <td><input name="roomId" placeholder="搜索房间id"/></td>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="tableSeatSearchFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'fi-x-circle',plain:true" onclick="tableSeatCleanFun();">清空</a>
                    </td>
                </tr>
            </table>
        </form>
     </div>
 
    <div data-options="region:'center',border:false">
        <table id="tableSeatDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>
<div id="tableSeatToolbar" style="display: none;">
    <shiro:hasPermission name="/tableSeat/add">
        <a onclick="tableSeatAddFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'fi-page-add'">添加</a>
    </shiro:hasPermission>
</div>