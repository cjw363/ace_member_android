package com.ace.member.main.home.transfer.recent_detail;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class TransferRecentDetailPresenterModule {

	private final TransferRecentDetailContract.TransferRecentDetailView mTransferRecentDetailView;
	private final Context mContext;

	public TransferRecentDetailPresenterModule(TransferRecentDetailContract.TransferRecentDetailView mTransferRecentDetailView, Context mContext) {this.mTransferRecentDetailView = mTransferRecentDetailView;
		this.mContext = mContext;
	}

	@Provides
	TransferRecentDetailContract.TransferRecentDetailView provideTransferRecentDetailContractView(){return mTransferRecentDetailView;}

	@Provides
	Context provideContext(){return mContext;}
}
