package com.ace.member.main.third_party.wsa.history;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class WsaHistoryPresenterModule {

	private final WsaHistoryContract.View mView;
	private final Context mContext;

	public WsaHistoryPresenterModule(WsaHistoryContract.View view, Context context) {
		mView = view;
		mContext = context;
	}

	@Provides
	WsaHistoryContract.View provideView() {
		return mView;
	}

	@Provides
	Context provideContext() {
		return mContext;
	}
}
