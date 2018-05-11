<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    var wechatDataGrid;
    $(function() {
        wechatDataGrid = $('#wechatDataGrid').datagrid({
        url : '${path}/wechat/dataGrid',
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
            width : '60',
            title : '微信号码',
            field : 'wechat'
        }, {
            width : '60',
            title : '状态',
            field : 'state',
            sortable : true,
            formatter : function(value, row, index) {
                switch (value) {
                case 0:
                    return '正常';
                case 1:
                    return '停用';
                }
            }
        }, {
            width : '140',
            title : '创建时间',
            field : 'createTime',
            sortable : true
        }, {
            field : 'action',
            title : '操作',
            width : 200,
            formatter : function(value, row, index) {
                var str = '';
                <shiro:hasPermission name="/wechat/edit">
                    str += $.formatString('<a href="javascript:void(0)" class="wechat-easyui-linkbutton-edit" data-options="plain:true,iconCls:\'fi-pencil icon-blue\'" onclick="wechatEditFun(\'{0}\');" >编辑</a>', row.id);
                </shiro:hasPermission>
                <shiro:hasPermission name="/wechat/delete">
                    str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
                    str += $.formatString('<a href="javascript:void(0)" class="wechat-easyui-linkbutton-del" data-options="plain:true,iconCls:\'fi-x icon-red\'" onclick="wechatDeleteFun(\'{0}\');" >删除</a>', row.id);
                </shiro:hasPermission>
                return str;
            }
        } ] ],
        onLoadSuccess:function(data){
            $('.wechat-easyui-linkbutton-edit').linkbutton({text:'编辑'});
            $('.wechat-easyui-linkbutton-del').linkbutton({text:'删除'});
        },
        toolbar : '#wechatToolbar'
    });
});

/**
 * 添加框
 * @param url
 */
function wechatAddFun() {
    parent.$.modalDialog({
        title : '添加',
        width : 700,
        height : 600,
        href : '${path}/wechat/addPage',
        buttons : [ {
            text : '确定',
            handler : function() {
                parent.$.modalDialog.openner_dataGrid = wechatDataGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
                var f = parent.$.modalDialog.handler.find('#wechatAddForm');
                f.submit();
            }
        } ]
    });
}

function wechatList() {
	var opts = {
            title : '测试',
            border : false,
            closable : true,
            fit : true,
            iconCls : 'fi-sound'
        };
	var href = '${path}/wechat/list';
	opts.content = '<iframe src="'+href+'" frameborder="0" style="border:0;width:100%;height:99.5%;"></iframe>';
    addTab(opts);
}


/**
 * 编辑
 */
function wechatEditFun(id) {
    if (id == undefined) {
        var rows = wechatDataGrid.datagrid('getSelections');
        id = rows[0].id;
    } else {
        wechatDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
    }
    parent.$.modalDialog({
        title : '编辑',
        width : 700,
        height : 600,
        href :  '${path}/wechat/editPage?id=' + id,
        buttons : [ {
            text : '确定',
            handler : function() {
                parent.$.modalDialog.openner_dataGrid = wechatDataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
                var f = parent.$.modalDialog.handler.find('#wechatEditForm');
                f.submit();
            }
        } ]
    });
}


/**
 * 删除
 */
 function wechatDeleteFun(id) {
     if (id == undefined) {//点击右键菜单才会触发这个
         var rows = wechatDataGrid.datagrid('getSelections');
         id = rows[0].id;
     } else {//点击操作里面的删除图标会触发这个
         wechatDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
     }
     parent.$.messager.confirm('询问', '您是否要删除当微信信息？', function(b) {
         if (b) {
             progressLoad();
             $.post('${path}/wechat/delete', {
                 id : id
             }, function(result) {
                 if (result.success) {
                     parent.$.messager.alert('提示', result.msg, 'info');
                     wechatDataGrid.datagrid('reload');
                 }
                 progressClose();
             }, 'JSON');
         }
     });
}


/**
 * 清除
 */
function wechatCleanFun() {
    $('#wechatSearchForm input').val('');
    wechatDataGrid.datagrid('load', {});
}
/**
 * 搜索
 */
function wechatSearchFun() {
     wechatDataGrid.datagrid('load', $.serializeObject($('#wechatSearchForm')));
}
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="wechatSearchForm">
            <table>
                <tr>
                    <th>微信名称:</th>
                    <td><input name="wechat" placeholder="搜索条件"/></td>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="wechatSearchFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'fi-x-circle',plain:true" onclick="wechatCleanFun();">清空</a>
                    </td>
                </tr>
            </table>
        </form>
     </div>
 
    <div data-options="region:'center',border:false">
        <table id="wechatDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>
<div id="wechatToolbar" style="display: none;">
    <shiro:hasPermission name="/wechat/add">
        <a onclick="wechatAddFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'fi-page-add'">添加</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="/wechat/list">
        <a onclick="wechatList();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'fi-page-add'">列表</a>
        
        <span id="layout_west_tree_4_switch" title="" class="button level1 switch center_docu" treenode_switch=""></span>
		<a id="layout_west_tree_4_a" class="level1" treenode_a="" onclick="" target="_blank" style="" title="列表">
			<span id="layout_west_tree_4_ico" title="" treenode_ico="" class="fi-torsos-all button ico_docu" style=""></span>
			<span id="layout_west_tree_4_span" class=" node_name">列表</span>
		</a>
        
    </shiro:hasPermission>
</div>