package com.ace.member.main.input_password;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class InputPasswordPresenterModule {

	private final InputPasswordContract.View mView;
	private final Context mContext;

	public InputPasswordPresenterModule(InputPasswordContract.View view, Context context) {
		mView = view;
		mContext = context;
	}

	@Provides
	InputPasswordContract.View provideInputPasswordContractView() {
		return mView;
	}

	@Provides
	Context providesContext() {
		return mContext;
	}
}
