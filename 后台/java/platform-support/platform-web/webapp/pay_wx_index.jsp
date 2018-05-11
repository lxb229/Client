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
    <title>微信支付</title>
    <link href="https://cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">
    <style>
        html,body{
            width: 100%;
            height: 100%;
        }
        .pay{
            width: 990px;
            height: 540px;
            margin: auto;
            background: url("./lztb/static/qrcode/12_bg.png") center center no-repeat;
        }
        .pay-ewm{
            width: 230px;
            height: 230px;
            margin-left: 200px;
            margin-top: 100px;
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
    </style>
</head>
<body>
    <div class="dmui-table">
        <div class="dmui-cell">
            <div class="pay">
                <img class="pay-ewm" src="http://paysdk.weixin.qq.com/example/qrcode.php?data=<%=request.getParameter("payWXImage")%>" alt="">
				<input type="hidden" name="out_trade_no" id="out_trade_no" value="<%=request.getParameter("out_trade_no")%>" />
            </div>
        </div>
    </div>
</body>
<script src="/lztb/static/plugs/jquery/jquery.min.js"></script>
<script type="text/javascript">
window.onload=function() {
	    setInterval("ajaxstatus()", 3000);    
	};
	
	function ajaxstatus() {
		var out_trade_no = document.getElementById("out_trade_no").value;
	    if (out_trade_no != "" && out_trade_no != 0) {
	        $.ajax({
	            url: "/weixin/ajaxstatus?out_trade_no=" + out_trade_no,
	            type: "GET",
	            dataType:"json",
	            data: "",
	            success: function (rs) {
					if (rs.code == '00000') {
						window.location.href = "http://ddmj.wolfsgame.com/pay_wx_success.jsp"; //页面跳转
					}
	            },
	            error: function () {
	            }
	        });
	    }
	}
</script>
</html>