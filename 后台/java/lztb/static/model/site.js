//普通alert弹窗
//alert(235)
function AlertBox(msg, title, time) {
    layer.open({
        title: (typeof title === "undefined" ? false : title),
        content: msg,
        time: (typeof time === "undefined" ? 0 : time),
        shade: 0.3,
        closeBtn: 0
    });
}


//成功消息
function SuccessBox(msg, title, time) {
    layer.msg(msg, {
        icon: 1,
        anim: 2,
        time: (typeof time === "undefined" ? 1500 : time),
        offset: ['70px', (document.body.offsetWidth - 205) + "px"]
    });
}

//失败消息
function ErrorBox(msg, title, time) {
    layer.open({
        icon: 2,
        title: (typeof title === "undefined" ? false : title),
        content: msg,
        time: (typeof time === "undefined" ? 0 : time),
        shade: 0.2,
        closeBtn: 0
    });
}

//询问框
//@cfmMsg 主体内容
//@cb_yes 点确定的回调事件
//@title 标题名字(不填的话直接不显示标题栏)
//@cb_cancel 点取消的回调事件
function ConfirmBox(cfmMsg, cb_yes, title, cb_cancel) {
    layer.confirm(cfmMsg, {
        icon: 3,
        title: (typeof title === "string" ? title : false),
        shade: 0.1
    }, function (index) { //“确定”回调
        layer.close(index);
        if (typeof cb_yes === "function") cb_yes();
    }, function (index) { //“取消”回调
        layer.close(index);
        if (typeof cb_cancel === "function") cb_cancel();
    });
}

//自动展现、消失的简略信息
function AlertMsg(msg) {
    layer.msg(msg, {
        icon: 5,
        shift: 4
    });
}

//打开并显示页面帮助
function gHelp(msg) {
    layer.open({
        title: '帮助',
        icon: 3,
        content: msg,
        closeBtn: 0,
        shade: 0,
        shift: 3,
        move: false,
        btn: ['知道了']
    });
}

//自动展现、消失的简略信息
function AlertTip(id, msg, position) {
    var p;
    switch (position) {
        case "up": p = 1; break;
        case "down": p = 3; break;
        case "left": p = 4; break;
        case "right": p = 2; break;
        default: p = 1; break;
    }


    layer.tips(msg, '#' + id, {
        tips: [p, '#42a9dc']
    });
}

//打开一个内容为iframe页的弹窗
//@url: iframe的url
//@width: 弹窗的长度（像素）
//@height: 弹窗的高度（像素）
//@allowScrollbar: 允许滚动条 bool
//@showMaxMin: 是否显示最大最小化
//@maxOnOpen: 弹出即全屏
//return: 弹窗的index
function gOpenModalWindow(title, url, width, height, allowScrollbar, showMaxMin, maxOnOpen,close_cb) {
	if (typeof width === 'number')
        width = width + 'px';
    if (typeof height === 'number')
        height = height + 'px';
    if (typeof showMaxMin === "undefined") {
        showMaxMin = false;
    }
    if (typeof allowScrollbar === "undefined" || allowScrollbar === true || allowScrollbar === "true")
        allowScrollbar = 'yes';
    else
        allowScrollbar = 'no';

    var _index = layer.open({
        type: 2,
        title: title,
        shade: 0.1,
        //offset: '50px',
        maxmin: showMaxMin,
        area: [width, height],
        content: [url, allowScrollbar], //如果不想让iframe出现滚动条，这样写content: ['http://xxx.com', 'no']
        end:close_cb
    });

    if (maxOnOpen === true || maxOnOpen === "true") {
        layer.full(_index);
    }

    return _index;
}

/**
 * 关闭弹窗窗口
 * @param {int} index 弹窗索引，不指定关闭全部
 */
function gCloseModalWindow(index) {
    if(index) {
        layer.close(index);
    } else {
        layer.closeAll();
    }
}

//实例化layPage
//@cont: 容器。值可以传入元素id或原生DOM或jquery对象（如 id ， $('#id')）
//@curr: 当前页数
//@total: 总页数
//@cb(pageIndexClicked, isFirst): 回调(跳转的页码，页面是否初始加载)
function gLayPageIni(cont, curr, total, cb) {
    var laypage = layui.laypage;
    laypage({
        cont: cont
        , pages: total
        , curr: curr
        , first: '首页'
        , skip: true
        , jump: function (obj, first) {
            console.log(cont + "翻到了第" + obj.curr + "页");
            if (typeof cb === "function") {
                cb(obj.curr, first);
            }
        }
    });
}

//获取url参数内容（key不区分大小写 value区分）
function gtUrl(gonten) {
    var url = window.location.href;
    var u = url.split("?");
    var s, ss, str;
    if (u.length === 1) s = '';
    else s = u[1];
    ss = s.split("&");
    gonten = gonten.toLowerCase();
    str = gonten + "=";
    var j = ss.length;
    for (i = 0; i < j; i++) {
        var kvp = ss[i].split("=");
        if (kvp[0].toLowerCase() === gonten) {
            $gonten_str = ss[i].substring(kvp[0].length + 1);
            break;
        } else {
            $gonten_str = '';
        }
    }
    try {
        return decodeURI($gonten_str.toLowerCase());

    } catch (e) {
        return decodeURIComponent(unescape($gonten_str.toLowerCase()));
    }
}


//获取url参数内容（key不区分大小写 value区分）
function gtUrl_sensitive(gonten) {
    var url = window.location.href;
    var u = url.split("?");
    var s, ss, str;
    if (u.length === 1) s = '';
    else s = u[1];
    ss = s.split("&");
    gonten = gonten.toLowerCase();
    str = gonten + "=";
    var j = ss.length;
    for (i = 0; i < j; i++) {
        var kvp = ss[i].split("=");
        if (kvp[0].toLowerCase() === gonten) {
            $gonten_str = ss[i].substring(kvp[0].length + 1);
            break;
        } else {
            $gonten_str = '';
        }
    }
    try {
        return decodeURI($gonten_str);

    } catch (e) {
        return decodeURIComponent(unescape($gonten_str));
    }
}

//Ajax请求基类
function AjaxBase(setting, SucCallBack) {
    var type = setting.type || "GET";
    var url = setting.url;
    var data = setting.data || {};
    var async = false;
    if (setting.async)
        async = true;
    var dataType = setting.dataType || "json";

    var beforeSend = function () {
        if (typeof setting.beforeSend === "function")
            setting.beforeSend();
    }

    $.ajax({
        type: type,
        url: url,
        data: data,
        headers: {
            'Authorization': 'Bearer ' + Cookies.get('jwt'),
        },
        async: async,
        dataType: dataType,
        beforeSend: beforeSend,
        success: function (result) {
            SucCallBack(result);
        },
        error: function (xhr, err, ex) {
            ErrorBox("服务器异常, 请稍后再试!");
            console.error(err);
            console.error(ex);
        }
    });
}

//返回Json格式的Ajax请求
function AjaxJSON(setting, SucCallBack) {
    AjaxBase(setting, function (result) {
        if (typeof result !== "object") {
            ErrorBox("远程调用解析失败");
            return;
        } else {
            SucCallBack(result);
        }
    });
}

//用于增删改的Ajax请求（封装了错误码的处理）
function AjaxAPI(setting, SucCallBack) {
    AjaxJSON(setting, function (result) {
        if (result.rs === "success") {
            SucCallBack(result);
        } else {
            var err = gApiErrCodeTransfer(result.msg);
            ErrorBox(err);
            return;
        }
    });
}

//ajax调用接口的错误码解析
function gApiErrCodeTransfer(err) {
    var str = '';
    switch (err) {
        case '0': str = "列表查询结果为空"; break;
       
        default: str = err; break;
    }
    return str;
}

function IsContainSpecialCharacter(s) {
    var containSpecial = RegExp(/[(\ )(\~)(\!)(\@)(\#)  (\$)(\%)(\^)(\&)(\*)(\()(\))(\-)(\_)(\+)(\=)  (\[)(\])(\{)(\})(\|)(\\)(\;)(\:)(\')(\")(\,)(\.)(\/)  (\<)(\>)(\?)(\)]+/);
    return (containSpecial.test(s));
}

//判断是否为空
function isEmpty(obj) {
    if(!obj && obj !== 0 && obj !== '') {　　　　　　　　
      return true;
    }
    if(Array.prototype.isPrototypeOf(obj) && obj.length === 0) { 　
        return true;
    }
    if(Object.prototype.isPrototypeOf(obj) && Object.keys(obj).length === 0) {
        　　　　
        return true; 　
    } 　　　
    return false; 
}

//日期格式
function formatDate(date, fmt) {
    if (/(y+)/.test(fmt)) {
        fmt = fmt.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length));
    }
    let o = {
        'M+': date.getMonth() + 1,
        'd+': date.getDate(),
        'h+': date.getHours(),
        'm+': date.getMinutes(),
        's+': date.getSeconds()
    };
    for (let k in o) {
        if (new RegExp(`(${k})`).test(fmt)) {
            let str = o[k] + '';
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? str : padLeftZero(str));
        }
    }
    return fmt;
};

function padLeftZero(str) {
    return ('00' + str).substr(str.length);
}