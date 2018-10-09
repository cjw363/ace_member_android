package com.ace.member.main.third_party.bill_payment;

import com.ace.member.base.BaseBean;
import com.ace.member.bean.BillerBean;
import com.ace.member.bean.BillerConfig;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BillPaymentData extends BaseBean {

	@SerializedName("biller")
	private BillerBean biller;
	@SerializedName("config")
	private List<BillerConfig> list;
	@SerializedName("is_function")
	private boolean isRunning;

	public void setBiller(BillerBean biller) {
		this.biller = biller;
	}

	public BillerBean getBiller() {
		return biller;
	}

	public void setList(List<BillerConfig> list) {
		this.list = list;
	}

	public List<BillerConfig> getList() {
		return list;
	}

	public void setRunning(boolean running) {
		this.isRunning = running;
	}

	public boolean getRunning() {
		return this.isRunning;
	}
}
