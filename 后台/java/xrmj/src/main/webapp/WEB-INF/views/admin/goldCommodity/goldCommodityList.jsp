<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    var goldCommodityDataGrid;
    $(function() {
        goldCommodityDataGrid = $('#goldCommodityDataGrid').datagrid({
        url : '${path}/goldCommodity/dataGrid',
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
            width : '100',
            title : '商品名称',
            field : 'commodityName',
            sortable : true
        }, {
            width : '80',
            title : '市场价',
            field : 'marketPrice',
            sortable : true
        }, {
            width : '80',
            title : '兑换价',
            field : 'exchangePrice',
            sortable : true
        }, {
            width : '70',
            title : '兑换数量',
            field : 'amount',
            sortable : true
        }, {
            width : '70',
            title : '剩余数量',
            field : 'residue',
            sortable : true
        }, {
            width : '100',
            title : '上架时间',
            field : 'issuedTime',
            sortable : true
        }, {
            width : '100',
            title : '下架时间',
            field : 'soldoutTime',
            sortable : true
        }, {
            field : 'action',
            title : '操作',
            width : 200,
            formatter : function(value, row, index) {
                var str = '';
                <shiro:hasPermission name="/goldCommodity/edit">
                    str += $.formatString('<a href="javascript:void(0)" class="goldCommodity-easyui-linkbutton-edit" data-options="plain:true,iconCls:\'fi-pencil icon-blue\'" onclick="goldCommodityEditFun(\'{0}\');" >编辑</a>', row.id);
                </shiro:hasPermission>
                <shiro:hasPermission name="/goldCommodity/delete">
                    str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
                    str += $.formatString('<a href="javascript:void(0)" class="goldCommodity-easyui-linkbutton-del" data-options="plain:true,iconCls:\'fi-x icon-red\'" onclick="goldCommodityDeleteFun(\'{0}\');" >删除</a>', row.id);
                </shiro:hasPermission>
                return str;
            }
        } ] ],
        onLoadSuccess:function(data){
            $('.goldCommodity-easyui-linkbutton-edit').linkbutton({text:'编辑'});
            $('.goldCommodity-easyui-linkbutton-del').linkbutton({text:'删除'});
        },
        toolbar : '#goldCommodityToolbar'
    });
});

/**
 * 添加框
 * @param url
 */
function goldCommodityAddFun() {
    parent.$.modalDialog({
        title : '添加',
        width : 700,
        height : 600,
        href : '${path}/goldCommodity/addPage',
        buttons : [ {
            text : '确定',
            handler : function() {
                parent.$.modalDialog.openner_dataGrid = goldCommodityDataGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
                var f = parent.$.modalDialog.handler.find('#goldCommodityAddForm');
                f.submit();
            }
        } ]
    });
}


/**
 * 编辑
 */
function goldCommodityEditFun(id) {
    if (id == undefined) {
        var rows = goldCommodityDataGrid.datagrid('getSelections');
        id = rows[0].id;
    } else {
        goldCommodityDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
    }
    parent.$.modalDialog({
        title : '编辑',
        width : 700,
        height : 600,
        href :  '${path}/goldCommodity/editPage?id=' + id,
        buttons : [ {
            text : '确定',
            handler : function() {
                parent.$.modalDialog.openner_dataGrid = goldCommodityDataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
                var f = parent.$.modalDialog.handler.find('#goldCommodityEditForm');
                f.submit();
            }
        } ]
    });
}


/**
 * 删除
 */
 function goldCommodityDeleteFun(id) {
     if (id == undefined) {//点击右键菜单才会触发这个
         var rows = goldCommodityDataGrid.datagrid('getSelections');
         id = rows[0].id;
     } else {//点击操作里面的删除图标会触发这个
         goldCommodityDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
     }
     parent.$.messager.confirm('询问', '您是否要删除当前兑换奖品？', function(b) {
         if (b) {
             progressLoad();
             $.post('${path}/goldCommodity/delete', {
                 id : id
             }, function(result) {
                 if (result.success) {
                     parent.$.messager.alert('提示', result.msg, 'info');
                     goldCommodityDataGrid.datagrid('reload');
                 }
                 progressClose();
             }, 'JSON');
         }
     });
}


/**
 * 清除
 */
function goldCommodityCleanFun() {
    $('#goldCommoditySearchForm input').val('');
    goldCommodityDataGrid.datagrid('load', {});
}
/**
 * 搜索
 */
function goldCommoditySearchFun() {
     goldCommodityDataGrid.datagrid('load', $.serializeObject($('#goldCommoditySearchForm')));
}
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="goldCommoditySearchForm">
            <table>
                <tr>
                    <th>商品名称:</th>
                    <td><input name="commodityName" placeholder="搜索商品名称"/></td>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="goldCommoditySearchFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'fi-x-circle',plain:true" onclick="goldCommodityCleanFun();">清空</a>
                    </td>
                </tr>
            </table>
        </form>
     </div>
 
    <div data-options="region:'center',border:false">
        <table id="goldCommodityDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>
<div id="goldCommodityToolbar" style="display: none;">
    <shiro:hasPermission name="/goldCommodity/add">
        <a onclick="goldCommodityAddFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'fi-page-add'">添加</a>
    </shiro:hasPermission>
</div>