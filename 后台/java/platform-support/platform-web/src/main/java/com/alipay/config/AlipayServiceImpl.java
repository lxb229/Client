package com.alipay.config;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.ZhimaCustomerCertificationInitializeModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.request.ZhimaCustomerCertificationInitializeRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.ZhimaCustomerCertificationInitializeResponse;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 个人主页 Service 实现
 *
 * Created by yal on 17/11/7.
 */

public class AlipayServiceImpl {
    private static final Logger logger = LoggerFactory.getLogger(AlipayServiceImpl.class);

    private String APP_ID = AlipayContants.appid;
    private String APP_PRIVATE_KEY = AlipayContants.appPrivateKey;
    private String ALIPAY_PUBLIC_KEY = AlipayContants.alipayPublicKey;
    private String SIGN_TYPE = AlipayContants.signType;
    private String CHARSET = AlipayContants.charset ;
    private String URL = AlipayContants.url;
    private String configVersion = "2017-12-20";
    public static String LOCK = "alipaylock";
    
    public static void main(String[] args) {
    	AlipayServiceImpl impl = new AlipayServiceImpl();
		String payUrl = impl.getAlipayPagePayUrl(null, "测试商品", "房卡100张", "0.01", "房卡100张");
		System.out.println(payUrl);
    }
    
    public String getAlipayPagePayUrl(String token, String desc, String title, String totalAmount, String body) {
        logger.info("######################进入支付宝 网页支付 方法################");
        if(!checkOrderAmount(totalAmount, body)){
            return "数据不合法2";
        }
        AlipayClient alipayClient = new DefaultAlipayClient(URL, APP_ID, APP_PRIVATE_KEY, "json", CHARSET, ALIPAY_PUBLIC_KEY, SIGN_TYPE); //获得初始化的AlipayClient
//        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();//创建API对应的request
        
        AlipayTradeAppPayRequest  alipayRequest = new AlipayTradeAppPayRequest();
        
        alipayRequest.setReturnUrl("http://120.78.231.106:8080/gs-cloud-web-order/alipay/returnUrl");
        alipayRequest.setNotifyUrl("http://120.78.231.106:8080/gs-cloud-web-order/alipay/notifyUrl");//在公共参数中设置回跳和通知地址
//        alipayRequest.setReturnUrl("http://www.wolfsgame.com/notify_url.jsp");
//        alipayRequest.setNotifyUrl("http://www.wolfsgame.com/return_url.jsp");
        
        JSONObject js = new JSONObject();
        js.put("out_trade_no",new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+(int)(Math.random()*90000+10000));
        js.put("total_amount",totalAmount);
        js.put("subject",title);
        js.put("product_code","QUICK_WAP_PAY");
//        js.put("product_code","FAST_INSTANT_TRADE_PAY");
        
//        try {
            js.put("body", body);//URLEncoder.encode(body,"UTF-8")网页版编码后，会出错
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
        alipayRequest.setBizContent(js.toString());
//        alipayRequest.setBizContent("{" +
//                " \"out_trade_no\":\"20150320010101002\"," +
//                " \"total_amount\":\"88.88\"," +
//                " \"subject\":\"Iphone6 16G\"," +
//                " \"product_code\":\"QUICK_WAP_PAY\"" +
//                " }");//填充业务参数
        String form="";
        try {
//        	AlipayTradePagePayResponse response = alipayClient.pageExecute(alipayRequest);
//        	System.out.println(response);
            form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return form;
    }
    
    
    /**
     * IOS用的支付方法
     * @param token
     * @param desc
     * @param title
     * @param totalAmount
     * @param body
     * @return
     */
//    public String getIOSAlipayPayUrl(String token, String desc, String title, String totalAmount, String body){
//        String tmpBody = null;
//        try {
//            tmpBody = URLEncoder.encode(body,"UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        return getAlipayPayUrl(token, desc, title, totalAmount, tmpBody);
//    }
        /**
         * 取下订单的URL
         *
         * @param token
         * @param desc
         * @param title
         * @param totalAmount
         * @return
//         */
//    public String getAlipayPayUrl(String token, String desc, String title, String totalAmount, String body){  //Map<String,Object> body
//        logger.info("进入支付宝getAlipayPayUrl方法!!!!!!!!!!!!!!");
//        if(!checkOrderAmount(totalAmount, body)){
//           return GsResult.error("数据不合法");
//        }
//        //实例化客户端
//        AlipayClient alipayClient = new DefaultAlipayClient(URL, APP_ID, APP_PRIVATE_KEY, "json", CHARSET, ALIPAY_PUBLIC_KEY,SIGN_TYPE);
//        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
//        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
//        //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
//        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
//        //try {
//            model.setPassbackParams(body); //URLEncoder.encode(body,"UTF-8")
////        } catch (UnsupportedEncodingException e) {
////            e.printStackTrace();
////        }
//        ;  //描述信息  添加附加数据
//        model.setBody(desc);//订单备注
//        model.setSubject(title); //商品标题
//        //请保证OutTradeNo值每次保证唯一
//        model.setOutTradeNo(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+(int)(Math.random()*90000+10000));//商家订单编号
//        model.setTimeoutExpress("30m");         //超时关闭该订单时间
//        model.setTotalAmount(totalAmount);      //订单总金额
//        model.setProductCode("QUICK_MSECURITY_PAY");//销售产品码，商家和支付宝签约的产品码，为固定值QUICK_MSECURITY_PAY
//        request.setBizModel(model);
//        request.setNotifyUrl("http://120.78.231.106:8080/gs-cloud-web-order/alipay/notifyUrl");//商户外网可以访问的异步地址
//        try {
//            //这里和普通的接口调用不同，使用的是sdkExecute
//            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
//            //System.out.println("就是orderString 可以直接给客户端请求，无需再做处理: "+response.getBody());//就是orderString 可以直接给客户端请求，无需再做处理。
//            return GsResult.build(0,"ok",response.getBody());
//        } catch (AlipayApiException e) {
//            e.printStackTrace();
//            return GsResult.build(-1,"取下订单URL失败");
//        }
//    }
//    
    /**
     * 检测金额是否正确
     * @return
     */
    private boolean checkOrderAmount(String totalAmount, String body){
        if(StringUtils.isBlank(totalAmount) || StringUtils.isBlank(body)){
            logger.info("totalAmount = "+totalAmount+"   body = "+body);
            return false;
        }
        return true;
//        //logger.info("解码前passback_params = "+body);
//        String passback_params = null;
//        try {
//            passback_params = URLDecoder.decode(body,"UTF-8"); //附加数据
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        Gson gson = new Gson();
//        //logger.info("解码后passback_params = "+passback_params);
//        ServiceOrder so = gson.fromJson(passback_params, ServiceOrder.class);
//        Double reward = so.getReward();   //传值，打赏金额
//        Integer purchaseTime =  so.getPurchaseTime();//购买时长
//
//        logger.info("数据验证方法：body= "+body+"    passback_params="+passback_params+"   so="+so);
//        Service service = serviceRepository.getService(so.getServiceId());
//        Double price = service.getPrice();//服务单价
//        Double totalAmount2 = Arith.add(Arith.mul(price,purchaseTime),reward);//理论总金额。
//
//        if(Double.parseDouble(totalAmount) == totalAmount2 ){
//            return true;
//        }
//        logger.info("totalAmount = "+Double.parseDouble(totalAmount)+"   totalAmount2 = "+totalAmount2);
//        return false;
    }
//    /**
//     * 支付宝交易成功后回调方法
//     * @param paramsStr
//     * @return
//     */
//    public String notifyUrl(String paramsStr){
//        logger.info("支付宝支付结果通知"+paramsStr);
//        try {
//            Gson gson = new Gson();
//            Map<String,String> params = gson.fromJson(paramsStr, Map.class);
//            //验证签名
//            boolean flag = AlipaySignature.rsaCheckV1(params, ALIPAY_PUBLIC_KEY, CHARSET, "RSA2");
//            if(flag){
//                if("TRADE_SUCCESS".equals(params.get("trade_status"))){
//                    String amount = params.get("buyer_pay_amount");     //付款金额
//                    String out_trade_no = params.get("out_trade_no");   //商户订单号
//                    String body = params.get("body");                   //备注
//                    //String trade_no = params.get("trade_no");           //支付宝交易号
//                    ServiceOrder so = null;
//                    try {
//                        String passback_params = URLDecoder.decode(params.get("passback_params"),"UTF-8"); //附加数据
//                        so = gson.fromJson(passback_params, ServiceOrder.class);
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    }
//                    synchronized (LOCK) {
//                        boolean result = orderDao.isOrderExists(out_trade_no);
//                        if (!result) {
//                            //member_id,seller_id,service_id,order_no,price,reward,purchase_time,total_money,state,remark,reserve_time();
//
//                            Service service = serviceRepository.getService(so.getServiceId());
//                            Integer unit = service.getUnit();//单位
//                            so.setPrice(service.getPrice());
////                        if(unit == 1){
////                           so.setPurchaseTime(so.getPurchaseTime()*60); //小时转换成分钟
////                        }
//                            so.setTotalMoney(Double.parseDouble(amount));
//                            so.setOrderNo(out_trade_no);
//                            so.setRemark(body);
//                            orderDao.addServiceOrder(so);
//                        }
//                    }
//                    //先查询数据库中是否已处理过了该订单，，如果已处理，，直接返回成功
//                    //在对业务数据进行状态检查和处理之前，要采用数据锁进行并发控制，以避免函数重入造成的数据混乱。
//                }
//            }
//        } catch (AlipayApiException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return "success";
//    }
//
//    public String returnUrl(String paramsStr){
//        return notifyUrl(paramsStr);
//    }
//
//    /**
//     * 取配置版本号
//     *
//     * @return
//     */
//    public String getConfigVersion() {
//        return GsResult.ok("测试正常，configVersion="+configVersion);
//    }
//
////    public static void main(String args[]) throws UnsupportedEncodingException {
////        HashMap hm = new HashMap();
////        hm.put("memberId","11111");
////        hm.put("sellerId","22222");
////        hm.put("remark","uuuuuuu");
////        hm.put("String","wwwwwww");
////        String hmStr = hm.toString();
////        System.out.println(hmStr);
//////
//////        ServiceOrder so;
//////        Gson gson = new Gson();
//////        //Map<String, Object> map = new HashMap<String, Object>();
//////        //map = gson.fromJson(hmStr, map.getClass());
//////        so = gson.fromJson(hmStr, ServiceOrder.class);
//////
//////
//////
//////
//////
//////
//////        System.out.println(so.getMemberId());
////
////        String params = "{charset='UTF-8', seller_email='smyvtu8931@sandbox.com', subject='购买服务', sign='VaXepLwPDKT56WfUIge/LSWOUwfKVTjzgYa7YN1+n2wetWfisrAdb6dxcCfhdlzQgoRh91fj8/CpzinoLm8YE90IL+7QfJEjGshPStdDIrXyNCBpfYuv9cVVnx2190uGeOPje6Iz3/UuDif6sC9s4d6PgAz9h3JIG0z4UhIhjhQivq+8Tvzzuv3CcE0Y9SZ7eZe0HDcrMlgOA9ay+4IMenEzQUAH4mVj/jVwcMKMEpuGsvdEW9gF2cBZg7tHeEwcJTH0oF+TNWKc985aa39KB6GdnSjXW9wqLX66Diwm4+pZ7Z+azDtdhhfuW0KVzRCSee+hJJYIYw8Zo3wCtkUqCQ==', body='荏苒购买 风流倜傥的测试服务666', buyer_id='2088102172462151', invoice_amount='77.00', notify_id='30755d7f539df831b5198b8a4eeaa44h5q', notify_type='trade_status_sync', trade_status='TRADE_SUCCESS', receipt_amount='77.00', app_id='2016080500175827', buyer_pay_amount='77.00', sign_type='RSA2', seller_id='2088102170115064', gmt_payment='2017-12-26 19:04:58', notify_time='2017-12-26 19:04:59', passback_params='%7Bprice%3D25.0%2C+sellerId%3D25%2C+serviceId%3D1%2C+purchaseTime%3D3%2C+reserveTime%3D1514372682360%2C+reward%3D2.0%2C+totalAmount%3D0.0%7D', version='1.0', out_trade_no='2017122619044770469', total_amount='77.00', trade_no='2017122621001004150200405008', auth_app_id='2016080500175827', buyer_logon_id='how***@sandbox.com', point_amount='0.00'}";
////        //params = new String(params.getBytes("ISO-8859-1"), "utf-8");
////        Gson gson = new Gson();
////        Map<String, String> map = new HashMap<String, String>();
////        map = gson.fromJson(params, map.getClass());
////        //System.out.println(map.toString());
////
////        System.out.println(System.currentTimeMillis());
////
////
////
////
////
////
//////        double price = 0.1;
//////        int purchaseTime = 3;
//////        double reward = 0.02;
//////        System.out.println(0.1*3+0.02);
//////        System.out.println(Arith.add(Arith.mul(price,purchaseTime),reward));
//////        System.out.println(add(0.05,0.01));
//////        System.out.println(6.1*3);
//////        System.out.println(4.015 * 100);
//////        System.out.println(123.3 / 100);
////
////
//////        try {
//////            System.out.println(new String("2017-12-12 12:12:12".getBytes("ISO-8859-1"), "utf-8"));
//////        } catch (UnsupportedEncodingException e) {
//////            e.printStackTrace();
//////        }
////    }
//
//    public static double add(double v1, double v2) {
//        BigDecimal b1 = new BigDecimal(Double.toString(v1));
//        BigDecimal b2 = new BigDecimal(Double.toString(v2));
//        return b1.add(b2).doubleValue();
//    }

//    public static void main(String args[]) throws AlipayApiException {
//        String gatewayUrl = "https://openapi.alipay.com/gateway.do";
//        String APP_ID="2017101909390753";
//        String APP_PRIVATE_KEY="MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDJj8szVe2RlqZIpgWTMP1vS4mG8JmBCWj9tRtlGf6r0eRVV+YHUveqLHJ3t0Py3gEGhL3AtEZ/e3v17Y9mfYX28fHP+pHJKUXs+NmolskgusxCkz1x66dHczC2FI1TtsUpzCElHJYlEdp1VcGK6cv9QYKqeCQA9JMHqIHZ1jk0evzDqARF3QkXivB+JBgqSEta2hxIDCreKbwipFG7LQS03EC/orInZhG8TcBj6zibUbHawmmgIJx25me3kPTYvqr/hpsBGcAqJf0VyBicLi0jUcxsRE799hDcJ3yZSF7VXmBoB4ZT3y8phEKCzGxXmTqU3sgugp2Xl9bY4JPly8fbAgMBAAECggEAY/Lr/m7TuWgM2OWKyix23+ODNOjGFdiGuZg/F2lGggk4wqEtreAUc8xKjtbP2PUBaSwVtCMw+jJPi+nbPCVucsAS+uDzvd67kADuXc03uwUmGLVOuNj0QFf8ha23TxXIuytAHFkKkyNBQwHdUXcR8mmz07hzkg21gwyqJ001XLHl+VC+nntA5Po0RuQQljAT8Jnp/t+E9TBNBFv3OmsJMLg+yDkgCXiF4WJB3FbFeETFaoBvbxdJy5SaU96gubCag9LOgOZgj5Ajfr3y03s1jcjz8EXzYa5DOhS3vZuteEGT+NuO0jNL0j3aiKyfSK2pzh4DnPEkHfyKPKS/AAGIUQKBgQD2AHShmDqTc9IQ3i+/ZliAS3xn99ljXg+2T6emddlIpq+RXet2+4y8k+xBl1NM5crAiC893Jb83n56VYS9/nDZxRNP1vh9m9OCvzLiJNUPmyzI4M9n5Jk7eMcBPojLJ/n4p6LPioIToWUEmrLwUB7VSs+AB/4lk5ZJjJoFN8ujVwKBgQDRwPU3rHhB3g24Fl1ve1lZdmNO8Mp/35jxIgVqjAhZ03X2879z/oSXtPG4iTB541shibvRol42QHWecpk3FilsDCsGEPZpQA///sEP0vpThAdRdVqAGNRr1AY1Sk4OD62hweDJJJFBfRyq7QGFkP6TIzWxt0hTB7waW3ScibqRHQKBgQDmQ0boezVZD/BxGYhX/9SXQhEQwE4bVNAgNi8XAPiBqIRt7RuaI5t4aH2cZ6jxMaa20MJnhTNPPnPkzmeR77hyHqYU9YNjD5jLqWxXgWJtWTJyVPvOo4tKv6KG56Hjh2fT+BXNbjiTZ17HEIatqlEyFPRlQxYA1kS2U4VXnQJfwQKBgEJB77QceQhlenPyLKWwkzLdYNVo3q6yFP/mvUtA3MpzL8pIiOu9NGkvsUOXooZ+OSVI+tlnrtb4Wa8piBiMmZ0V4ulRpmTs5aD5+kZjYMvM4vrqVSIttbwyB5wB0tJCRZjZQ4aGVJQZT1r27wogGSqlbqqquN/xi4vP4/Am2ZZJAoGAJUiG52QeAos4EAFnookGSnwWVyLK4AG+GRb5yBKzFTuI3Jgvws6tjQ65esdDJzYOwIuFCQvDHwlxjQaUOKdQIGsKFTMRqbhFxDGpV925QGPZxAkpaYZC3oGZv1Gzos6YVFBCSHijtLphtwjKxHbbCaRVDql17J5Hx6a9o50Ebqo=";
//        String ALIPAY_PUBLIC_KEY="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvPUFDo94K+ykFe5dr5uPT92bvRrwl4sy42NZtICy3BgcTkUbFGvUOqlOBfGUgUJgmspB1wB2B/6eua48lH1Tn/vXD3zvyjWRSD0sSrI2+6JM2RIbRNxgrZkN8hMKA8ALfQ3m3qvGbLSY6aBUx6gJsekNq0ApGgPw6WsXqKDvA66TCgbdOiig2iuE1BnaSdMGb/k5hb0bU3EZjRZs5ObRGZ9VWJ+gYxI2cqlnrARreO0OqFssyoSc//lT3VARotkN+zIHfV4qds+GbTT8Q7Sqk72YFiD4/hMf5cGW+ej5VaAmL9s74YQQcWVbmoMIEZHeP6+83VCAwerQQC6WnXefjQIDAQAB";
//
//
//        AlipayClient alipayClient = new DefaultAlipayClient(gatewayUrl, APP_ID, APP_PRIVATE_KEY, "json", "UTF-8", ALIPAY_PUBLIC_KEY, "RSA2");
//        ZhimaCustomerCertificationInitializeRequest request = new ZhimaCustomerCertificationInitializeRequest();
//        ZhimaCustomerCertificationInitializeModel model = new ZhimaCustomerCertificationInitializeModel();
//        //商户请求的唯一标志
//        model.setTransactionId("201708111421522133231111");
//        //芝麻认证产品码
//        model.setProductCode("11123324");
//        //认证场景码,FACE:人脸认证
//        model.setBizCode("FACE");
//        //值为一个json串,必须包含身份类型identity_type,不同的身份类型需要的身份信息不同，详细介绍，请参考文档
//        model.setIdentityParam("{\"identity_type\":\"CERT_INFO\",\"cert_type\":\"IDENTITY_CARD\",\"cert_name\":\"收委\",\"cert_no\":\"260104197909275964\"}");
//        request.setBizModel(model);
//        ZhimaCustomerCertificationInitializeResponse response = alipayClient.execute(request);
//        System.out.println(response.getBody());
//        System.out.println("bizNo:" + response.getBizNo());
//        if(response.isSuccess()){
//            System.out.println("调用成功");
//        } else {
//            System.out.println("调用失败");
//        }
//
////        //开始认证
////        zhimaCustomerCertificationInitialize  initialize = new zhimaCustomerCertificationInitialize();
////        initialize.zhimaCustomerCertificationCertify(response.getBizNo());
//
//
////        String APP_ID="2017101909390753";
////        String APP_PRIVATE_KEY="MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDJj8szVe2RlqZIpgWTMP1vS4mG8JmBCWj9tRtlGf6r0eRVV+YHUveqLHJ3t0Py3gEGhL3AtEZ/e3v17Y9mfYX28fHP+pHJKUXs+NmolskgusxCkz1x66dHczC2FI1TtsUpzCElHJYlEdp1VcGK6cv9QYKqeCQA9JMHqIHZ1jk0evzDqARF3QkXivB+JBgqSEta2hxIDCreKbwipFG7LQS03EC/orInZhG8TcBj6zibUbHawmmgIJx25me3kPTYvqr/hpsBGcAqJf0VyBicLi0jUcxsRE799hDcJ3yZSF7VXmBoB4ZT3y8phEKCzGxXmTqU3sgugp2Xl9bY4JPly8fbAgMBAAECggEAY/Lr/m7TuWgM2OWKyix23+ODNOjGFdiGuZg/F2lGggk4wqEtreAUc8xKjtbP2PUBaSwVtCMw+jJPi+nbPCVucsAS+uDzvd67kADuXc03uwUmGLVOuNj0QFf8ha23TxXIuytAHFkKkyNBQwHdUXcR8mmz07hzkg21gwyqJ001XLHl+VC+nntA5Po0RuQQljAT8Jnp/t+E9TBNBFv3OmsJMLg+yDkgCXiF4WJB3FbFeETFaoBvbxdJy5SaU96gubCag9LOgOZgj5Ajfr3y03s1jcjz8EXzYa5DOhS3vZuteEGT+NuO0jNL0j3aiKyfSK2pzh4DnPEkHfyKPKS/AAGIUQKBgQD2AHShmDqTc9IQ3i+/ZliAS3xn99ljXg+2T6emddlIpq+RXet2+4y8k+xBl1NM5crAiC893Jb83n56VYS9/nDZxRNP1vh9m9OCvzLiJNUPmyzI4M9n5Jk7eMcBPojLJ/n4p6LPioIToWUEmrLwUB7VSs+AB/4lk5ZJjJoFN8ujVwKBgQDRwPU3rHhB3g24Fl1ve1lZdmNO8Mp/35jxIgVqjAhZ03X2879z/oSXtPG4iTB541shibvRol42QHWecpk3FilsDCsGEPZpQA///sEP0vpThAdRdVqAGNRr1AY1Sk4OD62hweDJJJFBfRyq7QGFkP6TIzWxt0hTB7waW3ScibqRHQKBgQDmQ0boezVZD/BxGYhX/9SXQhEQwE4bVNAgNi8XAPiBqIRt7RuaI5t4aH2cZ6jxMaa20MJnhTNPPnPkzmeR77hyHqYU9YNjD5jLqWxXgWJtWTJyVPvOo4tKv6KG56Hjh2fT+BXNbjiTZ17HEIatqlEyFPRlQxYA1kS2U4VXnQJfwQKBgEJB77QceQhlenPyLKWwkzLdYNVo3q6yFP/mvUtA3MpzL8pIiOu9NGkvsUOXooZ+OSVI+tlnrtb4Wa8piBiMmZ0V4ulRpmTs5aD5+kZjYMvM4vrqVSIttbwyB5wB0tJCRZjZQ4aGVJQZT1r27wogGSqlbqqquN/xi4vP4/Am2ZZJAoGAJUiG52QeAos4EAFnookGSnwWVyLK4AG+GRb5yBKzFTuI3Jgvws6tjQ65esdDJzYOwIuFCQvDHwlxjQaUOKdQIGsKFTMRqbhFxDGpV925QGPZxAkpaYZC3oGZv1Gzos6YVFBCSHijtLphtwjKxHbbCaRVDql17J5Hx6a9o50Ebqo=";
////        String ALIPAY_PUBLIC_KEY="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvPUFDo94K+ykFe5dr5uPT92bvRrwl4sy42NZtICy3BgcTkUbFGvUOqlOBfGUgUJgmspB1wB2B/6eua48lH1Tn/vXD3zvyjWRSD0sSrI2+6JM2RIbRNxgrZkN8hMKA8ALfQ3m3qvGbLSY6aBUx6gJsekNq0ApGgPw6WsXqKDvA66TCgbdOiig2iuE1BnaSdMGb/k5hb0bU3EZjRZs5ObRGZ9VWJ+gYxI2cqlnrARreO0OqFssyoSc//lT3VARotkN+zIHfV4qds+GbTT8Q7Sqk72YFiD4/hMf5cGW+ej5VaAmL9s74YQQcWVbmoMIEZHeP6+83VCAwerQQC6WnXefjQIDAQAB";
////
////
////
////        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", APP_ID, APP_PRIVATE_KEY, "json","UTF-8", ALIPAY_PUBLIC_KEY, "RSA2");
////        AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
////        request.setCode("2e4248c2f50b4653bf18ecee3466UC18");
////        request.setGrantType("authorization_code");
////        try {
////            AlipaySystemOauthTokenResponse oauthTokenResponse = alipayClient.execute(request);
////            System.out.println(oauthTokenResponse.getAccessToken());
////        } catch (AlipayApiException e) {
////            //处理异常
////            e.printStackTrace();
////        }
//
//
////        String APP_ID="2017101909390753";
////        String APP_PRIVATE_KEY="MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDJj8szVe2RlqZIpgWTMP1vS4mG8JmBCWj9tRtlGf6r0eRVV+YHUveqLHJ3t0Py3gEGhL3AtEZ/e3v17Y9mfYX28fHP+pHJKUXs+NmolskgusxCkz1x66dHczC2FI1TtsUpzCElHJYlEdp1VcGK6cv9QYKqeCQA9JMHqIHZ1jk0evzDqARF3QkXivB+JBgqSEta2hxIDCreKbwipFG7LQS03EC/orInZhG8TcBj6zibUbHawmmgIJx25me3kPTYvqr/hpsBGcAqJf0VyBicLi0jUcxsRE799hDcJ3yZSF7VXmBoB4ZT3y8phEKCzGxXmTqU3sgugp2Xl9bY4JPly8fbAgMBAAECggEAY/Lr/m7TuWgM2OWKyix23+ODNOjGFdiGuZg/F2lGggk4wqEtreAUc8xKjtbP2PUBaSwVtCMw+jJPi+nbPCVucsAS+uDzvd67kADuXc03uwUmGLVOuNj0QFf8ha23TxXIuytAHFkKkyNBQwHdUXcR8mmz07hzkg21gwyqJ001XLHl+VC+nntA5Po0RuQQljAT8Jnp/t+E9TBNBFv3OmsJMLg+yDkgCXiF4WJB3FbFeETFaoBvbxdJy5SaU96gubCag9LOgOZgj5Ajfr3y03s1jcjz8EXzYa5DOhS3vZuteEGT+NuO0jNL0j3aiKyfSK2pzh4DnPEkHfyKPKS/AAGIUQKBgQD2AHShmDqTc9IQ3i+/ZliAS3xn99ljXg+2T6emddlIpq+RXet2+4y8k+xBl1NM5crAiC893Jb83n56VYS9/nDZxRNP1vh9m9OCvzLiJNUPmyzI4M9n5Jk7eMcBPojLJ/n4p6LPioIToWUEmrLwUB7VSs+AB/4lk5ZJjJoFN8ujVwKBgQDRwPU3rHhB3g24Fl1ve1lZdmNO8Mp/35jxIgVqjAhZ03X2879z/oSXtPG4iTB541shibvRol42QHWecpk3FilsDCsGEPZpQA///sEP0vpThAdRdVqAGNRr1AY1Sk4OD62hweDJJJFBfRyq7QGFkP6TIzWxt0hTB7waW3ScibqRHQKBgQDmQ0boezVZD/BxGYhX/9SXQhEQwE4bVNAgNi8XAPiBqIRt7RuaI5t4aH2cZ6jxMaa20MJnhTNPPnPkzmeR77hyHqYU9YNjD5jLqWxXgWJtWTJyVPvOo4tKv6KG56Hjh2fT+BXNbjiTZ17HEIatqlEyFPRlQxYA1kS2U4VXnQJfwQKBgEJB77QceQhlenPyLKWwkzLdYNVo3q6yFP/mvUtA3MpzL8pIiOu9NGkvsUOXooZ+OSVI+tlnrtb4Wa8piBiMmZ0V4ulRpmTs5aD5+kZjYMvM4vrqVSIttbwyB5wB0tJCRZjZQ4aGVJQZT1r27wogGSqlbqqquN/xi4vP4/Am2ZZJAoGAJUiG52QeAos4EAFnookGSnwWVyLK4AG+GRb5yBKzFTuI3Jgvws6tjQ65esdDJzYOwIuFCQvDHwlxjQaUOKdQIGsKFTMRqbhFxDGpV925QGPZxAkpaYZC3oGZv1Gzos6YVFBCSHijtLphtwjKxHbbCaRVDql17J5Hx6a9o50Ebqo=";
////        String ALIPAY_PUBLIC_KEY="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvPUFDo94K+ykFe5dr5uPT92bvRrwl4sy42NZtICy3BgcTkUbFGvUOqlOBfGUgUJgmspB1wB2B/6eua48lH1Tn/vXD3zvyjWRSD0sSrI2+6JM2RIbRNxgrZkN8hMKA8ALfQ3m3qvGbLSY6aBUx6gJsekNq0ApGgPw6WsXqKDvA66TCgbdOiig2iuE1BnaSdMGb/k5hb0bU3EZjRZs5ObRGZ9VWJ+gYxI2cqlnrARreO0OqFssyoSc//lT3VARotkN+zIHfV4qds+GbTT8Q7Sqk72YFiD4/hMf5cGW+ej5VaAmL9s74YQQcWVbmoMIEZHeP6+83VCAwerQQC6WnXefjQIDAQAB";
////        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",APP_ID,APP_PRIVATE_KEY,"json","GBK",ALIPAY_PUBLIC_KEY);
////        ZhimaCreditScoreGetRequest request = new ZhimaCreditScoreGetRequest();
////        request.setBizContent("{" +
////                " \"transaction_id\":\"201512100936588040000000465158\"," +
////                " \"product_code\":\"w1010100100000000001\"" +
////                " }");
////        ZhimaCreditScoreGetResponse response = alipayClient.execute(request,accessToken);
////        if(response.isSuccess()){
////            System.out.println("调用成功");
////            // 取得芝麻分
////            System.out.println("芝麻分=" + response.getZmScore());
////        } else {
////            System.out.println("调用失败");
////            // 处理各种异常
////            System.out.println("查询芝麻分错误 code=" + response.getCode());
////        }
//    }
}
//在if（flag）中，我们需要做如下判断：
//        **1、商户需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号
//        2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额）
//        3、校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email）
//        4、验证app_id是否为该商户本身。**
//        在上述验证通过后商户必须根据支付宝不同类型的业务通知，正确的进行不同的业务处理，并且过滤重复的通知结果数据。在支付宝的业务通知中，只有交易通知状态为TRADE_SUCCESS或TRADE_FINISHED时，支付宝才会认定为买家付款成功。