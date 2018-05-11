<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    var playerWishDataGrid;
    $(function() {
        playerWishDataGrid = $('#playerWishDataGrid').datagrid({
        url : '${path}/playerWish/dataGrid',
        striped : true,
        rownumbers : true,
        pagination : true,
        singleSelect : true,
        idField : 'startNo',
        sortName : 'startNo',
        sortOrder : 'asc',
        pageSize : 20,
        pageList : [ 10, 20, 30, 40, 50, 100, 200, 300, 400, 500],
        frozenColumns : [ [ {
            width : '100',
            title : '玩家明星号',
            field : 'startNo',
            sortable : true
        }, {
            width : '140',
            title : '昵称',
            field : 'nick',
            sortable : true
        }, {
            width : '50',
            title : '当前祝福值',
            field : 'wish',
            sortable : true
        }, {
            width : '50',
            title : '当前房卡',
            field : 'roomCard',
            sortable : true
        }, {
            width : '100',
            title : '总充值金额',
            field : 'payAmount',
            sortable : true
        }, {
            width : '80',
            title : 'APP内购',
            field : 'appPay',
            sortable : true
        }, {
            width : '80',
            title : '线下购买',
            field : 'offlinePay',
            sortable : true
        }, {
            width : '80',
            title : 'web购买',
            field : 'webPay',
            sortable : true
        }, {
            width : '80',
            title : '公众号购买',
            field : 'wechatPay',
            sortable : true
        }, {
            width : '80',
            title : '已消耗房卡',
            field : 'useRoomCard',
            sortable : true
        }, {
            width : '80',
            title : '当前金币',
            field : 'goldCoin',
            sortable : true
        }, {
            width : '80',
            title : '当前银币',
            field : 'silverCoin',
            sortable : true
        }, {
            width : '80',
            title : '总金币',
            field : 'allGold',
            sortable : true
        }, {
            width : '80',
            title : '总银币',
            field : 'allSilver',
            sortable : true
        } ] ],
        onLoadSuccess:function(data){
            $('.playerWish-easyui-linkbutton-edit').linkbutton({text:'编辑'});
            $('.playerWish-easyui-linkbutton-del').linkbutton({text:'删除'});
        },
        toolbar : '#playerWishToolbar'
    });
});

/**
 * 添加框
 * @param url
 */
function playerWishAddFun() {
    parent.$.modalDialog({
        title : '添加',
        width : 700,
        height : 600,
        href : '${path}/playerWish/addPage',
        buttons : [ {
            text : '确定',
            handler : function() {
                parent.$.modalDialog.openner_dataGrid = playerWishDataGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
                var f = parent.$.modalDialog.handler.find('#playerWishAddForm');
                f.submit();
            }
        } ]
    });
}


/**
 * 编辑
 */
function playerWishEditFun(id) {
    if (id == undefined) {
        var rows = playerWishDataGrid.datagrid('getSelections');
        id = rows[0].id;
    } else {
        playerWishDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
    }
    parent.$.modalDialog({
        title : '编辑',
        width : 700,
        height : 600,
        href :  '${path}/playerWish/editPage?id=' + id,
        buttons : [ {
            text : '确定',
            handler : function() {
                parent.$.modalDialog.openner_dataGrid = playerWishDataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
                var f = parent.$.modalDialog.handler.find('#playerWishEditForm');
                f.submit();
            }
        } ]
    });
}


/**
 * 删除
 */
 function playerWishDeleteFun(id) {
     if (id == undefined) {//点击右键菜单才会触发这个
         var rows = playerWishDataGrid.datagrid('getSelections');
         id = rows[0].id;
     } else {//点击操作里面的删除图标会触发这个
         playerWishDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
     }
     parent.$.messager.confirm('询问', '您是否要删除当前角色？', function(b) {
         if (b) {
             progressLoad();
             $.post('${path}/playerWish/delete', {
                 id : id
             }, function(result) {
                 if (result.success) {
                     parent.$.messager.alert('提示', result.msg, 'info');
                     playerWishDataGrid.datagrid('reload');
                 }
                 progressClose();
             }, 'JSON');
         }
     });
}


/**
 * 清除
 */
function playerWishCleanFun() {
    $('#playerWishSearchForm input').val('');
    playerWishDataGrid.datagrid('load', {});
}
/**
 * 搜索
 */
function playerWishSearchFun() {
     playerWishDataGrid.datagrid('load', $.serializeObject($('#playerWishSearchForm')));
}
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="playerWishSearchForm">
            <table>
                <tr>
                    <th>名称:</th>
                    <td><input name="startNo" placeholder="搜索条件"/></td>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="playerWishSearchFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'fi-x-circle',plain:true" onclick="playerWishCleanFun();">清空</a>
                    </td>
                </tr>
            </table>
        </form>
     </div>
 
    <div data-options="region:'center',border:false">
        <table id="playerWishDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>
<div id="playerWishToolbar" style="display: none;">
    <shiro:hasPermission name="/playerWish/add">
        <a onclick="playerWishAddFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'fi-page-add'">添加</a>
    </shiro:hasPermission>
</div>