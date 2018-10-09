package com.ace.member.main.third_party.samrithisak_loan.history;


import com.ace.member.bean.PartnerLoanFlow;

import java.util.List;

public interface HistoryContract {
	interface View {
		void addList(int nextPage,List<PartnerLoanFlow> list, boolean isHint);
	}

	interface Presenter {
		void start();
		void getList(int page);
	}
}
