<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    var silverLogDataGrid;
    $(function() {
        silverLogDataGrid = $('#silverLogDataGrid').datagrid({
        url : '${path}/silverLog/dataGrid',
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
            width : '80',
            title : '明星号',
            field : 'startNo',
            sortable : true
        }, {
            width : '100',
            title : '玩家昵称',
            field : 'playerNick',
            sortable : true
        }, {
            width : '110',
            title : '商品名称',
            field : 'commodityName',
            sortable : true
        }, {
            width : '90',
            title : '抽奖消耗',
            field : 'consume',
            sortable : true
        }, {
            width : '80',
            title : '奖品等级',
            field : 'awardName',
            sortable : true
        }, {
            width : '140',
            title : '出库单',
            field : 'warehouseOutNo',
            sortable : true
        }, {
            width : '140',
            title : '抽奖时间',
            field : 'createTime',
            sortable : true
        }] ],
        onLoadSuccess:function(data){
            $('.silverLog-easyui-linkbutton-edit').linkbutton({text:'编辑'});
            $('.silverLog-easyui-linkbutton-del').linkbutton({text:'删除'});
        },
        toolbar : '#silverLogToolbar'
    });
});

/**
 * 添加框
 * @param url
 */
function silverLogAddFun() {
    parent.$.modalDialog({
        title : '添加',
        width : 700,
        height : 600,
        href : '${path}/silverLog/addPage',
        buttons : [ {
            text : '确定',
            handler : function() {
                parent.$.modalDialog.openner_dataGrid = silverLogDataGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
                var f = parent.$.modalDialog.handler.find('#silverLogAddForm');
                f.submit();
            }
        } ]
    });
}


/**
 * 编辑
 */
function silverLogEditFun(id) {
    if (id == undefined) {
        var rows = silverLogDataGrid.datagrid('getSelections');
        id = rows[0].id;
    } else {
        silverLogDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
    }
    parent.$.modalDialog({
        title : '编辑',
        width : 700,
        height : 600,
        href :  '${path}/silverLog/editPage?id=' + id,
        buttons : [ {
            text : '确定',
            handler : function() {
                parent.$.modalDialog.openner_dataGrid = silverLogDataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
                var f = parent.$.modalDialog.handler.find('#silverLogEditForm');
                f.submit();
            }
        } ]
    });
}


/**
 * 删除
 */
 function silverLogDeleteFun(id) {
     if (id == undefined) {//点击右键菜单才会触发这个
         var rows = silverLogDataGrid.datagrid('getSelections');
         id = rows[0].id;
     } else {//点击操作里面的删除图标会触发这个
         silverLogDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
     }
     parent.$.messager.confirm('询问', '您是否要删除当前角色？', function(b) {
         if (b) {
             progressLoad();
             $.post('${path}/silverLog/delete', {
                 id : id
             }, function(result) {
                 if (result.success) {
                     parent.$.messager.alert('提示', result.msg, 'info');
                     silverLogDataGrid.datagrid('reload');
                 }
                 progressClose();
             }, 'JSON');
         }
     });
}


/**
 * 清除
 */
function silverLogCleanFun() {
    $('#silverLogSearchForm input').val('');
    silverLogDataGrid.datagrid('load', {});
}
/**
 * 搜索
 */
function silverLogSearchFun() {
     silverLogDataGrid.datagrid('load', $.serializeObject($('#silverLogSearchForm')));
}
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="silverLogSearchForm">
            <table>
                <tr>
                    <th>明星号:</th>
                    <td><input name="startNo" placeholder="搜索明星号"/></td>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="silverLogSearchFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'fi-x-circle',plain:true" onclick="silverLogCleanFun();">清空</a>
                    </td>
                </tr>
            </table>
        </form>
     </div>
 
    <div data-options="region:'center',border:false">
        <table id="silverLogDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>
<div id="silverLogToolbar" style="display: none;">
    <shiro:hasPermission name="/silverLog/add">
        <a onclick="silverLogAddFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'fi-page-add'">添加</a>
    </shiro:hasPermission>
</div>