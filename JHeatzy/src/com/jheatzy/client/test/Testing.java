package com.jheatzy.client.test;

import com.jheatzy.client.Account;
import com.jheatzy.client.Device;
import com.jheatzy.client.HeatingMode;

public class Testing {
	public static void main(String[] args) {
		Account a = new Account(System.getProperty("login"),System.getProperty("password"));
		System.out.println(a.getDevices());
		Device d = a.getDevice("Bureau");
		d.switchTo(HeatingMode.ECO);
		System.out.println(d.getSchedule());
		

	}
}
