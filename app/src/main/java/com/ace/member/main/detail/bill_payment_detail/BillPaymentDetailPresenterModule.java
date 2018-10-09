package com.ace.member.main.detail.bill_payment_detail;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
class BillPaymentDetailPresenterModule {
	private final BillPaymentDetailContract.view mView;
	private final Context mContext;
	BillPaymentDetailPresenterModule(BillPaymentDetailContract.view view, Context context){
		mView=view;
		mContext=context;
	}

	@Provides
	BillPaymentDetailContract.view providesView(){
		return mView;
	}
	@Provides
	Context providesContext(){
		return mContext;
	}
}
