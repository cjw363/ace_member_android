package com.ace.member.main.me.system_update;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class SystemUpdatePresenterModule {

	private final SystemUpdateContract.View mView;
	private final Context mContext;

	public SystemUpdatePresenterModule(SystemUpdateContract.View view, Context context) {
		mView = view;
		mContext = context;
	}

	@Provides
	SystemUpdateContract.View provideSystemUpdateContractView() {
		return mView;
	}

	@Provides
	Context providesContext() {
		return mContext;
	}
}
