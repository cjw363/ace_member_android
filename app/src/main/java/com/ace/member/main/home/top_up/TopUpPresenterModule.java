package com.ace.member.main.home.top_up;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class TopUpPresenterModule {

	private final TopUpContract.View mView;
	private final Context mContext;

	public TopUpPresenterModule(TopUpContract.View view, Context context) {
		mView = view;
		mContext = context;
	}

	@Provides
	TopUpContract.View provideTopUpContractView() {
		return mView;
	}

	@Provides
	Context providesContext() {
		return mContext;
	}
}
