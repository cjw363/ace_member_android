package com.ace.member.main.me.password.trading_password;

import android.content.Context;

import javax.inject.Inject;

import dagger.Module;
import dagger.Provides;

@Module
public class TradingPasswordPresenterModule {

	private final TradingPasswordContract.TradingPasswordView mTradingPassword;
	private final Context mContext;

	@Inject
	public TradingPasswordPresenterModule(TradingPasswordContract.TradingPasswordView mTradingPassword, Context mContext) {this.mTradingPassword = mTradingPassword;
		this.mContext = mContext;
	}

	@Provides
	TradingPasswordContract.TradingPasswordView providesTradingPasswordContractView(){
		return mTradingPassword;
	}

	@Provides
	Context providesContract(){
		return mContext;
	}

}
