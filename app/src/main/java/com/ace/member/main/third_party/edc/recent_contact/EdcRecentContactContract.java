package com.ace.member.main.third_party.edc.recent_contact;


import com.ace.member.bean.EdcBill;

import java.util.List;

public interface EdcRecentContactContract {
	interface View {
		void setList(List<EdcBill> list);

		void enableEmpty(boolean enable);
	}

	interface Presenter {
		void start();
	}
}
