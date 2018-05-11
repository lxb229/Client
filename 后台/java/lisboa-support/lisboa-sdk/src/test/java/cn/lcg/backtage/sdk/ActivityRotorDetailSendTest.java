package cn.lcg.backtage.sdk;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.guse.platform.entity.doudou.GameActivityRotorDetail;

import cn.lcg.backstage.sdk.mq.ActivityRotorDetailSend;
import cn.lcg.backstage.sdk.mq.SendData;

public class ActivityRotorDetailSendTest {
	public static final String WEEK = "周卡";
	public static final String MONTH = "月卡";
	public static final String diamond5 = "钻石5";
	public static final String diamond8 = "钻石8";
	public static final String gold2000 = "金币2000";
	public static final String gold5000 = "金币5000";
	public static final String gold1w = "金币1W";
	public static final String gold5w = "金币5W";
	public static final String gold10w = "金币10W";
	public static final String gold50w = "金币50W";
	public static final String gold300w = "金币300W";
	public static final String gold1000w = "金币1000W";

	public static void main(String[] args) throws ParseException {

		DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SendData<GameActivityRotorDetail> send = new ActivityRotorDetailSend();

		for (int i = 0; i < 100; i++) {
			GameActivityRotorDetail data = new GameActivityRotorDetail();
			data.setGardBettingGold(100);
			data.setGardCreateTime(dateFormat2.parse("2017-06-13 09:00:01"));
			data.setGardId("");
			data.setGardPrizeGold(100);
			int prize =  i%11; 
			String prizeStr = WEEK;
			if(prize==1){
				prizeStr = MONTH;
			}else if(prize==2){
				prizeStr = diamond5;
			}else if(prize==3){
				prizeStr = diamond8;
			}else if(prize==4){
				prizeStr = gold2000;
			}else if(prize==5){
				prizeStr = gold5000;
			}else if(prize==6){
				prizeStr = gold1w;
			}else if(prize==7){
				prizeStr = gold5w;
			}else if(prize==8){
				prizeStr = gold10w;
			}else if(prize==9){
				prizeStr = gold50w;
			}else if(prize==10){
				prizeStr = gold300w;
			}else if(prize==11){
				prizeStr = gold1000w;
			}
			data.setGardPrizeName(prizeStr);
			data.setGardPrizeNumber(1);
			data.setObuIsGoBroke(0);
			data.setGardPrizeTime(dateFormat2.parse("2017-06-13 09:10:01"));
			data.setObuUserid(500000L + i);
			data.setObuUserNick("张三SDK测试");

			boolean isok = send.send(data);
			System.out.println("数据发送：" + (i + 1) + isok);
		}

		// 如果是频繁的发送数据，建议不关闭发送链接
		send.close();
	}

}
