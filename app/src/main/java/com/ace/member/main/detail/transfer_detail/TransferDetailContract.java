package com.ace.member.main.detail.transfer_detail;


import com.ace.member.bean.TransferRecent;

import java.util.HashMap;

class TransferDetailContract {
	interface TransferDetailView {
		void setAmountDetail(TransferRecent data);

		void setFeeDetail(TransferRecent data);

		void setInDetail(TransferRecent data);
	}

	interface TransferDetailPresenter {
		void getTransferAmountData(HashMap<String, String> params);

		void getTransferFeeData(HashMap<String, String> params);

		void getTransferInData(HashMap<String, String> params);
	}
}
