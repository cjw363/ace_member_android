package com.ace.member.main.friends.chat.new_friends;

import dagger.Component;

@Component(modules = NewFriendsModule.class)
public interface NewFriendsComponent {
	void inject(NewFriendsActivity newFriendsActivity);
}
