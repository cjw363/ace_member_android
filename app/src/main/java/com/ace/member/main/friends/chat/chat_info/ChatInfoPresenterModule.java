package com.ace.member.main.friends.chat.chat_info;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class ChatInfoPresenterModule {
	private final ChatInfoContract.ChatInfoView mView;
	private final Context mContext;

	public ChatInfoPresenterModule(ChatInfoContract.ChatInfoView view, Context context) {
		mView = view;
		mContext = context;
	}

	@Provides
	ChatInfoContract.ChatInfoView provideChatView() {
		return mView;
	}

	@Provides
	Context provideContext() {
		return mContext;
	}
}
