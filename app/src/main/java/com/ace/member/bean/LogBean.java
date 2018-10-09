package com.ace.member.bean;

import com.ace.member.base.BaseBean;

public class LogBean extends BaseBean {
	private String time;
	private String ip;
	private String remark;
	private String date;
	private boolean isHead=false;
	public void setTime(String time){
		this.time=time;
	}
	public String getTime(){
		return time;
	}
	public void setIp(String ip){
		this.ip=ip;
	}
	public String getIp(){
		return this.ip;
	}
	public void setRemark(String remark){
		this.remark=remark;
	}
	public String getRemark(){
		return this.remark;
	}

	public void setDate(String date){
		this.date=date;
	}
	public String getDate(){
		return this.date;
	}
	public void setHead(boolean isHead){
		this.isHead=isHead;
	}
	public boolean getHead(){
		return  this.isHead;
	}
}
