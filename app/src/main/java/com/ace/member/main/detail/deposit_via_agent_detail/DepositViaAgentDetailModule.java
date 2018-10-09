package com.ace.member.main.detail.deposit_via_agent_detail;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
class DepositViaAgentDetailModule {
	private final DepositViaAgentDetailContract.MemberDepositDetailView mMemberDepositDetailView;
	private final Context mContext;


	DepositViaAgentDetailModule(DepositViaAgentDetailContract.MemberDepositDetailView view, Context context) {
		mMemberDepositDetailView = view;
		mContext = context;
	}

	@Provides
	DepositViaAgentDetailContract.MemberDepositDetailView provideMemberDepositDetailContractView() {
		return mMemberDepositDetailView;
	}

	@Provides
	Context providesContext() {
		return mContext;
	}
}
