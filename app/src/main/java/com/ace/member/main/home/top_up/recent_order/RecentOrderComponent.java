package com.ace.member.main.home.top_up.recent_order;

import dagger.Component;

@Component(modules = RecentOrderPresenterModule.class)
public interface RecentOrderComponent {
	void inject(RecentOrderActivity activity);
}

