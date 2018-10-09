package com.ace.member.main.third_party.wsa.recent_contact;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class WsaRecentContactPresenterModule {

	private final WsaRecentContactContract.View mView;
	private final Context mContext;

	public WsaRecentContactPresenterModule(WsaRecentContactContract.View view, Context context) {
		mView = view;
		mContext = context;
	}

	@Provides
	WsaRecentContactContract.View provideView() {
		return mView;
	}

	@Provides
	Context provideContext() {
		return mContext;
	}
}
