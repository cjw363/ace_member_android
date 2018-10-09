package com.ace.member.main.currency.new_bank;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class NewBankPresenterModule {

	private final NewBankContract.View mView;
	private final Context mContext;

	public NewBankPresenterModule(NewBankContract.View view, Context context) {
		mView = view;
		mContext = context;
	}

	@Provides
	NewBankContract.View newBankContractView() {
		return mView;
	}

	@Provides
	Context providesContext() {
		return mContext;
	}
}
