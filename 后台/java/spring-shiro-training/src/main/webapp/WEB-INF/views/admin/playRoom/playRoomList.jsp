<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    var playRoomDataGrid;
    $(function() {
        playRoomDataGrid = $('#playRoomDataGrid').datagrid({
        url : '${path}/playRoom/dataGrid',
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
            width : '50',
            title : '编号',
            field : 'id',
            sortable : true
        }, {
            width : '50',
            title : '玩家id',
            field : 'playId',
            sortable : true
        }, {
            width : '50',
            title : '房间id',
            field : 'roomId',
            sortable : true
        }, {
            width : '50',
            title : '房主id',
            field : 'houseOwner',
            sortable : true
        }, {
            width : '60',
            title : '房主昵称',
            field : 'ownerName',
            sortable : true
        }, {
            width : '125',
            title : '加入时间',
            field : 'joinTime',
            sortable : true
        }, {
            width : '125',
            title : '房间开始时间',
            field : 'roomCreateTime',
            sortable : true
        }, {
            width : '125',
            title : '房间结束时间',
            field : 'roomDisappaearTime',
            sortable : true
        }, {
            width : '80',
            title : '房间游戏局数',
            field : 'roomPartyNum',
            sortable : true
        }, {
            width : '80',
            title : '玩家参与局数',
            field : 'joinPartyNum',
            sortable : true
        }, {
            width : '60',
            title : '申请筹码',
            field : 'jettonNum',
            sortable : true
        }, {
            width : '60',
            title : '房间流水',
            field : 'financialWater',
            sortable : true
        }, {
            width : '60',
            title : '玩家流水',
            field : 'playerFinancialWater',
            sortable : true
        }, {
            width : '60',
            title : '用户盈亏',
            field : 'profitNum',
            sortable : true
        }, {
            width : '60',
            title : '房间保险',
            field : 'insuranceWater',
            sortable : true
        }, {
            width : '60',
            title : '玩家保险',
            field : 'playerInsuranceWater',
            sortable : true
        }, {
            width : '60',
            title : '保险盈利',
            field : 'insuranceProfitNum',
            sortable : true
        } ] ],
        toolbar : '#playRoomToolbar'
    });
});

/**
 * 添加框
 * @param url
 */
function playRoomAddFun() {
    parent.$.modalDialog({
        title : '添加',
        width : 700,
        height : 600,
        href : '${path}/playRoom/addPage',
        buttons : [ {
            text : '确定',
            handler : function() {
                parent.$.modalDialog.openner_dataGrid = playRoomDataGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
                var f = parent.$.modalDialog.handler.find('#playRoomAddForm');
                f.submit();
            }
        } ]
    });
}


/**
 * 编辑
 */
function playRoomEditFun(id) {
    if (id == undefined) {
        var rows = playRoomDataGrid.datagrid('getSelections');
        id = rows[0].id;
    } else {
        playRoomDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
    }
    parent.$.modalDialog({
        title : '编辑',
        width : 700,
        height : 600,
        href :  '${path}/playRoom/editPage?id=' + id,
        buttons : [ {
            text : '确定',
            handler : function() {
                parent.$.modalDialog.openner_dataGrid = playRoomDataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
                var f = parent.$.modalDialog.handler.find('#playRoomEditForm');
                f.submit();
            }
        } ]
    });
}


/**
 * 删除
 */
 function playRoomDeleteFun(id) {
     if (id == undefined) {//点击右键菜单才会触发这个
         var rows = playRoomDataGrid.datagrid('getSelections');
         id = rows[0].id;
     } else {//点击操作里面的删除图标会触发这个
         playRoomDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
     }
     parent.$.messager.confirm('询问', '您是否要删除当前角色？', function(b) {
         if (b) {
             progressLoad();
             $.post('${path}/playRoom/delete', {
                 id : id
             }, function(result) {
                 if (result.success) {
                     parent.$.messager.alert('提示', result.msg, 'info');
                     playRoomDataGrid.datagrid('reload');
                 }
                 progressClose();
             }, 'JSON');
         }
     });
}


/**
 * 清除
 */
function playRoomCleanFun() {
    $('#playRoomSearchForm input').val('');
    playRoomDataGrid.datagrid('load', {});
}
/**
 * 搜索
 */
function playRoomSearchFun() {
     playRoomDataGrid.datagrid('load', $.serializeObject($('#playRoomSearchForm')));
}
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="playRoomSearchForm">
            <table>
                <tr>
                    <th>名称:</th>
                    <td><input name="playId" placeholder="玩家id"/></td>
                    <td><input name="roomId" placeholder="房间id"/></td>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="playRoomSearchFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'fi-x-circle',plain:true" onclick="playRoomCleanFun();">清空</a>
                    </td>
                </tr>
            </table>
        </form>
     </div>
 
    <div data-options="region:'center',border:false">
        <table id="playRoomDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>
<div id="playRoomToolbar" style="display: none;">
    <shiro:hasPermission name="/playRoom/add">
        <a onclick="playRoomAddFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'fi-page-add'">添加</a>
    </shiro:hasPermission>
</div>