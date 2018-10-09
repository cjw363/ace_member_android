package com.ace.member.main.third_party.bill_payment.history;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class BillPaymentHistoryPresenterModule {
	private final BillPaymentHistoryContract.view mView;
	private final Context mContext;

	public BillPaymentHistoryPresenterModule(BillPaymentHistoryContract.view view,Context context){
		mView=view;
		mContext=context;
	}

	@Provides
	BillPaymentHistoryContract.view providesBillPaymentHistoryContractView(){
		return  mView;
	}

	@Provides
	Context providesContext(){
		return mContext;
	}
}
