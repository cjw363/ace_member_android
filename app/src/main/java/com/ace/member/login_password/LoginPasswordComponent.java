package com.ace.member.login_password;

import dagger.Component;

@Component(modules = LoginPasswordPresenterModule.class)
public interface LoginPasswordComponent {
	void inject(LoginPasswordActivity activity);
}

