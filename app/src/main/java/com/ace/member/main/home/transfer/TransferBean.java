package com.ace.member.main.home.transfer;

import com.ace.member.base.BaseBean;

public class TransferBean extends BaseBean {

	private String name;
	private String phone;
	private String time;
	private int type;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setName(String name){
		this.name=name;
	}
	public String getName(){
		return name;
	}
	public void setPhone(String phone){
		this.phone=phone;
	}
	public String getPhone(){
		return phone;
	}
	public void setTime(String time){
		this.time=time;
	}
	public String getTime(){
		return this.time;
	}
}
