package com.ace.member.main.home.transfer.recent;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class TransferRecentPresenterModule {

	private final TransferRecentContract.TransferRecentView mTransferRecentView;
	private final Context mContext;

	public TransferRecentPresenterModule(TransferRecentContract.TransferRecentView mTransferRecentView, Context mContext) {this.mTransferRecentView = mTransferRecentView;
		this.mContext = mContext;
	}

	@Provides
	TransferRecentContract.TransferRecentView provideTransferRecentContractView(){return mTransferRecentView;}

	@Provides
	Context provideContext(){return mContext;}
}
