package com.ace.member.main.home.top_up.phone_company;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class PhoneCompanyPresenterModule {

	private final PhoneCompanyContract.View mView;
	private final Context mContext;

	public PhoneCompanyPresenterModule(PhoneCompanyContract.View view, Context context) {
		mView = view;
		mContext = context;
	}

	@Provides
	PhoneCompanyContract.View providePhoneCompanyContractView() {
		return mView;
	}

	@Provides
	Context providesContext() {
		return mContext;
	}
}
