package com.ace.member.main.home;

import com.ace.member.main.MainActivity;

import dagger.Component;

@Component(modules = HomePresenterModule.class)
public interface HomeComponent {
	void inject(HomeFragment fragment);
}

