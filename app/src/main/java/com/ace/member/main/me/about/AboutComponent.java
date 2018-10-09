package com.ace.member.main.me.about;

import dagger.Component;

@Component(modules = AboutPresenterModule.class)
public interface AboutComponent {
	void inject(AboutActivity aboutActivity);
}
