<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    var roomDataGrid;
    $(function() {
        roomDataGrid = $('#roomDataGrid').datagrid({
        url : '${path}/room/dataGrid',
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
        },  {
            width : '60',
            title : '房间ID',
            field : 'roomId',
            sortable : true
        },  {
            width : '80',
            title : '房间名称',
            field : 'roomName',
            sortable : true
        },  {
            width : '120',
            title : '创建时间',
            field : 'createTime',
            sortable : true
        },  {
            width : '60',
            title : '房主ID',
            field : 'houseOwner',
            sortable : true
        },  {
            width : '70',
            title : '房主昵称',
            field : 'ownerName',
            sortable : true
        },  {
            width : '120',
            title : '解散时间',
            field : 'disappearTime',
            sortable : true
        },  {
            width : '60',
            title : '时间设置',
            field : 'timeSet',
            sortable : true
        },  {
            width : '50',
            title : '大小盲',
            field : 'sizeBlind',
            sortable : true
        },  {
            width : '60',
            title : '携入筹码',
            field : 'jettonSet',
            sortable : true
        },  {
            width : '60',
            title : '保险设置',
            field : 'insurance',
            sortable : true,
            formatter : function(value, row, index) {
                switch (value) {
                case 0:
                    return '开启';
                case 1:
                    return '未开启';
                }
            }
        },  {
            width : '50',
            title : 'straddle',
            field : 'straddle',
            sortable : true,
            formatter : function(value, row, index) {
                switch (value) {
                case 0:
                    return '开启';
                case 1:
                    return '未开启';
                }
            }
        },  {
            width : '60',
            title : '游戏局数',
            field : 'partyNum',
            sortable : true
        },  {
            width : '60',
            title : '房内流水',
            field : 'financialWater',
            sortable : true
        },  {
            width : '60',
            title : '保险流水',
            field : 'insuranceWater',
            sortable : true
        },  {
            width : '80',
            title : '进入房间人数',
            field : 'joinRoomNum',
            sortable : true
        },  {
            width : '60',
            title : '入局人数',
            field : 'joinPartyNum',
            sortable : true
        },  {
            width : '60',
            title : '筹码总数',
            field : 'jettonNum',
            sortable : true
        } ] ],
        onLoadSuccess:function(data){
            $('.room-easyui-linkbutton-edit').linkbutton({text:'编辑'});
            $('.room-easyui-linkbutton-del').linkbutton({text:'删除'});
        },
        toolbar : '#roomToolbar'
    });
});

/**
 * 添加框
 * @param url
 */
function roomAddFun() {
    parent.$.modalDialog({
        title : '添加',
        width : 700,
        height : 600,
        href : '${path}/room/addPage',
        buttons : [ {
            text : '确定',
            handler : function() {
                parent.$.modalDialog.openner_dataGrid = roomDataGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
                var f = parent.$.modalDialog.handler.find('#roomAddForm');
                f.submit();
            }
        } ]
    });
}


/**
 * 编辑
 */
function roomEditFun(id) {
    if (id == undefined) {
        var rows = roomDataGrid.datagrid('getSelections');
        id = rows[0].id;
    } else {
        roomDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
    }
    parent.$.modalDialog({
        title : '编辑',
        width : 700,
        height : 600,
        href :  '${path}/room/editPage?id=' + id,
        buttons : [ {
            text : '确定',
            handler : function() {
                parent.$.modalDialog.openner_dataGrid = roomDataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
                var f = parent.$.modalDialog.handler.find('#roomEditForm');
                f.submit();
            }
        } ]
    });
}


/**
 * 删除
 */
 function roomDeleteFun(id) {
     if (id == undefined) {//点击右键菜单才会触发这个
         var rows = roomDataGrid.datagrid('getSelections');
         id = rows[0].id;
     } else {//点击操作里面的删除图标会触发这个
         roomDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
     }
     parent.$.messager.confirm('询问', '您是否要删除当前角色？', function(b) {
         if (b) {
             progressLoad();
             $.post('${path}/room/delete', {
                 id : id
             }, function(result) {
                 if (result.success) {
                     parent.$.messager.alert('提示', result.msg, 'info');
                     roomDataGrid.datagrid('reload');
                 }
                 progressClose();
             }, 'JSON');
         }
     });
}


/**
 * 清除
 */
function roomCleanFun() {
    $('#roomSearchForm input').val('');
    roomDataGrid.datagrid('load', {});
}
/**
 * 搜索
 */
function roomSearchFun() {
     roomDataGrid.datagrid('load', $.serializeObject($('#roomSearchForm')));
}
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="roomSearchForm">
            <table>
                <tr>
                    <th>名称:</th>
                    <td><input name="name" placeholder="搜索条件"/></td>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="roomSearchFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'fi-x-circle',plain:true" onclick="roomCleanFun();">清空</a>
                    </td>
                </tr>
            </table>
        </form>
     </div>
 
    <div data-options="region:'center',border:false">
        <table id="roomDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>
<div id="roomToolbar" style="display: none;">
    <shiro:hasPermission name="/room/add">
        <a onclick="roomAddFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'fi-page-add'">添加</a>
    </shiro:hasPermission>
</div>