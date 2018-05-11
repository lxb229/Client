<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    var silverJackpotDataGrid;
    $(function() {
        silverJackpotDataGrid = $('#silverJackpotDataGrid').datagrid({
        url : '${path}/silverJackpot/dataGrid',
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
            title : '奖项名称',
            field : 'awardName',
            sortable : true
        }, {
            width : '140',
            title : '奖项等级',
            field : 'awardLv',
            sortable : true
        }, {
            width : '140',
            title : '奖项数量',
            field : 'amount',
            sortable : true
        }, {
            width : '140',
            title : '当前剩余',
            field : 'residue',
            sortable : true
        }, {
            field : 'action',
            title : '操作',
            width : 200,
            formatter : function(value, row, index) {
                var str = '';
                <shiro:hasPermission name="/silverJackpot/edit">
                    str += $.formatString('<a href="javascript:void(0)" class="silverJackpot-easyui-linkbutton-edit" data-options="plain:true,iconCls:\'fi-pencil icon-blue\'" onclick="silverJackpotEditFun(\'{0}\');" >编辑</a>', row.id);
                </shiro:hasPermission>
                <shiro:hasPermission name="/silverJackpot/delete">
                    str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
                    str += $.formatString('<a href="javascript:void(0)" class="silverJackpot-easyui-linkbutton-del" data-options="plain:true,iconCls:\'fi-x icon-red\'" onclick="silverJackpotDeleteFun(\'{0}\');" >删除</a>', row.id);
                </shiro:hasPermission>
                return str;
            }
        } ] ],
        onLoadSuccess:function(data){
            $('.silverJackpot-easyui-linkbutton-edit').linkbutton({text:'编辑'});
            $('.silverJackpot-easyui-linkbutton-del').linkbutton({text:'删除'});
        },
        toolbar : '#silverJackpotToolbar'
    });
});

/**
 * 添加框
 * @param url
 */
function silverJackpotAddFun() {
    parent.$.modalDialog({
        title : '添加',
        width : 700,
        height : 600,
        href : '${path}/silverJackpot/addPage',
        buttons : [ {
            text : '确定',
            handler : function() {
                parent.$.modalDialog.openner_dataGrid = silverJackpotDataGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
                var f = parent.$.modalDialog.handler.find('#silverJackpotAddForm');
                f.submit();
            }
        } ]
    });
}


/**
 * 编辑
 */
function silverJackpotEditFun(id) {
    if (id == undefined) {
        var rows = silverJackpotDataGrid.datagrid('getSelections');
        id = rows[0].id;
    } else {
        silverJackpotDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
    }
    parent.$.modalDialog({
        title : '编辑',
        width : 700,
        height : 600,
        href :  '${path}/silverJackpot/editPage?id=' + id,
        buttons : [ {
            text : '确定',
            handler : function() {
                parent.$.modalDialog.openner_dataGrid = silverJackpotDataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
                var f = parent.$.modalDialog.handler.find('#silverJackpotEditForm');
                f.submit();
            }
        } ]
    });
}


/**
 * 删除
 */
 function silverJackpotDeleteFun(id) {
     if (id == undefined) {//点击右键菜单才会触发这个
         var rows = silverJackpotDataGrid.datagrid('getSelections');
         id = rows[0].id;
     } else {//点击操作里面的删除图标会触发这个
         silverJackpotDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
     }
     parent.$.messager.confirm('询问', '您是否要删除当前角色？', function(b) {
         if (b) {
             progressLoad();
             $.post('${path}/silverJackpot/delete', {
                 id : id
             }, function(result) {
                 if (result.success) {
                     parent.$.messager.alert('提示', result.msg, 'info');
                     silverJackpotDataGrid.datagrid('reload');
                 }
                 progressClose();
             }, 'JSON');
         }
     });
}


/**
 * 清除
 */
function silverJackpotCleanFun() {
    $('#silverJackpotSearchForm input').val('');
    silverJackpotDataGrid.datagrid('load', {});
}
/**
 * 搜索
 */
function silverJackpotSearchFun() {
     silverJackpotDataGrid.datagrid('load', $.serializeObject($('#silverJackpotSearchForm')));
}
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="silverJackpotSearchForm">
            <table>
                <tr>
                    <th>奖项名称:</th>
                    <td><input name="awardName" placeholder="搜索奖项名称"/></td>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="silverJackpotSearchFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'fi-x-circle',plain:true" onclick="silverJackpotCleanFun();">清空</a>
                    </td>
                </tr>
            </table>
        </form>
     </div>
 
    <div data-options="region:'center',border:false">
        <table id="silverJackpotDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>
<div id="silverJackpotToolbar" style="display: none;">
    <shiro:hasPermission name="/silverJackpot/add">
        <a onclick="silverJackpotAddFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'fi-page-add'">添加</a>
    </shiro:hasPermission>
</div>