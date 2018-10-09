package com.ace.member.setting_gesture;

import dagger.Component;

@Component(modules = SettingGesturePresenterModule.class)
public interface SettingGestureComponent {
	void inject(SettingGestureActivity activity);
}

