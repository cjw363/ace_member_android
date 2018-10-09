package com.ace.member.main.bottom_dialog;

import com.ace.member.base.BaseBean;

public class ControllerBean extends BaseBean {
	private int id;
	private int iconID=0;
	private String content1;
	private String content2;
	private boolean isChoose;//是否被中

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public void setIvRes(int id) {
		iconID = id;
	}

	public int getIconID() {
		return iconID;
	}

	public void setContent1(String content) {
		this.content1 = content;
	}

	public String getContent1() {
		return content1;
	}

	public void setContent2(String content) {
		this.content2 = content;
	}

	public String getContent2() {
		return content2;
	}

	public void setChoose(boolean choose) {
		this.isChoose = choose;
	}

	public boolean getChoose() {
		return isChoose;
	}
}
