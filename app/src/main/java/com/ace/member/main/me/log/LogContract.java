package com.ace.member.main.me.log;

public interface LogContract {

	interface LogView {
		void showLogData(int nextPage, boolean isHint);
		void showNextLogData(int page);
	}

	interface LogPresenter{
		void getLogList(int page);
	}

}
