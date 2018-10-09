package com.ace.member.main.detail.deposit_via_agent_detail;

import dagger.Component;

@Component(modules = DepositViaAgentDetailModule.class)
interface DepositViaAgentDetailComponent {
	void inject(DepositViaAgentDetailActivity activity);
}
