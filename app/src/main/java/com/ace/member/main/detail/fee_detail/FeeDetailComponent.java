package com.ace.member.main.detail.fee_detail;

import dagger.Component;

@Component(modules = FeeDetailModule.class)
interface FeeDetailComponent {
	void inject(FeeDetailActivity activity);
}
