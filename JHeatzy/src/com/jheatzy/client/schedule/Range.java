package com.jheatzy.client.schedule;

import com.jheatzy.client.HeatingMode;
import com.jheatzy.client.Helper;

public class Range {
	private int startH, endH;
	private boolean halfS, halfE;
	private HeatingMode mode;
	private int startIndex;
	private int endIndex;
	
	public Range(HeatingMode mode, int startH, boolean halfS, int endH, boolean halfE) {
		startIndex = Helper.getHourIndex(startH, halfS);
		endIndex = Helper.getHourIndex(endH, halfE);
		if (startIndex<0 || endIndex>48 || startIndex>=endIndex)
			throw new InvalidRangeException("Invalid Range :"+Helper.getHourLabel(startH, halfS)+" - "+Helper.getHourLabel(endH, halfE));
		
		this.mode = mode;
		this.startH = startH;
		this.halfS = halfS;
		this.endH = endH;
		this.halfE = halfE;
	}
	
	Range(HeatingMode mode, int startIndex,  int endIndex) {
		this.startIndex=startIndex;
		this.endIndex=endIndex;
		if (startIndex<0 || endIndex>48 || startIndex>=endIndex)
			throw new InvalidRangeException("Invalid Range :"+Helper.getHourLabel(startH, halfS)+" - "+Helper.getHourLabel(endH, halfE));
		
		this.mode = mode;
		this.startH = startIndex/2;
		this.halfS = ((startIndex % 2)==1);
		this.endH = endIndex/2;
		this.halfE = ((endIndex % 2)==1);
	}
	
	
	
	
	int getStartIndex() {
		return startIndex;
	}
	int getEndIndex() {
		return endIndex;
	}

	public int getStartH() {
		return startH;
	}

	public int getEndH() {
		return endH;
	}

	public boolean isHalfS() {
		return halfS;
	}

	public boolean isHalfE() {
		return halfE;
	}

	public HeatingMode getMode() {
		return mode;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + endIndex;
		result = prime * result + ((mode == null) ? 0 : mode.hashCode());
		result = prime * result + startIndex;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Range other = (Range) obj;
		if (endIndex != other.endIndex)
			return false;
		if (mode != other.mode)
			return false;
		if (startIndex != other.startIndex)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return ""+mode+Helper.getHourLabel(startH, halfS)+" - "+Helper.getHourLabel(endH, halfE);
	}

	
	
}
