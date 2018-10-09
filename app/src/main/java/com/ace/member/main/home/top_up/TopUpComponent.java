package com.ace.member.main.home.top_up;

import dagger.Component;

@Component(modules = TopUpPresenterModule.class)
public interface TopUpComponent {
	void inject(TopUpActivity activity);
}

