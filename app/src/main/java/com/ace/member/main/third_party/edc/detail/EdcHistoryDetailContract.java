package com.ace.member.main.third_party.edc.detail;


import com.ace.member.bean.EdcBill;

public interface EdcHistoryDetailContract {
	interface View {
		void setBill(EdcBill bill);
	}

	interface Presenter {
		void start(int id);
	}
}
