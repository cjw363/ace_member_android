package com.ace.member.main.friends.chat.friend_profile;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class FriendProfileModule {
	private final FriendProfileContract.FriendProfileView mView;
	private final Context mContext;

	public FriendProfileModule(FriendProfileContract.FriendProfileView view, Context context) {
		mView = view;
		mContext = context;
	}

	@Provides
	FriendProfileContract.FriendProfileView provideFriendProfileView() {
		return mView;
	}

	@Provides
	Context provideContext() {
		return mContext;
	}
}
