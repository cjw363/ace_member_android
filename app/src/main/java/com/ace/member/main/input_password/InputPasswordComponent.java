package com.ace.member.main.input_password;

import dagger.Component;

@Component(modules = InputPasswordPresenterModule.class)
public interface InputPasswordComponent {
	void inject(InputPasswordFragment fragment);
}

