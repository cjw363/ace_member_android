package com.ace.member.main.third_party.bill_payment.history;

import dagger.Component;

@Component(modules = BillPaymentHistoryPresenterModule.class)
public interface BillPaymentHistoryComponent {
	void inject(BillPaymentHistoryActivity activity);
}
