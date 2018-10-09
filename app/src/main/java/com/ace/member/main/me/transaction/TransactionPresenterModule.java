package com.ace.member.main.me.transaction;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class TransactionPresenterModule {

	private final TransactionContract.TransactionView mTransactionView;
	private final Context mContext;

	public TransactionPresenterModule(TransactionContract.TransactionView mTransactionView, Context mContext) {
		this.mTransactionView = mTransactionView;
		this.mContext = mContext;
	}

	@Provides
	TransactionContract.TransactionView providesTransactionContractView() {return mTransactionView;}

	@Provides
	Context providesContext() {return mContext;}
}
