package com.ace.member.main.third_party.wsa.recent_contact;


import com.ace.member.bean.EdcBill;
import com.ace.member.bean.WsaBill;

import java.util.List;

public interface WsaRecentContactContract {
	interface View {
		void setList(List<WsaBill> list);

		void enableEmpty(boolean enable);
	}

	interface Presenter {
		void start();
	}
}
