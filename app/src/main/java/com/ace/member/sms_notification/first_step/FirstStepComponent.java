package com.ace.member.sms_notification.first_step;

import dagger.Component;

@Component(modules = FirstStepPresenterModule.class)
interface FirstStepComponent {
	void inject(FirstStepFragment fragment);
}

