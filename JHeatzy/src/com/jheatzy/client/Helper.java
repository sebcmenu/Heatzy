package com.jheatzy.client;

public class Helper {

	public static int getHourIndex(int ho,boolean ha) {
		return ho*2+(ha?1:0);
	}

	public static String getHourLabel(int ho,boolean ha) {
		return String.format("%02d",ho)+(ha?":30":":00");
	}

}
