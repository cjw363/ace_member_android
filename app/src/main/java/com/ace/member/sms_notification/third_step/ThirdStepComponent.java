package com.ace.member.sms_notification.third_step;

import dagger.Component;

@Component(modules = ThirdStepPresenterModule.class)
interface ThirdStepComponent {
	void inject(ThirdStepFragment fragment);
}

