package com.ace.member.main.third_party.wsa.recent_contact;

import dagger.Component;

@Component(modules = WsaRecentContactPresenterModule.class)
public interface WsaRecentContactComponent {
	void inject(WsaRecentContactFragment fragment);
}
