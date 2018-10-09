package com.ace.member.main.me.log;

import dagger.Component;

@Component (modules = LogPresenterModule.class)
public interface LogComponent {
	void inject(LogActivity logActivity);
}

