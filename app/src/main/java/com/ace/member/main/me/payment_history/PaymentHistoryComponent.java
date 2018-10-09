package com.ace.member.main.me.payment_history;

import dagger.Component;

@Component(modules = PaymentHistoryPresenterModule.class)
public interface PaymentHistoryComponent {
	void inject(PaymentHistoryActivity activity);
}

