package com.palmjoys.yf1b.act.framework.utils;

public class RandomUtils {
	/**
	 * 区间随机
	 * 
	 * @param zones
	 *            各个区间对应的长度,总长为各区间长的和 例如: double[] zones = {0.25,0.5,0.5}
	 *            将会在0-1.25之间随机. 若随出0.15,将会返回0 若随出0.35,则返回1 若随出1.1,则返回2
	 * @return 命中的区间的index,如果未能命中,返回-1
	 */
	public static int zoneRandom(double... zones) {
		double total = 0;
		for (double d : zones) {
			if (d < 0) {
				return -1;
			}
			total += d;
		}
		double r = Math.random() * total;
		for (int i = 0; i < zones.length; ++i) {
			if (r < zones[i]) {
				return i;
			} else {
				r -= zones[i];
			}
		}
		return -1;
	}

	public static double getRandomNum(double min, double max, int accuracyBit) {
		double accuracy = Math.pow(10, accuracyBit);
		double newMin = min * accuracy;
		double newMax = max * accuracy;
		long randomNum = Math.round(Math.random() * (newMax - newMin) + newMin);
		return (double) randomNum / (double) accuracy;
	}
	
	public static int getRandomIntNum(int  min, int  max)
	{
		int newMin = min ;
		int newMax = max ;
		int randomNum = (int) Math.round(Math.random() * (newMax - newMin) + newMin);
		return  randomNum ;
	}
	
	
	public static void main(String[] args) {
		
		for( int i = 0  ;i< 100 ;i++)
		{
			System.out.println(getRandomIntNum(0,10));
		}
	}

	public static Integer[] shuffle(int size) {
		Integer[] indexs = new Integer[size];
		for (int i = 0; i < size; i++) {
			indexs[i] = i;
		}

		for (int index = 0; index < size; index++) {
			int value = org.treediagram.nina.core.math.RandomUtils.betweenInt(0, size, false);
			int temp = indexs[index];
			indexs[index] = indexs[value];
			indexs[value] = temp;
		}

		return indexs;
	}
}
