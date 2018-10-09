package com.ace.member.main.third_party.edc.history;


import com.ace.member.bean.EdcBill;

import java.util.List;

public interface EdcHistoryContract {
	interface View {
		void addList(int nextPage, List<EdcBill> list, boolean isHint);
	}

	interface Presenter {
		void getEdcWsaBillList(int page);
	}
}
