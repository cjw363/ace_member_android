package com.ace.member.main.friends.chat_group.build_group;

import dagger.Component;

@Component(modules = BuildGroupPresenterModule.class)
public interface BuildGroupComponent {
	void inject(BuildGroupActivity buildGroupActivity);
}
