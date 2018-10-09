package com.ace.member.main.third_party.bill_payment.history;

import com.ace.member.bean.BillPaymentBean;

import java.util.List;

public interface BillPaymentHistoryContract {
	interface view{
		void addList(List<BillPaymentBean> list,int nextPage,boolean isHint);
	}
	interface presenter{
		void getBillPaymentList(int page);
	}
}
