<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    var mallProxyDataGrid;
    $(function() {
        mallProxyDataGrid = $('#mallProxyDataGrid').datagrid({
        url : '${path}/mallProxy/dataGrid',
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
            title : '代理类型',
            field : 'proxyType',
            sortable : true
        }, {
            width : '140',
            title : '代理微信号',
            field : 'wxNO',
            sortable : true
        }, {
            width : '140',
            title : '代理详情',
            field : 'proxyDesc',
            sortable : true
        }, {
            field : 'action',
            title : '操作',
            width : 200,
            formatter : function(value, row, index) {
                var str = '';
                <shiro:hasPermission name="/mallProxy/edit">
                    str += $.formatString('<a href="javascript:void(0)" class="mallProxy-easyui-linkbutton-edit" data-options="plain:true,iconCls:\'fi-pencil icon-blue\'" onclick="mallProxyEditFun(\'{0}\');" >编辑</a>', row.id);
                </shiro:hasPermission>
                <shiro:hasPermission name="/mallProxy/delete">
                    str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
                    str += $.formatString('<a href="javascript:void(0)" class="mallProxy-easyui-linkbutton-del" data-options="plain:true,iconCls:\'fi-x icon-red\'" onclick="mallProxyDeleteFun(\'{0}\');" >删除</a>', row.id);
                </shiro:hasPermission>
                return str;
            }
        } ] ],
        onLoadSuccess:function(data){
            $('.mallProxy-easyui-linkbutton-edit').linkbutton({text:'编辑'});
            $('.mallProxy-easyui-linkbutton-del').linkbutton({text:'删除'});
        },
        toolbar : '#mallProxyToolbar'
    });
});

/**
 * 添加框
 * @param url
 */
function mallProxyAddFun() {
    parent.$.modalDialog({
        title : '添加',
        width : 700,
        height : 600,
        href : '${path}/mallProxy/addPage',
        buttons : [ {
            text : '确定',
            handler : function() {
                parent.$.modalDialog.openner_dataGrid = mallProxyDataGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
                var f = parent.$.modalDialog.handler.find('#mallProxyAddForm');
                f.submit();
            }
        } ]
    });
}


/**
 * 编辑
 */
function mallProxyEditFun(id) {
    if (id == undefined) {
        var rows = mallProxyDataGrid.datagrid('getSelections');
        id = rows[0].id;
    } else {
        mallProxyDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
    }
    parent.$.modalDialog({
        title : '编辑',
        width : 700,
        height : 600,
        href :  '${path}/mallProxy/editPage?id=' + id,
        buttons : [ {
            text : '确定',
            handler : function() {
                parent.$.modalDialog.openner_dataGrid = mallProxyDataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
                var f = parent.$.modalDialog.handler.find('#mallProxyEditForm');
                f.submit();
            }
        } ]
    });
}


/**
 * 删除
 */
 function mallProxyDeleteFun(id) {
     if (id == undefined) {//点击右键菜单才会触发这个
         var rows = mallProxyDataGrid.datagrid('getSelections');
         id = rows[0].id;
     } else {//点击操作里面的删除图标会触发这个
         mallProxyDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
     }
     parent.$.messager.confirm('询问', '您是否要删除当前角色？', function(b) {
         if (b) {
             progressLoad();
             $.post('${path}/mallProxy/delete', {
                 id : id
             }, function(result) {
                 if (result.success) {
                	 showMsg(result.msg);
                     mallProxyDataGrid.datagrid('reload');
                 }
                 progressClose();
             }, 'JSON');
         }
     });
}


/**
 * 清除
 */
function mallProxyCleanFun() {
    $('#mallProxySearchForm input').val('');
    mallProxyDataGrid.datagrid('load', {});
}
/**
 * 搜索
 */
function mallProxySearchFun() {
     mallProxyDataGrid.datagrid('load', $.serializeObject($('#mallProxySearchForm')));
}
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="mallProxySearchForm">
            <table>
                <tr>
                </tr>
            </table>
        </form>
     </div>
 
    <div data-options="region:'center',border:false">
        <table id="mallProxyDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>
<div id="mallProxyToolbar" style="display: none;">
    <shiro:hasPermission name="/mallProxy/add">
        <a onclick="mallProxyAddFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'fi-page-add'">添加</a>
    </shiro:hasPermission>
</div>