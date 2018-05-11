<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    var investmentDataGrid;
    $(function() {
        investmentDataGrid = $('#investmentDataGrid').datagrid({
        url : '${path}/investment/dataGrid',
        striped : true,
        rownumbers : true,
        pagination : true,
        singleSelect : true,
        idField : 'id',
        sortName : 'i.id',
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
            title : '投资金额',
            field : 'amount',
            sortable : true
        }, {
            width : '140',
            title : 'GM操作人',
            field : 'name',
            sortable : true
        }, {
            width : '140',
            title : '创建时间',
            field : 'createTime',
            sortable : true
        } ] ],
        onLoadSuccess:function(data){
            $('.investment-easyui-linkbutton-edit').linkbutton({text:'编辑'});
            $('.investment-easyui-linkbutton-del').linkbutton({text:'删除'});
        },
        toolbar : '#investmentToolbar'
    });
});

/**
 * 添加框
 * @param url
 */
function investmentAddFun() {
    parent.$.modalDialog({
        title : '添加',
        width : 700,
        height : 600,
        href : '${path}/investment/addPage',
        buttons : [ {
            text : '确定',
            handler : function() {
                parent.$.modalDialog.openner_dataGrid = investmentDataGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
                var f = parent.$.modalDialog.handler.find('#investmentAddForm');
                f.submit();
            }
        } ]
    });
}


/**
 * 编辑
 */
function investmentEditFun(id) {
    if (id == undefined) {
        var rows = investmentDataGrid.datagrid('getSelections');
        id = rows[0].id;
    } else {
        investmentDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
    }
    parent.$.modalDialog({
        title : '编辑',
        width : 700,
        height : 600,
        href :  '${path}/investment/editPage?id=' + id,
        buttons : [ {
            text : '确定',
            handler : function() {
                parent.$.modalDialog.openner_dataGrid = investmentDataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
                var f = parent.$.modalDialog.handler.find('#investmentEditForm');
                f.submit();
            }
        } ]
    });
}


/**
 * 删除
 */
 function investmentDeleteFun(id) {
     if (id == undefined) {//点击右键菜单才会触发这个
         var rows = investmentDataGrid.datagrid('getSelections');
         id = rows[0].id;
     } else {//点击操作里面的删除图标会触发这个
         investmentDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
     }
     parent.$.messager.confirm('询问', '您是否要删除当前角色？', function(b) {
         if (b) {
             progressLoad();
             $.post('${path}/investment/delete', {
                 id : id
             }, function(result) {
                 if (result.success) {
                     parent.$.messager.alert('提示', result.msg, 'info');
                     investmentDataGrid.datagrid('reload');
                 }
                 progressClose();
             }, 'JSON');
         }
     });
}


/**
 * 清除
 */
function investmentCleanFun() {
    $('#investmentSearchForm input').val('');
    investmentDataGrid.datagrid('load', {});
}
/**
 * 搜索
 */
function investmentSearchFun() {
     investmentDataGrid.datagrid('load', $.serializeObject($('#investmentSearchForm')));
}
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="investmentSearchForm">
            <table>
                <tr>
                    <th>名称:</th>
                    <td><input name="name" placeholder="搜索条件"/></td>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="investmentSearchFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'fi-x-circle',plain:true" onclick="investmentCleanFun();">清空</a>
                    </td>
                </tr>
            </table>
        </form>
     </div>
 
    <div data-options="region:'center',border:false">
        <table id="investmentDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>
<div id="investmentToolbar" style="display: none;">
    <shiro:hasPermission name="/investment/add">
        <a onclick="investmentAddFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'fi-page-add'">添加</a>
    </shiro:hasPermission>
</div>