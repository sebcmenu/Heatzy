package com.jheatzy.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jheatzy.client.restio.HeatzyAPI;
import com.jheatzy.client.restio.TokenProvider;

public class Account implements TokenProvider {
	private String userToken;
	private String login, password;
	
	private Map<String,Device> devices;
	private HeatzyAPI api;

	public Account(String login, String password) {
		super();
		this.login = login;
		this.password = password;
	}

	public String getToken() {
		if (userToken == null) {
			try {
				userToken = getAPI().connect(login, password);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return userToken;
	}

	public List<Device> getDevices() {
		
		return new ArrayList<Device>(getDMap().values());
	}

	public Device getDevice(String alias) {
		return getDMap().get(alias);
	}
	
	private Map<String, Device> getDMap() {
		if (devices == null) {
		try {
			devices = new HashMap<String, Device>();
			for (Device d : getAPI().listDevices()) {
				devices.put(d.getAlias(),d);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		return devices;
	}

	private synchronized HeatzyAPI getAPI() {
		if (api == null)
			api = new HeatzyAPI(this);
		return api;
	}

}
