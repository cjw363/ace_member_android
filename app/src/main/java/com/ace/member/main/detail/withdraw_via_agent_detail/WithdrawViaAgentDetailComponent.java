package com.ace.member.main.detail.withdraw_via_agent_detail;

import dagger.Component;

@Component(modules = WithdrawViaAgentDetailModule.class)
interface WithdrawViaAgentDetailComponent {
	void inject(WithdrawViaAgentDetailActivity activity);
}
