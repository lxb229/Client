package com.guse.apple_reverse.service.socket;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import io.netty.channel.Channel;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import com.google.gson.Gson;
import com.guse.apple_reverse.Main.ServiceStart;
import com.guse.apple_reverse.common.JSONUtils;
import com.guse.apple_reverse.common.MD5Util;
import com.guse.apple_reverse.dao.AppleAccountRecordDao;
import com.guse.apple_reverse.dao.AppleIdTableDao;
import com.guse.apple_reverse.dao.AppleSercurityDao;
import com.guse.apple_reverse.dao.model.AppleAccountRecord;
import com.guse.apple_reverse.dao.model.AppleIdTable;
import com.guse.apple_reverse.dao.model.AppleSercurity;
import com.guse.apple_reverse.netty.handler.session.ClientSession;
import com.guse.apple_reverse.netty.handler.session.ClientSessionUtil;
import com.guse.apple_reverse.netty.handler.session.Message;
import com.guse.apple_reverse.service.ADTQueryService;
import com.guse.apple_reverse.service.ChangeSercurityService;
import com.guse.apple_reverse.service.QueryBillService;
import com.guse.apple_reverse.service.ReverseService;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

/** 
* @ClassName: SocketService 
* @Description: 处理socket服务类
* @author Fily GUSE
* @date 2017年11月24日 上午10:43:34 
*  
*/
public class SocketService {
	// 待处理消息
	private Message msg;
	private Channel channel;
	// 构造方法
	public SocketService(Message msg, Channel channel) {
		this.msg = msg;
		this.channel = channel;
	}
	
	/** 
	* @Title: start 
	* @Description: 开始处理信息 
	* @param 
	* @return void 
	* @throws 
	*/
	public void start() {
		if(msg.getResutlCode() == Message.RESULT_RESP) {
			queryRespond();
			return;
		}
		// 记录当前使用数
		ClientSession client = ClientSessionUtil.getClient(channel);
		if(client != null) {
			int useNum = client.getUseNum();
			client.setUseNum(useNum==0 ? 0 : useNum-1);
		} else {
			ClientSessionUtil.removeClient(channel);
		}
		switch(msg.getType()) {
			case Message.SERVER_QUERY_APPLE:
				queryApple();
				break;
			case Message.SERVER_QUERY_BILL:
				queryBill();
				break;
			case Message.SERVER_QUERY_ALL:
				queryApple();
				break;
			case Message.ADT_QUERY_BALANCE:
				queryAdt();
				break;
			case Message.CHANGE:
				change();
				break;
		}
	}
	
	/** 
	* @Title: returnReceipt 
	* @Description: 处理回执消息 
	* @param 
	* @return void 
	* @throws 
	*/
	private void queryRespond() {
		JSONObject data = JSONObject.fromObject(msg.getData());
		switch(msg.getType()) {
		case Message.SERVER_QUERY_APPLE:
			ReverseService.removeWaitRespond(data.getInt("id"));
			break;
		case Message.SERVER_QUERY_ALL:
			ReverseService.removeWaitRespond(data.getInt("id"));
			break;
		case Message.CHANGE:
			ChangeSercurityService.removeWaitRespond(data.getInt("id"));
			break;
		}
	}
	
	/** 
	* @Title: change 
	* @Description: 修改密码密保 
	* @param 
	* @return void 
	* @throws 
	*/
	public void change() {
		AppleSercurityDao dao = ServiceStart.factory.getBean(AppleSercurityDao.class);
		JSONObject data = JSONObject.fromObject(msg.getData());
		// 获取修改信息
		AppleSercurity apple = dao.getById(data.getInt("id"));
		if(apple != null) {
			try{
			// 查询成功
			if(msg.getResutlCode() == 1) {
				apple.setStatus_comment("成功");
				apple.setStatus(AppleSercurity.STATUS_SUCCESS);
				// 成功数+1
				ChangeSercurityService.queryResultCount(apple.getId(), Message.RESULT_SUCCESS);;
			} else {
				apple.setStatus(AppleSercurity.STATUS_FAIL);
				apple.setStatus_comment(msg.getResultCodeMsg());
				// 失败数+1
				ChangeSercurityService.queryResultCount(apple.getId(), Message.RESULT_FAIL);
			}
			// 处理时间
			apple.setModified_time((int)(new Date().getTime() / 1000));
			// 保存数据库
			dao.updateStatus(apple);
			// 添加处理记录
			ChangeSercurityService.queryResultAdd(apple);
			}catch(Exception e){e.printStackTrace();}
			// 记录处理完成日志
			ServiceStart.INFO_LOG.info("query apple account finish.  id:{}, appleId:{}, status:{}, status_status:{}"
					, apple.getId(), apple.getApple_id(),apple.getStatus(), apple.getStatus_comment());
		}
		// 标记已处理完成
		ChangeSercurityService.markExector(data.getInt("id"));
		
	}
	
	/** 
	* @Title: adtQuery 
	* @Description: 插件查询 
	* @param 
	* @return void 
	* @throws 
	*/
	public void queryAdt() {
		// 查询成功
		if(msg.getResutlCode() == 1) {
			JSONObject json = JSONObject.fromObject(msg.getResultCodeMsg());
			ADTQueryService.setQueryResult(-1,getData(json, "Apple ID 余额", "没有余额信息"));
		} else {
			ADTQueryService.setQueryResult(-1,msg.getResultCodeMsg());
		}
	}
	
	/** 
	* @Title: updateApple 
	* @Description: 查询苹果信息 
	* @param @param msg
	* @return void 
	* @throws 
	*/
	public void queryApple() {
		AppleIdTableDao appleDao = ServiceStart.factory.getBean(AppleIdTableDao.class);
		JSONObject data = JSONObject.fromObject(msg.getData());
		// 获取苹果帐号信息
		AppleIdTable apple = appleDao.getById(data.getInt("id"));
		if(apple != null) {
			try {
			// 查询成功
			if(msg.getResutlCode() == 1) {
				JSONObject json = JSONObject.fromObject(msg.getResultCodeMsg());
				
				AppleAccountRecord record = new AppleAccountRecord();
				record.setId(apple.getId());
				record.setCountry(getData(json, "国家或地区", null)); //国家
				//支付方式
				if(json.get("付款信息") != null) {
					record.setPayment(json.getString("付款信息"));
				} else if(json.get("付款方式") != null) {
					record.setPayment(json.getString("付款方式").split(" ")[0]);
				} else {
					record.setPayment("没有信用卡记录"); 
				}
				record.setFamily_sharing(getData(json, "家人共享", "无")); //家庭共享
				record.setBill_time(getData(json, "上次购买的项目", "无")); //最后一次购买日期
				record.setBalance(getData(json, "Apple ID 余额", null)); //余额
				record.setDebt(getData(json, "欠款", "无")); //是否有欠款
				if(!"无".equals(record.getDebt())) {
					String debt = record.getDebt();
					record.setDebt("true".equals(debt) ? "有" : "无");
				}
				record.setBill("无"); // 是否有账单
				
				record.setAgreement(null); //是否有协议
				record.setQuery_time(new Date().getTime() / 1000); //查询时间
				
				// 保存记录
				ServiceStart.factory.getBean(AppleAccountRecordDao.class).delRecord(record.getId());
				ServiceStart.factory.getBean(AppleAccountRecordDao.class).addRecord(record);
				
				//修改主表状态
				apple.setQuery_status(AppleIdTable.QS_QUERY_SUCCESS);
				apple.setAccount_status("成功");
				
				// 携带账单查询
				if(json.get("purchases") != null) {
					JSONObject purchases = new JSONObject();
					purchases.put("purchases", json.getString("purchases"));
					msg.setResultCodeMsg(purchases.toString());
					queryBill();
				}
				
				// 成功数+1
				ReverseService.queryResultCount(apple.getId(), Message.RESULT_SUCCESS);;
			} else {
				apple.setQuery_status(AppleIdTable.QS_FAILURE);
				apple.setAccount_status(msg.getResultCodeMsg());
				
				// 失败数+1
				ReverseService.queryResultCount(apple.getId(), Message.RESULT_FAIL);
			}
			// 保存数据库
			appleDao.updateQueryStatus(apple);
			// 添加处理记录
			ReverseService.queryResultAdd(apple);
			}catch(Exception e){e.printStackTrace();}
			// 记录处理完成日志
			ServiceStart.INFO_LOG.info("query apple account finish.  id:{}, appleId:{}, query_status:{}, account_status:{}"
					, apple.getId(), apple.getApple_id(),apple.getQuery_status(), apple.getAccount_status());
		}
		// 标记已处理完成
		ReverseService.markExector(data.getInt("id"));
	}
	
	/** 
	* @Title: queryBill 
	* @Description: 查询账单信息 
	* @param @param msg
	* @return void 
	* @throws 
	*/
	public void queryBill() {
		AppleAccountRecordDao recordDao = ServiceStart.factory.getBean(AppleAccountRecordDao.class);
		JSONObject data = JSONObject.fromObject(msg.getData());
		String result = "失败";
		// 查询成功
		if(msg.getResutlCode() == 1) {
			String appleId = data.getString("appleId");
			String context = msg.getResultCodeMsg();
			try {
				// 判断是否有账单
				JSONObject json = JSONObject.fromObject(context);
				if(json.get("purchases") != null) {
					//构造一个带指定Zone对象的配置类
					Configuration cfg = new Configuration(Zone.zone2());
					//...其他参数参考类注释
					UploadManager uploadManager = new UploadManager(cfg);
					//...生成上传凭证，然后准备上传
					String accessKey = "i-1WWsJAIvYQ7lpYxtdZUWqHwsKf9RNBwCDnm6Nl";
					String secretKey = "Qu9hN6i432YW-KeD-OOJ7Z0mUaE7-p14rgbD7xaw";
					String bucket = "billcontent";
					Auth auth = Auth.create(accessKey, secretKey);
					// 文件名,苹果帐号MD5
					String key = MD5Util.string2MD5(appleId) + ".txt";
					try {
						// 上传内容
					    byte[] uploadBytes = context.getBytes("utf-8");
					    // 设置上传空间
					    String upToken = auth.uploadToken(bucket, key);
					    try {
					        Response response = uploadManager.put(uploadBytes,key,upToken);
					        //解析上传成功的结果
					        DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
					        ServiceStart.INFO_LOG.info("已保存id:[{}]的账单信息", data.getInt("id"));
					        // 保存信息
					        AppleAccountRecord record = recordDao.findById(data.getInt("id"));
					        record.setBill_content(putRet.key);
					        // 设置是否有账单
					        record.setBill("无");
					        setBill(record, json.getString("purchases"));
							recordDao.updateBillContent(record);
					    } catch (QiniuException ex) {
					        Response r = ex.response;
					        result = r.toString();
					    }
					} catch (UnsupportedEncodingException ex) { 
						result = ex.getMessage();
					}
				} 
			}catch(Exception e) {
				e.printStackTrace();
				result = e.getMessage();
			}
		} else {
			result = msg.getResultCodeMsg();
		}
		
		QueryBillService.setBillResult(data.getInt("id"), result);
	}
	
	// 获取json里面的数据key对应的值,如果没有就返回默认值
	private String getData(JSONObject json, String key, String defVal) {
		if (json.get(key) != null
				&& StringUtils.isNotBlank(json.getString(key))) {
			defVal = json.getString(key);
		}
		return defVal;
	}
	
	/** 
	* @Title: setBill 
	* @Description: 设置账单信息 
	* @param @param record
	* @param @param data
	* @return void 
	* @throws 
	*/
	private void setBill(AppleAccountRecord record, String data) {
		try {
			JSONArray array = JSONUtils.toJSONArray(data);
			for(int r = 0; r < array.size(); r ++) {
				try {
					JSONObject temp = array.getJSONObject(r);
					String total = temp.getString("total");
					String invoice_date = temp.getString("invoice-date");
					// 判断是否有大于0的消费
					for(int i = 0; i < total.length(); i ++) {
						if(total.charAt(i) > 48 && total.charAt(i) <= 57) {
							record.setBill("有");
							record.setLast_invoice_date(invoice_date);
							return;
						}
					}
				}catch(Exception e){}
			}
		} catch(Exception e) {}
	}
}
