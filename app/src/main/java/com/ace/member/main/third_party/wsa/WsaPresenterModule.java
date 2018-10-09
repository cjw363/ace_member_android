package com.ace.member.main.third_party.wsa;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class WsaPresenterModule {

	private final WsaContract.View mView;
	private final Context mContext;

	public WsaPresenterModule(WsaContract.View view, Context context) {
		mView = view;
		mContext = context;
	}

	@Provides
	WsaContract.View provideView() {
		return mView;
	}

	@Provides
	Context provideContext() {
		return mContext;
	}
}
