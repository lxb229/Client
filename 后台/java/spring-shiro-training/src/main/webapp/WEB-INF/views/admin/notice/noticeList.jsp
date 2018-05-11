<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    var noticeDataGrid;
    $(function() {
        noticeDataGrid = $('#noticeDataGrid').datagrid({
        url : '${path}/notice/dataGrid',
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
            title : '公告名称',
            field : 'name',
            sortable : true
        }, {
            width : '270',
            title : '标题图片',
            field : 'titleImg',
            sortable : true
        }, {
            width : '270',
            title : '内容图片',
            field : 'contentImg',
            sortable : true
        }, {
            width : '120',
            title : '创建时间',
            field : 'createTime',
            sortable : true
        }, {
            field : 'action',
            title : '操作',
            width : 200,
            formatter : function(value, row, index) {
                var str = '';
                <shiro:hasPermission name="/notice/edit">
                    str += $.formatString('<a href="javascript:void(0)" class="notice-easyui-linkbutton-edit" data-options="plain:true,iconCls:\'fi-pencil icon-blue\'" onclick="noticeEditFun(\'{0}\');" >编辑</a>', row.id);
                    str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
                </shiro:hasPermission>
                <shiro:hasPermission name="/notice/delete">
                    str += $.formatString('<a href="javascript:void(0)" class="notice-easyui-linkbutton-del" data-options="plain:true,iconCls:\'fi-x icon-red\'" onclick="noticeDeleteFun(\'{0}\');" >删除</a>', row.id);
                </shiro:hasPermission>
                return str;
            }
        } ] ],
        onLoadSuccess:function(data){
            $('.notice-easyui-linkbutton-edit').linkbutton({text:'编辑'});
            $('.notice-easyui-linkbutton-del').linkbutton({text:'删除'});
        },
        toolbar : '#noticeToolbar'
    });
});

/**
 * 添加框
 * @param url
 */
function noticeAddFun() {
    parent.$.modalDialog({
        title : '添加',
        width : 700,
        height : 600,
        href : '${path}/notice/addPage',
        buttons : [ {
            text : '保存',
            click : 'ceshi()',
            handler : function() {
                parent.$.modalDialog.openner_dataGrid = noticeDataGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
                var f = parent.$.modalDialog.handler.find('#noticeAddForm');
                f.submit();
            }
        } ]
    });
}


/**
 * 编辑
 */
function noticeEditFun(id) {
    if (id == undefined) {
        var rows = noticeDataGrid.datagrid('getSelections');
        id = rows[0].id;
    } else {
        noticeDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
    }
    parent.$.modalDialog({
        title : '编辑',
        width : 700,
        height : 600,
        href :  '${path}/notice/editPage?id=' + id,
        buttons : [ {
            text : '确定',
            handler : function() {
                parent.$.modalDialog.openner_dataGrid = noticeDataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
                var f = parent.$.modalDialog.handler.find('#noticeEditForm');
                f.submit();
            }
        } ]
    });
}


/**
 * 删除
 */
 function noticeDeleteFun(id) {
     if (id == undefined) {//点击右键菜单才会触发这个
         var rows = noticeDataGrid.datagrid('getSelections');
         id = rows[0].id;
     } else {//点击操作里面的删除图标会触发这个
         noticeDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
     }
     parent.$.messager.confirm('询问', '您是否要删除当前角色？', function(b) {
         if (b) {
             progressLoad();
             $.post('${path}/notice/delete', {
                 id : id
             }, function(result) {
                 if (result.success) {
                     parent.$.messager.alert('提示', result.msg, 'info');
                     noticeDataGrid.datagrid('reload');
                 }
                 progressClose();
             }, 'JSON');
         }
     });
}


/**
 * 清除
 */
function noticeCleanFun() {
    $('#noticeSearchForm input').val('');
    noticeDataGrid.datagrid('load', {});
}
/**
 * 搜索
 */
function noticeSearchFun() {
     noticeDataGrid.datagrid('load', $.serializeObject($('#noticeSearchForm')));
}
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="noticeSearchForm">
            <table>
                <tr>
                    <th>名称:</th>
                    <td><input name="name" placeholder="搜索条件"/></td>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="noticeSearchFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'fi-x-circle',plain:true" onclick="noticeCleanFun();">清空</a>
                    </td>
                </tr>
            </table>
        </form>
     </div>
 
    <div data-options="region:'center',border:false">
        <table id="noticeDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>
<div id="noticeToolbar" style="display: none;">
    <shiro:hasPermission name="/notice/add">
        <a onclick="noticeAddFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'fi-page-add'">添加</a>
    </shiro:hasPermission>
</div>