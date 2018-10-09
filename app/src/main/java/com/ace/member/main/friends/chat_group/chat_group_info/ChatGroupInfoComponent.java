package com.ace.member.main.friends.chat_group.chat_group_info;

import dagger.Component;

@Component(modules = ChatGroupInfoPresenterModule.class)
public interface ChatGroupInfoComponent {
	void inject(ChatGroupInfoActivity chatGroupInfoActivity);
}
