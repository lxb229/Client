package cn.lcg.backtage.sdk;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.guse.platform.entity.doudou.OperationBaseLoginDetail;
import com.guse.platform.entity.doudou.OperationBaseLogoutDetail;
import com.guse.platform.utils.UserLoginAndLogOutInfo;

import cn.lcg.backstage.sdk.mq.UserLoginAndLogOutInfoSend;

public class UserLoginAndLogOutInfoSendTest {
	
	public static void main(String[] args) throws ParseException {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		UserLoginAndLogOutInfoSend send = new UserLoginAndLogOutInfoSend();

		for (int i = 0; i < 100; i++) {
			UserLoginAndLogOutInfo info = new UserLoginAndLogOutInfo();
			OperationBaseLoginDetail data = new OperationBaseLoginDetail();
			data.setOblLoginTime(new Date());
			data.setObuUserid(500000L+i);
			data.setOblSessionid(""+(500000L+i));
			data.setObdDloadChannel(i%19+1+"");
			info.setTypeCode(1);
			info.setOperationBaseLoginDetail(data);
			boolean isok = send.send(info);
			System.out.println("发送数据：" + (i + 1) + isok);
		}

		/*for (int i = 0; i < 100; i++) {
			UserLoginAndLogOutInfo info = new UserLoginAndLogOutInfo();
			OperationBaseLogoutDetail data = new OperationBaseLogoutDetail();
			long currentTime = System.currentTimeMillis() ;
			currentTime +=(i%30+1)*60*1000;
			Date date=new Date(currentTime);	
			data.setObodLogoutTime(df.parse("2017-08-17 15:13:12"));
			data.setObuUserid(500000L+i);
			data.setOblSessionid(""+(500000L+i));
			info.setTypeCode(2);
			info.setOperationBaseLogoutDetail(data);
			boolean isok = send.send(info);
			System.out.println("发送数据：" + (i + 1) + isok);
		}*/
		
		// 如果是频繁的发送数据，建议不关闭发送链接
		send.close();
	}

}
