package com.ace.member.main.home.transfer.to_member;

import dagger.Component;

@Component(modules = ToMemberPresenterModule.class)
public interface ToMemberComponent {
	void inject(ToMemberActivity ToMemberActivity);
}
