package com.ace.member.main.third_party.edc.history;

import dagger.Component;

@Component(modules = EdcHistoryPresenterModule.class)
public interface EdcHistoryComponent {
	void inject(EdcHistoryActivity activity);
}
