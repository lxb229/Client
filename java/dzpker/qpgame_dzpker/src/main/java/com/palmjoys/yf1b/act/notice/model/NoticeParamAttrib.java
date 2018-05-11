package com.palmjoys.yf1b.act.notice.model;

import java.util.ArrayList;
import java.util.List;

public class NoticeParamAttrib {
	//内容
	public String content;
	//显示颜色(RGB)
	public List<Integer> color = new ArrayList<Integer>();
	//类型(0=不变参,1=玩家对像,2=字符串,3=物品)
	public int type;
}
