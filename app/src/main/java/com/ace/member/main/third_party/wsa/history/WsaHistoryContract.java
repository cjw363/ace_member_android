package com.ace.member.main.third_party.wsa.history;


import com.ace.member.bean.EdcBill;
import com.ace.member.bean.WsaBill;

import java.util.List;

public interface WsaHistoryContract {
	interface View {
		void addList(int nextPage, List<WsaBill> list, boolean isHint);
	}

	interface Presenter {
		void getWsaBillList(int page);
	}
}
