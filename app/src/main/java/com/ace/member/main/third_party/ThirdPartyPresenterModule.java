package com.ace.member.main.third_party;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class ThirdPartyPresenterModule {

	private final ThirdPartyContract.View mView;
	private final Context mContext;

	public ThirdPartyPresenterModule(ThirdPartyContract.View view, Context context) {
		mView = view;
		mContext = context;
	}

	@Provides
	ThirdPartyContract.View provideView() {
		return mView;
	}

	@Provides
	Context provideContext() {
		return mContext;
	}
}
