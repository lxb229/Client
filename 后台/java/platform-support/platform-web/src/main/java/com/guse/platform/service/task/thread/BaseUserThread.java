package com.guse.platform.service.task.thread;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.guse.platform.entity.doudou.OperationBaseUser;
import com.guse.platform.service.task.BaseUserTask;


/**
 * <p>Description: 同步redis中的用户到mysql
 * <p>User: liyang
 * <p>Date: 2016-9-25
 * <p>Version: 1.0
 */
public class BaseUserThread implements Runnable{
  private static final Logger     logger = LoggerFactory.getLogger(BaseUserThread.class);
  private List<OperationBaseUser> users;
  private BaseUserTask task;
  
  public BaseUserThread(List<OperationBaseUser> users,BaseUserTask task){
	    this.users = users;
	    this.task = task;
	  }

  @Override
  public void run() {
	  try {
		  task.insert(users);
	} catch (Exception e) {
		logger.error("数据同步失败:{}",JSON.toJSONString(users));
	}
  }
}
