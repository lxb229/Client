package com.palmjoys.yf1b.act.majiang.model;

//桌子规则属性
public class TableRuleAttrib {
	//桌子底分
	public int baseScore;
	//桌子聊天类型(0=一般房,1=语音,2=视频)
	public int chatType;
	//最大游戏局数
	public int maxGameNum;
	//最大番数
	public int maxRate;
	//几门牌(2门或3门)
	public int cardTypeNum;
	//发手牌张数
	public int handCardNum;
	//自摸加底
	public boolean bZiMoAddBase;
	//自摸加番
	public boolean bZiMoAddRate;
	//点杠上花一家付
	public boolean bDianGangHuaOnce;
	//点杠上花大家付
	public boolean bDianGangHuaAll;
	//换三张
	public boolean bSwapCard;	
	//是否听牌提示
	public boolean bTingTips;
	//是否血战到底
	public boolean bXueZhaoDaoDi;
	//是否血流成河
	public boolean bXueLiuChengHe;
	//是否定缺
	public boolean bDingQue;
	//几番起胡
	public int canHuPaiFanShu;
	//几分起胡
	public int canHuPaiScore;
	//游戏人数
	public int gamePlayerNum;
	//对对胡2番
	public boolean bDuiduiHu2Fan;
	//自摸不加
	public boolean bZiMoUnAdd;
	//点杠花(一人自摸)
	public boolean bDianGangHuaOnceZiMo;
	//幺鸡任用
	public boolean bYaoJiRenYong;
	//软碰可杠
	public boolean bRuanPengKeGang;
	//两家不躺
	public boolean bLiangJiaBuTang;
	//躺牌
	public boolean bTangPai;
	//关死
	public boolean bGuanShi;
	//转雨
	public boolean bZhuanYu;
	//查叫退税
	public boolean bChaJiaoTuiShui;
	//过水加番可胡
	public boolean bGeShuiJiaFanHu;
	//最大飘几个(0=不能漂)
	public int maxPiaoNum;
	//查叫(0=查小叫,1=查大叫)
	public int chaDaJiao;
	//刮风下雨
	public boolean bGuaFengXiaYu;
	//是否报叫
	public boolean bBaoJia;
	//家家有(点杠每家都要给)
	public boolean bJiaJiaYou;
	//房卡支付方式(0=帮会支付,1=创建者付,2=随机付,3=赢家付,4=输家付)
	public int roomCardPayType;
	//支持胡牌牌形
	//大对胡
	public boolean bDuiDuiHu;
	//清一色
	public boolean bQinYiShe;
	//暗七对
	public boolean bAnQiDui;
	//龙七对
	public boolean bLongQiDui;
	//清大对
	public boolean bQinDaDui;
	//清暗七对
	public boolean bQinAnQiDui;
	//清龙七对
	public boolean bQinLongQiDui;
	//幺九
	public boolean bYaoJiu;
	//将对(将七对)
	public boolean bJiangDui;
	//门清
	public boolean bMengQin;
	//中张
	public boolean bZhongZhang;
	//金钩钓
	public boolean bJinGouDiao;
	//海底捞
	public boolean bHaiDiLao;
	//海底炮
	public boolean bHaiDiPao;
	//摆独张
	public boolean bBaiDuZhang;
	//缺一门
	public boolean bQueYiMeng;
	//一般高
	public boolean bYiBanGao;
	//天地胡
	public boolean bTianDiHu;
	//夹心5胡
	public boolean bJiaXin5Hu;
	//卡二条胡
	public boolean bJiaXin2Hu;
	//无幺鸡
	public boolean b0YaoJi;
	//四幺鸡
	public boolean b4YaoJi;	
	
	public TableRuleAttrib(){
		//桌子底分
		this.baseScore = 1;
		//桌子聊天类型(0=一般房,1=语音,2=视频)
		this.chatType = 0;
		//最大游戏局数
		this.maxGameNum = 4;
		//最大番数
		this.maxRate = 16;
		//几房牌(2房或3房)
		this.cardTypeNum = 3;
		//发手牌张数
		this.handCardNum = 13;
		//自摸加底
		this.bZiMoAddBase = false;
		//自摸加番
		this.bZiMoAddRate = false;
		//点杠上花(点炮)
		this.bDianGangHuaOnce = true;
		//点杠上花(自摸)
		this.bDianGangHuaAll = false;
		//换三张
		this.bSwapCard = false;
		//听牌提示
		this.bTingTips = false;
		//血战到底
		this.bXueZhaoDaoDi = false;
		//血流成河
		this.bXueLiuChengHe = false;
		//定缺
		this.bDingQue = false;
		//几番起胡
		this.canHuPaiFanShu = 0;
		//几分起胡
		this.canHuPaiScore = 0;
		//游戏人数
		this.gamePlayerNum = 4;
		//对对胡2番
		this.bDuiduiHu2Fan = false;
		//自摸不加
		this.bZiMoUnAdd = false;
		//点杠花(一人自摸)
		this.bDianGangHuaOnceZiMo = false;
		//幺鸡任用
		this.bYaoJiRenYong = false;
		//软碰可杠
		this.bRuanPengKeGang = false;
		//两家不躺
		this.bLiangJiaBuTang = false;
		//躺牌
		this.bTangPai = false;
		//关死
		this.bGuanShi = false;
		//转雨
		this.bZhuanYu = false;
		//查叫退税
		this.bChaJiaoTuiShui = false;
		//过水加番可胡
		this.bGeShuiJiaFanHu = false;
		//最大飘几个(0=不能漂)
		this.maxPiaoNum = 0;
		//查叫(0=查小叫,1=查大叫)
		this.chaDaJiao = 0;
		//刮风下雨
		this.bGuaFengXiaYu = false;
		//报叫
		this.bBaoJia = false;
		//家家有
		this.bJiaJiaYou = false;
		//房卡支付方式(1=创建者付,2=随机付,3=赢家付,4=输家付)
		this.roomCardPayType = 0;
		//大对胡
		this.bDuiDuiHu = false;
		//清一色
		this.bQinYiShe = false;
		//暗七对
		this.bAnQiDui = false;
		//龙七对
		this.bLongQiDui = false;
		//清大对
		this.bQinDaDui = false;
		//清暗七对
		this.bQinAnQiDui = false;
		//清龙七对
		this.bQinLongQiDui = false;
		//幺九
		this.bYaoJiu = false;
		//中张
		this.bZhongZhang = false;
		//将对(将七对)
		this.bJiangDui = false;
		//门清
		this.bMengQin = false;
		//金钩钓
		this.bJinGouDiao = false;
		//海底捞
		this.bHaiDiLao = false;
		//海底炮
		this.bHaiDiPao = false;
		//摆独张
		this.bBaiDuZhang = false;
		//缺一门
		this.bQueYiMeng = false;
		//一般高
		this.bYiBanGao = false;
		//天地胡
		this.bTianDiHu = false;
		//夹心5胡
		this.bJiaXin5Hu = false;
		//卡二条胡
		this.bJiaXin2Hu = false;
		//无幺鸡
		this.b0YaoJi = false;
		//四幺鸡
		this.b4YaoJi = false;
	}
}
