package com.ace.member.main.third_party.bill_payment.history_detail;

public interface BillPaymentHistoryDetailContract {
	interface view{
		void initDetail(BillPaymentDetailBean bean);
	}
	interface presenter{
		void getDetailData(int id);
	}
}
