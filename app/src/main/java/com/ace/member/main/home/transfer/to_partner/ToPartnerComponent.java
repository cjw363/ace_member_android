package com.ace.member.main.home.transfer.to_partner;

import dagger.Component;

@Component(modules = ToPartnerPresenterModule.class)
public interface ToPartnerComponent {
	void inject(ToPartnerActivity ToPartnerActivity);
}
