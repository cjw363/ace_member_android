package com.ace.member.main.friends.chat.new_friends;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class NewFriendsModule {
	private final NewFriendsContract.NewFriendsView mView;
	private final Context mContext;

	public NewFriendsModule(NewFriendsContract.NewFriendsView view, Context context) {
		mView = view;
		mContext = context;
	}

	@Provides
	NewFriendsContract.NewFriendsView provideNewFriendsView() {
		return mView;
	}

	@Provides
	Context provideContext() {
		return mContext;
	}
}
