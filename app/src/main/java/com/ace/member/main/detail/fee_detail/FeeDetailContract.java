package com.ace.member.main.detail.fee_detail;


import com.ace.member.bean.Fee;

import java.util.HashMap;

class FeeDetailContract {
	interface FeeDetailView {
		void setWithdrawFeeData(Fee data);

		void setBankFeeData(Fee data);
	}

	interface FeeDetailPresenter {
		void getWithdrawFeeData(HashMap<String, String> params, int mainSubType);

		void getBankFeeData(HashMap<String, String> params);
	}
}
