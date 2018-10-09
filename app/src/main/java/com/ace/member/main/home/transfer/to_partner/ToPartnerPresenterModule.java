package com.ace.member.main.home.transfer.to_partner;


import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class ToPartnerPresenterModule {

	private final ToPartnerContract.View mView;
	private final Context mContext;

	public ToPartnerPresenterModule(ToPartnerContract.View mView, Context mContext) {this.mView = mView;
		this.mContext = mContext;
	}

	@Provides
	ToPartnerContract.View providesToPartnerContractView(){return mView;}

	@Provides
	Context providesContext(){return mContext;}

}
