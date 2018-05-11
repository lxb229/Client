<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    var redPacketLogDataGrid;
    $(function() {
        redPacketLogDataGrid = $('#redPacketLogDataGrid').datagrid({
        url : '${path}/redPacketLog/dataGrid',
        striped : true,
        rownumbers : true,
        pagination : true,
        singleSelect : true,
        idField : 'id',
        sortName : 'r.id',
        sortOrder : 'asc',
        pageSize : 20,
        pageList : [ 10, 20, 30, 40, 50, 100, 200, 300, 400, 500],
        frozenColumns : [ [ {
            width : '60',
            title : '编号',
            field : 'id',
            sortable : true
        },  {
            width : '80',
            title : '玩家明星号',
            field : 'startNo',
            sortable : true
        },  {
            width : '100',
            title : '玩家昵称',
            field : 'nick',
            sortable : true
        },  {
            width : '60',
            title : '红包号数',
            field : 'redPacketNo',
            sortable : true
        },  {
            width : '60',
            title : '抽奖次数',
            field : 'lotteryNumber',
            sortable : true
        },  {
            width : '90',
            title : '保底金额',
            field : 'minimumAmount',
            sortable : true
        },  {
            width : '90',
            title : '众筹金额',
            field : 'crowdfundingAmount',
            sortable : true
        }, {
            width : '70',
            title : '奖金类型',
            field : 'type',
            sortable : true,
            formatter : function(value, row, index) {
                switch (value) {
                case 2:
                    return '金币';
                case 3:
                    return '银币';
                }
            }
        },  {
            width : '80',
            title : '领取数量',
            field : 'amount',
            sortable : true
        },  {
            width : '90',
            title : '领取祝福值',
            field : 'wish',
            sortable : true
        },  {
            width : '100',
            title : '领取时间',
            field : 'createTime',
            sortable : true
        }] ],
        onLoadSuccess:function(data){
            $('.redPacketLog-easyui-linkbutton-edit').linkbutton({text:'编辑'});
            $('.redPacketLog-easyui-linkbutton-del').linkbutton({text:'删除'});
        },
        toolbar : '#redPacketLogToolbar'
    });
});

/**
 * 添加框
 * @param url
 */
function redPacketLogAddFun() {
    parent.$.modalDialog({
        title : '添加',
        width : 700,
        height : 600,
        href : '${path}/redPacketLog/addPage',
        buttons : [ {
            text : '确定',
            handler : function() {
                parent.$.modalDialog.openner_dataGrid = redPacketLogDataGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
                var f = parent.$.modalDialog.handler.find('#redPacketLogAddForm');
                f.submit();
            }
        } ]
    });
}


/**
 * 编辑
 */
function redPacketLogEditFun(id) {
    if (id == undefined) {
        var rows = redPacketLogDataGrid.datagrid('getSelections');
        id = rows[0].id;
    } else {
        redPacketLogDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
    }
    parent.$.modalDialog({
        title : '编辑',
        width : 700,
        height : 600,
        href :  '${path}/redPacketLog/editPage?id=' + id,
        buttons : [ {
            text : '确定',
            handler : function() {
                parent.$.modalDialog.openner_dataGrid = redPacketLogDataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
                var f = parent.$.modalDialog.handler.find('#redPacketLogEditForm');
                f.submit();
            }
        } ]
    });
}


/**
 * 删除
 */
 function redPacketLogDeleteFun(id) {
     if (id == undefined) {//点击右键菜单才会触发这个
         var rows = redPacketLogDataGrid.datagrid('getSelections');
         id = rows[0].id;
     } else {//点击操作里面的删除图标会触发这个
         redPacketLogDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
     }
     parent.$.messager.confirm('询问', '您是否要删除当前角色？', function(b) {
         if (b) {
             progressLoad();
             $.post('${path}/redPacketLog/delete', {
                 id : id
             }, function(result) {
                 if (result.success) {
                     parent.$.messager.alert('提示', result.msg, 'info');
                     redPacketLogDataGrid.datagrid('reload');
                 }
                 progressClose();
             }, 'JSON');
         }
     });
}


/**
 * 清除
 */
function redPacketLogCleanFun() {
    $('#redPacketLogSearchForm input').val('');
    redPacketLogDataGrid.datagrid('load', {});
}
/**
 * 搜索
 */
function redPacketLogSearchFun() {
     redPacketLogDataGrid.datagrid('load', $.serializeObject($('#redPacketLogSearchForm')));
}
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="redPacketLogSearchForm">
            <table>
                <tr>
                    <th>明星号:</th>
                    <td><input name="startNo" placeholder="搜索玩家明星号"/></td>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="redPacketLogSearchFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'fi-x-circle',plain:true" onclick="redPacketLogCleanFun();">清空</a>
                    </td>
                </tr>
            </table>
        </form>
     </div>
 
    <div data-options="region:'center',border:false">
        <table id="redPacketLogDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>
<div id="redPacketLogToolbar" style="display: none;">
    <shiro:hasPermission name="/redPacketLog/add">
        <a onclick="redPacketLogAddFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'fi-page-add'">添加</a>
    </shiro:hasPermission>
</div>