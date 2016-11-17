package com.mx.util.hft;

public class HftCardType {
	int mainType;
	int subType;
	String mainTips;
	String subTips;
	String oldTips;
	
	
	public String getOldTips() {
		return oldTips;
	}
	public void setOldTips(String oldTips) {
		this.oldTips = oldTips;
	}
	public int getMainType() {
		return mainType;
	}
	public void setMainType(int mainType) {
		this.mainType = mainType;
	}
	public int getSubType() {
		return subType;
	}
	public void setSubType(int subType) {
		this.subType = subType;
	}
	public String getMainTips() {
		return mainTips;
	}
	public void setMainTips(String mainTips) {
		this.mainTips = mainTips;
	}
	public String getSubTips() {
		return subTips;
	}
	public void setSubTips(String subTips) {
		this.subTips = subTips;
	}
	
	
	
	@Override
	public String toString() {
		return "HftCardType [mainType=" + mainType + ", subType=" + subType
				+ ", mainTips=" + mainTips + ", subTips=" + subTips + "]";
	}
	
}
