package com.ace.member.main.me.portrait;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class PortraitPresenterModule {

	private final PortraitContract.View mView;
	private final Context mContext;

	public PortraitPresenterModule(PortraitContract.View view, Context context) {
		mView = view;
		mContext = context;
	}

	@Provides
	PortraitContract.View provideContractView() {
		return mView;
	}

	@Provides
	Context providesContext() {
		return mContext;
	}
}
