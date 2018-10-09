package com.ace.member.main.detail.withdraw_detail;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class WithdrawDetailPresenterModule {

	private final WithdrawDetailContract.View mView;
	private final Context mContext;

	public WithdrawDetailPresenterModule(WithdrawDetailContract.View view, Context context) {
		mView = view;
		mContext = context;
	}

	@Provides
	WithdrawDetailContract.View provideContractView() {
		return mView;
	}

	@Provides
	Context providesContext() {
		return mContext;
	}
}
