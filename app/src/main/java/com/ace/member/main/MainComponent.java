package com.ace.member.main;

import dagger.Component;

@Component(modules = MainPresenterModule.class)
public interface MainComponent {
	void inject(MainActivity mainActivity);
}

