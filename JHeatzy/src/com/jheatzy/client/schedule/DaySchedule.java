package com.jheatzy.client.schedule;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.jheatzy.client.HeatingMode;
import com.jheatzy.client.Helper;

public class DaySchedule {
	private DayOfWeek wDay;
	private List<Range> ranges = new ArrayList<Range>();
	private boolean needUpdate = true;

	private HeatingMode[] rawData = new HeatingMode[48];
	private WeekSchedule week;

	DaySchedule(DayOfWeek wDay,WeekSchedule ws) {
		super();
		this.wDay = wDay;
		this.week=ws;
		clear();
	}

	private void updateRequired() {
		needUpdate=true;
		week.updateRequired();
	}
	
	public void setMode(Range r) {
		updateRequired();
		Arrays.fill(rawData, r.getStartIndex(),r.getEndIndex(),r.getMode());
	}
	
	public void setMode(HeatingMode mode,int index) {
		updateRequired();
		rawData[index]=mode;
	}
	
	public HeatingMode getMode(int index) {
		return rawData[index];
	}
	
	public HeatingMode getMode(int hour,boolean half) {
		return getMode(Helper.getHourIndex(hour, half));
	}
	
	public void clear(HeatingMode mode) {
		updateRequired();
		Arrays.fill(rawData, mode);
	}
	public void clear() {
		clear(HeatingMode.ECO);
	}
	
	public List<Range> getRanges() {
		if (needUpdate)
			computeRanges();
		return new ArrayList<Range>(ranges);
	}

	private void computeRanges() {
		ranges.clear();
		HeatingMode currentMode=null;
		int startindex=0;
		
		for (int i = 0; i < rawData.length; i++) {
			if (currentMode!=rawData[i]) {
				if (currentMode!=null) 
					ranges.add(new Range(currentMode, startindex, i));
				startindex=i;
				currentMode=rawData[i];
			}
		}
		ranges.add(new Range(currentMode, startindex, rawData.length));
	}
	
	public String toString() {
		StringBuffer buf=new StringBuffer(String.format("%-10s",wDay.toString()));
		buf.append(" : [");
		for (int i = 0; i < rawData.length; i++) {
			buf.append(rawData[i].getScheme());
		}
		buf.append("]");
		return buf.toString();
	}
}
