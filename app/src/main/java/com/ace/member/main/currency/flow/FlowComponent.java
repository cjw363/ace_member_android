package com.ace.member.main.currency.flow;

import dagger.Component;

@Component(modules = FlowPresenterModule.class)
public interface FlowComponent {
	void inject(FlowActivity activity);
}

