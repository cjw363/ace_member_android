package com.ace.member.main.me.service_point;

import dagger.Component;

@Component(modules = ServicePointPresenterModule.class)
interface ServicePointComponent {
	void inject(ServicePointActivity activity);
}

