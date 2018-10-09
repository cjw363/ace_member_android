package com.ace.member.main.detail.deposit_detail;

import com.ace.member.bean.BalanceRecord;

public interface DepositDetailContract {

	interface View {
		void setBalanceRecord(BalanceRecord record);
	}

	interface Presenter {
		void getBalanceRecord(int id);
	}
}
