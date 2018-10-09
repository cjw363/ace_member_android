package com.ace.member.main.friends.search;

import dagger.Component;

@Component(modules = SearchFriendsPresenterModule.class)
public interface SearchFriendsComponent {
	void inject(SearchFriendsFragment searchFriendsFragment);
}
