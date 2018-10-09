package com.ace.member.bean;

import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;


public class Product extends BaseBean {

	private int id;
	@SerializedName("diff_to_close")
	private int diffToClose;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getDiffToClose() {
		return diffToClose;
	}

	public void setDiffToClose(int diffToClose) {
		this.diffToClose = diffToClose;
	}
}
