package com.ace.member.main.detail.bill_payment_detail;

import dagger.Component;

@Component(modules = BillPaymentDetailPresenterModule.class)
interface BillPaymentDetailComponent {
	void inject(BillPaymentDetailActivity activity);
}
