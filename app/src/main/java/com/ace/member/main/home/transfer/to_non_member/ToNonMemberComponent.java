package com.ace.member.main.home.transfer.to_non_member;

import dagger.Component;

@Component(modules = ToNonMemberPresenterModule.class)
public interface ToNonMemberComponent {
	void inject(ToNonMemberActivity ToNonMemberActivity);
}
