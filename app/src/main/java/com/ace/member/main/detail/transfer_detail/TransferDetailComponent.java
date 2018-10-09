package com.ace.member.main.detail.transfer_detail;

import dagger.Component;

@Component(modules = TransferDetailModule.class)
interface TransferDetailComponent {
	void inject(TransferDetailActivity activity);
}
