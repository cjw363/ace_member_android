package com.ace.member.main.third_party.bill_payment.fragment;

import com.ace.member.bean.BillerBean;

import java.util.List;

public interface BillerContract {
	interface View{
		void initListView(List<BillerBean> list);
	}
	interface Presenter{
		void getBiller(int type);
	}
}
