package com.weixin.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.weixin.entity.Unifiedorder;

public class WXImageUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(WXImageUtil.class);
	
	public static void main(String[] args) {
		WXImageUtil util = new WXImageUtil();
		String url = util.getWeixinPayUrl("房卡100张", "房卡100张", 1, "NO1234987");
		System.out.println(url);
	}
	
	/**
	 * 微信支付下订单
	 * @param desc 商品详情
	 * @param title 商品名称
	 * @param totalAmount 商品金额(单位是分)
	 * @param attachData 附加信息，回调时返回。推荐填入订单号
	 * @return
	 */
	public String getWeixinPayUrl(String desc, String title, Integer totalAmount,
			String attachData) {
		String passback_params="";
        try {
            passback_params = URLDecoder.decode(attachData,"UTF-8"); //附加数据
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //参数组
        String appid = WeiXinConstants.APPID;
        String mch_id = WeiXinConstants.MCH_ID;
        String nonce_str = RandCharsUtils.getRandomString(16);
        String body = title;           //商品描述
        String detail = desc;             //商品详情
        String attach = passback_params;      //附加数据   自定义数据
        String out_trade_no = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+(int)(Math.random()*90000+10000);           //订单号
        int total_fee = totalAmount;//单位是分，即是0.01元            //订单总金额，单位为分
        String spbill_create_ip = "127.0.0.1";  //用户端实际ip
//        String spbill_create_ip = "182.150.137.245";
        String time_start = RandCharsUtils.timeStart();
        String time_expire = RandCharsUtils.timeExpire();
        String notify_url = WeiXinConstants.NOTIFY_URL;              //接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数。
        String trade_type = WeiXinConstants.TRADE_TYPE_NATIVE;//"MWEB";//
//        String trade_type = "MWEB";
        String device_info = WeiXinConstants.DEVICE_INFO_WEB;

        //参数：开始生成签名
        SortedMap<Object,Object> parameters = new TreeMap<Object,Object>();
        parameters.put("device_info", device_info);
        parameters.put("appid", appid);
        parameters.put("mch_id", mch_id);
        parameters.put("nonce_str", nonce_str);
        parameters.put("body", body);
        parameters.put("detail", detail);
        parameters.put("attach", attach);
        parameters.put("out_trade_no", out_trade_no);
        parameters.put("total_fee", total_fee);
        parameters.put("time_start", time_start);
        parameters.put("time_expire", time_expire);
        parameters.put("notify_url", notify_url);
        parameters.put("trade_type", trade_type);
        parameters.put("spbill_create_ip", spbill_create_ip);
        // 生成签名
        String sign = WXSignUtils.createSign("UTF-8", parameters);

        Unifiedorder unifiedorder = new Unifiedorder();
        unifiedorder.setDevice_info(device_info);
        unifiedorder.setAppid(appid);
        unifiedorder.setMch_id(mch_id);
        unifiedorder.setNonce_str(nonce_str);
        unifiedorder.setSign(sign);
        unifiedorder.setBody(body);
        unifiedorder.setDetail(detail);
        unifiedorder.setAttach(attach);
        unifiedorder.setOut_trade_no(out_trade_no);
        unifiedorder.setTotal_fee(total_fee);
        unifiedorder.setSpbill_create_ip(spbill_create_ip);
        unifiedorder.setTime_start(time_start);
        unifiedorder.setTime_expire(time_expire);
        unifiedorder.setNotify_url(notify_url);
        unifiedorder.setTrade_type(trade_type);

        //构造xml参数
        String xmlInfo = HttpXmlUtils.xmlInfo(unifiedorder);
        logger.info(xmlInfo);

        String wxUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder";
        String method = "POST";
        String weixinPost = HttpXmlUtils.httpsRequest(wxUrl, method, xmlInfo).toString();

        JSONObject result = ParseXMLUtils.jdomParseXml(weixinPost);
        System.out.println("result:"+result);
        String codeurl = result.getString("code_url");
//        String codeurl = result.getString("mweb_url");
        return codeurl;
	}
	/**
	 * 生成微信支付二维码
	 * @param codeurl 下订单之后的code_url
	 * @param basePath 生成图片的路径
	 * @param imageName 图片的名称
	 * @return
	 */
	public String generateQrcode(String codeurl) {
		String uploadPath = WeiXinConstants.UPLOADPATH;
	    File foldler = new File(uploadPath);
	    if(!foldler.exists()) {
	        foldler.mkdirs();
	    }
	    String f_name = System.currentTimeMillis() + ".png";
	    try{
	    	
	        File f = new File( uploadPath, f_name);
	        FileOutputStream fio = new FileOutputStream(f);
	        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
	        Map hints = new HashMap();
	        hints.put(EncodeHintType.CHARACTER_SET,"UTF-8");//设置字符集编码类型
	        BitMatrix bitMatrix = null;
	        bitMatrix = multiFormatWriter.encode(codeurl, BarcodeFormat.QR_CODE, 300,300,hints);
	        BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);
	        //输出二维码图片流
	        ImageIO.write(image,"png", fio);
	        return("/lztb/static/qrcode/"+ f_name);
	    }catch(Exception e1) {
	        e1.printStackTrace();
	        return null;
	    }    
	}
}
