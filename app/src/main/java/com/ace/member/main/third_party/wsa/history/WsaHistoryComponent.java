package com.ace.member.main.third_party.wsa.history;

import dagger.Component;

@Component(modules = WsaHistoryPresenterModule.class)
public interface WsaHistoryComponent {
	void inject(WsaHistoryActivity activity);
}
