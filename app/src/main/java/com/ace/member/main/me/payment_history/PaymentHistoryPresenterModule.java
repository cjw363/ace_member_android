package com.ace.member.main.me.payment_history;

import android.content.Context;
import dagger.Module;
import dagger.Provides;

@Module
public class PaymentHistoryPresenterModule {
	private PaymentHistoryContract.PaymentHistoryView mLogView;
	private final Context mContext;

	public PaymentHistoryPresenterModule(PaymentHistoryContract.PaymentHistoryView mLogView, Context mContext) {
		this.mLogView = mLogView;
		this.mContext = mContext;
	}

	@Provides
	PaymentHistoryContract.PaymentHistoryView providePaymentHistoryContractView() { return mLogView; }

	@Provides
	Context providesContext() {return mContext; }
}
