

// 当前资源URL目录
var baseUrl = (function () {
    var scripts = document.scripts, src = scripts[scripts.length - 1].src;
    return src.substring(0, src.lastIndexOf("/") + 1);
})();

// RequireJs 配置参数s
require.config({
    baseUrl: baseUrl,
    waitSeconds: 0,
    map: {'*': {css: '//cdn.bootcss.com/require-css/0.1.8/css.min.js'}},
    paths: {
        // 开源插件 CDN
        'pace': ['//cdn.bootcss.com/pace/1.0.2/pace.min', '../plugs/jquery/pace.min'],
        'socket': ['//cdn.bootcss.com/web-socket-js/1.0.0/web_socket.min'],
        'zeroclipboard': ['//cdn.bootcss.com/zeroclipboard/2.3.0/ZeroClipboard.min'],
        'json': ['//cdn.bootcss.com/json2/20160511/json2.min', '../plugs/jquery/json2.min'],
        'qrcode': ['//cdn.bootcss.com/jquery.qrcode/1.0/jquery.qrcode.min'],
        'print': ['../plugs/jquery/jquery.PrintArea'],
        'base64': ['//cdn.bootcss.com/Base64/1.0.0/base64.min', '../plugs/jquery/base64.min'],
        'jquery': ['//cdn.bootcss.com/jquery/1.12.4/jquery.min', '../plugs/jquery/jquery.min'],
        'jquery.ztree': ['//cdn.bootcss.com/zTree.v3/3.5.28/js/jquery.ztree.all.min', '../plugs/ztree/jquery.ztree.all.min'],
        'jquery.icheck': ['//cdn.bootcss.com/iCheck/1.0.2/icheck.min'],
        'jquery.cookies': ['//cdn.bootcss.com/jquery-cookie/1.4.1/jquery.cookie', '../plugs/jquery/jquery.cookie'],
        'bootstrap': ['//cdn.bootcss.com/bootstrap/3.3.6/js/bootstrap.min', '../plugs/bootstrap/js/bootstrap.min'],
        'bootstrap.typeahead': ['//cdn.bootcss.com/bootstrap-3-typeahead/4.0.2/bootstrap3-typeahead.min'],
        'bootstrap.multiselect': ['//cdn.bootcss.com/bootstrap-multiselect/0.9.13/js/bootstrap-multiselect.min', '../plugs/multiselect/bootstrap-multiselect'],
        'echarts': ['//cdn.bootcss.com/echarts/3.5.4/echarts.common.min', '../plugs/echarts/echarts.common.min'],
        'vueresource':['//cdn.bootcss.com/vue-resource/1.0.3/vue-resource'],
        //'layer': ['//cdn.bootcss.com/echarts/3.5.4/echarts.common.min', '../plugs/layui/layui'],
        
        // 自定义插件
        'admin.plugs': ['plugs'],
        'admin.listen': ['listen'],
        'layui': ['../plugs/layui/layui'],
        'ueditor': ['../plugs/ueditor/ueditor.all'],
        'pcasunzips': ['../plugs/jquery/pcasunzips'],
        'laydate': ['../plugs/layui/laydate/laydate'],
        //
        'jstree':['../plugs/jstree/jstree'],
        'vue':['../plugs/vue/vue'],
        'site':['../model/site'],
        'jquery.step':['../model/jquery.step']
    },
    shim: {
        'laydate': {deps: ['layui','css!../plugs/layui/laydate/skins/molv/laydate.css']},
        'layui': {deps: ['jquery']},
        'socket': {deps: ['//cdn.bootcss.com/swfobject/2.2/swfobject.min.js']},
        'bootstrap': {deps: ['jquery']},
        'pcasunzips': {deps: ['jquery']},
        'bootstrap.multiselect': {deps: ['jquery', 'bootstrap', 'css!//cdn.bootcss.com/bootstrap-multiselect/0.9.13/css/bootstrap-multiselect.css']},
        'jquery.ztree': {deps: ['jquery', 'css!//cdn.bootcss.com/zTree.v3/3.5.28/css/zTreeStyle/zTreeStyle.min.css']},
        'jquery.icheck': {deps: ['jquery', 'bootstrap', 'css!//cdn.bootcss.com/iCheck/1.0.2/skins/square/blue.css']},
        'jquery.cookies': {deps: ['jquery']},
        'admin.plugs': {deps: ['jquery', 'layui']},
        'admin.listen': {deps: ['jquery', 'jquery.cookies', 'admin.plugs']},
        //
        'jstree':{deps: ['jquery','css!../plugs/jstree/themes/default/style.min.css']},
        'jquery.step':{deps: ['jquery']}
    },
    deps: ['css!//cdn.bootcss.com/font-awesome/4.7.0/css/font-awesome.min.css'],
    // 开启debug模式，不缓存资源
    urlArgs: "t=" + (new Date()).getTime()
});

window.WEB_SOCKET_SWF_LOCATION = "//cdn.bootcss.com/web-socket-js/1.0.0/WebSocketMain.swf";
window.UEDITOR_HOME_URL = (window.ROOT_URL ? window.ROOT_URL + '/static/' : baseUrl) + '../plugs/ueditor/';
window.LAYDATE_PATH = baseUrl + '../plugs/layui/laydate/';

// // UI框架初始化
// require(['pace', 'jquery', 'layui', 'bootstrap', 'ueditor', 'jquery.cookies','jstree'], function () {
//     layui.config({dir: baseUrl + '../plugs/layui/'});
//     layui.use(['layer', 'form', 'element'], function () {
//         window.layer = layui.layer;
//         window.form = layui.form();
//         require(['admin.listen']);
//     });
// });