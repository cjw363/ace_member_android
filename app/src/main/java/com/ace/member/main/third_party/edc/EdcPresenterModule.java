package com.ace.member.main.third_party.edc;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class EdcPresenterModule {

	private final EdcContract.View mView;
	private final Context mContext;

	public EdcPresenterModule(EdcContract.View view, Context context) {
		mView = view;
		mContext = context;
	}

	@Provides
	EdcContract.View provideView() {
		return mView;
	}

	@Provides
	Context provideContext() {
		return mContext;
	}
}
