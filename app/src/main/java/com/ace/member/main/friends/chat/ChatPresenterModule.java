package com.ace.member.main.friends.chat;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class ChatPresenterModule {
	private final ChatContract.ChatView mView;
	private final Context mContext;

	public ChatPresenterModule(ChatContract.ChatView view, Context context) {
		mView = view;
		mContext = context;
	}

	@Provides
	ChatContract.ChatView provideChatView() {
		return mView;
	}

	@Provides
	Context provideContext() {
		return mContext;
	}
}
