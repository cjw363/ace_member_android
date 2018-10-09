package com.ace.member.main.friends.search;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class SearchFriendsPresenterModule {
	private final SearchFriendsContract.SearchFriendsView mView;
	private final Context mContext;

	public SearchFriendsPresenterModule(SearchFriendsContract.SearchFriendsView view, Context context) {
		mView = view;
		mContext = context;
	}

	@Provides
	SearchFriendsContract.SearchFriendsView provideSearchFriendsView() {
		return mView;
	}

	@Provides
	Context provideContext() {
		return mContext;
	}
}
