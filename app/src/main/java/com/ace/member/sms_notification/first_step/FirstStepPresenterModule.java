package com.ace.member.sms_notification.first_step;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class FirstStepPresenterModule {

	private final FirstStepContract.FirstStepView mView;
	private final Context mContext;

	public FirstStepPresenterModule(FirstStepContract.FirstStepView view, Context context) {
		mView = view;
		mContext = context;
	}

	@Provides
	FirstStepContract.FirstStepView provideFirstStepContractView() {
		return mView;
	}

	@Provides
	Context providesContext() {
		return mContext;
	}
}
