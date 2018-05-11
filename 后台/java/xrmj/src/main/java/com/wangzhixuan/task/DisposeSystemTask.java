package com.wangzhixuan.task;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.wangzhixuan.mapper.SystemTaskMapper;
import com.wangzhixuan.model.SystemTask;
import com.wangzhixuan.service.IMahjongPlayerService;
import com.wangzhixuan.service.IMahjongService;
import com.wangzhixuan.service.IPlayerLuckService;
import com.wangzhixuan.service.IPlayerService;
import com.wangzhixuan.service.IPlayerWishService;
import com.wangzhixuan.service.IPropLogService;
import com.wangzhixuan.service.ISystemOrderService;

@Component
public class DisposeSystemTask  {

	@Autowired
    private SystemTaskMapper taskMapper;
	@Autowired
	private IPlayerService playerService;
	@Autowired
	private ISystemOrderService orderService;
	@Autowired
	private IPlayerWishService wishService;
	@Autowired
	private IPropLogService propLogService;
	@Autowired
	private IMahjongService mahjongService;
	@Autowired
	private IMahjongPlayerService mahjongPlayerService;
	@Autowired
	private IPlayerLuckService playerLuckService;
	
	private List<SystemTask> taskList = null;
	
    @Scheduled(cron="0 0/1 * * * ?")
    public void aTask() {
    	taskList = getPendingTask();
    	
    	if(taskList != null && taskList.size() > 0) {
    		for (int i = 0; i < taskList.size(); i++) {
				disposeTask(taskList.get(i));
			}
    	}
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        System.out.println(df.format(new Date()) + "********A任务每1分钟执行一次进入测试");
    }
    
    /**
     * 获取30条未处理的任务
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
				playerService.taskPlayer(task);
				break;
			case 2:
				orderService.taskOrder(task);
				break;
			case 3:
				propLogService.taskPropLog(task);
				break;
			case 4:
				wishService.taskPlayerWish(task);
				break;
			case 5:
				mahjongService.taskMahjong(task);
				break;
			case 6:
				mahjongPlayerService.taskMahjongPlayer(task);
				break;
			case 7:
				mahjongService.taskMahjongCard(task);
				break;
			case 8:
				mahjongService.taskMahjongCombat(task);
				break;
			case 9:
				playerLuckService.taskUseLuck(task);
				break;
			default:
				break;
		}
    }
    
    
}