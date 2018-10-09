package com.ace.member.sms_notification;

import android.content.Context;
import android.support.annotation.NonNull;


import com.ace.member.base.BasePresenter;

import javax.inject.Inject;

class SMSNotificationPresenter extends BasePresenter implements SMSNotificationContract.SMSNotificationPresenter {
	@NonNull
	private final SMSNotificationContract.SMSNotificationView mView;

	@Inject
	SMSNotificationPresenter(@NonNull SMSNotificationContract.SMSNotificationView view, Context context) {
		super(context);
		this.mView = view;
	}
}
