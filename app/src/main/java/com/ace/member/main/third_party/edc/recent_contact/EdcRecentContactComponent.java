package com.ace.member.main.third_party.edc.recent_contact;

import dagger.Component;

@Component(modules = EdcRecentContactPresenterModule.class)
public interface EdcRecentContactComponent {
	void inject(EdcRecentContactFragment fragment);
}
