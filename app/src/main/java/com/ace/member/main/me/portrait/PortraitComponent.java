package com.ace.member.main.me.portrait;

import dagger.Component;

@Component(modules = PortraitPresenterModule.class)
public interface PortraitComponent {
	void inject(PortraitActivity activity);
}

