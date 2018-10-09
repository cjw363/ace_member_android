package com.ace.member.gesture_lock_setup;

import dagger.Component;

@Component(modules = LockSetupPresenterModule.class)
public interface LockSetupComponent {

	void inject(LockSetupActivity lockSetupActivity);
}
