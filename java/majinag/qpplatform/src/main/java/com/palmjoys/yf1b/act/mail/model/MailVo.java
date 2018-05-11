package com.palmjoys.yf1b.act.mail.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MailVo {
	//列表项列表
	public List<MailVoItem> mails = new ArrayList<>();
	
	public void addItem(MailAttrib mailAttrib){
		MailVoItem item = new MailVoItem();
		item.mailId = mailAttrib.mailId;
		item.title = mailAttrib.title;
		item.recvTime = String.valueOf(mailAttrib.recvTime);
		item.read = mailAttrib.read;
		item.attachmentGetState = mailAttrib.attachmentGetState;
		if(null == mailAttrib || mailAttrib.attachment.isEmpty()){
			item.attachmentGetState = 1;
		}
		this.mails.add(item);
	}	
	
	public void sort(){
		this.mails.sort(new Comparator<MailVoItem>(){
			@Override
			public int compare(MailVoItem o1, MailVoItem o2) {
				if(o1.read > o2.read){
					return 1;
				}else{
					if(o1.read < o2.read){
						return -1;
					}else{
						long time1 = Long.parseLong(o1.recvTime);
						long time2 = Long.parseLong(o2.recvTime);
						if(time1 > time2){
							return -1;
						}else{
							if(time1 < time2){
								return 1; 
							}else{
								return 0;
							}
						}
					}
				}
			}
		});
	}
	
	public class MailVoItem{
		//邮件Id
		public int mailId;
		//邮件标题
		public String title;
		//接收时间
		public String recvTime;
		//是否已读
		public int read;
		//邮件附件领取状态(0=未领取,1=已领取)
		public int attachmentGetState;
	}
}
