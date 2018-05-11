<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    var commodityLvDataGrid;
    $(function() {
        commodityLvDataGrid = $('#commodityLvDataGrid').datagrid({
        url : '${path}/commodityLv/dataGrid',
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
            title : '等级名称',
            field : 'lvName',
            sortable : true
        }, {
            width : '140',
            title : '等级',
            field : 'lv',
            sortable : true
        }, {
            width : '140',
            title : '等级排序',
            field : 'rank',
            sortable : true
        }, {
            field : 'action',
            title : '操作',
            width : 200,
            formatter : function(value, row, index) {
                var str = '';
                <shiro:hasPermission name="/commodityLv/edit">
                    str += $.formatString('<a href="javascript:void(0)" class="commodityLv-easyui-linkbutton-edit" data-options="plain:true,iconCls:\'fi-pencil icon-blue\'" onclick="commodityLvEditFun(\'{0}\');" >编辑</a>', row.id);
                </shiro:hasPermission>
                <shiro:hasPermission name="/commodityLv/delete">
                    str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
                    str += $.formatString('<a href="javascript:void(0)" class="commodityLv-easyui-linkbutton-del" data-options="plain:true,iconCls:\'fi-x icon-red\'" onclick="commodityLvDeleteFun(\'{0}\');" >删除</a>', row.id);
                </shiro:hasPermission>
                return str;
            }
        } ] ],
        onLoadSuccess:function(data){
            $('.commodityLv-easyui-linkbutton-edit').linkbutton({text:'编辑'});
            $('.commodityLv-easyui-linkbutton-del').linkbutton({text:'删除'});
        },
        toolbar : '#commodityLvToolbar'
    });
});

/**
 * 添加框
 * @param url
 */
function commodityLvAddFun() {
    parent.$.modalDialog({
        title : '添加',
        width : 700,
        height : 600,
        href : '${path}/commodityLv/addPage',
        buttons : [ {
            text : '确定',
            handler : function() {
                parent.$.modalDialog.openner_dataGrid = commodityLvDataGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
                var f = parent.$.modalDialog.handler.find('#commodityLvAddForm');
                f.submit();
            }
        } ]
    });
}


/**
 * 编辑
 */
function commodityLvEditFun(id) {
    if (id == undefined) {
        var rows = commodityLvDataGrid.datagrid('getSelections');
        id = rows[0].id;
    } else {
        commodityLvDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
    }
    parent.$.modalDialog({
        title : '编辑',
        width : 700,
        height : 600,
        href :  '${path}/commodityLv/editPage?id=' + id,
        buttons : [ {
            text : '确定',
            handler : function() {
                parent.$.modalDialog.openner_dataGrid = commodityLvDataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
                var f = parent.$.modalDialog.handler.find('#commodityLvEditForm');
                f.submit();
            }
        } ]
    });
}


/**
 * 删除
 */
 function commodityLvDeleteFun(id) {
     if (id == undefined) {//点击右键菜单才会触发这个
         var rows = commodityLvDataGrid.datagrid('getSelections');
         id = rows[0].id;
     } else {//点击操作里面的删除图标会触发这个
         commodityLvDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
     }
     parent.$.messager.confirm('询问', '您是否要删除当前角色？', function(b) {
         if (b) {
             progressLoad();
             $.post('${path}/commodityLv/delete', {
                 id : id
             }, function(result) {
                 if (result.success) {
                	 showMsg(result.msg);
                     commodityLvDataGrid.datagrid('reload');
                 }
                 progressClose();
             }, 'JSON');
         }
     });
}


/**
 * 清除
 */
function commodityLvCleanFun() {
    $('#commodityLvSearchForm input').val('');
    commodityLvDataGrid.datagrid('load', {});
}
/**
 * 搜索
 */
function commodityLvSearchFun() {
     commodityLvDataGrid.datagrid('load', $.serializeObject($('#commodityLvSearchForm')));
}
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="commodityLvSearchForm">
            <table>
                <tr>
                    <th>等级名称:</th>
                    <td><input name="lvName" placeholder="搜索等级"/></td>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="commodityLvSearchFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'fi-x-circle',plain:true" onclick="commodityLvCleanFun();">清空</a>
                    </td>
                </tr>
            </table>
        </form>
     </div>
 
    <div data-options="region:'center',border:false">
        <table id="commodityLvDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>
<div id="commodityLvToolbar" style="display: none;">
    <shiro:hasPermission name="/commodityLv/add">
        <a onclick="commodityLvAddFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'fi-page-add'">添加</a>
    </shiro:hasPermission>
</div>