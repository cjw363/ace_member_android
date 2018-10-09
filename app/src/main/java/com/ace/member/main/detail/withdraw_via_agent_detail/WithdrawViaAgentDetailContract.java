package com.ace.member.main.detail.withdraw_via_agent_detail;


import com.ace.member.bean.CashApplication;

import java.util.HashMap;

class WithdrawViaAgentDetailContract {
	interface WithdrawViaAgentDetailView {
		void setData(CashApplication data);
	}

	interface WithdrawViaAgentDetailPresenter {
		void getData(HashMap<String, String> params);
	}
}
