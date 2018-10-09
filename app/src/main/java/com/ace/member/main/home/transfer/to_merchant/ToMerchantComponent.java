package com.ace.member.main.home.transfer.to_merchant;

import dagger.Component;

@Component(modules = ToMerchantPresenterModule.class)
public interface ToMerchantComponent {
	void inject(ToMerchantActivity ToMerchantActivity);
}
