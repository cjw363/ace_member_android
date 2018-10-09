package com.ace.member.main.third_party.wsa.detail;

import dagger.Component;

@Component(modules = WsaHistoryDetailPresenterModule.class)
public interface WsaHistoryDetailComponent {
	void inject(WsaHistoryDetailActivity activity);
}
