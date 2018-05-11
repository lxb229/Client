package com.alipay.config;

import java.io.FileWriter;
import java.io.IOException;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *修改日期：2017-04-05
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipayConfig {
	
//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

	// 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
	public static String app_id = "2018011701925900";
	
	// 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCFBMjDHJ46z57/C1ZOa/YFTC1zrMUZq4NPMQ3umSbiha3BMSaIsUxHoX2WbeLoM+To3mXjnUeB/L0hq2yjKXrdfd0JstEhm8Pf5xHz4beHxtLQW4QsmE+VA1Fp7gAL45JGA8ODPwYvVKrfwo0Uywb2UHxDvIZ+5rPmbV1BKlW9OCgWVW2BBnYx1Yf8wjLEtqDb1WsIWRqM43ouMFebiAc5MYavKYgfKAzS1jBuF0TuNul/iQvUkQphZzTPV4NDAZHHRNMj/+cuIJV7a7w2C9+KpyFuMLmPyYSXEEPz4opLLf3GO58KkWD3N5L9B5XSdkpyLlZ2HbLJfI3kyc8/bGtFAgMBAAECggEAC50hUje7cPMTtLzuqiYu8sN17HWXN+iLrGxiBQUGKhz10LpT3Pdt64skBfBOMWvmfpZ8WuUuiyDf+oKXF5/VhGUq2V/tdnu9HF12ac65UXeAjISRA7vApwgFIbSP+HnEGazWcy0bgyJnUOYRn1EiqHrafqeFLqtB5cqCq9/gYIbW+jl9KYsq0SulIgl4aoTSqkqHZS/pnOegRfop8RDIJl9WgZiH8OGx63sj6SvI1lQKiEYHkbSJ5+8FCFVe5NL8wIGau3ssewxEqETEasG8GrAuqFLtU1+qEgMeaDFKSJ4Mjr7nqZYlI9Rxp2tVTwxWExrL61rSTsU1ehclyC6WAQKBgQD5EISo6k4Qd5a1pviX65564TILNyia8lRHZH3vNavr5rhxnah55fces9QcqBUnC+4Mlfflmy2Doo3qYWorUuNDpV/6Jz/+asRqkq0xFLxStW5spsJ2KTCyqnlbdXt2BMRfeQzIuR3QnrYPStJ9cF0yqYUiUJp1stsuH4X8bmkdwQKBgQCIuQV/pMD4oyObOuqxIPrs12tf1HERD3QIkkorDLTFXtgR5RN22dKVTh3A5txbLBDPefYWwZJAe7KulegCYxq+uErjUX2bWovfiyzATVPlZfxtLErYXsSZp/7GV/CtSeMrBEgNU223F2I4RXItTeQ//Cl4l2LzpOyGi+F911l2hQKBgGtcPXL6nO/oPOTMtrf17viU/hdrjWtNLhsgLFDi19r2N2dySF9TS7TGW5aGtaeBMvnl41fsDjCB756qozNNpX1RWg/gv2sR5tzxoQpZH9yfLJxVPdnlY4KAjZNpDvEOmP92lJYphbqZz2zEGfvMZcAi5qqmCA7YLDazi/QGO/WBAoGAILJmzzzebVfOdxwi7lNdzvBBnHxYweS5MieSFzOFs8HX2Ie5HO5AvzYcRXqwbhlet8+J+601RbiPZdMx1ghPaBb4yd4+NL31v3MwM5kKnEzEjnNOPuqDB+A2PMbhJwAJlMsEVDOEhInSEnUBNjZvhKHQZSeHIY9qhU7EkWzD12UCgYAIO1DE1QPgs1hOUPdHlDcIjhAzyVJsZj0bSgA9EH5TY6L7NVihTVnGmkmzUA2FHmRAuGoefAr67Bzl5K90IQi6KMiEPtez8J4qmwiFDJ9otkvY7yU9Cw7wutxQ1U+M4sdy0001LMk+P3J4IQysh23yrNgPLaGjXe+L5YbykQeMHA==";
	
	// 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
//    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhQTIwxyeOs+e/wtWTmv2BUwtc6zFGauDTzEN7pkm4oWtwTEmiLFMR6F9lm3i6DPk6N5l451Hgfy9Iatsoyl63X3dCbLRIZvD3+cR8+G3h8bS0FuELJhPlQNRae4AC+OSRgPDgz8GL1Sq38KNFMsG9lB8Q7yGfuaz5m1dQSpVvTgoFlVtgQZ2MdWH/MIyxLag29VrCFkajON6LjBXm4gHOTGGrymIHygM0tYwbhdE7jbpf4kL1JEKYWc0z1eDQwGRx0TTI//nLiCVe2u8NgvfiqchbjC5j8mElxBD8+KKSy39xjufCpFg9zeS/QeV0nZKci5Wdh2yyXyN5MnPP2xrRQIDAQAB";
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvPUFDo94K+ykFe5dr5uPT92bvRrwl4sy42NZtICy3BgcTkUbFGvUOqlOBfGUgUJgmspB1wB2B/6eua48lH1Tn/vXD3zvyjWRSD0sSrI2+6JM2RIbRNxgrZkN8hMKA8ALfQ3m3qvGbLSY6aBUx6gJsekNq0ApGgPw6WsXqKDvA66TCgbdOiig2iuE1BnaSdMGb/k5hb0bU3EZjRZs5ObRGZ9VWJ+gYxI2cqlnrARreO0OqFssyoSc//lT3VARotkN+zIHfV4qds+GbTT8Q7Sqk72YFiD4/hMf5cGW+ej5VaAmL9s74YQQcWVbmoMIEZHeP6+83VCAwerQQC6WnXefjQIDAQAB";
	// 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String notify_url = "http://ddmj.wolfsgame.com/notify_url.jsp";
//    public static String notify_url = "http://118.31.66.39:8081/notify_url.jsp";
    

	// 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String return_url = "http://ddmj.wolfsgame.com/return_url.jsp";
//    public static String return_url = "http://118.31.66.39:8081/return_url.jsp";
    
    public static String phone_notify_url = "http://ddmj.wolfsgame.com/alipay/phoneNotifyUrl";
    public static String phone_return_url = "http://ddmj.wolfsgame.com/alipay/phoneReturnUrl";
    public static String product_code = "QUICK_WAP_PAY";
	// 签名方式
	public static String sign_type = "RSA2";
	
	// 字符编码格式
	public static String charset = "utf-8";
	
	// 支付宝网关
	public static String gatewayUrl = "https://openapi.alipay.com/gateway.do";
	
	// 支付宝网关
	public static String log_path = "C:\\";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /** 
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis()+".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

