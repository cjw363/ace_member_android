package com.ace.member.sms_notification;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class SMSNotificationPresenterModule {

	private final SMSNotificationContract.SMSNotificationView mView;
	private final Context mContext;

	public SMSNotificationPresenterModule(SMSNotificationContract.SMSNotificationView view, Context context) {
		mView = view;
		mContext = context;
	}

	@Provides
	SMSNotificationContract.SMSNotificationView provideRegisterContractView() {
		return mView;
	}

	@Provides
	Context providesContext() {
		return mContext;
	}
}
