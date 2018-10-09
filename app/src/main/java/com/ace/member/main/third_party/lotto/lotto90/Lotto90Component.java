package com.ace.member.main.third_party.lotto.lotto90;

import dagger.Component;

@Component(modules = Lotto90PresenterModule.class)
public interface Lotto90Component {
	void inject(Lotto90Activity activity);
}
