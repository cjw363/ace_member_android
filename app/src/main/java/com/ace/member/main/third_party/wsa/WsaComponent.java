package com.ace.member.main.third_party.wsa;

import dagger.Component;

@Component(modules = WsaPresenterModule.class)
public interface WsaComponent {
	void inject(WsaActivity activity);
}
