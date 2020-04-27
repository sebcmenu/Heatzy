package com.jheatzy.client.test;

import java.time.DayOfWeek;

import org.json.JSONObject;

import com.jheatzy.client.HeatingMode;
import com.jheatzy.client.restio.JSONHelper;
import com.jheatzy.client.schedule.Range;
import com.jheatzy.client.schedule.WeekSchedule;
import com.jheatzy.client.schedule.WeeklyRange;

public class TestingSchedule {

	public static void main(String[] args) {

		WeekSchedule ws = new WeekSchedule();
		ws.applySchedule(new WeeklyRange(new Range(HeatingMode.CONFORT, 8, false, 21, false), DayOfWeek.MONDAY,
				DayOfWeek.FRIDAY));
		System.out.println(ws);
		JSONObject jso=new JSONObject();
		JSONHelper.fillScheduleData(jso, ws.getDaySchedule(DayOfWeek.MONDAY));
		System.out.println(jso);
		jso.put("p3_data6", (Long)85L);
		
		JSONHelper.fillScheduleFromJson(jso, ws, HeatingMode.HORSGEL);
		System.out.println(ws);
		
	}
}
