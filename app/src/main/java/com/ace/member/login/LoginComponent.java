package com.ace.member.login;

import dagger.Component;

@Component(modules = LoginPresenterModule.class)
public interface LoginComponent {
	void inject(LoginActivity loginActivity);
}
