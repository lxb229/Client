package com.wangzhixuan.task;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.wangzhixuan.service.IPlayerWishService;

@Component
public class ClearWishTask  {

	@Autowired
	private IPlayerWishService wishService;
	
	
    
    @Scheduled(cron="0 0 0 * * ?")
    public void clearWish() {
    	wishService.clearWish();
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        System.out.println(df.format(new Date()) + "********B任务每天凌晨24点执行，清空祝福值");
    }
    
}