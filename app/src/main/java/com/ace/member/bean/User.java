package com.ace.member.bean;

import com.ace.member.BuildConfig;
import com.ace.member.base.BaseBean;

public class User extends BaseBean {
	private int id;
	private String name;
	private int type;
	private String phone;
	private int level;
	private int status;
	private boolean reset_password;
	private String gesture_password;
	private int status_trading_password;
	private String portrait;

	public void setId(int id){
		this.id=id;
	}
	public int getId(){
		return this.id;
	}
	public void setName(String name){
		this.name=name;
	}
	public String getName(){
		return name;
	}
	public void setType(int type){
		this.type=type;
	}
	public int getType(){
		return type;
	}
	public void setPhone(String phone){
		this.phone=phone;
	}
	public String getPhone(){
		return phone;
	}
	public void setLevel(int level){
		this.level=level;
	}
	public int getLevel(){
		return level;
	}
	public void setStatus(int status){
		this.status=status;
	}
	public int getStatus(){
		return status;
	}
	public void setGesturePassword(String gesture_password){
		this.gesture_password=gesture_password;
	}
	public String getGesturePassword(){
		return gesture_password;
	}
	public void setResetPassword(boolean reset_password){
		this.reset_password=reset_password;
	}
	public boolean getResetPassword(){
		return reset_password;
	}
	public void setStatusTradingPassword(int status){
		this.status_trading_password=status;
	}
	public int getStatusTradingPassword(){
		return status_trading_password;
	}
	public String getPortrait() {
		return portrait;
	}
	public void setPortrait(String portrait) {
		this.portrait = portrait;
	}

	public String getRealNormalPortrait() {
		return BuildConfig.FILE_BASE_URL + "images/normal/" + portrait;
	}

	public String getRealThumbnailPortrait() {
		return BuildConfig.FILE_BASE_URL + "images/thumbnails/" + portrait;
	}

}
