package com.ace.member.main.me;

import dagger.Component;

@Component(modules = MePresenterModule.class)
interface MeComponent {
	void inject(MeFragment fragment);
}

