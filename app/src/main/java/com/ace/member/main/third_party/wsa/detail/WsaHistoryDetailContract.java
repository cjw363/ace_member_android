package com.ace.member.main.third_party.wsa.detail;


import com.ace.member.bean.WsaBill;

public interface WsaHistoryDetailContract {
	interface View {
		void setBill(WsaBill bill);
	}

	interface Presenter {
		void start(int id);
	}
}
