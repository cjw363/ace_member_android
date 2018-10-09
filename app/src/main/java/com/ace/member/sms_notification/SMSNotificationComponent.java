package com.ace.member.sms_notification;

import dagger.Component;

@Component(modules = SMSNotificationPresenterModule.class)
public interface SMSNotificationComponent {
	void inject(SMSNotificationActivity activity);
}

