package com.ace.member.main.home.top_up.recent_order;

import com.ace.member.bean.TopUpOrder;

import java.util.List;

public interface RecentOrderContract {

	interface View {
		void addOrderList(int nextPage,List<TopUpOrder> list, boolean isHint);
	}

	interface Presenter {
		void start();

		void getTopUpOrderList(int page);
	}
}
