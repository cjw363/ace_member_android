package com.ace.member.main.home.receive_to_acct.detail;

import android.content.Context;
import dagger.Module;
import dagger.Provides;

@Module
public class R2ADetailPresenterModule {

	private final R2ADetailContract.R2ADetailView mTransferRecentDetailView;
	private final Context mContext;

	public R2ADetailPresenterModule(R2ADetailContract.R2ADetailView mTransferRecentDetailView, Context mContext) {this.mTransferRecentDetailView = mTransferRecentDetailView;
		this.mContext = mContext;
	}

	@Provides
	R2ADetailContract.R2ADetailView provideTransferRecentDetailContractView(){return mTransferRecentDetailView;}

	@Provides
	Context provideContext(){return mContext;}
}
