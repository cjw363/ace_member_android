package com.ace.member.main.friends.chat;

import dagger.Component;

@Component(modules = ChatPresenterModule.class)
public interface ChatComponent {
	void inject(ChatActivity chatActivity);
}
