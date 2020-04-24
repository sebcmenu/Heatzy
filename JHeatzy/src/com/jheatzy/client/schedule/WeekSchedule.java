package com.jheatzy.client.schedule;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jheatzy.client.HeatingMode;

public class WeekSchedule {
	private Map<DayOfWeek, DaySchedule> days=new HashMap<DayOfWeek, DaySchedule>();
	private List<WeeklyRange> ranges=new ArrayList<WeeklyRange>();
	private boolean needUpdate=true;
	
	public WeekSchedule() {
		for (DayOfWeek d : DayOfWeek.values()) 
			days.put(d, new DaySchedule(d, this));
	}
	
	void updateRequired() {
		needUpdate=true;
	}
	
	public DaySchedule getDaySchedule(DayOfWeek dw) {
		return days.get(dw);
	}
	public void clear(HeatingMode mode) {
		for (DaySchedule ds : days.values()) {
			ds.clear(mode);
		}
	}
	public void clear() {
		clear(HeatingMode.ECO);
	}
	
	public void applySchedule(WeeklyRange wr) {
		for (DayOfWeek dw : wr.getDays()) 
			days.get(dw).setMode(wr.getRange());
	}
	
	public List<WeeklyRange> getRanges() {
		if (needUpdate)
			computeRanges();
		return ranges;
	}
	
	public List<WeeklyRange> getRangesFiltered(HeatingMode mode) {
		getRanges();
		List<WeeklyRange> l=new ArrayList<WeeklyRange>();
		for (WeeklyRange weeklyRange : ranges) 
			if (weeklyRange.getRange().getMode()!=mode)
				l.add(weeklyRange);
		
		return l;
	}

	private void computeRanges() {
		ranges.clear();
		List<Range>[] dailyRanges=new ArrayList[DayOfWeek.values().length];
		for (DayOfWeek dw : DayOfWeek.values()) 
			dailyRanges[dw.getValue()-1]=days.get(dw).getRanges();
			
		for (int i = 0; i < dailyRanges.length; i++) {
			List<Range> l = dailyRanges[i];
			while (!l.isEmpty() ) {
				Range r = l.remove(0);
				WeeklyRange wr = new WeeklyRange(r, DayOfWeek.of(i+1));
				
				for (int j = i+1; j < dailyRanges.length; j++)
					if (dailyRanges[j].remove(r)) 
						wr.getDays().add(DayOfWeek.of(j+1));
					
				ranges.add(wr);
			}
		}		
	}
	
	public String toString() {
		StringBuffer res=new StringBuffer();
		for (DayOfWeek dw : DayOfWeek.values()) 
			res.append(days.get(dw)).append("\n");
		return res.toString();
	}
	
	
	public static void main(String[] args) {
		WeekSchedule ws = new WeekSchedule();
		ws.applySchedule(new WeeklyRange(new Range(HeatingMode.CONFORT, 8, false, 20, false), DayOfWeek.MONDAY,DayOfWeek.FRIDAY));
		System.out.println(ws);
	}
}
