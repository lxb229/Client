package com.palmjoys.yf1b.act.event.model;

public class EventAttrib_Corps {
	//命令码
	public int cmd;
	//数据
	public Object cmdData; 
	
	
	public void addCmd1Data(String mahjong_no, String mahjong_name, 
			String mahjong_wechat, String start_no, String pattern, String usable_amount, String create_time){
		EventAttrib_Corps_Cmd1 cmd1Data = new EventAttrib_Corps_Cmd1();
		cmd1Data.mahjong_no = mahjong_no;
		cmd1Data.mahjong_name = mahjong_name;
		cmd1Data.mahjong_wechat = mahjong_wechat;
		cmd1Data.start_no = start_no;
		cmd1Data.pattern = pattern;
		cmd1Data.usable_amount = usable_amount;
		cmd1Data.create_time = create_time;
		
		this.cmdData = cmd1Data;
	}
	
	public void addCmd2Data(String mahjong_no, String start_no, String create_time){
		EventAttrib_Corps_Cmd2 cmd2Data = new EventAttrib_Corps_Cmd2();
		cmd2Data.mahjong_no = mahjong_no;
		cmd2Data.start_no = start_no;
		cmd2Data.create_time = create_time;
		
		this.cmdData = cmd2Data;
	}
	
	public void addCmd3Data(String mahjong_no, String old_start_no, String new_start_no, String create_time){
		EventAttrib_Corps_Cmd3 cmd3Data = new EventAttrib_Corps_Cmd3();
		cmd3Data.mahjong_no = mahjong_no;
		cmd3Data.old_start_no = old_start_no;
		cmd3Data.new_start_no = new_start_no;
		cmd3Data.create_time = create_time;
		
		this.cmdData = cmd3Data;
	}
	
	public void addCmd4Data(String mahjong_no, String wxNO, String create_time){
		EventAttrib_Corps_Cmd4 cmd3Data = new EventAttrib_Corps_Cmd4();
		cmd3Data.mahjong_no = mahjong_no;
		cmd3Data.wxNO = wxNO;
		cmd3Data.create_time = create_time;
		
		this.cmdData = cmd3Data;
	}
	
	public class EventAttrib_Corps_Cmd1{
		//麻将馆唯一id
		public String mahjong_no;
		//麻将馆唯一名称
		public String mahjong_name;
		//麻将馆WX号
		public String mahjong_wechat;
		//创建人名明星号
		public String start_no;
		//麻将馆模式
		public String pattern;
		//房卡数
		public String usable_amount;	
		//创建时间
		public String create_time;
	}
	
	public class EventAttrib_Corps_Cmd2{
		//麻将馆唯一id
		public String mahjong_no;
		//创建人名明星号
		public String start_no;
		//创建时间
		public String create_time;
	}
	
	public class EventAttrib_Corps_Cmd3{
		//麻将馆唯一id
		public String mahjong_no;
		//原创建玩家明星号
		public String old_start_no;
		//新创建玩家明星号
		public String new_start_no;
		//创建时间
		public String create_time;
	}
	
	public class EventAttrib_Corps_Cmd4{
		//麻将馆唯一id
		public String mahjong_no;
		//修改后的微信号
		public String wxNO;
		//创建时间
		public String create_time;
	}
	
}
