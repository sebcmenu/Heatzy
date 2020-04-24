package com.jheatzy.client;

import java.util.HashMap;
import java.util.Map;

public enum HeatingMode {
  CONFORT(0,"O"),ECO(1,"-"),HORSGEL(2,"*"),OFF(3," ");
	private int value;
	private String scheme;
	private HeatingMode(int value, String scheme) {
		this.value = value;
		this.scheme = scheme;
	}
	
	private static  Map<Integer,HeatingMode> qAccess;
	
	static {
		qAccess=new HashMap<Integer, HeatingMode>();
		for (HeatingMode m : values()) {
			qAccess.put(m.getValue(),m);
		}
	}
	
	public static HeatingMode of(int v) {
		return qAccess.get(v);
	}

	public int getValue() {
		return value;
	}

	public String getScheme() {
		return scheme;
	}
	
}
