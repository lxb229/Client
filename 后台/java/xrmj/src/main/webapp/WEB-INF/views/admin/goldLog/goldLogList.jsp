<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    var goldLogDataGrid;
    $(function() {
        goldLogDataGrid = $('#goldLogDataGrid').datagrid({
        url : '${path}/goldLog/dataGrid',
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
            width : '70',
            title : '明星号',
            field : 'startNo',
            sortable : true
        }, {
            width : '80',
            title : '玩家昵称',
            field : 'playerNick',
            sortable : true
        }, {
            width : '80',
            title : '消耗数量',
            field : 'consume',
            sortable : true
        }, {
            width : '60',
            title : '商品名称',
            field : 'commodityName',
            sortable : true
        }, {
            width : '60',
            title : '发货状态',
            field : 'status',
            sortable : true,
            formatter : function(value, row, index) {
                switch (value) {
                case 0:
                    return '待发货';
                case 1:
                    return '已发货';
                }
            }
        }, {
            width : '80',
            title : '订单人姓名',
            field : 'playerName',
            sortable : true
        }, {
            width : '80',
            title : '订单人电话',
            field : 'playerPhone',
            sortable : true
        }, {
            width : '80',
            title : '订单人地址',
            field : 'playerAddress',
            sortable : true
        }, {
            width : '100',
            title : '快递公司',
            field : 'express',
            sortable : true
        }, {
            width : '100',
            title : '快递单号',
            field : 'expressCode',
            sortable : true
        }, {
            width : '120',
            title : '兑换时间',
            field : 'createTime',
            sortable : true
        }, {
            width : '80',
            title : '发货人',
            field : 'disposeName',
            sortable : true
        }, {
            width : '120',
            title : '发货时间',
            field : 'disposeTime',
            sortable : true
        }, {
            field : 'action',
            title : '操作',
            width : 100,
            formatter : function(value, row, index) {
                var str = '';
                <shiro:hasPermission name="/goldLog/edit">
                    str += $.formatString('<a href="javascript:void(0)" class="goldLog-easyui-linkbutton-edit" data-options="plain:true,iconCls:\'fi-pencil icon-blue\'" onclick="goldLogEditFun(\'{0}\');" >编辑</a>', row.id);
                </shiro:hasPermission>
                <shiro:hasPermission name="/goldLog/delete">
                    str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
                    str += $.formatString('<a href="javascript:void(0)" class="goldLog-easyui-linkbutton-del" data-options="plain:true,iconCls:\'fi-x icon-red\'" onclick="goldLogDeleteFun(\'{0}\');" >删除</a>', row.id);
                </shiro:hasPermission>
                return str;
            }
        } ] ],
        onLoadSuccess:function(data){
            $('.goldLog-easyui-linkbutton-edit').linkbutton({text:'编辑'});
            $('.goldLog-easyui-linkbutton-del').linkbutton({text:'删除'});
        },
        toolbar : '#goldLogToolbar'
    });
});

/**
 * 添加框
 * @param url
 */
function goldLogAddFun() {
    parent.$.modalDialog({
        title : '添加',
        width : 700,
        height : 600,
        href : '${path}/goldLog/addPage',
        buttons : [ {
            text : '确定',
            handler : function() {
                parent.$.modalDialog.openner_dataGrid = goldLogDataGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
                var f = parent.$.modalDialog.handler.find('#goldLogAddForm');
                f.submit();
            }
        } ]
    });
}


/**
 * 编辑
 */
function goldLogEditFun(id) {
    if (id == undefined) {
        var rows = goldLogDataGrid.datagrid('getSelections');
        id = rows[0].id;
    } else {
        goldLogDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
    }
    parent.$.modalDialog({
        title : '编辑',
        width : 700,
        height : 600,
        href :  '${path}/goldLog/editPage?id=' + id,
        buttons : [ {
            text : '确定',
            handler : function() {
                parent.$.modalDialog.openner_dataGrid = goldLogDataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
                var f = parent.$.modalDialog.handler.find('#goldLogEditForm');
                f.submit();
            }
        } ]
    });
}


/**
 * 删除
 */
 function goldLogDeleteFun(id) {
     if (id == undefined) {//点击右键菜单才会触发这个
         var rows = goldLogDataGrid.datagrid('getSelections');
         id = rows[0].id;
     } else {//点击操作里面的删除图标会触发这个
         goldLogDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
     }
     parent.$.messager.confirm('询问', '您是否要删除当前角色？', function(b) {
         if (b) {
             progressLoad();
             $.post('${path}/goldLog/delete', {
                 id : id
             }, function(result) {
                 if (result.success) {
                     parent.$.messager.alert('提示', result.msg, 'info');
                     goldLogDataGrid.datagrid('reload');
                 }
                 progressClose();
             }, 'JSON');
         }
     });
}


/**
 * 清除
 */
function goldLogCleanFun() {
    $('#goldLogSearchForm input').val('');
    goldLogDataGrid.datagrid('load', {});
}
/**
 * 搜索
 */
function goldLogSearchFun() {
     goldLogDataGrid.datagrid('load', $.serializeObject($('#goldLogSearchForm')));
}
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="goldLogSearchForm">
            <table>
                <tr>
                    <th>明星号:</th>
                    <td><input name="startNo" placeholder="搜索明星号"/></td>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="goldLogSearchFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'fi-x-circle',plain:true" onclick="goldLogCleanFun();">清空</a>
                    </td>
                </tr>
            </table>
        </form>
     </div>
 
    <div data-options="region:'center',border:false">
        <table id="goldLogDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>
<div id="goldLogToolbar" style="display: none;">
    <shiro:hasPermission name="/goldLog/add">
        <a onclick="goldLogAddFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'fi-page-add'">添加</a>
    </shiro:hasPermission>
</div>