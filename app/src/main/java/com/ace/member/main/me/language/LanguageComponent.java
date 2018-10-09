package com.ace.member.main.me.language;

import dagger.Component;

@Component(modules = LanguagePresenterModule.class)
public interface LanguageComponent {
	void inject( LanguageActivity activity);
}
