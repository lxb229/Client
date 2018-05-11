<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    var propLogDataGrid;
    $(function() {
        propLogDataGrid = $('#propLogDataGrid').datagrid({
        url : '${path}/propLog/dataGrid',
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
            title : '玩家明星号',
            field : 'startNo',
            sortable : true
        }, {
            width : '140',
            title : '玩家昵称',
            field : 'nick',
            sortable : true
        }, {
            width : '100',
            title : '货币类型',
            field : 'moneyType',
            sortable : true,
            formatter : function(value, row, index) {
                switch (value) {
                case 1:
                    return '房卡';
                case 2:
                    return '金币';
                case 3:
                    return '银币';
                }
            }
        }, {
            width : '80',
            title : '数量',
            field : 'amount',
            sortable : true
        }, {
            width : '100',
            title : '日志类型',
            field : 'logType',
            sortable : true,
            formatter : function(value, row, index) {
                switch (value) {
                case 1:
                    return '房卡购买';
                case 2:
                    return '房卡游戏消耗';
                case 3:
                    return '红包领取';
                case 4:
                    return '刷新抽奖';
                case 5:
                    return '银币抽奖';
                case 6:
                    return '金币兑换';
                }
            }
        }, {
            width : '380',
            title : '详细说明',
            field : 'content',
            sortable : true
        }, {
            width : '140',
            title : '时间',
            field : 'createTime',
            sortable : true
        } ] ],
        onLoadSuccess:function(data){
            $('.propLog-easyui-linkbutton-edit').linkbutton({text:'编辑'});
            $('.propLog-easyui-linkbutton-del').linkbutton({text:'删除'});
        },
        toolbar : '#propLogToolbar'
    });
});

/**
 * 添加框
 * @param url
 */
function propLogAddFun() {
    parent.$.modalDialog({
        title : '添加',
        width : 700,
        height : 600,
        href : '${path}/propLog/addPage',
        buttons : [ {
            text : '确定',
            handler : function() {
                parent.$.modalDialog.openner_dataGrid = propLogDataGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
                var f = parent.$.modalDialog.handler.find('#propLogAddForm');
                f.submit();
            }
        } ]
    });
}


/**
 * 编辑
 */
function propLogEditFun(id) {
    if (id == undefined) {
        var rows = propLogDataGrid.datagrid('getSelections');
        id = rows[0].id;
    } else {
        propLogDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
    }
    parent.$.modalDialog({
        title : '编辑',
        width : 700,
        height : 600,
        href :  '${path}/propLog/editPage?id=' + id,
        buttons : [ {
            text : '确定',
            handler : function() {
                parent.$.modalDialog.openner_dataGrid = propLogDataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
                var f = parent.$.modalDialog.handler.find('#propLogEditForm');
                f.submit();
            }
        } ]
    });
}


/**
 * 删除
 */
 function propLogDeleteFun(id) {
     if (id == undefined) {//点击右键菜单才会触发这个
         var rows = propLogDataGrid.datagrid('getSelections');
         id = rows[0].id;
     } else {//点击操作里面的删除图标会触发这个
         propLogDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
     }
     parent.$.messager.confirm('询问', '您是否要删除当前角色？', function(b) {
         if (b) {
             progressLoad();
             $.post('${path}/propLog/delete', {
                 id : id
             }, function(result) {
                 if (result.success) {
                     parent.$.messager.alert('提示', result.msg, 'info');
                     propLogDataGrid.datagrid('reload');
                 }
                 progressClose();
             }, 'JSON');
         }
     });
}


/**
 * 清除
 */
function propLogCleanFun() {
    $('#propLogSearchForm input').val('');
    propLogDataGrid.datagrid('load', {});
}
/**
 * 搜索
 */
function propLogSearchFun() {
     propLogDataGrid.datagrid('load', $.serializeObject($('#propLogSearchForm')));
}
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="propLogSearchForm">
            <table>
                <tr>
                    <th>玩家明星号:</th>
                    <td><input name="startNo" placeholder="搜索条件"/></td>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="propLogSearchFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'fi-x-circle',plain:true" onclick="propLogCleanFun();">清空</a>
                    </td>
                </tr>
            </table>
        </form>
     </div>
 
    <div data-options="region:'center',border:false">
        <table id="propLogDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>
<div id="propLogToolbar" style="display: none;">
    <shiro:hasPermission name="/propLog/add">
        <a onclick="propLogAddFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'fi-page-add'">添加</a>
    </shiro:hasPermission>
</div>