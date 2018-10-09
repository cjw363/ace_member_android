package com.ace.member.main.third_party.lotto.lotto90.single;

import dagger.Component;

@Component(modules = Lotto90SinglePresenterModule.class)
public interface Lotto90SingleComponent {
	void inject(Lotto90SingleActivity activity);
}
