package com.ace.member.main.friends.chat.chat_info.transfer_history;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class FriendTransferHistoryPresenterModule {

	private final FriendTransferHistoryContract.TransferRecentView mTransferRecentView;
	private final Context mContext;

	public FriendTransferHistoryPresenterModule(FriendTransferHistoryContract.TransferRecentView mTransferRecentView, Context mContext) {
		this.mTransferRecentView = mTransferRecentView;
		this.mContext = mContext;
	}

	@Provides
	FriendTransferHistoryContract.TransferRecentView provideTransferRecentContractView() {return mTransferRecentView;}

	@Provides
	Context provideContext() {return mContext;}
}
