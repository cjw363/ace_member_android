package com.ace.member.main.home.receive_to_acct;

import android.content.Context;
import dagger.Module;
import dagger.Provides;

@Module
public class ReceiveToAcctPresenterModule {

	private final ReceiveToAcctContract.View mView;
	private final Context mContext;

	public ReceiveToAcctPresenterModule(ReceiveToAcctContract.View view, Context context) {
		mView = view;
		mContext = context;
	}

	@Provides
	Context providesContext() {
		return mContext;
	}

	@Provides
	ReceiveToAcctContract.View provideReceiveToAcctContractView() {
		return mView;
	}
}
