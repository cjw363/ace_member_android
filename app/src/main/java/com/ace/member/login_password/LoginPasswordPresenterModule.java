package com.ace.member.login_password;

import android.content.Context;

import javax.inject.Inject;

import dagger.Module;
import dagger.Provides;

@Module
public class LoginPasswordPresenterModule {

	private final LoginPasswordContract.ResetPasswordView mResetPasswordView;
	private final Context mContext;

	@Inject
	LoginPasswordPresenterModule(LoginPasswordContract.ResetPasswordView view, Context context) {
		mResetPasswordView = view;
		mContext = context;
	}

	@Provides
	LoginPasswordContract.ResetPasswordView provideResetPasswordContractView() {
		return mResetPasswordView;
	}

	@Provides
	Context providesContext() {
		return mContext;
	}
}
