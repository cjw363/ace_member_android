package com.ace.member.main.home.receive_to_acct.history;

public interface R2ARecentContract {
	interface View {
		void showList(int nextPage, boolean isHint);

		void showNextList(int nextPage);
	}

	interface Presenter {
		void start();

		void getHistoryList(int page);
	}
}
