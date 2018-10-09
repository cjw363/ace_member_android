package com.ace.member.gesture_unlock;

import dagger.Component;

@Component(modules = UnlockPresenterModule.class)
public interface UnlockComponent {
	void inject(UnlockActivity unlockActivity);
}

