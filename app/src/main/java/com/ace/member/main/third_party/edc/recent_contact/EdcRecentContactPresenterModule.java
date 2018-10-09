package com.ace.member.main.third_party.edc.recent_contact;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class EdcRecentContactPresenterModule {

	private final EdcRecentContactContract.View mView;
	private final Context mContext;

	public EdcRecentContactPresenterModule(EdcRecentContactContract.View view, Context context) {
		mView = view;
		mContext = context;
	}

	@Provides
	EdcRecentContactContract.View provideView() {
		return mView;
	}

	@Provides
	Context provideContext() {
		return mContext;
	}
}
