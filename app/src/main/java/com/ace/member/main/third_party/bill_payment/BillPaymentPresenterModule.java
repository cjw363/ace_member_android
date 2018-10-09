package com.ace.member.main.third_party.bill_payment;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class BillPaymentPresenterModule {

	private final BillPaymentContract.view mView;
	private final Context mContext;

	public BillPaymentPresenterModule(BillPaymentContract.view view,Context context){
		mView=view;
		mContext=context;
	}

	@Provides
	BillPaymentContract.view providesBillPaymentContractView(){
		return mView;
	}
	@Provides
	Context providesContext(){
		return mContext;
	}
}
