package com.guse.platform.task;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.guse.platform.dao.doudou.SystemTaskMapper;
import com.guse.platform.entity.doudou.SystemTask;
import com.guse.platform.service.doudou.ClubService;
import com.guse.platform.service.doudou.ClubUserService;
import com.guse.platform.service.doudou.SystemLuckService;
import com.guse.platform.service.doudou.UserRoomcardsService;
import com.guse.platform.service.system.UsersService;

@Component
public class DisposeSystemTask  {
	
	@Autowired
	private SystemTaskMapper taskMapper;
	@Autowired
	private UsersService usersService;
	@Autowired
	private UserRoomcardsService roomcardsService;
	@Autowired
	private ClubService clubService;
	@Autowired
	private ClubUserService clubUserService;
	@Autowired
	private SystemLuckService luckService;
	
	private List<SystemTask> taskList = null;
	
	
	@Scheduled(cron="0 0/2 * * * ?")
    public void aTask() {
    	taskList = getPendingTask();
    	
    	if(taskList != null && taskList.size() > 0) {
    		for (int i = 0; i < taskList.size(); i++) {
				disposeTask(taskList.get(i));
			}
    	}
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        System.out.println(df.format(new Date()) + "********A任务每2分钟执行一次进入测试");
    }
    /**
     * 获取10条未处理的任务
     * @return
     */
    public List<SystemTask> getPendingTask() {
    	List<SystemTask> list = taskMapper.getPendingTask();
    	return list;
    }
    
    /**
     * 处理任务
     * @param task
     * @return
     */
    public void disposeTask(SystemTask task) {
    	int type = task.getTaskCmd();
    	switch (type) {
			case 1:
				usersService.taskUsers(task);
				break;
			case 2:
				roomcardsService.taskRoomCards(task);
				break;
			case 3:
				clubService.taskClubs(task);
				break;
			case 4:
				clubUserService.taskClubUsers(task);
				break;
			case 5:
				luckService.taskUserLuck(task);
				break;	
			default:
				break;
		}
    }
    
}