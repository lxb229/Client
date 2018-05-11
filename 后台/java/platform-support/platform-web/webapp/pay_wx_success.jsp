<%
/* *
 *功能：支付宝即时到账交易接口调试入口页面
 *版本：3.4
 *日期：2016-03-08
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 **********************************************
 */
%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="java.net.URLDecoder" %>
<!DOCTYPE html>
<html lang="ch">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width = device-width, initial-scale = 1.0, maximum-scale = 1.0, user-scalable = 0"/>
    <meta name="format-detection" content="telephone=no"/>
    <meta name="format-detection" content="email=no"/>
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta http-equiv="Content-Type" content="text/html">
    <meta name="full-screen" content="yes">
    <meta name="browsermode" content="application">
    <meta name="x5-fullscreen" content="true">
    <meta name="x5-page-mode" content="app">
    <meta name="msapplication-tap-highlight" content="no">
    <meta name="description" content="">
    <meta name="keywords" content="">
    <title>微信支付 - 支付结果</title>
    <link href="https://cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">
    <style>
        html,body{
            width: 100%;
            height: 100%;
            position: relative;
        }
        .dmui-table{
            display: table;
            width: 100%;
            height: 100%;
        }
        .dmui-cell{
            display: table-cell;
            height: 100%;
            vertical-align: middle;
        }
        .close-btn{
            padding: 10px 50px;
            background: #00C800;
            color: #fff;
            border-radius: 6px;
        }
        .title{
            position: absolute;
            left: 0;
            top: 20px;
        }
        .title img{
            width: 30px;
            height: 30px;
        }
        .result-text{
            color: #333;
            font-size: 18px;
            margin: 30px 0 60px;
            clear: both;
        }
        .icon-result{
            clear: both;
        }
        .glyphicon{
            background-color: #00C800;
            border-radius: 100%;
            padding: 16px;
            color: #fff;
            font-size: 20px;
        }
    </style>
</head>
<body class="container">
    <div class="dmui-table">
        <div class="dmui-cell text-center">
                <div class="title">
                    <img src="./lztb/static/qrcode/wx_icon.jpg" alt="">微信支付
                </div>
                <div class="icon-result">
                    <i class="glyphicon glyphicon-ok"></i>
                </div>
                <div class="result-text text-center">支付成功</div>
                <span class="close-btn" onclick="window.opener=null;window.open('','_self');window.close();">关闭</span>
        </div>
    </div>
</body>
</html>