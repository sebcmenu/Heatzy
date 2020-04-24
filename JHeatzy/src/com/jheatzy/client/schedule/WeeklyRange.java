package com.jheatzy.client.schedule;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WeeklyRange {

	private Range r;
	private List<DayOfWeek> days;
	
	public WeeklyRange(Range r,DayOfWeek...ds) {
		this.r=r;
		days=new ArrayList<DayOfWeek>(Arrays.asList(ds));
	}

	public Range getRange() {
		return r;
	}

	public List<DayOfWeek> getDays() {
		return days;
	}
	
	public void extendToDay(DayOfWeek d) {
		if (!days.contains(d))
			days.add(d);
	}
	
	public String toString() {
		StringBuffer b=new StringBuffer(r.toString());
		b.append(" {");
		for (DayOfWeek dayOfWeek : days) {
			b.append(dayOfWeek.toString()).append(",");
		}
		b.setLength(b.length()-1);
		b.append("}");
		return b.toString();
	}
	
}
