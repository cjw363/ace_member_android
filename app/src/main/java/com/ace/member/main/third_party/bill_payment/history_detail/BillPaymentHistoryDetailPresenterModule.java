package com.ace.member.main.third_party.bill_payment.history_detail;

import android.content.Context;

import com.ace.member.main.third_party.bill_payment.history.BillPaymentHistoryPresenterModule;

import dagger.Module;
import dagger.Provides;

@Module
public class BillPaymentHistoryDetailPresenterModule {
	private final BillPaymentHistoryDetailContract.view mView;
	private final Context mContext;
	public BillPaymentHistoryDetailPresenterModule(BillPaymentHistoryDetailContract.view view,Context context){
		mView=view;
		mContext=context;
	}

	@Provides
	BillPaymentHistoryDetailContract.view providesView(){
		return mView;
	}
	@Provides
	Context providesContext(){
		return mContext;
	}
}
