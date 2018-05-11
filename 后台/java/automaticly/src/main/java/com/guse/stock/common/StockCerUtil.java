package com.guse.stock.common;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

/**
 * 库存 凭证 基础方法
 * @author 不能
 *
 */
public class StockCerUtil {

	/**
	 * 生成随机订单编号
	 * @return
	 */
	public String randomOrder() {
		List<String> yearList = new ArrayList<String>();
		yearList.add("A");
		yearList.add("B");
		yearList.add("C");
		yearList.add("D");
		yearList.add("E");
		yearList.add("F");
		yearList.add("G");
		yearList.add("H");
		yearList.add("I");
		yearList.add("J");
		
		Calendar now = Calendar.getInstance();
		String year = yearList.get(now.get(Calendar.YEAR) - 2011);
		String month = now.get(Calendar.MONTH)+1 > 9 ? Integer.toString(now.get(Calendar.MONTH)+1) : "0"+(now.get(Calendar.MONTH)+1);
		String day = now.get(Calendar.DAY_OF_MONTH) > 9 ? Integer.toString(now.get(Calendar.DAY_OF_MONTH)) : "0"+now.get(Calendar.DAY_OF_MONTH);
		String hour = now.get(Calendar.HOUR_OF_DAY) > 9 ? Integer.toString(now.get(Calendar.HOUR_OF_DAY)) : "0"+now.get(Calendar.HOUR_OF_DAY);
		String minute = now.get(Calendar.MINUTE) > 9 ? Integer.toString(now.get(Calendar.MINUTE)) : "0"+now.get(Calendar.MINUTE);
		String second = now.get(Calendar.SECOND) > 9 ? Integer.toString(now.get(Calendar.SECOND)) : "0"+now.get(Calendar.SECOND);
		Random random=new Random();
		String randomNumber = Integer.toString(random.nextInt(100000)+10000); 
		String orderNum = year+month+day+hour+minute+second+randomNumber;
		return orderNum;
				
	}
}
