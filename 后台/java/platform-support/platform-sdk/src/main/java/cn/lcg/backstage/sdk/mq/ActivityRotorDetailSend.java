package cn.lcg.backstage.sdk.mq;

import com.guse.platform.entity.doudou.GameActivityRotorDetail;

/**
 * 活动幸运大转盘消息发送类
 * 说明：用户完大转盘时发送此数据。
 * @author yanhua
 *
 */
public class ActivityRotorDetailSend extends SendDataBase<GameActivityRotorDetail> {

	public ActivityRotorDetailSend() {
		queueName = "Game_Activity_RotorDetail";
		modelName = "活动幸运大转盘明细";
		init();
	}

}
