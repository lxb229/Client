<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    var jackpotDataGrid;
    $(function() {
        jackpotDataGrid = $('#jackpotDataGrid').datagrid({
        url : '${path}/jackpot/dataGrid',
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
            title : '每轮奖金',
            field : 'bonus',
            sortable : true
        }, {
            field : 'action',
            title : '操作',
            width : 200,
            formatter : function(value, row, index) {
                var str = '';
                <shiro:hasPermission name="/jackpot/edit">
                    str += $.formatString('<a href="javascript:void(0)" class="jackpot-easyui-linkbutton-edit" data-options="plain:true,iconCls:\'fi-pencil icon-blue\'" onclick="jackpotEditFun(\'{0}\');" >编辑</a>', row.id);
                </shiro:hasPermission>
                return str;
            }
        } ] ],
        onLoadSuccess:function(data){
            $('.jackpot-easyui-linkbutton-edit').linkbutton({text:'编辑'});
            $('.jackpot-easyui-linkbutton-del').linkbutton({text:'删除'});
        },
        toolbar : '#jackpotToolbar'
    });
});

/**
 * 添加框
 * @param url
 */
function jackpotAddFun() {
    parent.$.modalDialog({
        title : '添加',
        width : 700,
        height : 600,
        href : '${path}/jackpot/addPage',
        buttons : [ {
            text : '确定',
            handler : function() {
                parent.$.modalDialog.openner_dataGrid = jackpotDataGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
                var f = parent.$.modalDialog.handler.find('#jackpotAddForm');
                f.submit();
            }
        } ]
    });
}


/**
 * 编辑
 */
function jackpotEditFun(id) {
    if (id == undefined) {
        var rows = jackpotDataGrid.datagrid('getSelections');
        id = rows[0].id;
    } else {
        jackpotDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
    }
    parent.$.modalDialog({
        title : '编辑',
        width : 700,
        height : 600,
        href :  '${path}/jackpot/editPage?id=' + id,
        buttons : [ {
            text : '确定',
            handler : function() {
                parent.$.modalDialog.openner_dataGrid = jackpotDataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
                var f = parent.$.modalDialog.handler.find('#jackpotEditForm');
                f.submit();
            }
        } ]
    });
}


/**
 * 删除
 */
 function jackpotDeleteFun(id) {
     if (id == undefined) {//点击右键菜单才会触发这个
         var rows = jackpotDataGrid.datagrid('getSelections');
         id = rows[0].id;
     } else {//点击操作里面的删除图标会触发这个
         jackpotDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
     }
     parent.$.messager.confirm('询问', '您是否要删除当前角色？', function(b) {
         if (b) {
             progressLoad();
             $.post('${path}/jackpot/delete', {
                 id : id
             }, function(result) {
                 if (result.success) {
                     parent.$.messager.alert('提示', result.msg, 'info');
                     jackpotDataGrid.datagrid('reload');
                 }
                 progressClose();
             }, 'JSON');
         }
     });
}


/**
 * 清除
 */
function jackpotCleanFun() {
    $('#jackpotSearchForm input').val('');
    jackpotDataGrid.datagrid('load', {});
}
/**
 * 搜索
 */
function jackpotSearchFun() {
     jackpotDataGrid.datagrid('load', $.serializeObject($('#jackpotSearchForm')));
}
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="jackpotSearchForm">
            <table>
              
            </table>
        </form>
     </div>
 
    <div data-options="region:'center',border:false">
        <table id="jackpotDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>
<div id="jackpotToolbar" style="display: none;">
    <shiro:hasPermission name="/jackpot/add">
        <a onclick="jackpotAddFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'fi-page-add'">添加</a>
    </shiro:hasPermission>
</div>