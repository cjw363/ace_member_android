package com.ace.member.main.third_party.edc.detail;

import dagger.Component;

@Component(modules = EdcHistoryDetailPresenterModule.class)
public interface EdcHistoryDetailComponent {
	void inject(EdcHistoryDetailActivity activity);
}
