package com.guse.platform.utils;

public class Vip {
	
	/**
	 * 获取vip等级
	 * @param recharge
	 * @return
	 */
	public static int getVipRank(long recharge){
		int rank = 0;
		if(recharge==0){
			rank = 0;
   		}else if(recharge>=1&&recharge<=10){
   			rank = 1;
   		}else if(recharge>=11&&recharge<=30){
   			rank = 2;
   		}else if(recharge>=31&&recharge<=100){
   			rank = 3;
   		}else if(recharge>=101&&recharge<=200){
   			rank = 4;
   		}else if(recharge>=201&&recharge<=500){
   			rank = 5;
   		}else if(recharge>=501&&recharge<=1000){
   			rank = 6;
   		}else if(recharge>=1001&&recharge<=2000){
   			rank = 7;
   		}else if(recharge>=2001&&recharge<=5000){
   			rank = 8;
   		}else if(recharge>=5001&&recharge<=10000){
   			rank = 9;
   		}else if(recharge>=10001&&recharge<=20000){
   			rank = 10;
   		}else if(recharge>=20001&&recharge<=50000){
   			rank = 11;
   		}else if(recharge>=50001&&recharge<=100000){
   			rank = 12;
   		}else if(recharge>=100001&&recharge<=150000){
   			rank = 13;
   		}else if(recharge>=150001&&recharge<=200000){
   			rank = 14;
   		}else if(recharge>=200001&&recharge<=300000){
   			rank = 15;
   		}
		return rank;
	}

}
