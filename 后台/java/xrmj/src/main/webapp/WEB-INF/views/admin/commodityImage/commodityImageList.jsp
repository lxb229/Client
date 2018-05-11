<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    var commodityImageDataGrid;
    $(function() {
        commodityImageDataGrid = $('#commodityImageDataGrid').datagrid({
        url : '${path}/commodityImage/dataGrid',
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
        }, {
            width : '60',
            title : '图片类型',
            field : 'imageType',
            sortable : true,
            formatter : function(value, row, index) {
                switch (value) {
                case 1:
                    return 'icon图片';
                case 2:
                    return '详情图片';
                }
            }
        }, {
            width : '140',
            title : '图片地址',
            field : 'imageUrl',
            sortable : true
        }, {
            field : 'action',
            title : '操作',
            width : 200,
            formatter : function(value, row, index) {
                var str = '';
                <shiro:hasPermission name="/commodityImage/edit">
                    str += $.formatString('<a href="javascript:void(0)" class="commodityImage-easyui-linkbutton-edit" data-options="plain:true,iconCls:\'fi-pencil icon-blue\'" onclick="commodityImageEditFun(\'{0}\');" >编辑</a>', row.id);
                </shiro:hasPermission>
                <shiro:hasPermission name="/commodityImage/delete">
                    str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
                    str += $.formatString('<a href="javascript:void(0)" class="commodityImage-easyui-linkbutton-del" data-options="plain:true,iconCls:\'fi-x icon-red\'" onclick="commodityImageDeleteFun(\'{0}\');" >删除</a>', row.id);
                </shiro:hasPermission>
                return str;
            }
        } ] ],
        onLoadSuccess:function(data){
            $('.commodityImage-easyui-linkbutton-edit').linkbutton({text:'编辑'});
            $('.commodityImage-easyui-linkbutton-del').linkbutton({text:'删除'});
        },
        toolbar : '#commodityImageToolbar'
    });
});

/**
 * 添加框
 * @param url
 */
function commodityImageAddFun() {
    parent.$.modalDialog({
        title : '添加',
        width : 700,
        height : 600,
        href : '${path}/commodityImage/addPage',
        buttons : [ {
            text : '确定',
            handler : function() {
                parent.$.modalDialog.openner_dataGrid = commodityImageDataGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
                var f = parent.$.modalDialog.handler.find('#commodityImageAddForm');
                f.submit();
            }
        } ]
    });
}


/**
 * 编辑
 */
function commodityImageEditFun(id) {
    if (id == undefined) {
        var rows = commodityImageDataGrid.datagrid('getSelections');
        id = rows[0].id;
    } else {
        commodityImageDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
    }
    parent.$.modalDialog({
        title : '编辑',
        width : 700,
        height : 600,
        href :  '${path}/commodityImage/editPage?id=' + id,
        buttons : [ {
            text : '确定',
            handler : function() {
                parent.$.modalDialog.openner_dataGrid = commodityImageDataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
                var f = parent.$.modalDialog.handler.find('#commodityImageEditForm');
                f.submit();
            }
        } ]
    });
}


/**
 * 删除
 */
 function commodityImageDeleteFun(id) {
     if (id == undefined) {//点击右键菜单才会触发这个
         var rows = commodityImageDataGrid.datagrid('getSelections');
         id = rows[0].id;
     } else {//点击操作里面的删除图标会触发这个
         commodityImageDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
     }
     parent.$.messager.confirm('询问', '您是否要删除当前角色？', function(b) {
         if (b) {
             progressLoad();
             $.post('${path}/commodityImage/delete', {
                 id : id
             }, function(result) {
                 if (result.success) {
                     parent.$.messager.alert('提示', result.msg, 'info');
                     commodityImageDataGrid.datagrid('reload');
                 }
                 progressClose();
             }, 'JSON');
         }
     });
}


/**
 * 清除
 */
function commodityImageCleanFun() {
    $('#commodityImageSearchForm input').val('');
    commodityImageDataGrid.datagrid('load', {});
}
/**
 * 搜索
 */
function commodityImageSearchFun() {
     commodityImageDataGrid.datagrid('load', $.serializeObject($('#commodityImageSearchForm')));
}
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="commodityImageSearchForm">
            <table>
                <tr>
                    <th>商品名称:</th>
                    <td><input name="commodityName" placeholder="搜索商品名称"/></td>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="commodityImageSearchFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'fi-x-circle',plain:true" onclick="commodityImageCleanFun();">清空</a>
                    </td>
                </tr>
            </table>
        </form>
     </div>
 
    <div data-options="region:'center',border:false">
        <table id="commodityImageDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>
<div id="commodityImageToolbar" style="display: none;">
    <shiro:hasPermission name="/commodityImage/add">
        <a onclick="commodityImageAddFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'fi-page-add'">添加</a>
    </shiro:hasPermission>
</div>