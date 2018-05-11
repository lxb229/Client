<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    var playerDataGrid;
    $(function() {
        playerDataGrid = $('#playerDataGrid').datagrid({
        url : '${path}/player/dataGrid',
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
            width : '60',
            title : '明星号',
            field : 'startNo',
            sortable : true
        }, {
            width : '60',
            title : '账号状态',
            field : 'status',
            sortable : true,
            formatter : function(value, row, index) {
                switch (value) {
                case 0:
                    return '启用';
                case 1:
                    return '禁用';
                }
            }
        }, {
            width : '110',
            title : '用户昵称',
            field : 'nick',
            sortable : true
        }, {
            width : '110',
            title : '电话号码',
            field : 'phone',
            sortable : true
        }, {
            width : '110',
            title : '真实姓名',
            field : 'realName',
            sortable : true
        }, {
            width : '110',
            title : '身份证号',
            field : 'cardNo',
            sortable : true
        }, {
            field : 'action',
            title : '操作',
            width : 200,
            formatter : function(value, row, index) {
                var str = '';
                <shiro:hasPermission name="/player/edit">
                    str += $.formatString('<a href="javascript:void(0)" class="player-easyui-linkbutton-edit" data-options="plain:true,iconCls:\'fi-pencil icon-blue\'" onclick="playerEditFun(\'{0}\');" >编辑</a>', row.id);
                    str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
                </shiro:hasPermission>
                <shiro:hasPermission name="/player/delete">
                   	str += $.formatString('<a href="javascript:void(0)" class="player-easyui-linkbutton-del" data-options="plain:true,iconCls:\'fi-x icon-red\'" onclick="playerDeleteFun(\'{0}\',\'{1}\');" >禁/启用</a>', row.id,row.status);
                </shiro:hasPermission>
                return str;
            }
        } ] ],
        onLoadSuccess:function(data){
            $('.player-easyui-linkbutton-edit').linkbutton({text:'编辑'});
            $('.player-easyui-linkbutton-del').linkbutton({text:'禁/启用'});
        },
        toolbar : '#playerToolbar'
    });
});

/**
 * 添加框
 * @param url
 */
function playerAddFun() {
    parent.$.modalDialog({
        title : '添加',
        width : 700,
        height : 600,
        href : '${path}/player/addPage',
        buttons : [ {
            text : '确定',
            handler : function() {
                parent.$.modalDialog.openner_dataGrid = playerDataGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
                var f = parent.$.modalDialog.handler.find('#playerAddForm');
                f.submit();
            }
        } ]
    });
}


/**
 * 编辑
 */
function playerEditFun(id) {
    if (id == undefined) {
        var rows = playerDataGrid.datagrid('getSelections');
        id = rows[0].id;
    } else {
        playerDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
    }
    parent.$.modalDialog({
        title : '编辑',
        width : 700,
        height : 600,
        href :  '${path}/player/editPage?id=' + id,
        buttons : [ {
            text : '确定',
            handler : function() {
                parent.$.modalDialog.openner_dataGrid = playerDataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
                var f = parent.$.modalDialog.handler.find('#playerEditForm');
                f.submit();
            }
        } ]
    });
}


/**
 * 删除
 */
 function playerDeleteFun(id,status) {
     if (id == undefined) {//点击右键菜单才会触发这个
         var rows = playerDataGrid.datagrid('getSelections');
         id = rows[0].id;
     } else {//点击操作里面的删除图标会触发这个
         playerDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
     }
     var statusStr = '禁用';
     if(status == 0) {
    	 statusStr = '禁用';
    	 status = 1;
     } else {
    	 statusStr = '启用';
    	 status = 0;
     }
     parent.$.messager.confirm('询问', '您是否要'+statusStr+'当前角色？', function(b) {
         if (b) {
             progressLoad();
             $.post('${path}/player/delete', {
                 id : id,
                 status: status
             }, function(result) {
                 if (result.success) {
                     showMsg(result.msg);
                     playerDataGrid.datagrid('reload');
                 } else {
                	 showMsg(result.msg);
                 }
                 progressClose();
             }, 'JSON');
         }
     });
}


/**
 * 清除
 */
function playerCleanFun() {
    $('#playerSearchForm input').val('');
    playerDataGrid.datagrid('load', {});
}
/**
 * 搜索
 */
function playerSearchFun() {
     playerDataGrid.datagrid('load', $.serializeObject($('#playerSearchForm')));
}
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="playerSearchForm">
            <table>
                <tr>
                    <th>明星号:</th>
                    <td><input name="startNo" placeholder="搜索条件"/></td>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="playerSearchFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'fi-x-circle',plain:true" onclick="playerCleanFun();">清空</a>
                    </td>
                </tr>
            </table>
        </form>
     </div>
 
    <div data-options="region:'center',border:false">
        <table id="playerDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>
<div id="playerToolbar" style="display: none;">
    <shiro:hasPermission name="/player/add">
        <a onclick="playerAddFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'fi-page-add'">添加</a>
    </shiro:hasPermission>
</div>