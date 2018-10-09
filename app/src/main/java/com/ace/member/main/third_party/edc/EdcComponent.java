package com.ace.member.main.third_party.edc;

import dagger.Component;

@Component(modules = EdcPresenterModule.class)
public interface EdcComponent {
	void inject(EdcActivity activity);
}
