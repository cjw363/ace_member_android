package com.ace.member.main.third_party.bill_payment.fragment;

import dagger.Component;
import dagger.Module;

@Component(modules = BillerPresenterModule.class)
public interface BillerComponent {
	void inject(BillerFragment fragment);
}
