package com.jheatzy.client.restio;

import java.time.DayOfWeek;

import org.json.JSONObject;

import com.jheatzy.client.HeatingMode;
import com.jheatzy.client.schedule.DaySchedule;
import com.jheatzy.client.schedule.WeekSchedule;

public class JSONHelper {

	public static void fillScheduleData(JSONObject jso,WeekSchedule ws) {
		for (DayOfWeek dw : DayOfWeek.values()) {
			DaySchedule ds=ws.getDaySchedule(dw);
			
			fillScheduleData(jso, ds);
		}
	}

	public static void fillScheduleData(JSONObject jso, DaySchedule ds) {
		DayOfWeek dw = ds.getDayOfWeek();
		int cycle=0;
		int value=0;
		for (int index=47;index>=0;index--) {
			value<<=2;
			value+=ds.getMode(index).getValue();				
			cycle++;
			if (cycle==4) {
				jso.put("p"+(dw.getValue())+"_data"+((int)(index/4)+1),(long)value);
				cycle=0;
				value=0;
			}
		}
	}
	
	public static void fillScheduleFromJson(JSONObject jso, DaySchedule ds,HeatingMode def) {
		ds.clear(def);
		for (int i=1;i<13;i++) {
			String key = "p"+(ds.getDayOfWeek().getValue())+"_data"+i;
			if (jso.has(key)) {
				int v=jso.getInt(key);
				int cycle=0;
				while (cycle<4) {
					ds.setMode(HeatingMode.of((int)v % 4),(i-1)*4+cycle);					
					v>>=2;
					cycle++;
				}
			}
		}
	}
	
	
	public static void fillScheduleFromJson(JSONObject jso, WeekSchedule ds,HeatingMode def) {
		for (DayOfWeek dw : DayOfWeek.values()) {
			fillScheduleFromJson(jso,ds.getDaySchedule(dw),def);
		}
	}

	
}
