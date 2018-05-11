package com.guse.platform.common.utils;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
@Component
public class ApiAddressUtil {
	private static final Logger logger = LoggerFactory.getLogger(ApiAddressUtil.class);
	/** 大厅服务接口名 */
	public static final String HALL_SERVICE = "hall_service";
	/** 龙珠探宝服务接口名 */
	public static final String LZTB_SERVICE = "lztb_service";
	/** 五星宏辉（疯狂宝石）服务接口名 */
	public static final String WXHH_SERVICE = "WxhhGame_service";
	/** 水浒传服务接口名 */
	public static final String SHZ_SERVICE = "ShzGame_service";
	/** 糖果派对服务接口名 */
	public static final String TGPD_SERVICE = "CandyGame_service";
	/** 捕鱼达人服务接口名 */
	public static final String BYDR_SERVICE = "BydrGame_service";

	/** 公用密匙 */
	public static final String SID = "14d1d10adb8fb4a380966d55c145498320b9596b";
	
	/**GM-账号信息 */
	public static final String MID_PLAYER_INFO = "10000";
	
	/** 配置管理-大厅-跑马灯列表接口ID */
	public static final String MID_CONF_HELL_MARQUEE_LIST = "10004";
	/** 配置管理-大厅-跑马灯保存接口ID */
	public static final String MID_CONF_HELL_MARQUEE_SAVE = "10003";
	
	/** 配置管理-大厅-邮件列表接口ID */
	public static final String MID_CONF_HELL_EMAIL_LIST = "10005";
	/** 配置管理-大厅-邮件发送接口ID */
	public static final String MID_CONF_HELL_EMAIL_SEND = "10006";
	
	/** 配置管理-大厅-公告列表接口ID */
	public static final String MID_CONF_HELL_NOTICE_LIST = "10007";
	/** 配置管理-大厅-公告保存接口ID */
	public static final String MID_CONF_HELL_NOTICE_SAVE = "10008";

	/** GM-账号冻结 */
	public static final String MID_PLAYER_FROZEN = "10001";

	/** 配置管理-中奖展示配置-糖果派对配置列表接口ID */
	public static final String MID_CONF_WINNING_TGPD_LIST = "20000";
	
	/** 配置管理-中奖展示配置-糖果派对配置保存接口ID */
	public static final String MID_CONF_WINNING_TGPD_SAVE = "20001";
	
	/** 配置管理-中奖展示配置-五星宏辉配置列表接口ID */
	public static final String MID_CONF_WINNING_WXHH_LIST = "21012";
	/** 配置管理-中奖展示配置-五星宏辉配置保存接口ID */
	public static final String MID_CONF_WINNING_WXHH_SAVE = "21011";
	
	
	/** 配置管理-中奖展示配置-龙珠探宝配置列表接口ID */
	public static final String MID_CONF_WINNING_LZTB_LIST = "20000";
	/** 配置管理-中奖展示配置-龙珠探宝配置保存（添加、修改、删除）接口ID */
	public static final String MID_CONF_WINNING_LZTB_SAVE = "20001";
	
	/** 配置管理-中奖展示配置-水浒传配置列表接口ID */
	public static final String MID_CONF_WINNING_SHZ_LIST = "20011";
	/** 配置管理-中奖展示配置-水浒传配置保存（添加、修改、删除）接口ID */
	public static final String MID_CONF_WINNING_SHZ_SAVE = "20011";
	
	/** 配置管理-中奖展示配置-捕鱼达人配置列表接口ID */
	public static final String MID_CONF_WINNING_BYDR_LIST = "2029";
	/** 配置管理-中奖展示配置-捕鱼达人配置保存（添加、修改、删除）接口ID */
	public static final String MID_CONF_WINNING_BYDR_SAVE = "2029";
	
	/** GM-道具增减 */
	public static final String MID_PROPS_ADDORDEL = "10002";
	/** GM-删除龙珠探宝评论 */
	public static final String MID_LZTB_FORBID_DEL = "20012";
	/** GM-查询龙珠探宝评论 */
	public static final String MID_LZTB_FORBID_WORDS = "20013";
	
	/** GM-删除五星宏辉评论 */
	public static final String MID_WXHH_FORBID_DEL = "21016";
	/** GM-查询五星宏辉评论 */
	public static final String MID_WXHH_FORBID_WORDS = "21016";
	
	/** GM-删除大厅评论 */
	public static final String MID_HALL_FORBID_DEL = "20013";
	/** GM-查询大厅评论 */
	public static final String MID_HALL_FORBID_WORDS = "20013";
	
	/** GM-删除糖果派对评论 */
	public static final String MID_TGPD_FORBID_DEL = "20012";
	/** GM-查询糖果派对评论 */
	public static final String MID_TGPD_FORBID_WORDS = "20013";
	
	@Autowired
	private  PropertyConfigurer configurer;
	
	/** 动态获取大厅和龙珠探宝的地址 */
    public  String getIpAddress(String service) {
    	String ipAddress1 = configurer.getProperty("ipAddress1");
    	String ipAddress2 = configurer.getProperty("ipAddress2");
        String result = null;
        try {
        	result = HttpClientUtil.httpGet(ipAddress1 + "?service=" + service + "&mid=1002");
		} catch (Exception e) {
			logger.error("获取接口地址异常{}",e);
		}
        logger.info("获取接口地址:{}",result);
        String address = "";
        if (result != null && result != "") {
            //JSON格式转换
            JSONObject obj = JSONObject.parseObject(result);
            String serverNode = obj.getString("serverNode");

            JSONArray jArray = JSONArray.parseArray(serverNode);
            if (null != jArray) {
                Object tmp = jArray.get(0);
                JSONObject tmp2 = JSONObject.parseObject(tmp.toString());
                String server_ip = tmp2.getString("server_ip");
                String port = tmp2.getString("server_port_http");
                address = server_ip + ":" + port;
            }
        } else {
            result = HttpClientUtil.httpGet(ipAddress2 + "?service=" + service + "&mid=1002");
            if (result != null && result != "") {
                //JSON格式转换
                JSONObject obj = JSONObject.parseObject(result);
                String serverNode = obj.getString("serverNode");
                JSONObject serverNodeObj = JSONObject.parseObject(serverNode);
                String port = serverNodeObj.getString("server_port_http");
                address = ipAddress2 + ":" + port;
            }
        }
        if(StringUtils.isEmpty(address)){
        	throw new RuntimeException("空地址");
        }
        if(!address.startsWith("http://")){
        	address = "http://"+address;
        }
        return address;
    }
}
