package com.ace.member.main.home.top_up.order_detail;

import dagger.Component;

@Component(modules = TopUpOrderDetailPresenterModule.class)
public interface TopUpOrderDetailComponent {
	void inject(TopUpOrderDetailActivity activity);
}

