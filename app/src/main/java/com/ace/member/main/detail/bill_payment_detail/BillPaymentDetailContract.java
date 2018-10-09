package com.ace.member.main.detail.bill_payment_detail;

import com.ace.member.main.third_party.bill_payment.history_detail.BillPaymentDetailBean;

interface BillPaymentDetailContract {
	interface view{
		void initDetail(BillPaymentDetailBean bean);
	}
	interface presenter{
		void getDetailData(int id);
	}
}
