package com.ace.member.main.home.money.receive_money;


import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class ReceiveMoneyPresenterModule {

	private final ReceiveMoneyContract.View mView;
	private final Context mContext;

	public ReceiveMoneyPresenterModule(ReceiveMoneyContract.View view, Context context) {
		this.mView = view;
		this.mContext = context;
	}

	@Provides
	ReceiveMoneyContract.View provideView() {
		return mView;
	}

	@Provides
	Context provideContext() {
		return mContext;
	}
}
