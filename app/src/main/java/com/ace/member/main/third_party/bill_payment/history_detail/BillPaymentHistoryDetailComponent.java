package com.ace.member.main.third_party.bill_payment.history_detail;

import dagger.Component;

@Component(modules = BillPaymentHistoryDetailPresenterModule.class)
public interface BillPaymentHistoryDetailComponent {
	void inject(BillPaymentHistoryDetailActivity activity);
}
