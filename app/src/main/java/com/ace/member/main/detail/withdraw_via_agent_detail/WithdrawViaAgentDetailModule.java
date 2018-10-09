package com.ace.member.main.detail.withdraw_via_agent_detail;


import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
class WithdrawViaAgentDetailModule {

	private final WithdrawViaAgentDetailContract.WithdrawViaAgentDetailView mWithdrawViaAgentDetailView;
	private final Context mContext;

	WithdrawViaAgentDetailModule(WithdrawViaAgentDetailContract.WithdrawViaAgentDetailView withdrawViaAgentDetailView, Context context) {
		mWithdrawViaAgentDetailView = withdrawViaAgentDetailView;
		mContext = context;
	}

	@Provides
	WithdrawViaAgentDetailContract.WithdrawViaAgentDetailView providesWithdrawViaAgentDetailView() {
		return mWithdrawViaAgentDetailView;
	}

	@Provides
	Context providesContext() {
		return mContext;
	}
}
