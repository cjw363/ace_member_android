package com.ace.member.main.third_party;


import dagger.Component;

@Component(modules = ThirdPartyPresenterModule.class)
public interface ThirdPartyComponent {
	void inject(ThirdPartyFragment fragment);
}
