package com.palmjoys.yf1b.act.majiang.model;

import java.util.ArrayList;
import java.util.List;

public class RuleCfgVo {
	//条目Id
	public int itemId;
	//条目名称
	public String itemName;
	//规则内容
	public List<RuleContent> ruleContents = new ArrayList<RuleContent>();
	
	public RuleContent newRuleContent(){
		RuleContent vo = new RuleContent();
		return vo;
	}
	
	public RuleContentItem newRuleContentItem(){
		RuleContentItem vo = new RuleContentItem();
		return vo;
	}
	
	public RuleContentItemAttrib newRuleContentItemAttrib(){
		RuleContentItemAttrib vo = new RuleContentItemAttrib();
		return vo;
	}
	
	public class RuleContent{
		//是否显示(0=不显示)
		public int show;
		//默认值
		public int value;
		//规则名称
		public String ruleName;
		//规则子项组
		public List<RuleContentItem> ruleContentItems = new ArrayList<RuleContentItem>();
	}
	
	public class RuleContentItem{
		//单选多选(0=单选,1=多选)
		public int ridio;
		//规则列表
		public List<RuleContentItemAttrib> ruleContentItemAttribs = new ArrayList<RuleContentItemAttrib>();
	}
	
	public class RuleContentItemAttrib{
		//规则Id
		public int ruleId;
		//规则名称
		public String ruleName;
		//初始选中状态(0=未选中,1=选中)
		public int state;
	}
}
