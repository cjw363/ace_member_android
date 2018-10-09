package com.ace.member.main.friends.chat_group.chat_group_info;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class ChatGroupInfoPresenterModule {
	private final ChatGroupInfoContract.ChatInfoView mView;
	private final Context mContext;

	public ChatGroupInfoPresenterModule(ChatGroupInfoContract.ChatInfoView view, Context context) {
		mView = view;
		mContext = context;
	}

	@Provides
	ChatGroupInfoContract.ChatInfoView provideChatView() {
		return mView;
	}

	@Provides
	Context provideContext() {
		return mContext;
	}
}
