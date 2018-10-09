package com.ace.member.sms_notification.second_step;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class SecondStepPresenterModule {

	private final SecondStepContract.SecondStepView mView;
	private final Context mContext;

	public SecondStepPresenterModule(SecondStepContract.SecondStepView view, Context context) {
		mView = view;
		mContext = context;
	}

	@Provides
	SecondStepContract.SecondStepView provideSecondStepContractView() {
		return mView;
	}

	@Provides
	Context providesContext() {
		return mContext;
	}
}
