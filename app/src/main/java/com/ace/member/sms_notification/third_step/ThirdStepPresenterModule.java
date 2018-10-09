package com.ace.member.sms_notification.third_step;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class ThirdStepPresenterModule {

	private final ThirdStepContract.ThirdStepView mView;
	private final Context mContext;

	public ThirdStepPresenterModule(ThirdStepContract.ThirdStepView view, Context context) {
		mView = view;
		mContext = context;
	}

	@Provides
	ThirdStepContract.ThirdStepView provideThirdStepContractView() {
		return mView;
	}

	@Provides
	Context providesContext() {
		return mContext;
	}
}
