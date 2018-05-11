<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    var playerLuckDataGrid;
    $(function() {
        playerLuckDataGrid = $('#playerLuckDataGrid').datagrid({
        url : '${path}/playerLuck/dataGrid',
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
            title : '明星号',
            field : 'startNo',
            sortable : true
        }, {
            width : '90',
            title : '昵称',
            field : 'nick',
            sortable : true
        }, {
            width : '70',
            title : '幸运值',
            field : 'luck',
            sortable : true
        }, {
            width : '70',
            title : '总次数',
            field : 'luckCount',
            sortable : true
        }, {
            width : '70',
            title : '剩余次数',
            field : 'luckRemain',
            sortable : true
        }, {
            width : '140',
            title : '幸运开始时间',
            field : 'luckStart',
            sortable : true
        }, {
            width : '140',
            title : '幸运结束时间',
            field : 'luckEnd',
            sortable : true
        }, {
            width : '70',
            title : 'GM操作人',
            field : 'userName',
            sortable : true
        }, {
            width : '140',
            title : '创建时间',
            field : 'createTime',
            sortable : true
        }, {
            field : 'action',
            title : '操作',
            width : 200,
            formatter : function(value, row, index) {
                var str = '';
                <shiro:hasPermission name="/playerLuck/edit">
                    str += $.formatString('<a href="javascript:void(0)" class="playerLuck-easyui-linkbutton-edit" data-options="plain:true,iconCls:\'fi-pencil icon-blue\'" onclick="playerLuckEditFun(\'{0}\');" >编辑</a>', row.id);
                </shiro:hasPermission>
                <shiro:hasPermission name="/playerLuck/delete">
                    str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
                    str += $.formatString('<a href="javascript:void(0)" class="playerLuck-easyui-linkbutton-del" data-options="plain:true,iconCls:\'fi-x icon-red\'" onclick="playerLuckDeleteFun(\'{0}\');" >删除</a>', row.id);
                </shiro:hasPermission>
                return str;
            }
        } ] ],
        onLoadSuccess:function(data){
            $('.playerLuck-easyui-linkbutton-edit').linkbutton({text:'编辑'});
            $('.playerLuck-easyui-linkbutton-del').linkbutton({text:'删除'});
        },
        toolbar : '#playerLuckToolbar'
    });
});

/**
 * 添加框
 * @param url
 */
function playerLuckAddFun() {
    parent.$.modalDialog({
        title : '添加',
        width : 700,
        height : 600,
        href : '${path}/playerLuck/addPage',
        buttons : [ {
            text : '确定',
            handler : function() {
                parent.$.modalDialog.openner_dataGrid = playerLuckDataGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
                var f = parent.$.modalDialog.handler.find('#playerLuckAddForm');
                f.submit();
            }
        } ]
    });
}


/**
 * 编辑
 */
function playerLuckEditFun(id) {
    if (id == undefined) {
        var rows = playerLuckDataGrid.datagrid('getSelections');
        id = rows[0].id;
    } else {
        playerLuckDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
    }
    parent.$.modalDialog({
        title : '编辑',
        width : 700,
        height : 600,
        href :  '${path}/playerLuck/editPage?id=' + id,
        buttons : [ {
            text : '确定',
            handler : function() {
                parent.$.modalDialog.openner_dataGrid = playerLuckDataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
                var f = parent.$.modalDialog.handler.find('#playerLuckEditForm');
                f.submit();
            }
        } ]
    });
}


/**
 * 删除
 */
 function playerLuckDeleteFun(id) {
     if (id == undefined) {//点击右键菜单才会触发这个
         var rows = playerLuckDataGrid.datagrid('getSelections');
         id = rows[0].id;
     } else {//点击操作里面的删除图标会触发这个
         playerLuckDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
     }
     parent.$.messager.confirm('询问', '您是否要删除当前角色？', function(b) {
         if (b) {
             progressLoad();
             $.post('${path}/playerLuck/delete', {
                 id : id
             }, function(result) {
                 if (result.success) {
                	 showMsg(result.msg);
                     playerLuckDataGrid.datagrid('reload');
                 }
                 progressClose();
             }, 'JSON');
         }
     });
}


/**
 * 清除
 */
function playerLuckCleanFun() {
    $('#playerLuckSearchForm input').val('');
    playerLuckDataGrid.datagrid('load', {});
}
/**
 * 搜索
 */
function playerLuckSearchFun() {
     playerLuckDataGrid.datagrid('load', $.serializeObject($('#playerLuckSearchForm')));
}
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="playerLuckSearchForm">
            <table>
                <tr>
                    <th>名称:</th>
                    <td><input name="name" placeholder="搜索条件"/></td>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="playerLuckSearchFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'fi-x-circle',plain:true" onclick="playerLuckCleanFun();">清空</a>
                    </td>
                </tr>
            </table>
        </form>
     </div>
 
    <div data-options="region:'center',border:false">
        <table id="playerLuckDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>
<div id="playerLuckToolbar" style="display: none;">
    <shiro:hasPermission name="/playerLuck/add">
        <a onclick="playerLuckAddFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'fi-page-add'">添加</a>
    </shiro:hasPermission>
</div>