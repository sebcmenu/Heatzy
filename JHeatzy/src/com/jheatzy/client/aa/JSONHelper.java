package com.jheatzy.client.aa;

import java.time.DayOfWeek;

import org.json.JSONObject;

import com.jheatzy.client.schedule.DaySchedule;
import com.jheatzy.client.schedule.WeekSchedule;

public class JSONHelper {

	public static void fillScheduleData(JSONObject jso,WeekSchedule ws) {
		for (DayOfWeek dw : DayOfWeek.values()) {
			DaySchedule ds=ws.getDaySchedule(dw);
			
			int cycle=0;
			int value=0;
			for (int index=47;index>=0;index++) {
				value<<=2;
				value+=ds.getMode(index).getValue();				
				cycle++;
				if (cycle==4) {
					jso.put("p"+dw.getValue()+1+"_data"+((int)(index/4)+1), value);
					cycle=0;
					value=0;
				}
			}
		}
	}
	
	public static void main(String[] args) {
		JSONObject js=new JSONObject();
	}
}
