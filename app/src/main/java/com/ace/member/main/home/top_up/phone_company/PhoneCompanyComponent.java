package com.ace.member.main.home.top_up.phone_company;

import dagger.Component;

@Component(modules = PhoneCompanyPresenterModule.class)
public interface PhoneCompanyComponent {
	void inject(PhoneCompanyFragment fragment);
}

