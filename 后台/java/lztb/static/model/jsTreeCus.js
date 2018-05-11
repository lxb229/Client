/*
document.write("<link href=\"/lib/jsTree/jstree.css\" rel=\"stylesheet\" type=\"text/css\" />");
document.write('<script language="JavaScript" src="/lib/jsTree/jstree.js"><\/script>');
*/

/* jsTree相关方法 */
var _jsTreeCheckedNodeId = [];
var _jsTreeUncheckedNodeId = [];

//初始化jsTree统计相关变量
function jsTreeDataIni() {
    _jsTreeCheckedNodeId.length = 0;
    _jsTreeUncheckedNodeId.length = 0;
}

//jsTree nodechange事件配合使用
function jsTreeNodeChange(data) {
    var _clickedId;

    if (data.action == "select_node") {
        _clickedId = data.node.id;
        jsTreeNodeChange_RemoveFromUncheck(_clickedId);
        _jsTreeCheckedNodeId.push(_clickedId);
    }
    if (data.action == "deselect_node") {
        _clickedId = data.node.id;
        jsTreeNodeChange_RemoveFromCheck(_clickedId);
        _jsTreeUncheckedNodeId.push(_clickedId);
    }

}

function jsTreeNodeChange_RemoveFromCheck(id) {
    arrayValueRemove(_jsTreeCheckedNodeId, id);
}

function jsTreeNodeChange_RemoveFromUncheck(id) {
    arrayValueRemove(_jsTreeUncheckedNodeId, id);
}

//获取jsTree中这一轮选中的ID值
function jsTreeGetCheckedNodesId() {
    return filterArrayduplication(_jsTreeCheckedNodeId);
}

//获取jsTree中这一轮取消选中的ID值
function jsTreeGetUncheckedNodesId() {
    return filterArrayduplication(_jsTreeUncheckedNodeId);
}

//过滤数组中的重复值
function filterArrayduplication(arr) {
    var str = [];
    for (var i = 0, len = arr.length; i < len; i++) {
        //!RegExp(arr[i], "g").test(str.join(",")) && (str.push(arr[i]));
        !RegExp("#_" + arr[i] + "_#", "g").test("#_" + str.join("_#,#_") + "_#") && (str.push(arr[i])); //匹配的时候添加前缀、后缀，防止匹配出错
    }
    return str;
}

function arrayValueRemove(arr, value) {
    position = $.inArray(value, arr);
    if (~position) arr.splice(position, 1);
}

//公用 - 渲染jsTree树 
//@el: 树控件的ID（不带#）
//@url: ajax异步数据获取地址（可为string 可为function）
//@selCb(string id, string text, object originalNodeInfo): 点击结点事件
//@pluginsArr: https://www.jstree.com/plugins/ (插件名数组)
//@pluginsOpts: 插件的参数({插件名1:参数1, 插件名2:参数2}格式)
//@coreOpts: core的参数({插件名1:参数1, 插件名2:参数2}格式)
function gTreeLoadCom(el, url, selCb, pluginsArr, pluginsOpts, coreOpts) {
    if (typeof pluginsArr !== "object" || pluginsArr.length <= 0) {
        pluginsArr = ["state"];
    } else {
        if (pluginsArr.indexOf("state") < 0) {
            pluginsArr.push("state"); //始终要用state这个插件
        }
    }

    if (typeof pluginsOpts === "undefined") {
        pluginsOpts = {};
    }

    if (typeof coreOpts === "undefined") {
        coreOpts = {};
    }

    $('#' + el)
        .on('select_node.jstree', function (e, data) {
            if (selCb) {
                var value = data.node.original.dataId;
                if (typeof value !== "string" || value == null)
                    value = data.node.original.id;

                var text = data.node.original.text;
                selCb(value, text, data.node.original);
            }
        })
        .on('loaded.jstree', function (e, data) {
            $('#' + el).jstree("open_all");
        })
        .jstree({
            'core': {
                'data': {
                    "url": function () {
                        if (typeof url === "function")
                            return url();
                        else
                            return url;
                    },
                    "dataType": "json"
                },
                'strings': {
                    'Loading ...': '加载中...'
                },
                'animation': 200, //set false to close animation
                'multiple': coreOpts.multiple === true || coreOpts.multiple === 'true',
                'expand_selected_onload': true
            },
            "plugins": pluginsArr,
            "contextmenu": pluginsOpts.contextmenu,
            "checkbox": pluginsOpts.checkbox
        });
}