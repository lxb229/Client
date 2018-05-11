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
import com.wangzhixuan.service.IBetLogService;
import com.wangzhixuan.service.IInsuranceLogService;
import com.wangzhixuan.service.IJettonLogService;
import com.wangzhixuan.service.IJoinPartyLogService;
import com.wangzhixuan.service.IJoinRoomLogService;
import com.wangzhixuan.service.ILoginLogService;
import com.wangzhixuan.service.IPlayerInsuranceProfitService;
import com.wangzhixuan.service.IPlayerProfitService;
import com.wangzhixuan.service.IPlayerService;
import com.wangzhixuan.service.IRoomDisappearLogService;
import com.wangzhixuan.service.IRoomService;

@Component
public class DisposeSystemTask  {

	@Autowired
    private SystemTaskMapper taskMapper;
	@Autowired
	private IPlayerService playerService;
	@Autowired
	private IRoomService roomService;
	@Autowired
	private ILoginLogService loginLogService;
	@Autowired
	private IJoinRoomLogService joinRoomLogService;
	@Autowired
	private IJoinPartyLogService joinPartyLogService;
	@Autowired
	private IJettonLogService jettonLogService;
	@Autowired
	private IBetLogService betLogService;
	@Autowired
	private IInsuranceLogService insuranceLogService;
	@Autowired
	private IPlayerInsuranceProfitService playerInsuranceProfitService;
	@Autowired
	private IPlayerProfitService playerProfitService;
	@Autowired
	private IRoomDisappearLogService roomDisappearLogService;
	
	private List<SystemTask> taskList = null;
	
    @Scheduled(cron="0 0/5 * * * ?")
    public void aTask() {
    	taskList = getPendingTask();
    	
    	if(taskList != null && taskList.size() > 0) {
    		for (int i = 0; i < taskList.size(); i++) {
				disposeTask(taskList.get(i));
			}
    	}
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        System.out.println(df.format(new Date()) + "********A任务每5分钟执行一次进入测试");
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
				playerService.taskPlayer(task);
				break;
			case 2:
				roomService.taskRoom(task);
				break;
			case 3:
				loginLogService.taskLoginLog(task);
				break;
			case 4:
				joinRoomLogService.taskJoinRoomLog(task);
				break;
			case 5:
				joinPartyLogService.taskJoinPartyLog(task);
				break;
			case 6:
				jettonLogService.taskJettonLog(task);
				break;
			case 7:
				betLogService.taskBetLog(task);
				break;
			case 8:
				insuranceLogService.taskInsuranceLog(task);
				break;
			case 9:
				playerInsuranceProfitService.taskPlayerInsuranceProfit(task);;
				break;
			case 10:
				playerProfitService.taskPlayerProfit(task);;
				break;
			case 11:
				roomDisappearLogService.taskRoomDisappearLog(task);;
				break;
			default:
				break;
		}
    }
    
    
}