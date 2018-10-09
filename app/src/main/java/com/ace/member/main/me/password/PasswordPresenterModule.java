package com.ace.member.main.me.password;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class PasswordPresenterModule {
	private final PasswordContract.PasswordView mPasswordView;
	private final Context mContext;

	public PasswordPresenterModule(PasswordContract.PasswordView mPasswordView, Context mContext) {this.mPasswordView = mPasswordView;
		this.mContext = mContext;
	}

	@Provides
	PasswordContract.PasswordView providePasswordContractView(){
		return mPasswordView;
	}

	@Provides
	Context providesContext(){
		return mContext;
	}
}
