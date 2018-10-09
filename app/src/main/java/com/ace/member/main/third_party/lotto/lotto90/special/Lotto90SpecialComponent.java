package com.ace.member.main.third_party.lotto.lotto90.special;


import dagger.Component;

@Component(modules = Lotto90SpecialPresenterModule.class)
public interface Lotto90SpecialComponent {
	void inject(Lotto90SpecialActivity activity);
}
