package com.jtager.libs.entity;

import java.io.Serializable;

public class AlarmEvent implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7527552945863119678L;
	private String id = "";
	private String flag = "";
	private String alarmDate = "";
	private String alarmTime = "";
	private String description = "";
	private String repeat = "1";// 1表示重复， 0 表示 1次 ，-1表示无
	private String action = "com.hehp.alarm";
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getAlarmDate() {
		return alarmDate;
	}
	public void setAlarmDate(String alarmDate) {
		this.alarmDate = alarmDate;
	}
	public String getAlarmTime() {
		return alarmTime;
	}
	public void setAlarmTime(String alarmTime) {
		this.alarmTime = alarmTime;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getRepeat() {
		return repeat;
	}
	public void setRepeat(String repeat) {
		this.repeat = repeat;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	@Override
	public String toString() {
		return "AlarmEvent [id=" + id + ", flag=" + flag + ", alarmDate="
				+ alarmDate + ", alarmTime=" + alarmTime + ", description="
				+ description + ", repeat=" + repeat + ", action=" + action
				+ "]";
	}
}
