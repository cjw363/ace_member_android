package com.ace.member.start;

import dagger.Component;

@Component(modules = StartPresenterModule.class)
public interface StartComponent {
	void inject(StartActivity activity);
}

