package com.guse.apple_reverse_client.netty.handler.service;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import com.guse.apple_reverse_client.Main.ServiceStart;
import com.guse.apple_reverse_client.common.HttpUtil;
import com.guse.apple_reverse_client.netty.QueryLineService;
import com.guse.apple_reverse_client.netty.handler.Message;
import com.guse.apple_reverse_client.netty.handler.QueryLine;

/**
 * @ClassName: QueryService
 * @Description: 查询服务类
 * @author Fily GUSE
 * @date 2017年11月23日 下午5:50:39
 * 
 */
public class QueryService implements Runnable {

	private QueryLine line; //查询线路
	private Message msg; //处理信息
	
	public QueryService(QueryLine line, Message msg) {
		this.line = line;
		this.msg = msg;
	}
	
	public void run() {
		// 获取查询类型
		String type = ServiceStart.factory.getBean(QueryLineService.class).getQueryType(msg.getType());
		ServiceStart.CONSOLE_LOG.info("start exector port:{}, type:{}, data:{}", line.getPort(), type, msg.getData());
		// 计算时间节点
		Date start = new Date();
		Long exectorOut = 0l;
		// 默认查询失败
		msg.setResutlCode(Message.RESULT_FAIL);
		if(line != null) {
			if(StringUtils.isNotBlank(type)) {
				switch (type) {
				case "change":
					exectorOut = change();
					break;
				default:
					exectorOut = query(type);
					break;
				}
			} else {
				msg.setResultCodeMsg("服务器不支持此类处理");
			}
			// 使用完成后设置查询端口为空闲
			line.setStatus(QueryLine.STATUS_IDLE);
		} else {
			msg.setResultCodeMsg("服务器繁忙");
		}
		// 计算用时
		long allOut = new Date().getTime() - start.getTime();
		ServiceStart.CONSOLE_LOG.info("exector finish. type:{}, result:{}. time-out:{}, exector time-out:{}, data:{}"
				, type, msg.getResutlCode(), allOut, exectorOut, msg.getData());
		// 回执结果
		Message.sendMessage(msg);
	}
	
	/** 
	* @Title: change 
	* @Description: 修改苹果帐号密码密保 
	* @param @return
	* @return long 
	* @throws 
	*/
	private long change() {
		JSONObject data = JSONObject.fromObject(msg.getData());
		data.remove("id");
		// 计算时间节点
		Date eStart = new Date();
		try {
			// 判断处理类型
			Object np = data.get("newpassword");
			Object nq = data.get("new_questions");
			String type = "";
			if(np != null && nq != null) {
				type = "changePasswordAndSecurityQuestions";
			} else if(np != null) {
				type = "changePassword";
			} else if(nq != null) {
				type = "changeSecurityQuestions";
			}
			
			if(StringUtils.isNotBlank(type)) {
				String url = "http://127.0.0.1:" + line.getPort() + "/" + type; // 访问地址
				// 开始查询
				String response = null;
				int count = 0;
				do{
					try {
						ServiceStart.INFO_LOG.info("Http Put. url:{}, data:{}", url, data);
						response = HttpUtil.sendPUT(url, data.toString());
					}catch(Exception e){
						ServiceStart.INFO_LOG.error(e.getMessage());
					}
					count ++;
					if(StringUtils.isBlank(response)) {
						// 10秒后再试
						Thread.sleep(1000 * 10);
					}
				}while(StringUtils.isBlank(response) && count < 5);
				// 多次查询仍然失败
				if(StringUtils.isBlank(response)) {
					throw new Exception("尝试多次仍然失败");
				}
				// 非法数据处理
				replaceBlank(response);
				response = response.replace("\n", "");
				JSONObject json = JSONObject.fromObject(response);
				if(json.get("err") == null) {
					// 设置返回信息
					msg.setResutlCode(Message.RESULT_SUCCESS);
					msg.setResultCodeMsg(response);
				} else {
					msg.setResultCodeMsg(json.getString("err"));
				}
			} else {
				msg.setResultCodeMsg("没有修改信息");
			}
		} catch(Exception e) {
			ServiceStart.CONSOLE_LOG.error(e.getMessage());
			msg.setResultCodeMsg("查询线路繁忙，请稍后再试");
		}
		// 返回处理时间
		return new Date().getTime() - eStart.getTime();
	}
	
	
	/** 
	* @Title: query 
	* @Description: 查询信息 
	* @param 
	* @return void 
	* @throws 
	*/
	private long query(String queryType) {
		JSONObject apple = JSONObject.fromObject(msg.getData());
		String appleId = apple.getString("appleId");
		String applePw = apple.getString("applePw");
		// 计算时间节点
		Date eStart = new Date();
		try {
			String url = "http://127.0.0.1:" + line.getPort() + "/" + queryType; // 访问地址
			String param = "u=" + appleId + "&p=" + applePw; // 查询条件
			// 开始查询
			String response = null;
			int count = 0;
			do{
				try {
					ServiceStart.INFO_LOG.info("Http Get. url:{}, param:{}", url, param);
					response = HttpUtil.sendGet(url, param);
				}catch(Exception e){
					count ++;
					// 10秒后再试
					Thread.sleep(1000 * 10);
				}
			}while(StringUtils.isBlank(response) && count < 5);
			// 多次查询仍然失败
			if(StringUtils.isBlank(response)) {
				throw new Exception();
			}
			// 非法数据处理
			replaceBlank(response);
			response = response.replace("\n", "");
			
			JSONObject json = JSONObject.fromObject(response);
			if(json.get("err") == null) {
				// 设置返回信息
				msg.setResutlCode(1);
				msg.setResultCodeMsg(response);
			} else {
				msg.setResultCodeMsg(json.getString("err"));
			}
		} catch (Exception e) {
			ServiceStart.CONSOLE_LOG.error("query line error.port:{}", line.getPort());
			// 线路挂掉了
			msg.setResultCodeMsg("查询线路繁忙，请稍后再试");
		}
		// 返回处理时间
		return new Date().getTime() - eStart.getTime();
	}
	
	// 字符替换
	private void replaceBlank(String str) {
		String dest = "";
		if (str != null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		str = dest;
	}
}
