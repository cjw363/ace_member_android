package com.ace.member.main.friends;

import dagger.Component;

@Component(modules = FriendsPresenterModule.class)
public interface FriendsComponent {
	void inject(FriendsFragment friendsFragment);
}
