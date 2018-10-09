package com.ace.member.main.home.ace_loan.ace_loan_prepayment;

import com.ace.member.base.BaseBean;
import com.ace.member.bean.ACELoanPrepaymentBean;
import com.ace.member.bean.Balance;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ACELoanPrepaymentData extends BaseBean {
	@SerializedName("loan_list")
	private List<ACELoanPrepaymentBean> list;
	@SerializedName("balance_list")
	private List<Balance> listBalance;

	public List<ACELoanPrepaymentBean> getList() {
		return list;
	}

	public void setList(List<ACELoanPrepaymentBean> list) {
		this.list = list;
	}

	public List<Balance> getListBalance() {
		return listBalance;
	}

	public void setListBalance(List<Balance> list) {
		listBalance = list;
	}

}
