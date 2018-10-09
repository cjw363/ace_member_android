package com.ace.member.main.me.password;

import dagger.Component;

@Component(modules = PasswordPresenterModule.class)
public interface PasswordComponent {
	void inject(PasswordActivity passwordActivity);
}
