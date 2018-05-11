package cn.org.rapid_framework.generator;


/**
 * 
 * @author nbin
 * 
 */

public class GeneratorMain {
	/**
	 * 请直接修改以下代码调用不同的方法以执行相关生成任务.
	 */
	public static void main(String[] args) throws Exception {
		GeneratorFacade g = new GeneratorFacade();
		g.deleteOutRootDir();							//删除生成器的输出目录
		//通过数据库表生成文件,template为模板的根目录
		
		g.generateByTable("wechat","template");
//		g.generateByTable("system_profit","template");
//		g.generateByTable("gold_log","template");
//		g.generateByTable("system_feedback","template");
//		g.generateByTable("system_notice","template");
//		g.generateByTable("system_message","template");
//		g.generateByTable("system_luck","template");
//		g.generateByTable("system_cash","template");
//		g.generateByTable("city","template");
//		
//		g.generateByTable("user_roomcards","template");	
//		g.generateByTable("system_prop_log","template");	
//		g.generateByTable("system_product","template");	
//		g.generateByTable("system_order","template");	
//		g.generateByTable("system_agency","template");	
//		g.generateByTable("club","template");	
//		g.generateByTable("statistics_earnings","template");	
//		g.generateByTable("club_user","template");	
//		g.generateByTable("statistics_roomcard","template");	
//		g.generateByTable("statistics_user","template");	
//		g.generateByTable("statistics_exploits","template");	
		
//		g.generateByTable("game_economy_statistics","template");	
//		g.generateByTable("game_fkbs_bet_detail","template");	
//		g.generateByTable("game_fkbs_gold_detail","template");	
//		g.generateByTable("game_fkbs_prize_detail","template");	
//		g.generateByTable("game_game_statistics","template");	
//		g.generateByTable("game_lztb_gold_detail","template");	
//		g.generateByTable("game_mail_dateil","template");	
//		g.generateByTable("game_mail_statistics","template");	
//		g.generateByTable("game_other_gold_detail","template");	
//		g.generateByTable("game_prop_statistics","template");	
//		g.generateByTable("game_prop_user_info","template");	
//		g.generateByTable("game_realtime_win_lose","template");	
//		g.generateByTable("game_shz_gold_detail","template");	
//		g.generateByTable("game_shz_room","template");	
//		g.generateByTable("game_tgpd_bet","template");	
//		g.generateByTable("game_tgpd_gold_detail","template");	
//		g.generateByTable("gm_prop_operation_detail","template");	
//		g.generateByTable("operation_base_active","template");	
//		g.generateByTable("operation_base_download","template");	
//		g.generateByTable("operation_base_game_collapse","template");	
//		g.generateByTable("operation_base_game_into","template");	
//		g.generateByTable("operation_base_login_detail","template");	
//		g.generateByTable("operation_base_recharge","template");	
//		g.generateByTable("operation_base_user","template");	
//		g.generateByTable("operation_base_user_online","template");	
//		g.generateByTable("operation_device_memory","template");	
//		g.generateByTable("operation_device_model","template");	
//		g.generateByTable("operation_device_os","template");	
//		g.generateByTable("operation_device_resolution","template");	
//		g.generateByTable("operation_device_telcos","template");	
//		g.generateByTable("operation_money_day","template");	
//		g.generateByTable("operation_money_ltv","template");	
//		g.generateByTable("operation_money_ranking","template");	
//		g.generateByTable("operation_money_trend","template");	
//		g.generateByTable("operation_online_duration","template");	
//		g.generateByTable("operation_online_duration_scatter","template");	
//		g.generateByTable("operation_online_login_count","template");	
//		g.generateByTable("operation_online_startup","template");	
//		g.generateByTable("operation_online_trend","template");	
//		g.generateByTable("operation_player_brisk","template");	
//		g.generateByTable("operation_player_brisk_channel","template");	
//		g.generateByTable("operation_player_brisk_game_days","template");	
//		g.generateByTable("operation_player_brisk_pay","template");	
//		g.generateByTable("operation_player_brisk_trend","template");	
//		g.generateByTable("operation_player_loss_back","template");	
//		g.generateByTable("operation_player_new","template");	
//		g.generateByTable("operation_player_new_channel","template");	
//		g.generateByTable("operation_player_new_first_online","template");	
//		g.generateByTable("operation_player_new_smallmark_device","template");	
//		g.generateByTable("operation_player_new_smallmark_ip","template");	
//		g.generateByTable("operation_realtime_day","template");	
//		g.generateByTable("operation_remain_channel","template");	
//		g.generateByTable("operation_remain_day","template");	
//		g.generateByTable("operation_remain_month","template");	
//		g.generateByTable("operation_remain_new_user","template");	
//		g.generateByTable("operation_remain_register","template");	
//		g.generateByTable("operation_remain_week","template");	
//		g.generateByTable("operation_report_day","template");	
//		g.generateByTable("operation_report_month","template");	
//		g.generateByTable("operation_report_week","template");	
//		g.generateByTable("operation_tatol_month","template");
//		g.generateByTable("operation_total_all","template");
		
		
		
		
//		g.generateByAllTable("template");
		//打开文件夹
		Runtime.getRuntime().exec("cmd.exe /c start "+GeneratorProperties.getRequiredProperty("outRoot"));
	}
}
