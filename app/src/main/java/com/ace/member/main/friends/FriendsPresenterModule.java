package com.ace.member.main.friends;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class FriendsPresenterModule {
	private final FriendsContract.FriendsView mView;
	private final Context mContext;

	public FriendsPresenterModule(FriendsContract.FriendsView view, Context context) {
		mView = view;
		mContext = context;
	}

	@Provides
	FriendsContract.FriendsView provideFriendsView() {
		return mView;
	}

	@Provides
	Context provideContext() {
		return mContext;
	}
}
