package com.ace.member.main.detail.withdraw_detail;

import com.ace.member.bean.BalanceRecord;

public interface WithdrawDetailContract {

	interface View {
		void setBalanceRecord(BalanceRecord record);
	}

	interface Presenter {
		void getBalanceRecord(int id);
	}
}
