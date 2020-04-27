package com.jheatzy.client;

import com.jheatzy.client.restio.HeatzyAPI;
import com.jheatzy.client.schedule.WeekSchedule;

public class Device {
	private String id, alias;
	private WeekSchedule ws;
	private HeatzyAPI api;

	public Device(HeatzyAPI api, String id) {
		super();
		this.id = id;
		this.api = api;
	}

	public String getId() {
		return id;
	}

	public String getAlias() {
		return alias;
	}

	public WeekSchedule getWs() {
		return ws;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public void switchTo(HeatingMode mode) {
		try {
			api.changeMode(this, mode);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public WeekSchedule getSchedule() {
		if (ws == null) {
			try {
				ws = api.getDeviceSchedule(this);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return ws;
	}



	@Override
	public String toString() {
		return "Device [id=" + id + ", alias=" + alias + "]";
	}

}
