package com.ace.member.main.friends.chat.chat_info.transfer_history;

import dagger.Component;

@Component(modules = FriendTransferHistoryPresenterModule.class)
public interface FriendTransferHistoryComponent {
	void inject(FriendTransferHistoryActivity friendTransferHistoryActivity);
}
