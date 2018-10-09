package com.ace.member.main.friends.chat.friend_profile;

import dagger.Component;

@Component(modules = FriendProfileModule.class)
public interface FriendProfileComponent {
	void inject(FriendProfileActivity friendProfileActivity);
}
