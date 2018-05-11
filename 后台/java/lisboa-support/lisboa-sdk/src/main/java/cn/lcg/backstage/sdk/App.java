package cn.lcg.backstage.sdk;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.guse.platform.entity.doudou.GameActivityRotorDetail;

import cn.lcg.backstage.sdk.mq.ActivityRotorDetailSend;
import cn.lcg.backstage.sdk.mq.SendData;

public class App {

	public static void main(String[] args) throws ParseException {
		DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SendData<GameActivityRotorDetail> send = new ActivityRotorDetailSend();

		for (int i = 0; i < 500; i++) {
			GameActivityRotorDetail data = new GameActivityRotorDetail();
			data.setGardBettingGold(100);
			data.setGardCreateTime(dateFormat2.parse("2017-06-13 09:00:01"));
			data.setGardId("");
			data.setGardPrizeGold(100);
			data.setGardPrizeName("普通奖励");
			data.setGardPrizeNumber(5);
			data.setGardPrizeTime(dateFormat2.parse("2017-06-13 09:10:01"));
			data.setObuUserid(10001L);
			data.setObuUserNick("张三SDK测试");

			boolean isok = send.send(data);
			System.out.println("数据发送：" + (i + 1) + isok);
		}

		// 如果是频繁的发送数据，建议不关闭发送链接
		//send.close();

	}

}
