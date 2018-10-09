package com.ace.member.main.third_party.wsa.detail;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class WsaHistoryDetailPresenterModule {

	private final WsaHistoryDetailContract.View mView;
	private final Context mContext;

	public WsaHistoryDetailPresenterModule(WsaHistoryDetailContract.View view, Context context) {
		mView = view;
		mContext = context;
	}

	@Provides
	WsaHistoryDetailContract.View provideView() {
		return mView;
	}

	@Provides
	Context provideContext() {
		return mContext;
	}
}
