package com.ace.member.main.friends.chat.chat_info;

import dagger.Component;

@Component(modules = ChatInfoPresenterModule.class)
public interface ChatInfoComponent {
	void inject(ChatInfoActivity chatInfoActivity);
}
