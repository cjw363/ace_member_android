package com.ace.member.main.third_party.lotto.lotto90.multiple;

import dagger.Component;


@Component(modules = Lotto90MultiplePresenterModule.class)
public interface Lotto90MultipleComponent {
	void inject(Lotto90MultipleActivity activity);
}
