package com.ace.member.main.third_party.lotto;

import com.ace.member.base.BaseBean;
import com.ace.member.bean.Balance;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LottoOutstandingBean extends BaseBean {
	@SerializedName("outstanding")
	private double outstanding;
	@SerializedName("balance")
	private List<Balance> list;

	public double getOutstanding() {
		return outstanding;
	}

	public void setOutstanding(double outstanding) {
		this.outstanding = outstanding;
	}

	public List<Balance> getList() {
		return list;
	}

	public void setList(List<Balance> list) {
		this.list = list;
	}
}

