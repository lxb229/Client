<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    var supplierDataGrid;
    $(function() {
        supplierDataGrid = $('#supplierDataGrid').datagrid({
        url : '${path}/supplier/dataGrid',
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
            title : '公司名字',
            field : 'companyName',
            sortable : true
        }, {
            width : '100',
            title : '供应商名字',
            field : 'supplierName',
            sortable : true
        }, {
            width : '50',
            title : '性别',
            field : 'supplierSex',
            sortable : true,
            formatter : function(value, row, index) {
                switch (value) {
                case 1:
                    return '男士';
                case 2:
                    return '女士';
                }
            }
        }, {
            width : '100',
            title : '电话',
            field : 'supplierPhone',
            sortable : true
        }, {
            width : '100',
            title : '微信',
            field : 'supplierWechat',
            sortable : true
        }, {
            width : '90',
            title : 'QQ',
            field : 'supplierQq',
            sortable : true
        }, {
            width : '140',
            title : '邮箱',
            field : 'supplierEmail',
            sortable : true
        }, {
            width : '140',
            title : '供应商官网',
            field : 'supplierUrl',
            sortable : true
        }, {
            width : '140',
            title : '地址',
            field : 'companyAddress',
            sortable : true
        }, {
            width : '140',
            title : '备注',
            field : 'remark',
            sortable : true
        }, {
            field : 'action',
            title : '操作',
            width : 200,
            formatter : function(value, row, index) {
                var str = '';
                <shiro:hasPermission name="/supplier/edit">
                    str += $.formatString('<a href="javascript:void(0)" class="supplier-easyui-linkbutton-edit" data-options="plain:true,iconCls:\'fi-pencil icon-blue\'" onclick="supplierEditFun(\'{0}\');" >编辑</a>', row.id);
                </shiro:hasPermission>
                <shiro:hasPermission name="/supplier/delete">
                    str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
                    str += $.formatString('<a href="javascript:void(0)" class="supplier-easyui-linkbutton-del" data-options="plain:true,iconCls:\'fi-x icon-red\'" onclick="supplierDeleteFun(\'{0}\');" >删除</a>', row.id);
                </shiro:hasPermission>
                return str;
            }
        } ] ],
        onLoadSuccess:function(data){
            $('.supplier-easyui-linkbutton-edit').linkbutton({text:'编辑'});
            $('.supplier-easyui-linkbutton-del').linkbutton({text:'删除'});
        },
        toolbar : '#supplierToolbar'
    });
});

/**
 * 添加框
 * @param url
 */
function supplierAddFun() {
    parent.$.modalDialog({
        title : '添加',
        width : 700,
        height : 600,
        href : '${path}/supplier/addPage',
        buttons : [ {
            text : '确定',
            handler : function() {
                parent.$.modalDialog.openner_dataGrid = supplierDataGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
                var f = parent.$.modalDialog.handler.find('#supplierAddForm');
                f.submit();
            }
        } ]
    });
}


/**
 * 编辑
 */
function supplierEditFun(id) {
    if (id == undefined) {
        var rows = supplierDataGrid.datagrid('getSelections');
        id = rows[0].id;
    } else {
        supplierDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
    }
    parent.$.modalDialog({
        title : '编辑',
        width : 700,
        height : 600,
        href :  '${path}/supplier/editPage?id=' + id,
        buttons : [ {
            text : '确定',
            handler : function() {
                parent.$.modalDialog.openner_dataGrid = supplierDataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
                var f = parent.$.modalDialog.handler.find('#supplierEditForm');
                f.submit();
            }
        } ]
    });
}


/**
 * 删除
 */
 function supplierDeleteFun(id) {
     if (id == undefined) {//点击右键菜单才会触发这个
         var rows = supplierDataGrid.datagrid('getSelections');
         id = rows[0].id;
     } else {//点击操作里面的删除图标会触发这个
         supplierDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
     }
     parent.$.messager.confirm('询问', '您是否要删除当前角色？', function(b) {
         if (b) {
             progressLoad();
             $.post('${path}/supplier/delete', {
                 id : id
             }, function(result) {
                 if (result.success) {
                     parent.$.messager.alert('提示', result.msg, 'info');
                     supplierDataGrid.datagrid('reload');
                 }
                 progressClose();
             }, 'JSON');
         }
     });
}


/**
 * 清除
 */
function supplierCleanFun() {
    $('#supplierSearchForm input').val('');
    supplierDataGrid.datagrid('load', {});
}
/**
 * 搜索
 */
function supplierSearchFun() {
     supplierDataGrid.datagrid('load', $.serializeObject($('#supplierSearchForm')));
}
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="supplierSearchForm">
            <table>
                <tr>
                    <th>供应商名称:</th>
                    <td><input name="supplierName" placeholder="供应商名称"/></td>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="supplierSearchFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'fi-x-circle',plain:true" onclick="supplierCleanFun();">清空</a>
                    </td>
                </tr>
            </table>
        </form>
     </div>
 
    <div data-options="region:'center',border:false">
        <table id="supplierDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>
<div id="supplierToolbar" style="display: none;">
    <shiro:hasPermission name="/supplier/add">
        <a onclick="supplierAddFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'fi-page-add'">添加</a>
    </shiro:hasPermission>
</div>