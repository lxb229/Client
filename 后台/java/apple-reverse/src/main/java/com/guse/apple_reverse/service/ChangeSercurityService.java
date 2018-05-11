package com.guse.apple_reverse.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guse.apple_reverse.Main.ServiceStart;
import com.guse.apple_reverse.dao.AppleSercurityDao;
import com.guse.apple_reverse.dao.model.AppleSercurity;
import com.guse.apple_reverse.netty.handler.session.Message;
import com.guse.apple_reverse.service.query.ListeningQuery;
import com.guse.apple_reverse.service.query.QueryBean;
import com.guse.apple_reverse.service.query.QueryClient;
import com.guse.apple_reverse.service.socket.SocketService;

/**
 * @ClassName: ChangeSecurityService
 * @Description: 修改密码和密保
 * @author Fily GUSE
 * @date 2017年12月5日 下午5:50:47
 * 
 */
@Service
public class ChangeSercurityService implements ListeningQuery {

	@Autowired
	AppleSercurityDao securityDao;

	boolean is_work = false; // 是否在执行

	public List<AppleSercurity> start() {
		if (is_work) {
			return new ArrayList<AppleSercurity>();
		}
		is_work = true;

		ServiceStart.CONSOLE_LOG.info("start exector change security....");
		Date start = new Date();

		// 初始化结果信息
		result.clear();
		result.put("t", 0); // 处理总条数
		result.put("s", 0); // 成功个数
		result.put("f", 0); // 失败个数
		// 保存最后处理的10个信息
		resultData.clear();
		// 查询信息标记
		querys.clear();

		// 当前查询到未处理条数
		if (securityDao.findCount(AppleSercurity.STATUS_IMPORT) > 0) {
			// 循环处理未查询数据
			while (true) {
				// 查询未处理的信息，每次300条
				List<AppleSercurity> list = securityDao.findAllImport(AppleSercurity.STATUS_IMPORT, 300);
				if (list == null || list.isEmpty()) {
					break;
				}
				String ids = "";
				// 修改数据状态为查询中
				for (AppleSercurity apple : list) {
					ids += apple.getId() + ",";
				}
				securityDao.batchUpdateStatus(AppleSercurity.STATUS_EXECTOR, ids.substring(0, ids.length() - 1));
				result.put("t", result.get("t") + list.size()); // 处理总条数累加
				
				List<Message> msgList = new ArrayList<Message>();
				for(AppleSercurity apple : list) {
					msgList.add(Message.createQuery(apple));
				}
				new QueryClient(msgList, this).run();
				ServiceStart.CONSOLE_LOG.info("start exector change security data total no.{}", list.size());
			}
			// 等待结果
			int waitNum = 0;
			do {
				waitNum = 0;
				for(QueryBean bean : querys) {
					// 没有响应
					if(!bean.isResult()) {
						//超时2分钟
						if((new Date().getTime() - bean.getQueryTime()) > 1000 * 60 * 2) {
							bean.setResult(true);
							AppleSercurity apple = securityDao.getById(bean.getId());
							if(apple != null) {
								Message msg = Message.createQuery(apple);
								msg.setResutlCode(0);
								msg.setResultCodeMsg("查询服务器繁忙，请稍后再试");
								new SocketService(msg, null).queryApple();
							}
						} else {
							waitNum ++;
						}
					}
				}
				if(waitNum > 0) {
					ServiceStart.CONSOLE_LOG.info("surplus query change data NO.{}", waitNum);
					try {
						Thread.sleep(1000 * 5);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} 
				}
			} while(waitNum > 0);
		}
		ServiceStart.CONSOLE_LOG.info("exector change security finish. data number:{} , success:{} , failure:{} . elapsed time:{}",
						result.get("t"), result.get("s"), result.get("f"), +(new Date().getTime() - start.getTime()));

		// 复制返回结果返回
		List<AppleSercurity> result = new ArrayList<AppleSercurity>();
		result.addAll(resultData);

		is_work = false;
		return result;
	}

	// 处理信息记录
	private static Map<String, Integer> result = new HashMap<String, Integer>();
	// 保留10条最后处理的数据
	private static List<AppleSercurity> resultData = new ArrayList<AppleSercurity>();
	// 查询结果统计 
	public static void queryResultCount(int appleId, int type) {
		QueryBean bean = getBean(appleId);
		if(bean != null && !bean.isResult()) {
			if(type == Message.RESULT_SUCCESS) {
				result.put("s", result.get("s") + 1);
			} else {
				result.put("f", result.get("f") + 1);
			}
		}
	}
	// 添加查询结果 
	public static void queryResultAdd(AppleSercurity apple) {
		QueryBean bean = getBean(apple.getId());
		if(bean != null) {
			resultData.add(apple);
			if(resultData.size() > 10) {
				resultData.remove(0);
			}
		}
	}
	// 移除等待列表
	public static void removeWaitRespond(Integer id) {
		QueryBean bean = getBean(id);
		if(bean != null) {
			bean.setRespond(true);
		}
	}
	//标记处理 
	public static void markExector(int id) {
		QueryBean bean = getBean(id);
		if(bean != null) {
			bean.setRespond(true);
			bean.setResult(true);
		}
	}

	// //////////////////////////////////////////////////
	// //////////////////////实现查询回调接口////////////////////
	// //////////////////////////////////////////////////
	// 处理结果集对象
	private static List<QueryBean> querys = new ArrayList<QueryBean>();
	// 根据id 获取查询结果对象
	private static QueryBean getBean(int id) {
		if (!querys.isEmpty()) {
			for (QueryBean bean : querys) {
				if (bean.getId() == id) {
					return bean;
				}
			}
		}
		return null;
	}

	@Override
	public void queryFail(Message msg) {
		new SocketService(msg, null).change();
	}

	@Override
	public void markQuery(Message msg, Long markTime) {
		int id = JSONObject.fromObject(msg.getData()).getInt("id");
		querys.add(new QueryBean(id, msg, markTime));
	}

	@Override
	public List<QueryBean> getQuerys() {
		return querys;
	}
}
