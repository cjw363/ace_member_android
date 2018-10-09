package com.ace.member.sms_notification.second_step;

import dagger.Component;

@Component(modules = SecondStepPresenterModule.class)
interface SecondStepComponent {
	void inject(SecondStepFragment fragment);
}

