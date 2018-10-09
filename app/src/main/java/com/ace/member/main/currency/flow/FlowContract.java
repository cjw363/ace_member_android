package com.ace.member.main.currency.flow;


import com.ace.member.bean.BalanceFlow;
import com.ace.member.bean.PartnerLoanFlow;

import java.util.List;

public class FlowContract {
	interface View {
		void addList(int nextPage, List<BalanceFlow> list, boolean isHint);
		String getCurrency();
	}

	interface Presenter {
		void start();

		void getList(int page);
	}
}
