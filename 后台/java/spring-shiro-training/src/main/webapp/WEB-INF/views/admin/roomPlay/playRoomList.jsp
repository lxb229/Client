<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    var roomPlayDataGrid;
    $(function() {
        roomPlayDataGrid = $('#roomPlayDataGrid').datagrid({
        url : '${path}/roomPlay/dataGrid',
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
            width : '70',
            title : '玩家id',
            field : 'playId',
            sortable : true
        }, {
            width : '135',
            title : '玩家昵称',
            field : 'playNick',
            sortable : true
        }, {
            width : '70',
            title : '房间id',
            field : 'roomId',
            sortable : true
        }, {
            width : '70',
            title : '房主id',
            field : 'houseOwner',
            sortable : true
        }, {
            width : '70',
            title : '房主昵称',
            field : 'ownerName',
            sortable : true
        }, {
            width : '90',
            title : '房间游戏局数',
            field : 'roomPartyNum',
            sortable : true
        }, {
            width : '90',
            title : '玩家参与局数',
            field : 'joinPartyNum',
            sortable : true
        }, {
            width : '70',
            title : '用户盈亏',
            field : 'profitNum',
            sortable : true
        }, {
            width : '70',
            title : '保险盈利',
            field : 'insuranceProfitNum',
            sortable : true
        } ] ],
        toolbar : '#roomPlayToolbar'
    });
});

/**
 * 添加框
 * @param url
 */
function roomPlayAddFun() {
    parent.$.modalDialog({
        title : '添加',
        width : 700,
        height : 600,
        href : '${path}/roomPlay/addPage',
        buttons : [ {
            text : '确定',
            handler : function() {
                parent.$.modalDialog.openner_dataGrid = roomPlayDataGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
                var f = parent.$.modalDialog.handler.find('#roomPlayAddForm');
                f.submit();
            }
        } ]
    });
}


/**
 * 编辑
 */
function roomPlayEditFun(id) {
    if (id == undefined) {
        var rows = roomPlayDataGrid.datagrid('getSelections');
        id = rows[0].id;
    } else {
        roomPlayDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
    }
    parent.$.modalDialog({
        title : '编辑',
        width : 700,
        height : 600,
        href :  '${path}/roomPlay/editPage?id=' + id,
        buttons : [ {
            text : '确定',
            handler : function() {
                parent.$.modalDialog.openner_dataGrid = roomPlayDataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
                var f = parent.$.modalDialog.handler.find('#roomPlayEditForm');
                f.submit();
            }
        } ]
    });
}


/**
 * 删除
 */
 function roomPlayDeleteFun(id) {
     if (id == undefined) {//点击右键菜单才会触发这个
         var rows = roomPlayDataGrid.datagrid('getSelections');
         id = rows[0].id;
     } else {//点击操作里面的删除图标会触发这个
         roomPlayDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
     }
     parent.$.messager.confirm('询问', '您是否要删除当前角色？', function(b) {
         if (b) {
             progressLoad();
             $.post('${path}/roomPlay/delete', {
                 id : id
             }, function(result) {
                 if (result.success) {
                     parent.$.messager.alert('提示', result.msg, 'info');
                     roomPlayDataGrid.datagrid('reload');
                 }
                 progressClose();
             }, 'JSON');
         }
     });
}


/**
 * 清除
 */
function roomPlayCleanFun() {
    $('#roomPlaySearchForm input').val('');
    roomPlayDataGrid.datagrid('load', {});
}
/**
 * 搜索
 */
function roomPlaySearchFun() {
     roomPlayDataGrid.datagrid('load', $.serializeObject($('#roomPlaySearchForm')));
}
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="roomPlaySearchForm">
            <table>
                <tr>
                    <th>名称:</th>
                    <td><input name="playId" placeholder="玩家id"/></td>
                    <td><input name="roomId" placeholder="房间id"/></td>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="roomPlaySearchFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'fi-x-circle',plain:true" onclick="roomPlayCleanFun();">清空</a>
                    </td>
                </tr>
            </table>
        </form>
     </div>
 
    <div data-options="region:'center',border:false">
        <table id="roomPlayDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>
<div id="roomPlayToolbar" style="display: none;">
    <shiro:hasPermission name="/roomPlay/add">
        <a onclick="roomPlayAddFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'fi-page-add'">添加</a>
    </shiro:hasPermission>
</div>