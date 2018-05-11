<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    var silverCommodityDataGrid;
    $(function() {
        silverCommodityDataGrid = $('#silverCommodityDataGrid').datagrid({
        url : '${path}/silverCommodity/dataGrid',
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
            title : '商品名称',
            field : 'commodityName',
            sortable : true
        },{
            width : '60',
            title : '等级名称',
            field : 'lvName',
            sortable : true
        }, {
            width : '140',
            title : '上架时间',
            field : 'issuedTime',
            sortable : true
        }, {
            width : '140',
            title : '下架时间',
            field : 'soldoutTime',
            sortable : true
        }, {
            field : 'action',
            title : '操作',
            width : 200,
            formatter : function(value, row, index) {
                var str = '';
                <shiro:hasPermission name="/silverCommodity/edit">
                    str += $.formatString('<a href="javascript:void(0)" class="silverCommodity-easyui-linkbutton-edit" data-options="plain:true,iconCls:\'fi-pencil icon-blue\'" onclick="silverCommodityEditFun(\'{0}\');" >编辑</a>', row.id);
                </shiro:hasPermission>
                <shiro:hasPermission name="/silverCommodity/delete">
                    str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
                    str += $.formatString('<a href="javascript:void(0)" class="silverCommodity-easyui-linkbutton-del" data-options="plain:true,iconCls:\'fi-x icon-red\'" onclick="silverCommodityDeleteFun(\'{0}\');" >删除</a>', row.id);
                </shiro:hasPermission>
                return str;
            }
        } ] ],
        onLoadSuccess:function(data){
            $('.silverCommodity-easyui-linkbutton-edit').linkbutton({text:'编辑'});
            $('.silverCommodity-easyui-linkbutton-del').linkbutton({text:'删除'});
        },
        toolbar : '#silverCommodityToolbar'
    });
});

/**
 * 添加框
 * @param url
 */
function silverCommodityAddFun() {
    parent.$.modalDialog({
        title : '添加',
        width : 700,
        height : 600,
        href : '${path}/silverCommodity/addPage',
        buttons : [ {
            text : '确定',
            handler : function() {
                parent.$.modalDialog.openner_dataGrid = silverCommodityDataGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
                var f = parent.$.modalDialog.handler.find('#silverCommodityAddForm');
                f.submit();
            }
        } ]
    });
}


/**
 * 编辑
 */
function silverCommodityEditFun(id) {
    if (id == undefined) {
        var rows = silverCommodityDataGrid.datagrid('getSelections');
        id = rows[0].id;
    } else {
        silverCommodityDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
    }
    parent.$.modalDialog({
        title : '编辑',
        width : 700,
        height : 600,
        href :  '${path}/silverCommodity/editPage?id=' + id,
        buttons : [ {
            text : '确定',
            handler : function() {
                parent.$.modalDialog.openner_dataGrid = silverCommodityDataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
                var f = parent.$.modalDialog.handler.find('#silverCommodityEditForm');
                f.submit();
            }
        } ]
    });
}


/**
 * 删除
 */
 function silverCommodityDeleteFun(id) {
     if (id == undefined) {//点击右键菜单才会触发这个
         var rows = silverCommodityDataGrid.datagrid('getSelections');
         id = rows[0].id;
     } else {//点击操作里面的删除图标会触发这个
         silverCommodityDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
     }
     parent.$.messager.confirm('询问', '您是否要删除当前抽奖奖品？', function(b) {
         if (b) {
             progressLoad();
             $.post('${path}/silverCommodity/delete', {
                 id : id
             }, function(result) {
                 if (result.success) {
                     parent.$.messager.alert('提示', result.msg, 'info');
                     silverCommodityDataGrid.datagrid('reload');
                 }
                 progressClose();
             }, 'JSON');
         }
     });
}


/**
 * 清除
 */
function silverCommodityCleanFun() {
    $('#silverCommoditySearchForm input').val('');
    silverCommodityDataGrid.datagrid('load', {});
}
/**
 * 搜索
 */
function silverCommoditySearchFun() {
     silverCommodityDataGrid.datagrid('load', $.serializeObject($('#silverCommoditySearchForm')));
}
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="silverCommoditySearchForm">
            <table>
                <tr>
                    <th>商品名称:</th>
                    <td><input name="commodityName" placeholder="搜索商品名称"/></td>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="silverCommoditySearchFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'fi-x-circle',plain:true" onclick="silverCommodityCleanFun();">清空</a>
                    </td>
                </tr>
            </table>
        </form>
     </div>
 
    <div data-options="region:'center',border:false">
        <table id="silverCommodityDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>
<div id="silverCommodityToolbar" style="display: none;">
    <shiro:hasPermission name="/silverCommodity/add">
        <a onclick="silverCommodityAddFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'fi-page-add'">添加</a>
    </shiro:hasPermission>
</div>