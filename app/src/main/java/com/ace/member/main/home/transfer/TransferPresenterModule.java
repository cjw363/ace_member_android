package com.ace.member.main.home.transfer;


import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class TransferPresenterModule {

	private final TransferContract.TransferView mTransferView;
	private final Context mContext;

	public TransferPresenterModule(TransferContract.TransferView mTransferView, Context mContext) {this.mTransferView = mTransferView;
		this.mContext = mContext;
	}

	@Provides
	TransferContract.TransferView provideTransferContractView(){return mTransferView;}

	@Provides
	Context provideContext(){return mContext;}
}
