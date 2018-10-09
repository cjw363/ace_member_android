package com.ace.member.main.third_party.bill_payment;

import dagger.Component;

@Component(modules = BillPaymentPresenterModule.class)
public interface BillPaymentComponent {
	void inject(BillPaymentActivity activity);
}
