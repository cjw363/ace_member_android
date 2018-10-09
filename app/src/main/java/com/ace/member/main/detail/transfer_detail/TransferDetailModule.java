package com.ace.member.main.detail.transfer_detail;


import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
class TransferDetailModule {

	private final TransferDetailContract.TransferDetailView mTransferDetailView;
	private final Context mContext;

	TransferDetailModule(TransferDetailContract.TransferDetailView transferDetailView, Context context) {
		mTransferDetailView = transferDetailView;
		mContext = context;
	}

	@Provides
	TransferDetailContract.TransferDetailView providesTransferDetailView() {
		return mTransferDetailView;
	}

	@Provides
	Context providesContext() {
		return mContext;
	}
}
