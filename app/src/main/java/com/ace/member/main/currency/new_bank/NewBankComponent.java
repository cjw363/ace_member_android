package com.ace.member.main.currency.new_bank;

import dagger.Component;

@Component(modules = NewBankPresenterModule.class)
public interface NewBankComponent {
	void inject(NewBankFragment fragment);
}

