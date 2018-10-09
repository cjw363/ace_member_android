package com.ace.member.main.me.system_update;


import dagger.Component;

@Component(modules = SystemUpdatePresenterModule.class)
public interface SystemUpdateComponent {
	void inject(SystemUpdateActivity activity);
}
